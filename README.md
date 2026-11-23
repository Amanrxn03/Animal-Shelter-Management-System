# Animal Shelter Management System in Scala

A complete, beginner-friendly, console-based **Animal Shelter Management System** developed in **Scala 2.13.x** using **SBT** for the B.Tech Computer Science course **Data Science – Tools and Techniques (Scala)**.

---

## Table of Contents
- [Project Overview](#project-overview)
- [Key Features](#key-features)
- [Scala Concepts Demonstrated](#scala-concepts-demonstrated)
- [Project Architecture & File Structure](#project-architecture--file-structure)
- [Prerequisites](#prerequisites)
- [How to Run the Application (Windows & Cross-Platform)](#how-to-run-the-application)
- [Running Unit Tests](#running-unit-tests)
- [Example Application Output](#example-application-output)
- [Project Documentation](#project-documentation)

---

## Project Overview

The Animal Shelter Management System allows shelter staff to manage incoming animals (dogs, cats, birds), maintain staff directories, process pet adoptions with automated receipt generation, track adoption history, and compute real-time shelter statistics.

The application adheres to clean object-oriented design and functional collection handling, featuring a comprehensive custom exception hierarchy to ensure zero crashes during invalid user input.

---

## Key Features

1. **Animal Hierarchy:**
   - Abstract `Animal` parent class.
   - Species subclasses: `Dog` (with breed and vaccination status), `Cat` (with breed and indoor/outdoor preference), and `Bird` (with species and flight capability).
   - Prevents duplicate animal IDs.

2. **Staff Management:**
   - Register and view shelter personnel (Veterinarians, Managers, Coordinators).
   - Safe optional contact details modeled using Scala's `Option[String]`.

3. **Adoption Lifecycle & Receipts:**
   - Adopts available animals and automatically changes status to `Adopted`.
   - Generates immutable `AdoptionRecord` case class entries with auto-generated adoption IDs and dates.
   - Rejects adoption of already-adopted animals, nonexistent IDs, empty names, or invalid phone numbers.

4. **Search & Analytics:**
   - Search animals by ID.
   - Search adoption records by Adopter Name (case-insensitive substring) or Animal ID.
   - Real-time summary dashboard of shelter population and statistics.

5. **Menu-Driven Console Interface:**
   - Clean 11-option console menu driven by Scala's pattern matching (`match`).

---

## Scala Concepts Demonstrated

- **Classes and Objects:** `class Staff`, singleton `object Main`, companion `object Staff`.
- **Abstract Classes:** `abstract class Animal` enforcing `getAnimalType` contract.
- **Inheritance & Polymorphism:** `Dog`, `Cat`, and `Bird` extend `Animal`.
- **Method Overriding:** Explicit `override def displayInfo(): Unit` and `override def getAnimalType: String`.
- **Encapsulation:** Private mutable state (`_isAdopted`) with public accessors and controlled mutators (`markAdopted()`).
- **Case Classes:** `case class AdoptionRecord` and `case class ShelterStatistics` providing built-in equality, string representation, and immutability.
- **Variables (`val` vs `var`):** Immutable attributes (`val id`, `val name`) vs controlled mutable state (`var _isAdopted`).
- **Collections:** Internal `ListBuffer` for efficient mutation; external immutable `List` views.
- **Higher-Order Functions:** Declarative queries using `filter`, `map`, `find`, `count`, and `foreach`.
- **Pattern Matching:** Menu selection handling and species choice evaluation.
- **Custom Exceptions:** `AnimalNotFoundException`, `AnimalAlreadyAdoptedException`, `InvalidAdoptionRequestException`, `StaffNotFoundException`, and `DuplicateIdException`.
- **String Interpolation:** `s"..."` and formatted table alignment with `f"..."`.
- **Option Type:** Eliminates `NullPointerException` risks in optional fields (`Option[String]`).

---

## Project Architecture & File Structure

```
Animal shelter management system/
│
├── build.sbt                          # SBT build definition & ScalaTest dependency
├── README.md                          # Project guide & documentation
├── .gitignore                         # Git ignore rules
├── run.bat                            # Windows one-click script to run the application
├── test.bat                           # Windows one-click script to run unit tests
│
├── project/
│   └── build.properties               # SBT version specification (1.10.7)
│
├── src/
│   ├── main/
│   │   └── scala/
│   │       ├── Animal.scala           # Abstract base class for animals
│   │       ├── Dog.scala              # Dog child class with breed & vaccination
│   │       ├── Cat.scala              # Cat child class with breed & indoor preference
│   │       ├── Bird.scala             # Bird child class with species & flight ability
│   │       ├── Staff.scala            # Staff class & companion object with Option type
│   │       ├── AdoptionRecord.scala   # Case class representing an adoption transaction
│   │       ├── ShelterStatistics.scala# Case class for shelter population analytics
│   │       ├── Shelter.scala          # Business logic service & collections manager
│   │       ├── CustomExceptions.scala # Custom domain exception hierarchy
│   │       └── Main.scala             # Menu-driven console interface entrypoint
│   │
│   └── test/
│       └── scala/
│           └── ShelterSpec.scala      # 12 automated ScalaTest test cases
│
└── docs/
    ├── ProjectReport.md               # 18-section academic project report
    ├── VivaQuestions.md               # 30 viva questions with clear answers
    └── TestCases.md                   # Comprehensive test case specification
```

---

## Prerequisites

- **Java JDK:** Java 17 LTS, Java 21 LTS, or higher.
- **SBT:** SBT 1.10.x (a portable standalone SBT distribution is included in the project directory for convenience).

---

## How to Run the Application

### Option A: Using the Windows Batch Script (Fastest)
Double-click `run.bat` or open PowerShell / Command Prompt and run:
```powershell
.\run.bat
```

### Option B: Using Standard SBT
```bash
sbt run
```
Or specify the main class directly:
```bash
sbt "runMain animalshelter.Main"
```

---

## Running Unit Tests

Execute the 12 automated ScalaTest test cases:

### Using the Windows Batch Script:
```powershell
.\test.bat
```

### Using Standard SBT:
```bash
sbt test
```

Expected test output:
```
[info] ShelterSpec:
[info] - Test 1: Adding a new animal successfully
[info] - Test 2: Adding an animal with a duplicate ID should throw DuplicateIdException
[info] - Test 3: Searching for an existing animal returns Some(Animal)
[info] - Test 4: Searching for a nonexistent animal returns None
[info] - Test 5: Successfully adopting an available animal updates status and creates record
[info] - Test 6: Attempting to adopt an already adopted animal should throw AnimalAlreadyAdoptedException
[info] - Test 7: Attempting to adopt an animal with an invalid ID should throw AnimalNotFoundException
[info] - Test 8: Attempting adoption with an empty adopter name should throw InvalidAdoptionRequestException
[info] - Test 9: Attempting adoption with an invalid contact number should throw InvalidAdoptionRequestException
[info] - Test 10: Attempting adoption with a nonexistent staff ID should throw StaffNotFoundException
[info] - Test 11: Displaying, filtering, and querying adoption records
[info] - Test 12: Calculating shelter statistics
[info] Total number of tests run: 12
[info] Tests: succeeded 12, failed 0, canceled 0, ignored 0, pending 0
[info] All tests passed.
```

---

## Example Application Output

### 1. Main Console Menu
```
========================================
   ANIMAL SHELTER MANAGEMENT SYSTEM    
========================================
 1. View All Animals
 2. View Available Animals
 3. Add New Animal
 4. Search Animal by ID
 5. View Staff Members
 6. Add Staff Member
 7. Adopt an Animal
 8. View Adoption Records
 9. Search Adoption Record
 10. View Shelter Statistics
 11. Exit
========================================
Enter your choice (1-11): 
```

### 2. View All Animals (Option 1)
```
--- All Registered Animals ---
+-------------------------------------------------------------------------------------------+
| ID     | Name         | Type   | Age    | Breed/Species      | Attributes   | Status    |
+-------------------------------------------------------------------------------------------+
| D101   | Buddy        | Dog    |  2 yrs | Labrador           | Vac: Yes    | Status: Available |
| D102   | Max          | Dog    |  3 yrs | Golden Retriever   | Vac: Yes    | Status: Available |
| C101   | Whiskers     | Cat    |  1 yrs | Persian            | Pref: Indoor  | Status: Available |
| C102   | Luna         | Cat    |  2 yrs | Siamese            | Pref: Outdoor | Status: Available |
| B101   | Rio          | Bird   |  3 yrs | Parrot             | Fly: Yes     | Status: Available |
| B102   | Coco         | Bird   |  1 yrs | Lovebird           | Fly: Yes     | Status: Available |
+-------------------------------------------------------------------------------------------+
Total registered: 6
```

### 3. Adopt an Animal (Option 7)
```
--- Adopt an Animal ---
Enter Animal ID to adopt: D101
Enter Adopter Full Name: Sunita Sen
Enter Adopter Contact Number: +91-9876543210
Enter Staff ID handling the adoption: S101

============================================================
             ADOPTION COMPLETED SUCCESSFULLY!               
============================================================
  Adoption ID  : ADOPT-1001
  Animal       : Buddy (ID: D101)
  Adopter      : Sunita Sen
  Contact      : +91-9876543210
  Date         : 2026-11-23
  Handled By   : Rahul Sharma (Veterinarian)
============================================================
```

### 4. Shelter Statistics Dashboard (Option 10)
```
+----------------------------------------------------+
|                SHELTER STATISTICS                  |
+----------------------------------------------------+
| Total Animals Registered   : 6                            |
|   - Dogs                   : 2                            |
|   - Cats                   : 2                            |
|   - Birds                  : 2                            |
|----------------------------------------------------|
| Available for Adoption     : 5                            |
| Successfully Adopted       : 1                            |
|----------------------------------------------------|
| Total Staff Members        : 3                            |
| Total Adoption Records     : 1                            |
+----------------------------------------------------+
```

---

## Project Documentation

Detailed academic project documentation has been prepared in the `docs/` folder:
- [docs/ProjectReport.md](file:///c:/Animal%20shelter%20management%20system/docs/ProjectReport.md): Complete 18-section B.Tech college project report with system architecture, Mermaid class diagrams, and screenshot placeholders.
- [docs/VivaQuestions.md](file:///c:/Animal%20shelter%20management%20system/docs/VivaQuestions.md): 30 viva voce questions with clear, beginner-friendly answers.
- [docs/TestCases.md](file:///c:/Animal%20shelter%20management%20system/docs/TestCases.md): Test specification sheet detailing all 12 test scenarios with automated and manual steps.

