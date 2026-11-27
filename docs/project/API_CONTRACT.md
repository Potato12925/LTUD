# REST / AI v1 contract (source design, backend unverified)

This is the self-contained documented API contract. Base `/api/v1`; UTF-8 JSON, multipart only with a specific contract. Production Backend Gateway; direct Gemini in development returns structured data equivalent to `data`. See `docs/rules/SECURITY.md`. Do not invent endpoints, payloads, tokens, or model versions.

## Envelope and errors
Success HTTP 200/201: `{success:true,message?,data:{...},meta?:{timestamp}}`. Error HTTP 4xx/5xx: `{success:false,error:{code,message,details?}}`. DTO nullability must follow the contract; validate data regardless of HTTP status. Use Authorization: Bearer access token when required; do not attach tokens to public requests.

| HTTP / code | Handling |
|---|---|
| 400 VALIDATION_ERROR | Appropriate inline field errors |
| 401 UNAUTHORIZED/TOKEN_EXPIRED | Refresh if endpoint is agreed, or navigate to login |
| 413 IMAGE_TOO_LARGE | Image >5MB; recompress |
| 422 UNREADABLE_DOCUMENT | Ask user to retake image |
| 429 RATE_LIMIT_EXCEEDED | Wait/bounded retry |
| 503 AI_ENGINE_UNAVAILABLE | Keep scan PENDING for retry |

## Authentication and users
All endpoints below have prefix `/api/v1`.
| Method/path | Request | Success data |
|---|---|---|
| POST /auth/register | fullName, email, password, university?, major? | userId, email, requiresOtpVerification |
| POST /auth/login | email, password | accessToken, refreshToken, expiresIn (seconds), user |
| POST /auth/forgot-password | email | Unspecified |
| POST /auth/reset-password | email, otpCode, newPassword | Unspecified |
| GET /users/me | bearer | user (detailed response unspecified) |
| PUT /users/me | fullName, university, major | Response unspecified |

Login user object: id, fullName, email, avatarUrl nullable, university, major, authProvider, createdAt (epoch ms). Refresh is mentioned as `/auth/refresh` without method/body; registration OTP verification, resend, logout/revoke, password change, account deletion, avatar upload, Google Sign-In, and sync lack complete contracts. Do not assume the server implements them. See `docs/project/DECISIONS.md`.

## AI extraction
`POST /ai/documents/extract-graph`
```json
{
  "subjectName": "Android",
  "documentTitle": "SQLite",
  "imageBase64": "<base64>",
  "mimeType": "image/jpeg",
  "existingNodes": [{"canonicalKey":"activity_lifecycle","title":"Activity lifecycle"}]
}
```
Example `data`:
```json
{
  "extractedMarkdown": "## SQLite",
  "nodes": [{"canonicalKey":"sqlite_open_helper","title":"SQLiteOpenHelper","summary":"Database management","formulaLatex":null,"suggestedPosX":120.0,"suggestedPosY":200.0}],
  "edges": [{"sourceCanonicalKey":"activity_lifecycle","targetCanonicalKey":"sqlite_open_helper","relationType":"RELATED","weight":0.8}]
}
```
Edge endpoints may be existing or new nodes; resolve within the correct subject. AI must not directly assign SQLite IDs/mastery. Base64 is the specifically documented request format; multipart is allowed only if the backend contract supports it.

## Diagnostic question generation
`POST /ai/diagnostics/generate-questions`
Request:
- subjectName, targetNodes[] {nodeId, canonicalKey, title, summary, prerequisiteTitles[]}, questionCount, includeSocraticOpen.
Response data:
- questions[] {nodeId, questionType, difficulty, questionText, options[], correctAnswer, explanation}.
- MCQ has four options, example correctAnswer A–D; SOCRATIC_OPEN options null, example correctAnswer is expected content. difficulty 1..3.

## Diagnostic evaluation
`POST /ai/diagnostics/evaluate`
Request: nodeId, nodeTitle, questionText, expectedAnswer, userAnswer, prerequisiteNodes[] {nodeId,title}.
Response data: isCorrect, score (0..100 per domain rules), updatedMasteryStatus, aiFeedback, suspectedRootCauseNodeId (may have no suspected cause), recommendedNextAction (e.g. PROBE_PREREQUISITE). Full action enum is unresolved. Do not directly record a suspected cause as confirmed.

## Study session
`POST /ai/study-sessions/generate`
Request: subjectName, durationMinutes, studentMajor, targetNodes[] {nodeId,title,recentMistakeSummary}.
Response data:
```json
{
  "sessionTitle":"Knowledge recovery",
  "durationMinutes":15,
  "steps":{
    "step1Analogy":{"title":"Explanation","contentMarkdown":"..."},
    "step2CounterExamples":{"title":"Counterexamples","items":[{"misconception":"...","correction":"..."}]},
    "step3ActiveRecallDrills":[{"questionText":"...","options":["A. ...","B. ...","C. ...","D. ..."],"correctAnswer":"A","explanation":"..."}]
  }
}
```
## Contextual tutor
`POST /ai/tutor/chat`
Request: nodeId, nodeTitle, nodeSummary, chatHistory[] {role,content}, userMessage.
Response data: replyMarkdown. Example roles user/assistant. Limit history according to need and policy; do not send the entire DB.

## Mapping rules
- Java DTOs use camelCase, DB uses snake_case; separate mapping and validate IDs, enums, nullability, ranges, and list sizes.
- Do not treat example JSON as a complete mandatory schema for unspecified fields. Agree with the backend team and update the contract when extending it.
- Use labeled test fixtures; no running backend URL has been verified; inspect the actual configuration.
