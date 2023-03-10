
// Escape string s, with array of tuples [regex, replacement text] i.e
// stresc(mystr, [["_", "\\_"], ["[", "\\["]])
function stresc(s, m) {
  return m.reduce((acc, [re, repl]) => {
    return acc.replace(re, repl)
  }, s)
}

export function md(title, url) {
  let esc = stresc(title, [
    ["_", "\\_"],
    ["[", "\\["],
    ["]", "\\]"]]
  );
  return `[${esc}](${url})`;
}
