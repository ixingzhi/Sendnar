package com.shichuang.open.tool;

import android.os.CountDownTimer;
import android.widget.TextView;

public class RxTimeCountTool extends CountDownTimer {
	private TextView mTextView;

	public RxTimeCountTool(long millisInFuture, long countDownInterval, TextView mTextView) {
		super(millisInFuture, countDownInterval);
		this.mTextView = mTextView;
	}

	@Override
	public void onTick(long millisUntilFinished) {
		mTextView.setClickable(false); // 倒计时过程中不能点击
		mTextView.setText(millisUntilFinished / 1000 + "秒后重新获取");// 设置倒计时时间
	}

	@Override
	public void onFinish() {
		mTextView.setText("重新发送验证码");
		mTextView.setClickable(true); // 重新获得点击
		cancel();
	}
}
