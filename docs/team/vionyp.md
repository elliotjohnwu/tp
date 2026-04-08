# Viony - Project Portfolio Page

## Project: InventoryBRO

InventoryBRO is a desktop application for small shop owners to manage their inventory efficiently via a Command Line Interface (CLI). It supports adding, editing, deleting, and transacting items, with features like autocompletion and typo detection.

---

## Summary of Contributions

### Code Contributed
[View my code on the tP Code Dashboard](https://nus-cs2113-ay2526s2.github.io/tp-dashboard/?search=vionyp)

---

### Enhancements Implemented

#### 1. Edit Description Feature (`editDescription`)
- **What it does:** Allows the user to update an existing item's description: `editDescription INDEX d/NEW_DESCRIPTION`
- **Justification:** Store owners frequently need to rename items without touching other fields. A dedicated command avoids forcing re-entry of unchanged quantity or price values.
- **Highlights:**
  - Implemented `EditDescriptionCommand.java` and `EditDescriptionCommandValidator.java`.
  - Validator enforces format correctness, index bounds, and non-empty description, each with a distinct error message.
  - Added `//@@author vionyp` tags for accurate RepoSense attribution.

#### 2. Edit Price Feature (`editPrice`)
- **What it does:** Allows the user to update an existing item's price: `editPrice INDEX p/NEW_PRICE`
- **Justification:** Prices change frequently in retail. A focused command lets users update price without risk of accidentally overwriting name or quantity.
- **Highlights:**
  - Implemented `EditPriceCommand.java` and `EditPriceCommandValidator.java`.
  - Validator accepts both integer and decimal inputs and rejects negative prices.

#### 3. Edit Quantity Feature (`editQuantity`)
- **What it does:** Allows the user to set an existing item's quantity to a specific value: `editQuantity INDEX q/NEW_QUANTITY`
- **Justification:** Complements `transact` (relative changes) with the ability to set an absolute quantity, useful for stock-takes and corrections.
- **Highlights:**
  - Implemented `EditQuantityCommand.java` and `EditQuantityCommandValidator.java`.
  - Validator rejects negative quantities and non-integer inputs with clear error messages.

#### 4. Exit Feature (`exit`)
- **What it does:** Safely closes the application: `exit`
- **Justification:** A clean exit command ensures the application terminates gracefully after all data has been saved, rather than relying on force-closing the terminal.
- **Highlights:**
  - Implemented `ExitCommand.java`, which displays a farewell message via `Ui` then throws `ExitException` to signal the main loop to terminate.
  - Added `//@@author vionyp` tags for accurate RepoSense attribution.

#### 5. Price Field in `Item` class
- **What it does:** Added a `price` field (stored as `double`) to the `Item` class, with `setPrice()` and `getPrice()` methods, and updated `toString()` to display price in `$X.XX` format.
- **Justification:** Without price tracking, the inventory system would be incomplete for real-world shop use cases.

---

### Contributions to the User Guide

- Wrote the **Editing an Item's Description (`editDescription`)** section (§3), including format, example, and expected output.
- Wrote the **Editing an Item's Price (`editPrice`)** section (§4), including format, example, and expected output.
- Wrote the **Editing an Item's Quantity (`editQuantity`)** section (§5), including format, example, and expected output.
- Added all three commands to the **Command Summary** table.

---

### Contributions to the Developer Guide

- Wrote the **Editing an Item's Description** section under Implementation (Figures 30–31), including:
  - Step-by-step execution walkthrough derived from source
  - PlantUML class diagram (`EditDescriptionCommandClassDiagram.puml`)
  - PlantUML sequence diagram (`EditDescriptionCommandSequenceDiagram.puml`)
- Wrote the **Editing an Item's Price** section (Figures 32–33), including class and sequence diagrams.
- Wrote the **Editing an Item's Quantity** section (Figures 34–35), including class and sequence diagrams.

---

### Contributions to Team-Based Tasks

- Refactored the original monolithic `EditCommand` into three focused commands (`editDescription`, `editPrice`, `editQuantity`), each with its own validator and test class.
- Registered all three new command keywords in `CommandWord.java` so autocomplete and typo detection pick them up automatically.
- Updated `Parser.java` to route `editDescription`, `editPrice`, and `editQuantity` correctly, using individual imports (no wildcard).
- Updated the text-UI test (`input.txt` and `EXPECTED.TXT`) to replace `editName` test cases with the new edit commands.
- Added `//@@author vionyp` tags to all code authored to ensure accurate RepoSense attribution.

---
