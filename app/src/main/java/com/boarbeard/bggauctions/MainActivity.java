package com.boarbeard.bggauctions;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.boarbeard.bggauctions.io.BggService;
import com.boarbeard.bggauctions.io.BggServiceFactory;
import com.boarbeard.bggauctions.model.GeekList;
import com.boarbeard.bggauctions.model.GeekListComment;
import com.boarbeard.bggauctions.model.GeekListEntry;
import com.boarbeard.bggauctions.model.GeekListItem;
import com.boarbeard.bggauctions.model.GeekListsResponse;
import com.boarbeard.bggauctions.parser.BidParser;
import com.boarbeard.bggauctions.ui.SampleRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private List<Disposable> disposables = new ArrayList<>();

    private List<GeekListItem> geekListItems = new ArrayList<>();

    private StaggeredGridLayoutManager _sGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        _sGridLayoutManager = new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(_sGridLayoutManager);

        final SampleRecyclerViewAdapter rcAdapter = new SampleRecyclerViewAdapter(
                MainActivity.this, geekListItems);
        recyclerView.setAdapter(rcAdapter);

        final BggService bggServiceJson = BggServiceFactory.createForJson();
        final BggService bggServiceXML = BggServiceFactory.createForXml();

        disposables.add(bggServiceJson.geekListsFrontPage().subscribeOn(Schedulers.io())
                .map(new Function<GeekListsResponse, List<GeekListEntry>>() {
                    @Override
                    public List<GeekListEntry> apply(GeekListsResponse geekListsResponse) throws Exception {
                        return geekListsResponse.getGeekListEntries();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<GeekListEntry>>() {
                    @Override
                    public void accept(List<GeekListEntry> geekListEntries) throws Exception {

                        for (final GeekListEntry geekListEntry : geekListEntries) {
                            disposables.add(bggServiceXML.geekList(geekListEntry.getId())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<GeekList>() {
                                        @Override
                                        public void accept(GeekList geekList) throws Exception {
                                            Timber.v(geekList.getTitle() + " : " + geekListEntry.getHref() + "\n");
                                            for (GeekListItem geekListItem : geekList.getItems()) {
                                                Timber.v(geekListItem.getObjectName() + "\n");

                                                geekListItems.add(geekListItem);

                                                List<GeekListComment> comments = geekListItem.getComments();
                                                Collections.reverse(comments);
                                                for (GeekListComment geekListComment : comments) {
                                                    Timber.v(BidParser.parse(geekListComment.content) + "\n");
                                                }

                                                rcAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    }));
                            //break;
                        }

                    }
                }));

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        for (Disposable disposable : disposables) {
            disposable.dispose();
        }
    }

}
