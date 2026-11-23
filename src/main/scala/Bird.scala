package animalshelter

/**
 * Concrete class representing a Bird in the shelter.
 *
 * Demonstrates:
 * 1. Inheritance: Extends `Animal`.
 * 2. Additional Attributes: Adds `species` and `canFly` ability flag.
 * 3. Method Overriding: Overrides `getAnimalType` and `displayInfo()`.
 */
class Bird(
    id: String,
    name: String,
    age: Int,
    val species: String,
    val canFly: Boolean,
    isAdopted: Boolean = false
) extends Animal(id, name, age, isAdopted) {

  // Override abstract method
  override def getAnimalType: String = "Bird"

  // Override displayInfo to show Bird-specific attributes
  override def displayInfo(): Unit = {
    val status = if (isAdopted) "Adopted" else "Available"
    val flyStatus = if (canFly) "Yes" else "No"
    println(f"| ${id}%-6s | ${name}%-12s | ${getAnimalType}%-6s | ${age}%2d yrs | ${species}%-18s | Fly: ${flyStatus}%-3s     | Status: ${status}%-9s |")
  }
}

