module.exports = {
  out: '/tmp/project-releaser/project/docs/build/html',
  exclude: [
    '**/node_modules/**',
    'test-types.ts'
  ],
  name: "LaunchDarkly Server-Side Node SDK (6.2.1)",
  readme: 'none',                // don't add a home page with a copy of README.md
  entryPoints: "/tmp/project-releaser/project/index.d.ts"
};
