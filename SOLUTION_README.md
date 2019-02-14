# WeblogChallenge

## Processing & Analytical goals:

1. Sessionize the web log by IP. Sessionize = aggregrate all page hits by visitor/IP during a session.
    https://en.wikipedia.org/wiki/Session_(web_analytics)
   * files are saved in hdfs, sample output as below for #4 

2. Determine the average session time
   * Session inactivity window 15 mins: ~100.70s
   * Session inacticity window 30 mins: ~163.97s

3. Determine unique URL visits per session. To clarify, count a hit to a unique URL only once per session.
   * sample output as below for #4

4. Find the most engaged users, ie the IPs with the longest session times
   * Session inactivity window 15 mins:
```bash
   +---------------+---------+----------+----------+---------------+--------+
   |             IP|sessionId| StartTime|   EndTime|SessionDuration|URLCount|
   +---------------+---------+----------+----------+---------------+--------+
   |  119.81.61.166|        5|1437561028|1437563097|           2069|    1739|
   |   52.74.219.71|        5|1437561028|1437563097|           2069|    9532|
   |  106.186.23.95|        5|1437561028|1437563097|           2069|    2731|
   |   125.20.39.66|        4|1437561028|1437563096|           2068|     239|
   |   125.19.44.66|        5|1437561028|1437563096|           2068|     457|
   | 180.211.69.209|        4|1437561028|1437563095|           2067|      75|
   |   192.8.190.10|        3|1437561029|1437563096|           2067|     111|
   |  54.251.151.39|        5|1437561030|1437563097|           2067|       7|
   | 180.179.213.70|        5|1437561028|1437563094|           2066|     121|
   |  122.15.156.64|        3|1437561029|1437563095|           2066|      61|
   | 203.191.34.178|        2|1437561030|1437563096|           2066|     160|
   | 203.189.176.14|        5|1437561030|1437563096|           2066|      44|
   | 180.151.80.140|        4|1437561030|1437563095|           2065|     122|
   | 125.16.218.194|        1|1437561029|1437563094|           2065|      31|
   | 103.29.159.138|        1|1437561029|1437563094|           2065|       4|
   |213.239.204.204|        2|1437561029|1437563094|           2065|     234|
   |    78.46.60.71|        1|1437561028|1437563092|           2064|     237|
   | 103.29.159.186|        2|1437561029|1437563093|           2064|       2|
   |  192.71.175.30|        3|1437561031|1437563094|           2063|      37|
   |   14.99.226.79|        1|1437561033|1437563096|           2063|      78|
   +---------------+---------+----------+----------+---------------+--------+
```

   * Session inactivity window 30 mins:
```bash
   +---------------+---------+----------+----------+---------------+--------+
   |             IP|sessionId| StartTime|   EndTime|SessionDuration|URLCount|
   +---------------+---------+----------+----------+---------------+--------+
   |  220.226.206.7|        8|1437580238|1437583487|           3249|     245|
   |   52.74.219.71|        5|1437561028|1437563097|           2069|    9532|
   |  119.81.61.166|        5|1437561028|1437563097|           2069|    1739|
   |  106.186.23.95|        5|1437561028|1437563097|           2069|    2731|
   |   125.20.39.66|        4|1437561028|1437563096|           2068|     239|
   |   125.19.44.66|        5|1437561028|1437563096|           2068|     457|
   |  54.251.151.39|        5|1437561030|1437563097|           2067|       7|
   | 180.211.69.209|        4|1437561028|1437563095|           2067|      75|
   |   192.8.190.10|        3|1437561029|1437563096|           2067|     111|
   |  122.15.156.64|        3|1437561029|1437563095|           2066|      61|
   | 203.191.34.178|        2|1437561030|1437563096|           2066|     160|
   | 180.179.213.70|        5|1437561028|1437563094|           2066|     121|
   | 202.167.250.59|        4|1437561029|1437563095|           2066|      16|
   | 203.189.176.14|        5|1437561030|1437563096|           2066|      44|
   |     46.4.95.15|        1|1437561032|1437563097|           2065|     111|
   | 125.16.218.194|        1|1437561029|1437563094|           2065|      31|
   |213.239.204.204|        2|1437561029|1437563094|           2065|     234|
   | 103.29.159.138|        1|1437561029|1437563094|           2065|       4|
   | 180.151.80.140|        4|1437561030|1437563095|           2065|     122|
   | 103.29.159.186|        2|1437561029|1437563093|           2064|       2|
   +---------------+---------+----------+----------+---------------+--------+
```

## Tools
- Spark 2.3.0
- Scala 2.11
- Docker image for HDP Sandbox

## Commands to run 
```bash
sbt assembly
```
* submit to HDP Sandbox, spark.sh in script/

### Additional notes:
- IP addresses do not guarantee distinct users, but this is the limitation of the data. As a bonus, consider what additional data would help make better analytical conclusions
  * Need more information to identidy user, together with user_agent, such as user_id etc.
- For this dataset, complete the sessionization by time window rather than navigation. Feel free to determine the best session window time on your own, or start with 15 minutes.
  * After a brief research on the following papers: http://users.umiacs.umd.edu/~jimmylin/publications/Murray_etal_ASIST2006.pdf, http://isidroaguillo.webometrics.info/sites/default/files/publicaciones/Ortega2010-Differences_between_web_sessions_according_to_the_origin_of_their_visits.pdf and http://www.cs.huji.ac.il/~feit/papers/Ses12SYSTOR.pdf, I gained some insights on how to determine the session window time.
  * Did some experiments on the global session threshold to investigate the session window that stablize the number of sessions. With the current dataset, the session window is 35 minutes
```bash
# of Session generated is:105467
Average Session Time (in seconds) is: [169.5174888827785]
Top 20 most engaged users with longest session time are:
+---------------+---------+----------+----------+---------------+--------+
|             IP|sessionId| StartTime|   EndTime|SessionDuration|URLCount|
+---------------+---------+----------+----------+---------------+--------+
|  220.226.206.7|        8|1437580238|1437583487|           3249|     245|
|  119.81.61.166|        5|1437561028|1437563097|           2069|    1739|
|   52.74.219.71|        5|1437561028|1437563097|           2069|    9532|
|  106.186.23.95|        5|1437561028|1437563097|           2069|    2731|
|   125.20.39.66|        4|1437561028|1437563096|           2068|     239|
|   125.19.44.66|        5|1437561028|1437563096|           2068|     457|
| 180.211.69.209|        4|1437561028|1437563095|           2067|      75|
|   192.8.190.10|        3|1437561029|1437563096|           2067|     111|
|  54.251.151.39|        5|1437561030|1437563097|           2067|       7|
|  122.15.156.64|        3|1437561029|1437563095|           2066|      61|
| 203.191.34.178|        2|1437561030|1437563096|           2066|     160|
| 180.179.213.70|        5|1437561028|1437563094|           2066|     121|
| 203.189.176.14|        5|1437561030|1437563096|           2066|      44|
| 202.167.250.59|        4|1437561029|1437563095|           2066|      16|
| 125.16.218.194|        1|1437561029|1437563094|           2065|      31|
|213.239.204.204|        2|1437561029|1437563094|           2065|     234|
|     46.4.95.15|        1|1437561032|1437563097|           2065|     111|
| 180.151.80.140|        4|1437561030|1437563095|           2065|     122|
| 103.29.159.138|        1|1437561029|1437563094|           2065|       4|
|    78.46.60.71|        1|1437561028|1437563092|           2064|     237|
+---------------+---------+----------+----------+---------------+--------+
```
  * Session window threshold can be user-specific as well 
