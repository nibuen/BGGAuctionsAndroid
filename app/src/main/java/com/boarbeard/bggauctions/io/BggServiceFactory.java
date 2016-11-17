package com.boarbeard.bggauctions.io;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class BggServiceFactory {
	public static BggService createForXml() {
		Retrofit.Builder builder = createBuilderWithoutConverterFactory(null);
		builder.addConverterFactory(SimpleXmlConverterFactory.createNonStrict());
		builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
		return builder.build().create(BggService.class);
	}

	public static BggService createForXmlWithAuth(Context context) {
		Retrofit.Builder builder = createBuilderWithoutConverterFactory(context);
		builder.addConverterFactory(SimpleXmlConverterFactory.createNonStrict());
		return builder.build().create(BggService.class);
	}

	public static BggService createForJson() {
		Retrofit.Builder builder = createBuilderWithoutConverterFactory(null);
		builder.addConverterFactory(GsonConverterFactory.create());
		builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
		return builder.build().create(BggService.class);
	}

	private static Retrofit.Builder createBuilderWithoutConverterFactory(Context context) {
		okhttp3.OkHttpClient httpClient;
		httpClient = HttpUtils.getHttpClient();

		return new Retrofit.Builder()
			.baseUrl("https://www.boardgamegeek.com/")
			.client(httpClient);
	}
}