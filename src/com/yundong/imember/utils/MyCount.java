package com.yundong.imember.utils;

import com.yundong.imember.R;

import android.os.CountDownTimer;
import android.widget.Button;

public class MyCount extends CountDownTimer {
	private Button btn;

	public MyCount(long millisInFuture, long countDownInterval, Button btn) {
		super(millisInFuture, countDownInterval);
		this.btn = btn;
	}

	@Override
	public void onFinish() {
		btn.setText("���»�ȡ��֤��");
		btn.setBackgroundResource(R.drawable.btn_getvalidate_bg);
		btn.setClickable(true);
	}

	@Override
	public void onTick(long millisUntilFinished) {
		btn.setText("���»�ȡ��֤��(" + millisUntilFinished / 1000 + "s)");
		btn.setBackgroundResource(R.drawable.btn_getvalidate_clicked);
	}
}