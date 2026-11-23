package animalshelter

import scala.io.StdIn
import scala.util.control.NonFatal

/**
 * Main application entrypoint for the Animal Shelter Management System.
 *
 * Demonstrates:
 * 1. Scala Objects: `object Main` acts as a singleton entrypoint with `def main(args: Array[String])`.
 * 2. Pattern Matching (`match`): Used for menu choice dispatching and option selection.
 * 3. Exception Handling (`try-catch`): Catches custom exceptions and general errors,
 *    preventing the application from crashing and returning the user to the menu.
 * 4. User Interaction & Input Validation: Clean prompts, safe parsing, and formatted tables.
 */
object Main {

  // Global instance of the Shelter managing all entities
  private val shelter = new Shelter()

  /**
   * Safe input reader that trims input and avoids NullPointerException on EOF.
   */
  private def readInput(): String = {
    val line = StdIn.readLine()
    if (line == null) "" else line.trim
  }

  def main(args: Array[String]): Unit = {
    println("\n========================================================")
    println("  Welcome to the Animal Shelter Management System (Scala) ")
    println("========================================================")

    var continueRunning = true

    while (continueRunning) {
      displayMenu()
      print("Enter your choice (1-11): ")
      val input = StdIn.readLine()

      if (input == null) {
        // Handle EOF (e.g. piped input or end of stream)
        continueRunning = false
      } else {
        println() // Add blank line for readability

        // Pattern matching to dispatch user choice
        input.trim match {
          case "1"  => viewAllAnimals()
          case "2"  => viewAvailableAnimals()
          case "3"  => addNewAnimal()
          case "4"  => searchAnimalById()
          case "5"  => viewStaffMembers()
          case "6"  => addStaffMember()
          case "7"  => adoptAnimal()
          case "8"  => viewAdoptionRecords()
          case "9"  => searchAdoptionRecord()
          case "10" => viewShelterStatistics()
          case "11" =>
            println("Thank you for using Animal Shelter Management System. Goodbye!")
            continueRunning = false
          case invalid =>
            println(s"[ERROR] Invalid option '$invalid'. Please enter a number between 1 and 11.")
        }

        if (continueRunning) {
          println("\nPress [ENTER] to return to the Main Menu...")
          val pause = StdIn.readLine()
          if (pause == null) continueRunning = false
        }
      }
    }
  }

  /**
   * Displays the clean main console menu.
   */
  private def displayMenu(): Unit = {
    println("========================================")
    println("   ANIMAL SHELTER MANAGEMENT SYSTEM    ")
    println("========================================")
    println(" 1. View All Animals")
    println(" 2. View Available Animals")
    println(" 3. Add New Animal")
    println(" 4. Search Animal by ID")
    println(" 5. View Staff Members")
    println(" 6. Add Staff Member")
    println(" 7. Adopt an Animal")
    println(" 8. View Adoption Records")
    println(" 9. Search Adoption Record")
    println(" 10. View Shelter Statistics")
    println(" 11. Exit")
    println("========================================")
  }

  // -------------------------------------------------------------
  // Option 1: View All Animals
  // -------------------------------------------------------------
  private def viewAllAnimals(): Unit = {
    println("--- All Registered Animals ---")
    val list = shelter.getAllAnimals
    if (list.isEmpty) {
      println("No animals are currently registered in the shelter.")
    } else {
      printAnimalTableHeader()
      list.foreach(_.displayInfo())
      printAnimalTableFooter()
      println(s"Total registered: ${list.size}")
    }
  }

  // -------------------------------------------------------------
  // Option 2: View Available Animals
  // -------------------------------------------------------------
  private def viewAvailableAnimals(): Unit = {
    println("--- Animals Available for Adoption ---")
    val list = shelter.getAvailableAnimals
    if (list.isEmpty) {
      println("No animals are currently available for adoption.")
    } else {
      printAnimalTableHeader()
      list.foreach(_.displayInfo())
      printAnimalTableFooter()
      println(s"Total available: ${list.size}")
    }
  }

  // -------------------------------------------------------------
  // Option 3: Add New Animal
  // -------------------------------------------------------------
  private def addNewAnimal(): Unit = {
    println("--- Add New Animal ---")
    try {
      print("Select Animal Type (1: Dog, 2: Cat, 3: Bird): ")
      val typeChoice = readInput()

      print("Enter Animal ID (e.g., D103, C103, B103): ")
      val id = readInput()
      if (id.isEmpty) throw new IllegalArgumentException("Animal ID cannot be empty.")

      print("Enter Animal Name: ")
      val name = readInput()
      if (name.isEmpty) throw new IllegalArgumentException("Animal Name cannot be empty.")

      print("Enter Age (in years): ")
      val ageInput = readInput()
      val age = ageInput.toInt
      if (age < 0 || age > 50) throw new IllegalArgumentException("Age must be between 0 and 50.")

      // Pattern matching on animal species choice
      val newAnimal: Animal = typeChoice match {
        case "1" =>
          print("Enter Breed (e.g., Beagle): ")
          val breed = readInput()
          print("Is the dog vaccinated? (y/n): ")
          val vac = readInput().equalsIgnoreCase("y")
          new Dog(id, name, age, if (breed.isEmpty) "Mixed Breed" else breed, vac)

        case "2" =>
          print("Enter Breed (e.g., Bengal): ")
          val breed = readInput()
          print("Is the cat an indoor cat? (y/n): ")
          val indoor = readInput().equalsIgnoreCase("y")
          new Cat(id, name, age, if (breed.isEmpty) "Domestic" else breed, indoor)

        case "3" =>
          print("Enter Species (e.g., Canary): ")
          val species = readInput()
          print("Can the bird fly? (y/n): ")
          val canFly = readInput().equalsIgnoreCase("y")
          new Bird(id, name, age, if (species.isEmpty) "Common Bird" else species, canFly)

        case other =>
          throw new IllegalArgumentException(s"Invalid animal type choice '$other'. Must be 1, 2, or 3.")
      }

      shelter.addAnimal(newAnimal)
      println(s"[SUCCESS] Successfully added ${newAnimal.getAnimalType} '${newAnimal.name}' (ID: ${newAnimal.id}) to the shelter.")

    } catch {
      case ex: DuplicateIdException =>
        println(s"[ERROR] ${ex.getMessage}")
      case ex: NumberFormatException =>
        println("[ERROR] Invalid numeric format entered for age. Please enter a valid number.")
      case ex: IllegalArgumentException =>
        println(s"[ERROR] ${ex.getMessage}")
      case NonFatal(e) =>
        println(s"[ERROR] Failed to add animal: ${e.getMessage}")
    }
  }

  // -------------------------------------------------------------
  // Option 4: Search Animal by ID
  // -------------------------------------------------------------
  private def searchAnimalById(): Unit = {
    println("--- Search Animal by ID ---")
    print("Enter Animal ID to search: ")
    val id = readInput()

    if (id.isEmpty) {
      println("[ERROR] Search ID cannot be empty.")
      return
    }

    shelter.findAnimalById(id) match {
      case Some(animal) =>
        println("\n[FOUND] Animal details:")
        printAnimalTableHeader()
        animal.displayInfo()
        printAnimalTableFooter()
      case None =>
        println(s"[NOT FOUND] No animal found with ID '$id'.")
    }
  }

  // -------------------------------------------------------------
  // Option 5: View Staff Members
  // -------------------------------------------------------------
  private def viewStaffMembers(): Unit = {
    println("--- Shelter Staff Members ---")
    val staffList = shelter.getAllStaff
    if (staffList.isEmpty) {
      println("No staff members are currently registered.")
    } else {
      printStaffTableHeader()
      staffList.foreach(_.displayStaff())
      printStaffTableFooter()
      println(s"Total staff members: ${staffList.size}")
    }
  }

  // -------------------------------------------------------------
  // Option 6: Add Staff Member
  // -------------------------------------------------------------
  private def addStaffMember(): Unit = {
    println("--- Add Staff Member ---")
    try {
      print("Enter Staff ID (e.g., S104): ")
      val staffId = readInput()
      if (staffId.isEmpty) throw new IllegalArgumentException("Staff ID cannot be empty.")

      print("Enter Staff Full Name: ")
      val name = readInput()
      if (name.isEmpty) throw new IllegalArgumentException("Staff name cannot be empty.")

      print("Enter Designation (e.g., Caretaker, Vet Assistant): ")
      val designation = readInput()
      if (designation.isEmpty) throw new IllegalArgumentException("Designation cannot be empty.")

      print("Enter Contact Information (optional, press Enter to skip): ")
      val contact = readInput()

      val newStaff = Staff(staffId, name, designation, contact)
      shelter.addStaff(newStaff)
      println(s"[SUCCESS] Staff member '${newStaff.name}' (ID: ${newStaff.staffId}) registered successfully.")

    } catch {
      case ex: DuplicateIdException =>
        println(s"[ERROR] ${ex.getMessage}")
      case ex: IllegalArgumentException =>
        println(s"[ERROR] ${ex.getMessage}")
      case NonFatal(e) =>
        println(s"[ERROR] Failed to add staff: ${e.getMessage}")
    }
  }

  // -------------------------------------------------------------
  // Option 7: Adopt an Animal
  // -------------------------------------------------------------
  private def adoptAnimal(): Unit = {
    println("--- Adopt an Animal ---")
    try {
      print("Enter Animal ID to adopt: ")
      val animalId = readInput()

      print("Enter Adopter Full Name: ")
      val adopterName = readInput()

      print("Enter Adopter Contact Number: ")
      val adopterContact = readInput()

      print("Enter Staff ID handling the adoption: ")
      val staffId = readInput()

      // Call shelter adoption method which performs all business logic and exception throwing
      val record = shelter.adoptAnimal(animalId, adopterName, adopterContact, staffId)

      println("\n============================================================")
      println("             ADOPTION COMPLETED SUCCESSFULLY!               ")
      println("============================================================")
      println(s"  Adoption ID  : ${record.adoptionId}")
      println(s"  Animal       : ${record.animalName} (ID: ${record.animalId})")
      println(s"  Adopter      : ${record.adopterName}")
      println(s"  Contact      : ${record.adopterContact}")
      println(s"  Date         : ${record.adoptionDate}")
      println(s"  Handled By   : ${record.handledByStaff}")
      println("============================================================")

    } catch {
      case ex: AnimalNotFoundException =>
        println(s"[ADOPTION FAILED] ${ex.getMessage}")
      case ex: AnimalAlreadyAdoptedException =>
        println(s"[ADOPTION FAILED] ${ex.getMessage}")
      case ex: InvalidAdoptionRequestException =>
        println(s"[ADOPTION FAILED] ${ex.getMessage}")
      case ex: StaffNotFoundException =>
        println(s"[ADOPTION FAILED] ${ex.getMessage}")
      case NonFatal(e) =>
        println(s"[ERROR] An unexpected error occurred: ${e.getMessage}")
    }
  }

  // -------------------------------------------------------------
  // Option 8: View Adoption Records
  // -------------------------------------------------------------
  private def viewAdoptionRecords(): Unit = {
    println("--- All Adoption Records ---")
    val records = shelter.getAllAdoptionRecords
    if (records.isEmpty) {
      println("No adoptions have been recorded yet.")
    } else {
      printAdoptionTableHeader()
      records.foreach(_.displayRecord())
      printAdoptionTableFooter()
      println(s"Total adoptions recorded: ${records.size}")
    }
  }

  // -------------------------------------------------------------
  // Option 9: Search Adoption Record
  // -------------------------------------------------------------
  private def searchAdoptionRecord(): Unit = {
    println("--- Search Adoption Records ---")
    println("1. Search by Adopter Name")
    println("2. Search by Animal ID")
    print("Choose search criteria (1 or 2): ")
    val searchChoice = readInput()

    searchChoice match {
      case "1" =>
        print("Enter Adopter Name (or partial name): ")
        val nameQuery = readInput()
        val results = shelter.searchAdoptionsByAdopterName(nameQuery)
        displaySearchResults(results, s"Adopter name matching '$nameQuery'")

      case "2" =>
        print("Enter Animal ID: ")
        val animalIdQuery = readInput()
        val results = shelter.searchAdoptionsByAnimalId(animalIdQuery)
        displaySearchResults(results, s"Animal ID '$animalIdQuery'")

      case other =>
        println(s"[ERROR] Invalid search option '$other'.")
    }
  }

  private def displaySearchResults(records: List[AdoptionRecord], criteria: String): Unit = {
    if (records.isEmpty) {
      println(s"No adoption records found for $criteria.")
    } else {
      println(s"\nFound ${records.size} record(s) for $criteria:")
      printAdoptionTableHeader()
      records.foreach(_.displayRecord())
      printAdoptionTableFooter()
    }
  }

  // -------------------------------------------------------------
  // Option 10: View Shelter Statistics
  // -------------------------------------------------------------
  private def viewShelterStatistics(): Unit = {
    val stats = shelter.getStatistics
    stats.display()
  }

  // -------------------------------------------------------------
  // Table Formatting Helpers
  // -------------------------------------------------------------
  private def printAnimalTableHeader(): Unit = {
    println("+" + "-" * 91 + "+")
    println(f"| ${"ID"}%-6s | ${"Name"}%-12s | ${"Type"}%-6s | ${"Age"}%-6s | ${"Breed/Species"}%-18s | ${"Attributes"}%-12s | ${"Status"}%-9s |")
    println("+" + "-" * 91 + "+")
  }

  private def printAnimalTableFooter(): Unit = {
    println("+" + "-" * 91 + "+")
  }

  private def printStaffTableHeader(): Unit = {
    println("+" + "-" * 73 + "+")
    println(f"| ${"Staff ID"}%-8s | ${"Name"}%-20s | ${"Designation"}%-24s | ${"Contact"}%-16s |")
    println("+" + "-" * 73 + "+")
  }

  private def printStaffTableFooter(): Unit = {
    println("+" + "-" * 73 + "+")
  }

  private def printAdoptionTableHeader(): Unit = {
    println("+" + "-" * 105 + "+")
    println(f"| ${"AdoptionID"}%-10s | ${"AnimID"}%-6s | ${"Name"}%-12s | ${"Adopter"}%-18s | ${"Contact"}%-14s | ${"Date"}%-12s | ${"Handled By"}%-24s |")
    println("+" + "-" * 105 + "+")
  }

  private def printAdoptionTableFooter(): Unit = {
    println("+" + "-" * 105 + "+")
  }
}
