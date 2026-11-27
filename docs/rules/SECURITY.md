# Security, authentication, APIs, and system permissions

This file defines the local security and permission rules.

## Authentication and secrets
- Production: HTTPS Backend Gateway with Bearer JWT; server checks authorization, hashes passwords, and enforces OTP expiration/rate limits. Client validation does not replace server validation.
- Guest is `guest_local`, offline; do not fake tokens or bypass authorization. Guest-to-account migration requires an approved merge/ownership policy.
- Store tokens using Android Keystore-backed protection; inspect the existing implementation/dependencies. Source proposes EncryptedSharedPreferences, not verified as installed. Do not automatically add outdated dependency versions.
- Do not log Authorization, passwords, OTPs, slide images, or full AI prompts containing personal data. Never commit local.properties, keys, tokens, or secret signing configurations.
- Direct Gemini is dev/prototype-only with restricted keys/quotas; never ship keys in the APK. `BuildConfig` does not protect secrets against reverse engineering. Production requires a proxy or suitable credential design.
- Use HTTPS, timeouts, bounded retries; never disable TLS validation. Refresh on 401 at most once when a real contract exists; prevent loops. If refresh fails, navigate to login while preserving correctly scoped offline data.
- For unimplemented APIs, use clearly labeled interfaces/fakes in test/debug; do not claim OTP delivery or real login without a backend.

## Permissions / FileProvider
- Request permissions only when needed; check API level (current min 24). INTERNET is a normal permission.
- Prefer Photo Picker/SAF or ActivityResultContracts for image selection; avoid broad storage permission. Request CAMERA only for APIs that require it; external camera uses FileProvider/contracts according to configuration.
- For direct media access, use Android-version-specific permissions and handle limited photo selection. Request POST_NOTIFICATIONS on API 33+ when reminders are enabled.
- Handle denial, cancellation, and permanent denial without crashes. FileProvider is not exported, grants temporary URIs, and exposes only minimal paths, never the entire file root.

## AI, WebView, and privacy
- Treat scan/LLM content as untrusted data; validate JSON/schema and ownership. Do not execute AI-suggested code, commands, or URLs.
- Render Markdown/LaTeX in WebView using local assets and sanitized HTML/JS; restrict navigation, broad file access, and JS interfaces. Enable JS only when rendering requires it; expose minimal bridges only to trusted content.
- Do not upload personal photos/documents beyond the user-selected task. Minimize payload data and show errors when offline.
