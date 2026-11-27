# Local-first data rules

Schema and keys: `docs/project/DATABASE_CONTRACT.md`. Do not change table/column names or versions without approval.

## SQLite
- Use one DatabaseHelper with applicationContext; enable FK constraints in `onConfigure` via `setForeignKeyConstraintsEnabled(true)`. Create tables in dependency order and create indexes.
- Centralize CRUD in DAOs using ContentValues, parameterized selectionArgs `?`, and explicit projections. Never concatenate user input into SQL; whitelist dynamic column/sort names.
- Close Cursor via try-with-resources or finally; use `getColumnIndexOrThrow` and check isNull. Do not close a shared DB while other DAOs use it.
- Use transactions for graph merge, quiz + mastery, guest migration, and coordinated cascades; roll back on failure and update UI after commit.
- Avoid indiscriminate `INSERT OR REPLACE` on existing nodes (it may delete rows and cascade history); update/insert while preserving IDs and mastery.
- New DB_VERSION requires sequential, data-preserving migrations and upgrade tests. Never DROP all tables or clear data to fix errors. Reset only when explicitly requested for test data.
- Scope all queries to the current user/subject. Verify that nodes/edges/documents/questions/attempts belong to the same subject and owner; single-column FKs do not guarantee this invariant.
- SQLite cascades do not delete disk files. Confirm subject/document deletion, commit the DB change, then clean up scoped files; handle orphans and retries.

## Preferences/session
- Reuse `core/preferences/AppPreferences` for the implemented explicit guest choice. Its existing file/key are `learngraph_entry` / `guest_selected`; preserve them for installed users. The planned `learngraph_prefs` keys in DATABASE_CONTRACT are not implemented. Agree on any consolidation and migration before changing storage. Use `apply()` for ordinary settings; if synchronous confirmation is genuinely necessary, use `commit()` on a background thread and check its result.
- Never store passwords/access/refresh tokens in ordinary preferences. See SECURITY.md. Remember Me preserves sessions according to policy, not passwords.
- A default guest ID does not prove that Guest Mode was selected; distinguish unauthenticated, explicit guest, and cloud session states.
- Logout clears relevant session/secrets without clearing onboarding/settings/learning data beyond the request. Prevent data leakage between accounts.

## FileStorageManager
- Durable images go in `getFilesDir()/slides/subject_<id>/scan_<timestamp>.jpg` (relative DB path); temporary images go in cache. Use FileProvider URIs, not file:// or hardcoded absolute paths.
- Check MIME, size, and URI permissions; copy to app storage for long-term access if needed. Compress/resize in the background; source suggests JPEG 80%, max long edge 1920px. Check small-text quality and the API's 5MB image limit.
- Cache cleaning deletes temporary files only, never saved scans. Use appropriate sharing/SAF mechanisms for exports.
- Handle IOException, storage exhaustion, and missing files. Do not mark COMPLETED until data is successfully saved.
