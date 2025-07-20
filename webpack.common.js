import path from "path";
import { fileURLToPath } from "url";

const __dirname = path.dirname(fileURLToPath(import.meta.url));

export default {
  entry: {
    background: "./src/background.js",
    popup: "./src/popup.js",
  },
  output: {
    path: path.resolve(__dirname, "addon"),
    filename: "[name]/index.js",
  },
};
