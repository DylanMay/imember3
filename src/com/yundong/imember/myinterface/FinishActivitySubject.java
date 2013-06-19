package com.yundong.imember.myinterface;

public interface FinishActivitySubject {
	public void registFinishSubActivity(FinishActivityListener finishActivityListener);
	public void removeFinishSubActivity(FinishActivityListener finishActivityListener);
	public void notifyFinishSubActivity();
	
	public void registFinishMainActivity(FinishActivityListener finishActivityListener);
	public void removeFinishMainActivity(FinishActivityListener finishActivityListener);
	public void notifyFinishMainActivity();
}
