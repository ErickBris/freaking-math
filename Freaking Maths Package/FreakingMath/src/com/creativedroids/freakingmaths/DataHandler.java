package com.creativedroids.freakingmaths;

import java.util.Random;

import android.content.Context;
import android.graphics.Typeface;

public class DataHandler {

	private static int level = 0;
	private static int levelAdd = 3;
	private static int currentMaxFirstDigit = 0;
	private static int currentMaxSecondDigit = 3;
	private static int maxFirstDigit = 100;
	private static int maxSecondDigit = 20;
	private static int firstDigitIncrease = 1;
	private static int SecondDigitIncrease = 4;
	private static int event = 0;
	private static int deviceHeight = 480;
	private static int deviceWidth = 320;
	private static Random rand = new Random();

	public static int getDeviceHeight() {
		return deviceHeight;
	}
	
	public static Typeface getFont(Context context) {
		return Typeface.createFromAsset(context.getAssets(), "walkway.ttf");
	}

	public static void setDeviceHeight(int deviceHeight) {
		DataHandler.deviceHeight = deviceHeight;
	}

	public static int getDeviceWidth() {
		return deviceWidth;
	}

	public static void setDeviceWidth(int deviceWidth) {
		DataHandler.deviceWidth = deviceWidth;
	}

	public static String randomColorGetter() {
		String[] listOfColorCodes = "009900,009933,009966,009999,0099CC,0099FF,00CC00,00CC33,00CC66,00CC99,00CCCC,00CCFF,00FF00,00FF33,00FF66,00FF99,330000,330033,330066,330099,3300CC,3300FF,333300,333333,333366,333399,3333CC,3333FF,336600,336633,336666,336699,3366CC,3366FF,339900,339933,339966,339999,3399CC,3399FF,33CC00,33CC33,33CC66,33CC99,33CCCC,33CCFF,33FF00,33FF33,33FF66,33FF99,660000,660033,660066,660099,6600CC,6600FF,663300,663333,663366,663399,6633CC,6633FF,666600,666633,666699,6666CC,6666FF,666666,669900,669933,669966,669999,6699CC,6699FF,66CC00,66CC33,66CC66,66CC99,66CCCC,66CCFF,66FF00,66FF33,66FF66,66FF99,990000,990033,990066,990099,9900CC,9900FF,993300,993333,993366,993399,9933CC,9933FF,996600,996633,996666,996699,9966CC,9966FF,999900,999933,999966,999999,9999CC,9999FF,99CC00,99CC33,99CC66,99CC99,99CCCC,99CCFF,99FF00,CC0000,CC0033,CC0066,CC0099,CC00CC,CC00FF,CC3300,CC3333,CC3366,CC3399,CC33CC,CC33FF,CC6600,CC6633,CC6666,CC6699,CC66CC,CC66FF,CC9900,CC9933,CC9966,CC9999,CC99CC,CC99FF,CCCC00,CCCC33,CCCC66,CCCC99,CCCCCC,CCFF00,FF0000,FF0033,FF0066,FF0099,FF00CC,FF00FF,FF3300,FF3333,FF3366,FF3399,FF33CC,FF33FF,FF6600,FF6633,FF6666,FF6699,FF66CC,FF66FF,FF9900,FF9933,FF9966,FF9999,FF99CC,FF99FF,FFCC00,FFCC33,FFCC66,FFCC99,FFFF00"
				.split(",");
		return "#" + listOfColorCodes[randInt(0, -1 + listOfColorCodes.length)];
	}

	public static int randInt(int start, int end) {
		return start + (new Random()).nextInt(1 + (end - start));
	}

	public static boolean randomBytes() {
		return randomNumber(2) == 0;
	}

	public static DataVars extensiveRandomNumber() {
		++event;
		if (event % levelAdd == 0) {
			currentMaxSecondDigit += SecondDigitIncrease;
			currentMaxFirstDigit += firstDigitIncrease;
			if (currentMaxSecondDigit > maxSecondDigit) {
				currentMaxSecondDigit = maxSecondDigit;
			}

			if (currentMaxFirstDigit > maxFirstDigit) {
				currentMaxFirstDigit = maxFirstDigit;
			}
		}

		int rand1 = 1 + randomNumber(currentMaxSecondDigit);
		int rand2 = 1 + randomNumber(currentMaxSecondDigit);
		if (currentMaxSecondDigit > 15) {
			if (rand1 != 1) {
				rand1 += randomNumber(currentMaxFirstDigit);
			}

			if (rand2 != 1) {
				rand2 += randomNumber(currentMaxFirstDigit);
			}
		}

		int total = rand1 + rand2;
		boolean isRand = randomBytes();
		boolean isRestRand = randomBytes();
		if (!isRand) {
			if (randomBytes() && total > 10) {
				if (isRestRand) {
					total += 10;
				} else {
					total -= 10;
				}
			} else {
				int randArr = randomNumber(5);
				if (randArr == 0) {
					++randArr;
				}

				if (total - randArr <= 0) {
					total += randArr;
				} else if (isRestRand) {
					total += randArr;
				} else {
					total -= randArr;
				}
			}
		}

		boolean isTrueThere = randomBytes();
		if (!isTrueThere) {
			int restNum = rand1;
			rand1 = total;
			total = restNum;
		}

		return new DataVars(rand1, rand2, total, isRand, isTrueThere);
	}

	public static int randomNumber(int limit) {
		int curLevel = level;
		level = curLevel + 1;
		if (curLevel > 8) {
			rand = new Random();
		}
		return rand.nextInt(limit);
	}

	public static void replay() {
		level = 0;
		event = 0;
		SecondDigitIncrease = 3;
		currentMaxFirstDigit = 0;
		currentMaxSecondDigit = 3;
		levelAdd = 3;
		maxFirstDigit = 100;
		maxSecondDigit = 20;
		firstDigitIncrease = 1;
	}
}
