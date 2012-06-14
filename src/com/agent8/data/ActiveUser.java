package com.agent8.data;

import java.util.Date;
import java.util.HashMap;

public class ActiveUser {

    private String key;

	private String userName;

	HashMap<String, HashMap<String, String>> emailAccounts;

	HashMap<String, String> facebookAccount;

	Boolean isFacebookEnabled;

	Boolean isGoogleEnabled;

	private Date lastUpdateTime;
	
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public HashMap<String, HashMap<String, String>> getEmailAccounts() {
		return emailAccounts;
	}

	public void setEmailAccounts(
			HashMap<String, HashMap<String, String>> emailAccounts) {
		this.emailAccounts = emailAccounts;
	}

	public HashMap<String, String> getFacebookAccount() {
		return facebookAccount;
	}

	public void setFacebookAccount(HashMap<String, String> facebookAccount) {
		this.facebookAccount = facebookAccount;
	}

	public boolean isFacebookEnabled() {
		return isFacebookEnabled;
	}

	public void setFacebookEnabled(boolean isFacebookEnabled) {
		this.isFacebookEnabled = isFacebookEnabled;
	}

	public boolean isGoogleEnabled() {
		return isGoogleEnabled;
	}

	public void setGoogleEnabled(boolean isGoogleEnabled) {
		this.isGoogleEnabled = isGoogleEnabled;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	@Override
	public String toString() {
		return "ActiveUser [key=" + key + ", userName=" + userName
				+ ", emailAccounts=" + emailAccounts + ", facebookAccount="
				+ facebookAccount + ", isFacebookEnabled=" + isFacebookEnabled
				+ ", isGoogleEnabled=" + isGoogleEnabled + ", lastUpdateTime="
				+ lastUpdateTime + "]";
	}
}