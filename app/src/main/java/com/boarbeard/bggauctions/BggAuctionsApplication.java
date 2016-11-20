package com.boarbeard.bggauctions;

import android.app.Application;
import android.os.StrictMode;

import timber.log.Timber;

public class BggAuctionsApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		if (BuildConfig.DEBUG) {
			Timber.plant(new Timber.DebugTree());
			enableStrictMode();
		} else {
			Timber.plant(new Timber.DebugTree());
		}
	}

	private void enableStrictMode() {
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
			.detectAll()
			.penaltyLog()
			.build());
		StrictMode.ThreadPolicy.Builder builder = new StrictMode.ThreadPolicy.Builder()
			.detectAll()
			.penaltyLog();
		builder.penaltyFlashScreen();
		StrictMode.setThreadPolicy(builder.build());
	}
}