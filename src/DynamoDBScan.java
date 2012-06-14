import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.dynamodb.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodb.model.AttributeValue;
import com.amazonaws.services.dynamodb.model.ComparisonOperator;
import com.amazonaws.services.dynamodb.model.Condition;
import com.amazonaws.services.dynamodb.model.ScanRequest;
import com.amazonaws.services.dynamodb.model.ScanResult;


public class DynamoDBScan {
	

	static AmazonDynamoDBClient dynamoDB;
	
	private static void init() throws Exception {
		AWSCredentials credentials = new PropertiesCredentials(
				AmazonDynamoDBSample.class
						.getResourceAsStream("AwsCredentials.properties"));

		dynamoDB = new AmazonDynamoDBClient(credentials);
	}
	
	
	public static void main(String[] args) throws Exception {
		init();
		
		String tableName = "mg-ActiveUsers-2";
		
		long start = new Date().getTime();
		long end;
		
		sampleSearch1(tableName);
		//sampleSearch2(tableName);
		end = new Date().getTime();
		
		System.out.println("Took millis : "+ (end-start));
		
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
		//System.out.println("Result: " + scanResult);
		
		Iterator<Map<String, AttributeValue>> itr = scanResult.getItems().iterator();
		int count = 0;
		while(itr.hasNext()) {
			HashMap<String, AttributeValue> item = (HashMap<String, AttributeValue>) itr.next();
			count ++;
			//System.out.println();
			//System.out.println(item.get("userName"));
			//List<String> _gmailAccounts = item.get("gmail").getSS();
			//for (String _account : _gmailAccounts) {
			//	System.out.println(item.get(_account).getS());
			//}
			
		}
		System.out.println(count);
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
