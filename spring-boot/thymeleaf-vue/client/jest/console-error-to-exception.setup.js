import { format } from "util";

/**
 * Log UnhandledRejection exceptions to console.error to be picked up by
 * below logging patterns. This will cause tests to fail when Vue components
 * are missing required props, for example.
 */
process.on("unhandledRejection", (error) => {
  // Will print "unhandledRejection err is not defined"
  console.error("unhandledRejection", error.message);
});

/**
 * Intercept calls to console.error and fail a test on certain logging patterns.
 */
beforeEach(() => {
  const { error } = global.console;

  global.console.error = (...args) => {
    for (let i = 0; i < args.length; i += 1) {
      const arg = args[i];

      // add patterns here that should fail a test
      if (
        typeof arg === "string" &&
        (arg.includes("Vue warn") ||
          arg.includes("unhandledRejection") ||
          arg.includes("Not implemented"))
      ) {
        throw new Error(format(...args));
      }
    }
    error(...args);
  };
});
