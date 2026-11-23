package animalshelter

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/**
 * Comprehensive Unit Test Suite for Animal Shelter Management System.
 * Tests cover all 12 functional scenarios required by the project specification.
 */
class ShelterSpec extends AnyFunSuite with Matchers {

  // Helper method to create a fresh shelter instance for isolated tests
  def createFreshShelter(): Shelter = new Shelter()

  // Test Case 1: Adding a new animal successfully
  test("Test 1: Adding a new animal successfully") {
    val shelter = createFreshShelter()
    val initialCount = shelter.getAllAnimals.size

    val newDog = new Dog("D999", "Rocky", 4, "German Shepherd", isVaccinated = true)
    shelter.addAnimal(newDog)

    shelter.getAllAnimals.size shouldBe (initialCount + 1)
    val fetched = shelter.findAnimalById("D999")
    fetched shouldBe defined
    fetched.get.name shouldBe "Rocky"
    fetched.get.getAnimalType shouldBe "Dog"
  }

  // Test Case 2: Adding an animal with a duplicate ID
  test("Test 2: Adding an animal with a duplicate ID should throw DuplicateIdException") {
    val shelter = createFreshShelter()

    // "D101" is already present in initial sample data (Buddy)
    val duplicateAnimal = new Dog("D101", "CopyCat", 2, "Poodle", isVaccinated = false)

    an[DuplicateIdException] should be thrownBy {
      shelter.addAnimal(duplicateAnimal)
    }
  }

  // Test Case 3: Searching for an existing animal
  test("Test 3: Searching for an existing animal returns Some(Animal)") {
    val shelter = createFreshShelter()

    val result = shelter.findAnimalById("C101")
    result shouldBe defined
    result.get.name shouldBe "Whiskers"
    result.get.getAnimalType shouldBe "Cat"
  }

  // Test Case 4: Searching for a nonexistent animal
  test("Test 4: Searching for a nonexistent animal returns None") {
    val shelter = createFreshShelter()

    val result = shelter.findAnimalById("NONEXISTENT_999")
    result shouldBe None
  }

  // Test Case 5: Successfully adopting an available animal
  test("Test 5: Successfully adopting an available animal updates status and creates record") {
    val shelter = createFreshShelter()

    // B101 is Rio (initially available)
    val animal = shelter.findAnimalById("B101").get
    animal.isAvailable shouldBe true

    val record = shelter.adoptAnimal(
      animalId = "B101",
      adopterName = "Alice Smith",
      adopterContact = "+91-9123456789",
      staffId = "S101"
    )

    record.animalId shouldBe "B101"
    record.animalName shouldBe "Rio"
    record.adopterName shouldBe "Alice Smith"
    animal.isAdopted shouldBe true
    animal.isAvailable shouldBe false

    // Record should be saved in shelter records
    shelter.getAllAdoptionRecords should contain(record)
  }

  // Test Case 6: Attempting to adopt an already adopted animal
  test("Test 6: Attempting to adopt an already adopted animal should throw AnimalAlreadyAdoptedException") {
    val shelter = createFreshShelter()

    // Adopt B101 first
    shelter.adoptAnimal("B101", "First Adopter", "9876543210", "S101")

    // Attempt second adoption of B101
    an[AnimalAlreadyAdoptedException] should be thrownBy {
      shelter.adoptAnimal("B101", "Second Adopter", "9876543211", "S102")
    }
  }

  // Test Case 7: Attempting to adopt an animal with an invalid/nonexistent ID
  test("Test 7: Attempting to adopt an animal with an invalid ID should throw AnimalNotFoundException") {
    val shelter = createFreshShelter()

    an[AnimalNotFoundException] should be thrownBy {
      shelter.adoptAnimal("INVALID_ID", "Bob", "9876543210", "S101")
    }
  }

  // Test Case 8: Attempting adoption with an empty adopter name
  test("Test 8: Attempting adoption with an empty adopter name should throw InvalidAdoptionRequestException") {
    val shelter = createFreshShelter()

    an[InvalidAdoptionRequestException] should be thrownBy {
      shelter.adoptAnimal("D102", "   ", "9876543210", "S101")
    }
  }

  // Test Case 9: Attempting adoption with an invalid contact number
  test("Test 9: Attempting adoption with an invalid contact number should throw InvalidAdoptionRequestException") {
    val shelter = createFreshShelter()

    // Less than 7 digits
    an[InvalidAdoptionRequestException] should be thrownBy {
      shelter.adoptAnimal("D102", "Charlie", "123", "S101")
    }
  }

  // Test Case 10: Attempting adoption with a nonexistent staff ID
  test("Test 10: Attempting adoption with a nonexistent staff ID should throw StaffNotFoundException") {
    val shelter = createFreshShelter()

    an[StaffNotFoundException] should be thrownBy {
      shelter.adoptAnimal("D102", "David", "9876543210", "STAFF_999")
    }
  }

  // Test Case 11: Displaying and querying adoption records
  test("Test 11: Displaying, filtering, and querying adoption records") {
    val shelter = createFreshShelter()

    shelter.adoptAnimal("D101", "John Doe", "9876543210", "S101")
    shelter.adoptAnimal("C101", "Jane Doe", "9876543211", "S102")

    val allRecords = shelter.getAllAdoptionRecords
    allRecords.size shouldBe 2

    // Search by adopter name substring "Doe"
    val doeRecords = shelter.searchAdoptionsByAdopterName("Doe")
    doeRecords.size shouldBe 2

    // Search by specific animal ID
    val d101Records = shelter.searchAdoptionsByAnimalId("D101")
    d101Records.size shouldBe 1
    d101Records.head.animalName shouldBe "Buddy"
  }

  // Test Case 12: Calculating shelter statistics correctly
  test("Test 12: Calculating shelter statistics") {
    val shelter = createFreshShelter()

    // Initial state: 6 animals (2 dogs, 2 cats, 2 birds), 3 staff, 0 adoptions
    val initialStats = shelter.getStatistics
    initialStats.totalAnimals shouldBe 6
    initialStats.dogCount shouldBe 2
    initialStats.catCount shouldBe 2
    initialStats.birdCount shouldBe 2
    initialStats.availableAnimals shouldBe 6
    initialStats.adoptedAnimals shouldBe 0
    initialStats.totalStaff shouldBe 3
    initialStats.totalAdoptions shouldBe 0

    // Adopt one animal
    shelter.adoptAnimal("D101", "Test Adopter", "9876543210", "S101")

    val updatedStats = shelter.getStatistics
    updatedStats.totalAnimals shouldBe 6
    updatedStats.availableAnimals shouldBe 5
    updatedStats.adoptedAnimals shouldBe 1
    updatedStats.totalAdoptions shouldBe 1
  }
}

