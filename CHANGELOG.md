# Change Log
All notable changes to this project will be documented in this file. This change
log follows the conventions of [keepachangelog.com](http://keepachangelog.com/).

## [Unreleased]

## [v2.1.1] - 2025-07-20
### Fix

- (md) Reduce overzealous markdown escaping that isn't needed.
- (md) Replace bracket escape (\\]) with braces ({}) because some parsers stuggle
  with the bracket escape in links

### Dev

- Migrate eslint config
- Upgrade development dependencies
- Fix lint error for console.log/error print

## [v2.1.0] - 2025-02-18
### Added
- Add support for [Wikitext](https://en.wikipedia.org/wiki/Help:Wikitext)

## [v2.0.0] - 2023-03-25
### Changed
- BREAKING: Remove shortcut option, use extension shortcut instead (commands)
  - You will need to rebind your shortcut via `Manage Extension Shortcuts` in `Manage Extensions`
- Rework theme, and how dark/light variants are handled (see `src/theme.json`)
  - Create two new themes based of Firefox light and dark, will also use media query to check for system preference
- Remove content script entirely and in process greatly simplifies permissions,
  now more in line with what the plugin does, see
  [permissions](README.md#permissions) for a breakdown
- Add a new format; [typst](https://typst.app/)
- BREAKING: Yank no longer syncs the one setting it has (format) with account, its only saved on local machine.
  - I don't really see the need to sync this, its trivial to change, and its also something I personally keep changing on need anyways
- Default format is now markdown

### Fixed
- Extended or added to escapes for multiple formats
- Fix latex escapes (the previous attempts with certain symbols failed in parser)

## [v1.0.6] - 2020-01-13
### Changed
- Update dependencies

## [v1.0.5] - 2018-09-12
### Changed
- Use dark icon as default for browser-action

### Fix
- Background color for popup and options page should now be set correctly matching OS theme

## [v1.0.4] - 2018-09-11
### Fix
- Sync browser-action changes in options page
- Figwheel multiple build and repl environments

## [v1.0.3] - 2018-08-15
### Fix
- Fix theme icon path

## [v1.0.2] - 2018-08-15
### Added
- Icon for dark/light theme

## [v1.0.1] - 2018-07-29
### Added
- LaTeX support

## [v1.0.0] - 2018-07-25
### Added
- Add integration, background, HTML, and manifest tests
- Use travis CI

### Changed
- Upgrade dependencies
- Formatting fns are now pure and move to separte NS
- Improve reStructuredText escapes

### Fixed
- Options defaults are used and displayed when nothing is present in storage.

## [v0.3.0] - 2018-02-25
### Added
- Popup menu when clicking the toolbar icon, options to change the format on the
  fly. Shorcut option is still in preferences page.
- Add CSS to popup menu and options page

### Changed
- All HTML and manifest files are now generated from clojure src and is no
  longer tracked in repo
- License changed to MIT

## [v0.2.1] - 2017-09-24
### Fixed
- Minor issue with manifest version vs published versions

## [v0.2.0] - 2017-09-24
### Added
- Context menu, right click on a link in a page and copy its URL and description
- Added support for mac keyboards (untested)

## [v0.1.0] - 2017-09-24
### Initial release
Base feature-set in place, allows yanking using a custom keybind and supports
the following markup languages:
- Org-mode
- Markdown
- Textile
- AsciiDoc
- reStructuredText
- HTML

[Unreleased]: https://github.com/roosta/yank/compare/v2.1.1...HEAD
[v2.1.1]: https://github.com/roosta/yank/compare/v2.1.0...v2.1.1
[v2.1.0]: https://github.com/roosta/yank/compare/v2.0.0...v2.1.0
[v2.0.0]: https://github.com/roosta/yank/compare/v1.0.6...v2.0.0
[v1.0.6]: https://github.com/roosta/yank/compare/v1.0.5...v1.0.6
[v1.0.5]: https://github.com/roosta/yank/compare/v1.0.4...v1.0.5
[v1.0.4]: https://github.com/roosta/yank/compare/v1.0.3...v1.0.4
[v1.0.3]: https://github.com/roosta/yank/compare/v1.0.2...v1.0.3
[v1.0.2]: https://github.com/roosta/yank/compare/v1.0.1...v1.0.2
[v1.0.1]: https://github.com/roosta/yank/compare/v1.0.0...v1.0.1
[v1.0.0]: https://github.com/roosta/yank/compare/v0.3.0...v1.0.0
[v0.3.0]: https://github.com/roosta/yank/compare/v0.2.1...v0.3.0
[v0.2.1]: https://github.com/roosta/yank/compare/v0.2.0...v0.2.1
[v0.2.0]: https://github.com/roosta/yank/compare/v0.1.0...v0.2.0
[v0.1.0]: https://github.com/roosta/yank/compare/v0.1.0...main
