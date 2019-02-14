spark_submit="/usr/hdp/current/spark2-client/bin/spark-submit"
app_jar="PayTMChallenge-0.1.jar"
input="/tmp/weblogdata/2015_07_22_mktplace_shop_web_log_sample.log"
date=`date +%Y%m%d%H%M%S`
output="/tmp/weblogdata/output/$date/"

$spark_submit --master yarn-client --class WebLogProcess.WebLog $app_jar $input $output

