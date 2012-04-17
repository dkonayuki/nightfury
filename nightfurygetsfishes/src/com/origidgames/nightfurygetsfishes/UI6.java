package com.origidgames.nightfurygetsfishes;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

public class UI6 extends Activity{
	private Cursor c;
	TextView name[] = new TextView[6];
	TextView time[] = new TextView[6];
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.layout_ui6);
	        
	        name[1] = (TextView) findViewById(R.id.name1);
	        name[2] = (TextView) findViewById(R.id.name2);
	        name[3] = (TextView) findViewById(R.id.name3);
	        name[4] = (TextView) findViewById(R.id.name4);
	        name[5] = (TextView) findViewById(R.id.name5);
	        
	        time[1] = (TextView) findViewById(R.id.time1);
	        time[2] = (TextView) findViewById(R.id.time2);
	        time[3] = (TextView) findViewById(R.id.time3);
	        time[4] = (TextView) findViewById(R.id.time4);
	        time[5] = (TextView) findViewById(R.id.time5);

	        GameMode gm = PublicResource.getHighscore(getBaseContext());
	        switch (gm) {
	        case EASY: 
	        	c = PublicResource.getDataBase().getAllEasy();
	        	break;
	        case NORMAL: 
	        	c = PublicResource.getDataBase().getAllNormal();
	        	break;
	        case HARD:
	        	c = PublicResource.getDataBase().getAllHard();
	        }
	        
	        c.moveToFirst();
	        for (int i = 1; i <= 5; i++)
	        {
	        	name[i].setText(c.getString(1));
	        	time[i].setText(c.getString(2));
	        	c.moveToNext();
	        }
	    }
}
