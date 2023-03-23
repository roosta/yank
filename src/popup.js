import { fetchSettings } from "./settings.js"
import defaultTheme from "./theme.json";

let settings = fetchSettings();
const selectEl = document.getElementById("format-select")

function prefersDark() {
  return window.matchMedia("(prefers-color-scheme:dark)").matches
}

// Returns a default theme based on prefersDark result
function getDefaultTheme() {
  if (prefersDark())
    return { colors: defaultTheme.dark }
  return { colors: defaultTheme.light }
}


const fallbackTheme = getDefaultTheme();

// Sets CSS vars in DOM based on input theme obj
//
function setVars({ colors }) {
  const vars = [
    `--color-background: ${
      colors.popup || fallbackTheme.colors.popup
    }`,
    `--color-text: ${
      colors.popup_text || fallbackTheme.colors.popup_text
    }`,
    `--color-button-background: ${
      colors.button
        || colors.popup
        || fallbackTheme.colors.button
    }`,
    `--color-button-hover: ${
      colors.button_hover
        || colors.popup_highlight
        || prefersDark() ? 'rgba(0, 0, 0, 0.2)' : 'rgba(255, 255, 255, 0.2)'
    }`,
    `--color-button-active: ${
      colors.button_active
        || colors.focus_outline
        || colors.popup_highlight
        || fallbackTheme.colors.button_active
    }`,
    `--color-input-background: ${
      colors.input_background
        || colors.popup
        || fallbackTheme.colors.input_background
    }`,
    `--color-input-border: ${
      colors.input_border || fallbackTheme.colors.input_border
    }`,
    `--color-input: ${
      colors.input || fallbackTheme.colors.input
    }`,
  ];
  document.body.setAttribute('style', vars.join(';'));
}

function setTheme(theme) {
  if (theme && theme.colors) {
    console.log(theme);
    setVars(theme);
  } else {
    setVars(fallbackTheme);
  }
}

browser.theme.getCurrent().then(setTheme);
