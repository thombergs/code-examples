var assert = require('assert');
var UserFilter = require('../user_filter');

describe('user_filter', function() {
  // users to serialize
  var user = {
    'key': 'abc',
    'firstName': 'Sue',
    'custom': { 'bizzle': 'def', 'dizzle': 'ghi' }
  };

  var userSpecifyingOwnPrivateAttr = {
    'key': 'abc',
    'firstName': 'Sue',
    'custom': { 'bizzle': 'def', 'dizzle': 'ghi' },
    'privateAttributeNames': [ 'dizzle', 'unused' ]
  };

  var userWithUnknownTopLevelAttrs = {
    'key': 'abc',
    'firstName': 'Sue',
    'species': 'human',
    'hatSize': 6,
    'custom': { 'bizzle': 'def', 'dizzle': 'ghi' }
  };

  var anonUser = {
    'key': 'abc',
    'anonymous': true,
    'custom': { 'bizzle': 'def', 'dizzle': 'ghi' }
  };

  // expected results from serializing user
  var userWithAllAttrsHidden = {
    'key': 'abc',
    'custom': { },
    'privateAttrs': [ 'bizzle', 'dizzle', 'firstName' ]
  };

  var userWithSomeAttrsHidden = {
    'key': 'abc',
    'custom': {
      'dizzle': 'ghi'
    },
    'privateAttrs': [ 'bizzle',  'firstName' ]
  };

  var userWithOwnSpecifiedAttrHidden = {
    'key': 'abc',
    'firstName': 'Sue',
    'custom': {
      'bizzle': 'def'
    },
    'privateAttrs': [ 'dizzle' ]
  };

  var anonUserWithAllAttrsHidden = {
    'key': 'abc',
    'anonymous': true,
    'custom': { },
    'privateAttrs': [ 'bizzle', 'dizzle' ]
  };

  it('includes all user attributes by default', function() {
    var uf = UserFilter({});
    assert.deepEqual(uf.filterUser(user), user);
  });

  it('hides all except key if allAttributesPrivate is true', function() {
    var uf = UserFilter({ allAttributesPrivate: true});
    assert.deepEqual(uf.filterUser(user), userWithAllAttrsHidden);
  });

  it('hides some attributes if privateAttributeNames is set', function() {
    var uf = UserFilter({ privateAttributeNames: [ 'firstName', 'bizzle' ]});
    assert.deepEqual(uf.filterUser(user), userWithSomeAttrsHidden);
  });

  it('hides attributes specified in per-user privateAttrs', function() {
    var uf = UserFilter({});
    assert.deepEqual(uf.filterUser(userSpecifyingOwnPrivateAttr), userWithOwnSpecifiedAttrHidden);
  });

  it('looks at both per-user privateAttrs and global config', function() {
    var uf = UserFilter({ privateAttributeNames: [ 'firstName', 'bizzle' ]});
    assert.deepEqual(uf.filterUser(userSpecifyingOwnPrivateAttr), userWithAllAttrsHidden);
  });

  it('strips unknown top-level attributes', function() {
    var uf = UserFilter({});
    assert.deepEqual(uf.filterUser(userWithUnknownTopLevelAttrs), user);
  });

  it('leaves the "anonymous" attribute as is', function() {
    var uf = UserFilter({ allAttributesPrivate: true});
    assert.deepEqual(uf.filterUser(anonUser), anonUserWithAllAttrsHidden);
  });
});
