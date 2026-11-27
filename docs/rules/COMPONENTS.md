# Shared components

## Choosing a reuse mechanism
1. Search existing resources/layouts/classes; compare behavior and configurability.
2. For differences only in color/text size/dimensions, use styles/themes/drawables; no separate Java class.
3. For static UI groups reused in multiple places, use XML `component_*.xml` + `<include>`; use `<merge>` only with a suitable parent.
4. For repeated UI with behavior, use a Custom View/compound View in `core/components`, XML attributes if necessary, a small public API, and callbacks. Keep feature-specific views in their feature package. Avoid dependence on a specific Activity.
5. For lifecycle-aware dialogs/bottom sheets, use DialogFragment/BottomSheetDialogFragment; pass IDs and return results without retaining old Activities.
6. For list/grid items, use `item_*.xml` and model-specific adapters; do not build a generic framework for a single use case.

## Adapters
- Advanced ListView/GridView: BaseAdapter + ViewHolder, `inflate(layout, parent, false)` only when convertView is null; use correct tag type and fully bind all states to avoid stale data.
- RecyclerView/ViewPager2: ViewHolder and consistent list updates; use DiffUtil/ListAdapter if compatible with existing code. No API/DAO calls in bind/getView.
- Item callbacks use IDs or valid positions at click time; avoid stale indexes. Update UI on the main thread.
- Do not force ListView/GridView migration to RecyclerView when coursework/features require older components.

## Implemented catalog
Read `docs/project/COMPONENT_CATALOG.md` and the linked source before adding a class or resource. Register only implemented, reusable components there, with their consumers and limitations. Do not create base classes, adapters, DAOs, or placeholder layouts without a real use case.
