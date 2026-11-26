# Documentation scope and authority

This is a standalone English documentation baseline for LearnGraph AI. It consolidates previously reviewed project and course information. No earlier Markdown documents, external rule files, external links, or remote workspaces are required.

## Local documentation map
| Topic | Authoritative file |
|---|---|
| Agent workflow and routing | `AGENTS.md` |
| Product and stack | `docs/project/OVERVIEW.md` |
| Functional requirements | `docs/project/FEATURES.md` |
| SQLite schema and storage keys | `docs/project/DATABASE_CONTRACT.md` |
| REST and AI data contracts | `docs/project/API_CONTRACT.md` |
| Accepted and unresolved decisions | `docs/project/DECISIONS.md` |
| Implementation evidence | `docs/project/STATUS.md` |
| Course alignment | `docs/project/COURSE_MAPPING.md` |
| Implementation rules | `docs/rules/` (see AGENTS.md routing table) |

## Limits
- The baseline is based on project documentation reviewed on 26 November 2026, not a direct audit of the Android repository or backend.
- Exact palette values, missing API endpoints, mastery thresholds, and other unresolved details must not be invented. Use existing resources and the decisions document.
- Source code, Gradle configuration, and Android resource files are implementation evidence, not external rule dependencies.
- Keep this documentation self-contained when adding features. If new requirements are confirmed, incorporate them into the relevant local document rather than requiring a separate external file.
