## Yank
A Firefox extension to copy current tab URL to clipboard, formatted for
a markup language. Supports custom shortcuts and several different
markup languages.

### Usage

By default this plugin binds `ctrl+y` to copy URL as org-mode. To change
this open up the options page, either from a `about:addons` or by
clicking the addon icon in the toolbar

### Shortcut keys

Currently allowed keybinds are any alphanumeric key and/or any
combination of ctrl/alt/shift

### Supported formats

Yank currently supports these markup languages:

  - [Org-mode](http://orgmode.org/)
  - [Markdown](https://daringfireball.net/projects/markdown/)
  - [Textile](https://github.com/textile)
  - [AsciiDoc](http://asciidoc.org/)
  - [reStructuredText](http://docutils.sourceforge.net/rst.html)
  - [HTML](https://www.w3.org/html/)

I wrote this plugin solely for org-mode, and just added the remaining
formats since it required very little once the code surrounding it
actually worked. Reason I mention this is that I don't actively use the
other formats, so aside from org-mode and possibly markdown I can't say
with certainty that the plugin handles it correctly. Please open an
issue if there is a problem, or any other format is desired.

## Development

1.  Clone this repository

<!-- end list -->

``` example
$ git clone https://github.com/roosta/yank
$ cd yank
```

1.  To get a development version going, open up a terminal and run:

<!-- end list -->

``` example
$ lein fig
```

A. Alternatively there's a file called `script/repl.clj`, You can use
this in Emacs to start figwheel when using
[cider](https://github.com/clojure-emacs/cider). Put this in your Emacs
config file:

``` elisp
(setq cider-cljs-lein-repl "(require 'repl)")
```

1.  Open up a second terminal and run:

<!-- end list -->

``` example
$ lein content
```

This will build everything, and give you a REPL connected to the options
namespace. Open up Firefox and goto `about:debugging` -\> Load Temporary
Add-on -\> browse to: `[project-root]/resources/dev/manifest.json`

1.  To build a release version and package it run:

<!-- end list -->

``` example
$ lein release && lein package
```

## Troubleshooting

This plugin loads a content script that listens for a chosen key, but it is
loaded as a context of the current page, which means it acts just like any other
website key-press listener, therefore when the focus is on say an input field
yank won't override any keyboard events and will simply not work. Just keep that
in mind when trying to yank an URL and something else has the keyboard focus.

## Bugs

There are very likely bugs contained in this plugin. I'm actively using
it myself, and fixing stuff as I find it, but any submitted issue is
appreciated.

## Other projects

### chromex-sample

<https://github.com/binaryage/chromex-sample>

Borrowed a fair bit from this project's tooling. Never coded a
web-extension plugin before and this repo really helped me out

### chrome-shortkeys

<https://github.com/mikecrittenden/chrome-shortkeys>

Inspired how to handle binding custom keys, before I stumbled on this
plugin I really had no idea how to go about doing this.

### Mousetrap

<https://craig.is/killing/mice>

Using this neat library to bind the custom shortcut keys.

### Figwheel

<https://github.com/bhauman/lein-figwheel>

Plugin used to hotload source code when developing

### Other plugins

  - [Copy as Org-mode](https://addons.mozilla.org/en-US/firefox/addon/copy-as-org-mode/?src=search)
  - [Org Capture](https://addons.mozilla.org/en-US/firefox/addon/org-capture/?src=search)

These plugins looks nice, I would have used one of them, but they were
either to complicated or didn't support custom shortcuts.