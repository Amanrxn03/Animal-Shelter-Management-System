package animalshelter

/**
 * Case class representing an Adoption Record in the system.
 *
 * Demonstrates:
 * 1. Case Classes: Immutable data container.
 *    - All fields are implicitly `val` (immutable).
 *    - Automatically provides `equals`, `hashCode`, `toString`, and `copy` methods.
 *    - Can be instantiated without the `new` keyword.
 * 2. Clean separation of concerns: Represents historical transaction data.
 */
case class AdoptionRecord(
    adoptionId: String,
    animalId: String,
    animalName: String,
    adopterName: String,
    adopterContact: String,
    adoptionDate: String,
    handledByStaff: String
) {

  /**
   * Displays the adoption record in a clean, formatted table row.
   */
  def displayRecord(): Unit = {
    println(f"| ${adoptionId}%-10s | ${animalId}%-6s | ${animalName}%-12s | ${adopterName}%-18s | ${adopterContact}%-14s | ${adoptionDate}%-12s | ${handledByStaff}%-24s |")
  }
}

