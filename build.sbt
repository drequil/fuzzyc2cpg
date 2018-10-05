name := "fuzzyc2cpg"
organization := "io.shiftleft"

val cpgVersion = "0.9.51"

libraryDependencies ++= Seq(
  "org.antlr" % "antlr4-runtime" % "4.5.4",
  "io.shiftleft" % "codepropertygraph" % cpgVersion,
  "io.shiftleft" % "codepropertygraph-protos" % cpgVersion,
  "com.novocode" % "junit-interface" % "0.11" % Test,
  "junit" % "junit" % "4.12" % Test
)

// uncomment if you want to use a cpg version that has *just* been released
// (it takes a few hours until it syncs to maven central)
// resolvers += "Sonatype OSS" at "https://oss.sonatype.org/content/repositories/public"

scalacOptions ++= Seq("-deprecation", "-feature")
compile / javacOptions ++= Seq("-Xlint:all", "-Xlint:-cast", "-g")
Test / javaOptions ++= Seq("-Dlog4j2.configurationFile=../cpg2sp/src/test/resources/log4j2-test.xml")
Test / fork := true
testOptions += Tests.Argument(TestFrameworks.JUnit, "-a", "-v")

checkstyleConfigLocation := CheckstyleConfigLocation.File("config/checkstyle/google_checks.xml")
checkstyleSeverityLevel := Some(CheckstyleSeverityLevel.Info)
// checkstyle := checkstyle.triggeredBy(Compile / compile).value

enablePlugins(Antlr4Plugin)
Antlr4 / antlr4PackageName := Some("io.shiftleft.fuzzyc2cpg")
Antlr4 / antlr4Version := "4.7"
Antlr4 / javaSource := (sourceManaged in Compile).value

enablePlugins(JavaAppPackaging)

