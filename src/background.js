import { fetchSettings } from "./settings.js"
import { dispatch } from "./format.js"
import { errorHandler } from "./logging.js"

var settings;

function createContextMenu() {
  browser.contextMenus.create({
    id: "yank",
    title: "Yank link to clipboard",
    contexts: ["link"]
  })

}
// Updates clipboard with newClip, ignore succees, catch error
function updateClipboard(newClip) {
  navigator.clipboard
    .writeText(newClip)
    .catch(errorHandler("Clipboard write failed"));
}

// Get active tab info, format, then update clipboard
function yank() {
  browser.tabs.query({currentWindow: true, active: true}, ([tab]) => {
    const text = dispatch({
      title: tab.title,
      format: settings.format,
      url: tab.url
    });
    updateClipboard(text);
  });
}

function handleCommand(command) {
  if (command === "yank") {
    yank();
  }
}

function handleMessage(request) {
  if (request === "yank") {
    yank();
  }
}

function handleContext(info) {
  const text = dispatch({
    title: info.linkText,
    url: info.linkUrl,
    format: settings.format
  })
  updateClipboard(text);
}

async function main() {
  settings = await fetchSettings();
  createContextMenu();
  browser.contextMenus.onClicked.addListener(handleContext);
  browser.runtime.onMessage.addListener(handleMessage);
  browser.commands.onCommand.addListener(handleCommand);
  browser.storage.onChanged.addListener(({ yank: { newValue, oldValue } }) => {
    if (newValue !== oldValue) {
      settings = newValue;
    }
  })
}

main();

// vim: set ts=2 sw=2 tw=0 fdm=marker et :
