package com.boarbeard.bggauctions;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.boarbeard.bggauctions.io.Adapter;
import com.boarbeard.bggauctions.model.GeekListEntry;
import com.boarbeard.bggauctions.model.GeekListsResponse;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Timber.v("Trying out a request.");
        Observable<GeekListsResponse> observable = Adapter.createForXml().geekLists();

        Single<List<GeekListEntry>> listSingle = observable.subscribeOn(Schedulers.io())
                .map(new Function<GeekListsResponse, List<GeekListEntry>>() {
                    @Override
                    public List<GeekListEntry> apply(GeekListsResponse geekListsResponse) throws Exception {
                        return geekListsResponse.getGeekListEntries();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .firstOrError();

        Timber.v(listSingle.toString());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (disposable != null) {
            disposable.dispose();
        }
    }

}
