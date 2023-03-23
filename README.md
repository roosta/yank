## Yank
[![Build Status](https://travis-ci.org/roosta/yank.svg?branch=master)](https://travis-ci.org/roosta/yank)

A Firefox extension to copy current tab URL to clipboard, formatted for a markup
language. Supports custom shortcuts and several different markup languages.

### Test title

```js
const tmp = '<my> {fancy} "title" \\with chars backtick(`), tilde(~), underscore(_), amp(&), dollar($), uptick(^)';
```

### Usage

By default this plugin binds `ctrl+y` to copy URL as org-mode. To change this
open up the options page, either from a `about:addons` or by clicking the addon
icon in the toolbar

There is also a context menu option, that allows for copying an anchors href and
content.

Quickly change the markup format by clicking the toolbar icon to display the popup
menu.

### Shortcut keys

Currently allowed shortcuts are any alphanumeric key and/or any combination of
ctrl/alt/shift/meta

### Supported formats

Yank currently supports these markup languages:

  - [Org-mode](http://orgmode.org/)
  - [Markdown](https://daringfireball.net/projects/markdown/)
  - [Textile](https://github.com/textile)
  - [AsciiDoc](http://asciidoc.org/)
  - [reStructuredText](http://docutils.sourceforge.net/rst.html)
  - [HTML](https://www.w3.org/html/)
  - [LaTeX](https://www.latex-project.org/)

## Development

The development of this extension was done using using
[leiningen](https://github.com/technomancy/leiningen) version 2.7.0 running on
OpenJDK Java version 1.8.0_181 on Arch linux. It also runs on
[travis-ci](https://travis-ci.org/roosta/yank), refer to
[.travis.yaml](https://github.com/roosta/yank/blob/master/.travis.yml).

``` example
$ git clone https://github.com/roosta/yank && cd yank
```

### Building assets

To build HTML, manifest and CSS assets call:

```shell
$ lein build
```

### Starting autobuild and repl

Start a repl either from an editor like emacs cider: `M-x cider-jack-in`, or from the terminal:

```shell
$ lein repl
```

You are then put into the user namespace where some figwheel helper functions are
defined. To get started call the function `start`:

```clojure
user=> (start)
```

This will start autobuilding. From here you can connect to a repl environment by calling:
```clojure
user=> (cljs-repl "BUILD NAME HERE")
```

to exit out of a repl env, call
```clojure
cljs.user=> :cljs/quit
```

That puts you back into the user namespace where you can `(stop)` figwheel or
start another `cljs-repl`

### Load extension in firefox

Open up Firefox and goto `about:debugging` -\> Load Temporary
Add-on -\> browse to: `[project-root]/resources/dev/manifest.json`

## Package/Release build

1.  To build a release version and package it run:

``` example
$ lein release
```

This will produce an unsigned .xpi file that can be temporarily loaded in
firefox. Bear in mind that this package is unsigned and you'd need to sign it
yourself to use it as anything but a temporary addon.

## Troubleshooting

This plugin loads a content script that listens for a chosen key, but it is
loaded as a context of the current page, which means it acts just like any other
website key-press listener, therefore when the focus is on say an input field
yank won't override any keyboard events and will simply not work. Just keep that
in mind when trying to yank an URL and something else has the keyboard focus.

I wrote this plugin solely for org-mode, and just added the remaining
formats since it required very little once the code surrounding it
actually worked. Reason I mention this is that I don't actively use the
other formats, so aside from org-mode and possibly markdown I can't say
with certainty that the plugin handles it correctly.

Please open an issue if there is a problem, or any other format is desired.

## Libs

- [Hiccup](https://github.com/weavejester/hiccup) - Used to render HTML from clojure data structures
- [Mousetrap](https://craig.is/killing/mice)  - Library to bind the custom shortcut keys.
- [Environ](https://github.com/weavejester/environ) - Manage environment variables
- [Garden](https://github.com/noprompt/garden) - Generate CSS
- [lein-asset-minifier](https://github.com/yogthos/lein-asset-minifier) - Minify generated CSS
- [Figwheel](https://github.com/bhauman/lein-figwheel) - Plugin used to hotload source code when developing
- [Piggieback](https://github.com/nrepl/piggieback/blob/master/README.md) - Used together with [cider](https://github.com/clojure-emacs/cider) to get a CLJS REPL
- [Doo](https://github.com/bensu/doo) - JS test runner
- [lein-shell](https://github.com/hyPiRion/lein-shell) - Call shell from within Leiningen, used in packaging process
- [Hickory](https://github.com/davidsantiago/hickory) - Parse HTML as data for use in tests
- [Etaoin](https://github.com/igrishaev/etaoin) - Webdriver protocol implementation used for integration testing


## Other projects

- [chromex-sample](https://github.com/binaryage/chromex-sample) - Borrowed a fair bit from this project's tooling. Never coded a
web-extension plugin before and this repo really helped

- [chrome-shortkeys](https://github.com/mikecrittenden/chrome-shortkeys) - Inspired how to handle binding custom keys, before I stumbled on this
plugin I really had no idea how to go about doing this.
