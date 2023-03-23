import { fetchSettings } from "./settings.js"
import { dispatch } from "./format.js"

var settings;

function createContextMenu() {
  browser.contextMenus.create({
    id: "yank",
    title: "Yank link to clipboard",
    contexts: ["link"]
  })

}
function onError(error) {
  console.error(`Yank error: ${error}`);
}

// Updates clipboard with newClip, ignore succees, catch error
function updateClipboard(newClip) {
  navigator.clipboard.writeText(newClip).catch(onError);
}

// Addon command
browser.commands.onCommand.addListener((command) => {
  if (command === "yank") {
    browser.tabs.query({currentWindow: true, active: true}, ([tab]) => {
      const text = dispatch({
        title: tab.title,
        format: settings.format,
        url: tab.url
      });
      updateClipboard(text);
    });
  }
});

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
  browser.contextMenus.onClicked.addListener(handleContext)

  browser.storage.onChanged.addListener(({ yank: { newValue } }) => {
    settings = newValue;
  })
}
main();

// vim: set ts=2 sw=2 tw=0 fdm=marker et :
