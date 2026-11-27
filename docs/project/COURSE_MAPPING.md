# Course knowledge mapping

This standalone mapping summarizes seven Android course topics. It is not an instructor-confirmed official rubric; preserve the coursework technology stack and verify actual submission requirements with the user if needed.

| Knowledge area | Project application |
|---|---|
| 01 Android core, Gradle, Manifest | Component declarations, resources, permissions, namespace |
| 02 Activity lifecycle, Intent, Bundle, result | Entry flow, subject/node IDs, camera/gallery, saved state |
| 03 XML layouts and UI widgets | Constraint/Linear/Relative/Frame when appropriate, EditText, ImageView/ImageButton, Toast, Toggle/Switch, SeekBar, WebView, Drawable |
| 04 Adapter ListView/GridView | Subject grid, gap/diagnostic ListView, Custom Adapter + ViewHolder; RecyclerView/ViewPager2 by feature |
| 05 SharedPreferences, internal/cache storage | Onboarding, settings, secure session persistence, slide images |
| 06 SQLite | SQLiteOpenHelper, DAO, Cursor, ContentValues, CRUD, transactions, FK |
| 07 Java/OOP | POJO, encapsulation, reference safety, Java utility/domain code |

## Application rules
- Prefer readable code that can be explained in a demo; do not replace course concepts wholesale with external frameworks.
- Use ActivityResultLauncher for new code instead of legacy startActivityForResult examples; preserve Intent/Bundle concepts. Do not copy examples that store plaintext passwords or drop tables on upgrade.
- Do not add widgets merely because they appear in lessons. If the rubric explicitly requires one, use it in an appropriate feature and document the requirement.
- This mapping is sufficient for the documented course-alignment rules. No original lecture files are required; additional grading criteria must be supplied by the user if applicable.
