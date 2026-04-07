# InventoryBRO - Developer Guide

## Table of Contents
1. [Acknowledgements](#acknowledgements)
2. [Design](#design)
    * [Architecture](#architecture)
    * [UI Component](#ui-component)
    * [Parser Component](#parser-component)
3. [Implementation](#implementation)
    * [Adding an Item](#adding-an-item)
    * [Deleting an Item](#deleting-an-item)
    * [Finding an Item](#finding-an-item)
    * [Filtering Items](#filtering-items)
    * [Transacting an Item](#transacting-an-item)
    * [Viewing Transaction History](#viewing-transaction-history)
    * [Viewing list of items in the inventory](#viewing-list-of-items-in-the-inventory)
    * [Viewing help instructions of how to use commands](#Viewing-help-instructions-of-how-to-use-commands)
    * [Storage System](#storage-system)
    * [Command Autocompletion (Trie & JLine)](#command-autocompletion)
    * [Typo Detection](#typo-detection)
4. [Proposed/Planned Features](#proposedplanned-features)
    * [Storage & Data Persistence](#storage--data-persistence)

---

## Acknowledgements
* **JLine3**: Used for implementing the interactive terminal, intercepting keystrokes, and providing tab-autocompletion functionality for commands.
* **PlantUML**: Used to generate the UML diagrams in this guide.

---

## Design

### Architecture
The architecture of InventoryBRO strictly adheres to Object-Oriented principles and utilizes the **Command Pattern** to decouple the parsing of user input from the execution of the application's core logic.

The main components are:
* `Ui`: Handles all interactions with the user.
* `Parser`: Interprets input and creates `Command` objects.
* `Command`: Interface implemented by all commands.
* `ItemList` / `Item`: Core data model.
* `Storage`: Handles persistence of inventory and transaction data.

**Figure 1: Overall Architecture / Class Diagram**  
![InventoryBro Class Diagram](diagrams/InventoryBroClassDiagram.png)

---

### UI Component
The `Ui` class acts as the bridge between the user and the internal logic.

**Figure 2: UI Class Diagram**  
![UI Class Diagram](diagrams/UiClassDiagram.png)

**Design highlights:**
* Detects interactive vs automated mode
* Uses JLine for autocompletion
* Falls back to BufferedReader for testing

---

### Parser Component
The `Parser` is responsible for routing user input to the correct command.

**Figure 3: Parser Class Diagram**  
![Parser Class Diagram](diagrams/ParserClassDiagram.png)

**Design highlights:**
* Uses switch-based factory pattern
* Integrates TypoDetector for suggestions

---

## Implementation
### Adding an Item

The add mechanism is handled by the `AddCommand` class. It validates the input, creates a new `Item`, and appends it to the inventory.

**Figure 13: Add Command Class Diagram**
![Add Command Class Diagram](diagrams/AddCommandClassDiagram.png)

**Step-by-step Execution:**
1. The user inputs `addItem d/Apple q/50`.
2. `Parser` matches the `addItem` prefix and instantiates a new `AddCommand` with the raw input string.
3. `Parser` calls `execute(items, ui)` on the `AddCommand`.
4. `AddCommand.execute()` immediately creates a new `AddCommandValidator` and calls `validate(items)`.
5. `AddCommandValidator` applies the regex `^addItem d/(.*?) q/(\d+)$` to the input. If it does not match, it throws `IllegalArgumentException` with `"Invalid addItem format! Use: addItem d/NAME q/INITIAL_QUANTITY"`. If the format is valid, it trims the captured name and delegates to `DuplicateItemValidator`, which iterates the `ItemList` performing a case-insensitive name comparison; a match throws `IllegalArgumentException` with `"An item named '<NAME>' already exists in the inventory."`.
6. If validation passes, `AddCommand` re-applies the same regex to extract the trimmed name and parses the quantity as an integer.
7. A new `Item` is constructed and appended to the `ItemList` via `items.addItem(newItem)`.
8. `ui.showMessage("Added: " + newItem)` confirms the addition to the user.

**Figure 14: Add Command Sequence Diagram**
![Add Command Sequence Diagram](diagrams/AddCommandSequenceDiagram.png)

---

### Adding an Item

The add mechanism is handled by the `AddCommand` class. It validates the input, creates a new `Item`, and appends it to the inventory.

**Figure 13: Add Command Class Diagram**
![Add Command Class Diagram](diagrams/AddCommandClassDiagram.png)

**Step-by-step Execution:**
1. The user inputs `addItem d/Apple q/50`.
2. `Parser` matches the `addItem` prefix and instantiates a new `AddCommand` with the raw input string.
3. `Parser` calls `execute(items, ui)` on the `AddCommand`.
4. `AddCommand.execute()` immediately creates a new `AddCommandValidator` and calls `validate(items)`.
5. `AddCommandValidator` applies the regex `^addItem d/(.*?) q/(\d+)$` to the input. If it does not match, it throws `IllegalArgumentException` with `"Invalid addItem format! Use: addItem d/NAME q/INITIAL_QUANTITY"`. If the format is valid, it trims the captured name and delegates to `DuplicateItemValidator`, which iterates the `ItemList` performing a case-insensitive name comparison; a match throws `IllegalArgumentException` with `"An item named '<NAME>' already exists in the inventory."`.
6. If validation passes, `AddCommand` re-applies the same regex to extract the trimmed name and parses the quantity as an integer.
7. A new `Item` is constructed and appended to the `ItemList` via `items.addItem(newItem)`.
8. `ui.showMessage("Added: " + newItem)` confirms the addition to the user.

**Figure 14: Add Command Sequence Diagram**
![Add Command Sequence Diagram](diagrams/AddCommandSequenceDiagram.png)

---

### Deleting an Item

**Figure 15: Delete Command Class Diagram**
![Delete Class Diagram](diagrams/DeleteClassDiagram.png)

**Step-by-step Execution:**
1. When the user inputs `deleteItem 1`, the `Parser` instantiates a new `DeleteCommand` with the raw input string.
2. The `Parser` invokes the `execute(items, ui)` method on the `DeleteCommand`.
3. The `DeleteCommand` immediately creates a `DeleteCommandValidator` and calls `validate(items)`.
4. The `DeleteCommandValidator` uses Regex (`^deleteItem\s+(\d+)$`) to ensure the format is correct. If the format is invalid or the parsed index is out of bounds, it throws an `IllegalArgumentException` which halts execution.
5. If validation passes, `DeleteCommand` calculates the zero-based index and calls `deleteItem()` on the `ItemList`.
6. Finally, a success message containing the removed item's details is passed to the `Ui` to be displayed to the user.


**Figure 16: Delete Command Sequence Diagram**
![Delete Sequence Diagram](diagrams/DeleteSequenceDiagram.png)


### Finding an Item

**Figure 17: Find Command Class Diagram**
![Find Class Diagram](diagrams/FindClassDiagram.png)

**Step-by-step Execution:**
1. When the user inputs `findItem keyword`, the `Parser` instantiates a new `FindCommand`.
2. The `Parser` calls `execute(items, ui)` on the command.
3. The `FindCommand` uses a Regex pattern (`^findItem\s+(.+)$`) to extract the search keyword. If the format is invalid, it throws an `IllegalArgumentException`.
4. The command iterates through the `ItemList`, retrieving each `Item` and checking if its description contains the target keyword.
5. Matching items are immediately passed to the `Ui` to be displayed. If no items match by the end of the loop, a "not found" message is displayed instead.


**Figure 18: Find Command Sequence Diagram**
![Find Sequence Diagram](diagrams/FindSequenceDiagram.png)


### Filtering Items

The filter mechanism is handled by the `FilterCommand` class. It evaluates one or more field-operator-value predicates — joined by `AND` / `OR` — against every item in the inventory and displays all matching results.

**Figure 19: Filter Command Class Diagram**
![Filter Command Class Diagram](diagrams/FilterCommandClassDiagram.png)

**Step-by-step Execution:**
1. The user inputs a command such as `filterItem quantity > 10` or `filterItem description = 'Apple' OR quantity < 5`. The `Parser` instantiates a new `FilterCommand` with the raw input string.
2. The `Parser` calls `execute(items, ui)` on the `FilterCommand`.
3. Inside `execute()`, `FilterCommand` immediately creates a `FilterCommandValidator` and calls `validate(items)`.
4. `FilterCommandValidator` first checks that the input starts with `"filterItem "`. It then applies the predicate regex `(description|quantity|price) (=|<|>) ('.*?'|[^\s']+)` to extract all predicate matches and their positions.
5. The validator checks every gap between consecutive matches: the first gap must be empty, each subsequent gap must be exactly `AND` or `OR`, and no trailing text may follow the last predicate. A bad gap throws `IllegalArgumentException` (e.g. `"Expected AND or OR between predicates, found: '...'"` ). It then validates each predicate's value type: `description` values must be single-quoted; `quantity` and `price` values must match `^\d+$`. A type mismatch throws `IllegalArgumentException`.
6. Back in `execute()`, the same regex builds a flat list of `[field, operator, value]` arrays and a corresponding list of joining operators.
7. `buildAndGroups()` splits the flat list into AND-groups: consecutive predicates joined by `AND` stay in the same group; an `OR` starts a new group. This implements AND-before-OR precedence without explicit precedence parsing.
8. `collectMatchingItems()` iterates every `Item` in the `ItemList`. For each item, `passesFilter()` checks whether it satisfies every predicate in at least one AND-group. Within a group, `evaluatePredicate()` resolves each field: `description` uses `String.compareTo`; `quantity` and `price` use `Integer.compare`. The comparator result is tested against `=`, `<`, or `>` by `satisfiesOperator()`.
9. If no items match, `Ui` displays `"No items match the given filter."`. Otherwise it displays `"Here are the filtered items:"` followed by a numbered list of matching items in their original inventory order.

**Figure 20: Filter Command Sequence Diagram**
![Filter Command Sequence Diagram](diagrams/FilterCommandSequenceDiagram.png)

---

### Transacting an Item

The transact mechanism is handled by the `TransactCommand` class. It updates an item's quantity and records the transaction.

**Figure 21: Transact Command Class Diagram**
![Transact Class Diagram](diagrams/TransactCommandClassDiagram.png)

**Step-by-step Execution:**
1. User inputs `transact 1 q/-5`
2. `Parser` creates `TransactCommand`
3. `execute()` is called
4. `TransactCommandValidator` validates format and index
5. Input is parsed to extract:
    * target index
    * quantity change
6. `ItemList.getItem(index)` retrieves the item
7. Item quantity is updated
8. `TransactionStorage.saveHistory()` records the transaction
9. UI displays updated quantity

**Figure 22: Transact Command Sequence Diagram**
![Transact Sequence Diagram](diagrams/TransactCommandSequenceDiagram.png)


### Viewing Transaction History

The `ShowTransactionHistoryCommand` retrieves and displays all past transactions.

**Figure 23: Show History Class Diagram**
![Show History Class Diagram](diagrams/ShowTransactionHistoryCommandClassDiagram.png)

**Step-by-step Execution:**
1. User inputs `showHistory`
2. `Parser` creates the command
3. Validator checks correct usage
4. `TransactionStorage.load()` retrieves all entries
5. If empty → show message
6. Otherwise → iterate and print all entries

**Figure 24: Show History Sequence Diagram**
![Show History Sequence Diagram](diagrams/ShowTransactionHistoryCommandSequenceDiagram.png)

### Viewing list of items in the inventory
**Figure 25: List Command Class Diagram**
![Show List Command Class Diagram](diagrams/ListCommandClassDiagram.png)

**Step-by-step Execution:**
1. When the user inputs `listItems`, the parser instantiates a new `ListCommand` with the raw input string.
2. The `execute` method of `ListCommand` is called.
3. The `execute` method creates `ListCommandValidator` with the raw input string and calls the `validate` method.
4. The `validate` method checks that the raw input string follows the correct format for `listItems` command. If the correct format is not followed, it will throw an `IllegalArgumentException` and halt the execution.
5. Control is returned to the `execute` method which checks if the inventory list is empty and passes a message that the inventory is empty to the `ui` to display to the user.
6. Otherwise, it passes the list of items in the inventory to the `ui` to display to the user.

**Figure 26: List Command Sequence Diagram**
![List Command Sequence Diagram](diagrams/ListCommandSequenceDiagram.png)

### Viewing help instructions of how to use commands
**Figure 27: Help Command Sequence Diagram**
![Show Help Command Class Diagram](diagrams/HelpCommandClassDiagram.png)

**Step-by-step Execution:**
1. The user inputs `help` or specifies a particular command and inputs `help [command_name]`.
2. The parser instantiates a new `HelpCommand` with the raw input string and the `execute` method is called.
3. The `execute` method creates `HelpCommandValidator` with the raw input string and calls the `validate` method.
4. The `validate` method checks that the raw input string follows the correct format for the `help` command. If an invalid command name is given or there are more than one command name specified, an `IllegalArgumentException` is thrown and execution is halted.
5. The `execute` method then checks the raw input string if a particular command name is specified:
    * If yes, then the detailed instruction of that particular command is passed to the `ui` to be displayed to the user.
    * If no, which means the user input is only `help`, then the command names and their summaries are passed to the `ui` to display to the user.

**Figure 28: Help Command Sequence Diagram**
![Help Command Sequence Diagram](diagrams/HelpCommandSequenceDiagram.png)

---
### Storage System

The storage system is responsible for persisting both inventory data and transaction history.

**Figure 25: Storage Class Diagram**
![Storage Class Diagram](diagrams/StorageClassDiagram.png)

**Design breakdown:**

* **Storage (Abstract Class)**
    * Provides generic file handling
    * Defines `saveArray`, `saveHistory`, and `load`

* **ArrayStorage**
    * Handles `ItemList`
    * Converts between `Item` and string format

* **TransactionStorage**
    * Stores transaction history as strings
    * Automatically generates timestamps

**Key Design Decisions:**
* Use of generics (`Storage<T>`) for reusability
* Separation of inventory and transaction files
* Append-only strategy for transaction history



### Command Autocompletion

The autocompletion mechanism is handled by the `Autocompleter` class, which wraps a `Trie` data structure and integrates with JLine's completer API to provide real-time tab-completion of command keywords in interactive terminal sessions.

**Figure 26: Autocompleter Class Diagram**

![Autocompleter Class Diagram](diagrams/AutocompleterClassDiagram.png)

**Step-by-step Execution:**

**Initialisation:**
1. `Ui` is constructed and immediately instantiates a new `Autocompleter`.
2. `Autocompleter`'s constructor calls `buildTrie()`, which creates a new `Trie` (and its root `TrieNode`).
3. `buildTrie()` iterates over every value in `CommandWord.values()` and calls `trie.insert(cmd.getWord())` for each keyword.
4. `Trie.insert()` traverses the trie character by character (lowercased), calling `getOrCreateChild(ch)` on each `TrieNode`. At the terminal node it calls `setKeyword(word)` to store the original-cased keyword, marking that node as an end-of-word.
5. After construction, `Autocompleter` holds a fully populated `Trie` that can answer prefix-match queries for all known command words.

**Tab-completion (user presses Tab):**
1. JLine detects the Tab keystroke and invokes `Ui.complete(reader, line, candidates)`.
2. `Ui.complete()` checks `line.wordIndex() == 0`; if the cursor is not on the first word, it returns immediately (no completion for arguments).
3. `Ui.complete()` calls `autocompleter.getMatches(line.word())`, passing the current partial word as the prefix.
4. `Autocompleter.getMatches()` delegates to `trie.findWithPrefix(prefix)`.
5. `Trie.findWithPrefix()` calls `navigateTo(prefix.toLowerCase())` to walk down the trie to the node corresponding to the prefix. If no such node exists, an empty list is returned.
6. Starting from the prefix node, `collectWords()` recursively visits every descendant node. Any node where `isEndOfWord()` is true contributes its stored keyword to the result list.
7. The list of matching keywords is returned to `Ui.complete()`, which wraps each keyword in a `Candidate` object and adds it to JLine's `candidates` list.
8. JLine displays the candidates to the user in the terminal (inline if only one match, or as a menu if multiple).

**Figure 27: Autocompleter Sequence Diagram**
![Autocompleter Sequence Diagram](diagrams/AutocompleterSequenceDiagram.png)

---

### Typo Detection

When a user enters an unknown command, InventoryBRO attempts to detect whether it is a near-miss typo and suggests the closest known command.

**Figure 28: Typo Detector Class Diagram**
![Typo Detector Class Diagram](diagrams/TypoDetectorClassDiagram.png)

**Step-by-step Execution:**
1. The user inputs an unrecognised command (e.g. `adItem d/Apple q/5`).
2. `Parser.parseCommand()` evaluates the first word against all known command keywords in the switch statement and returns `null` because no branch matches.
3. `Parser.parse()` detects the `null` result and calls `handleUnknownCommand(line, ui)`.
4. `handleUnknownCommand()` extracts the first word from the raw input and calls `TYPO_DETECTOR.findClosestMatch(firstWord)`.
5. `TypoDetector.findClosestMatch()` converts the input to lowercase and iterates over `KNOWN_COMMANDS` (`addItem`, `deleteItem`, `editItem`, `transact`, `listItems`, `help`, `exit`). For each known command it calls `calculateWeightedEditDistance()`, which uses dynamic programming with QWERTY keyboard Manhattan distance as the substitution cost: adjacent keys on the same row cost less than keys far apart, encouraging the algorithm to prefer swaps of physically close keys over arbitrary substitutions.
6. After scoring all commands, `findClosestMatch()` calls `isBelowTypoThreshold()` on the best candidate. The threshold is `TYPO_THRESHOLD_FACTOR (0.2) * max(inputLength, commandLength)`. If the best distance is below this threshold the command name is returned as a non-empty `Optional`; otherwise an empty `Optional` is returned.
7. Back in `handleUnknownCommand()`, if the `Optional` is present, `ui.showMessage("Do you mean " + suggestion + "?")` prompts the user with the suggested correction. If no command qualifies, `ui.showError(...)` displays the full list of valid commands.

**Figure 29: Typo Detector Sequence Diagram**
![Typo Detector Sequence Diagram](diagrams/TypoDetectorSequenceDiagram.png)

---

## Product scope

### Target user profile

InventoryBRO is designed for small shop owners (e.g., “BRO”) who need a simple and fast way to manage their inventory using a Command Line Interface (CLI).

The target user:

Manages a small-scale retail inventory (e.g., drinks, snacks, convenience items)
Prefers typing commands over using graphical interfaces
Requires quick and precise stock updates during daily operations
Has basic familiarity with using a computer terminal
May not have access to complex inventory management systems


**Implementation:** `EditCommand` parses the input in the format `edit INDEX d/NEW_NAME q/NEW_QUANTITY`. It retrieves the item at the given index from `ItemList`, then calls `setDescription()` and `setQuantity()` on it.

InventoryBRO provides a lightweight and efficient CLI-based inventory management system that allows users to:

Track current stock levels in real time
Quickly update inventory through transactions (sales/restocks)
View and manage all items in a structured list
Record and review transaction history for accountability

Unlike complex enterprise systems, InventoryBRO focuses on:

speed (fast command execution)
simplicity (minimal setup, no GUI overhead)
accuracy (clear, structure)


## User Stories
| Version  | As a ...  | I want to ...             | So that I can ...                                           |
|----------|-----------|---------------------------|-------------------------------------------------------------|
| v1.0   | new user       | see usage instructions               | refer to them when I forget how to use the application |
| v1.0   | store owner    | add items                            | keep track of new products in my inventory             |
| v1.0   | store owner    | delete items                         | remove products that are no longer sold                |
| v1.0   | store owner    | edit item details                    | update product name or quantity when needed            |
| v1.0   | store owner    | view all items                       | know what products I currently have                    |
| v1.0   | store owner    | update item quantity via transactions| record sales or restocking accurately                  |
| v1.0   | store owner    | exit the application                 | safely close the program after use                     |
| v2.0   | store owner    | find items by keyword                | locate items quickly without scanning the full list    |
| v2.0   | store owner    | view transaction history             | review past transactions for tracking and reference    |
| v2.0   | store owner | have my inventory automatically saved   | avoid losing data when I close the application           |
| v2.0   | store owner | load previously saved inventory         | continue managing my shop from where I left off          |
| v2.0   | store owner | view detailed instructions for a specific command | learn how to use a command correctly                     |


## Proposed/Planned Features

### Storage & Data Persistence

Future improvements may include:
* Undo/redo functionality
* Backup and restore features

---
