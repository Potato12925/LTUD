# Implemented component catalog

This catalog reflects the checked-out app after the 27 November 2026 architecture refactor. Read the source before reusing a component. No database helper, DAO, repository, ViewModel, Fragment, adapter, or generic base class is implemented yet.

| Component | Source | Responsibility and current consumer | API / use | Dependencies and limits |
|---|---|---|---|---|
| `AppPreferences` | `app/src/main/java/com/example/ltud/core/preferences/AppPreferences.java` | Persists the explicit guest choice; used by `WelcomeActivity` | `new AppPreferences(context).isGuestSelected()` and `selectGuest()` | Android `SharedPreferences`; retains existing `learngraph_entry/guest_selected` data. Does not represent authenticated sessions or the planned `learngraph_prefs` settings. |
| `SystemBarInsets` | `app/src/main/java/com/example/ltud/core/ui/SystemBarInsets.java` | Adds system bar insets to the initial XML padding; used by `WelcomeActivity` and `MainActivity` | `SystemBarInsets.applyToPadding(rootView)` after `setContentView` | AndroidX Core `ViewCompat`; intended for root views that need system-bar padding. Call once per view; does not handle custom IME inset policies. |
| `KnowledgeOrbView` | `app/src/main/java/com/example/ltud/feature/welcome/KnowledgeOrbView.java` | Draws the animated and draggable welcome illustration; used in `activity_welcome.xml` | XML tag with the full class name | Feature-specific Canvas view, not a general graph viewer; animation is cancelled on detach. |
| Welcome feature row | `app/src/main/res/layout/item_welcome_feature.xml` | Displays one title, description, and symbol in the welcome flow; inflated by `WelcomeActivity` | Set `tvFeatureIcon`, `tvFeatureTitle`, `tvFeatureDescription` after inflation | Feature-specific; does not provide a generic card API. |
| `Theme.LTUD` | `app/src/main/res/values/themes.xml`, `app/src/main/res/values-night/themes.xml` | Material 3 DayNight theme used by the application | `@style/Theme.LTUD` in Manifest | Reuse theme attributes and `values/colors.xml` tokens; onboarding palette is current visual baseline. |

## Existing screen owners

- `feature/welcome/WelcomeActivity.java` owns three-step navigation, dots, guest entry, and `EXTRA_FORCE_SHOW` replay. Its step content is feature-specific.
- `feature/home/MainActivity.java` owns the current guest placeholder and replay button.

## Before adding a component

Search the catalog and actual source, then decide whether reuse, configuration, or a small extraction serves the current feature. Keep one-use views and text with their screen. Add a catalog row only after an implementation has a consumer and has been checked.
