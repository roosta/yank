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

browser.runtime.onMessage.addListener((request) => {
  if (request.yank) {
    copyToClipboard(request.yank);
    return Promise.resolve({ response: true })
  }
});
