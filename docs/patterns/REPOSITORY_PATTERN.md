# Repository pattern: instructions for a coding agent

## Apply this pattern
Add `data/repository/<Entity>Repository.java` when a requested feature needs DAO access, background work, transactions, or local/remote coordination. No repository exists now. A repository is the UI-facing data boundary; it is not a second place to render screen state or define SQLite schema. Read `DAO_PATTERN.md`, `DATA.md`, and the API contract if networking is involved.

## Decide the interface from the feature
1. List the exact user operations and their inputs/outputs (for example `listForUser(userId)`, `create(userId, input)`, `delete(userId, id)`). Include ownership IDs in each operation. Define what a missing row, validation failure, constraint error, and offline state return. Do not invent a generic repository API without consumers.
2. Inject the entity DAOs and an application-scoped disk executor. Use application context only when needed. A single disk thread preserves write order; check for an existing executor before adding `core/concurrency/AppExecutors`.
3. Execute SQLite/file work off main. Deliver immutable results on main or expose a lifecycle-aware observable if already adopted. The screen/ViewModel is responsible for ignoring callbacks after destruction or a newer request.
4. If one user operation writes multiple tables, begin one transaction on a writable database, pass that database into each DAO, set successful only after every check/write passes, and always end it in `finally`. Return success only after commit.

## Asynchronous mechanism (uncompiled future reference)
```java
public void loadSubjects(String userId, Consumer<List<Subject>> onSuccess,
                         Consumer<Exception> onFailure) {
    diskExecutor.execute(() -> {
        try {
            List<Subject> subjects = subjectDao.listForUser(userId);
            mainHandler.post(() -> onSuccess.accept(subjects));
        } catch (SQLiteException error) {
            mainHandler.post(() -> onFailure.accept(error));
        }
    });
}
```
In real code, prefer one explicit result type if several operations share the same error handling. The sample assumes future `Subject`, DAO, executor, and handler; it has not been compiled. Do not pass a live Cursor or database handle across the callback.

## Local-first and failure behavior
- Read saved data locally when offline. Network/AI calls, when approved, live behind `data/remote` and must not overwrite local IDs/mastery or fabricate responses. Keep DTOs separate from DB models.
- Validate requested owner/subject relationships before mutations. Surface actionable failures rather than empty success or silent retries. For DB-plus-file deletion, commit scoped DB changes first and recover orphaned file cleanup separately.
- Never retain an Activity/Fragment or create a thread pool per click. Keep callbacks bounded; prevent duplicate writes and stale results at the caller.

## Verify the boundary
Test success, missing/foreign IDs, constraint failure, transaction rollback, offline read, and callback thread with the smallest practical tests. Check that the UI contains no SQL and that no success state appears before commit. Update STATUS only with observed results.

