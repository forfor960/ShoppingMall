package com.yingzi.shoppingmall.fragment;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yingzi.shoppingmall.R;
import com.yingzi.shoppingmall.base.BaseFragment;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoaderInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by yingzi on 2018/2/8.
 */

public class HomepageFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerview;
    private BaseQuickAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLinearLayoutManager;
    private final static int REFRESH = 1;
    private final static int LOAD_MORE = 2;
    private final static int LOAD_NUM = 10;//一次加载条数
    private List<String> mDatas;
    private View top_view;
    private Banner banner;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_homepage;
    }

    @Override
    protected void initViews(View view) {
        mRecyclerview = (RecyclerView) view.findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        mLinearLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerview.setLayoutManager(mLinearLayoutManager);
        mRecyclerview.addItemDecoration(new DividerItemDecoration(mActivity, LinearLayoutManager.VERTICAL));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerview.setItemAnimator(new DefaultItemAnimator());
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue);
        mDatas = new ArrayList<>();
        mAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.homepage_item, mDatas) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.name_tv, item);
                Random random = new Random();
                helper.setText(R.id.price_tv, "￥" + (random.nextInt(2000) + 100));
            }
        };
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(mActivity, mDatas.get(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });
        mAdapter.bindToRecyclerView(mRecyclerview);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getDatas(LOAD_MORE);
            }
        });
        mAdapter.disableLoadMoreIfNotFullPage(mRecyclerview);

        top_view = LayoutInflater.from(mActivity).inflate(R.layout.homepage_top, null);
        banner = (Banner) top_view.findViewById(R.id.banner);
        mAdapter.addHeaderView(top_view);
    }

    @Override
    protected void initDatas() {
        initBanner();
        getDatas(REFRESH);
    }

    private void getDatas(int refresh) {
        if (refresh == REFRESH) {
            mDatas.clear();
            for (int i = 0; i < LOAD_NUM; i++) {
                mDatas.add("something" + i);
            }
            mAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(false);
        } else if (refresh == LOAD_MORE) {
            for (int i = 0; i < LOAD_NUM; i++) {
                mDatas.add("something" + i);
            }
            mAdapter.loadMoreComplete();
            mAdapter.notifyDataSetChanged();
        }
    }

    private void initBanner() {
        List<Integer> images = new ArrayList<Integer>() {{
            add(R.mipmap.ic_launcher);
            add(R.mipmap.ic_launcher);

        }};
        banner.setImageLoader(new BannerImageLoader());
        banner.setImages(images);
        banner.start();
    }

    @Override
    public void onRefresh() {
        getDatas(REFRESH);
    }

    private class BannerImageLoader implements ImageLoaderInterface {
        @Override
        public void displayImage(Context context, Object path, View imageView) {
            imageView.setBackgroundResource((Integer) path);
        }

        @Override
        public View createImageView(Context context) {
            return null;
        }
    }
}
