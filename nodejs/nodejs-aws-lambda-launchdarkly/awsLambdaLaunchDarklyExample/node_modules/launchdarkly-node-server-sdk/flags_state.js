function FlagsStateBuilder(valid, withReasons) {
  const builder = {};
  const flagValues = {};
  const flagMetadata = {};

  builder.addFlag = (flag, value, variation, reason, trackEvents, trackReason, detailsOnlyIfTracked) => {
    flagValues[flag.key] = value;
    const meta = {};
    if (variation !== undefined && variation !== null) {
      meta.variation = variation;
    }
    const omitDetails =
      detailsOnlyIfTracked &&
      !trackEvents &&
      !trackReason &&
      (flag.debugEventsUntilDate === undefined || flag.debugEventsUntilDate === null);
    if (!omitDetails) {
      meta.version = flag.version;
    }
    if (reason && (trackReason || (withReasons && !omitDetails))) {
      meta.reason = reason;
    }
    if (trackEvents) {
      meta.trackEvents = true;
    }
    if (trackReason) {
      meta.trackReason = true;
    }
    if (flag.debugEventsUntilDate !== undefined && flag.debugEventsUntilDate !== null) {
      meta.debugEventsUntilDate = flag.debugEventsUntilDate;
    }
    flagMetadata[flag.key] = meta;
  };

  builder.build = () => ({
    valid: valid,
    allValues: () => flagValues,
    getFlagValue: key => flagValues[key],
    getFlagReason: key => (flagMetadata[key] ? flagMetadata[key].reason : null),
    toJSON: () => Object.assign({}, flagValues, { $flagsState: flagMetadata, $valid: valid }),
  });

  return builder;
}

module.exports = FlagsStateBuilder;
