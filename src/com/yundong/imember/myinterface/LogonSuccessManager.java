package com.yundong.imember.myinterface;

import java.util.ArrayList;


public class LogonSuccessManager {
	private ArrayList<LogonSuccessListener> logonSuccessListeners;
	
	public LogonSuccessManager() {
		logonSuccessListeners = new ArrayList<LogonSuccessListener>();
	}
	
	public void notifyLogonSuccess() {
		for (int i = 0; i < logonSuccessListeners.size(); i++) {
			LogonSuccessListener logonSuccessListener = (LogonSuccessListener)logonSuccessListeners.get(i);
			logonSuccessListener.logonSuccess();
		}
	}
	
	public void notifyLogonFail(String username) {
		for (int i = 0; i < logonSuccessListeners.size(); i++) {
			LogonSuccessListener logonSuccessListener = (LogonSuccessListener)logonSuccessListeners.get(i);
			logonSuccessListener.logonFail(username);
		}
	}

	public void registeLogonSuccessListener(LogonSuccessListener logonSuccessListener) {
		logonSuccessListeners.add(logonSuccessListener);
	}

	public void removeLogonSuccessListener(LogonSuccessListener logonSuccessListener) {
		int i = logonSuccessListeners.indexOf(logonSuccessListener);
		if(i >= 0){
			logonSuccessListeners.remove(i);
		}
	}
}
