// import Mousetrap from "mousetrap";
import { fetchSettings } from "./settings.js"

let settings = fetchSettings();

function copyToClipboard(text) {
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

// Mousetrap.bind(settings.keybind.composed, copyToClipboard);
browser.storage.onChanged.addListener(() => {
  settings = fetchSettings();
})
console.log(browser)
// browser.commands.onCommand.addListener((command) => {
//   copyToClipboard("command: ", command);
// });
