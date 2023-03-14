import { fetchSettings } from "./settings.js"
import { dispatch } from "./format.js"

let settings = fetchSettings();

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

function updateClipboard(newClip) {
  navigator.clipboard.writeText(newClip).catch(onError);
}

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

createContextMenu();


browser.contextMenus.onClicked.addListener(handleContext)
// Listen for settings change
browser.storage.onChanged.addListener(() => {
  settings = fetchSettings();
})
