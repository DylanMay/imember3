package com.yundong.imember.activity;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.db.SQLite;

import android.view.KeyEvent;
import android.widget.Toast;

public abstract class HomeActivity extends SuperActivity{
	private long mExitTime;

	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		 if (keyCode == KeyEvent.KEYCODE_BACK) {
             if ((System.currentTimeMillis() - mExitTime) > 2000) {
                     Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                     mExitTime = System.currentTimeMillis();

             } else {
            	 ImemberApplication.getInstance().getFinishActivityManager().finishAllSubActivitys();
            	 ImemberApplication.getInstance().getFinishActivityManager().finishAllMainActivitys();
            	 
            	 SQLite sqlite = ImemberApplication.getInstance().getSQLite();
            	 if(sqlite.queryCity()){
            		 sqlite.deleteCity();
            	 }
            	 sqlite.insertCity(ImemberApplication.getInstance().getCurrentCity());
            	 
            	 System.exit(0);
             }
             return true;
     }
     return super.onKeyDown(keyCode, event);
	};
}
