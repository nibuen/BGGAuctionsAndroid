package com.boarbeard.bggauctions;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boarbeard.bggauctions.io.BggService;
import com.boarbeard.bggauctions.io.BggServiceFactory;
import com.boarbeard.bggauctions.model.GeekList;
import com.boarbeard.bggauctions.model.GeekListComment;
import com.boarbeard.bggauctions.model.GeekListItem;
import com.boarbeard.bggauctions.parser.BidParser;
import com.boarbeard.bggauctions.ui.SampleRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    public static final String ARG_HREF = "href";

    private List<Disposable> disposables = new ArrayList<>();

    private List<GeekListItem> geekListItems = new ArrayList<>();

    private StaggeredGridLayoutManager _sGridLayoutManager;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {

            Activity activity = this.getActivity();
            final CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);

            RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);

            _sGridLayoutManager = new StaggeredGridLayoutManager(3,
                    StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(_sGridLayoutManager);

            final SampleRecyclerViewAdapter rcAdapter = new SampleRecyclerViewAdapter(
                    activity, geekListItems);
            recyclerView.setAdapter(rcAdapter);

            final BggService bggServiceXML = BggServiceFactory.createForXml();

            int geeklistId = getArguments().getInt(ARG_ITEM_ID);
            disposables.add(bggServiceXML.geekList(geeklistId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<GeekList>() {
                        @Override
                        public void accept(GeekList geekList) throws Exception {
                            Timber.v(geekList.getTitle() + " : " + getArguments().getString(ARG_HREF) + "\n");
                            if (appBarLayout != null) {
                                appBarLayout.setTitle(geekList.getTitle());
                            }

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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.geek_list_item, container, false);

        return rootView;
    }
}
