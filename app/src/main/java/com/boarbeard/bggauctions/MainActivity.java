package com.boarbeard.bggauctions;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.boarbeard.bggauctions.io.Adapter;
import com.boarbeard.bggauctions.model.GeekListsResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Observable<GeekListsResponse> observable = Adapter.createForXml().geekLists();
        observable.subscribeOn(Schedulers.io())
                .map(response -> response.getGeekListEntries());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (disposable != null) {
            disposable.dispose();
        }
    }

}
