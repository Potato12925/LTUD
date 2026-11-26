# LearnGraph AI — Agent Instructions

Applies to the entire repository. Read this file before each task. All required documentation is included in this standalone rule set. Paths below are relative to the repository root. Load only relevant documents. No earlier Markdown file, external rule set, or external workspace is required.

## 1. Non-negotiable constraints
- LearnGraph AI, namespace/applicationId `com.example.ltud`.
- Native Android Java 11 + XML Views + Material 3; do not migrate to Kotlin, Compose, or Room.
- Local-first, SQLiteOpenHelper + DAO, SharedPreferences for lightweight settings, Internal Storage for images; AI/network through the data layer.
- Preserve existing Gradle configuration, packages, themes, contracts, screens, and data. Inspect the actual repository before editing; do not upgrade SDKs, add dependencies, or change schemas/APIs without approval.
- UI must not directly perform SQL, networking, graph algorithms, or image processing; never block the main thread.
- Do not hardcode colors or shared dimensions in screens. Write fixed, screen-specific UI text directly in its one-use XML attribute or Java statement; do not create a `strings.xml` entry for text used only once on one screen. Use `strings.xml` for app-wide text, text reused in multiple places, and text that needs localization or formatting/plural rules. Reuse resources and components before creating new ones.
- Never store plaintext passwords/tokens, commit or log secrets, bypass authentication, or present simulated AI results as real data.
- Do not delete data, reset Git, overwrite others' changes, commit, or push unless requested.
- Each Markdown rule file must contain at most 300 lines, including blank lines. Split by topic if necessary and update the routing table without losing contract details.

## 2. Required task-specific reading
Always read `docs/project/OVERVIEW.md` and `docs/project/STATUS.md`; for code changes, also read `docs/rules/CODING_STYLE.md` and `docs/rules/TESTING.md`. Read every matching row below, not just the first one.

| Task | Additional required files |
|---|---|
| Features, flows, navigation | `docs/project/FEATURES.md`, `docs/rules/ARCHITECTURE.md` |
| UI, layouts, colors, themes, components | `docs/rules/DESIGN_SYSTEM.md`, `docs/rules/COMPONENTS.md`, `docs/project/FEATURES.md` |
| Activity, Fragment, Adapter, Intent, lifecycle | `docs/rules/ARCHITECTURE.md`, `docs/rules/COMPONENTS.md` |
| SQLite, DAO, models, file storage, preferences | `docs/rules/DATA.md`, `docs/project/DATABASE_CONTRACT.md`, `docs/rules/ARCHITECTURE.md` |
| Auth, sessions, permissions, camera, WebView | `docs/rules/SECURITY.md`, `docs/rules/DATA.md`, `docs/project/API_CONTRACT.md` |
| API, Gemini, JSON, networking | `docs/rules/SECURITY.md`, `docs/project/API_CONTRACT.md`, `docs/rules/ARCHITECTURE.md` |
| Scan, graph, diagnostic, study, review | `docs/rules/GRAPH_AND_LEARNING.md`, `docs/project/FEATURES.md`, `docs/project/DATABASE_CONTRACT.md`, `docs/project/API_CONTRACT.md` |
| Architecture, stack, schema, or contract changes | `docs/project/DECISIONS.md`, and relevant files |
| Build, Git, environment, testing | `docs/rules/TESTING.md`, `docs/project/DECISIONS.md` |
| Course requirement alignment | `docs/project/COURSE_MAPPING.md` |

## 3. Sources of truth and conflicts
- Explicit user-confirmed requirements and accepted ADRs define scope; running code/configuration is evidence of implementation, not proof that every deviation is valid.
- `FEATURES` defines behavior; `DATABASE_CONTRACT` and `API_CONTRACT` define planned contracts; `DESIGN_SYSTEM` defines resource usage. Actual visual values come from the repository and approved designs.
- Documentation describes designs and snapshots, not verified working features. If code and docs disagree, report the discrepancy, preserve compatibility, and do not silently change contracts. Read unresolved items in `docs/project/DECISIONS.md`.
- Use only this rule set as the project documentation baseline. Never require deleted legacy Markdown files or retrieve external rules. If information is missing, inspect the implementation, record the uncertainty, and request a decision when needed.

## 4. Workflow for every task
1. Identify scope and acceptance criteria; read the required files from this rule set. Do not assume that additional instruction files exist.
2. Inspect git status, project structure, Gradle, Manifest, related code/resources/DAOs/adapters, and reuse opportunities.
3. For major changes or conflicts affecting contracts, report the issue and request a decision; for minor changes, choose the smallest suitable solution.
4. Implement according to layer and coding conventions; handle errors, empty/loading states, lifecycle, and offline behavior where applicable. Avoid unrelated changes.
5. Review the diff and check changed code for obvious errors. Follow `docs/rules/TESTING.md` for the smallest feasible automated check; do not run exhaustive tests by default. Never disable checks to conceal errors.
6. Update the registry for new components and STATUS for verified changes; update contracts/ADRs only when agreed. Do not accumulate lengthy history in rule files.
7. Respond in Vietnamese unless the user requests another language: summarize changes, files, checks actually performed, unrun tests, limitations, and open decisions. Keep repository rule documents in English.

## 5. Definition of Done
- Readable code, no duplicated logic/resources, preserved existing flows, and no secrets.
- Review changed code and report the checks actually performed. Automated build, lint, unit, and device tests are optional unless the user requests them; identify unrun checks and leave functional testing to the user.
- UI uses shared themes/tokens, valid states, and light/dark support as designed.
- Never mark a feature complete based solely on UI mocks, fake APIs, or unverified behavior.
