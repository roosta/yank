import { fetchSettings } from "./settings.js"

let settings = fetchSettings();
const selectEl = document.getElementById("format-select");

browser.theme.getCurrent().then((theme) => {
  if (theme && theme.colors) {
    console.log(theme.colors)
    const colors = [
      `--color-background: ${theme.colors.popup}`,
      `--color-primary: ${theme.colors.popup_text}`,
      `--color-button: ${theme.colors.button}`,
    ];
    document.body.setAttribute('style', colors.join(';'));
  }
});
// console.log(browser.theme.getCurrent())
