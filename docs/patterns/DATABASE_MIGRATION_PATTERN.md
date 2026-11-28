# Database migration pattern: instructions for a coding agent

## Apply this pattern
Use only when changing an **implemented** SQLite schema/version. The current repository has no database implementation; `DATABASE_CONTRACT.md` documents planned v1. Before changing schema, inspect deployed code/DDL and obtain approval for the contract and accepted ADR required by `AGENTS.md`. Do not raise `DATABASE_VERSION` merely because a pattern describes migrations.

## Design each version step
1. Record old version, new version, exact table/column/index changes, existing rows, null/default/backfill behavior, ownership, and rollback conditions. Preserve IDs, mastery, timestamps, user scope, and relative file paths.
2. Update the fresh-create schema and write one migration method per adjacent version (`migrate1To2`, `migrate2To3`). `onUpgrade` must run every intervening step for a jump such as 1 → 3. Missing steps throw; never silently skip or recreate all tables.
3. Prefer additive `ALTER TABLE` when it preserves constraints. For a rebuild, create a new table with approved DDL, copy and validate rows, swap tables, then recreate indexes/triggers within the upgrade transaction. Never `DROP` valuable data or reset the DB to make an upgrade pass.
4. Keep foreign keys enabled in `onConfigure`. Android calls `onUpgrade` in a transaction; let failures throw so the open rolls back. Do not manually mark an incomplete migration successful.

## Dispatch mechanism (uncompiled future reference)
```java
@Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    for (int version = oldVersion; version < newVersion; version++) {
        switch (version) {
            case 1: migrate1To2(db); break;
            case 2: migrate2To3(db); break;
            default: throw new IllegalStateException("Missing migration from " + version);
        }
    }
}
```
Add only the approved cases and their real methods. This is control flow, not a proposal for v2 or v3. Place migration code in the single `data/local/DatabaseHelper.java`; constants remain in `DatabaseContract.java`.

## Prove data preservation
Create instrumented fixtures at every supported old version containing nonempty records, nulls, foreign keys, and indexes. Open with the new helper, then compare IDs/row counts/representative values, run `PRAGMA foreign_key_check`, verify indexes and CRUD, and exercise version jumps. Compare the end schema of fresh create with upgraded DB. Test a forced migration failure for rollback where practical. Run focused build/device tests and document any unrun path; update the schema contract and STATUS only after the approved implementation is verified.

