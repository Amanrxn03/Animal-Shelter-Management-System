name := "AnimalShelterManagementSystem"

version := "1.0.0"

scalaVersion := "2.13.14"

// ScalaTest library for automated unit testing
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.19" % Test
)

// Compiler options for cleaner code and warnings
scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked"
)

// Enable interactive terminal / console input
Compile / run / fork := true
Compile / run / connectInput := true
