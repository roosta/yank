import { fetchSettings } from "./settings.js"
import themeData from "./theme.json";

let settings = fetchSettings();
const selectEl = document.getElementById("format-select")

// Returns a default theme based on prefersDark result
function getTheme() {
  if (window.matchMedia("(prefers-color-scheme:dark)").matches)
    return themeData.dark
  return themeData.light
}

// Sets CSS vars in DOM based on input theme obj
function setTheme(theme) {
  const vars = [
    `--color-background: ${theme.background}`,
    `--color-text: ${theme.text}`,
    `--color-button: ${theme.button}`,
    `--color-button-active: ${theme.button_active}`,
    `--color-input-hover: ${theme.button_hover}`,
    `--color-input-background: ${theme.input_background}`,
    `--color-input-border: ${theme.input_border}`,
  ];
  document.body.setAttribute('style', vars.join(';'));
}

setTheme(getTheme())
