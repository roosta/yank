import settings from "./settings.json";
import { errorHandler } from "./logging.js";

export const defaults = {
  ...settings
};

// save settings Takes either an event object and settings map or only settings
export function saveSettings(opts, e) {
  const res = browser.storage.local.set({ yank: opts })
  res.catch(errorHandler("Failed to set options"));
  if (e) e.preventDefault();
}

export function fetchSettings() {
  const result = browser.storage.local.get("yank").then(res => {
    return res.yank ? res.yank : defaults;
  }, errorHandler("Failed to get options"));
  return result;
}
