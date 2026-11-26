# Architecture decisions and unresolved items

## Accepted ADRs (documented baseline, 2026-11)
| ID | Decision | Rationale |
|---|---|---|
| ADR-001 | Java 11, XML Views, Material 3 | Coursework and existing code alignment; no Kotlin/Compose |
| ADR-002 | SQLiteOpenHelper + DAO, no Room | Direct SQLite practice and graph query control |
| ADR-003 | Local-first, cloud account or Guest `guest_local` | Offline use without mandatory account at first launch |
| ADR-004 | Graph adjacency tables concept_nodes/concept_edges | Local queries and Java graph traversal |
| ADR-005 | Multimodal LLM structured JSON + canonical key | Merge slides and avoid duplicate concepts |
| ADR-006 | WebView Markdown/KaTeX; graph WebView or Canvas | Formula rendering and pan/zoom interaction |

## Conflicts and decisions still needed
| Issue | Agent handling until resolved |
|---|---|
| Earlier concept proposal used Kotlin/Compose/Room | Accepted ADRs specify Java/XML/SQLiteOpenHelper; do not migrate |
| Default guest_local/true in prefs vs explicit guest/login choice | Do not bypass login; agree on representation of unselected guest/session state |
| `users.auth_provider` descriptive table defaults to LOCAL, DDL defaults to GUEST | Preserve reference DDL GUEST; verify before implementation/migration |
| Lesson UI example 5/15/30 minutes vs specification 10/15/20 | Follow project specification 10/15/20, default 15 |
| Earlier course examples used different module numbering | Use the nine-module specification in `docs/project/FEATURES.md` |
| Graph DAG but PART_OF/RELATED may not be acyclic | Propose enforcing DAG only for PREREQUISITE; confirm before extending |
| GraphMerger: multiple documents per concept but schema only has document_id | Preserve first source; many-to-many mapping needs approved migration |
| Mastery/coverage | Thresholds, coverage rounding, and zero-node policy unspecified; confirm before implementation |
| Spaced repetition/streak | Specific algorithm/schedule unspecified |
| OTP verification/resend, refresh, logout, change/delete account, sync | The local API contract has no complete definitions; do not present fake endpoints as real |
| Profile year of study is in features but not DB/API | Do not add a column without agreeing on the contract |
| Guest-to-account upgrade | Merge/conflict policy unresolved; do not automatically reassign all data to another user |
| Gemini model, network stack, secure token library | Unconfirmed proposals; verify compatibility and obtain approval before adding dependencies |
| Exact palette, typography, shape | Exact values were not specified in the reviewed materials. Use repository resources; fallback spacing is a proposed rule |
| Scan status during unsaved preview; retry PROCESSING after process death | Agree on transitions if needed; COMPLETED only after commit; retain image for retry |
| API session steps camelCase vs DB description snake_case JSON | Agree on local mapping/format; do not silently change existing contracts |

Safety precautions, component registry, document routing, fallback spacing, and testing checklists in this rule set are proposed maintenance conventions, not claims that the team has approved every detail.

## New ADR template
- ID, date, status (Proposed/Accepted/Superseded), approver if available.
- Context, alternatives, decision, trade-offs, migration/API/UI/test impact, documents to update.
- Do not mark an ADR Accepted without confirmation. Split by topic when approaching 300 lines.
