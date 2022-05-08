package com.creativedroids.freakingmaths;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;
import com.google.example.games.basegameutils.BaseGameActivity;

public class StartScreen extends BaseGameActivity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	public static Context context;
	public static int[] soundContainer;
	public static SoundPool sp;
	private Animation left_to_right, frombottom;
	private Button brate, bstart, bleaderboard, bsound;
	private GoogleApiClient googleApiClient;
	private static String leaderBoard = "";
	public static AdRequest adRequest = new AdRequest.Builder().addTestDevice(
			AdRequest.DEVICE_ID_EMULATOR).build();

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startscreen);

		context = this.getApplicationContext();
		AppRater.app_launched(this);
		sp = new SoundPool(7, AudioManager.STREAM_MUSIC, 0);
		soundContainer = new int[4];
		soundContainer[0] = sp.load(context, R.raw.buttonclick, 1);
		soundContainer[1] = sp.load(context, R.raw.correct, 1);
		soundContainer[2] = sp.load(context, R.raw.wrong, 1);

		left_to_right = AnimationUtils.loadAnimation(this,
				R.anim.slide_left_to_right);
		frombottom = AnimationUtils.loadAnimation(this, R.anim.slide_bottom);

		brate = (Button) findViewById(R.id.brate);
		bstart = (Button) findViewById(R.id.bstart);
		bleaderboard = (Button) findViewById(R.id.bleaderboard);
		bsound = (Button) findViewById(R.id.bsound);

		brate.setAnimation(frombottom);
		bleaderboard.setAnimation(frombottom);
		bstart.setAnimation(left_to_right);
		bsound.setAnimation(left_to_right);

		((TextView) findViewById(R.id.tvname)).setAnimation(left_to_right);

		leaderBoard = getResources().getString(R.string.leaderboard_id);

		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		DataHandler.setDeviceHeight(dm.heightPixels);
		DataHandler.setDeviceWidth(dm.widthPixels);

		GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this);
		builder.addApi(Games.API).addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN);
		googleApiClient = builder.build();

		if (!isSignedIn())
			beginUserInitiatedSignIn();
		googleApiClient.connect();

		((TextView) findViewById(R.id.tvname)).setTypeface(DataHandler
				.getFont(context));

		setSound();
	}

	private void submitScoreToLeaderboard() {
		if (isSignedIn()) {

			Games.Leaderboards.submitScore(getApiClient(), leaderBoard,
					PrefClass.getMaxScore());

		}
	}
	
	@SuppressWarnings("deprecation")
	private void setSound(){
	if (!PrefClass.getSound()) {
			bsound.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.sound_off));
		} else {
			bsound.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.sound_on));
		}
	}

	@SuppressWarnings("deprecation")
	private void toggleSound() {
		if (PrefClass.getSound()) {
			bsound.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.sound_off));
			PrefClass.setSound(false);
		} else {
			bsound.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.sound_on));
			PrefClass.setSound(true);
		}
	}

	public static Context getAppContext() {
		return context;
	}

	public static void correctSound() {
		if (PrefClass.getSound())
			sp.play(soundContainer[1], 100, 100, 1, 0, 1);
	}

	public static void wrongSound() {
		if (PrefClass.getSound())
			sp.play(soundContainer[2], 100, 100, 1, 0, 1);
	}

	public static void buttonClickSound() {
		if (PrefClass.getSound())
			sp.play(soundContainer[0], 100, 100, 1, 0, 1);
	}

	
	public void clicked(View v) {
		switch (v.getId()) {
		case R.id.bstart:
			buttonClickSound();
			startActivity(new Intent(context, GameScreen.class));
			break;
		case R.id.bleaderboard:
			if (isSignedIn() && googleApiClient.isConnected()) {
				submitScoreToLeaderboard();
				startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
						getApiClient(), leaderBoard), 0);
			} else {
				beginUserInitiatedSignIn();
			}
			break;
		case R.id.brate:
			try {
				startActivity(new Intent(Intent.ACTION_VIEW,
						Uri.parse("market://details?id=" + getPackageName())));
			} catch (android.content.ActivityNotFoundException excep) {
				startActivity(new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("http:\\play.google.com/store/apps/details?id="
								+ getPackageName())));
			}
			break;
		case R.id.bsound:
			toggleSound();
			break;
		}
	}

	@Override
	public void onSignInFailed() {

	}

	@Override
	public void onSignInSucceeded() {
		googleApiClient.connect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {

	}

	@Override
	public void onConnected(Bundle arg0) {
		submitScoreToLeaderboard();
	}

	@Override
	public void onDisconnected() {

	}
}
