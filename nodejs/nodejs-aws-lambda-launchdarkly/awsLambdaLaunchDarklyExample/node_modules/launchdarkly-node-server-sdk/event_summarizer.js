function EventSummarizer() {
  const es = {};

  let startDate = 0,
    endDate = 0,
    counters = {};

  es.summarizeEvent = event => {
    if (event.kind === 'feature') {
      const counterKey =
        event.key +
        ':' +
        (event.variation !== null && event.variation !== undefined ? event.variation : '') +
        ':' +
        (event.version !== null && event.version !== undefined ? event.version : '');
      const counterVal = counters[counterKey];
      if (counterVal) {
        counterVal.count = counterVal.count + 1;
      } else {
        counters[counterKey] = {
          count: 1,
          key: event.key,
          version: event.version,
          variation: event.variation,
          value: event.value,
          default: event.default,
        };
      }
      if (startDate === 0 || event.creationDate < startDate) {
        startDate = event.creationDate;
      }
      if (event.creationDate > endDate) {
        endDate = event.creationDate;
      }
    }
  };

  es.getSummary = () => {
    const flagsOut = {};
    for (const c of Object.values(counters)) {
      let flag = flagsOut[c.key];
      if (!flag) {
        flag = {
          default: c.default,
          counters: [],
        };
        flagsOut[c.key] = flag;
      }
      const counterOut = {
        value: c.value,
        count: c.count,
      };
      if (c.variation !== undefined && c.variation !== null) {
        counterOut.variation = c.variation;
      }
      if (c.version !== undefined && c.version !== null) {
        counterOut.version = c.version;
      } else {
        counterOut.unknown = true;
      }
      flag.counters.push(counterOut);
    }
    return {
      startDate: startDate,
      endDate: endDate,
      features: flagsOut,
    };
  };

  es.clearSummary = () => {
    startDate = 0;
    endDate = 0;
    counters = {};
  };

  return es;
}

module.exports = EventSummarizer;
