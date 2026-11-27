# Project status

## Verified architecture — 27 November 2026

- Current source packages: `feature/welcome` (`WelcomeActivity`, `KnowledgeOrbView`), `feature/home` (`MainActivity`), `core/preferences` (`AppPreferences`), and `core/ui` (`SystemBarInsets`). Manifest and welcome XML refer to the relocated classes.
- `AppPreferences` retains the installed `learngraph_entry/guest_selected` preference. Both Activities use `SystemBarInsets` for system bar padding.
- No SQLite helper, DAO, repository, ViewModel, Fragment, or adapter exists in the current source. The actual reuse inventory is `docs/project/COMPONENT_CATALOG.md`; no schema or dependency changed.
- `assembleDebug`, `testDebugUnitTest`, and `lintDebug` succeeded after the refactor. Lint reported warnings, including an existing `clipToOutline` API-level warning in the welcome layout. Device navigation, process recreation, and visual behavior after the package move remain unverified.

## Verified repository state — 26 November 2026

- The checked-out app was an Android Studio template in `com.example.ltud`, with one `MainActivity` showing “Hello World”. The user confirmed retaining `com.example.ltud`; the planned namespace in AGENTS.md and older snapshot do not match this checkout.
- Implemented a native, three-step welcome screen from the supplied Stitch design in `WelcomeActivity`, `KnowledgeOrbView`, `activity_welcome.xml`, and `item_welcome_feature.xml`. It has next/back/skip/dots, restores the step after rotation, and stores an explicit guest choice in SharedPreferences. `MainActivity` offers replay. The login button reports that authentication is unavailable.
- The hero is a local Canvas illustration. GPA scores, streaks, AI chat messages, and other sample figures in the Stitch HTML were not imported as real data. The post-onboarding screen remains a guest placeholder; dashboard, auth, data, and AI features are not implemented.
- `KnowledgeOrbView` now animates a deterministic multicolor 3D particle sphere, draws precomputed synapse connections, and responds to horizontal and vertical drag. Its animator is cancelled when detached. Build and runtime checks for this animation revision were not run at the user's request.
- Refined the orb to follow the user's supplied Three.js sample: 2,100 spherical particles with soft halos, 750 nearby-node links, a pulsing nebula core, and continuous drag/auto rotation. Build and runtime checks remain unrun at the user's request.
- Enlarged the rendered orb diameter and reduced the particle count to 1,200 at the user's request; kept the existing links and animation. Build and runtime checks were not run.
- Updated string-resource guidance: one-use fixed text is inline; app-wide/reused and localizable text stays in resources. Applied this to the existing MainActivity/WelcomeActivity layouts and WelcomeActivity code; `strings.xml` now retains only app name and the repeated LearnGraph brand. No build or tests were run.
- Updated dimension guidance (27 November 2026): `dimens.xml` is reserved for values reused across screens/components. Screen-specific dimensions now live in their owning layout/drawables; the fixed three-step indicators are declared in `activity_welcome.xml`. No build or tests were run.
- `assembleDebug`, `testDebugUnitTest`, and `lintDebug` passed before the final sticky-action layout edit; `assembleDebug` passed again after that edit. On an emulator, the first welcome screen rendered, skip opened step 3, explicit guest selection opened `MainActivity`, and relaunch bypassed onboarding. The user requested stopping further checks before a final lint rerun or broader visual review.

The older baseline below was documented on 26 November 2026 without repository verification. The inventory above is the current implementation evidence.

| Area | Snapshot status | Evidence to inspect |
|---|---|---|
| Gradle/SDK/theme | Verified in current checkout | `app/build.gradle.kts`, version catalog, Manifest, themes |
| M1 onboarding | Three-step welcome implemented; feature contract describes four planned steps | `feature/welcome/WelcomeActivity.java`, `activity_welcome.xml` |
| Preferences | Explicit guest flag implemented | `core/preferences/AppPreferences.java` |
| MainActivity | Guest placeholder and replay implemented | `feature/home/MainActivity.java` |
| M0 auth/profile | Planned | auth/profile, backend/session |
| M1 entry auth routing | Incomplete per source | navigation/Manifest, welcome/main |
| M2 subject/dashboard | Planned | DAO, UI, tests |
| M3 scanner/AI | Planned | FileProvider, storage, AiService |
| M4 graph | Planned | GraphMerger, DAO, viewer |
| M5 diagnostic | Planned | RootCauseAnalyzer, attempts |
| M6 study/tutor | Planned | planner, session, service |
| M7 review and M8 settings | Planned | UI and persistence |
| SQLite, Models, Repository, Remote | Absent in current source | inspect `app/src/main/java/com/example/ltud/` |

## How to update
- After inspecting the repository, update status to `verified`, `partial`, `planned`, or `blocked`, with path/test evidence and date; do not duplicate commit history.
- UI mocks do not establish completion. A passing build does not prove backend/UI functionality.
- The next task depends on the user's request; P0 is a default roadmap recommendation, not permission to implement everything.

## Latest test results
- `assembleDebug` passed before and after the package refactor on 27 November 2026; `testDebugUnitTest` and `lintDebug` passed afterward with lint warnings.
- Blockers/known issues: see `docs/project/DECISIONS.md` for unresolved contracts; do not infer the current app is broken.
