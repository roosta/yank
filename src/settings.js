import settings from "./settings.json";

export const defaults = {
  ...settings
};

// save settings Takes either an event object and settings map or only settings
export function saveSettings(opts, e) {
  browser.storage.sync.set({ yank: opts })
  if (e) e.preventDefault();
}

export function fetchSettings() {
  const result = browser.storage.sync.get("yank").then(res => {
    return res.yank ? res.yank : defaults;
  });
  return result;
}
