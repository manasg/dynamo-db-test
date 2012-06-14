package com.agent8.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class TestActiveUsers {
	public ArrayList<ActiveUser> generateTestData(int numUsers) {
		ArrayList<ActiveUser> activeUsers = new ArrayList<ActiveUser>();
		
		Random generator = new Random(12323);
		
		for(int i=0; i<numUsers; i++) {
			String index = Integer.toString(i);
			
			String key = index + "_KKKKcd54c7c2af5cf3a80";
			String username = index + "_username";
			
			HashMap<String, HashMap<String, String>> emailAccounts = getEmailAccounts(index);
		
			HashMap<String, String> facebookAccount = new HashMap<String, String>();
			
			Boolean isFacebookEnabled = false;
			if (generator.nextInt()%2 == 0) {
				isFacebookEnabled = true;
				facebookAccount.put(index+"facebook", "FFFF128973213812");	
			}
			
			Boolean isGoogleEnabled = generator.nextInt()%2 == 0 ? true : false;

			long lastUpdateTime = new Date().getTime() - (15*i*60*1000);
			
			ActiveUser activeUser = new ActiveUser();
			activeUser.setKey(key);
			activeUser.setUserName(username);
			activeUser.setEmailAccounts(emailAccounts);
			activeUser.setFacebookAccount(facebookAccount);
			activeUser.setFacebookEnabled(isFacebookEnabled);
			activeUser.setGoogleEnabled(isGoogleEnabled);
			activeUser.setLastUpdateTime(new Date(lastUpdateTime));
		
			activeUsers.add(activeUser);
		}
		
		return activeUsers;
	}
	
	private HashMap<String, HashMap<String, String>> getEmailAccounts(String index) {
		HashMap<String, String> emailAccount = new HashMap<String, String>();
		emailAccount.put(index+".g@gmail-1", index + "GGGGad974d00f82ab5");
		emailAccount.put(index+".g@gmail-2", index + "GGGG7a68ebbde119fe");
		
		HashMap<String, HashMap<String, String>> emailAccounts = new HashMap<String, HashMap<String, String>>();
		emailAccounts.put("gmail", emailAccount);
		
		emailAccount = new HashMap<String, String>();
		emailAccount.put(index+".y@yahoo-1", "YYYYYYY9743647778d00f82ab5");
		emailAccounts.put("yahoo", emailAccount);
		
		return emailAccounts;
	}

}
