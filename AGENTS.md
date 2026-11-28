# LearnGraph AI — Agent Instructions

Applies to the entire repository. Read this file before each task. All required documentation is included in this standalone rule set. Paths below are relative to the repository root. Load only relevant documents. No earlier Markdown file, external rule set, or external workspace is required.

## 1. Non-negotiable constraints
- LearnGraph AI, namespace/applicationId `com.example.ltud`.
- Native Android Java 11 + XML Views + Material 3; do not migrate to Kotlin, Compose, or Room.
- Local-first, SQLiteOpenHelper + DAO, SharedPreferences for lightweight settings, Internal Storage for images; AI/network through the data layer.
- Preserve existing Gradle configuration, themes, contracts, screens, and data. Preserve package organization unless a task explicitly requests refactoring it. Inspect the actual repository before editing; do not upgrade SDKs, add dependencies, or change schemas/APIs without approval.
- UI must not directly perform SQL, networking, graph algorithms, or image processing; never block the main thread.
- Do not hardcode colors or shared dimensions in screens. Keep `dimens.xml` limited to dimensions reused across multiple screens or shared components. Put dimensions specific to one screen or section directly in that screen's owning XML layout/drawable. Put all user-visible UI text, including one-use labels, messages, accessibility descriptions, and preview text, in `strings.xml`; reference it from XML or Java. Use string arrays for related fixed text, formatted strings for variable values, and plurals for quantities. Reuse resources and components before creating new ones.
- Never store plaintext passwords/tokens, commit or log secrets, bypass authentication, or present simulated AI results as real data.
- Do not delete data, reset Git, overwrite others' changes, commit, or push unless requested.
- Each Markdown rule file must contain at most 300 lines, including blank lines. Split by topic if necessary and update the routing table without losing contract details.

## 2. Required task-specific reading
Always read `docs/project/OVERVIEW.md` and `docs/project/STATUS.md`; for code changes, also read `docs/rules/CODING_STYLE.md` and `docs/rules/TESTING.md`. Read every matching row below, not just the first one.
Before creating a class, layout, drawable, or shared resource, read `docs/project/COMPONENT_CATALOG.md` and inspect the listed source. Reuse or configure an existing implementation when it fits; extract meaningful duplication without building unused abstractions.
For implementation work, also read every matching row in the pattern table below and follow its agent-facing implementation sequence. Pattern examples are not evidence that their classes exist; verify source before copying them.

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
| Any new or changed shared component | `docs/project/COMPONENT_CATALOG.md`, `docs/rules/COMPONENTS.md`, `docs/rules/CODING_STYLE.md` |
| Refactoring or package moves | `docs/rules/ARCHITECTURE.md`, `docs/project/COMPONENT_CATALOG.md`, `docs/rules/TESTING.md` |

| Implementation task | Additional required pattern |
|---|---|
| Activity or navigation | `docs/patterns/ACTIVITY_PATTERN.md` |
| Fragment or view lifecycle | `docs/patterns/FRAGMENT_PATTERN.md` |
| ListView, GridView, RecyclerView, or adapter | `docs/patterns/ADAPTER_PATTERN.md` |
| SQLite helper, contract, DAO, or local persistence | `docs/patterns/DAO_PATTERN.md` |
| Repository or coordination between sources | `docs/patterns/REPOSITORY_PATTERN.md` |
| ViewModel or retained asynchronous screen state | `docs/patterns/VIEWMODEL_PATTERN.md` |
| New or extended CRUD feature | `docs/patterns/CRUD_FEATURE_PATTERN.md` and its linked layer patterns |
| New or changed UI component or layout | `docs/patterns/UI_COMPONENT_PATTERN.md` |
| Database schema or version change | `docs/patterns/DATABASE_MIGRATION_PATTERN.md` |

## 3. Sources of truth and conflicts
- Explicit user-confirmed requirements and accepted ADRs define scope; running code/configuration is evidence of implementation, not proof that every deviation is valid.
- `FEATURES` defines behavior; `DATABASE_CONTRACT` and `API_CONTRACT` define planned contracts; `DESIGN_SYSTEM` defines resource usage. Actual visual values come from the repository and approved designs.
- Documentation describes designs and snapshots, not verified working features. If code and docs disagree, report the discrepancy, preserve compatibility, and do not silently change contracts. Read unresolved items in `docs/project/DECISIONS.md`.
- Use only this rule set as the project documentation baseline. Never require deleted legacy Markdown files or retrieve external rules. If information is missing, inspect the implementation, record the uncertainty, and request a decision when needed.

## 4. Workflow for every task
1. Identify scope and acceptance criteria; read relevant rules and implementation patterns from the tables above. Do not assume that additional instruction files exist.
2. Inspect the component catalog and its source, then verify git status, project structure, Gradle, Manifest, related source/resources/DAOs/adapters, and reuse opportunities. Check whether an equivalent class or resource already exists before creating one.
3. For major changes or conflicts affecting contracts, report the issue and request a decision; for minor changes, choose the smallest suitable solution.
4. Reuse existing components and implement only missing functionality. Extract shared infrastructure when actual consumers justify it. Follow layer and coding conventions; handle errors, empty/loading states, lifecycle, and offline behavior where applicable. Avoid unrelated changes.
5. Review the diff and check changed code for obvious errors. Build and test the implementation according to `docs/rules/TESTING.md` with the smallest feasible checks; report any tooling limits. Never disable checks to conceal errors.
6. Update `docs/project/COMPONENT_CATALOG.md` for new shared components and STATUS for verified changes; update contracts/ADRs only when agreed. Do not accumulate lengthy history in rule files.
   Keep the catalog limited to implemented components and record consumers, public APIs, and limitations.
7. Respond in Vietnamese unless the user requests another language: summarize changes, files, checks actually performed, unrun tests, limitations, and open decisions. Keep repository rule documents in English.

## 5. Definition of Done
- Readable code, no duplicated logic/resources, preserved existing flows, and no secrets.
- Review changed code and report the checks actually performed. Automated build, lint, unit, and device tests are optional unless the user requests them; identify unrun checks and leave functional testing to the user.
- UI uses shared themes/tokens, valid states, and light/dark support as designed.
- Never mark a feature complete based solely on UI mocks, fake APIs, or unverified behavior.
