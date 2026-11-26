# LearnGraph AI — Overview

## Product direction
- LearnGraph AI, native Android, package `com.ptithcm.learngraphai`.
- Mobile Application Development coursework project. Audience: university students and self-learners, especially subjects with prerequisite knowledge.
- Addresses fragmented materials, illusions of competence, and unfocused revision. Turns slides/notes into knowledge graphs, diagnoses gaps, traces root causes, and generates recovery sessions.
- Journey: Onboarding → Login/Guest → create subject → capture/select image → AI extraction → preview/edit → save graph → quiz → heatmap → study session → reassessment.
- Local-first: access saved materials, graphs, history, and questions offline; new AI requests and cloud authentication require networking.

## Agreed stack (documented baseline; verify repository)
| Component | Value |
|---|---|
| Java / UI | Java 11, XML Views, Material 3 DayNight NoActionBar |
| AGP | 9.3.3 via version catalog |
| SDK | compile/target 37, min 27 |
| Version | code 1, name 1.0 |
| Reported libraries | AppCompat 1.8.0, Material 1.14.0, activity-ktx 1.13.0, ConstraintLayout 2.2.2, ViewPager2/RecyclerView per dependency tree, JUnit 4.13.2, AndroidX test ext 1.3.0, Espresso 3.7.0 |
| Storage | SQLiteOpenHelper + DAO, SharedPreferences, internal/cache files |
| AI | Gemini multimodal structured JSON or equivalent; deployment model unresolved |
| Network | Retrofit/OkHttp/Gson proposed, not confirmed installed |

Inspect `app/build.gradle`, `gradle/libs.versions.toml`, wrapper, Manifest, and actual theme. Do not change versions merely because they differ from the snapshot. FastAPI/Node/Firebase backend runtime options are proposals. Do not install every planned library by default.

## Roadmap
- P0: local data foundation, auth/guest, subject CRUD, scanner, basic graph.
- P1: diagnostic, root-cause, heatmap, search, settings.
- P2: micro-session, tutor, progress, spaced repetition, notifications.
- P3: cloud sync, Google Sign-In, multipage PDF, export/flashcards.

Functional details: `docs/project/FEATURES.md`; snapshot: `docs/project/STATUS.md`; decisions: `docs/project/DECISIONS.md`.

## Design and task management
- Use the implemented Android resources and the local `docs/rules/DESIGN_SYSTEM.md` as the design baseline. Previously approved visual designs may be supplied by the user, but no external design file or service is required.
- Track verified implementation progress in `docs/project/STATUS.md`. No external task tracker is required.
