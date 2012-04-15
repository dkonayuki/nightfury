package com.origidgames.nightfurygetsfishes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

public class UI6 extends Activity{
	private Cursor c;
	private DBAdapter db;
	TextView name[] = new TextView[6];
	TextView time[] = new TextView[6];
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.layout_ui6);
	        db = new DBAdapter(this);
	        try {
	        	String destPath = "/data/data/" + getPackageName() + "/databases/MyDB";
	        	File f = new File(destPath);
	        	if (!f.exists()) {
	        		db.CopyDB(getBaseContext().getAssets().open("mydb"),new FileOutputStream(destPath));
	        	}
	        } catch (FileNotFoundException e) {
	        	e.printStackTrace();
	        } catch (IOException e) {
	        	e.printStackTrace();
	        }
	       
	        try {
				db.open();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
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

	        int scoretype = PublicResource.getHighscore(getBaseContext());
	        switch (scoretype) {
	        case 1: 
	        	c = db.getAllEasy();
	        	break;
	        case 2: 
	        	c = db.getAllNormal();
	        	break;
	        case 3:
	        	c = db.getAllHard();
	        }
	        
	        c.moveToFirst();
	        for (int i = 1; i <= 5; i++)
	        {
	        	name[i].setText(c.getString(1));
	        	time[i].setText(c.getString(2));
	        	c.moveToNext();
	        }
	       
	        db.close();
	    }
}
