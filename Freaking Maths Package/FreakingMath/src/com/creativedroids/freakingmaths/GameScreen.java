package com.creativedroids.freakingmaths;

import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class GameScreen extends Activity implements OnClickListener {

	int highScore = 0;
	AtomicBoolean gameEnded;
	private RelativeLayout llMain;
	boolean resultOfGame;
	private TextView tvfirstDigit, tvoperator, tvsecondDigit, tvresult,
			tvhighscore;
	private Button bright, bwrong;
	private ImageView ivtimerBar;
	private int width;
	private CustomAnimationBar barAnimation;
	private Intent intent;
	private Context context = this;
	private InterstitialAd interstitialAd;
	private AdView adView;
	private int heightOfTimer;
	private boolean isPaused = false;
	private int adcounter = 5;
	private int gamePlayNumber;
	private SharedPreferences pref;
	private SharedPreferences.Editor edit;
	private final static int inititialTimer = 1000;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamescreen);
		gameEnded = new AtomicBoolean(false);
		initialiseUiElements();

		pref = getSharedPreferences("SETTINGS", MODE_PRIVATE);
		edit = pref.edit();

		gamePlayNumber = pref.getInt("GAMEPLAYNUMBER", 1);

		ivtimerBar.setBackgroundColor(getResources()
				.getColor(R.color.timerBack));
		llMain.setBackgroundColor(Color.parseColor(DataHandler
				.randomColorGetter()));
		tvhighscore.setText("0");
		width = DataHandler.getDeviceWidth();

		heightOfTimer = getResources().getDimensionPixelSize(R.dimen.padding);

		barAnimation = new CustomAnimationBar(ivtimerBar, (float) width,
				heightOfTimer, 0.0F, heightOfTimer, inititialTimer);
		barAnimation.setAnimationListener(new AnimationListener() {
			public void onAnimationEnd(Animation anim) {
				if (ivtimerBar.getWidth() == 0) {
					endGame();
				}

			}

			public void onAnimationRepeat(Animation anim) {
			}

			public void onAnimationStart(Animation anim) {
			}
		});
		barAnimation.cancel();
		ivtimerBar.setAnimation(barAnimation);
		bright.setOnClickListener(this);
		bwrong.setOnClickListener(this);
		DataHandler.replay();

		initialiseGame();
		loadAds();
		loadinterstitial();
	}

	private void loadAds() {
		adView.loadAd(StartScreen.adRequest);
	}
	
	
	private void initialiseUiElements() {
		tvfirstDigit = (TextView) findViewById(R.id.tvfirstDigit);
		tvsecondDigit = (TextView) findViewById(R.id.tvsecondDigit);
		tvresult = (TextView) findViewById(R.id.tvresult);
		tvoperator = (TextView) findViewById(R.id.tvoperator);
		tvhighscore = (TextView) findViewById(R.id.tvhighscore);
		llMain = (RelativeLayout) findViewById(R.id.llmain);
		ivtimerBar = (ImageView) findViewById(R.id.ivtimerBar);
		bright = (Button) findViewById(R.id.bright);
		bwrong = (Button) findViewById(R.id.bwrong);
		adView = (AdView) findViewById(R.id.adView);

		tvfirstDigit.setTypeface(DataHandler.getFont(context));
		tvsecondDigit.setTypeface(DataHandler.getFont(context));
		tvresult.setTypeface(DataHandler.getFont(context));
		tvoperator.setTypeface(DataHandler.getFont(context));
		tvhighscore.setTypeface(DataHandler.getFont(context));
		((TextView) findViewById(R.id.tvequal)).setTypeface(DataHandler
				.getFont(context));
	}

	private void initialiseGame() {
		DataVars dataVars = DataHandler.extensiveRandomNumber();
		resultOfGame = dataVars.isCorrect;
		tvfirstDigit.setText(dataVars.one + "");
		tvsecondDigit.setText(dataVars.two + "");
		tvresult.setText(dataVars.resources + "");
		if (dataVars.isAdd) {
			tvoperator.setText("+");
		} else {
			tvoperator.setText("-");
		}

		llMain.setBackgroundColor(Color.parseColor(DataHandler
				.randomColorGetter()));
	}

	private void loadinterstitial() {
		interstitialAd = new InterstitialAd(context);
		interstitialAd.setAdUnitId(getResources().getString(
				R.string.interstitial_id));
		AdRequest adRequest = new AdRequest.Builder().addTestDevice(
				AdRequest.DEVICE_ID_EMULATOR).build();
		interstitialAd.loadAd(adRequest);
	}

	private void showInterstitial() {
		if (interstitialAd.isLoaded()) {
			interstitialAd.show();
		}
	}

	private void endGame() {
		gameEnded.set(true);
		StartScreen.wrongSound();
		if (highScore > PrefClass.getMaxScore()) {
			PrefClass.setHighScore(highScore);
		}
		DataHandler.replay();
		intent = new Intent(context, GameOverActivity.class);
		intent.putExtra("SCORE", highScore);
		startActivity(intent);
		finish();
		if (gamePlayNumber >= adcounter){
			showInterstitial();
			edit.putInt("GAMEPLAYNUMBER", 1);
			edit.commit();
		}else{
			gamePlayNumber++;
			edit.putInt("GAMEPLAYNUMBER", gamePlayNumber);
			edit.commit();
		}
			
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.bright:

			if (!barAnimation.hasEnded()) {
				barAnimation.cancel();
			}

			if (resultOfGame) {
				StartScreen.correctSound();
				ivtimerBar.getLayoutParams().width = width;
				initialiseGame();
				barAnimation.start();
				GameScreen gameScreen = GameScreen.this;
				++gameScreen.highScore;
				tvhighscore.setText(highScore + "");
			} else {
				endGame();
			}
			break;
		case R.id.bwrong:
			if (!gameEnded.get()) {
				if (!barAnimation.hasEnded()) {
					barAnimation.cancel();
				}

				if (!resultOfGame) {
					StartScreen.correctSound();
					ivtimerBar.getLayoutParams().width = width;
					initialiseGame();
					barAnimation.start();
					GameScreen gameScreen = GameScreen.this;
					++gameScreen.highScore;
					tvhighscore.setText(highScore + "");
				} else {
					endGame();
				}
			}
			break;
		}

	}

	@Override
	public void onDestroy() {
		if (adView != null)
			adView.destroy();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		isPaused = true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isPaused) {
			isPaused = false;
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					barAnimation.setAnimationListener(new AnimationListener() {
						@Override
						public void onAnimationEnd(Animation anim) {
							if (ivtimerBar.getWidth() == 0) {
								endGame();
							}
						}
						@Override
						public void onAnimationRepeat(Animation anim) {
						}
						@Override
						public void onAnimationStart(Animation anim) {
						}
					});
				}
			});
			initialiseGame();
			barAnimation.start();
			GameScreen gameScreen = GameScreen.this;
			ivtimerBar.setAnimation(barAnimation);
		}
	}

}
