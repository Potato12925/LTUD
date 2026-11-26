# Project status

## Verified repository state — 26 November 2026

- The checked-out app was an Android Studio template in `com.example.ltud`, with one `MainActivity` showing “Hello World”. The user confirmed retaining `com.example.ltud`; the planned namespace in AGENTS.md and older snapshot do not match this checkout.
- Implemented a native, three-step welcome screen from the supplied Stitch design in `WelcomeActivity`, `KnowledgeOrbView`, `activity_welcome.xml`, and `item_welcome_feature.xml`. It has next/back/skip/dots, restores the step after rotation, and stores an explicit guest choice in SharedPreferences. `MainActivity` offers replay. The login button reports that authentication is unavailable.
- The hero is a local Canvas illustration. GPA scores, streaks, AI chat messages, and other sample figures in the Stitch HTML were not imported as real data. The post-onboarding screen remains a guest placeholder; dashboard, auth, data, and AI features are not implemented.
- `KnowledgeOrbView` now animates a deterministic multicolor 3D particle sphere, draws precomputed synapse connections, and responds to horizontal and vertical drag. Its animator is cancelled when detached. Build and runtime checks for this animation revision were not run at the user's request.
- `assembleDebug`, `testDebugUnitTest`, and `lintDebug` passed before the final sticky-action layout edit; `assembleDebug` passed again after that edit. On an emulator, the first welcome screen rendered, skip opened step 3, explicit guest selection opened `MainActivity`, and relaunch bypassed onboarding. The user requested stopping further checks before a final lint rerun or broader visual review.

Documented baseline dated 26 November 2026. The repository was not accessed and no Android build was run when creating this rule set. Do not treat the table as current implementation evidence.

| Area | Snapshot status | Evidence to inspect |
|---|---|---|
| Gradle/SDK/theme | Initialized | app/build.gradle, version catalog, Manifest, themes |
| M1 onboarding | Four-step core present | ui/welcome/WelcomeActivity, WelcomeAdapter, WelcomeItem, corresponding layouts |
| Preferences | Onboarding flag present | data/local/PreferencesManager.java, util/Constants.java |
| MainActivity | Placeholder, logo and replay welcome | ui/main/MainActivity.java |
| M0 auth/profile | Planned | auth/profile, backend/session |
| M1 entry auth routing | Incomplete per source | navigation/Manifest, welcome/main |
| M2 subject/dashboard | Planned | DAO, UI, tests |
| M3 scanner/AI | Planned | FileProvider, storage, AiService |
| M4 graph | Planned | GraphMerger, DAO, viewer |
| M5 diagnostic | Planned | RootCauseAnalyzer, attempts |
| M6 study/tutor | Planned | planner, session, service |
| M7 review and M8 settings | Planned | UI and persistence |
| SQLite, Models, Repository, Remote | Absent per snapshot | inspect data/ |

## How to update
- After inspecting the repository, update status to `verified`, `partial`, `planned`, or `blocked`, with path/test evidence and date; do not duplicate commit history.
- UI mocks do not establish completion. A passing build does not prove backend/UI functionality.
- The next task depends on the user's request; P0 is a default roadmap recommendation, not permission to implement everything.

## Latest test results
- No repository verification results in this rule set.
- Blockers/known issues: see `docs/project/DECISIONS.md` for unresolved contracts; do not infer the current app is broken.
