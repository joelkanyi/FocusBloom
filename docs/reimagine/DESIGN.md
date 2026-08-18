# FocusBloom Reimagining — Design

Status: DRAFT, pending sign-off. No implementation begins until this is approved.

This document is written before any code, in the Kepler tradition (design, then release
plan, then implementation). It is the single source of truth for the reimagining. Every
non-trivial decision below carries a provenance tag: `[doc]` from a reference we studied,
`[ref]` from a reference library or my doctrine, `[inference]` my own reasoning.

> **Amended by [STRATEGY.md](STRATEGY.md).** A product-strategy pass (four research passes plus
> a synthesis) reshapes the product this engineering foundation carries. The foundation here
> stands; the deltas are: `Task` gains `location` (SHELF / TODAY / COMPOST), `drawnOn`,
> `lastTouchedAt`; the session state machine gains `SoftHorizonReached`, `Parked`, `Overrun`; a
> `:capability:assist:api` seam is added in Phase 0.3; no streak fields ever enter the schema;
> and two new phases land after the current five (Phase 6 Sync/accounts/Bloom, Phase 7
> Assist/MCP). Read STRATEGY.md for the product; read on here for the architecture.

---

## 1. Decisions locked with Joel

| Question | Decision |
|---|---|
| Scope | Full reimagining now: new IA, new/changed features, new UX, built directly on the new stack. |
| Vision | The full **Personal Productivity OS**, built **focus-first** (the deep-work session is the anchor and Phase 1), with a **mindful / gentle** ethos as a cross-cutting principle. Phased and prioritised, each phase ships something whole. |
| Platforms | **All four first-class**: Android, iOS, Desktop (JVM), Web (wasmJs). |
| Brand | **Editorial ink + one bloom accent**: near-monochrome ink/paper, single restrained accent, content-first, calm. |
| Workflow | Branch + PR per reviewable slice. Public text (PR/commit/issue) authored by Joel in his voice (Lysine policy). No AI attribution anywhere. |

---

## 2. North star and positioning

FocusBloom is a calm, focus-first personal productivity OS. The deep-work session is the
hero; everything else exists to feed and reflect on sessions. The design ethos is mindful:
no streak-guilt, gentle nudges over pressure, reflection over vanity metrics. The visual
language is editorial: ink on paper, generous space, one accent, the timer and your data
allowed to breathe.

The growth metaphor ("bloom") is earned through use, not decoration: a day of focused
sessions visibly fills out; the app rewards attention, not busyness.

---

## 3. Product: information architecture and feature set (phased)

Top-level navigation is a small, stable set of destinations. Features arrive by phase; the
shell is designed for the full set from day one so nothing is retrofitted.

```
Today        Focus (hero)     Plan            Insights        You
(home)       session engine   tasks+calendar  reflection      profile+settings
```

### Feature set by phase

- **Phase 1 — Focus-first core (the hero)**
  - Focus session engine: focus/break cycles, session presets, ambient sound, gentle
    completion. The session is a first-class state machine, not a countdown.
  - Today: what to focus on now, the day at a glance, start a session in one tap.
  - Tasks (lightweight): the minimum task model needed to feed a session (title, estimate,
    focus color). Full task management is Phase 2.
  - Onboarding: intention-setting, session defaults, brand-consistent.
  - Settings/You (minimum): theme, session defaults, hour format.

- **Phase 2 — Plan (tasks + calendar reimagined)**
  - Full task management (subtasks, priority, estimates, scheduling).
  - Day/calendar timeline, reimagined, tasks feed sessions.

- **Phase 3 — Insights (reflection over metrics)**
  - Where your attention went, session history, gentle trends. Mindful framing: no guilt,
    no punitive streaks. Optional streaks that celebrate, never scold.

- **Phase 4 — Habits and goals**
  - Recurring intentions, goal linkage to sessions and tasks.

- **Phase 5 — Notes and review**
  - Session notes, periodic review/reflection ritual.

Cross-cutting through every phase: the mindful ethos (copy, motion, absence of dark
patterns), full jenga adoption, four-platform parity, accessibility/contrast.

---

## 4. Architecture: the module graph

We clone PikaPal's tiered multi-module architecture and its compile-time module-graph law
`[doc: PikaPal]`. This is the concrete meaning of "PikaPal level."

```
:app:android   :app:desktop   :app:web   :shared(iOS entry)     ← composition roots, own the DI graph
        |            |            |            |
        +------------+------------+------------+
                          |
                    :feature:*                                    ← UI + presentation ONLY, one per screen
              today, focus, plan, insights, you, onboarding
                          |
                 :capability:api   /   :capability:impl           ← business services, api ⟂ impl
        session, tasks, planning, insights, preferences, notification, sync
                          |
   :core:model  :core:common  :core:designsystem  :core:navigation
   :core:database  :core:datastore  :core:testing                ← foundation, no business opinion
```

### The one law (compile-time enforced) `[doc: PikaPal CheckModuleGraphTask]`

1. `feature -> capability:api -> core`. Impls are known only to the composition roots
   (the four `:app:*` and `:shared`).
2. Features never depend on other features.
3. Features reach data only through capability apis. `:core:database` / `:core:datastore`
   are forbidden to features.
4. Nothing depends on `:app:*`.

Enforced by a `checkModuleGraph` Gradle task in CI, mirrored from PikaPal.

### Convention plugins (`build-logic/`) `[doc: PikaPal]`

- `focusbloom.kmp.library` — base KMP library: the four targets, JVM 17, namespace from
  path, serialization, host test source set.
- `focusbloom.compose` — applies Compose Multiplatform + compiler.
- `focusbloom.kmp.feature` — the two above, plus wiring every feature to `:core:model`,
  `:core:common`, `:core:designsystem`, Compose runtime/foundation/ui/resources,
  lifecycle-viewmodel-compose, and test deps (`:core:testing`, coroutines-test, Turbine).
  Deliberately **no Material dependency**: screens are built only from jenga.

### Deliberate divergences from PikaPal `[inference]`

- **Four targets, not two.** PikaPal is Android + iOS. We add `:app:desktop`, `:app:web`,
  and the `desktop` + `wasmJs` KMP targets throughout. This is the single biggest source of
  added work and risk, and is the reason for several decisions below.
- **SQLDelight, not Room.** Room KMP has no wasmJs target; copying PikaPal would kill Web.
  We keep SQLDelight (proven here, spans all four via `web-worker-driver`). Apply, not copy.

---

## 5. The MVI contract (the canonical shape to clone)

Hand-rolled, ViewModel-based, three types, no shared base class. Copied per feature exactly
as PikaPal does `[doc: PikaPal TodayContract/TodayViewModel/TodayScreen]`.

One `XxxContract.kt` per feature:

```kotlin
@Immutable
data class XxxUiState(
    // every field defaulted; loading/error/empty modeled explicitly as fields
    val isLoading: Boolean = true,
    val error: String? = null,
    // ...
) {
    val showEmpty: Boolean get() = /* derived */
}

sealed interface XxxUiEvent {         // intents in
    data object Load : XxxUiEvent
    data class Choose(val id: Long) : XxxUiEvent
}

sealed interface XxxUiEffect {        // one-shot out, never re-rendered
    data class Navigate(val key: FocusBloomKey) : XxxUiEffect
    data class ShowMessage(val text: String) : XxxUiEffect
}
```

`XxxViewModel : ViewModel()`:

```kotlin
class XxxViewModel(
    private val session: SessionRepository,          // constructor-injected collaborators
    private val analytics: Analytics = NoOpAnalytics, // optional ones default to a no-op
) : ViewModel() {
    private val _uiState = MutableStateFlow(XxxUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<XxxUiEffect>(Channel.BUFFERED)
    val uiEffect: Flow<XxxUiEffect> = _uiEffect.receiveAsFlow()

    init { observe(); load() }

    fun onEvent(event: XxxUiEvent) = when (event) { /* exhaustive */ }

    private fun updateState(block: XxxUiState.() -> XxxUiState) = _uiState.update(block)
}
```

Two composables per screen:

```kotlin
// stateful half
@Composable fun XxxScreen(viewModel: XxxViewModel, /* nav callbacks */) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ObserveAsEvents(viewModel.uiEffect) { effect -> when (effect) { /* ... */ } }
    XxxScreenContent(state, onEvent = viewModel::onEvent)
}

// pure, previewable half, no VM
@Composable fun XxxScreenContent(state: XxxUiState, onEvent: (XxxUiEvent) -> Unit) {
    // renders a `when` over the explicit state machine, built entirely from Jenga blocks
}
```

`ObserveAsEvents` (lifecycle-safe effect collector, `repeatOnLifecycle(STARTED)` +
`Dispatchers.Main.immediate`) lives in `:core:designsystem`, cloned from PikaPal `[doc]`.

Per-feature layout is flat (no data/domain/presentation inside a feature): `XxxContract.kt`,
`XxxViewModel.kt`, `XxxScreen.kt`, `commonTest/XxxViewModelTest.kt` (Turbine),
`androidMain/XxxScreenContentPreviews.kt`, `commonMain/composeResources/` strings.

---

## 6. Design system: jenga to the core

`[doc: PikaPal core:designsystem]` jenga is consumed only through `:core:designsystem`,
which does `api(libs.jenga)`. Features import `com.joelkanyi.focusbloom.core.designsystem.*`
and get jenga transitively. Never `io.github.joelkanyi.jenga` directly outside the swap-point,
never Material, never raw `Color(0x..)` or `.dp` literals in features (enforced by test).

Branded theme wraps `JengaTheme` `[ref: jenga]`:

```kotlin
@Composable fun FocusBloomTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) focusBloomDarkColors() else focusBloomLightColors()
    JengaTheme(darkTheme = darkTheme, colors = colors) { content() }
}
```

Editorial ink brand: near-monochrome `JengaColors` (ink/paper roles) with a single bloom
accent mapped to `brand`. Tuned via `JengaColors.copy(...)` from `jengaLightColors()` /
`jengaDarkColors()`. Accent value to be finalised in Phase 0 (a `copy` on tokens, cheap to
change).

FocusBloom-specific composites (a timer ring, a session card, a day-timeline block) live in
`:core:designsystem` over generic jenga blocks, prefixed `FocusBloom*`, exactly as PikaPal
puts `PikaPal*` composites over jenga `[doc]`. Rule when jenga lacks something: improve jenga
upstream, never hand-roll in a feature `[doc]`.

### jenga upstream work (Phase 0 hard dependency) `[inference]`

jenga currently targets Android, desktop-JVM, iosArm64, iosSimulatorArm64 only (verified in
`jenga/jenga/build.gradle.kts`). Web needs a **`wasmJs` target added to jenga**, its Compose
Resources (Outfit fonts + icons) verified on wasm, then a version published that includes it.
Until that ships there is no web UI. This is the critical-path item.

---

## 7. Stack, verified for all four targets

| Concern | Choice | wasmJs | Desktop | Provenance |
|---|---|---|---|---|
| DI | **Metro** (`dev.zacsweers.metro`) | Yes | Yes | `[ref]` doctrine KB `kmp-compile-time-di`; wasm/desktop verified |
| Navigation | **Navigation3** (`org.jetbrains.androidx.navigation3`) | Yes | Yes | `[doc: PikaPal]`; web landed in Compose MP 1.10.0, we're on 1.10.3 |
| Data | **SQLDelight** 2.1.0 | Yes | Yes | `[inference]`; `web-worker-driver`, `generateAsync=true` for wasm |
| Design system | **jenga** | after upstream | Yes | `[doc: PikaPal]`; needs wasm target |
| Compose | Compose Multiplatform 1.10.3 | Yes | Yes | already in project |

### DI: Metro graphs `[doc: PikaPal AppGraph]`

One `@DependencyGraph(AppScope::class)` per composition root, binding each capability api to
its impl; a missing or duplicate binding is a compile error. ViewModels provided fresh,
singletons via `@SingleIn`. Shared UI stays DI-agnostic behind a `FocusBloomDependencies`
interface read via `staticCompositionLocalOf` (the composition-local seam PikaPal uses), so
the four platforms each assemble their own graph while the feature code never sees DI types.

### Navigation: Nav3 with serializable keys `[doc: PikaPal]`

Routes owned by leaf `:core:navigation`:

```kotlin
sealed interface FocusBloomKey : NavKey
@Serializable data object TodayKey : FocusBloomKey
@Serializable data class SessionKey(val taskId: Long?) : FocusBloomKey
```

Type-safe args, `@Serializable` so the back stack survives process death. Features never
reference keys directly; the shell maps feature nav callbacks onto keys.

### Data: SQLDelight for four targets `[inference / ref: memory project_sqldelight_wasm]`

`:core:database` owns `.sq` schema, driver factories per platform (Android/iOS/JVM already
exist; wasm uses `web-worker-driver`). `generateAsync = true` is required for wasm, which
forces the `awaitAsList()` / `awaitAsOne()` async API. To keep one common repository surface,
capability impls use the coroutines-extensions async API uniformly. Repositories live in
`:capability:impl`, interfaces in `:capability:api`, DAOs never leave the impl.

---

## 8. Platform strategy (four first-class)

- `:app:android` — Metro graph, Activity host, Android SQLDelight driver, notifications.
- `:app:desktop` — Metro graph, window host, JVM driver, desktop notification glue.
- `:app:web` — Metro graph, wasm entry, web-worker SQLDelight, webpack copy of `sql-wasm.wasm`.
- `ios/` + `:shared` — iOS Metro graph, native driver, SwiftUI/Compose entry.

Each phase is only "done" when it holds on all four. A per-phase smoke check compiles and
launches each target. Known per-platform risks tracked in section 11.

---

## 9. Phased roadmap (PR-sized slices)

Each phase is a set of small, reviewable PRs onto an integration branch, not one mega-PR.

- **Phase 0 — Foundation (no user-visible product yet)**
  0.1 jenga gains `wasmJs` target, resources verified, version published. (jenga repo)
  0.2 New Gradle skeleton: `build-logic/` convention plugins, settings with the module tiers,
      `checkModuleGraph` task, `kotlin.code.style=official`.
  0.3 `:core:model`, `:core:common` (Clock, DispatcherProvider, Analytics/NoOp), `:core:testing`.
  0.4 `:core:designsystem`: `api(libs.jenga)`, `FocusBloomTheme`, editorial-ink brand tokens,
      `ObserveAsEvents`, first `FocusBloom*` composites.
  0.5 `:core:database` (SQLDelight, four drivers, async), `:core:datastore` (settings).
  0.6 `:core:navigation` (keys, navigator), the four `:app:*` shells with Metro graphs and the
      `FocusBloomDependencies` seam. Empty app boots on all four.

- **Phase 1 — Focus-first core.** `:capability:session` (api+impl), `:feature:focus` (the hero
  session state machine), `:feature:today`, minimal `:capability:tasks`, `:feature:onboarding`,
  minimal `:feature:you`. Ships a whole focus-first app on four platforms.

- **Phase 2 — Plan.** Full `:capability:tasks` + `:capability:planning`, `:feature:plan`
  (tasks + reimagined calendar timeline).

- **Phase 3 — Insights.** `:capability:insights`, `:feature:insights` (reflection, gentle trends).

- **Phase 4 — Habits and goals.**

- **Phase 5 — Notes and review.**

Migration note: the old single-`:shared` code is not refactored in place. It is the reference
we read while building the new modules, then retired. No behavior-preserving net exists because
Joel chose full reimagining; correctness is held by new Turbine ViewModel tests + previews.

---

## 10. Testing and quality gates

- `explicitApi()` on every `:core:*` and `:capability:api` module (public library surface),
  Binary Compatibility Validator with committed `.api` / `.klib.api` dumps on those. Not on
  features or apps `[ref: doctrine §14; PikaPal omits it on features]`.
- `checkModuleGraph` architecture test in CI.
- A "no raw dimensions/colors/Material in features" test, mirrored from PikaPal.
- Per-feature `commonTest` ViewModel test with Turbine + coroutines-test.
- Android `@Preview` per state; jenga already carries Roborazzi goldens for its own components.
- Contrast/accessibility check on the brand palette.
- spotless + ktlint, license header file, `kotlin.code.style=official`.

---

## 11. Risks and open items

- **jenga wasm target (critical path).** Compose Resources on wasm, icon/font loading. Must be
  proven before any web UI. Mitigation: Phase 0.1 is a spike with a throwaway wasm sample.
- **Metro on wasm/native.** Verified supported; contribution-hint generation limited on
  native/Wasm before Kotlin 2.3.20-Beta1 but works within a compilation. Mitigation: keep the
  graph in the composition roots, not in common wasm code, and pin a known-good Kotlin/Metro pair.
- **SQLDelight async API.** `generateAsync=true` changes the query API app-wide. Mitigation:
  standardise on the async extensions from the start so there is no sync/async split to unwind.
- **Nav3 maturity on desktop/web.** New. Mitigation: a Phase 0.6 smoke test navigating between
  two empty screens on all four before feature work.
- **Four-target CI cost.** macOS runners are ~10x `[ref: memory github-actions-billing]`.
  Mitigation: split-by-target jobs, one simulator target, publish-on-tag, concurrency cancel-CI.
- **Brand accent value.** Deferred to Phase 0.4, a `copy()` on tokens, cheap.

---

## 12. Workflow

- Integration branch off `main` for the reimagining; each slice is its own `<type>/<kebab-title>`
  branch PR'd into it `[ref: doctrine dlight-branch-naming]`.
- PR titles/descriptions, commit messages, issues authored by Joel in his voice; I draft
  locally, he owns what ships `[ref: Lysine policy]`. No AI attribution on any surface.
- jenga changes ship as their own PRs in the jenga repo (0.1), released before FocusBloom
  consumes them.

---

## 13. Provenance summary

| Decision | Source |
|---|---|
| Multi-module tiers + module-graph law | `[doc]` PikaPal `settings.gradle.kts`, root `CheckModuleGraphTask` |
| MVI three-type contract, two-composable split, `ObserveAsEvents` | `[doc]` PikaPal today feature |
| Metro DI, graphs in composition roots, composition-local seam | `[doc]` PikaPal `AppGraph`; `[ref]` KB `kmp-compile-time-di` |
| Navigation3 serializable keys | `[doc]` PikaPal `:core:navigation`; wasm support `[web]` Compose MP 1.10.0 |
| jenga through `:core:designsystem`, no Material/raw literals in features | `[doc]` PikaPal; `[ref]` jenga catalog |
| Keep SQLDelight, reject Room | `[inference]` Room has no wasmJs; Web is first-class |
| Add wasmJs target to jenga | `[inference]` verified jenga has no wasm target |
| explicitApi + BCV on core/api only | `[ref]` doctrine §14; PikaPal omits on features |
| Four first-class platforms, full reimagining, editorial-ink brand | Joel, this session |
</content>
</invoke>
