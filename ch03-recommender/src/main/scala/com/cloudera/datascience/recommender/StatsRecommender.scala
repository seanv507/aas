/*
 * Copyright 2015 and onwards Sanford Ryza, Uri Laserson, Sean Owen and Joshua Wills
 *
 * See LICENSE file for further information.
 */

package com.cloudera.datascience.recommender

import scala.collection.Map
import scala.collection.mutable.ArrayBuffer
import scala.util.Random
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.ml.recommendation.{ALS, ALSModel}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.apache.spark.sql.functions._

object StatsRecommender {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().getOrCreate()

    val base = "data/profiledata_06-May-2005/"
    val rawUserArtistData = spark.read.textFile(base + "user_artist_data.txt")
    val rawArtistData = spark.read.textFile(base + "artist_data.txt")
    val rawArtistAlias = spark.read.textFile(base + "artist_alias.txt")

    val runRecommender = new RunRecommender(spark)

    
    runRecommender.preparation(rawUserArtistData, rawArtistData, rawArtistAlias)
    val bArtistAlias = spark.sparkContext.broadcast(runRecommender.buildArtistAlias(rawArtistAlias))
    val allData = runRecommender.buildCounts(rawUserArtistData, bArtistAlias).cache()
    allData.agg(
        count("user").as("rows"),
        countDistinct("user"), 
        countDistinct("artist"),
        max("count"), 
        sum("count")).show()
/* +--------+--------------------+----------------------+----------+----------+
   |    rows|count(DISTINCT user)|count(DISTINCT artist)|max(count)|sum(count)|
   +--------+--------------------+----------------------+----------+----------+
   |24296858|              148111|               1568126|    439771| 371638969|
   +--------+--------------------+----------------------+----------+----------+
*/

    val artistsPerUser = allData.groupBy("user").agg(countDistinct("artist").as("num_artists"))
    .groupBy("num_artists").agg(count("num_artists"))
    artistsPerUser.write.format("csv").save("artists_per_user")
    val usersPerArtist = allData.groupBy("artist").agg(countDistinct("user").as("num_users"))
    .groupBy("num_users").agg(count("num_users"))

    
    val artistByID = buildArtistByID(rawArtistData)
    # how to rename 
    artistByID.rename(.join(artistsPerUser, "artist").
      select("name").show()



    artistsPerUser.write.format("csv").save("artists_per_user")
    usersPerArtist.write.format("csv").save("users_per_artist")
    //runRecommender.model(rawUserArtistData, rawArtistData, rawArtistAlias)
    //runRecommender.evaluate(rawUserArtistData, rawArtistAlias)
    //runRecommender.recommend(rawUserArtistData, rawArtistData, rawArtistAlias)
  }

}

