module.exports = function stringifyAttrs(object, attrs) {
  if (!object) {
    return object;
  }
  let newObject;
  for (const attr of attrs) {
    const value = object[attr];
    if (value !== undefined && typeof value !== 'string') {
      newObject = newObject || Object.assign({}, object);
      newObject[attr] = String(value);
    }
  }
  return newObject || object;
};
