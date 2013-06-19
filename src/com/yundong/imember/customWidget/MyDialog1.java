package com.yundong.imember.customWidget;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yundong.imember.R;

public class MyDialog1 extends Dialog {
	private Context _context;
	private Button left;
	private Button right;
	private boolean left_wake;
	private boolean right_wake;
	private ArrayList<Button> list = new ArrayList<Button>();
	private LayoutInflater inflater;
	private View mydialog;
	
	private ImageView icon;
	private boolean icon_wake;
//	private ImageView load;
	private ProgressBar loadbar;
	private boolean load_wake;
//	private boolean isRunning;
	private TextView title;
	private boolean title_wake;
	private TextView message;
	private boolean message_wake;
	private ImageView line_bottom;
	
	private int[] load_drawables = {R.drawable.load1, R.drawable.load2, R.drawable.load3, R.drawable.load4, 
			R.drawable.load5, R.drawable.load6, R.drawable.load7, R.drawable.load8, R.drawable.load9, R.drawable.load10};
	private int step;

	public MyDialog1(Context context) {
		super(context, R.style.MyDialog);
		setContentView(R.layout.mydialog);
		
		initUI(context);
	}
	
	private void initUI(Context context){
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mydialog = (FrameLayout)inflater.inflate(R.layout.mydialog, null);
		
		left = (Button) findViewById(R.id.mydialog_btn_left);
		right = (Button) findViewById(R.id.mydialog_btn_right);
		
		icon = (ImageView)findViewById(R.id.mydialog_icon);
//		load = (ImageView)findViewById(R.id.load);
		loadbar = (ProgressBar)findViewById(R.id.loadbar);
		title = (TextView)findViewById(R.id.mydialog_title);
		message = (TextView)findViewById(R.id.mydialog_message);
		line_bottom = (ImageView)findViewById(R.id.line_bottom);
	}
	
	public void create(){
		if(left_wake){
			left.setVisibility(View.VISIBLE);
		}
		if(right_wake){
			right.setVisibility(View.VISIBLE);
		}
		if(!left_wake && !right_wake){
			line_bottom.setVisibility(View.GONE);
		}
		if(icon_wake){
			icon.setVisibility(View.GONE);
		}
		if(title_wake){
			title.setVisibility(View.VISIBLE);
		}
		if(load_wake){
//			load.setVisibility(View.VISIBLE);
			loadbar.setVisibility(View.VISIBLE);
//			startLoad();
		}
		if(message_wake){
			message.setVisibility(View.VISIBLE);
		}
	}
	
	public void setIcon(int id){
		icon_wake = true;
		icon.setImageResource(id);
	}
	
	public void setTitle(String content){
		title_wake = true;
		title.setText(content);
	}

	public void setLoad(){
		load_wake = true;
	}
	
	public void setProgress(String str){
		Message message = handler_text.obtainMessage();
		message.obj = str;
		handler_text.sendMessage(message);
	}
	
//	private void startLoad(){
//		isRunning = true;
//		handler_load.sendEmptyMessage(0);
//	}
//	private void stopLoad(){
//		isRunning = false;
//	}
	
//	private Handler handler_load = new Handler(){
//		@Override
//		public void handleMessage(Message msg) {
//			// TODO Auto-generated method stub
//			if(load != null){
//				load.setBackgroundResource(load_drawables[step]);
//				step ++;
//				if(step > 9){
//					step = 0;
//				}
//				if(isRunning){
//					handler_load.sendEmptyMessageDelayed(0, 150);
//				}
//			}
//		}
//	};
	
	private Handler handler_text = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(message != null){
				message.setText((String)msg.obj);
			}
		}
	};
	
	public void setMessage(String content){
		message_wake = true;
		message.setText(content);
	}
	
	public void setLeftButton(String content, final MyOnClickListener listener){
		left_wake = true;
		left.setText(content);
		left.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listener.onClick(mydialog, list.indexOf(v));
				
//				if(isRunning){
//					stopLoad();
//				}
				cancel();
			}

		});
		list.add(left);
	}
	
	public void setRightButton(String content, final MyOnClickListener listener){
		right_wake = true;
		right.setText(content);
		right.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listener.onClick(mydialog, list.indexOf(v));
				
//				if(isRunning){
//					stopLoad();
//				}
				cancel();
			}

		});
		list.add(right);
	}
	
	public interface MyOnClickListener{
		void onClick(View dialog, int which);
	}
	
	public interface MyCloseListener{
		void onClick(View dialog);
	}
}
