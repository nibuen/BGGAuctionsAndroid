package com.boarbeard.bggauctions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boarbeard.bggauctions.io.BggService;
import com.boarbeard.bggauctions.io.BggServiceFactory;
import com.boarbeard.bggauctions.model.GeekListEntry;
import com.boarbeard.bggauctions.model.GeekListsResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private List<Disposable> disposables = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(new ArrayList<GeekListEntry>()));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.GeekListsViewHolder> {

        private final List<GeekListEntry> geekListEntries;

        public SimpleItemRecyclerViewAdapter(List<GeekListEntry> items) {
            geekListEntries = items;

            final BggService bggServiceJson = BggServiceFactory.createForJson();

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
                            Timber.d("number of geeklist Entries: %d", geekListEntries.size());
                            for (GeekListEntry geekListEntry : geekListEntries) {
                                SimpleItemRecyclerViewAdapter.this.geekListEntries.add(geekListEntry);
                                SimpleItemRecyclerViewAdapter.this.notifyDataSetChanged();
                            }


                        }
                    }));
        }

        @Override
        public GeekListsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);

            return new GeekListsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final GeekListsViewHolder holder, int position) {
            holder.mItem = geekListEntries.get(position);
            holder.mNumItemsView.setText(String.valueOf(geekListEntries.get(position).getNumberOfItems()));
            holder.mContentView.setText(geekListEntries.get(position).getTitle());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putInt(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.getId());
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ItemDetailActivity.class);
                        intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.getId());

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return geekListEntries.size();
        }

        public class GeekListsViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mContentView;
            private final TextView mNumItemsView;
            public GeekListEntry mItem;

            public GeekListsViewHolder(View view) {
                super(view);
                mView = view;
                mContentView = (TextView) view.findViewById(R.id.content);
                mNumItemsView = (TextView) view.findViewById(R.id.num_items);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        for (Disposable disposable : disposables) {
            disposable.dispose();
        }

    }
}
