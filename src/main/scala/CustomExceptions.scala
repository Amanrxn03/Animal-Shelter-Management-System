package animalshelter

/**
 * Custom exception hierarchy for the Animal Shelter Management System.
 * In Scala, custom exceptions inherit from the standard `Exception` class.
 *
 * Demonstrates:
 * - Object-Oriented inheritance with Java/Scala Exception class
 * - Meaningful error messages for business-logic validation
 */

// 1. Thrown when an animal with the requested ID does not exist in the shelter.
class AnimalNotFoundException(message: String) extends Exception(message)

// 2. Thrown when an adopter attempts to adopt an animal that has already been adopted.
class AnimalAlreadyAdoptedException(message: String) extends Exception(message)

// 3. Thrown when adoption request data is invalid (e.g., empty adopter name, invalid phone number).
class InvalidAdoptionRequestException(message: String) extends Exception(message)

// 4. Thrown when a staff member ID referenced during adoption or search does not exist.
class StaffNotFoundException(message: String) extends Exception(message)

// 5. Thrown when attempting to register an animal or staff member with an ID that already exists.
class DuplicateIdException(message: String) extends Exception(message)

