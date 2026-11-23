package animalshelter

/**
 * Concrete class representing a Cat in the shelter.
 *
 * Demonstrates:
 * 1. Inheritance: Extends `Animal`.
 * 2. Additional Attributes: Adds `breed` and `isIndoor` (Indoor/Outdoor preference).
 * 3. Method Overriding: Overrides `getAnimalType` and `displayInfo()`.
 * 4. Pattern Matching / Conditional Formatting in display logic.
 */
class Cat(
    id: String,
    name: String,
    age: Int,
    val breed: String,
    val isIndoor: Boolean,
    isAdopted: Boolean = false
) extends Animal(id, name, age, isAdopted) {

  // Override abstract method
  override def getAnimalType: String = "Cat"

  // Override displayInfo to show Cat-specific attributes
  override def displayInfo(): Unit = {
    val status = if (isAdopted) "Adopted" else "Available"
    val pref = if (isIndoor) "Indoor" else "Outdoor"
    println(f"| ${id}%-6s | ${name}%-12s | ${getAnimalType}%-6s | ${age}%2d yrs | ${breed}%-18s | Pref: ${pref}%-7s | Status: ${status}%-9s |")
  }
}

