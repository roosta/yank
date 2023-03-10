import { fetchSettings } from "./settings.js"
import { md } from "./format.js"

let settings = fetchSettings();

function createContextMenu() {
  browser.contextMenus.create({
    id: "yank",
    title: "Yank link to clipboard",
    contexts: ["link"]
  })

}
function onError(error) {
  console.error(`Error: ${error}`);
}

browser.commands.onCommand.addListener(async (command) => {
  if (command === "yank") {
    browser.tabs.query({currentWindow: true, active: true}, ([tab]) => {
      const text = md("my fancy title _ with chars [hello]", tab.url);
      browser.tabs.sendMessage(tab.id, { yank: text });
    }).then((response) => {
      if (!response.response)
        console.warn("Yank: bad response, something went wrong");
    }).catch(onError);
  }
});

// Listen for settings change
browser.storage.onChanged.addListener(() => {
  settings = fetchSettings();
})
