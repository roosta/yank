// webpack.dev.js
import { merge } from 'webpack-merge';
import common from './webpack.common.js';

export default merge(common, {
  mode: 'none',
  watch: true,
});
