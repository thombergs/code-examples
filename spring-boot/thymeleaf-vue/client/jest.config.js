module.exports = {
  preset: "@vue/cli-plugin-unit-jest",
  collectCoverage: true,
  collectCoverageFrom: ["src/**/*.{js,vue}", "!**/node_modules/**"],
  coverageReporters: ["html", "text-summary"],
  setupFilesAfterEnv: [
      "./jest/console-error-to-exception.setup.js",
      "./jest/mock-canvas.setup.js"
 ]
};
