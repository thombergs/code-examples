const path = require('path');

const WebpackServeUrl = 'http://localhost:3002';

module.exports = {
  mode: 'development',
  devtool: 'source-map',
  entry: ['@babel/polyfill', './src/client/index'],
  output: {
    path: path.resolve('dist'),
    publicPath: `${WebpackServeUrl}/dist/`, // MUST BE FULL PATH!
    filename: 'bundle.js',
  },
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
      },
    ],
  },
};
