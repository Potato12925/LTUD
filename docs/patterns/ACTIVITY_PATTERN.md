# Activity pattern: instructions for a coding agent

## Apply this pattern
Use this file when adding or changing an Activity, its Intent contract, or screen navigation. The implemented examples are `feature/welcome/WelcomeActivity.java` and `feature/home/MainActivity.java`. Read those files and `core/ui/SystemBarInsets.java` before copying an approach. The code excerpt below is from the implemented app; future feature classes are not present.

## Decide the smallest screen owner
1. Search `feature/`, Manifest, and the component catalog for an existing screen that can own the flow. Extend it when the new behavior belongs there. Create `feature/<feature>/<Name>Activity.java` and `activity_<name>.xml` only for a distinct destination.
2. If the screen has only local state, keep that state in the Activity and `onSaveInstanceState`. Introduce a ViewModel only for nontrivial asynchronous or retained state, after reading `VIEWMODEL_PATTERN.md` and checking its dependency.
3. Keep UI concerns here: inflate views, bind resources, set listeners, navigate, and render state. Delegate persistence/network/algorithms to the data/domain layer. Do not run them on the main thread.

## Implement in this order
1. Define an explicit Intent contract in the destination: `EXTRA_*` constant, required ID type, and behavior for absent/invalid extras. Pass IDs rather than large objects. Validate ownership again in the data layer.
2. Register the Activity in `app/src/main/AndroidManifest.xml`; internal destinations use `android:exported="false"`. Change launcher/deep link declarations only when required by the feature.
3. Inflate the XML in `onCreate`, bind views, configure system bars once, restore transient state, attach listeners, then load/observe data. Match existing `initViews`/`setupListeners` style without adding empty methods.
4. Render loading, empty, content, and error states only where asynchronous data exists. Disable duplicate submits; display success after repository confirmation. Give errors a retry path where useful.
5. Save transient selection/step/scroll IDs in state. Durable edits go through the repository. Guard callbacks that could arrive after `finish()`/destroy or after a newer request.

## Existing mechanism to reuse
```java
EdgeToEdge.enable(this);
setContentView(R.layout.activity_main);
View main = findViewById(R.id.main);
SystemBarInsets.applyToPadding(main);
```
This is the implemented `MainActivity` inset setup. Apply it once to the appropriate root. `SystemBarInsets` adds the initial XML padding and does not define an IME policy. For replay, follow the existing explicit Intent with `WelcomeActivity.EXTRA_FORCE_SHOW`; do not alter the installed guest preference.

## Stop conditions and verification
- If navigation requires an unresolved auth/session or contract change, report the exact mismatch before altering it. Do not fake access to protected screens.
- Check Intent failure handling, back behavior, rotation/process recreation, insets, resource strings, and stale callbacks. Compile the changed variant and run focused tests per `TESTING.md`; report device checks separately.
- Never put raw SQL, networking, image processing, graph algorithms, or a retained Activity reference in this class.

