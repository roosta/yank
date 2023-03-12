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
  navigator.clipboard.writeText(newClip).then(() => {
    console.log("Yank: successfully wrote to clipboard")
  }, onError);
}

browser.commands.onCommand.addListener((command) => {
  if (command === "yank") {
    browser.tabs.query({currentWindow: true, active: true}, ([tab]) => {
      const text = dispatch({
        title: 'my <fancy> "``title" _ with $ chars [hello]',
        format: settings.format,
        url: tab.url
      });
      updateClipboard(text);
    });
  }
});

// Listen for settings change
browser.storage.onChanged.addListener(() => {
  settings = fetchSettings();
})
