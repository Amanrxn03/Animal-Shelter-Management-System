package animalshelter

/**
 * Abstract class representing a general Animal in the shelter.
 *
 * Demonstrates key Scala concepts:
 * 1. Abstract Classes: Defines common properties and contract without direct instantiation.
 * 2. Immutable Variables (`val`): Animal ID, Name, and Age cannot be modified after creation.
 * 3. Mutable Variables (`var`): `_isAdopted` is private mutable state indicating adoption status.
 * 4. Encapsulation: Adoption status is read via `isAdopted` / `isAvailable` and updated
 *    only via the controlled `markAdopted()` method.
 * 5. Abstract Method: `getAnimalType` forces child classes to identify their specific species.
 */
abstract class Animal(
    val id: String,
    val name: String,
    val age: Int,
    private var _isAdopted: Boolean = false
) {

  /**
   * Getter for adoption status (Encapsulation).
   * True if animal is already adopted, false otherwise.
   */
  def isAdopted: Boolean = _isAdopted

  /**
   * Convenience method to check if the animal is currently available for adoption.
   */
  def isAvailable: Boolean = !_isAdopted

  /**
   * Transitions the animal's status to adopted.
   * Demonstrates state mutation within an encapsulated object.
   */
  def markAdopted(): Unit = {
    _isAdopted = true
  }

  /**
   * Abstract method that every concrete animal subclass must implement.
   * Returns "Dog", "Cat", "Bird", etc. Demonstrates Polymorphism.
   */
  def getAnimalType: String

  /**
   * Displays formatted information about the animal to the console.
   * Subclasses override this to append their species-specific attributes.
   */
  def displayInfo(): Unit
}

