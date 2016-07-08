package com.lzg.speedmeter;

import java.util.Random;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.lzg.speedmeter.view.TestNeetSpeedView;

/**
 * 
 * @author lzg
 *
 */
public class MainActivity extends BaseActivity {
	private TestNeetSpeedView neetSpeedView;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		neetSpeedView = getViewById(R.id.testneetspeedview);
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					Random random = new Random();
					int speed = random.nextInt(204800);
					neetSpeedView.setNeetSpeed(speed * 1d);
					break;
				}

			}
		};
		new Thread() {
			@Override
			public void run() {
				while (true) {
					Message msg = Message.obtain();
					msg.what = 1;
					handler.sendMessage(msg);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

		}.start();
		;
	}

}
