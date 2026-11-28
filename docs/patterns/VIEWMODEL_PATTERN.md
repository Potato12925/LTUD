# ViewModel pattern: instructions for a coding agent

## Apply this pattern
Use `feature/<feature>/<Name>ViewModel.java` for a screen with nontrivial asynchronous state, retries, or state retained over configuration changes. No ViewModel exists; the implemented welcome/home flow does not require one. Confirm AndroidX Lifecycle ViewModel/LiveData availability in the actual Gradle classpath before use; adding a dependency requires approval. A simple Activity plus saved state remains valid.

## Define the screen contract first
1. Write one immutable state representation that distinguishes loading, empty, content, and error for the requested screen. Include current owner/record ID and any selection needed to render. Do not use `null` to mean several states.
2. Inject the repository through a real factory if constructor arguments are required. The ViewModel holds no View, Activity, Fragment, Cursor, or mutable database.
3. Expose state as `LiveData` or another dependency already available in the project. UI observes with its Activity lifecycle or `getViewLifecycleOwner()` in a Fragment, then renders on main.
4. For each load or mutation, set the in-flight state, invoke the repository, and render success/error only when the callback matches the current request and owner. Increment a generation counter or cancel work to prevent stale responses. Disable duplicate submissions until completion.

## Stale-result mechanism (uncompiled future reference)
```java
private long requestGeneration;

public void refresh(String userId) {
    long generation = ++requestGeneration;
    state.setValue(ScreenState.loading());
    repository.loadSubjects(userId, subjects -> {
        if (generation != requestGeneration) return;
        state.setValue(subjects.isEmpty()
                ? ScreenState.empty() : ScreenState.content(subjects));
    }, error -> {
        if (generation == requestGeneration) {
            state.setValue(ScreenState.error(error));
        }
    });
}
```
`ScreenState`, repository, and `state` are future feature declarations; this mechanism is not compiled. Implement a factory and immutable snapshots before copying it.

## Recreation and errors
Use saved state for small transient IDs/filters if its API is available. A ViewModel surviving rotation does not replace SQLite for durable data and does not guarantee process-death restoration. Do not place raw exception text or secrets directly in user-facing UI; map errors to `strings.xml` and retry actions. Test rapid refresh, error then retry, rotation, and owner switch before treating the screen state as reliable.

