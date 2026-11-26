# Graph engine and learning behavior

This file defines graph and learning rules. Read with `docs/project/DATABASE_CONTRACT.md` and `docs/project/API_CONTRACT.md`.

## Scan-to-graph
1. Receive camera/gallery images, compress and save locally; scan record PENDING/PROCESSING.
2. AiService sends subjectName, documentTitle, image, mimeType, and existingNodes `{canonicalKey,title}` according to the API.
3. Parse structured JSON, validate nodes/edges, types and reasonable limits; display Markdown/LaTeX and let users edit/remove noisy concepts.
4. Only after confirmation, GraphMerger merges within a transaction, updates scan to COMPLETED and coverage. On failure, roll back, set an appropriate state, and retain the image for retry.
5. Do not assume AI results are correct or fabricate edges/concepts to fill gaps.

## Graph invariants
- Nodes are unique by `(subject_id, canonical_key)`; normalize consistently (lowercase, accent-free, snake_case). Do not use titles as IDs; collisions may require user correction rather than merging different concepts.
- `source -> target` for PREREQUISITE means source is the prerequisite, target is dependent. Trace backward through incoming edges. PART_OF/RELATED are not prerequisites by default.
- PREREQUISITE subgraph must be a DAG. Detect cycles, self-loops, missing endpoints, and cross-subject links before commit. Do not assume the entire RELATED graph is acyclic. Deduplicate by source/target/type; weight 0..1.
- Merge preserves local IDs, history, mastery score/status; never reset on subsequent scans. Document_id identifies the first source document; the schema has no many-to-many document/node mapping yet.
- Graph viewer (Canvas or WebView): pan, pinch zoom, recenter, hit test, filter/search, Node Inspector; semantic colors plus labels. No DB queries in onDraw.

## Diagnostic
- Generate 3–5 questions per set; MCQ has four options; SOCRATIC_OPEN requires online AI grading. Cached MCQs can be retried offline; never fabricate open-question grading offline.
- RootCauseAnalyzer traverses incoming PREREQUISITE via BFS/DFS with visited tracking; probe UNEXPLORED/FRAGILE/GAP nodes using follow-up questions. AI `suspectedRootCauseNodeId` is a hypothesis; confirm through probing before recording a definite root cause.
- Save QuizAttempt, mastery, and coverage transactionally. Do not invent unspecified mastery thresholds; agree on policy first. States: UNEXPLORED/GAP/FRAGILE/MASTERED; score 0..100.
- Coverage means percentage of MASTERED nodes per schema; proposed zero-node result is 0%. Agree on rounding/thresholds in policy. AI-generated nodes do not imply learning completion.

## Study/review
- TopologicalStudyPlanner selects related GAP/FRAGILE nodes with prerequisites first; deterministic results, handling cycles/invalid data.
- Duration: 10/15/20 minutes, default 15; discrete SeekBar. Three steps: analogy explanation, counter-examples, active recall; not merely rereading summaries.
- Tutor uses node/document/recent mistake context; AI content is not verified truth. Completion depends on reinforcement results, not simply clicking Finish.
- Spaced repetition prioritizes due GAP/FRAGILE nodes; `next_review_at` may be null; scheduling algorithm is unresolved. Do not invent thresholds or cron jobs beyond scope.

## Testing
- Duplicate-key merge, preserved ID/mastery, reversed edges, cycles, orphan/cross-subject links, rollback, branching root-cause traversal and visited tracking, topological order, zero nodes, idempotent retry, malformed JSON, and offline behavior.
