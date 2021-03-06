/*
 * Copyright (C) 2017 Sysdata Spa.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sysdata.demo.accordionview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.sysdata.widget.accordion.FancyAccordionView;
import com.sysdata.widget.accordion.ExpandableItemHolder;
import com.sysdata.widget.accordion.Item;
import com.sysdata.widget.accordion.ItemAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SampleActivity extends AppCompatActivity {

    private static final String KEY_EXPANDED_ID = "expandedId";

    public static final int VIEW_TYPE_1 = 1;
    public static final int VIEW_TYPE_2 = 2;

    private Toast mToast;
    private FancyAccordionView mRecyclerView;
    private ItemAdapter.OnItemClickedListener mListener = new ItemAdapter.OnItemClickedListener() {
        @Override
        public void onItemClicked(ItemAdapter.ItemViewHolder<?> viewHolder, int id) {
            ItemAdapter.ItemHolder itemHolder = viewHolder.getItemHolder();
            SampleItem item = ((SampleItem) itemHolder.item);

            switch (id) {
                case ItemAdapter.OnItemClickedListener.ACTION_ID_COLLAPSED_VIEW:
                    showToast(String.format("Collapsed %s clicked!", item.getTitle()));
                    break;
                case ItemAdapter.OnItemClickedListener.ACTION_ID_EXPANDED_VIEW:
                    showToast(String.format("Expanded %s clicked!", item.getTitle()));
                    break;
                default:
                    // do nothing
                    break;
            }
        }
    };

    /**
     * This method use {@link ActivityCompat#startActivity(Context, Intent, Bundle)} to launch SampleActivity.
     *
     * @param fromActivity the activity from
     */
    public static void launch(FragmentActivity fromActivity) {
        Intent intent = new Intent(fromActivity, SampleActivity.class);
        ActivityCompat.startActivity(fromActivity, intent, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        mRecyclerView = (FancyAccordionView) findViewById(R.id.alarms_recycler_view);

        // bind the factory to create view holder for item collapsed of type 1
        mRecyclerView.setCollapsedViewHolderFactory(
                SampleCollapsedViewHolder.Factory.create(R.layout.sample_layout_collapsed),
                mListener,
                VIEW_TYPE_1
        );
        // bind the factory to create view holder for item collapsed of type 2
        mRecyclerView.setCollapsedViewHolderFactory(
                SampleCollapsedViewHolder.Factory.create(R.layout.sample_layout_collapsed_type2),
                mListener,
                VIEW_TYPE_2
        );

        // bind the factory to create view holder for item expanded of type 1
        mRecyclerView.setExpandedViewHolderFactory(
                SampleExpandedViewHolder.Factory.create(R.layout.sample_layout_expanded),
                mListener,
                VIEW_TYPE_1
        );
        // bind the factory to create view holder for item expanded of type 2
        mRecyclerView.setExpandedViewHolderFactory(
                SampleExpandedViewHolder.Factory.create(R.layout.sample_layout_expanded_type2),
                mListener,
                VIEW_TYPE_2
        );

        // restore the expanded item from state
        if (savedInstanceState != null) {
            mRecyclerView.setExpandedItemId(savedInstanceState.getLong(KEY_EXPANDED_ID, Item.INVALID_ID));
        }

        // populate RecyclerView with mock data
        loadData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_EXPANDED_ID, mRecyclerView.getExpandedItemId());
    }

    private void loadData() {
        final int dataCount = 50;
        int index = 0;

        final List<ExpandableItemHolder> itemHolders = new ArrayList<>(dataCount);
        Item itemModel;
        ExpandableItemHolder itemHolder;
        for (; index < dataCount; index++) {
            int viewType = VIEW_TYPE_1;
            if(!isOdd(index)){
                viewType = VIEW_TYPE_2;
            }
            itemModel = SampleItem.create(getItemTitle(index), getItemDescription(index));
            itemHolder = new ExpandableItemHolder(itemModel, viewType);
            itemHolders.add(itemHolder);
        }

        mRecyclerView.setAdapterItems(itemHolders);
    }

    private boolean isOdd(int index) {
        return (index % 2) == 0;
    }

    private String getItemTitle(int position) {
        String title = String.format(Locale.ITALY, "Item %d", position + 1);
        if(!isOdd(position)){
            title += " (Type 2)";
        } else {
            title += " (Type 1)";
        }
        return title;
    }

    private String getItemDescription(int position) {
        String description = "Hello World, I'm an expandable item";
        if(!isOdd(position)){
            description += " of type 2!";
        } else {
            description += " of type 1!";
        }
        return description;
    }

    private void showToast(String text) {
        if (mToast != null) {
            mToast.cancel();
        }

        mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
