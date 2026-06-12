# IntelliJ IDEA File and Code Templates — Automation Tests

Automated UI tests for the **Settings | Editor | File and Code Templates** feature in IntelliJ IDEA Ultimate, built using the [IntelliJ Platform Starter](https://github.com/JetBrains/intellij-community/blob/master/tools/intellij.tools.ide.starter/README.md) and [Driver](https://github.com/JetBrains/intellij-community/tree/master/tools/intellij.tools.ide.starter.driver) frameworks.

---

## Test Cases

### 1. Verify Template Content On Selection
Selects each file template from the list and verifies that the editor area displays the correct expected content for that template.

**Templates covered:**
- HTML File — web file template
- Kotlin File — Kotlin language template
- Class — fundamental Java construct
- Interface — Java abstraction
- Enum — Java enum type
- Record — modern Java (Java 16+) construct

These templates were chosen to cover different categories — web, Kotlin, and core Java constructs — prioritized by developer usage frequency. Full coverage of all 50+ templates would require maintaining expected content for each one, creating high maintenance overhead for minimal additional value. Adding new templates to the `FileTemplate` enum is straightforward when needed.

---

### 2. Verify Custom Template Creation and Persistence
Creates a new custom template with a name, extension, and content, saves it, then reopens the settings to verify:
- The template count increased by 1
- The created template appears in the list with the correct name
- The template extension is saved correctly
- The template content persists after saving

Creating and persisting custom templates is the primary user action on this page — verifying the full create-save-reopen cycle ensures the feature works end to end, not just in isolation.

---

### 3. Verify Tab Switching Shows Correct Templates
Switches between all four tabs (Files, Includes, Code, Other) and verifies that each tab:
- Shows a non-empty template list
- Contains a known template specific to that category

This verifies that tab switching correctly filters templates by category.

---

## Project Structure
```
src/
├── main/kotlin/
│   └── constants/
│       ├── IdeConstants.kt         # IDE navigation constants (section names, version)
│       ├── TextConstants.kt        # UI text constants (tab names, template names, test data)
│       └── FileTemplate.kt         # Enum of file templates with expected content for verification
└── test/kotlin/
    ├── base/
    │   └── BaseTest.kt             # Shared IDE context setup and CI error reporting configuration
    ├── pages/
    │   └── FileAndCodeTemplatesPanel.kt  # Panel object encapsulating all UI interactions
    │                                     # with the File and Code Templates settings panel
    └── FileAndCodeTemplatesTest.kt # Test cases
```

---

## Architectural Decisions

### Page Object Pattern
UI interactions and element locators are encapsulated in `FileAndCodeTemplatesPanel`, keeping test methods focused on assertions rather than UI mechanics. This makes tests readable and maintainable — if a UI element changes, only the panel class needs updating.

### No Steps Layer
In a larger framework with multiple test classes sharing common actions, a dedicated Steps layer would be appropriate. For this assignment scope — one settings panel, three test cases — a Steps layer would add complexity without meaningful benefit. The panel class handles both locators and interactions, which is a deliberate, pragmatic choice for this scope.

### Constants and Enums
All hardcoded values are extracted to constants files and enums. `FileTemplate` enum pairs template names with their expected content, making it easy to add new templates to Test 1 without modifying test logic.

### `assertIsLoaded()` as Precondition
Each test begins with `page.assertIsLoaded()` which verifies the panel is in the correct default state before the actual test logic runs. This ensures test failures are attributed to the right cause — if the panel doesn't load correctly, it fails immediately with a clear message rather than producing a misleading assertion failure later in the test.

---

## Prerequisites

- JDK 17 or higher
- Internet connection on first run (downloads IntelliJ IDEA 2024.3 Ultimate — approximately 1.5GB)
- The downloaded IDE is cached after the first run

## Running the Tests

```bash
./gradlew test
```

Or run individual tests from IntelliJ IDEA by clicking the green play button next to each test method.

Test artifacts including logs and screenshots are saved to: out/ide-tests/tests/

Note: this directory is excluded from version control. Artifacts are generated locally when tests are run.

## Notes

- Tests are written in Kotlin using JUnit 5
- The Starter framework launches a real instance of IntelliJ IDEA 2024.3 Ultimate for each test — this means tests take longer than typical unit tests but provide accurate, real-world verification
- First run may take several minutes due to IDE download and unpacking
- Settings are opened via keyboard shortcut (Cmd+, on macOS, Ctrl+, on Windows/Linux) — this mirrors real user behavior and is more stable than clicking UI elements whose accessible names may change between IDE versions
- The `docs` folder contains the generated Allure HTML report published via GitHub Pages.
    It is intentionally committed to enable the live report at the link above.
    