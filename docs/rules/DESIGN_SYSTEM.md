# Design system — Material 3 / XML

This file defines the local design rules. Exact brand palette and typography were not established by the reviewed materials; do not invent a replacement for existing approved designs.

## Sources of truth
- `app/src/main/res/values/colors.xml`, `themes.xml`, `values-night/themes.xml`, `styles.xml`, `dimens.xml`, `strings.xml` (create missing files as needed).
- Documented theme: `Theme.Learngraphai` extends `Theme.Material3.DayNight.NoActionBar`; verify the actual name.
- Inspect the theme, welcome layouts, and existing resources. Use these as the visual baseline; no external design file is required. If no usable brand values exist, propose a palette for approval before applying it globally.
- Make shared design changes at the source of truth; do not override global themes for a single screen.

## Tokens and conventions
- Use `?attr/colorPrimary`, `?attr/colorOnPrimary`, `?attr/colorSurface`, `?attr/colorOnSurface`, `?attr/colorError`, and compatible theme roles. Additional tokens use `@color/...` with `values-night` variants when needed.
- No raw HEX values in layouts/Java/UI drawables; domain data such as `Subject.colorHex` is an exception, validated with a shared-token fallback. Schema default `#4F46E5` does not establish the brand color.
- Use the same semantic token across screens: `MASTERED` green, `FRAGILE` yellow, `GAP` red, `UNEXPLORED` neutral gray/white. Include text/icons/legends, not color alone.
- Centralize text appearances, text sizes, spacing, radii, elevation, and stroke widths; use `sp` for text and `dp` for dimensions. Structural values `0dp`, `match_parent`, `wrap_content`, and weights need no tokens.
- If no spacing system exists, propose 4/8/16/24/32dp, 16dp screen padding, and 12dp card radius. These are suggested defaults; prioritize existing values and user-approved designs. Do not change established onboarding dimensions without approval.
- Use Material text appearances. Name new styles by role (`Widget.LearnGraph.*`, `TextAppearance.LearnGraph.*`); reuse existing style names if different.
- Put app-wide or repeated UI strings and content descriptions in `strings.xml`. Keep fixed text used once on a single screen directly in its XML attribute or Java statement. Use resources for localization, formatting, and plurals; use plurals for quantities.

## Layouts and components
- Prefer MaterialButton, TextInputLayout/TextInputEditText, MaterialCardView, BottomNavigationView, and MaterialAlertDialogBuilder when dependencies are available.
- Do not replace MaterialButton backgrounds in ways that remove ripple/shape; use tint, shapeAppearance, and appropriate styles. Selectors cover pressed, disabled, and focused states.
- Use ConstraintLayout for complex layouts and LinearLayout for simple groups; avoid deep nesting. Prefer start/end over left/right; `tools:` is preview-only.
- Minimum touch target: 48dp. Provide meaningful labels/descriptions, sufficient light/dark contrast, and large-font support without clipping.
- Preserve image aspect ratios (`fitCenter` for slides, `centerCrop` for thumbnails when suitable). Do not load full-resolution bitmaps into lists.
- Provide clear loading/empty/error/content states and appropriate retry or CTA. Do not use Toast instead of inline validation errors.
- Test small screens, keyboards, insets, scrolling, orientation, and dark mode. Preserve approved UI outside task scope.
