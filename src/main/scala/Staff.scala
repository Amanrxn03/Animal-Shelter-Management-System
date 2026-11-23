package animalshelter

/**
 * Class representing a Shelter Staff Member.
 *
 * Demonstrates:
 * 1. Classes and Objects: Standard Scala class definition.
 * 2. Scala `Option` Type: Models optional values (`contactInfo`) safely without null pointer risks.
 * 3. Companion Object: Provides factory `apply` methods for cleaner instantiation without `new`.
 * 4. Method Overriding: Overrides `toString` for friendly text representation.
 */
class Staff(
    val staffId: String,
    val name: String,
    val designation: String,
    val contactInfo: Option[String] = None
) {

  /**
   * Displays staff details in a neat table row.
   */
  def displayStaff(): Unit = {
    val contact = contactInfo.getOrElse("N/A")
    println(f"| ${staffId}%-8s | ${name}%-20s | ${designation}%-24s | ${contact}%-16s |")
  }

  override def toString: String = s"$name ($designation, ID: $staffId)"
}

/**
 * Companion object for the Staff class.
 * Demonstrates:
 * - Factory methods (`apply`)
 * - Trimming and sanitizing input parameters
 */
object Staff {
  def apply(staffId: String, name: String, designation: String, contact: String): Staff = {
    val contactOpt = if (contact != null && contact.trim.nonEmpty) Some(contact.trim) else None
    new Staff(staffId.trim, name.trim, designation.trim, contactOpt)
  }

  def apply(staffId: String, name: String, designation: String): Staff = {
    new Staff(staffId.trim, name.trim, designation.trim, None)
  }
}

