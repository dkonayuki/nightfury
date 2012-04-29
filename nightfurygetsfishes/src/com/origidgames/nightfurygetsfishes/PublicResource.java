package com.origidgames.nightfurygetsfishes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class PublicResource {
	private static Animation 	anim_InFromLeft,
	anim_InFromRight,
	anim_InFromTop,
	anim_InFromBot,
	anim_OutToLeft,
	anim_OutToRight,
	anim_OutToTop,
	anim_OutToBot,
	anim_FadeIn,
	anim_FadeOut,
	anim_BotToTop;
	private static PausableRotate anim_Rotate;
	private static Bitmap bmpStar = null;
	private static SoundPool soundPool;
	private static int answer_correct,answer_wrong,clock,page_flip,lose,pause;
	private static DBAdapter db;
	private static final String sPrefName = "Preferences";
	private static final String sAudio = "Audio";
	private static final String sHighScore = "Highscore";
	private static final String sGameMode = "Gamemode";

    public static DBAdapter getDataBase() {
    	return db;
    }
    
    public static void closeDataBase() {
		db.close();
	}
	
	public static void LoadResource(Context ct) {
		anim_InFromLeft = AnimationUtils.loadAnimation(ct, R.anim.infromleft);
		anim_InFromRight = AnimationUtils.loadAnimation(ct, R.anim.infromright);
		anim_InFromTop= AnimationUtils.loadAnimation(ct, R.anim.infromtop);
		anim_InFromBot= AnimationUtils.loadAnimation(ct, R.anim.infrombot);
		anim_OutToLeft = AnimationUtils.loadAnimation(ct, R.anim.outtoleft);
		anim_OutToRight = AnimationUtils.loadAnimation(ct, R.anim.outtoright);
		anim_OutToBot = AnimationUtils.loadAnimation(ct, R.anim.outtobot);
		anim_OutToTop= AnimationUtils.loadAnimation(ct, R.anim.outtotop);
		anim_FadeIn = AnimationUtils.loadAnimation(ct, R.anim.fadein);
		anim_FadeOut = AnimationUtils.loadAnimation(ct, R.anim.fadeout);
		anim_Rotate = new PausableRotate(0,360,Animation.RELATIVE_TO_SELF,(float)0.5,
				Animation.RELATIVE_TO_SELF,(float)0.5);
		anim_Rotate.setDuration(90000);
		anim_BotToTop = AnimationUtils.loadAnimation(ct, R.anim.bottotop);
		bmpStar = BitmapFactory.decodeResource(ct.getResources(), R.drawable.img_checkpoint);
		soundPool = new SoundPool(20,AudioManager.STREAM_MUSIC,0);
		
		AssetFileDescriptor descriptor = null;
		try {
			AssetManager assetManager = ct.getAssets();
			descriptor = assetManager.openFd("answer_correct.mp3");
			answer_correct = soundPool.load(descriptor, 1);
			descriptor = assetManager.openFd("answer_wrong.mp3");
			answer_wrong = soundPool.load(descriptor, 1);
			descriptor = assetManager.openFd("clock.wav");
			clock = soundPool.load(descriptor, 1);
			descriptor = assetManager.openFd("page_flip.mp3");
			page_flip = soundPool.load(descriptor, 1);
			descriptor = assetManager.openFd("lose.mp3");
			lose = soundPool.load(descriptor, 1);
			descriptor = assetManager.openFd("pause.mp3");
			pause = soundPool.load(descriptor, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		db = new DBAdapter(ct);
        try {
        	String destPath = "/data/data/" + ct.getPackageName() + "/databases/";
        	File dir = new File(destPath);	
        	//File f = new File(destPath + "MyDB");
        	if (!dir.exists()) {
        		dir.mkdir();
        	}	
        	db.CopyDB(ct.getAssets().open("mydb"),new FileOutputStream(destPath + "MyDB"));
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
	}
	public static Animation BotToTop() { return anim_BotToTop; }
	public static Animation Rotate() { return anim_Rotate; }
	public static void pauseRotate() {
		anim_Rotate.pause();
	}
	public static void resumeRotate() {
		anim_Rotate.resume();
	}
	public static Animation FadeOut() { return anim_FadeOut;}
	public static Animation FadeIn() { return anim_FadeIn; }
	public static Animation InFromLeft() { return anim_InFromLeft; }
	public static Animation InFromBot() { return anim_InFromBot; }
	public static Bitmap Star(){return bmpStar;}
	
	public static void playSoundAnsRight() {
		soundPool.play(answer_correct, 1, 1, 0, 0, 1);
	}
	
	public static void playSoundAnsWrong() {
		soundPool.play(answer_wrong, 1, 1, 0, 0, 1);
	}
	
	public static void playSoundClock() {
		soundPool.play(clock, 1, 1, 0, 0, 1);
	}
	
	public static void playSoundChange() {
		soundPool.play(page_flip, 1, 1, 0, 0, 1);
	}
	
	public static void playSoundLose() {
		soundPool.play(lose, 1, 1, 0, 0, 1);
	}
	
	public static void playSoundPause() {
		soundPool.play(pause, 1, 1, 0, 0, 1);
	}
	
	public static GameMode getHighscore(Context ct) {
		SharedPreferences m_Pref = ct.getSharedPreferences(sPrefName, Context.MODE_PRIVATE);
		switch (m_Pref.getInt(sHighScore, 1))
		{
		case 2: return GameMode.NORMAL;
		case 3: return GameMode.HARD;
		}
		return GameMode.EASY;
	}
	
	public static void setHighscore(Context ct, GameMode gm) {
		SharedPreferences m_Pref = ct.getSharedPreferences(sPrefName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = m_Pref.edit();
		switch (gm) {
		case EASY: 
			editor.putInt(sHighScore, 1); 
			break;
		case NORMAL: 
			editor.putInt(sHighScore, 2);
			break;
		case HARD: 
			editor.putInt(sHighScore, 3);
			break;
		}
		editor.commit();
	}
	
	public static Boolean getAudioPref(Context ct) {
		SharedPreferences m_Pref = ct.getSharedPreferences(sPrefName, Context.MODE_PRIVATE);
		Boolean b = m_Pref.getBoolean(sAudio, true);
		return b;
	}
	
	public static void setAudioPref(Context ct,Boolean b) {
		SharedPreferences m_Pref = ct.getSharedPreferences(sPrefName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = m_Pref.edit();
		if (b) editor.putBoolean(sAudio, true); else editor.putBoolean("Audio", false);
		editor.commit();
	}
	
	public static void setGameMode(Context ct, GameMode gm) {
		SharedPreferences m_Pref = ct.getSharedPreferences(sPrefName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = m_Pref.edit();
		switch (gm) {
		case EASY: 
			editor.putInt(sGameMode, 1); 
			break;
		case NORMAL: 
			editor.putInt(sGameMode, 2);
			break;
		case HARD: 
			editor.putInt(sGameMode, 3);
			break;
		}
		editor.commit();
	}
	
	public static GameMode getGameMode(Context ct) {
		SharedPreferences m_Pref = ct.getSharedPreferences(sPrefName, Context.MODE_PRIVATE);
		switch (m_Pref.getInt(sGameMode, 1))
		{
		case 2: return GameMode.NORMAL;
		case 3: return GameMode.HARD;
		}
		return GameMode.EASY;
	}
	
	
}
