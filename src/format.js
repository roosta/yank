
// Replace in string s, with array of tuples [[regex, replacement text], [...]]
function stresc(s, m) {
  return m.reduce((acc, [re, repl]) => {
    return acc.replace(new RegExp(`${re}`, "g"), repl)
  }, s)
}

const formats = {
  md: (title, url) => {
    let esc = stresc(title, [
      ["_", "\\_"],
      ["\\[", "\\["],
      ["]", "\\]"],
      [">", "\\>"],
      ["<", "\\<"],
      ["\\*", "\\*"],
      ["`", "\\`"]]
    );
    return `[${esc}](${url})`;
  },
  // Org mode doesn't seem to have any escape sequence for description, so
  // using curly brackets
  // https://stackoverflow.com/questions/27284913/how-to-escape-square-bracket-in-org-mode-links
  org: (title, url) => {
    let esc = stresc(title, [
      ["\\[", "{"],
      ["\\]", "}"]]
    );
    return `[[${url}][${esc}]]`;
  },
  textile: (title, url) => {
    let esc = stresc(title, [
      ['"', '\\"']]
    );
    return `"${esc}":${url}`
  },
  asciidoc: (title, url) => {
    let esc = stresc(title, [
      ["\\]", "\\]"]]
    );
    return `${url}[${esc}]`
  },
  rest: (title, url) => {
    let esc = stresc(title, [
      ["_", "\\_"],
      ["`", "\\`"]]
    );
    return `\`${esc} <${url}>\`_`
  },

  // Start by replacing all apersand symbols, otherwise it'll replace the
  // ampersand in the remaining escapes
  html: (title, url) => {
    let esc = stresc(title, [
      ["&", "&amp;"],
      ["<", "&lt;"],
      [">", "&gt;"]]
    );
    return `<a href="${url}">${esc}</a>`
  }
}

export function dispatch({ title, url, format }) {
  return formats[format](title, url);
}
