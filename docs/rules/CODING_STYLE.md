# Coding style — Java 11 / XML

Sources: DEVELOPMENT_GUIDE and course topics 03, 04, 07. Preserve existing conventions; avoid mass renaming.

## Java
- Classes/interfaces: PascalCase; methods/variables: camelCase; constants: UPPER_SNAKE_CASE; packages: lowercase.
- Use responsibility-specific suffixes: Activity, Fragment, Adapter, Dao, Helper, Manager, Repository, Service, BottomSheet.
- View fields/IDs follow existing conventions: `btnLogin`, `tvTitle`, `edtEmail`, `ivAvatar`, `ibSearch`, `layoutDots`, `rootWelcome`, `lvGaps`, `gvSubjects`, `rvDocuments`, `viewPagerWelcome`.
- POJOs use private fields, explicit constructors, necessary getters/setters, `long` for SQLite IDs/timestamps, String for user IDs, wrappers for nullable values. Compare strings with equals, not ==.
- Separate methods by responsibility; prefer descriptive names, early returns, and centralized validation. Comments explain why, not what.
- Avoid magic numbers/strings for contracts, enums, intents/preferences, and states. Keep `KEY_*`, `EXTRA_*` with their owner or `util/Constants.java` according to existing code; column names belong in the DB contract/helper.
- No public mutable global state, static Activity references, empty catch blocks, or ignored nulls/errors. Do not introduce frameworks/abstractions merely to save a few lines.
- Use explicit imports, four-space indentation, and avoid formatting unrelated files. Split large classes by responsibility; there is no hard line limit for Java source.

## XML
- File/resource names use snake_case: activity_, fragment_, item_, component_, dialog_, bottom_sheet_, ic_, bg_, btn_, selector_. IDs use existing camelCase convention.
- Prefix shared string resources by feature (`auth_login_button`, `subject_empty_state`); styles/tokens follow DESIGN_SYSTEM. Fixed text used once on one screen may be written directly in that XML attribute or Java statement.
- Keep layouts readable and avoid duplicate blocks; declare width/height, complete constraints, and matching XML/Java IDs.
- Keep `values/dimens.xml` for dimensions reused across multiple screens or shared components. Write screen- or section-specific dimensions directly in the owning layout/drawable XML rather than making one-use dimension resources.
- Do not hardcode device paths, secrets, or colors. Use `strings.xml` for app-wide/shared text, localization, formatting, and plurals; keep one-use fixed screen text inline instead of creating a resource used nowhere else. No business logic in Views.

## Review
- Read similar files before creating new ones; check dead code, duplication, nulls, exceptions, lifecycle, and account data.
- Prefer simple code suitable for learners, without unnecessary boilerplate. Add regression tests for bug fixes when feasible.
