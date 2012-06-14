package com.agent8.data;

import java.util.ArrayList;
import java.util.Map;

import com.amazonaws.services.dynamodb.model.AttributeValue;

public class Runner {
	public static void main(String[] args) {
		
		TestActiveUsers test = new TestActiveUsers();
		ArrayList<ActiveUser> users = test.generateTestData(3);
		
		for (ActiveUser activeUser : users) {
			System.out.println(activeUser.toString());
			System.out.println();
			
			Map<String, AttributeValue> item = ConvertToKV.getActiveUserKV(activeUser); 
			System.out.println(item);
			
			System.out.println("-----");
		}
		
		
	}

}
