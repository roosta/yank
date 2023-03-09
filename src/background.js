import { fetchSettings } from "settings.js"

let settings = fetchSettings();

function createContextMenu() {
  browser.contextMenus.create({
    id: "yank",
    title: "Yank link to clipboard",
    contexts: ["link"]
  })

}
