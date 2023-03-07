import Mousetrap from "mousetrap";
import { fetchSettings } from "./settings.js"

var settings = fetchSettings();

// Sends a message using browser runtime
// Gets handled in background script
function copyToClipboard() {
  const text = "hello clipboard";
  const oncopy = (e) => {
    document.removeEventListener("copy", oncopy, true);
    // Hide the event from the page to prevent tampering.
    e.stopImmediatePropagation();

    // Overwrite the clipboard content.
    e.preventDefault();
    e.clipboardData.setData("text/plain", text);
  }
  document.addEventListener("copy", oncopy, true);

  // Requires the clipboardWrite permission, or a user gesture:
  document.execCommand("copy");
}

Mousetrap.bind(settings.keybind.composed, copyToClipboard);
browser.storage.onChanged.addListener(() => {
  const resp = fetchSettings();
  const { keybind: { composed: oc } } = { ...settings };
  const { keybind: { composed: nc } } = resp;
  if (oc !== nc) {
    Mousetrap.unbind(oc);
    Mousetrap.bind(nc, copyToClipboard);
  }
  settings = resp;
})
console.log(Mousetrap);
