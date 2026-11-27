# Android architecture

This file defines the local architecture rules. The current entry flow uses `feature/welcome`, `feature/home`, `core/preferences`, and `core/ui`. Other packages below are planned boundaries, not claims that code exists.

## Layers
| Package | Responsibility |
|---|---|
| `feature/welcome`, `feature/home` | Existing Activities and welcome-specific custom view |
| `feature/<name>` | Future feature-specific Activities, Fragments, adapters, and UI state when implemented |
| `core/preferences`, `core/ui` | Existing entry preferences and system-bar inset helper |
| `core/components` | Future shared views/dialogs only when reused |
| `domain` | GraphMerger, RootCauseAnalyzer, TopologicalStudyPlanner; testable Java algorithms |
| `data/model` | Domain POJOs and enums |
| `data/local` | Future DatabaseHelper and FileStorageManager; neither exists yet |
| `data/local/dao` | UserDao, SubjectDao, DocumentScanDao, GraphDao, DiagnosticDao, StudySessionDao |
| `data/remote` and `data/remote/dto` | ApiClient, AuthService, AiService, request/response DTOs |
| `data/repository` | Local/remote coordination, transactions, cache, ownership |
| `core/concurrency`, `core/utils` | Future executors and utilities when consumed |

UI calls repositories/domain through clear interfaces when those layers exist; repositories call DAOs/services. Domain handles data and algorithms independently of Activities. DAOs must not depend on UI; adapters must not call networks. Do not create empty classes merely to match the diagram or impose MVVM/DI frameworks. Introduce a ViewModel for nontrivial asynchronous or retained screen state; the current entry flow does not need one.

## Activity/Fragment
- Follow `initViews()`, `setupData()`/`loadData()`, `setupAdapter()`, and `setupListeners()` where applicable.
- Keep `findViewById` consistent with existing code; do not migrate the entire project to View Binding without approval.
- Register new Activities in Manifest; set `exported=false` for internal screens. Add launcher/deep links only when necessary.
- Use explicit Intents, `EXTRA_*` keys in Constants or the owning screen, and pass IDs instead of large images/graphs via Bundle. Validate missing extras and invalid IDs.
- Use ActivityResultLauncher for new camera/picker/permission flows. Retain legacy `startActivityForResult` only for lab compatibility; do not spread deprecated APIs.
- Save IDs, current question, and selected answers in saved state; persist durable data through repositories/SQLite. Handle rotation, back stack, and process recreation.
- Use EdgeToEdge and WindowInsets consistently; avoid double system padding. Fragments release view references, listeners, and callbacks when their views are destroyed.
- Auth/logout must prevent back navigation into protected screens; replaying onboarding must not clear sessions or data.

## Concurrency
- `AppExecutors`: single disk thread, fixed network pool (source suggests 3), main Handler/Looper; check for an existing class before adding one.
- Run SQLite, file I/O, image compression, networking, and expensive algorithms in the background; render and notify adapters on the UI thread.
- Do not retain Activity/Fragment in singletons; use applicationContext where appropriate. Do not create a thread pool per click.
- Cancel or ignore stale callbacks; check lifecycle (Activity isFinishing/isDestroyed, Fragment view lifecycle) and the current screen ID.
- Disable in-flight submission, prevent stale responses overwriting newer state, and return actionable errors/retry rather than swallowing exceptions.

## Local-first
- SQLite is the source of saved learning data; networking adds AI results and account services. Offline does not imply new AI or OTP requests work.
- Use transactions for multi-table operations; display success only after commit. Do not introduce cloud sync without a contract.
- Separate API DTOs from DB models to prevent overwriting local IDs/mastery when importing AI results.
