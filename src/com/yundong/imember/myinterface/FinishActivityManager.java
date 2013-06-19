package com.yundong.imember.myinterface;

import java.util.ArrayList;

public class FinishActivityManager implements FinishActivitySubject {
	private ArrayList<FinishActivityListener> finishSubActivitys;
	private ArrayList<FinishActivityListener> finishMainActivitys;

	public FinishActivityManager() {
		finishSubActivitys = new ArrayList<FinishActivityListener>();
		finishMainActivitys = new ArrayList<FinishActivityListener>();
	}
	
	@Override
	public void notifyFinishSubActivity() {
		for (int i = 0; i < finishSubActivitys.size(); i++) {
			FinishActivityListener finishActivity = (FinishActivityListener)finishSubActivitys.get(i);
			finishActivity.finishActivity();
		}
	}

	@Override
	public void registFinishSubActivity(FinishActivityListener reflushActivity) {
		finishSubActivitys.add(reflushActivity);
	}

	@Override
	public void removeFinishSubActivity(FinishActivityListener reflushActivity) {
		int i = finishSubActivitys.indexOf(reflushActivity);
		if(i >= 0){
			finishSubActivitys.remove(i);
		}
	}

	public void finishAllSubActivitys(){
		notifyFinishSubActivity();
	}
	
	@Override
	public void notifyFinishMainActivity() {
		for (int i = 0; i < finishMainActivitys.size(); i++) {
			FinishActivityListener finishActivity = (FinishActivityListener)finishMainActivitys.get(i);
			finishActivity.finishActivity();
		}
	}

	@Override
	public void registFinishMainActivity(FinishActivityListener reflushActivity) {
		finishMainActivitys.add(reflushActivity);
	}

	@Override
	public void removeFinishMainActivity(FinishActivityListener reflushActivity) {
		int i = finishMainActivitys.indexOf(reflushActivity);
		if(i >= 0){
			finishMainActivitys.remove(i);
		}
	}

	public void finishAllMainActivitys(){
		notifyFinishMainActivity();
	}
}
