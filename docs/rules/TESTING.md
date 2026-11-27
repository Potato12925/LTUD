# Build, testing, Git, and delivery

## Before editing
- Read Gradle wrapper, the actual app build file (`app/build.gradle` or `app/build.gradle.kts`), gradle/libs.versions.toml, Manifest, and git status; determine actual modules/tasks.
- Source specifies Java source/target 11; use a Gradle daemon JDK compatible with AGP (verify wrapper/IDE; do not assume JDK 11 can run AGP).
- Do not upgrade SDK/AGP, change signing, add libraries, or run clean unnecessarily.

## Default verification
- Review the changed files and diff for syntax, XML/resource references, imports, types, null handling, lifecycle, and obvious logic errors relevant to the task.
- Run only a quick, feasible check when it materially helps catch errors in the changed code. A focused compile/build is appropriate when available without a long wait. Do not keep retrying slow or unavailable tooling.
- Build, lint, unit, instrumented, and full regression suites are not mandatory by default. Run broader checks when the user explicitly requests them or a concrete risk warrants them.
- Leave device, emulator, visual, and end-to-end behavior testing to the user unless requested. Never claim that code review or a passing build proves runtime behavior.
- For optional checks, stop if tooling is unavailable or takes too long; note why and report the remaining manual checks. If the user explicitly requests a test, complete it or report the concrete blocker. Do not disable checks to hide failures.

## Optional Windows PowerShell commands (root)
```powershell
.\gradlew.bat assembleDebug
.\gradlew.bat testDebugUnitTest
.\gradlew.bat lintDebug
# With an emulator/device when requested:
.\gradlew.bat connectedDebugAndroidTest
```
macOS/Linux uses `./gradlew` with corresponding tasks. These are examples, not a required sequence. Run `installDebug` when the user wants device installation/testing. A passing build is not a passing UI test. Actual variant/flavor tasks may differ.

## Scope-specific testing when requested or warranted
- JVM JUnit: validation, enum mapping, graph merge logic, traversal/topological sort, mastery policy, DTO parsing; null/empty/invalid cases.
- Instrumented: SQLite fresh creation, upgrade, CRUD, FK cascades, transactions, user/subject isolation; Espresso UI as needed.
- Manual UI: small screen, large fonts, light/dark, back, rotation, keyboard, loading/empty/error, retry, disabled states, accessibility.
- Auth: first launch, replay, guest, remember session, expired token, logout/back, account isolation, OTP failure.
- Scanner: camera/gallery cancellation, denied permission, missing/large files, offline, retry, preview editing, single save.
- API: labeled fake servers/fixtures, timeout, 401, 413, 422, 429, 503, malformed JSON. Do not call paid services in automated tests without permission.
- Docs-only: check paths, internal links, conflicts, and the 300-line limit; Android build is unnecessary if code did not change.

## Git
- Keep main stable; team branches `feature/<module>` or `fix/<issue>`. Do not switch branches, commit, or push without a request.
- Conventional commits `feat(scanner): ...`, `fix(graph): ...`, `docs: ...`. Exclude secrets, local.properties, build outputs, and IDE caches.
- Do not revert others' changes. Review git diff and remove unrelated edits.

## Final task report
- Summarize changes and main files; identify reused components.
- List actual checks and commands with pass/fail/not run results. State why important checks were not run and what the user should test manually. Never claim unrun tests passed.
- Report mocks, missing backend, migrations, and device limitations; update STATUS based on evidence and contracts/ADRs only when approved.
