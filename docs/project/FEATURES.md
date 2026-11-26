# Functional specification — nine modules

This is the complete local functional baseline for the nine project modules. These are target requirements, not implementation status. Module IDs 0–8 are authoritative for this rule set.

## M0 — Auth and profile (P0)
- RegisterActivity: full name, email, password and confirmation, minimum 8 characters, inline errors; optional university/major/year. OTP/email activation before completion according to backend.
- LoginActivity: email/password, show/hide, Remember Me (secure session), Guest Mode; Google Sign-In extension P3. Enter Main only with a valid session or explicit guest choice.
- ForgotPasswordActivity: email, resend OTP after 60 seconds, verification and new password. Never fake OTP.
- ProfileActivity: avatar, full name, university/major, password change (old/new/confirmation), logout confirmation/unsynced data warning, explicit confirmation for account deletion.
- Acceptance: validation, loading/error, no cross-user access, no back navigation into protected screens after logout. See API_CONTRACT for missing endpoints.

## M1 — Onboarding and entry flow
- WelcomeActivity four steps: digitization, knowledge graph, knowledge gaps, personalized sessions. ViewPager2/dots, next/back/skip/start, replay via EXTRA_FORCE_SHOW.
- `onboarding_completed` controls first launch. Welcome → auth/guest check → Login or Main. Replay returns to appropriate screen without resetting user data.
- Acceptance: first launch, skip, relaunch, force-show, rotation/back, distinguish explicit guest from unauthenticated.

## M2 — Dashboard and subjects (P0)
- Extend MainActivity with BottomNavigation: Home / Subjects / Review / Settings. Dashboard shows subject count, MASTERED node count, GAP count; quick actions for scan and gap review.
- Subject CRUD: required name, code, description, color; card shows name, lecture count, coverage, and gap count. Confirm deletion of related data.
- Subject detail: three tabs Uploaded Documents / Knowledge Graph / Diagnostic History.
- Acceptance: user-scoped data, empty state, validation, offline CRUD, metrics refresh after operations.

## M3 — Scan/digitization (P0)
- Camera via ActivityResultLauncher/FileProvider, gallery supports multiple images/pages; multipage PDF P3.
- Compress images and save locally; AI extracts text, headings, LaTeX, tables/diagrams/code, concepts and prerequisites as JSON with existingNodes.
- Preview Markdown/LaTeX, edit titles/concepts, remove noise before confirming merge.
- Acceptance: cancellation/permission denial, network/blurry-image errors, retry, no image loss, no graph writes before confirmation.

## M4 — Knowledge Graph (P0)
- GraphMerger links new and existing nodes using canonical keys within the same subject without duplicates.
- Canvas/WebView viewer, pan, pinch, recenter; GAP filter and title search.
- Heatmap: MASTERED green, FRAGILE yellow, GAP red, UNEXPLORED neutral. Node Inspector: summary, formula, prerequisites, related documents, quick diagnosis/recovery study.
- Acceptance: saved graph viewable offline, no prerequisite cycles, no mastery reset, working node detail and search. Multiple source relationships require a contract extension.

## M5 — Diagnostic/root-cause (P1)
- Generate 3–5 questions after scan or for a selected branch, conceptual MCQ and SOCRATIC_OPEN, explain mistakes, trace prerequisites and probe.
- Save QuizAttempt, update score/status and coverage, retain history. Do not treat AI suspicions as confirmed root causes.
- Acceptance: cached MCQ offline, open evaluation requires AI or pending grading, correct user/node persistence, no guessed mastery thresholds.

## M6 — Study session and tutor (P1/P2)
- Group GAP/FRAGILE nodes with prerequisites first; SeekBar 10/15/20 minutes, default 15.
- Three steps: intuitive/analogy explanation, counterexamples, progressively challenging active recall.
- AI Tutor uses node/document/recent mistake context; mastery updates depend on reinforcement results, not clicking Finish.
- Acceptance: locally saved session, offline replay when content exists, retry on API failure, no fabricated grading.

## M7 — Progress and review (P2)
- Frequently missed concepts by subject, study streak, total study time; due nodes via next_review_at, prioritizing GAP/FRAGILE.
- Export Markdown/PDF/flashcards P3. Agree on streak calculation/review scheduling before implementation.

## M8 — Settings and utilities (P1/P2)
- System/light/dark theme, default duration 10/15/20, Vietnamese/English, replay onboarding.
- Scheduled reminders, AI completion/due-node notifications; request permissions when necessary. Global search for subjects/documents/nodes.
- Storage usage, cache clearing, cloud sync/backup restore extension according to contract; FAQ/feedback/privacy/terms/version.
- Acceptance: persistent settings, durable data preserved during cache cleaning, user-scoped search, permission-aware reminders.
