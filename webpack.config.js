const path = require("path");

module.exports = {
  watch: true,
  entry: {
    content_script: "./src/content_script.js",
  },
  output: {
    path: path.resolve(__dirname, "addon"),
    filename: "[name]/index.js"
  },
  mode: 'none',
};
