const UniversalHotReload = require('universal-hot-reload').default;

// supply your own webpack configs
const serverConfig = require('../../webpack.config.server.js');
const clientConfig = require('../../webpack.config.client.js');

// the configs are optional, you can supply either one or both.
// if you omit say the server config, then your server won't hot reload.
UniversalHotReload({ serverConfig, clientConfig });
