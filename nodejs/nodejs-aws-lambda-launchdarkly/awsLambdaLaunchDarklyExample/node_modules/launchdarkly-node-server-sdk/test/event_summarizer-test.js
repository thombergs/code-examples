var EventSummarizer = require('../event_summarizer');

describe('EventSummarizer', function() {

  var user = { key: 'key1' };

  it('does nothing for identify event', function() {
    var es = EventSummarizer();
    var snapshot = es.getSummary();
    es.summarizeEvent({ kind: 'identify', creationDate: 1000, user: user });
    expect(es.getSummary()).toEqual(snapshot);
  });

  it('does nothing for custom event', function() {
    var es = EventSummarizer();
    var snapshot = es.getSummary();
    es.summarizeEvent({ kind: 'custom', creationDate: 1000, key: 'eventkey', user: user });
    expect(es.getSummary()).toEqual(snapshot);
  });

  it('sets start and end dates for feature events', function() {
    var es = EventSummarizer();
    var event1 = { kind: 'feature', creationDate: 2000, key: 'key', user: user };
    var event2 = { kind: 'feature', creationDate: 1000, key: 'key', user: user };
    var event3 = { kind: 'feature', creationDate: 1500, key: 'key', user: user };
    es.summarizeEvent(event1);
    es.summarizeEvent(event2);
    es.summarizeEvent(event3);
    var data = es.getSummary();

    expect(data.startDate).toEqual(1000);
    expect(data.endDate).toEqual(2000);
  });

  it('increments counters for feature events', function() {
    var es = EventSummarizer();
    var event1 = { kind: 'feature', creationDate: 1000, key: 'key1', version: 11, user: user,
      variation: 1, value: 100, default: 111 };
    var event2 = { kind: 'feature', creationDate: 1000, key: 'key1', version: 11, user: user,
      variation: 2, value: 200, default: 111 };
    var event3 = { kind: 'feature', creationDate: 1000, key: 'key2', version: 22, user: user,
      variation: 1, value: 999, default: 222 };
    var event4 = { kind: 'feature', creationDate: 1000, key: 'key1', version: 11, user: user,
      variation: 1, value: 100, default: 111 };
    var event5 = { kind: 'feature', creationDate: 1000, key: 'badkey', user: user,
      value: 333, default: 333 };
    var event6 = { kind: 'feature', creationDate: 1000, key: 'zero-version', version: 0, user: user,
    variation: 1, value: 100, default: 444 };
    es.summarizeEvent(event1);
    es.summarizeEvent(event2);
    es.summarizeEvent(event3);
    es.summarizeEvent(event4);
    es.summarizeEvent(event5);
    es.summarizeEvent(event6);
    var data = es.getSummary();

    data.features.key1.counters.sort(function(a, b) { return a.value - b.value; });
    var expectedFeatures = {
      'zero-version': {
        default: 444,
        counters: [
          { variation: 1, value: 100, version: 0, count: 1}
        ]
      },
      key1: {
        default: 111,
        counters: [
          { variation: 1, value: 100, version: 11, count: 2 },
          { variation: 2, value: 200, version: 11, count: 1 }
        ]
      },
      key2: {
        default: 222,
        counters: [ { variation: 1, value: 999, version: 22, count: 1 }]
      },
      badkey: {
        default: 333,
        counters: [ { value: 333, unknown: true, count: 1 }]
      },
    };
    expect(data.features).toEqual(expectedFeatures);
  });

  it('distinguishes between zero and null/undefined in feature variation', function() {
    var es = EventSummarizer();
    var event1 = { kind: 'feature', creationDate: 1000, key: 'key1', version: 11, user: user,
      variation: 0, value: 100, default: 111 };
    var event2 = { kind: 'feature', creationDate: 1000, key: 'key1', version: 11, user: user,
      variation: null, value: 111, default: 111 };
    var event3 = { kind: 'feature', creationDate: 1000, key: 'key1', version: 11, user: user,
      /* variation undefined */ value: 111, default: 111 };
    es.summarizeEvent(event1);
    es.summarizeEvent(event2);
    es.summarizeEvent(event3);
    var data = es.getSummary();

    data.features.key1.counters.sort(function(a, b) { return a.value - b.value; });
    var expectedFeatures = {
      key1: {
        default: 111,
        counters: [
          { variation: 0, value: 100, version: 11, count: 1 },
          { value: 111, version: 11, count: 2 }
        ]
      }
    };
    expect(data.features).toEqual(expectedFeatures);
  });
});
