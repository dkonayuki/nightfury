package com.origidgames.nightfurygetsfishes;

import java.io.IOException;

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
	anim_Rotate;
	private static final String PrefName = "Preferences";
	private static Bitmap bmpStar = null;
	private static SoundPool soundPool;
	private static int answer_correct,answer_wrong,clock,page_flip;
	
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
		anim_Rotate = AnimationUtils.loadAnimation(ct, R.anim.rotate);
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	public static Animation Rotate() { return anim_Rotate; }
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
	
	public static Boolean getAudioPref(Context ct) {
		SharedPreferences m_Pref = ct.getSharedPreferences(PrefName, Context.MODE_PRIVATE);
		Boolean b = m_Pref.getBoolean("Audio", true);
		return b;
	}
	
	public static void setAudioPref(Context ct,Boolean b) {
		SharedPreferences m_Pref = ct.getSharedPreferences(PrefName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = m_Pref.edit();
		if (b) editor.putBoolean("Audio", true); else editor.putBoolean("Audio", false);
		editor.commit();
	}
}
