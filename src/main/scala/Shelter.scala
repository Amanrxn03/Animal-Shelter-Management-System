package animalshelter

import scala.collection.mutable.ListBuffer
import java.time.LocalDate

/**
 * Shelter class managing animals, staff, and adoptions.
 *
 * Demonstrates:
 * 1. Collections: Mutable `ListBuffer` for internal storage, converted to
 *    immutable `List` for external querying.
 * 2. Higher-Order Collection Methods:
 *    - `filter`: Filter available animals or matching records.
 *    - `map`: Transform collections.
 *    - `find`: Search an element matching a predicate.
 *    - `count`: Count elements matching specific conditions.
 *    - `foreach`: Iterate over elements to display details.
 * 3. Exception Handling: Throws custom exceptions when business invariants fail.
 * 4. Encapsulation: Internal lists are private, protecting internal state.
 */
class Shelter {

  // Private mutable buffers to hold shelter entities (Encapsulation)
  private val animals: ListBuffer[Animal] = ListBuffer.empty[Animal]
  private val staffMembers: ListBuffer[Staff] = ListBuffer.empty[Staff]
  private val adoptionRecords: ListBuffer[AdoptionRecord] = ListBuffer.empty[AdoptionRecord]

  // Sequence generator for adoption IDs
  private var adoptionCounter: Int = 1000

  // Populate shelter with initial sample data upon creation
  loadInitialData()

  // -------------------------------------------------------------
  // 1. Animal Management
  // -------------------------------------------------------------

  /**
   * Adds a new animal to the shelter.
   * Throws `DuplicateIdException` if an animal with the same ID already exists.
   */
  def addAnimal(animal: Animal): Unit = {
    if (animals.exists(_.id.equalsIgnoreCase(animal.id.trim))) {
      throw new DuplicateIdException(s"Animal with ID '${animal.id}' already exists!")
    }
    animals += animal
  }

  /**
   * Returns an immutable List of all registered animals.
   */
  def getAllAnimals: List[Animal] = animals.toList

  /**
   * Returns all animals currently available for adoption using `filter`.
   */
  def getAvailableAnimals: List[Animal] = animals.filter(_.isAvailable).toList

  /**
   * Returns all animals that have been adopted using `filter`.
   */
  def getAdoptedAnimals: List[Animal] = animals.filter(_.isAdopted).toList

  /**
   * Finds an animal by its unique ID (case-insensitive).
   * Returns `Some(Animal)` if found, or `None` if not found.
   */
  def findAnimalById(id: String): Option[Animal] = {
    animals.find(_.id.equalsIgnoreCase(id.trim))
  }

  // -------------------------------------------------------------
  // 2. Staff Management
  // -------------------------------------------------------------

  /**
   * Adds a new staff member to the shelter.
   * Throws `DuplicateIdException` if the staff ID already exists.
   */
  def addStaff(staff: Staff): Unit = {
    if (staffMembers.exists(_.staffId.equalsIgnoreCase(staff.staffId.trim))) {
      throw new DuplicateIdException(s"Staff member with ID '${staff.staffId}' already exists!")
    }
    staffMembers += staff
  }

  /**
   * Returns an immutable List of all registered staff members.
   */
  def getAllStaff: List[Staff] = staffMembers.toList

  /**
   * Finds a staff member by ID (case-insensitive).
   */
  def findStaffById(staffId: String): Option[Staff] = {
    staffMembers.find(_.staffId.equalsIgnoreCase(staffId.trim))
  }

  // -------------------------------------------------------------
  // 3. Adoption Management
  // -------------------------------------------------------------

  /**
   * Adopts an animal after performing rigorous validations.
   *
   * @param animalId       ID of the animal to adopt
   * @param adopterName    Full name of the adopter
   * @param adopterContact Contact phone number of the adopter
   * @param staffId        ID of the staff coordinator handling the adoption
   * @return The newly generated AdoptionRecord
   *
   * Throws:
   * - `AnimalNotFoundException` if the animal ID does not exist.
   * - `AnimalAlreadyAdoptedException` if the animal is already adopted.
   * - `InvalidAdoptionRequestException` if adopter name is empty or contact is invalid.
   * - `StaffNotFoundException` if the staff ID does not exist.
   */
  def adoptAnimal(
      animalId: String,
      adopterName: String,
      adopterContact: String,
      staffId: String
  ): AdoptionRecord = {

    // 1. Validate animal existence
    val animal = findAnimalById(animalId).getOrElse(
      throw new AnimalNotFoundException(s"No animal found with ID '$animalId'.")
    )

    // 2. Validate adoption availability
    if (animal.isAdopted) {
      throw new AnimalAlreadyAdoptedException(
        s"Animal '${animal.name}' (ID: ${animal.id}) has already been adopted and is not available!"
      )
    }

    // 3. Validate adopter name
    if (adopterName == null || adopterName.trim.isEmpty) {
      throw new InvalidAdoptionRequestException("Adopter name cannot be empty.")
    }

    // 4. Validate adopter contact number (must contain at least 7 to 15 digits/characters)
    val sanitizedContact = if (adopterContact != null) adopterContact.trim else ""
    if (!isValidContact(sanitizedContact)) {
      throw new InvalidAdoptionRequestException(
        s"Invalid contact number '$sanitizedContact'. Contact number must be between 7 and 15 digits."
      )
    }

    // 5. Validate staff existence
    val staff = findStaffById(staffId).getOrElse(
      throw new StaffNotFoundException(s"No staff member found with ID '$staffId'.")
    )

    // Update animal's status (Encapsulated state change)
    animal.markAdopted()

    // Auto-generate adoption ID and current date
    adoptionCounter += 1
    val adoptionId = s"ADOPT-$adoptionCounter"
    val currentDate = LocalDate.now().toString
    val staffInfo = s"${staff.name} (${staff.designation})"

    // Create immutable adoption record (Case class instantiation)
    val record = AdoptionRecord(
      adoptionId = adoptionId,
      animalId = animal.id,
      animalName = animal.name,
      adopterName = adopterName.trim,
      adopterContact = sanitizedContact,
      adoptionDate = currentDate,
      handledByStaff = staffInfo
    )

    // Store record in adoption log
    adoptionRecords += record
    record
  }

  /**
   * Returns an immutable List of all adoption records.
   */
  def getAllAdoptionRecords: List[AdoptionRecord] = adoptionRecords.toList

  /**
   * Searches adoption records by adopter name (case-insensitive substring match).
   */
  def searchAdoptionsByAdopterName(name: String): List[AdoptionRecord] = {
    val query = name.trim.toLowerCase
    adoptionRecords.filter(_.adopterName.toLowerCase.contains(query)).toList
  }

  /**
   * Searches adoption records by animal ID (case-insensitive exact match).
   */
  def searchAdoptionsByAnimalId(animalId: String): List[AdoptionRecord] = {
    adoptionRecords.filter(_.animalId.equalsIgnoreCase(animalId.trim)).toList
  }

  // -------------------------------------------------------------
  // 4. Statistics
  // -------------------------------------------------------------

  /**
   * Computes shelter metrics using higher-order functions `count` and collection sizes.
   */
  def getStatistics: ShelterStatistics = {
    ShelterStatistics(
      totalAnimals = animals.size,
      dogCount = animals.count(_.getAnimalType == "Dog"),
      catCount = animals.count(_.getAnimalType == "Cat"),
      birdCount = animals.count(_.getAnimalType == "Bird"),
      availableAnimals = animals.count(_.isAvailable),
      adoptedAnimals = animals.count(_.isAdopted),
      totalStaff = staffMembers.size,
      totalAdoptions = adoptionRecords.size
    )
  }

  // -------------------------------------------------------------
  // Helper / Internal Methods
  // -------------------------------------------------------------

  /**
   * Validates telephone number format (allows digits, spaces, hyphens, and optional leading +).
   */
  private def isValidContact(contact: String): Boolean = {
    val digitsOnly = contact.filter(_.isDigit)
    digitsOnly.length >= 7 && digitsOnly.length <= 15
  }

  /**
   * Populates the shelter with initial sample data specified in requirements.
   */
  private def loadInitialData(): Unit = {
    // Sample Animals
    // 1. Dog – Buddy – Labrador – Age 2
    animals += new Dog("D101", "Buddy", 2, "Labrador", isVaccinated = true)
    // 2. Dog – Max – Golden Retriever – Age 3
    animals += new Dog("D102", "Max", 3, "Golden Retriever", isVaccinated = true)
    // 3. Cat – Whiskers – Persian – Age 1
    animals += new Cat("C101", "Whiskers", 1, "Persian", isIndoor = true)
    // 4. Cat – Luna – Siamese – Age 2
    animals += new Cat("C102", "Luna", 2, "Siamese", isIndoor = false)
    // 5. Bird – Rio – Parrot – Age 3
    animals += new Bird("B101", "Rio", 3, "Parrot", canFly = true)
    // 6. Bird – Coco – Lovebird – Age 1
    animals += new Bird("B102", "Coco", 1, "Lovebird", canFly = true)

    // Sample Staff
    // 1. Rahul Sharma – Veterinarian
    staffMembers += Staff("S101", "Rahul Sharma", "Veterinarian", "+91-9876543210")
    // 2. Priya Verma – Shelter Manager
    staffMembers += Staff("S102", "Priya Verma", "Shelter Manager", "+91-9876543211")
    // 3. Amit Kumar – Adoption Coordinator
    staffMembers += Staff("S103", "Amit Kumar", "Adoption Coordinator", "+91-9876543212")
  }
}

