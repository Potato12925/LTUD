# CRUD feature pattern: instructions for a coding agent

## Apply this pattern
Use when implementing actual create/read/update/delete behavior, such as the planned M2 Subject feature. No Subject table, DAO, repository, adapter, ViewModel, or CRUD screen exists. This file is an execution recipe, not permission to implement the whole roadmap. Read the feature contract, `DATABASE_CONTRACT.md`, `DECISIONS.md`, catalog, and each layer pattern the feature actually needs.

## Decide boundaries before writing code
1. Translate the requested behavior into operations, ownership rules, fields, validation, deletion consequences, and observable states. For Subject, `FEATURES.md` requires name, code, description, color, user-scoped CRUD, metrics, and deletion confirmation. Unresolved guest identity, coverage calculation, or schema deviations require a decision before changing contracts.
2. Inspect existing packages, screens, layouts, resources, and dependencies. Reuse implemented `SystemBarInsets`, Material theme, and current entry flow where applicable. Do not add a ViewModel, Fragment, generic adapter, or shared executor solely because the directory outline mentions it.
3. Decide whether the existing host can own the feature or needs `feature/<feature>/<Name>Activity`/Fragment. Keep SQL and background scheduling in data layers, row binding in Adapter, navigation/rendering in UI.

## Write in dependency order
1. Add or extend the model and exact schema constants from the approved database contract. If this is the first persisted feature, implement the **complete** v1 schema and indexes in one `DatabaseHelper`, not a partial table. Do not invent columns or raise the version.
2. Implement entity DAO methods with full Cursor mapping, parameterized owner-scoped queries, `ContentValues`, row-count checks, and transaction-friendly methods. Add instrumented DB tests for constraints and isolation.
3. Implement repository operations on a shared background disk executor. Define explicit success/failure responses. Multi-table writes use one transaction; file cleanup is separate after commit.
4. Add screen state handling: a ViewModel only if async/retained complexity warrants it and its dependency is available. Represent loading, empty, content, and error distinctly. Ignore stale responses.
5. Add a feature-specific adapter and `item_<entity>.xml` for real rows. Bind every recycled state; pass IDs through callbacks. Add the list/form/dialog layouts and strings. Validate required fields at input and persistence boundaries. Confirm deletion in a Material dialog and show success only after commit.
6. Wire create/edit/delete to refresh the correct owner-scoped list and metrics. Disable repeated submissions. Handle offline use, missing IDs, and failed writes without losing entered input.

## Expected ownership of files when needed
```text
data/model/Subject.java
data/local/DatabaseContract.java
data/local/DatabaseHelper.java
data/local/dao/SubjectDao.java
data/repository/SubjectRepository.java
feature/subject/SubjectListActivity.java       # or a host-owned Fragment
feature/subject/SubjectAdapter.java
feature/subject/SubjectListViewModel.java      # only if justified
res/layout/activity_subject_list.xml
res/layout/item_subject.xml
res/values/strings.xml
```
This is a future directory map, not a list of files to create for every task. Keep existing package conventions and name the actual feature owner accordingly.

## Input and state mechanism (uncompiled future reference)
```java
String name = edtName.getText() == null ? "" : edtName.getText().toString().trim();
if (name.isEmpty()) {
    inputName.setError(getString(R.string.subject_name_required));
    return;
}
inputName.setError(null);
btnSave.setEnabled(false);
// Dispatch to repository/ViewModel; re-enable on success or failure.
```
The UI validation improves feedback; the repository still enforces owner and data invariants. Put all visible copy, including errors and dialog actions, in `strings.xml`. Keep screen-only dimensions in the owning XML and shared values in theme/resources.

## Evidence required from the implementation
Review and test the exact operations delivered: fresh DB creation, all CRUD paths, invalid/foreign IDs, constraints, transaction rollback, empty/loading/error/retry, duplicate taps, rotation/process recreation, deletion cascade and disk files, offline list, and light/dark resources. Run focused build and tests that tooling permits; report unrun device checks. Update `COMPONENT_CATALOG.md` only for implemented reusable pieces and STATUS only with verified behavior. Do not substitute mock rows or simulated AI output for stored data.

