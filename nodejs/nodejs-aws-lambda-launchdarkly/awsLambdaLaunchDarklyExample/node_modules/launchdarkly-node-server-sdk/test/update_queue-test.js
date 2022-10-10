var UpdateQueue = require('../update_queue');

describe('UpdateQueue', function() {
  it('executes task immediately if there are no pending tasks', function(done) {
    const q = new UpdateQueue();

    var updated = false;
    const updateFn = function(a, b, cb) {
      expect(a).toEqual(1);
      expect(b).toEqual(2);
      updated = true;
      cb();
    };

    q.enqueue(updateFn, [1, 2], function() {
      expect(updated).toEqual(true);
      done();
    });
  });

  it('serializes async tasks in the order submitted', function(done) {
    const q  = new UpdateQueue();

    var progress = [];

    // This simulates a condition in which events are being received asynchronously and each
    // event triggers an asynchronous task. We want to make sure that the tasks are executed in
    // the order submitted, even if one is submitted during the execution of the previous one.
    const taskFn = function(i, cb) {
      progress.push('start ' + i);
      // assume that we're doing something asynchronous here - make sure it takes a little time
      setTimeout(cb, 20);
    };

    const expected = [
      'submit 1',
      'start 1',  // note, this one executes immediately because there was nothing pending
      'submit 2',
      'submit 3',
      'end 1',
      'start 2',
      'end 2',
      'start 3',
      'end 3'
    ];

    for (var i = 1; i <= 3; i++) {
      const j = i;
      setImmediate(function() {
        progress.push('submit ' + j);
        q.enqueue(taskFn, [j], function() {
          progress.push('end ' + j);
          if (j >= 3) {
            expect(progress).toEqual(expected);
            done();
          }
        });
      });
    }
  });
});
