# Jansen Ken Pegrasio - Project Portfolio Page

## Overview

InventoryBRO is a CLI-based inventory management application for small business owners who prefer typing over mouse-driven UIs. It supports adding, editing, deleting, filtering, and transacting inventory items, with built-in tab-completion and typo detection to speed up daily use.

---

## Summary of Contributions

### Code Contributed

[View my code contributions on the tP Code Dashboard](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=kenpegrasio)

---

### Enhancements Implemented

#### 1. Add Item Command (`addItem`)

- **What it does:** Allows the user to add a new item to the inventory with a name and an initial quantity using the format `addItem d/NAME q/INITIAL_QUANTITY`.
- **Justification:** This is the core create operation of the inventory — without it, there is nothing to manage.
- **Highlights:**
  - Format is enforced via a strict regex (`^addItem d/(.*?) q/(\d+)$`) shared between `AddCommand` and `AddCommandValidator`.
  - Duplicate name detection is handled by `DuplicateItemValidator`, which performs a case-insensitive comparison against all existing items. This prevents silent data corruption from near-identical names like `Apple` and `apple`.
  - Validation is fully separated from execution following the project's SLAP principle: `AddCommandValidator` throws `IllegalArgumentException` before any mutation occurs.
- **Files:** `AddCommand.java`, `AddCommandValidator.java`, `AddCommandTest.java`, `AddCommandValidatorTest.java`

---

#### 2. Filter Item Command (`filterItem`)

- **What it does:** Filters the inventory by one or more predicates on `description`, `quantity`, or `price`, combined with `AND`/`OR` logic. For example: `filterItem quantity > 10 AND quantity < 40`.
- **Justification:** As an inventory grows, a simple list is unwieldy. Structured filtering lets users query exactly what they need without scrolling through hundreds of items.
- **Highlights:**
  - Supports all three comparison operators (`=`, `<`, `>`) across three field types (`description`, `quantity`, `price`).
  - Implements AND-before-OR evaluation (AND binds tighter), achieved by grouping consecutive AND predicates into "AND-groups" and evaluating OR between groups.
  - `FilterCommandValidator` validates structural correctness (gaps between predicates must be exactly `AND` or `OR`), field–value type consistency (description values must be single-quoted; quantity/price values must be non-negative integers), and trailing text.
  - The predicate parser uses a single regex (`(description|quantity|price) (=|<|>) ('.*?'|[^\s']+)`) in both validator and command, following the project convention of no shared utility class.
- **Files:** `FilterCommand.java`, `FilterCommandValidator.java`, `FilterCommandTest.java`, `FilterCommandValidatorTest.java`

---

#### 3. Command Autocompletion (Trie + JLine)

- **What it does:** When the user presses `Tab` in the terminal, InventoryBRO completes or suggests command keywords based on the prefix typed so far (e.g., `add` → `addItem`; `f` → `filterItem`, `findItem`).
- **Justification:** Command autocompletion reduces the cognitive load of remembering exact command names and speeds up repeated use.
- **Highlights:**
  - Built a `Trie` data structure from scratch (`Trie.java`, `TrieNode.java`) that stores command keywords in lowercase and retrieves them with original casing. Prefix traversal is O(k) where k is the prefix length; full word collection is DFS over the remaining subtree.
  - `Autocompleter` wraps the `Trie` and derives its word list from the `CommandWord` enum — the single source of truth — so adding a new command keyword to the enum automatically makes it tab-completable with no additional wiring.
  - Integrated with JLine3's `Completer` API in `Ui`, which intercepts `Tab` keystrokes in interactive terminal sessions (JAR path only; gracefully degrades to no-op when stdin is piped).
  - Matching is case-insensitive: typing `ADD` still completes to `addItem`.
- **Files:** `Autocompleter.java`, `Trie.java`, `TrieNode.java`, `AutocompleterTest.java`, `TrieTest.java`

---

#### 4. Typo Detection (`TypoDetector`)

- **What it does:** When the user enters an unrecognised command (e.g., `adItem`), InventoryBRO automatically suggests the closest known command: `"Do you mean addItem?"`.
- **Justification:** Typos are a common source of frustration in CLI tools. A helpful suggestion dramatically reduces the feedback loop for mistyped commands.
- **Highlights:**
  - Implements a **QWERTY-weighted edit distance** algorithm: the substitution cost between two characters is their Manhattan distance on a standard QWERTY keyboard layout (e.g., replacing `s` with `d` costs less than replacing `s` with `p`). This means physically adjacent key swaps — the most common typing errors — are penalised less than arbitrary substitutions.
  - Uses a dynamic-programming approach (Wagner–Fischer) with floating-point costs to accumulate fractional keyboard distances across the full word.
  - A configurable `TYPO_THRESHOLD_FACTOR` (0.2 × max word length) prevents the detector from suggesting commands that are not genuinely similar, avoiding false positives for completely unrelated inputs.
  - Runs automatically on every unrecognised command with no configuration required.
- **Files:** `TypoDetector.java`, `TypoDetectorTest.java`

---

### User Guide Contributions

I authored the following sections of the User Guide:

- **Section 1 — Adding an Item (`addItem`):** format, constraints, and example output
- **Section 6 — Filtering Items (`filterItem`):** format, operator reference table, and four worked examples (single predicate, AND, OR, price filter)
- **Command Autocompletion section:** how tab-completion works, the behavior table, and the limitations note (JAR-only, keyword-only, case-insensitive)
- **Typo Detection section:** explanation of the QWERTY distance model, the suggestion behavior table, and the note that it runs automatically

---

### Developer Guide Contributions

I authored the following sections of the Developer Guide:

- **Add Item — Design & Implementation:** step-by-step sequence walkthrough from user input through `Parser` → `AddCommand` → `AddCommandValidator` → `DuplicateItemValidator` → `ItemList`, with the class and sequence diagrams
- **Filter Item — Design & Implementation:** predicate extraction, AND-group building, OR evaluation, validator gap-checking, and the class and sequence diagrams
- **Command Autocompletion (Trie & JLine) — Design & Implementation:** Trie construction sequence, tab-completion sequence (JLine → `Autocompleter` → `Trie`), and the class and sequence diagrams (Figures 26–27)
- **Typo Detection — Design & Implementation:** weighted edit distance algorithm walkthrough, threshold calculation, and the class and sequence diagrams (Figures 28–29)

---

### Team-Based Tasks

- Set up the command and validator layer separation (SLAP principle) as the standard pattern
- Reviewed and provided feedback on PRs related to the edit item and help command
- Resolved test coverage bug where `System.exit(0)` terminate the entire JVM
---

## Contributions to the User Guide (Extracts)

### Adding an Item: `addItem`

Adds a new item to the inventory.

**Format:** `addItem d/NAME q/INITIAL_QUANTITY`

| Parameter | Meaning |
|---|---|
| `NAME` | The item's display name. Must not already exist in the inventory (case-insensitive). |
| `INITIAL_QUANTITY` | A non-negative integer representing the starting stock level. |

**Example:** `addItem d/Coke Can q/50`

```
Added: Coke Can | Qty: 50
```

---

### Filtering Items: `filterItem`

Filters and displays inventory items matching one or more field–operator–value predicates.

**Format:** `filterItem FIELD OPERATOR VALUE [AND|OR FIELD OPERATOR VALUE ...]`

| FIELD | OPERATOR | VALUE |
|---|---|---|
| `description` | `=`, `<`, `>` | Text enclosed in single quotes, e.g. `'Coke Can'` |
| `quantity` | `=`, `<`, `>` | Non-negative integer, e.g. `10` |
| `price` | `=`, `<`, `>` | Non-negative integer, e.g. `5` |

AND binds before OR: `A AND B OR C` means `(A AND B) OR C`.

**Example — single predicate:**
```
filterItem quantity > 10
```
**Example — AND (both conditions must hold):**
```
filterItem quantity > 10 AND quantity < 40
```
**Example — OR (either condition matches):**
```
filterItem description = 'Coke Can' OR description = 'Sprite Bottle'
```

---

## Contributions to the Developer Guide (Extracts)

### Command Autocompletion (Trie & JLine)

The autocompletion mechanism is handled by the `Autocompleter` class, which wraps a custom `Trie` data structure and integrates with JLine3's `Completer` API.

**Construction sequence:**
1. `Ui` constructs a new `Autocompleter`.
2. `Autocompleter` calls `buildTrie()`, which iterates every `CommandWord.values()` entry and inserts each keyword into the `Trie` (lowercased character-by-character; original casing stored at the terminal node).

**Tab-completion sequence (user presses Tab):**
1. JLine3 intercepts the keystroke and calls `Ui.complete()`.
2. `Ui.complete()` calls `autocompleter.getMatches(partial)`.
3. `Autocompleter` delegates to `trie.findWithPrefix(prefix.toLowerCase())`.
4. `Trie.navigateTo()` walks down to the prefix node; `collectWordsRecursively()` performs DFS to collect all terminal nodes in the subtree.
5. JLine3 displays the results inline or completes unambiguously.

The `Autocompleter` derives its word list from `CommandWord` — the single source of truth — so any new command keyword added to the enum is automatically tab-completable.

---

### Typo Detection

When a user enters an unrecognised command, `InventoryBro.handleUnknownCommand()` extracts the first word and calls `TypoDetector.findClosestMatch()`.

**Algorithm:**
- Computes a **QWERTY-weighted edit distance** for each known command using dynamic programming (Wagner–Fischer). The substitution cost between two characters is their Manhattan distance on a standard QWERTY keyboard layout, so physically adjacent key swaps cost less than arbitrary substitutions.
- Selects the command with the lowest weighted distance.
- Applies a threshold (`0.2 × max(inputLength, commandLength)`): only suggests if the best distance is below this threshold, preventing false positives for completely unrelated input.

**Design rationale:** Using keyboard distance rather than uniform substitution cost (Levenshtein) makes the detector more accurate for the most common real-world typos (adjacent-key swaps like `addItem` → `adItem`) while remaining resistant to suggesting commands for inputs that are simply not commands at all.
