
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
      ["]", "\\]"]]
    );
    return `[${esc}](${url})`;
  },
  org: (title, url) => {
    let esc = stresc(title, [
      ["[", "{"],
      ["]", "}"]]
    );
    return `[[${esc}][${url}]]`;
  },
  textile: (title, url) => {
    let esc = stresc(title, [
      ['"', '\\"']]
    );
    return `"${esc}":${url}`
  },
  asciidoc: (title, url) => {
    let esc = stresc(title, [
      ["]", "\\]"]]
    );
    return `${url}[${esc}]`
  },
  rest: (title, url) => {
    let esc = stresc(title, [
      ["_", "\\_"],
      ["`", "\\`"]]
    );
    return `\`${esc} <${url}>\``
  },
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
