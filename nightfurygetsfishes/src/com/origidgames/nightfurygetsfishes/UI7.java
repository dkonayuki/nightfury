package com.origidgames.nightfurygetsfishes;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class UI7 extends Activity {
	private Button btn_2times,btn_speed,btn_50,btn_autoanswer,btn_change,btn_time,btn_decrease,btn_fish;
	private ImageView upgrade;
	private boolean focus;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_ui7);
		focus = false;
		
		upgrade = (ImageView) findViewById(R.id.upgrade_text);
		upgrade.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (focus) {
					focus = false;
					upgrade.setVisibility(View.GONE);
					upgrade.startAnimation(PublicResource.FadeOut());
				}
			}
			
		});
		
		btn_2times = (Button) findViewById(R.id.btn_2times);
		btn_2times.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!focus) {
					focus = true;
					upgrade.setBackgroundResource(R.drawable.upgrade_2times);
					upgrade.setVisibility(View.VISIBLE);
					upgrade.startAnimation(PublicResource.FadeIn());
				}
			}
		});
		
		btn_speed = (Button) findViewById(R.id.btn_speed);
		btn_speed.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!focus) {
					focus = true;
					upgrade.setBackgroundResource(R.drawable.upgrade_speed);
					upgrade.setVisibility(View.VISIBLE);
					upgrade.startAnimation(PublicResource.FadeIn());
				}
			}
		});
		

		btn_50 = (Button) findViewById(R.id.btn_50);
		btn_50.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!focus) {
					focus = true;
					upgrade.setBackgroundResource(R.drawable.upgrade_5050);
					upgrade.setVisibility(View.VISIBLE);
					upgrade.startAnimation(PublicResource.FadeIn());
				}
			}
		});

		btn_autoanswer = (Button) findViewById(R.id.btn_autoanswer);
		btn_autoanswer.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!focus) {
					focus = true;
					upgrade.setBackgroundResource(R.drawable.upgrade_autoanswer);
					upgrade.setVisibility(View.VISIBLE);
					upgrade.startAnimation(PublicResource.FadeIn());
				}
			}
		});

		btn_change = (Button) findViewById(R.id.btn_change);
		btn_change.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!focus) {
					focus = true;
					upgrade.setBackgroundResource(R.drawable.upgrade_change);
					upgrade.setVisibility(View.VISIBLE);
					upgrade.startAnimation(PublicResource.FadeIn());
				}
			}
		});

		btn_time = (Button) findViewById(R.id.btn_time);
		btn_time.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!focus) {
					focus = true;
					upgrade.setBackgroundResource(R.drawable.upgrade_time);
					upgrade.setVisibility(View.VISIBLE);
					upgrade.startAnimation(PublicResource.FadeIn());
				}
			}
		});

		btn_decrease = (Button) findViewById(R.id.btn_decrease);
		btn_decrease.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!focus) {
					focus = true;
					upgrade.setBackgroundResource(R.drawable.upgrade_decrease);
					upgrade.setVisibility(View.VISIBLE);
					upgrade.startAnimation(PublicResource.FadeIn());
				}
			}
		});
		

		btn_fish = (Button) findViewById(R.id.btn_fish);
		btn_fish.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!focus) {
					focus = true;
					upgrade.setBackgroundResource(R.drawable.upgrade_fish);
					upgrade.setVisibility(View.VISIBLE);
					upgrade.startAnimation(PublicResource.FadeIn());
				}
			}
		});
	}
}