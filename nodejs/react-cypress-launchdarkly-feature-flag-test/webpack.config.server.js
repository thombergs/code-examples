const path = require('path');
const nodeExternals = require('webpack-node-externals');

module.exports = {
  mode: 'development',
  devtool: 'source-map',
  entry: ['@babel/polyfill', './src/server/server.js'], // set this to your server entry point. This should be where you start your express server with .listen()
  target: 'node', // tell webpack this bundle will be used in nodejs environment.
  externals: [nodeExternals()], // Omit node_modules code from the bundle. You don't want and don't need them in the bundle.
  output: {
    path: path.resolve('dist'),
    filename: 'serverBundle.js',
    libraryTarget: 'commonjs2', // IMPORTANT! Add module.exports to the beginning of the bundle, so universal-hot-reload can access your app.
  },
  // The rest of the config is pretty standard and can contain other webpack stuff you need.
  module: {
    rules: [
      {
        test: /\.jsx?$/,
        include: path.resolve('src'),
        exclude: /node_modules/,
        loader: 'babel-loader',
        options: {
          cacheDirectory: true,
        },
      }],
  },
};