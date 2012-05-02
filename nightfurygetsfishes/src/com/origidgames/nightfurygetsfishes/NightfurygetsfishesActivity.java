/**************************************************************************************************************
 * Copyright (c) 2012 ORIGID GAMES STUDIO. 
 *************************************************************************************************************/

package com.origidgames.nightfurygetsfishes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class NightfurygetsfishesActivity extends Activity {
    /** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        PublicResource.LoadResource(getBaseContext());
        startActivity(new Intent("com.origidgames.nightfuryUI7"));

    }
    protected void onDestroy() {
    	super.onDestroy();
    	PublicResource.closeDataBase();
    }
    
}