package animalshelter

/**
 * Concrete class representing a Dog in the shelter.
 *
 * Demonstrates:
 * 1. Inheritance: Extends the abstract parent class `Animal`.
 * 2. Constructor Overloading & Parameter Passing: Passes common parameters
 *    to `Animal`'s constructor.
 * 3. Additional Attributes: Adds `breed` and `isVaccinated` specific to dogs.
 * 4. Method Overriding: Implements abstract `getAnimalType` and overrides `displayInfo()`.
 * 5. Formatted String Output (`f` interpolator): Clean table-like console display.
 */
class Dog(
    id: String,
    name: String,
    age: Int,
    val breed: String,
    val isVaccinated: Boolean,
    isAdopted: Boolean = false
) extends Animal(id, name, age, isAdopted) {

  // Override abstract method to return species type
  override def getAnimalType: String = "Dog"

  // Override displayInfo to show Dog-specific attributes
  override def displayInfo(): Unit = {
    val status = if (isAdopted) "Adopted" else "Available"
    val vacStatus = if (isVaccinated) "Yes" else "No"
    println(f"| ${id}%-6s | ${name}%-12s | ${getAnimalType}%-6s | ${age}%2d yrs | ${breed}%-18s | Vac: ${vacStatus}%-3s    | Status: ${status}%-9s |")
  }
}

