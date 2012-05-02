/**************************************************************************************************************
 * Copyright (c) 2012 ORIGID GAMES STUDIO. 
 *************************************************************************************************************/

package com.origidgames.nightfurygetsfishes;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class UI7 extends Activity {
	private ImageView upgrade;
	private TextView upgradeNumber;
	private Button btn_upgrade;
	private boolean focus;
	private static final String preference[] = {
		"Upgrade2Times","UpgradeSpeed", "Upgrade50" ,"UpgradeAutoAnswer", "UpgradeTime", 
		"UpgradeChange", "UpgradeDecrease",  "UpgradeFish"
	};
	private static final int btnList[] = {
		R.id.btn_2times, R.id.btn_speed, R.id.btn_50, R.id.btn_autoanswer, R.id.btn_change
		, R.id.btn_time, R.id.btn_decrease, R.id.btn_fish
	};
	
	private static final int infoList[] = {
		R.drawable.upgrade_2times, R.drawable.upgrade_speed, R.drawable.upgrade_5050, R.drawable.upgrade_autoanswer
		, R.drawable.upgrade_change, R.drawable.upgrade_time, R.drawable.upgrade_decrease, R.drawable.upgrade_fish
	};
	
	private static final int bgList[] = {
		R.drawable.upgrade_2times_btn, 
		R.drawable.upgrade_speed_btn,
		R.drawable.upgrade_5050_btn,
		R.drawable.upgrade_autoanswer_btn,
		R.drawable.upgrade_change_btn,
		R.drawable.upgrade_time_btn,
		R.drawable.upgrade_decrease_btn,
		R.drawable.upgrade_fish_btn
	};
	private int _currentUpgrade;
	private ImageView _currentImg;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_ui7);
		focus = false;
		
		upgradeNumber = (TextView) findViewById(R.id.upgradeNumber);
		upgradeNumber.setText(String.valueOf(PublicResource.getUpgrade(getBaseContext(), "UpgradeFish")));
		upgradeNumber.setTypeface(PublicResource.getTrajanFont());
		btn_upgrade = (Button) findViewById(R.id.btn_upgrade);
		upgrade = (ImageView) findViewById(R.id.upgrade_text);
		upgrade.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (focus) {
					focus = false;
					upgrade.setVisibility(View.GONE);
					upgrade.startAnimation(PublicResource.FadeOut());
					if (btn_upgrade.getVisibility() == View.VISIBLE )
						btn_upgrade.setVisibility(View.GONE);
				}
			}
			
		});
		
		
		for (int i = 0; i < 8; i++) {
			final ImageView img_upgrade = (ImageView) findViewById(btnList[i]);
			if (PublicResource.getUpgrade(getBaseContext(), preference[i]) > 0) {
				img_upgrade.setBackgroundResource(bgList[i]);
			}
			final int j = i;
			img_upgrade.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						if (!focus) {
							focus = true;
							upgrade.setBackgroundResource(infoList[j]);
							upgrade.setVisibility(View.VISIBLE);
							upgrade.startAnimation(PublicResource.FadeIn());				
							if ((PublicResource.getUpgrade(getBaseContext(), preference[j]) != 1)||(j==7)) {
								_currentUpgrade = j;
								_currentImg = img_upgrade;
								btn_upgrade.startAnimation(PublicResource.FadeIn());
								btn_upgrade.setVisibility(View.VISIBLE);
							}					
					}
				}
			});
		}
		
		btn_upgrade.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (_currentUpgrade != 7) {
					btn_upgrade.startAnimation(PublicResource.FadeOut());
					btn_upgrade.setVisibility(View.GONE);
				}
				_currentImg.setBackgroundResource(bgList[_currentUpgrade]);
				PublicResource.setUpgrade(getBaseContext(), preference[_currentUpgrade],
						PublicResource.getUpgrade(getBaseContext(), preference[_currentUpgrade]) + 1);
				upgradeNumber.setText(String.valueOf(PublicResource.getUpgrade(getBaseContext(), "UpgradeFish")));
			}
			
		});
		
	}
}
