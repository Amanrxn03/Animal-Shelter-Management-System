# Animal Shelter Management System - Test Cases Specification

This document provides a comprehensive test plan and execution record for the **Animal Shelter Management System** in Scala. It covers both **Automated Unit Tests** (implemented using ScalaTest in `src/test/scala/ShelterSpec.scala`) and **Manual Console Interaction Test Cases**.

---

## Summary of Test Cases

| Test Case ID | Test Scenario | Type | Expected Outcome | Status |
|---|---|---|---|---|
| **TC-01** | Add a new animal successfully | Automated & Manual | Animal added to collection; total count increments by 1 | **PASS** |
| **TC-02** | Add animal with duplicate ID | Automated & Manual | Throws `DuplicateIdException`; error displayed | **PASS** |
| **TC-03** | Search for an existing animal by ID | Automated & Manual | Returns animal details (`Some(Animal)`) | **PASS** |
| **TC-04** | Search for a nonexistent animal by ID | Automated & Manual | Returns `None`; displays user-friendly "Not Found" message | **PASS** |
| **TC-05** | Successfully adopt an available animal | Automated & Manual | Animal status updated to `Adopted`; record logged; returns `AdoptionRecord` | **PASS** |
| **TC-06** | Attempt to adopt an already adopted animal | Automated & Manual | Throws `AnimalAlreadyAdoptedException`; adoption blocked | **PASS** |
| **TC-07** | Attempt to adopt animal with nonexistent ID | Automated & Manual | Throws `AnimalNotFoundException`; adoption blocked | **PASS** |
| **TC-08** | Attempt adoption with empty adopter name | Automated & Manual | Throws `InvalidAdoptionRequestException`; error displayed | **PASS** |
| **TC-09** | Attempt adoption with invalid contact number | Automated & Manual | Throws `InvalidAdoptionRequestException`; error displayed | **PASS** |
| **TC-10** | Attempt adoption with nonexistent staff ID | Automated & Manual | Throws `StaffNotFoundException`; error displayed | **PASS** |
| **TC-11** | View and search adoption records | Automated & Manual | Correctly displays records; filters by name or animal ID | **PASS** |
| **TC-12** | Calculate shelter statistics | Automated & Manual | Computes exact counts for animals, species, status, staff, and records | **PASS** |

---

## Detailed Test Cases

### TC-01: Adding a New Animal Successfully
- **Objective:** Verify that a user can register a new dog, cat, or bird with valid attributes.
- **Preconditions:** System initialized with initial sample data.
- **Input Data:**
  - Type: `Dog` (Option 1)
  - ID: `D999`
  - Name: `Rocky`
  - Age: `4`
  - Breed: `German Shepherd`
  - Vaccinated: `Yes` (`y`)
- **Execution Steps (Console):**
  1. Select option `3` (Add New Animal).
  2. Enter animal type `1`.
  3. Enter ID `D999`, Name `Rocky`, Age `4`.
  4. Enter Breed `German Shepherd`, Vaccinated `y`.
  5. Observe console confirmation message.
- **Expected Result:** Confirmation message displayed; animal appears in option `1` (View All Animals).
- **Automated Test Assertion:** `shelter.findAnimalById("D999") shouldBe defined`
- **Result:** **PASS**

---

### TC-02: Adding an Animal with a Duplicate ID
- **Objective:** Verify that the system prevents duplicate animal IDs.
- **Preconditions:** Animal with ID `D101` already exists (Buddy).
- **Input Data:**
  - ID: `D101`
  - Name: `Charlie`
  - Age: `2`
- **Execution Steps (Console):**
  1. Select option `3` (Add New Animal).
  2. Enter `D101` as the animal ID.
- **Expected Result:** System catches `DuplicateIdException` and displays `[ERROR] Animal with ID 'D101' already exists!`. Application does not crash.
- **Automated Test Assertion:** `an [DuplicateIdException] should be thrownBy { shelter.addAnimal(duplicateAnimal) }`
- **Result:** **PASS**

---

### TC-03: Searching for an Existing Animal by ID
- **Objective:** Verify that searching for a valid animal ID displays all attributes.
- **Preconditions:** Initial animal `C101` (Whiskers) exists.
- **Input Data:** Search ID: `C101`
- **Execution Steps (Console):**
  1. Select option `4` (Search Animal by ID).
  2. Enter `C101`.
- **Expected Result:** Displays Whiskers, Cat, Age 1, Breed Persian, Indoor: Yes, Status: Available.
- **Automated Test Assertion:** `shelter.findAnimalById("C101").get.name shouldBe "Whiskers"`
- **Result:** **PASS**

---

### TC-04: Searching for a Nonexistent Animal by ID
- **Objective:** Verify that searching for an unknown ID fails gracefully.
- **Preconditions:** No animal with ID `Z999` exists.
- **Input Data:** Search ID: `Z999`
- **Execution Steps (Console):**
  1. Select option `4` (Search Animal by ID).
  2. Enter `Z999`.
- **Expected Result:** Displays `[NOT FOUND] No animal found with ID 'Z999'`.
- **Automated Test Assertion:** `shelter.findAnimalById("Z999") shouldBe None`
- **Result:** **PASS**

---

### TC-05: Successfully Adopting an Available Animal
- **Objective:** Verify successful adoption workflow and record creation.
- **Preconditions:** Animal `B101` (Rio) is available; Staff `S101` exists.
- **Input Data:**
  - Animal ID: `B101`
  - Adopter Name: `Alice Smith`
  - Contact: `+91-9123456789`
  - Staff ID: `S101`
- **Execution Steps (Console):**
  1. Select option `7` (Adopt an Animal).
  2. Enter the inputs above.
- **Expected Result:**
  - Displays "ADOPTION COMPLETED SUCCESSFULLY!" with auto-generated ID (e.g., `ADOPT-1001`).
  - Animal `B101` status changes from `Available` to `Adopted`.
  - Record is visible under Option `8`.
- **Automated Test Assertion:**
  `animal.isAdopted shouldBe true`
  `shelter.getAllAdoptionRecords should contain(record)`
- **Result:** **PASS**

---

### TC-06: Attempting to Adopt an Already Adopted Animal
- **Objective:** Ensure an adopted animal cannot be adopted a second time.
- **Preconditions:** Animal `B101` has already been adopted in TC-05.
- **Input Data:**
  - Animal ID: `B101`
  - Adopter Name: `John Doe`
  - Contact: `9876543210`
  - Staff ID: `S102`
- **Execution Steps (Console):**
  1. Select option `7` (Adopt an Animal).
  2. Enter animal ID `B101`.
- **Expected Result:** Throws and handles `AnimalAlreadyAdoptedException`. Displays `[ADOPTION FAILED] Animal 'Rio' (ID: B101) has already been adopted and is not available!`.
- **Automated Test Assertion:** `an [AnimalAlreadyAdoptedException] should be thrownBy { shelter.adoptAnimal("B101", ...) }`
- **Result:** **PASS**

---

### TC-07: Attempting to Adopt an Animal with an Invalid/Nonexistent ID
- **Objective:** Verify error handling when animal ID does not exist during adoption.
- **Input Data:** Animal ID: `FAKE999`
- **Execution Steps (Console):**
  1. Select option `7` (Adopt an Animal).
  2. Enter `FAKE999`.
- **Expected Result:** Throws `AnimalNotFoundException`. Displays `[ADOPTION FAILED] No animal found with ID 'FAKE999'`.
- **Automated Test Assertion:** `an [AnimalNotFoundException] should be thrownBy { shelter.adoptAnimal("FAKE999", ...) }`
- **Result:** **PASS**

---

### TC-08: Attempting Adoption with an Empty Adopter Name
- **Objective:** Prevent adoptions without a valid adopter name.
- **Input Data:**
  - Animal ID: `D102`
  - Adopter Name: `   ` (spaces only)
- **Execution Steps (Console):**
  1. Select option `7`.
  2. Enter Animal ID `D102`, leave name blank.
- **Expected Result:** Throws `InvalidAdoptionRequestException`. Displays `[ADOPTION FAILED] Adopter name cannot be empty.`.
- **Automated Test Assertion:** `an [InvalidAdoptionRequestException] should be thrownBy { shelter.adoptAnimal("D102", "   ", ...) }`
- **Result:** **PASS**

---

### TC-09: Attempting Adoption with an Invalid Contact Number
- **Objective:** Validate contact number length and format.
- **Input Data:**
  - Contact Number: `123` (less than 7 digits) or invalid letters.
- **Expected Result:** Throws `InvalidAdoptionRequestException`. Displays `[ADOPTION FAILED] Invalid contact number '123'. Contact number must be between 7 and 15 digits.`.
- **Automated Test Assertion:** `an [InvalidAdoptionRequestException] should be thrownBy { shelter.adoptAnimal("D102", "Charlie", "123", "S101") }`
- **Result:** **PASS**

---

### TC-10: Attempting Adoption with a Nonexistent Staff ID
- **Objective:** Ensure only valid registered staff members can coordinate adoptions.
- **Input Data:**
  - Staff ID: `S999` (does not exist)
- **Execution Steps (Console):**
  1. Select option `7`.
  2. Enter Staff ID `S999`.
- **Expected Result:** Throws `StaffNotFoundException`. Displays `[ADOPTION FAILED] No staff member found with ID 'S999'`.
- **Automated Test Assertion:** `an [StaffNotFoundException] should be thrownBy { shelter.adoptAnimal("D102", "David", "9876543210", "S999") }`
- **Result:** **PASS**

---

### TC-11: Displaying and Searching Adoption Records
- **Objective:** Verify that adoption records are logged and searchable by adopter name or animal ID.
- **Preconditions:** At least one adoption has taken place.
- **Execution Steps (Console):**
  1. Select option `8` (View Adoption Records) -> all logged records displayed in formatted table.
  2. Select option `9` (Search Adoption Record).
  3. Search by adopter name substring -> returns matching records.
  4. Search by animal ID -> returns matching records.
- **Expected Result:** Filtered records match search parameters.
- **Automated Test Assertion:**
  `shelter.searchAdoptionsByAdopterName("Doe").size shouldBe 2`
  `shelter.searchAdoptionsByAnimalId("D101").head.animalName shouldBe "Buddy"`
- **Result:** **PASS**

---

### TC-12: Calculating Shelter Statistics Correctly
- **Objective:** Verify aggregate calculations (total animals, dogs, cats, birds, available, adopted, staff, adoptions).
- **Preconditions:** Initial dataset (6 animals, 3 staff).
- **Expected Initial Values:**
  - Total Animals: 6 (2 Dogs, 2 Cats, 2 Birds)
  - Available Animals: 6
  - Adopted Animals: 0
  - Staff Members: 3
  - Total Adoptions: 0
- **Expected Post-Adoption Values (after 1 adoption):**
  - Available Animals: 5
  - Adopted Animals: 1
  - Total Adoptions: 1
- **Automated Test Assertion:** `stats.availableAnimals shouldBe 5; stats.adoptedAnimals shouldBe 1`
- **Result:** **PASS**

