import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.dynamodb.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodb.model.AttributeValue;
import com.amazonaws.services.dynamodb.model.GetItemRequest;
import com.amazonaws.services.dynamodb.model.GetItemResult;
import com.amazonaws.services.dynamodb.model.Key;
import com.amazonaws.services.dynamodb.model.QueryRequest;
import com.amazonaws.services.dynamodb.model.QueryResult;


public class DynamoDBQuery {

	static AmazonDynamoDBClient dynamoDB;
	
	private static void init() throws Exception {
		AWSCredentials credentials = new PropertiesCredentials(
				AmazonDynamoDBSample.class
						.getResourceAsStream("AwsCredentials.properties"));

		dynamoDB = new AmazonDynamoDBClient(credentials);
	}
	
	public static void main(String[] args) throws Exception {
		init();
		
		long start = new Date().getTime();
		long end;
		
		printAllGoogleEnabled("mg-ActiveUsers-2","mg-ActiveUsers-googleEnabled-2");
		end = new Date().getTime();
		
		System.out.println("Took millis : "+ (end-start));
		
	}
	private static void printAllGoogleEnabled(String tableName, String indexTable) {
		printAllGoogleEnabledPaginated(tableName, indexTable, 2);
	}
	
	private static void printAllGoogleEnabledPaginated(String tableName, String indexTable, int resultLimit) {
		QueryRequest q = new QueryRequest();
		
		// hashKey of the table was set in the tableDefinition
		q.setTableName(indexTable);
		q.setHashKeyValue(new AttributeValue().withN("1"));
		q.setLimit(resultLimit);
		
		QueryResult queryResult = dynamoDB.query(q);
		
		Key lastEvaluatedKey = queryResult.getLastEvaluatedKey();
		printResults(tableName, queryResult);
		
		// Now using the last evaluated key and not limiting results
		q = new QueryRequest();
		
		q.setTableName(indexTable);
		q.setHashKeyValue(new AttributeValue().withN("1"));
		
		q.setExclusiveStartKey(lastEvaluatedKey);
		
		queryResult = dynamoDB.query(q);
		
		printResults(tableName, queryResult);
	}

	private static void printResults(String tableName, QueryResult queryResult) {
		Iterator<Map<String, AttributeValue>> itr = queryResult.getItems().iterator();
		int count = 0;
		while(itr.hasNext()) {
			HashMap<String, AttributeValue> item = (HashMap<String, AttributeValue>) itr.next();
			//System.out.println();
			
			// lets get the key first
			Key key = new Key(item.get("key"));
			
			// now lets get the full item from the main table
			GetItemRequest getRequest = new GetItemRequest(tableName, key);
			//GetItemResult itemResult = dynamoDB.getItem(getRequest);
			
			//Map<String, AttributeValue> item2 = itemResult.getItem();
			count++;
			//System.out.println(item2.get("userName"));
			//List<String> _gmailAccounts = item2.get("gmail").getSS();
			//for (String _account : _gmailAccounts) {
			//	System.out.println(item2.get(_account).getS());
			//}
		}
		
		System.out.println(count);
	}

}
