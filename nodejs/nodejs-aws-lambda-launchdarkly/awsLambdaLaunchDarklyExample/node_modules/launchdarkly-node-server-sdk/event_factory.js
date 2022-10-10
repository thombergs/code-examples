function isExperiment(flag, reason) {
  if (reason) {
    // If the reason says we're in an experiment, we are. Otherwise, apply
    // the legacy rule exclusion logic.
    if (reason.inExperiment) {
      return true;
    }
    switch (reason.kind) {
      case 'RULE_MATCH': {
        const index = reason.ruleIndex;
        if (index !== undefined) {
          const rules = flag.rules || [];
          return index >= 0 && index < rules.length && !!rules[index].trackEvents;
        }
        break;
      }
      case 'FALLTHROUGH':
        return !!flag.trackEventsFallthrough;
    }
  }
  return false;
}

function EventFactory(withReasons) {
  const ef = {};

  ef.newEvalEvent = (flag, user, detail, defaultVal, prereqOfFlag) => {
    const addExperimentData = isExperiment(flag, detail.reason);
    const e = {
      kind: 'feature',
      creationDate: new Date().getTime(),
      key: flag.key,
      user: user,
      value: detail.value,
      variation: detail.variationIndex,
      default: defaultVal,
      version: flag.version,
    };
    // the following properties are handled separately so we don't waste bandwidth on unused keys
    if (addExperimentData || flag.trackEvents) {
      e.trackEvents = true;
    }
    if (flag.debugEventsUntilDate) {
      e.debugEventsUntilDate = flag.debugEventsUntilDate;
    }
    if (prereqOfFlag) {
      e.prereqOf = prereqOfFlag.key;
    }
    if (addExperimentData || withReasons) {
      e.reason = detail.reason;
    }
    return e;
  };

  ef.newDefaultEvent = (flag, user, detail) => {
    const e = {
      kind: 'feature',
      creationDate: new Date().getTime(),
      key: flag.key,
      user: user,
      value: detail.value,
      default: detail.value,
      version: flag.version,
    };
    // the following properties are handled separately so we don't waste bandwidth on unused keys
    if (flag.trackEvents) {
      e.trackEvents = true;
    }
    if (flag.debugEventsUntilDate) {
      e.debugEventsUntilDate = flag.debugEventsUntilDate;
    }
    if (withReasons) {
      e.reason = detail.reason;
    }
    return e;
  };

  ef.newUnknownFlagEvent = (key, user, detail) => {
    const e = {
      kind: 'feature',
      creationDate: new Date().getTime(),
      key: key,
      user: user,
      value: detail.value,
      default: detail.value,
    };
    if (withReasons) {
      e.reason = detail.reason;
    }
    return e;
  };

  ef.newIdentifyEvent = user => ({
    kind: 'identify',
    creationDate: new Date().getTime(),
    key: user.key,
    user: user,
  });

  ef.newCustomEvent = (eventName, user, data, metricValue) => {
    const e = {
      kind: 'custom',
      creationDate: new Date().getTime(),
      key: eventName,
      user: user,
    };
    if (data !== null && data !== undefined) {
      e.data = data;
    }
    if (metricValue !== null && metricValue !== undefined) {
      e.metricValue = metricValue;
    }
    return e;
  };

  ef.newAliasEvent = (user, previousUser) => {
    const userContextKind = u => (u.anonymous ? 'anonymousUser' : 'user');
    return {
      kind: 'alias',
      key: user.key,
      contextKind: userContextKind(user),
      previousKey: previousUser.key,
      previousContextKind: userContextKind(previousUser),
      creationDate: new Date().getTime(),
    };
  };

  return ef;
}

module.exports = {
  EventFactory,
  isExperiment,
};
