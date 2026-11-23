package animalshelter

/**
 * Case class encapsulating the current summary statistics of the shelter.
 *
 * Demonstrates:
 * - Case Classes for aggregated data reporting
 * - Higher-order method results representation
 * - Formatted console output using String Interpolation
 */
case class ShelterStatistics(
    totalAnimals: Int,
    dogCount: Int,
    catCount: Int,
    birdCount: Int,
    availableAnimals: Int,
    adoptedAnimals: Int,
    totalStaff: Int,
    totalAdoptions: Int
) {

  /**
   * Pretty-prints the shelter statistics in a formatted box.
   */
  def display(): Unit = {
    println("+" + "-" * 52 + "+")
    println("|                SHELTER STATISTICS                  |")
    println("+" + "-" * 52 + "+")
    println(f"| Total Animals Registered   : ${totalAnimals}%-28d |")
    println(f"|   - Dogs                   : ${dogCount}%-28d |")
    println(f"|   - Cats                   : ${catCount}%-28d |")
    println(f"|   - Birds                  : ${birdCount}%-28d |")
    println("|" + "-" * 52 + "|")
    println(f"| Available for Adoption     : ${availableAnimals}%-28d |")
    println(f"| Successfully Adopted       : ${adoptedAnimals}%-28d |")
    println("|" + "-" * 52 + "|")
    println(f"| Total Staff Members        : ${totalStaff}%-28d |")
    println(f"| Total Adoption Records     : ${totalAdoptions}%-28d |")
    println("+" + "-" * 52 + "+")
  }
}
