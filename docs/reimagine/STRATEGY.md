# FocusBloom: The Reimagining Strategy

Status: DRAFT, living document. Product vision for the reimagining, grounded in four
independent research passes (user pain points, competitor paywall teardown, AI/MCP/integrations,
market/monetization). Pairs with DESIGN.md (engineering) which it amends, not replaces.

---

## 1. The pitch and the positioning

**One sentence:** FocusBloom is the focus app for people who organize beautifully and produce
nothing. It hides your backlog, hands you a small honest day, and bends the timer around your
flow instead of shattering it.

**The failure mode we name out loud: productivity theater.** The deepest wound in this market
is not missing features. It is that the tools reward organizing over doing, that managing the
system becomes its own task, and that the aspirational backlog becomes a daily source of dread.
Every competitor sells a better way to arrange work. FocusBloom sells a smaller day and a
finished session.

**The wedge persona: the ADHD-leaning deep-work knowledge worker.**

- Strongest evidenced underserved segment with proven willingness to pay (Tiimo, Sunsama), and
  a loud, word-of-mouth community.
- Every named pain maps to a mechanic below: hyperfocus shattered by rigid Pomodoro, streak
  shame spirals, backlog dread, friction-killed capture, hollow completion.
- Designing for ADHD produces a product that is simply better for everyone. Neurotypical
  deep-work developers (segment two, where a KMP indie app has native credibility) get the same
  benefits without the label. Wedge ADHD, market calm. Expand to developers and students later.

We do not say "ADHD app" on the tin. We name the failure mode ("you plan beautifully and
produce nothing") and let the community self-identify.

## 2. North star and the signature mechanics

**North star metric: focused sessions completed per active user per week.** Not tasks created,
not lists organized, not opens. Every design decision is judged by whether it produces one more
finished session.

**Product truth in one line: draw a handful, bend the timer, keep the page.**

### Mechanic 1: The Handful (today is a loan, not a list)

You never see your backlog by default. The day starts by drawing a **handful**: at most five
tasks pulled from the Shelf into Today. The Shelf (the full pool) is one deliberate tap away,
browsed only when drawing. Overdue items do not glare in red; an undrawn task simply stays on
the Shelf, unjudged.

- Morning: the app shows yesterday's bloom and an empty tray with five slots. You drag or tap
  tasks in. Drawing a sixth requires removing one first. The tray is the whole home screen.
- Stale items **compost**: untouched for 21 days, a task fades ink saturation, then folds into
  the Compost, a reversible archive with a one-line note ("this rested for a while, still
  matters?"). One tap revives, one tap releases.

Nobody ships "the backlog is hidden by design" as the core mechanic. That is the bet, and it is
cheap to build.

### Mechanic 2: The Bending Timer (flow-aware sessions)

The session is a state machine, not a countdown. You set a **soft horizon** (25 / 50 / 90 min).
When the horizon arrives, nothing rings. The ring completes and begins a slow second bloom, and
a whisper-level cue ("in flow? keep going") appears. Doing nothing continues the session. One
tap ends it. Breaks are offered, never imposed, at natural pauses the app notices (you returned
to the app, you paused audio).

- Overrun is celebrated, not flagged: a 74-minute session against a 25-minute horizon renders as
  one long confident stroke, never "session exceeded."
- Interruption is first-class: a "life happened" button parks the session without failing it.
  Parked sessions resume. Nothing resets to zero.

### Mechanic 3: The Day's Page (a felt moment of done)

Completion gets weight through the editorial-ink brand itself. Each finished session inks a
stroke onto the day's page; finishing a task blooms the accent once, briefly. Today can flip to
its **Page**: a generated, beautiful, monochrome summary of what you actually did. Sundays, the
week's pages bind into a **Weekly Page** you can keep or share.

- Presence, not streaks: the only continuity language is "you showed up 5 of the last 7 days."
  No zero, no red, no chain to break. Miss a week and the app says "welcome back", nothing else.
- The shareable Weekly Page is also the organic-growth artifact.

## 3. The product

### Information architecture

The five destinations survive; their contents serve the mechanics.

```
Today          Focus           Plan            Insights        You
the Handful    the Bending     Shelf + Compost pages, presence  profile,
+ the Page     Timer (hero)    + calendar      trends           settings
```

### One record model, many views

One `Task` record. Views over it, never separate silos.

| View | Where | What it shows |
|---|---|---|
| Handful tray | Today | at most 5 drawn tasks, session-startable in one tap |
| Shelf list | Plan | full pool, grouped by project, stale items faded |
| Day timeline | Plan | drawn tasks + calendar imports on one vertical day |
| Board | Plan (later) | Shelf by project/status, for people who think in columns |
| Page | Today / Insights | the day rendered as editorial output |

### Principles made concrete

- **Capture (anti-friction):** one global capture affordance per platform (Android quick tile +
  widget, iOS widget + share sheet, desktop global hotkey, web shortcut). NL date parsing
  ("review PR tomorrow 2pm") ships early. Captured tasks land on the Shelf silently, never
  forcing triage.
- **Zero-setup onboarding:** first launch asks one question ("what is one thing you want to
  finish today?"), creates the task, offers a session. No workspace building, no templates.
  Time to first session under 60 seconds. Opinionated defaults, power options behind
  progressive disclosure.
- **Sessions:** presets, ambient sound, the bending horizon, park/resume, optional one-line note
  on completion.
- **Insights:** where attention went by project, session-length distribution, a presence
  calendar (filled/empty, never red), the bound Weekly Pages. Ink-styled, few, readable charts.
  Basic insights are free.
- **Notifications:** conservative by default. Three categories, each independently mutable, all
  opt-in past the first: session cues, one optional morning "draw your handful" nudge, one
  optional Sunday Page. Hard ceiling of 2 notifications per day by default.
- **Habits (later):** recurring intentions that draw into the Handful, tracked as presence,
  never as chains.
- **Four-platform parity is a feature:** every release ships all four or ships nothing. Mobile
  is the primary surface, not the companion.

## 4. The AI + MCP + integrations layer

Architect all AI behind a capability interface from day one (`:capability:assist:api`) so cloud,
on-device, and none are swappable. One interface now versus a rewrite later.

**Table stakes:** NL date parsing (deterministic parser first, no LLM); one-way ICS calendar
import with an explicit source-of-truth rule and a **visible sync log**; "what did I do this
week" summarization feeding the Weekly Page; a Todoist-shaped webhook API (typed events, HMAC
signatures, delivery IDs).

**Differentiators:**

- **The FocusBloom MCP server.** OAuth-gated, stateless, exposing tools (`create_task`,
  `draw_to_today`, `start_focus_session`, `reschedule`, `compost_task`) and resources (shelf,
  today's handful, weekly summary). The user's Claude or ChatGPT becomes a client of their focus
  life: "look at my shelf and propose tomorrow's handful" is the killer prompt. It is the same
  capability API the features already consume, re-exposed.
- **Approve-then-execute, always.** Any AI or MCP action that mutates data follows propose,
  approve, execute, reversible. Silent autonomy is banned by principle.
- **AI handful drafting in-app:** "draft my day" reads the shelf, calendar, and recent presence,
  proposes five with one-line reasons. AI as a reducer of decisions, not a chat box.

**Moonshots (keep the seams ready):** MCP Apps inline widget rendered inside Claude/ChatGPT;
on-device local-first AI tier (a provider swap behind the seam); long-running "replan my week"
as an async MCP task flow.

## 5. Monetization

Generous free, one paid tier, one lifetime. Names: **FocusBloom** (free), **Bloom** (paid).

| | Free forever | Bloom |
|---|---|---|
| Price | 0 | $3.99/mo, $29.99/yr, $149 lifetime |
| Tasks, sessions, Handful, Compost | unlimited | unlimited |
| Cross-device sync, all four platforms | **free** | free |
| Reminders and notifications | free | free |
| Basic insights + presence | free | free |
| Calendar import | one ICS | unlimited + sync log |
| Weekly Page | view | keep, bind, export, share |
| Deep insights (trends, history > 90 days) | | yes |
| AI handful drafting + summaries | taste (a few/month) | included, no meter, no credits |
| MCP server + webhooks | read-only | full read-write |
| Focus shield (app/site blocking) | basic on-device | scheduled + cross-device |

Why, from the evidence:

- **Free sync is the wedge.** The single most-resented paywall in the category, and the one
  thing FocusBloom's four-platform KMP reach can offer that most focus apps structurally cannot.
- **Free reminders** because charging for them is a running joke, and notification restraint
  makes them cheap.
- **AI included, never metered.** Every competitor treats AI as a separate tax. Cost control
  comes from scoping AI to drafting and summaries, not open chat.
- **$29.99/yr is the category median.** Undercutting does not reliably win, execution does.
  Lifetime at ~5x annual, promoted sparingly, for the subscription-fatigued.
- People pay for sync, analytics, blocking, calendar, AI planning. They will not pay for the
  timer or basic lists. The table follows that line, except sync, which we weaponize.

**Hard rules:** no retroactive caps ever; free data stays free forever. Cancel in two taps, no
dark-pattern retention, clear renewal emails. No hard paywall (kills word-of-mouth). Freemium
with a 14-day full Bloom trial triggered when a user first hits a Bloom feature, not at install.

## 6. Business, ROI, phasing

Honest expectations: median indie never crosses $1K/mo. Realistic ambition is top of the
focus/Pomodoro sub-niche ($412M, 14% CAGR), not top of productivity overall. The niche converts
unusually well (~9.2%).

| Period | State | MRR |
|---|---|---|
| Months 0-6 | Phases 0-1 ship, free only, community building | $0 (deliberate) |
| Months 6-10 | Plan + Insights ship, Bloom launches | $300-1,000 |
| Months 12-18 | AI + MCP live, wedge community compounding | $3K-8K |
| Month 24, top-quartile | sub-niche leadership | $10K-15K |

**Go-to-market wedge motion:**

- Build in public in the KMP/Android dev community first (Joel's native audience, zero CAC,
  segment two). The four-platform KMP story is content in itself.
- ADHD community second, led by the mechanics, not the label. The Handful and the bending timer
  are 30-second-clip shaped. The Weekly Page is the organic loop: every share is a branded
  artifact of real work done.
- ASO: own "gentle focus timer", "ADHD focus timer", "flexible pomodoro", "focus app no
  streaks". The anti-streak position is searchable pain.
- The MCP server launch is a Hacker News moment aimed at developers.
- Body-doubling stays on the roadmap as a community lever, not a year-one feature.

## 7. Reconcile with the build

The DESIGN.md foundation survives almost entirely. This strategy changes what gets built on it.

**Keep unchanged:** the module graph and law, Metro seam, jenga / editorial-ink (the brand is
now load-bearing: the Page mechanic IS the brand), hand-rolled MVI, SQLDelight async, four
first-class platforms, Phase 0 exactly as written (0.1 jenga wasm remains critical path), the
five-destination shell.

**Change:**

- The session state machine gains states DESIGN.md did not name: `SoftHorizonReached` (bending),
  `Parked` (life happened), `Overrun` (celebrated). Bake into the `:capability:session` contract
  from the start.
- Phase 1's minimal task model must already carry the Handful model, or Phase 2 becomes a
  migration. `Task` gains `location` (SHELF / TODAY / COMPOST), `drawnOn: LocalDate?`,
  `lastTouchedAt` (drives compost fade). Cheap now, expensive later.
- Insights reframes from "gentle trends" to Pages + presence: add a `DayPage` record (date,
  session strokes, reflection line) and a presence projection. No streak fields anywhere in the
  schema, so guilt mechanics stay unbuildable by construction.
- Notifications capability enforces the two-per-day ceiling in the contract, not feature code.

**Add:**

- `:capability:assist:api` defined in Phase 0.3 alongside `:core:common` (interface only, no-op
  impl), so AI is a binding swap later.
- **Phase 6, Sync + accounts + Bloom:** first server-side work (sync backend, auth, billing).
  Free-sync-for-all is the launch message.
- **Phase 7, Assist + MCP:** AI drafting, summaries, the MCP server (thin adapter over
  capability apis), webhooks.
- Capture surfaces (widgets, quick tile, hotkey, share sheet) attach to Phase 2.

**Re-sequenced roadmap:** Phase 0 (as designed) -> Phase 1 Focus core with the Bending Timer +
proto-Handful -> Phase 2 Plan: Shelf, Compost, capture surfaces, NL dates, ICS import -> Phase 3
Insights: Pages, presence -> Phase 4 Habits (presence-framed) -> Phase 5 Notes/review (folds
into Pages) -> Phase 6 Sync/monetization -> Phase 7 Assist/MCP. Public beta after Phase 3, money
after Phase 6. Do not let monetization plumbing jump the queue before the mechanics exist to
charge for.

## 8. Risks and what not to do

- **Notion-drift.** Every "just add fields/views/templates" request is a step toward the
  franken-app the audience fled. The Handful cap, the five destinations, and the two-notification
  ceiling are constitutional. Say no by default.
- **Integration over-promising.** Amie died on promised integrations and missing platforms.
  Announce integrations when they ship, never before. One-way ICS before two-way anything.
- **Guilt leaking back in.** Red overdue badges, streak counters, "you missed X" copy: banned.
  Enforce in review like the no-Material rule. The schema-level absence of streaks helps.
- **AI-as-chatbot.** A chat box inside a focus app is scope creep and cost exposure. AI only
  drafts, summarizes, and acts via approve-then-execute.
- **Solo-dev server burn.** Sync + accounts is the phase most likely to stall a solo dev. Keep
  it boring (managed backend, one region). Never gate the local app on it: FocusBloom must be
  fully useful offline forever (also the privacy-trust position).
- **Hard paywall temptation.** Converts better per-install, kills the word-of-mouth this whole
  strategy depends on.
- **Four-platform CI cost** (tracked in DESIGN.md section 11). The "all four or nothing" rule
  makes discipline non-optional.

---

**If we get one thing right:** the moment a person with forty undone tasks opens FocusBloom and
feels relief instead of dread, because the app shows them five things and a timer that bends
around their best hours, and at the end of the day hands them a page proving they did real work.
Everything else, the platforms, the AI, the MCP server, the pricing, exists to protect and
compound that one feeling. Build the app that hides the list and honors the work.
</content>
