const path = require("path");

module.exports = {
  entry: {
    background: "./src/background.js",
    popup: "./src/popup.js",
  },
  output: {
    path: path.resolve(__dirname, "addon"),
    filename: "[name]/index.js",
  },
};
