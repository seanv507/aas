export SPARK_HOME=/home/sean/predictionio_11_0/PredictionIO-0.11.0-incubating/vendors/spark-2.1.0-bin-hadoop2.7
$SPARK_HOME/bin/spark-submit --class "com.cloudera.datascience.recommender.StatsRecommender" \
 target/scala-2.11/recommender-engines-with-audioscrobbler-data_2.11-0.0.1.jar

