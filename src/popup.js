import { fetchSettings, saveSettings } from "./settings.js";
import themeData from "./theme.json";
import { errorHandler } from "./logging.js";

var settings;
const selectEl = document.getElementById("format-select");
const buttonEl = document.getElementById("yank-button");

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

function inputSync(v) {
  selectEl.value = v;
}

function onFormatChange(e) {
  const val = e.target.value;
  saveSettings({ "format": val });
}

function onButtonClick() {
  const sending = browser.runtime.sendMessage("yank");
  sending.catch(errorHandler("Failed to send a message"));
}

async function main() {
  settings = await fetchSettings();
  setTheme(getTheme())
  selectEl.addEventListener("change", onFormatChange);
  buttonEl.addEventListener("click", onButtonClick);
  inputSync(settings.format);

  browser.storage.onChanged.addListener(({ yank: { newValue } }) => {
    settings = newValue;
    inputSync(newValue.format)
  })
}

main();
