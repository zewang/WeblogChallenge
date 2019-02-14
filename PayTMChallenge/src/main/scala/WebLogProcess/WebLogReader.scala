package WebLogProcess

import org.apache.spark.sql.{SparkSession}

/**
  *
  * @param spark
  * @param inputFile
  */

class WebLogReader(spark: SparkSession, inputFile: String) {

  val rawDF = spark.read
      .format("csv")
      .option("header", "false")
      .option("delimiter", " ")
      .load(inputFile)
}
