name := "bb3rest"

organization  := "org.biobank"

version       := "0.1-SNAPSHOT"

scalaVersion  := "2.11.2"

scalacOptions in ThisBuild ++= Seq(
  "-target:jvm-1.7",
  "-encoding", "UTF-8",
  "deprecation",        // warning and location for usages of deprecated APIs
  "-feature",           // warning and location for usages of features that should be imported explicitly
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:existentials",
  "-language:postfixOps",
  "-unchecked",          // additional warnings where generated code depends on assumptions
  "-Xlint",
  "-Ywarn-adapted-args", // Warn if an argument list is modified to match the receiver
  "-Ywarn-dead-code",
  "-Ywarn-inaccessible",
  "-Ywarn-value-discard" // Warn when non-Unit expression results are unused
)

scalacOptions in (Compile,doc) := Seq("-groups", "-implicits")

libraryDependencies ++= {
  val akkaV = "2.3.5"
  val sprayV = "1.3.1"
  Seq(
    "org.slf4j"                  %  "slf4j-simple"         % "1.7.2",
    "org.json4s"                 %% "json4s-native"        % "3.2.9",
    "com.typesafe.slick"         %% "slick"                % "2.1.0",
    "mysql"                      %  "mysql-connector-java" % "5.1.28",
    "io.spray"                   %% "spray-can"            % sprayV,
    "io.spray"                   %% "spray-routing"        % sprayV,
    "io.spray"                   %% "spray-testkit"        % sprayV   % "test",
    "com.typesafe.akka"          %% "akka-actor"           % akkaV,
    "com.typesafe.akka"          %% "akka-testkit"         % akkaV    % "test",
    "org.specs2"                 %% "specs2-core"          % "2.3.11" % "test"
  )
}

Revolver.settings
