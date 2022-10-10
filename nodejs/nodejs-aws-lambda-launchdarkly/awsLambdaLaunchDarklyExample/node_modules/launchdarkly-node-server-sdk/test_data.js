const dataKind = require('./versioned_data_kind');
const { promisify } = require('util');

function TestData() {
  const existingFlagBuilders = {};
  const currentFlags = {};
  const currentSegments = {};
  const dataSourceImpls = [];

  function makeInitData() {
    return {
      [dataKind.features.namespace]: { ...currentFlags },
      [dataKind.segments.namespace]: { ...currentSegments },
    };
  }

  const td = config => {
    const featureStore = config.featureStore;

    const tds = {
      start: start,
      stop: stop,
      initialized: () => true,
      close: stop,
      upsert: upsert,
    };

    function start(cb = () => {}) {
      featureStore.init(makeInitData(), cb);
    }

    function stop() {
      dataSourceImpls.splice(dataSourceImpls.indexOf(this));
    }

    function upsert(kind, value, cb = () => {}) {
      featureStore.upsert(kind, value, cb);
    }

    dataSourceImpls.push(tds);

    return tds;
  };

  td.flag = flagName => {
    if (existingFlagBuilders[flagName]) {
      return existingFlagBuilders[flagName].copy();
    } else {
      return new TestDataFlagBuilder(flagName).booleanFlag();
    }
  };

  td.update = flagBuilder => {
    const oldItem = currentFlags[flagBuilder._key];
    const oldVersion = oldItem ? oldItem.version : 0;
    const newFlag = flagBuilder.build(oldVersion + 1);
    currentFlags[flagBuilder._key] = newFlag;
    existingFlagBuilders[flagBuilder._key] = flagBuilder.copy();

    return Promise.all(dataSourceImpls.map(impl => promisify(impl.upsert)(dataKind.features, newFlag)));
  };

  function usePreconfiguredItem(kind, item, current) {
    const oldItem = current[item.key];
    const newItem = { ...item, version: oldItem ? oldItem.version + 1 : item.version };
    /* eslint-disable no-param-reassign */
    current[item.key] = newItem;
    /* eslint-enable no-param-reassign */

    return Promise.all(dataSourceImpls.map(impl => promisify(impl.upsert)(kind, newItem)));
  }

  td.usePreconfiguredFlag = flag => usePreconfiguredItem(dataKind.features, flag, currentFlags);

  td.usePreconfiguredSegment = segment => usePreconfiguredItem(dataKind.segments, segment, currentSegments);

  return td;
}

function TestDataFlagBuilder(flagName) {
  this._key = flagName;
  this._on = true;
  this._variations = [];
}

const TRUE_VARIATION_INDEX = 0;
const FALSE_VARIATION_INDEX = 1;

function variationForBoolean(aBool) {
  return aBool ? TRUE_VARIATION_INDEX : FALSE_VARIATION_INDEX;
}

TestDataFlagBuilder.prototype.copy = function () {
  const to = new TestDataFlagBuilder(this._key);
  to._variations = [...this._variations];
  to._offVariation = this._offVariation;
  to._on = this._on;
  to._fallthroughVariation = this._fallthroughVariation;
  to._targets = !this._targets ? null : new Map(this._targets);
  to._rules = !this._rules ? null : JSON.parse(JSON.stringify(this._rules));
  return to;
};

TestDataFlagBuilder.prototype.isBooleanFlag = function () {
  return (
    this._variations.length === 2 &&
    this._variations[TRUE_VARIATION_INDEX] === true &&
    this._variations[FALSE_VARIATION_INDEX] === false
  );
};

TestDataFlagBuilder.prototype.booleanFlag = function () {
  if (this.isBooleanFlag()) {
    return this;
  } else {
    return this.variations(true, false).fallthroughVariation(TRUE_VARIATION_INDEX).offVariation(FALSE_VARIATION_INDEX);
  }
};

TestDataFlagBuilder.prototype.on = function (aBool) {
  this._on = aBool;
  return this;
};

TestDataFlagBuilder.prototype.fallthroughVariation = function (variation) {
  if (typeof variation === 'boolean') {
    this.booleanFlag().fallthroughVariation(variationForBoolean(variation));
  } else {
    this._fallthroughVariation = variation;
  }
  return this;
};

TestDataFlagBuilder.prototype.offVariation = function (variation) {
  if (typeof variation === 'boolean') {
    this.booleanFlag().offVariation(variationForBoolean(variation));
  } else {
    this._offVariation = variation;
  }
  return this;
};

TestDataFlagBuilder.prototype.variations = function (...values) {
  this._variations = [...values];
  return this;
};

TestDataFlagBuilder.prototype.clearUserTargets = function () {
  this._targets = null;
  return this;
};

TestDataFlagBuilder.prototype.clearRules = function () {
  this._rules = null;
  return this;
};

TestDataFlagBuilder.prototype.variationForAllUsers = function (variation) {
  return this.on(true).clearRules().clearUserTargets().fallthroughVariation(variation);
};

TestDataFlagBuilder.prototype.valueForAllUsers = function (value) {
  return this.variations(value).variationForAllUsers(0);
};

TestDataFlagBuilder.prototype.variationForUser = function (userKey, variation) {
  if (typeof variation === 'boolean') {
    return this.booleanFlag().variationForUser(userKey, variationForBoolean(variation));
  }

  if (!this._targets) {
    this._targets = new Map();
  }

  this._variations.forEach((_, i) => {
    if (i === variation) {
      //If there is no set at the current variation then set it to the empty array
      let targetForVariation;
      if (this._targets.has(i)) {
        targetForVariation = this._targets.get(i);
      } else {
        targetForVariation = [];
      }
      // Add user to current variation set if they arent already there
      if (targetForVariation.indexOf(userKey) === -1) {
        targetForVariation.push(userKey);
      }

      this._targets.set(i, targetForVariation);
    } else {
      // remove user from other variation set if necessary
      if (this._targets.has(i)) {
        const targetForVariation = this._targets.get(i);
        const userIndex = targetForVariation.indexOf(userKey);
        if (userIndex !== -1) {
          this._targets.set(i, targetForVariation.splice(userIndex));
        }
      }
    }
  });

  return this;
};

TestDataFlagBuilder.prototype.addRule = function (flagRuleBuilder) {
  if (!this._rules) {
    this._rules = [];
  }
  this._rules.push(flagRuleBuilder);
};

TestDataFlagBuilder.prototype.ifMatch = function (attribute, ...values) {
  const flagRuleBuilder = new TestDataRuleBuilder(this);
  return flagRuleBuilder.andMatch(attribute, ...values);
};

TestDataFlagBuilder.prototype.ifNotMatch = function (attribute, ...values) {
  const flagRuleBuilder = new TestDataRuleBuilder(this);
  return flagRuleBuilder.andNotMatch(attribute, ...values);
};

TestDataFlagBuilder.prototype.build = function (version) {
  const baseFlagObject = {
    key: this._key,
    version: version,
    on: this._on,
    offVariation: this._offVariation,
    fallthrough: {
      variation: this._fallthroughVariation,
    },
    variations: [...this._variations],
  };

  if (this._targets) {
    const targets = [];
    for (const [variationIndex, userKeys] of this._targets) {
      targets.push({
        variation: variationIndex,
        values: userKeys,
      });
    }
    baseFlagObject.targets = targets;
  }

  if (this._rules) {
    baseFlagObject.rules = this._rules.map((rule, i) => rule.build(i));
  }

  return baseFlagObject;
};

/* TestDataRuleBuilder */
function TestDataRuleBuilder(flagBuilder) {
  this._flagBuilder = flagBuilder;
  this._clauses = [];
  this._variation = null;
}

TestDataRuleBuilder.prototype.andMatch = function (attribute, ...values) {
  this._clauses.push({
    attribute: attribute,
    operator: 'in',
    values: values,
    negate: false,
  });
  return this;
};

TestDataRuleBuilder.prototype.andNotMatch = function (attribute, ...values) {
  this._clauses.push({
    attribute: attribute,
    operator: 'in',
    values: values,
    negate: true,
  });
  return this;
};

TestDataRuleBuilder.prototype.thenReturn = function (variation) {
  if (typeof variation === 'boolean') {
    this._flagBuilder.booleanFlag();
    return this.thenReturn(variationForBoolean(variation));
  }

  this._variation = variation;
  this._flagBuilder.addRule(this);
  return this._flagBuilder;
};

TestDataRuleBuilder.prototype.build = function (id) {
  return {
    id: 'rule' + id,
    variation: this._variation,
    clauses: this._clauses,
  };
};

module.exports = TestData;
