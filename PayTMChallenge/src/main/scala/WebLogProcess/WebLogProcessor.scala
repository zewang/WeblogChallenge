package WebLogProcess

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._

class WebLogProcessor {

  /**
    *
    * @param df
    * @param window_m
    * @param outputPath
    */

  def ProcessLog(df: DataFrame, window_m: Int, outputPath: String) = {

    println("Start processing of session window " + window_m.toString() + " minutes")

    // Extract major columns (Timestamp, IP address and URL) and convert timestamp type to long for further processing
    val refined_df = df
      .select("_c0", "_c2", "_c11")
      .withColumn("Timestamp", col("_c0").cast("timestamp").cast("long"))
      .withColumn("IP", split(col("_c2"), ":").getItem(0))
      .withColumn("URL", split(col("_c11"), " ").getItem(1))
      .drop("_c0","_c2","_c11")

    // Utilize window function to identify new session and then derive session id
    val refined_df_withSession = refined_df
      .withColumn("PrevTimestamp",
        lag(col("Timestamp"),1).over(Window.partitionBy(col("IP")).orderBy(col("Timestamp"))))
      .withColumn("NewSessionInd",
        when(col("Timestamp").minus(col("PrevTimestamp")) < lit(window_m * 60), lit(0)).otherwise(lit(1)))
      .withColumn("SessionId", sum(col("NewSessionInd")).over(Window.partitionBy(col("IP")).orderBy(col("IP"), col("Timestamp"))))

    // Aggregate by IP and session id to calculate session duration and unique url counts
    val session_df = refined_df_withSession.
      groupBy("IP", "sessionId")
      .agg(
        min("Timestamp").as("StartTime"),
        max("Timestamp").as("EndTime"),
        (max("Timestamp")-min("Timestamp")).as("SessionDuration"),
        countDistinct("URL").as("URLCount")
      )

    // Write output to hdfs location
    println("Session results (session duration and url unique counts) will be saved at: " + outputPath)
    val outputFile = outputPath + window_m.toString() + "_session"
    session_df.write.csv(outputFile)
    println("# of Session generated is:" + session_df.count().toString())

    // Calculate the average session time by averaging session duration
    val Avg_Session = session_df.agg(avg("SessionDuration"))
    println("Average Session Time (in seconds) is: " + Avg_Session.take(1)(0).toString())

    // Sort by session duration to get the top 20 most engaged users with the longest session time
    val Engaged_User = session_df.sort(desc("SessionDuration"))
    println("Top 20 most engaged users with longest session time are: ")
    Engaged_User.show(20)
  }
}
