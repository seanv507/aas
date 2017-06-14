name := "Recommender Engines with Audioscrobbler data"
version := "0.0.1"
scalaVersion := "2.11.8"
// additional libraries
// spark core marked as provided so that later when using assembly jar, 
// dont include since already on classpath of workers
libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-core" % "2.1.0" % "provided",
    "org.apache.spark" %% "spark-sql" % "2.1.0" % "provided",
    "org.apache.spark" %% "spark-mllib" % "2.1.0" % "provided"
)