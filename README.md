# [ yank ]

A Firefox extension to copy current tab URL and title to clipboard, formatted
for a markup language. Supports several different markup languages.

## Usage

Click on the extension in the toolbar, opening a popup. From there you can
choose a  markup format (default `markdown`), then click the button to copy to
clipboard.

Yank can also be used as a command (keyboard shortcut), and can be configured
in Firefox `about:addons`. Click the preferences icon and click `Manage
Extension Shortcuts`, and configure your preferred shortcut. Default is
<kbd>Ctrl+Y</kbd>

There is also a context menu option, that allows for copying an anchors `href`
and content.

## Supported formats

Yank currently supports these markup languages:

  - [Markdown](https://daringfireball.net/projects/markdown/) (default)
  - [Org-mode](http://orgmode.org/)
  - [Textile](https://github.com/textile)
  - [AsciiDoc](http://asciidoc.org/)
  - [reStructuredText](http://docutils.sourceforge.net/rst.html)
  - [HTML](https://www.w3.org/html/)
  - [LaTeX](https://www.latex-project.org/)
  - [typst](https://typst.app/)
  - [Wikitext](https://en.wikipedia.org/wiki/Help:Wikitext)

## Permissions

The full list of permissions is:

- `activeTab`,
  -  Needed to get the title and URL that gets copied
- `clipboardWrite`,
  - Needed to write to clipboard, reading is never done
- `contextMenus`,
  - Needed to enable context menu to copy links on page
- `storage`
  - Needed to save format. Its saved in local storage, and is not synced with account (anymore)

## Dependency free

Since version `2.0.0` the yank extension doesn't bundle any dependencies, there
are only development dependencies.

## Development

``` example
git clone https://github.com/roosta/yank && cd yank && npm install
```

Run `npm run dev` to start a
[concurrent](https://github.com/open-cli-tools/concurrently)
[webpack](https://webpack.js.org/) and
[web-ext](https://github.com/mozilla/web-ext) watch processes. It'll open
Firefox developer edition and a debugger.

## Release

Run `npm run prod` to build webpack, package using [web-ext](https://github.com/mozilla/web-ext)

See [package.json](package.json) `scripts` for details on each individual step

## Testing

I've manually doubled checked each format using this string:

```
<my> {fancy} "title" \\with\\ [ chars ] backtick(`), tilde(~), underscore(_), amp(&), dollar($), uptick(^), pipe(|)
```
That string runs through all the usual suspects, then I will check the result
with a parser depending on the format.

Automating this might be possible but IMO not worth the effort, and would be
needlessly complicated. Might setup some unit tests later for various
functions, but for now I'm satisfied with manual testing.

## License

[MIT](LICENSE)
