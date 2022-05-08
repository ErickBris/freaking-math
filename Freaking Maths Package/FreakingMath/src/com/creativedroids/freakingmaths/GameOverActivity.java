package com.creativedroids.freakingmaths;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

public class GameOverActivity extends Activity implements OnClickListener {

	private Button bplay, bmenu, bshare;
	private Intent intent;
	private TextView tvlastScore, tvtopscore;
	private Animation left_to_right, rightToLeft, fromBottom;
	private int score;
	private Context context = this;
	private AdView adView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gameoverscreen);
		score = getIntent().getIntExtra("SCORE", 0);
		tvlastScore = (TextView) findViewById(R.id.last_score);
		tvtopscore = (TextView) findViewById(R.id.top_score);
		bplay = (Button) findViewById(R.id.bplay);
		bmenu = (Button) findViewById(R.id.bmenu);
		bshare = (Button) findViewById(R.id.bshare);
		tvlastScore.setText(score + "");
		tvtopscore.setText(PrefClass.getMaxScore() + "");
		bplay.setOnClickListener(this);
		bmenu.setOnClickListener(this);
		bshare.setOnClickListener(this);
		adView = (AdView) findViewById(R.id.adView);
		adView.loadAd(StartScreen.adRequest);

		left_to_right = AnimationUtils.loadAnimation(this,
				R.anim.slide_left_to_right);
		rightToLeft = AnimationUtils.loadAnimation(this,
				R.anim.slide_right_to_left);
		fromBottom = AnimationUtils.loadAnimation(context, R.anim.slide_bottom);

		tvlastScore.setTypeface(DataHandler.getFont(context));
		tvtopscore.setTypeface(DataHandler.getFont(context));

		((TextView) findViewById(R.id.tvlabellast)).setTypeface(DataHandler
				.getFont(context));
		((TextView) findViewById(R.id.tvgameover)).setTypeface(DataHandler
				.getFont(context));
		((TextView) findViewById(R.id.tvlabeltop)).setTypeface(DataHandler
				.getFont(context));

		((TextView) findViewById(R.id.tvgameover)).setAnimation(left_to_right);
		bplay.setAnimation(left_to_right);
		bmenu.setAnimation(rightToLeft);
		bshare.setAnimation(fromBottom);

	}

	private void shareIt() {
		String text = String.format(
				getResources().getString(R.string.shareSubject),
				PrefClass.getMaxScore());
		String appPackageName = getPackageName();
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain");
		i.putExtra(Intent.EXTRA_TEXT,
				"http://play.google.com/store/apps/details?id="
						+ appPackageName);
		i.putExtra(Intent.EXTRA_SUBJECT, text);
		startActivity(Intent.createChooser(i, "Share"));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bplay:
			StartScreen.buttonClickSound();
			intent = new Intent(GameOverActivity.this, GameScreen.class);
			startActivity(intent);
			finish();
			break;
		case R.id.bmenu:
			StartScreen.buttonClickSound();
			finish();
			break;
		case R.id.bshare:
			shareIt();
			break;
		}

	}

	@Override
	public void onDestroy() {
		if (adView != null)
			adView.destroy();
		super.onDestroy();
	}
}
