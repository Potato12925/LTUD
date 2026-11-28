# Fragment pattern: instructions for a coding agent

## Apply this pattern
Use for a real tab, nested screen, or screen section whose view lifecycle is distinct from its host Activity. There is no implemented Fragment; the sample is an uncompiled future reference. Confirm a Fragment is needed before adding `feature/<feature>/<Name>Fragment.java` and `fragment_<name>.xml`. Read the host Activity and `ARCHITECTURE.md` first. Do not add a navigation library for this pattern.

## Implement in this order
1. Define stable ID arguments using `ARG_*` keys and a `newInstance(long id)` factory. Keep a public empty constructor for framework recreation; validate arguments before loading.
2. Inflate/bind the view in `onCreateView`/`onViewCreated`. Attach adapters and listeners to the current view only. Observe LiveData, if used, with `getViewLifecycleOwner()`.
3. Hand actions to the host, ViewModel, or repository as the actual feature requires. The Fragment does not perform SQL/network/file work. The host owns root system insets unless the nested view needs a separate, deliberate policy.
4. On `onDestroyView`, clear view fields, adapter callbacks, and pending UI work. An asynchronous result must check that it still belongs to the active view and ID before rendering.

## View lifecycle reference (uncompiled)
```java
public final class SubjectFragment extends Fragment {
    private View root;

    public SubjectFragment() { super(R.layout.fragment_subject); }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle state) {
        super.onViewCreated(view, state);
        root = view;
        // Bind this view; observe state using getViewLifecycleOwner().
    }

    @Override public void onDestroyView() {
        root = null;
        super.onDestroyView();
    }
}
```
Replace the comment with real bindings only when the feature exists. Do not keep `root`, its children, or its Context in a singleton or long-lived callback.

## Verify the actual feature
Check recreation, argument validation, back stack, loading/empty/error rendering, observer cleanup, and rapid navigation away/back. Compile and run focused navigation tests when available. If the host already manages the same content adequately, edit that host instead of adding a Fragment.

