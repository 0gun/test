package com.test.webplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class InputURL extends Activity implements OnClickListener{

	private String StartUrl;
	private EditText mInput;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inputurl);
		
		mInput = (EditText)findViewById(R.id.editUrl);
		Button mGo = (Button)findViewById(R.id.goUrl);
		mInput.setText("http://192.168.10.200:8430/demo/demo.html");
		mGo.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v == null)
			return;
		
		int id = v.getId();
		switch (id) {
		case R.id.goUrl:
			Intent intent = new Intent(this, WebPlayerActivity.class);
			StartUrl = mInput.getText().toString().trim();
			Log.d(">>>>> ::: ", StartUrl.toString());
			intent.putExtra("StartUrl", StartUrl);
			startActivity(intent);
			finish();
			break;
		}
		
	}
	
}
