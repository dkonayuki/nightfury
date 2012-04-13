package com.origidgames.nightfurygetsfishes;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	}
	public static Animation Rotate() { return anim_Rotate; }
	public static Animation FadeIn() { return anim_FadeIn; }
	public static Animation InFromLeft() { return anim_InFromLeft; }
	public static Animation InFromBot() { return anim_InFromBot; }
	public static Bitmap Star(){return bmpStar;}
	
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
