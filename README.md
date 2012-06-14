dynamo-db-test
==============

Playing around with Dynamo DB in Java (choice of language because of $DAYJOB$)
I run this as an Eclipse Project for now

Requirements
==============
- AWS Java SDK

- AwsCredentials.properties
placed under src/
with your ACCESS and SECRET KEY

- Tables in DynamoDB - either created via Management Console or API
- AmazonDynamoDBSample contains code


Notes
=============
I ran some naive tests to get an idea of performance.

Individual inserts -

Throughput - 10, 10 (R,W)
1000 records
Done adding to DB : Took millis : 100231 => 100seconds

Increased throughput - 20, 20 (R,W) - MAX
1000 records
Same index prefix as before
Done adding to DB : Took millis : 50921 => 50seconds

Nice! And as expected.

The API supports batch operations as well that should yield much better results

Individual inserts for 10K records takes ~1000 seconds => ~16 mins

Table Scan is pretty fast till a limit. After that it's slow/expensive and its limited to 1 MB anyway.

Custom index table
-----------------
Created a second table which has reverse mapping.
Primary - isGoogleEnabled
Range - Key

During a put, I insert in 2 tables. The second table has just 2 attributes per item.

Now I can query - simply if Google is Enabled
And then use those keys to fetch the remaining data

---

For table containing about 20K records - A query takes about - 3.8s to get the keys. To get the actual items, there is one more step.

---

My JAVA skills are rusty (adding Strings in a loop is not a good idea :) ) 

There are several wrapper DynamoDB APIs in the wild. Might be better to use them. Also I saw an example on AWS where they used Java Annotations.
http://docs.amazonwebservices.com/amazondynamodb/latest/developerguide/JavaCRUDHighLevelExample1.html

Another helpful example (much cleaner API) - 
http://openmymind.net/2012/2/6/Lets-Build-Something-Using-Amazons-DynamoDB/


Addendum
=========
@DAYJOB we use GoogleAppEngine. Classes are serialized easily because of DataNucleus and other JAVA mumbo jumbo. I used brute force here to flatten out a class.

The class in question is - com.agent8.data.ActiveUser