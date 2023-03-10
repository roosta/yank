import { fetchSettings } from "./settings.js"

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

// Escape string s, with array of tuples [regex, replacement text] i.e
// stresc(mystr, [["_", "\\_"], ["[", "\\["]])
function stresc(s, m) {
  return m.reduce((acc, [re, repl]) => {
    return acc.replace(re, repl)
  }, s)

}

browser.commands.onCommand.addListener(async (command) => {
  if (command === "yank") {
    browser.tabs.query({currentWindow: true, active: true}, ([tab]) => {
      // const re = /_/g;
      const text = "my fancy title _ with chars [hello]";
      // const asd = text.replace(re, "\\_") ;
      console.log(stresc(text, [["_", "\\_"], ["[", "\\["]]));
      browser.tabs.sendMessage(tab.id, { yank: "asd" });
    }).then((response) => {
      if (!response.response)
        console.warn("Yank: bad response, something went wrong");
    }).catch(onError);
  }
});
