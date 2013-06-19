package com.yundong.imember.customWidget;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ProgressBar;

public class MyDialog extends ProgressDialog {
	private Context _context;
	private AlertDialog.Builder builder;
	private boolean isLoad;
	
	public MyDialog(Context context) {
		super(context);
		
		builder = new AlertDialog.Builder(context);
	}
	
	public void create(){
		builder.create();
	}
	
	public void show(){
		if(isLoad){
			super.show();
		}
		else{
			builder.show();
		}
	}
	
	public void setIcon(int id){
		super.setIcon(id);
		builder.setIcon(id);
	}
	
	public void setTitle(String content){
		super.setTitle(content);
		builder.setTitle(content);
	}

	public void setLoad(){
		isLoad = true;
	}
	
	public void setProgress(String str){
		
	}
	
	public void setMessage(String content){
		super.setMessage(content);
		builder.setMessage(content);
	}
	
	public void setLeftButton(String content, final MyOnClickListener listener){
		setButton(DialogInterface.BUTTON_NEUTRAL, content, listener);
		builder.setPositiveButton(content, listener);
	}
	
	public void setRightButton(String content, final MyOnClickListener listener){
		setButton(DialogInterface.BUTTON_NEGATIVE, content,listener);
		builder.setNegativeButton(content, listener);
	}
	
	public interface MyOnClickListener extends DialogInterface.OnClickListener{
//		void onClick(View dialog, int which);
	}
	
	public interface MyCloseListener{
		void onClick(View dialog);
	}
}
