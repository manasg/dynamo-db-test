package com.agent8.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.amazonaws.services.dynamodb.model.AttributeValue;

public class ConvertToKV {
	
	public static Map<String, AttributeValue> getActiveUserKV(ActiveUser activeUser) {
		
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		
		item.put("key", new AttributeValue().withS(activeUser.getKey()));
		item.put("userName", new AttributeValue(activeUser.getUserName()));
		
		// TODO : Don't pass the hashmap around!
		item = getEmailAccountsKV(item, activeUser);
		
		if (activeUser.isFacebookEnabled()) {
			item = getFacebookAccountKV(item, activeUser);
			item.put("isFacebookEnabled", new AttributeValue().withN("1"));	
		} else {
			item.put("isFacebookEnabled", new AttributeValue().withN("0"));
		}
		
		if (activeUser.isGoogleEnabled()) {
			item.put("isGoogleEnabled", new AttributeValue().withN("1"));	
		} else {
			item.put("isGoogleEnabled", new AttributeValue().withN("0"));
		}
		
		// Getting Date object -> Getting TIME since Epoch -> Storing as N
		long time = activeUser.getLastUpdateTime().getTime();
		item.put("lastUpdateTime", new AttributeValue().withN(Long.toString(time)));
		
		return item;
	}
	

	
	private static Map<String, AttributeValue> getEmailAccountsKV(Map<String, AttributeValue> item, ActiveUser activeUser) {
		// TODO : This should be a generic method to traverse levels of HashMaps?
		HashMap<String, HashMap<String, String>> _emailAccounts = (HashMap<String, HashMap<String, String>>) activeUser.getEmailAccounts();
		
		Collection<String> c = _emailAccounts.keySet();
		item.put("emailProviders", new AttributeValue().withSS(c));
		
		Iterator<String> itr = c.iterator();
		while(itr.hasNext()) {
			String emailProvider = (String) itr.next();
			HashMap<String, String> emailProviderAccounts = (HashMap<String, String>) _emailAccounts.get(emailProvider);
			
			Collection<String> _accountNames = emailProviderAccounts.keySet();
			item.put(emailProvider, new AttributeValue().withSS(_accountNames));
			
			Iterator<String> itr2 = _accountNames.iterator();
			while(itr2.hasNext()) {
				String _account = (String) itr2.next();
				String token = emailProviderAccounts.get(_account);
				
				item.put(_account, new AttributeValue(token));
			}
			
		}
		
		return item;
		
	}
	
	private static Map<String, AttributeValue> getFacebookAccountKV(Map<String, AttributeValue> item, ActiveUser activeUser) {
		// TODO : Assuming ONLY 1 fb account!
		HashMap<String, String> _facebookAccount = activeUser.getFacebookAccount();
		
		Set<String> c = _facebookAccount.keySet();
		Iterator<String> itr = c.iterator();
		
		while(itr.hasNext()) {
			String i = (String) itr.next();
			String _token = _facebookAccount.get(i);
			
			item.put(i, new AttributeValue(_token));
			// The following line needs to be changed if more than one FB accounts are needed
			item.put("facebook",new AttributeValue(i));
			 
		}
		
		return item;
	}

}
