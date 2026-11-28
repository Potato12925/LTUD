# UI component pattern: instructions for a coding agent

## Apply this pattern
Use when adding or changing a layout, drawable, style, row, custom view, or shared UI behavior. Implemented reusable pieces are recorded in `COMPONENT_CATALOG.md`: `SystemBarInsets`, `KnowledgeOrbView` (welcome-specific), the welcome feature row, and `Theme.LTUD`. Inspect their actual source plus `DESIGN_SYSTEM.md` and `COMPONENTS.md` before creating a resource.

## Choose the least new code
1. If existing theme attributes, drawable, style, or layout can express the desired UI, configure/reuse it. Preserve approved welcome appearance and support DayNight.
2. For a one-screen section, keep XML and dimensions in the owning screen/layout. For repeated static groups with the same structure, extract `component_<name>.xml` and `<include>` it. For repeated behavior across real consumers, create `core/components/<Name>View.java` with a small data/callback API. Keep a feature-only custom view in `feature/<feature>`.
3. For list/grid rows, use `item_<entity>.xml` with its feature-specific adapter. Do not create a generic component until actual consumers have the same visual and interaction contract.

## Build the component
- Set layout size, padding, constraints, and focus/touch behavior in XML. Put **all** visible and accessibility text, including one-use and preview copy, in `strings.xml`; use arrays, formats, and plurals where appropriate. Do not hardcode UI colors in Java/XML; use `?attr/...` or existing `@color` resources with night variants.
- Put a dimension in `dimens.xml` only if it is reused across screens/shared components. Keep one-screen/section dimensions in the owning XML. Use `dp` for size and `sp` for text.
- Expose callbacks rather than capturing an Activity. A custom view renders data and emits actions; it does not own navigation, DAO, network, or image processing. Stop animation and detach callbacks when its view lifecycle ends, following `KnowledgeOrbView` where applicable.
- Apply `SystemBarInsets.applyToPadding(root)` once for a root requiring system-bar padding. Do not stack a second inset listener on the same view without a deliberate policy; handle IME separately.

## Mechanism for repeated static content (uncompiled reference)
```xml
<!-- Only create component_empty_state.xml when real screens reuse it. -->
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent" android:layout_height="wrap_content"
    android:orientation="vertical" android:padding="16dp">
    <TextView android:id="@+id/tvEmptyMessage"
        android:layout_width="match_parent" android:layout_height="wrap_content"
        android:text="@string/common_empty_message"
        android:textColor="?attr/colorOnSurface" />
</LinearLayout>
```
If a screen needs different copy, set its TextView from a feature-specific string resource in the host. Do not make a custom Java class merely to pass a string.

## Verify and register
Check loading/empty/error/content variants that the host renders, 48dp touch targets, large text, accessibility descriptions, contrast, light/dark, small screen, keyboard, and insets. Build resource changes; visually inspect when a device is available. Once a shared implementation has real consumers, record its source, public API, consumers, and limits in the catalog. Do not catalog a pattern or future example as implemented.

