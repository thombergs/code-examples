function UpdateQueue() {
  const updateQueue = [];
  this.enqueue = (updateFn, fnArgs, cb) => {
    updateQueue.push([updateFn, fnArgs, cb]);
    if (updateQueue.length === 1) {
      // if nothing else is in progress, we can start this one right away
      executePendingUpdates();
    }
  };
  function executePendingUpdates() {
    if (updateQueue.length > 0) {
      const entry = updateQueue[0];
      const fn = entry[0];
      const args = entry[1];
      const cb = entry[2];
      const newCb = () => {
        updateQueue.shift();
        if (updateQueue.length > 0) {
          setImmediate(executePendingUpdates);
        }
        cb && cb();
      };
      fn.apply(null, args.concat([newCb]));
    }
  }
}

module.exports = UpdateQueue;
