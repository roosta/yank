import { fetchSettings } from "./settings.js"
import { light, dark } from "./theme.json";

let settings = fetchSettings();
const selectEl = document.getElementById("format-select")

// Returns a default theme based on prefersDark result
function getDefaultTheme() {
  if (window.matchMedia("(prefers-color-scheme:dark)").matches)
    return { colors: dark }
  return { colors: light }
}

// Sets CSS vars in DOM based on input theme obj
function setVars({ colors }) {
  const vars = [
    `--color-background: ${colors.popup}`,
    `--color-text: ${colors.popup_text}`,
    `--color-button-background: ${colors.button_primary_color}`,
    `--color-button-hover: ${colors.button_hover}`,
    `--color-button-active: ${colors.button_active}`,
    `--color-input-background: ${colors.input_background}`,
    `--color-input-border: ${colors.input_border}`,
    `--color-input: ${colors.input}`,
  ];
  document.body.setAttribute('style', vars.join(';'));
}

function setTheme(theme) {
  if (theme && theme.colors) {
    setVars(theme);
  } else {
    setVars(getDefaultTheme());
  }
}

browser.theme.getCurrent().then(setTheme);
