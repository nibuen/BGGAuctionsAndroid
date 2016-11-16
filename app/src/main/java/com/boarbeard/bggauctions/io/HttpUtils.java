package com.boarbeard.bggauctions.io;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;

public class HttpUtils {
	private static final int HTTP_REQUEST_TIMEOUT_SEC = 15;

	private HttpUtils() {
	}

	/**
	 * Encodes {@code s} using UTF-8 using the format required by {@code application/x-www-form-urlencoded} MIME content type.
	 */
	public static String encode(String s) {
		return Uri.encode(s, "UTF-8");
	}

	public static OkHttpClient getHttpClient() {
		Builder builder = getBuilder();
		final List<Interceptor> interceptors = builder.interceptors();
		interceptors.add(new RetryInterceptor());
		//addLoggingInterceptor(builder);
		return builder.build();
	}

	@NonNull
	private static Builder getBuilder() {
		return new Builder()
			.connectTimeout(HTTP_REQUEST_TIMEOUT_SEC, TimeUnit.SECONDS)
			.readTimeout(HTTP_REQUEST_TIMEOUT_SEC, TimeUnit.SECONDS)
			.writeTimeout(HTTP_REQUEST_TIMEOUT_SEC, TimeUnit.SECONDS);
	}

	/*
	private static void addLoggingInterceptor(Builder builder) {
		if (BuildConfig.DEBUG) {
			HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
			httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
			builder.addInterceptor(httpLoggingInterceptor);
			builder.addNetworkInterceptor(new StethoInterceptor());
		}
	}
	*/

	/**
	 * Ensures the URL has a scheme, setting it to HTTPS if missing.
	 */
	public static String ensureScheme(String url) {
		if (TextUtils.isEmpty(url)) {
			return url;
		}
		if (url.startsWith("//")) {
			return "https:" + url;
		}
		return url;
	}
}