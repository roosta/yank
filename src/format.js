
// Escape string s, with array of tuples [regex, replacement text] i.e
// stresc(mystr, [["_", "\\_"], ["[", "\\["]])
function stresc(s, m) {
  return m.reduce((acc, [re, repl]) => {
    return acc.replace(re, repl)
  }, s)
}

const formats = {
  md: (title, url) => {
    let esc = stresc(title, [
      ["_", "\\_"],
      ["[", "\\["],
      ["]", "\\]"]]
    );
    return `[${esc}](${url})`;
  }
}

export function dispatch({ title, url, format }) {
  return formats[format](title, url);
}
