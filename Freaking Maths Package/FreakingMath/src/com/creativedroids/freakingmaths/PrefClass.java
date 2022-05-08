package com.creativedroids.freakingmaths;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefClass {

	public static final int highScore = 0;

	public static int getMaxScore() {
		return getSharedPreferences().getInt("HIGHSCORE", 0);
	}

	public static SharedPreferences getSharedPreferences() {
		return PreferenceManager.getDefaultSharedPreferences(StartScreen
				.getAppContext());
	}

	public static SharedPreferences getSharedPreferencesWithContext(
			Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	public static void setHighScore(int score) {
		getSharedPreferences().edit().putInt("HIGHSCORE", score).commit();
	}
	
	public static boolean getSound(){
		return getSharedPreferences().getBoolean("SOUND", true);
	}
	
	public static void setSound(boolean sound){
		getSharedPreferences().edit().putBoolean("SOUND", sound).commit();
	}
}
