# SQLite and DAO pattern: instructions for a coding agent

## Apply this pattern
Use only when a requested feature needs saved structured data. No SQLite helper, contract class, model, or DAO exists. The documented starting schema is `learngraph.db` version 1 in `docs/project/DATABASE_CONTRACT.md`; inspect that contract and `DATA.md` before writing SQL. Do not create speculative tables or a generic CRUD framework in advance.

## Build the local layer in this order
1. Add `data/local/DatabaseContract.java` with `DATABASE_NAME`, `DATABASE_VERSION`, and nested table/column constants for the **approved complete schema**. Match the contract spelling, types, constraints, indexes, and defaults exactly. Use `long` for SQLite IDs/timestamps and `String` for user IDs in `data/model` POJOs.
2. Add one `data/local/DatabaseHelper.java` extending `SQLiteOpenHelper`, constructed with `context.getApplicationContext()`. In `onConfigure`, enable foreign keys. In `onCreate`, execute the complete v1 DDL in dependency order and create all indexes. If a helper already exists when the task starts, extend it rather than adding another.
3. Add `data/local/dao/<Entity>Dao.java` only for an entity the feature uses. Inject the helper. Give each DAO explicit projections and row mapping; use `ContentValues` for insert/update and `selectionArgs` for user values. Return models/IDs/counts, never a live Cursor to UI.
4. Expose DAO methods that accept a caller-supplied `SQLiteDatabase` for coordinated transactions where needed. A repository owns the transaction and invokes multiple DAOs with that same database. Do not close the shared database in a DAO.

## Copy the mechanisms, then fill the real schema
```java
// Inside DatabaseHelper; this API works with the project's minSdk 24.
@Override public void onConfigure(SQLiteDatabase db) {
    super.onConfigure(db);
    db.setForeignKeyConstraintsEnabled(true);
}
```
```java
// Inside a future SubjectDao; uncompiled until Subject and contract exist.
try (Cursor cursor = db.query(DatabaseContract.Subjects.TABLE,
        SUBJECT_PROJECTION,
        DatabaseContract.Subjects.USER_ID + " = ? AND "
                + DatabaseContract.Subjects.ID + " = ?",
        new String[] { userId, Long.toString(subjectId) },
        null, null, null)) {
    if (!cursor.moveToFirst()) return null;
    int nameIndex = cursor.getColumnIndexOrThrow(DatabaseContract.Subjects.NAME);
    String name = cursor.getString(nameIndex);
    // Map every approved field; call cursor.isNull(index) for nullable fields.
}
```
`SUBJECT_PROJECTION` must list every field the mapper needs. The query is only a mechanism, not a complete Subject model or table definition. Never copy the fragment as an entire DAO.

## Rules while writing methods
- Scope every read/update/delete by current owner and subject as relevant. Verify relationships across tables; a single-column FK does not prove a node or scan belongs to the requested subject/user.
- Use `?` parameters for values. Dynamic columns/sort order must come from an explicit allowlist. Use `getColumnIndexOrThrow`, `isNull`, and try-with-resources for every Cursor.
- Use `insertOrThrow`, scoped `update`/`delete`, and inspect affected row counts. Do not use `INSERT OR REPLACE` on existing graph records because it can cascade-delete history.
- Run every SQLite operation on a background disk executor through the repository. Propagate constraint/SQLite failures; do not swallow them or report success before commit.
- For graph merge, quiz/mastery, guest migration, or coordinated deletion, use a transaction with `endTransaction()` in `finally`. File deletion after a DB commit needs retry/orphan handling because SQLite cannot roll it back.

## Verify before calling the layer ready
Instrument fresh DB creation, complete schema/indexes, FK enforcement, CRUD, null mapping, constraints, transaction rollback, and cross-user/subject isolation. Compile the app, run the focused Android test when a device exists, and record unrun checks. For any version increase, use `DATABASE_MIGRATION_PATTERN.md` first.

