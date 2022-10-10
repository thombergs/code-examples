const wrapPromiseCallback = require('../wrapPromiseCallback');

describe('wrapPromiseCallback', () => {
  it('should resolve to the value', () => {
    const promise = wrapPromiseCallback(Promise.resolve('woohoo'));
    return expect(promise).resolves.toBe('woohoo');
  });

  it('should reject with the error', () => {
    const error = new Error('something went wrong');
    const promise = wrapPromiseCallback(Promise.reject(error));
    return expect(promise).rejects.toBe(error);
  });

  it('should call the callback with a value if the promise resolves', done => {
    const promise = wrapPromiseCallback(Promise.resolve('woohoo'), (error, value) => {
      expect(promise).toBeUndefined();
      expect(error).toBeNull();
      expect(value).toBe('woohoo');
      done();
    });
  });

  it('should call the callback with an error if the promise rejects', done => {
    const actualError = new Error('something went wrong');
    const promise = wrapPromiseCallback(Promise.reject(actualError), (error, value) => {
      expect(promise).toBeUndefined();
      expect(error).toBe(actualError);
      expect(value).toBeNull();
      done();
    });
  });
});
