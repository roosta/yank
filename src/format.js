
// Replace in string s, with array of tuples [[regex, replacement text], [...]]
function stresc(s, m) {
  return m.reduce((acc, [re, repl]) => {
    return acc.replace(new RegExp(`${re}`, "g"), repl)
  }, s)
}

const formats = {
  md: (title, url) => {
    let esc = stresc(title, [
      ["\\[", "{"],
      ["]", "}"],
    ]);
    return `[${esc}](${url})`;
  },
  org: (title, url) => {
    let esc = stresc(title, [
      ["\\\\", "\\textbackslash{}"],
      ["\\[", "\\lbrack{}"],
      ["\\]", "\\rbrack{}"]
    ]);
    return `[[${url}][${esc}]]`;
  },
  textile: (title, url) => {
    let esc = stresc(title, [
      ['"', '\\"']
    ]);
    return `"${esc}":${url}`
  },
  asciidoc: (title, url) => {
    let esc = stresc(title, [
      ["\\]", "\\{endsb}"],
    ]);
    return `${url}[${esc}]`
  },
  rest: (title, url) => {
    let esc = stresc(title, [
      ["_", "\\_"],
      ["`", "\\`"]
    ]);
    return `\`${esc} <${url}>\`_`
  },

  // Start by replacing all apersand symbols, otherwise it'll replace the
  // ampersand in the remaining escapes
  html: (title, url) => {
    let esc = stresc(title, [
      ["&", "&amp;"],
      ["<", "&lt;"],
      [">", "&gt;"]
    ]);
    return `<a href="${url}">${esc}</a>`
  },
  // Latex was a tricky one, I might possibly have missed something, some other
  // char that gets interpeted as something other than text, but I feel I've
  // covered most bases here. Got a bit fancy with the regex but there wasn't a
  // logical order that would work.
  latex: (title, url) =>  {
    let esc = stresc(title, [
      ["\\\\", "\\textbackslash{}"],
      ["(?<!textbackslash|textasciitilde|textgreater|textless|textasciicircum){", "\\{"],
      ["(?<!textbackslash{|textasciitilde{|textless{|textgreater{|textasciicircum{)}", "\\}"],
      ["&", "\\&"],
      ["%", "\\%"],
      ["\\$", "\\$"],
      ["#", "\\#"],
      ["_", "\\_"],
      ["\\~", "\\textasciitilde{}"],
      ["<", "\\textless{}"],
      [">", "\\textgreater{}"],
      ["\\^", "\\textasciicircum{}"]
    ]);
    return `\\href{${url}}{${esc}}`
  },
  typst: (title, url) => {
     let esc = stresc(title, [
       ["\\\\", "\\\\"],
       ["`", "\\`"],
       ["\\^", "\\^"],
       ["\\$", "\\$"],
       ["_", "\\_"]
       ,
     ]);
    return `#link("${url}")[${esc}]`
  },

  // https://en.wikipedia.org/wiki/Help:URL#Fixing_links_with_unsupported_characters
  wikitext: (title, url) => {
    let esc = stresc(title, [
      ["]", "&#93;"],
      ["\\n", " "],
      ["\\[", "&#91;"],
      ["\\|", "&#124;"],
    ]);
    return `[${url} ${esc}]`
  }
}

export function dispatch({ title, url, format }) {
  return formats[format](title, url);
}
