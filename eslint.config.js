import mozilla from "eslint-plugin-mozilla"
import globals from "globals";

export default [
  ...mozilla.configs["flat/recommended"],
  {
    ignores: ["releases/**", "addon/**"]
  },
  {
    rules: {
      "no-unused-vars": "warn"
    },
    languageOptions: {
      ecmaVersion: "latest",
      sourceType: "module",
      globals: {
        ...globals.browser,
        ...globals.node,
        ...globals.webextensions,
      },
    }
  },
]
