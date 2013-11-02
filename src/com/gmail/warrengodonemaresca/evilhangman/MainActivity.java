package com.gmail.warrengodonemaresca.evilhangman;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

	public static final String PREFS_NAME = "MyPrefsFile";

	private Button startButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Hides the options fragment
		((OptionsFragment)this.getFragmentManager().
				findFragmentById(R.id.fragment_options)).getView().
				setVisibility(View.GONE);
		
		startButton = (Button)this.findViewById(R.id.button_start);
		startButton.setOnClickListener(onClickListener);//TODO change to this
	}

	private OnClickListener onClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			if(((Button)v).getId() == startButton.getId()){
				//TODO startDialog();

				ImageView img = (ImageView)
						(((View)v.getParent()).findViewById(R.id.imageView1));
				
				View optionsView = ((OptionsFragment)MainActivity.this.
						getFragmentManager().
						findFragmentById(R.id.fragment_options)).getView();

				if(optionsView.getVisibility() == View.GONE){				
					if(!getResources().getConfiguration().isLayoutSizeAtLeast(
							Configuration.SCREENLAYOUT_SIZE_LARGE)){
						
						img.setVisibility(View.GONE);
					}
					
					optionsView.setVisibility(View.VISIBLE);
				} else {
					optionsView.setVisibility(View.GONE);
					
					if(img.getVisibility() == View.GONE){
						img.setVisibility(View.VISIBLE);
					}
				}
				//End startButton listener
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == resultCode){

		}
	}
}