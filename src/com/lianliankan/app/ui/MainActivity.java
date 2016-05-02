package com.lianliankan.app.ui;

import com.lianliankan.app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private Button startBtn = null;
	
	private Button rankBtn = null;
	
	private Button settingBtn = null;
	
	private Button helpBtn = null;
	
	private Button exitBtn = null;
			
			
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		startBtn = (Button) findViewById(R.id.main_start_btn);
		startBtn.setOnClickListener(this);
		rankBtn = (Button) findViewById(R.id.main_rank_btn);
		rankBtn.setOnClickListener(this);
		settingBtn = (Button) findViewById(R.id.main_setting_btn);
		settingBtn.setOnClickListener(this);
		helpBtn = (Button) findViewById(R.id.main_help_btn);
		helpBtn.setOnClickListener(this);
		exitBtn = (Button) findViewById(R.id.main_exit_btn);
		exitBtn.setOnClickListener(this);
	}



	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		String choice = "";
		switch (arg0.getId()) {
		case R.id.main_start_btn:
			choice = "开始游戏";
			Intent intent = new Intent(this, GameActivity.class);
			startActivity(intent);
			break;
		case R.id.main_rank_btn:
			choice = "排行";
			break;
		case R.id.main_setting_btn:
			choice = "设置";
			break;
		case R.id.main_help_btn:
			choice = "帮助";
			break;
		case R.id.main_exit_btn:
			choice = "退出";
			break;
		default:
			break;
		}
		Toast.makeText(this, choice, Toast.LENGTH_SHORT).show();
	}
	
}
