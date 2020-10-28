Global / onChangedBuildSource := ReloadOnSourceChanges
scalaVersion := "2.13.3"

scalacOptions ++= Seq(
  "-Xfatal-warnings",
  "-deprecation"
)

libraryDependencies ++= Seq(
  "org.scalatest"     %% "scalatest-shouldmatchers" % "3.2.2",
  "org.scalatest"     %% "scalatest-funspec"        % "3.2.2",
  "com.lihaoyi"       %% "fansi"                    % "0.2.7",
)

