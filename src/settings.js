import * as settings from "./settings.json";

export const defaults = {
  ...settings
};

// save settings Takes either an event object and settings map or only settings
export function saveSettings(e, opts) {
  browser.storage.sync.set({ yank: opts })
  if (e) e.preventDefault();
}

export function onStorageChange(ref, resp) {
  const newv = resp?.yank?.newValue
  if (newv) ref = newv;
}

export function fetchSettings() {
  let payload = defaults;
  browser.storage.sync.get("yank").then(res => {
    payload = res;
  });
  return payload
}
