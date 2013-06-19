package com.yundong.imember.myinterface;

import java.util.ArrayList;


public class LoginSuccessManager {
	private ArrayList<LoginSuccessListener> loginSuccessListeners;
	
	public LoginSuccessManager() {
		loginSuccessListeners = new ArrayList<LoginSuccessListener>();
	}
	
	public void notifyLoginSuccess(String userName, String psd) {
		for (int i = 0; i < loginSuccessListeners.size(); i++) {
			LoginSuccessListener loginSuccessListener = (LoginSuccessListener)loginSuccessListeners.get(i);
			loginSuccessListener.loginSuccess(userName, psd);
		}
	}

	public void registeLoginSuccessListener(LoginSuccessListener loginSuccessListener) {
		loginSuccessListeners.add(loginSuccessListener);
	}

	public void removeLoginSuccessListener(LoginSuccessListener loginSuccessListener) {
		int i = loginSuccessListeners.indexOf(loginSuccessListener);
		if(i >= 0){
			loginSuccessListeners.remove(i);
		}
	}
}
