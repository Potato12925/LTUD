# Adapter pattern: instructions for a coding agent

## Apply this pattern
Use when binding a real collection to ListView, GridView, RecyclerView, or ViewPager2. No adapter exists. `item_welcome_feature.xml` is welcome-specific, so do not reuse it for unrelated entities. Read `COMPONENTS.md`, the feature's layout, the catalog, and Gradle before selecting a widget. Put `NameAdapter.java` and `item_<entity>.xml` with the owning feature unless identical row behavior already has multiple consumers.

## Select and implement
- Keep ListView/GridView when the feature or coursework calls for them. Use a `BaseAdapter` with a static ViewHolder/tag. Inflate once with `inflate(R.layout.item_subject, parent, false)`, then bind **every** row property on every `getView`, including hidden/selected/error states. Return a stable DB ID from `getItemId` only if available and declare `hasStableIds` consistently.
- Use RecyclerView for a feature that needs it after confirming the artifact is on the actual classpath. Prefer `ListAdapter` + `DiffUtil` for changing immutable lists. Create a new list before `submitList`; never mutate the submitted list. A plain `RecyclerView.Adapter` is enough for a fixed small collection.
- Manual ViewHolder and `findViewById` match current code. Use ViewBinding only if enabled for that feature; do not migrate the app to it just to write one adapter.
- Keep selection state in the screen/ViewModel by item ID, not holder position. Click and long-click callbacks pass the current bound item's ID. Resolve current position only at action time if a position is truly needed.

## Binding reference (uncompiled, inside a future `BaseAdapter`)
```java
@Override public View getView(int position, View recycled, ViewGroup parent) {
    Holder holder;
    if (recycled == null) {
        recycled = inflater.inflate(R.layout.item_subject, parent, false);
        holder = new Holder(recycled);
        recycled.setTag(holder);
    } else {
        holder = (Holder) recycled.getTag();
    }
    Subject subject = getItem(position); // Override getItem with Subject return type.
    holder.title.setText(subject.getName());
    holder.itemView.setSelected(selectedId == subject.getId());
    holder.itemView.setOnClickListener(view -> listener.onClick(subject.getId()));
    return recycled;
}
```
`Holder`, `Subject`, and `listener` represent future feature code. Bind all actual fields and accessibility text in the real adapter. Text comes from the model or `strings.xml`, never a UI literal.

## Updates, errors, and reuse decision
The repository/host loads data off the main thread and posts the resulting snapshot to the main thread. The host renders empty/loading/error state; the adapter only renders items. For BaseAdapter, replace its snapshot and call `notifyDataSetChanged()` on main. For ListAdapter, call `submitList(newSnapshot)` on main. Never query or save from `getView`/`onBindViewHolder`.

Reuse an existing adapter when row model, layout, actions, and selection semantics match. Otherwise create a feature-specific one. Extract a shared adapter only after real consumers require the same binding contract. Verify recycling, deletion/reorder, rapid updates, click/long-click, and selection persistence with focused tests or device checks.

