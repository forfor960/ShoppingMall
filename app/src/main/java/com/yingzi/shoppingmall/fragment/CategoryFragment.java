package com.yingzi.shoppingmall.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yingzi.shoppingmall.R;
import com.yingzi.shoppingmall.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yingzi on 2018/2/8.
 */

public class CategoryFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerview_left, mRecyclerview_right;
    private BaseQuickAdapter mAdapter_left, mAdapter_right;
    private LinearLayoutManager mLinearLayoutManager;
    private List<String> mDatas_left;
    private List<String> mDatas_right;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int position_choice;
    private final static int REFRESH = 1;
    private final static int LOAD_MORE = 2;
    private final static int LOAD_NUM = 20;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_category;
    }

    @Override
    protected void initViews(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        mRecyclerview_left = (RecyclerView) view.findViewById(R.id.recyclerView_left);
        mLinearLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerview_left.setLayoutManager(mLinearLayoutManager);
        mRecyclerview_right = (RecyclerView) view.findViewById(R.id.recyclerview_right);
        mLinearLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerview_right.setLayoutManager(new GridLayoutManager(mActivity, 3));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue);

    }

    @Override
    protected void initDatas() {
        mDatas_left = new ArrayList<>();
        mDatas_right = new ArrayList<>();
        mAdapter_left = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.category_left_item, mDatas_left) {

            @Override
            protected void convert(BaseViewHolder helper, String item) {
                if (helper.getAdapterPosition() == 0) {
                    helper.getView(R.id.tag_lyt).setSelected(true);
                    position_choice = 0;
                    getList(REFRESH, 0);
                }
                helper.setText(R.id.tag_tv, item);
            }

        };
        mAdapter_right = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.category_right_item, mDatas_right) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.name_tv, item);
            }
        };
        mAdapter_left.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i = 0; i < mAdapter_left.getItemCount(); i++) {
                    View v = mRecyclerview_left.getChildAt(i);
                    v.setSelected(false);
                }
                position_choice = position;
                view.setSelected(true);
                getList(REFRESH, position);
            }
        });
        mAdapter_left.bindToRecyclerView(mRecyclerview_left);
        mAdapter_left.setEnableLoadMore(false);

        mAdapter_left.disableLoadMoreIfNotFullPage(mRecyclerview_left);

        mAdapter_right.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(mActivity, mDatas_right.get(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });
        mAdapter_right.bindToRecyclerView(mRecyclerview_right);
        mAdapter_right.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getList(LOAD_MORE, position_choice);
            }
        });
        mAdapter_right.disableLoadMoreIfNotFullPage(mRecyclerview_right);
        getLeftDatas();
    }

    private void getLeftDatas() {
        mDatas_left.clear();
        for (int i = 0; i < 5; i++) {
            mDatas_left.add("category" + i);
        }
        mAdapter_left.notifyDataSetChanged();
    }

    private void getList(int refresh, int position) {
        if (refresh == REFRESH) {
            mDatas_right.clear();
            for (int i = 0; i < LOAD_NUM; i++) {
                mDatas_right.add("category" + position);
            }
            mAdapter_right.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(false);
        } else if (refresh == LOAD_MORE) {
            for (int i = 0; i < LOAD_NUM; i++) {
                mDatas_right.add("category" + position);
            }
            mAdapter_right.notifyDataSetChanged();
            mAdapter_right.loadMoreComplete();
        }
    }

    @Override
    public void onRefresh() {
        getList(REFRESH, position_choice);
    }
}
