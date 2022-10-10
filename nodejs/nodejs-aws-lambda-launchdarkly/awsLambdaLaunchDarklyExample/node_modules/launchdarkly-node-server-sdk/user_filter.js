/**
 * The UserFilter object transforms user objects into objects suitable to be sent as JSON to
 * the server, hiding any private user attributes.
 *
 * @param {Object} the LaunchDarkly client configuration object
 **/
function UserFilter(config) {
  const filter = {};
  const allAttributesPrivate = config.allAttributesPrivate;
  const privateAttributeNames = config.privateAttributeNames || [];
  const ignoreAttrs = { key: true, custom: true, anonymous: true };
  const allowedTopLevelAttrs = {
    key: true,
    secondary: true,
    ip: true,
    country: true,
    email: true,
    firstName: true,
    lastName: true,
    avatar: true,
    name: true,
    anonymous: true,
    custom: true,
  };

  filter.filterUser = user => {
    const userPrivateAttrs = user.privateAttributeNames || [];
    const isPrivateAttr = name =>
      !ignoreAttrs[name] &&
      (allAttributesPrivate || userPrivateAttrs.indexOf(name) !== -1 || privateAttributeNames.indexOf(name) !== -1);
    const filterAttrs = (props, isAttributeAllowed) =>
      Object.keys(props).reduce(
        (accIn, name) => {
          const acc = accIn;
          if (isAttributeAllowed(name)) {
            if (isPrivateAttr(name)) {
              // add to hidden list
              acc[1][name] = true;
            } else {
              acc[0][name] = props[name];
            }
          }
          return acc;
        },
        [{}, {}]
      );

    const result = filterAttrs(user, key => allowedTopLevelAttrs[key]);
    const filteredProps = result[0];
    const removedAttrs = result[1];
    if (user.custom) {
      const customResult = filterAttrs(user.custom, () => true);
      filteredProps.custom = customResult[0];
      Object.assign(removedAttrs, customResult[1]);
    }
    const removedAttrNames = Object.keys(removedAttrs);
    if (removedAttrNames.length) {
      removedAttrNames.sort();
      filteredProps.privateAttrs = removedAttrNames;
    }
    return filteredProps;
  };

  return filter;
}

module.exports = UserFilter;
