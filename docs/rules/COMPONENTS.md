# Shared components

## Choosing a reuse mechanism
1. Search existing resources/layouts/classes; compare behavior and configurability.
2. For differences only in color/text size/dimensions, use styles/themes/drawables; no separate Java class.
3. For static UI groups, use XML `component_*.xml` + `<include>`; use `<merge>` only with a suitable parent.
4. For repeated UI with behavior, use a Custom View/compound View in `ui/components`, XML attributes if necessary, a small public API, and callbacks. Avoid dependence on a specific Activity.
5. For lifecycle-aware dialogs/bottom sheets, use DialogFragment/BottomSheetDialogFragment; pass IDs and return results without retaining old Activities.
6. For list/grid items, use `item_*.xml` and model-specific adapters; do not build a generic framework for a single use case.

## Adapters
- Advanced ListView/GridView: BaseAdapter + ViewHolder, `inflate(layout, parent, false)` only when convertView is null; use correct tag type and fully bind all states to avoid stale data.
- RecyclerView/ViewPager2: ViewHolder and consistent list updates; use DiffUtil/ListAdapter if compatible with existing code. No API/DAO calls in bind/getView.
- Item callbacks use IDs or valid positions at click time; avoid stale indexes. Update UI on the main thread.
- Do not force ListView/GridView migration to RecyclerView when coursework/features require older components.

## Registry — inspect repository before marking as implemented
| Component | Source | Documented status |
|---|---|---|
| WelcomeActivity | `app/src/main/java/com/example/ltud/WelcomeActivity.java` | Three steps, dots, guest selection and replay; `EXTRA_FORCE_SHOW` controls replay; build verified 2026-11-26 |
| KnowledgeOrbView | `app/src/main/java/com/example/ltud/KnowledgeOrbView.java` | Decorative draggable point cloud; XML use in `activity_welcome.xml`; build verified 2026-11-26 |
| Onboarding feature row | `app/src/main/res/layout/item_welcome_feature.xml` | Title, description, symbol set by WelcomeActivity; build verified 2026-11-26 |
| App theme | `app/src/main/res/values/themes.xml` and `values-night/themes.xml` | Material 3 DayNight palette from supplied Stitch screen; build verified 2026-11-26 |
| Shared button/input/text styles | `res/values/styles.xml` | Proposed; unverified |
| Toolbar/header, loading/empty/error | `res/layout/component_*.xml` | Proposed; create only as needed |
| SearchBar, PasswordInput | `ui/components/` | Proposed for repeated behavior |
| Subject card | `res/layout/item_subject_card.xml` | Specified; unverified |
| Node Inspector | `ui/graph/`, `bottom_sheet_*.xml` | Specified; unverified |

When creating a real component, update its registry entry with name, exact path, purpose, parameters/callbacks, brief usage, and verification status. Do not register fake components or mass-create placeholders. Split the registry if it grows too long and update AGENTS.md.
