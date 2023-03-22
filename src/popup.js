import { fetchSettings } from "./settings.js"

let settings = fetchSettings();
const selectEl = document.getElementById("format-select");

browser.theme.getCurrent().then((theme) => {
  if (theme && theme.colors) {
    console.log(theme.colors)
    const colors = [
      `--color-background: ${theme.colors.popup}`,
      `--color-text: ${theme.colors.popup_text}`,
      `--color-button: ${theme.colors.button}`,
      `--color-button-background: ${theme.colors.button_primary_color}`,
      `--color-button-hover: ${theme.colors.button_hover}`,
      `--color-button-active: ${theme.colors.button_active}`,
      `--color-input-background: ${theme.colors.input_background}`,
      `--color-input-border: ${theme.colors.input_border}`,
      `--color-input: ${theme.colors.input}`,
    ];
    document.body.setAttribute('style', colors.join(';'));
  }
});
// console.log(browser.theme.getCurrent())
