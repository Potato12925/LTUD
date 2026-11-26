# SQLite and storage contract — planned schema v1

The self-contained schema below defines the documented DDL table/column names, defaults, and constraints; inspect the actual schema before use. Do not migrate without approval. Read `docs/rules/DATA.md` for implementation rules.

DB `learngraph.db`, DATABASE_VERSION = 1 per source. SQLite INTEGER IDs use long in Java; user IDs are TEXT; timestamps are epoch milliseconds; Booleans use 0/1. Enable foreign keys in onConfigure.

## Reference DDL (repository unverified)
```sql
CREATE TABLE users (
 id TEXT PRIMARY KEY,
 full_name TEXT NOT NULL,
 email TEXT UNIQUE,
 avatar_url TEXT,
 university TEXT,
 major TEXT,
 auth_provider TEXT NOT NULL DEFAULT 'GUEST',
 created_at INTEGER NOT NULL
);
CREATE TABLE subjects (
 id INTEGER PRIMARY KEY AUTOINCREMENT,
 user_id TEXT NOT NULL,
 name TEXT NOT NULL,
 code TEXT,
 description TEXT,
 color_hex TEXT NOT NULL DEFAULT '#4F46E5',
 coverage_percent INTEGER NOT NULL DEFAULT 0 CHECK (coverage_percent BETWEEN 0 AND 100),
 created_at INTEGER NOT NULL,
 updated_at INTEGER NOT NULL,
 FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
CREATE TABLE document_scans (
 id INTEGER PRIMARY KEY AUTOINCREMENT,
 subject_id INTEGER NOT NULL,
 title TEXT NOT NULL,
 image_path TEXT NOT NULL,
 extracted_markdown TEXT,
 status TEXT NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING','PROCESSING','COMPLETED','FAILED')),
 created_at INTEGER NOT NULL,
 FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
);
CREATE TABLE concept_nodes (
 id INTEGER PRIMARY KEY AUTOINCREMENT,
 subject_id INTEGER NOT NULL,
 document_id INTEGER,
 canonical_key TEXT NOT NULL,
 title TEXT NOT NULL,
 summary TEXT NOT NULL,
 formula_latex TEXT,
 mastery_status TEXT NOT NULL DEFAULT 'UNEXPLORED' CHECK (mastery_status IN ('UNEXPLORED','GAP','FRAGILE','MASTERED')),
 mastery_score INTEGER NOT NULL DEFAULT 0 CHECK (mastery_score BETWEEN 0 AND 100),
 pos_x REAL NOT NULL DEFAULT 0.0,
 pos_y REAL NOT NULL DEFAULT 0.0,
 last_reviewed_at INTEGER,
 next_review_at INTEGER,
 created_at INTEGER NOT NULL,
 UNIQUE(subject_id,canonical_key),
 FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE,
 FOREIGN KEY (document_id) REFERENCES document_scans(id) ON DELETE SET NULL
);
CREATE TABLE concept_edges (
 id INTEGER PRIMARY KEY AUTOINCREMENT,
 subject_id INTEGER NOT NULL,
 source_node_id INTEGER NOT NULL,
 target_node_id INTEGER NOT NULL,
 relation_type TEXT NOT NULL DEFAULT 'PREREQUISITE' CHECK (relation_type IN ('PREREQUISITE','PART_OF','RELATED')),
 weight REAL NOT NULL DEFAULT 1.0,
 UNIQUE(source_node_id,target_node_id,relation_type),
 CHECK(source_node_id != target_node_id),
 FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE,
 FOREIGN KEY (source_node_id) REFERENCES concept_nodes(id) ON DELETE CASCADE,
 FOREIGN KEY (target_node_id) REFERENCES concept_nodes(id) ON DELETE CASCADE
);
CREATE TABLE diagnostic_questions (
 id INTEGER PRIMARY KEY AUTOINCREMENT,
 node_id INTEGER NOT NULL,
 question_text TEXT NOT NULL,
 question_type TEXT NOT NULL DEFAULT 'MCQ' CHECK (question_type IN ('MCQ','SOCRATIC_OPEN')),
 options_json TEXT,
 correct_answer TEXT NOT NULL,
 explanation TEXT NOT NULL,
 difficulty INTEGER NOT NULL DEFAULT 1 CHECK (difficulty BETWEEN 1 AND 3),
 FOREIGN KEY (node_id) REFERENCES concept_nodes(id) ON DELETE CASCADE
);
CREATE TABLE quiz_attempts (
 id INTEGER PRIMARY KEY AUTOINCREMENT,
 node_id INTEGER NOT NULL,
 question_id INTEGER NOT NULL,
 user_answer TEXT NOT NULL,
 is_correct INTEGER NOT NULL CHECK (is_correct IN (0,1)),
 ai_feedback TEXT,
 root_cause_node_id INTEGER,
 timestamp INTEGER NOT NULL,
 FOREIGN KEY (node_id) REFERENCES concept_nodes(id) ON DELETE CASCADE,
 FOREIGN KEY (question_id) REFERENCES diagnostic_questions(id) ON DELETE CASCADE,
 FOREIGN KEY (root_cause_node_id) REFERENCES concept_nodes(id) ON DELETE SET NULL
);
CREATE TABLE study_sessions (
 id INTEGER PRIMARY KEY AUTOINCREMENT,
 subject_id INTEGER NOT NULL,
 target_node_ids_json TEXT NOT NULL,
 duration_minutes INTEGER NOT NULL DEFAULT 15,
 lesson_content_json TEXT NOT NULL,
 is_completed INTEGER NOT NULL DEFAULT 0 CHECK (is_completed IN (0,1)),
 created_at INTEGER NOT NULL,
 completed_at INTEGER,
 FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
);
CREATE INDEX idx_subjects_user ON subjects(user_id);
CREATE INDEX idx_scans_subject ON document_scans(subject_id);
CREATE INDEX idx_nodes_subject_status ON concept_nodes(subject_id,mastery_status);
CREATE INDEX idx_nodes_next_review ON concept_nodes(next_review_at);
CREATE INDEX idx_edges_subject ON concept_edges(subject_id);
CREATE INDEX idx_edges_target ON concept_edges(target_node_id);
CREATE INDEX idx_questions_node ON diagnostic_questions(node_id);
CREATE INDEX idx_attempts_node_time ON quiz_attempts(node_id,timestamp DESC);
```

## Field semantics and invariants
- `users.id` is a backend ID or `guest_local`; providers EMAIL/GOOGLE/GUEST (source DDL defaults to GUEST; see DECISIONS).
- `subjects.coverage_percent` is the percentage of MASTERED nodes; `color_hex` is subject-specific data, not the app palette.
- `document_scans.image_path` is relative to files; status follows CHECK. `document_id` identifies the node's first source document; document deletion sets it to NULL.
- `canonical_key` is lowercase and accent-free; nodes are unique per subject. PREREQUISITE source is the prerequisite; validate weight 0..1, scope, and cycles in domain/DAO because DDL does not guarantee these.
- options_json is an array of four MCQ choices; null for open questions. Standardize correct_answer with the API contract (e.g. A–D); difficulty 1..3.
- QuizAttempt root_cause_node_id is nullable; question and node must correspond, root cause belongs to the same subject.
- StudySession target_node_ids_json is an ordered ID array; lesson_content_json contains three-step content; duration 10/15/20; is_completed/completed_at must be consistent. Agree on camelCase API JSON mapping to the local contract; do not silently change the schema.
- Models: User, Subject, DocumentScan, ConceptNode, ConceptEdge, DiagnosticQuestion, QuizAttempt, StudySession. Map snake_case DB to camelCase Java.

## SharedPreferences `learngraph_prefs`
| Constant | Key | Type / documented default | Status |
|---|---|---|---|
| KEY_ONBOARDING_COMPLETED | onboarding_completed | boolean false | Present in snapshot |
| KEY_CURRENT_USER_ID | current_user_id | String guest_local | Planned |
| KEY_IS_GUEST_MODE | is_guest_mode | boolean true | Planned; see DECISIONS |
| KEY_THEME_MODE | theme_mode | int 0 system, 1 light, 2 dark | Planned |
| KEY_DEFAULT_STUDY_DURATION | default_study_duration | int 15 | Planned |
| KEY_DAILY_REMINDER_ENABLED | daily_reminder_enabled | boolean true | Planned; respect permissions |
| KEY_DAILY_REMINDER_HOUR | daily_reminder_hour | int 20 | Planned |

Planned secure auth storage: `learngraph_secure_auth`, no plaintext. Do not use guest defaults as proof that Guest Mode was selected.

## File storage
- files/slides/subject_<subject_id>/scan_<timestamp>.jpg
- files/exports/review_<subject_id>.md (P3)
- cache/scan_temp/ or cache/camera_capture_temp.jpg (temporary); follow existing code conventions.
