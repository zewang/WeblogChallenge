package WebLogProcess

import org.apache.spark.sql.{SparkSession}

object WebLog {

  def main(args: Array[String]): Unit = {

    // # of arguments validation
    if (args.length != 2) {
      println("Require two parameters <inputFile> <outputPath>")
      System.exit(-1)
    }

    // First argument is the hdfs path of input file
    val inputFile = args(0)
    // Second argument is the output file hdfs path
    val outputPath = args(1)

    // Start a spark session
    val spark = SparkSession
      .builder()
      .appName("WebLogProcess")
      .enableHiveSupport()
      .getOrCreate()

    // Initialize WebLogReader to read csv file
    val webLogReader = new WebLogReader(spark, inputFile)
    val raw_log_df = webLogReader.rawDF

    // Initialize WebLog Processor to process log for different session window
    val webLogProcessor = new WebLogProcessor

    //for (i <- 15 to 45) {
    //  webLogProcessor.ProcessLog(raw_log_df,i,outputPath)
    //}

    webLogProcessor.ProcessLog(raw_log_df,35,outputPath)

    // Stop spark session
    spark.stop()
  }
}
