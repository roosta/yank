const path = require("path");

module.exports = {
  watch: true,
  entry: {
    background: "./src/background.js",
  },
  output: {
    path: path.resolve(__dirname, "addon"),
    filename: "[name]/index.js"
  },
  mode: 'none',
};
