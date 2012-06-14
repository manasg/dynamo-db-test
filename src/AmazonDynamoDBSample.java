
/*

 * Copyright 2012 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.agent8.data.ActiveUser;
import com.agent8.data.ConvertToKV;
import com.agent8.data.TestActiveUsers;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.dynamodb.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodb.model.AttributeValue;
import com.amazonaws.services.dynamodb.model.ComparisonOperator;
import com.amazonaws.services.dynamodb.model.Condition;
import com.amazonaws.services.dynamodb.model.CreateTableRequest;
import com.amazonaws.services.dynamodb.model.DescribeTableRequest;
import com.amazonaws.services.dynamodb.model.KeySchema;
import com.amazonaws.services.dynamodb.model.KeySchemaElement;
import com.amazonaws.services.dynamodb.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodb.model.PutItemRequest;
import com.amazonaws.services.dynamodb.model.PutItemResult;
import com.amazonaws.services.dynamodb.model.ScanRequest;
import com.amazonaws.services.dynamodb.model.ScanResult;
import com.amazonaws.services.dynamodb.model.TableDescription;
import com.amazonaws.services.dynamodb.model.TableStatus;

/**
 * This sample demonstrates how to perform a few simple operations with the
 * Amazon DynamoDB service.
 */
public class AmazonDynamoDBSample {

	/*
	 * Important: Be sure to fill in your AWS access credentials in the
	 * AwsCredentials.properties file before you try to run this sample.
	 * http://aws.amazon.com/security-credentials
	 */

	static AmazonDynamoDBClient dynamoDB;

	/**
	 * The only information needed to create a client are security credentials
	 * consisting of the AWS Access Key ID and Secret Access Key. All other
	 * configuration, such as the service endpoints, are performed
	 * automatically. Client parameters, such as proxies, can be specified in an
	 * optional ClientConfiguration object when constructing a client.
	 * 
	 * @see com.amazonaws.auth.BasicAWSCredentials
	 * @see com.amazonaws.auth.PropertiesCredentials
	 * @see com.amazonaws.ClientConfiguration
	 */
	private static void init() throws Exception {
		AWSCredentials credentials = new PropertiesCredentials(
				AmazonDynamoDBSample.class
						.getResourceAsStream("AwsCredentials.properties"));

		dynamoDB = new AmazonDynamoDBClient(credentials);
	}

	public static void main(String[] args) throws Exception {
		init();

		try {
			String tableName = "mg-ActiveUsers";

            // Create a table with a primary key named key, which holds a string
			// IMPORTANT : NEEDS to run one time only
            /*
            CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName)
                .withKeySchema(new KeySchema(new KeySchemaElement().withAttributeName("key").withAttributeType("S")))
                .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(10L).withWriteCapacityUnits(10L));
            
            TableDescription createdTableDescription = dynamoDB.createTable(createTableRequest).getTableDescription();
            System.out.println("Created Table: " + createdTableDescription);
			*/
			
			
			
			TestActiveUsers test = new TestActiveUsers();
			ArrayList<ActiveUser> users = test.generateTestData(5);
			
			for (ActiveUser activeUser : users) {
				//System.out.println(activeUser.toString());
				//System.out.println();

				Map<String, AttributeValue> item = ConvertToKV
						.getActiveUserKV(activeUser);
				
				System.out.println(item.get("userName"));
				long t = new Long(item.get("lastUpdateTime").getN());
				System.out.println(new Date(t));				
				//System.out.println(new Date(lastUpdateTime));
				System.out.println();
				
				PutItemRequest putItemRequest = new PutItemRequest(tableName,item);
				PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
				System.out.println("Result: " + putItemResult);

			}

			System.out.println();
			System.out.println("Done adding to DB");
			
			//sampleSearch1(tableName);
			sampleSearch2(tableName);
			//

		} catch (AmazonServiceException ase) {
			System.out
					.println("Caught an AmazonServiceException, which means your request made it "
							+ "to AWS, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out
					.println("Caught an AmazonClientException, which means the client encountered "
							+ "a serious internal problem while trying to communicate with AWS, "
							+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
	}

	private static void sampleSearch1(String tableName) {
		System.out.println();
		
		// Get googleEnabled Accounts and print their gmail tokens + usernames
		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
		Condition condition = new Condition().withComparisonOperator(
				ComparisonOperator.EQ.toString()).withAttributeValueList(
				new AttributeValue().withN("1"));
		scanFilter.put("isGoogleEnabled", condition);
		ScanRequest scanRequest = new ScanRequest(tableName)
				.withScanFilter(scanFilter);
		ScanResult scanResult = dynamoDB.scan(scanRequest);
		System.out.println("Result: " + scanResult);
		
		Iterator<Map<String, AttributeValue>> itr = scanResult.getItems().iterator();
		while(itr.hasNext()) {
			HashMap<String, AttributeValue> item = (HashMap<String, AttributeValue>) itr.next();
			System.out.println();
			System.out.println(item.get("userName"));
			List<String> _gmailAccounts = item.get("gmail").getSS();
			for (String _account : _gmailAccounts) {
				System.out.println(item.get(_account).getS());
			}
		}
	}
	
	private static void sampleSearch2(String tableName) {
		System.out.println();
		
		// Get all users - who used the app in the last 30 mins
		// 30 mins = 30 * 60 * 1000 = 1800000 ms
		long timeLimit = new Date().getTime() - 1800000;
		System.out.println("Thirty mins ago " + timeLimit + " OR " + new Date(timeLimit));
		
		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
		
		Condition condition = new Condition().withComparisonOperator(
				ComparisonOperator.GT.toString()).withAttributeValueList(
				new AttributeValue().withN(Long.toString(timeLimit)));
		scanFilter.put("lastUpdateTime", condition);
		
		ScanRequest scanRequest = new ScanRequest(tableName).withScanFilter(scanFilter);
		ScanResult scanResult = dynamoDB.scan(scanRequest);

		//System.out.println(scanResult);
		Iterator<Map<String, AttributeValue>> itr = scanResult.getItems().iterator();
		while(itr.hasNext()) {
			HashMap<String, AttributeValue> item = (HashMap<String, AttributeValue>) itr.next();
			System.out.println();
			System.out.println(item.get("userName"));
			System.out.println(item.get("lastUpdateTime") + " OR " + new Date(new Long(item.get("lastUpdateTime").getN())));
			List<String> _gmailAccounts = item.get("gmail").getSS();
			for (String _account : _gmailAccounts) {
				System.out.println(item.get(_account).getS());
			}
		}
	}

}
