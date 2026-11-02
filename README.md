RUNNING TRACKER - JAVA CONSOLE APPLICATION
==========================================


A Java 17 command-line application for recording, reviewing, searching, and deleting running workouts. The project supports Easy Runs, Tempo Runs, Long Runs, and Interval Runs, calculates useful workout metrics, provides run-type-specific feedback, and saves runs to a local CSV file so they can be recovered between application sessions.

This project was developed to demonstrate object-oriented programming (OOP), SOLID design principles, input validation, Java and GitHub best practices, suitable data structures, and automated testing with JUnit 5.


1. PROJECT OVERVIEW
===================


WHAT THE APPLICATION DOES
=========================


  - Log a run: Select one of four workout types and enter the required details.
  - View run history: Display previously recorded workouts and their summaries.
  - Search: Find a run by its unique ID or find runs recorded on a particular date.
  - Delete: Remove a run by ID or use the date-based deletion workflow, with confirmation.
  - Calculate metrics: Display distance, elapsed duration, average pace, and optional heart-rate information.
  - Show specialized feedback: Each run type adds information relevant to that workout.
  - Persist data: Store runs in runs.csv and load them again when the application starts.
  - Recover from invalid input: Reject invalid entries and re-prompt; supported prompts accept B to cancel.


FOUR SUPPORTED RUN TYPES
========================


Easy Run
  Additional input: Perceived effort (1-10)
  Specialized behavior: Displays perceived effort alongside the standard run summary.

Tempo Run
  Additional input: Target pace (seconds per kilometer, entered as a formatted pace)
  Specialized behavior: Compares actual average pace with target pace and reports faster, slower, or equal results.

Long Run
  Additional input: Target distance (km)
  Specialized behavior: Reports whether the distance goal was achieved, exceeded, or not yet reached.

Interval Run
  Additional input: Number of intervals, work/recovery durations, work/recovery distances
  Specialized behavior: Calculates total workout duration and distance from repeated work and recovery intervals.


All run types share a unique run ID, distance, duration, optional average heart rate, and run date. Summaries may include heart-rate-zone feedback when a heart rate is provided.


2. TECHNOLOGY STACK
===================


  - Java 17: Core language and object-oriented design.
  - Maven: Project configuration, dependencies, and test execution through pom.xml.
  - JUnit Jupiter 5.11.4: Unit testing.
  - Maven Surefire 3.5.2: Runs JUnit tests in the Maven lifecycle.
  - JaCoCo 0.8.13: Generates code-coverage reports.
  - Java standard library: Scanner, Duration, LocalDate, List, Map, HashMap, ArrayList, Optional, file I/O, and streams.
  - CSV: Local file-based persistence; no database server or external service is required.
  - Git/GitHub: Source control and commit history.


3. GETTING STARTED
==================


REQUIREMENTS
============


Install JDK 17 or newer and Maven. Eclipse can also import the project as an existing Maven project.


BUILD AND TEST
==============


From the directory containing pom.xml:

mvn clean verify

This compiles the application and tests, executes the JUnit suite, and generates a JaCoCo report. To run only the tests:

mvn test


RUN THE APPLICATION
===================


Compile the application if necessary:

mvn compile

Then launch its main class:

java -cp target/classes runner_app.RunningTrackerApp

Alternatively, in Eclipse, open src/main/java/runner_app/RunningTrackerApp.java and select Run As -> Java Application.

Working-directory note: CsvArchive uses the relative file name runs.csv. Start the program from the project root to keep the CSV file in the expected location. The file is created/updated as runs are saved. Do not run the application and CSV-related tests simultaneously because both use the same relative file name.


MAIN MENU
=========


The console application provides options to add a run, view run history, search for a run, delete a run, or exit. Follow the on-screen prompts to choose the run type and provide the appropriate values.


4. PROJECT STRUCTURE AND RESPONSIBILITIES
=========================================


runner_app/
|-- pom.xml
|-- README.md
|-- runs.csv                         # Local run data, created/updated during use
|-- src/
|   |-- main/java/runner_app/
|   |   |-- Run.java                 # Abstract base class and shared run logic
|   |   |-- EasyRun.java             # Easy-run effort and summary
|   |   |-- TempoRun.java            # Tempo target pace and comparison
|   |   |-- LongRun.java             # Long-run distance goal comparison
|   |   |-- IntervalRun.java         # Interval workout calculations
|   |   |-- RunArchive.java          # Persistence interface
|   |   |-- CsvArchive.java          # CSV-backed implementation of RunArchive
|   |   |-- RunService.java          # Run-management business operations
|   |   |-- InputHandler.java        # Validated console input
|   |   |-- InputCancelledException.java
|   |   |-- RunningTrackerUI.java    # Console presentation and prompts
|   |   \-- RunningTrackerApp.java   # Menu workflow and main entry point
|   \-- test/java/runner_app/
|       |-- RunTest.java
|       |-- EasyRunTest.java
|       |-- TempoRunTest.java
|       |-- LongRunTest.java
|       |-- IntervalRunTest.java
|       |-- RunServiceTest.java
|       |-- InputHandlerTest.java
|       \-- CsvArchiveTest.java
\-- target/                         # Maven-generated output (do not commit)


ARCHITECTURAL FLOW
==================


User at console
      |
      v
RunningTrackerApp  <---->  RunningTrackerUI
      |
      +----> InputHandler  ----> validated values / cancellation
      |
      v
  RunService
      |
      v
 RunArchive (interface)
      ^
      |
 CsvArchive  <----> runs.csv
      |
      +----> Run / EasyRun / TempoRun / LongRun / IntervalRun

RunningTrackerApp coordinates the workflow. RunningTrackerUI handles user-facing messages and menus. InputHandler validates entered values. RunService performs run-management operations through the RunArchive abstraction, while CsvArchive implements actual storage and retrieval. Run subclasses encapsulate the behavior specific to each workout.


5. RUBRIC ALIGNMENT
===================


A. OOP AND SOLID PRINCIPLES - 30%
=================================


THE FOUR OOP PRINCIPLES
=======================


1. Abstraction

Run is an abstract class containing the attributes and functionality common to all runs. It declares abstract methods such as getRunType(), getRunSummary(), and zoneSummary(), leaving workout-specific behavior to the subclasses. RunArchive is an interface that describes storage operations without requiring the service layer to know how runs are stored.

2. Encapsulation

Run keeps shared fields such as runId, distance, duration, averageHeartRate, and date private and final, and exposes them through getter methods. Constructor validation prevents invalid objects from being created. Run-type-specific fields, such as tempo pace and target distance, are likewise managed within their respective classes.

3. Inheritance

EasyRun, TempoRun, LongRun, and IntervalRun extend Run, reusing its common fields and validation while adding specialized workout attributes and behaviors. This avoids repeating the same basic run model in four unrelated classes.

4. Polymorphism

The application can store and work with different subclasses using the shared Run type. Calls such as getRunSummary() and getRunType() resolve to the implementation appropriate for the actual workout object. The CSV implementation also reconstructs the correct subclass when reading persisted records.


SOLID PRINCIPLES APPLIED
========================


S - Single Responsibility Principle
  Evidence in the project: `InputHandler` validates input; `RunningTrackerUI` presents console output; `RunService` coordinates run operations; `CsvArchive` manages CSV persistence.
  Why it matters: Each class has a distinct primary responsibility, making code easier to understand and maintain.

O - Open/Closed Principle
  Evidence in the project: The abstract `Run` type supports specialized subclasses with their own summary behavior.
  Why it matters: New run behavior can be introduced through subclasses without rewriting the shared `Run` model; CSV serialization would still need an update for a newly persisted run type.

L - Liskov Substitution Principle
  Evidence in the project: `EasyRun`, `TempoRun`, `LongRun`, and `IntervalRun` are used as `Run` objects in collections and service operations.
  Why it matters: Shared operations work with any supported run subclass.

I - Interface Segregation Principle
  Evidence in the project: `RunArchive` exposes a focused set of archive operations: add, retrieve all, retrieve one, and delete.
  Why it matters: Consumers depend on a small, relevant storage contract rather than an oversized interface.

D - Dependency Inversion Principle
  Evidence in the project: `RunService` accepts a `RunArchive` interface through its constructor, rather than creating a `CsvArchive` internally.
  Why it matters: Persistence can be replaced or simulated during tests without changing `RunService`.


Particularly useful example for an instructor: In RunServiceTest, anonymous RunArchive implementations provide controlled in-memory behavior. This demonstrates the benefit of depending on an interface: business logic can be tested independently of the CSV file.


B. JAVA AND GITHUB BEST PRACTICES - 10%
=======================================


Java practices visible in the source: Classes have focused roles; reusable functionality is organized into methods; inheritance and interfaces reduce duplication; typed objects (Duration, LocalDate, Optional<Run>) communicate intent; invalid states are rejected with exceptions; and JUnit tests are separated from production code under src/test/java. JavaDoc describes the purpose of classes and methods.

Git/GitHub practices: The project is structured for source control, and changes can be committed separately for features, validation, persistence, testing, and documentation. The rubric's requirement that each feature have a separate, trackable commit must be demonstrated using the actual Git history; the source code alone cannot prove this. Use git log --oneline to show the sequence of commits. Generated target/ files and JVM crash logs should not be committed.


C. INPUT VALIDATION - 10%
=========================


InputHandler handles user input and re-prompts when the entry is invalid. Validation includes substantially more than the rubric's minimum of three categories:

Duration: Requires a valid `hh:mm:ss` format, valid minute/second ranges, and a positive duration; rejects malformed values and overflow.

Distance: Requires a positive, finite number; rejects zero, negatives, `NaN`, and infinity.

Heart rate: Optional, but when supplied must be within 80-230 BPM.

Run date: Must be a valid date and cannot be in the future.

Perceived effort: Must be an integer between 1 and 10.

Tempo pace: Must represent a valid, positive pace.

Target distance: Must be positive and finite.

Interval count: Must be a positive integer.

Work and recovery distances: Enforce the relevant numeric constraints; recovery distance may be zero.

Cancellation: Supported input prompts recognize `B` and throw `InputCancelledException` so the workflow can return without saving an incomplete run.


Constructors also validate core model values. For example, Run rejects invalid IDs, distances, durations, heart rates, and dates. This creates a second validation boundary beyond the interactive input prompts.


D. DATA STRUCTURES - 20%
========================


1. HashMap<Integer, Run> in CsvArchive

CsvArchive stores runs in a Map<Integer, Run> implemented with a HashMap. The run ID is the key and the Run object is the value. This supports efficient lookup, duplicate-ID detection, and deletion by ID. The map is a suitable fit because IDs are unique and archive operations frequently target one ID.

2. List<Run> / ArrayList<Run>

getAllRecordedRuns() returns an ArrayList created from the map's values. Lists are suitable for iterating through runs, displaying history, and returning the results of date-based searches. RunService.searchRunByDate() filters runs into an ArrayList.

Important ordering distinction: A HashMap does not promise insertion order. Date-search results preserve the order returned by the archive, but that is not necessarily the order runs were originally entered. The project should not claim that the archive itself guarantees chronological or insertion ordering.


E. TESTING - 30%
================


TEST FRAMEWORK AND ORGANIZATION
===============================


The project uses JUnit 5 with eight test classes. Tests cover the shared run model, each of the four run types, service-layer operations, input validation, and CSV persistence/error handling.

`RunTest`
  Tests in the uploaded Maven reports: 6
  What it covers: Shared attributes, pace formatting, and base-model validation.

`EasyRunTest`
  Tests in the uploaded Maven reports: 3
  What it covers: Effort bounds, summaries, and heart-rate-zone output.

`TempoRunTest`
  Tests in the uploaded Maven reports: 6
  What it covers: Faster/slower/equal target comparisons, pace differences, summaries, and validation.

`LongRunTest`
  Tests in the uploaded Maven reports: 3
  What it covers: Goal achievement comparisons, target-distance validation, and heart-rate summaries.

`IntervalRunTest`
  Tests in the uploaded Maven reports: 4
  What it covers: Total distance/duration calculations, invalid workout parameters, and heart-rate output.

`RunServiceTest`
  Tests in the uploaded Maven reports: 16
  What it covers: Run IDs, saving, searching by ID/date, retrieving, and deleting.

`InputHandlerTest`
  Tests in the uploaded Maven reports: 12
  What it covers: Valid and invalid input, re-prompting, bounds, overflow, and cancellation.

`CsvArchiveTest`
  Tests in the uploaded Maven reports: 7
  What it covers: Persistence, reloading, duplicate IDs, deletion, malformed CSV, and file errors.

Total
  Tests in the uploaded Maven reports: 57
  What it covers: 57 passed; 0 failures; 0 errors; 0 skipped in the saved reports.


These counts come from the target/surefire-reports files included in the submitted ZIP. They are previous test-run results, not a fresh Maven execution in this review.


TWO SELECTED COMPLEX METHODS - 100% COVERAGE
============================================


Complex method 1: InputHandler.readDuration(String prompt)

  - Why it is complex: It parses structured hh:mm:ss input, validates format and numeric ranges, rejects zero or invalid durations, handles oversized values, re-prompts on errors, and supports cancellation.
  - Where tested: InputHandlerTest.java, especially durationAcceptsValidValues, durationRetriesAllInvalidFormatsAndRanges, durationRetriesOverflow, and durationCancellation.
  - Coverage in the uploaded JaCoCo XML: 100% instruction coverage (77/77 instructions) and 100% branch coverage (14/14 branches).
  - What the tests demonstrate: Correct conversion for valid input, continued prompting after invalid input, safe handling of overflow, and throwing the cancellation exception for B.

Complex method 2: TempoRun.getRunSummary()

  - Why it is complex: It assembles a formatted workout summary, includes tempo-specific target-pace feedback, and conditionally includes average heart rate and zone information.
  - Where tested: TempoRunTest.java, especially comparesFasterSlowerAndEqualTargetPaces, summaryIncludesFormattedDurationAndPace, summaryIncludesEveryHeartRateZone, and paceDifferenceFormatsCorrectly.
  - Coverage in the uploaded JaCoCo XML: 100% instruction coverage (26/26 instructions) and 100% branch coverage (2/2 branches).
  - What the tests demonstrate: Correct summary text for different pace outcomes, proper duration and pace formatting, and correct handling of optional heart rate information.


COVERAGE REPORT - IMPORTANT DISTINCTION
=======================================


The saved JaCoCo report in the ZIP records 61% overall instruction coverage and 66% overall branch coverage for the application. The selected complex methods above each have 100% instruction and branch coverage. Overall coverage is lower because the console orchestration and UI classes (RunningTrackerApp and RunningTrackerUI) are not exercised by the recorded automated tests.

The rubric says “100% code coverage, including the two most complex methods.” If the instructor interprets this as 100% coverage across the entire application, the current saved report does not meet that stricter standard. If the requirement is 100% coverage of two chosen complex methods, the recorded method-level results demonstrate that target. The distinction should be explained accurately during evaluation.


HOW TO INSPECT COVERAGE
=======================


Run:

mvn clean verify

Then open:

target/site/jacoco/index.html

Navigate to InputHandler and TempoRun to inspect method-level coverage. The HTML report provides visual coverage information; target/site/jacoco/jacoco.xml contains the numeric counters.


6. CSV PERSISTENCE AND ERROR HANDLING
=====================================


CsvArchive implements RunArchive and stores runs in runs.csv. It loads saved records when constructed, converts supported run types to CSV records when saving, and recreates the appropriate subclass when loading.

Important safeguards include:

1. Null-run and duplicate-ID checks: Invalid additions are rejected.
2. Malformed or unsupported CSV records: Loading errors are detected and further saving is blocked to avoid overwriting problematic source data.
3. Duplicate IDs in saved data: Detected during CSV loading.
4. I/O failures: File-operation problems are surfaced as exceptions or load-error states.
5. Rollback on failed writes: Failed additions are removed from the in-memory map, and failed deletions restore the removed run.
6. Persistence tests: CsvArchiveTest backs up and restores the working CSV file around each test to reduce interference with existing data.

CSV storage is deliberately lightweight and appropriate for a local console assignment. It is not intended to provide multi-user concurrency, transactional database guarantees, or sophisticated reporting.


7. DESIGN DECISIONS AND TRADE-OFFS
==================================


Why a console application? The assignment focuses on Java fundamentals, object-oriented architecture, validation, data structures, and testing rather than graphical interface development.

Why Run is abstract: Every workout has common properties, but each workout type presents different details and feedback. The abstract base class centralizes shared rules while requiring specialized behavior.

Why use RunArchive instead of calling CsvArchive everywhere? It separates the business layer from storage details. The application uses CSV persistence, while unit tests can supply lightweight in-memory archive implementations.

Why use a CSV file instead of a database? CSV provides persistence without setup or external dependencies. It also gives the project meaningful file parsing, serialization, and error-handling cases to test.

Why use both a map and lists? The map is appropriate for ID-keyed storage, while lists are appropriate for displaying and filtering collections of runs.

Why have a separate InputHandler? Input validation and re-prompting are substantial responsibilities. Keeping them separate from the menu controller makes the behavior easier to test with a Scanner constructed from test strings.


8. DEMONSTRATION GUIDE FOR THE INSTRUCTOR
=========================================


A suggested live demonstration:

1. Launch RunningTrackerApp and show the main menu.
2. Add an Easy Run with distance, duration, effort, date, and optional heart rate.
3. Add a Tempo Run and demonstrate the target-pace comparison.
4. Add a Long Run and demonstrate the distance-goal message.
5. Add an Interval Run and show how the work/recovery values determine totals.
6. Show run history and explain the shared Run abstraction and polymorphic summaries.
7. Search by run ID and date.
8. Enter invalid input to demonstrate rejection and re-prompting; use B to demonstrate cancellation where supported.
9. Delete a run, exit, restart the program, and show that CSV persistence retains the remaining runs.
10. Open the JUnit test classes and JaCoCo report; identify the two selected complex methods and their 100% method-level coverage.
11. Open the Git commit history to demonstrate separate, traceable development steps.


9. SUGGESTED TALKING POINTS FOR RUBRIC QUESTIONS
================================================


How do you demonstrate all four OOP principles? Run abstracts common behavior; private fields and validation provide encapsulation; four run types inherit from Run; and overridden methods such as getRunSummary() provide polymorphism.

Which three SOLID principles are clearest? Single Responsibility (InputHandler, RunningTrackerUI, RunService, CsvArchive), Open/Closed (specialized run subclasses), and Dependency Inversion (RunService depends on RunArchive). The design also illustrates Liskov Substitution and a focused interface.

What are your two data structures? HashMap<Integer, Run> for ID-based storage and ArrayList<Run> / List<Run> for returning, filtering, and displaying collections.

How do you validate user input? InputHandler checks format, ranges, and numeric constraints and keeps prompting until the user enters valid input or cancels. Model constructors provide additional checks.

Which two complex methods have 100% coverage? InputHandler.readDuration(String prompt) and TempoRun.getRunSummary(). The saved JaCoCo report shows 100% instruction and branch coverage for both.

Why are there anonymous archive implementations in RunServiceTest? They isolate service logic from disk I/O and demonstrate dependency inversion by injecting controlled implementations of the RunArchive interface.

Does the whole application have 100% coverage? No. The included report records 61% overall instruction coverage; the two selected complex methods each have 100% instruction and branch coverage. Additional tests would be needed for full application coverage.


10. VERIFICATION STATUS FOR THIS SUBMITTED ZIP
==============================================


  - Java source compilation: Passed using javac for all 12 application source files; no compiler errors.
  - Maven configuration: Java 17, JUnit Jupiter 5.11.4, Surefire 3.5.2, JaCoCo 0.8.13.
  - Saved JUnit results: 57 tests passed across 8 test classes, with no failures, errors, or skips.
  - Saved JaCoCo overall coverage: 61% instructions and 66% branches.
  - Selected complex methods: Both show 100% instruction and branch coverage in the saved JaCoCo XML.
  - Verification limitation: Maven was not available in the review environment, so the included Maven reports were inspected rather than rerunning mvn clean verify here. A fresh Maven run is recommended before submission.


Project: Running Tracker  
Language: Java 17  
Application type: Console / CLI  
Persistence: Local CSV  
Testing: JUnit 5 and JaCoCo  
Entry point: runner_app.RunningTrackerApp
