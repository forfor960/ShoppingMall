package com.yingzi.shoppingmall.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yingzi.shoppingmall.R;
import com.yingzi.shoppingmall.base.BaseFragment;
import com.yingzi.shoppingmall.model.ShoppingcartModel;
import com.yingzi.shoppingmall.widget.NumberAddSubView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yingzi on 2018/2/8.
 */

public class ShoppingcartFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerview;
    private BaseQuickAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLinearLayoutManager;
    private final static int REFRESH = 1;
    private final static int LOAD_MORE = 2;
    private final static int LOAD_NUM = 30;
    private RelativeLayout bottom_relyt1, bottom_relyt2;
    private TextView change_btn;
    public static final int EDIT = 1;
    public static final int COMPLETE = 2;
    private TextView total_price_tv;
    private CheckBox allchoice_ck1, allchoice_ck2;
    private Button delete_btn;
    private List<String> choice_arr = new ArrayList<>();
    private List<String> del_arr = new ArrayList<>();
    private List<ShoppingcartModel.GoodsListBean> mDatas;
    private Button pay_btn;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shoppingcart;
    }

    @Override
    protected void initViews(View view) {
        pay_btn = (Button) view.findViewById(R.id.pay_btn);
        pay_btn.setOnClickListener(this);
        delete_btn = (Button) view.findViewById(R.id.delete_btn);
        delete_btn.setOnClickListener(this);
        allchoice_ck1 = (CheckBox) view.findViewById(R.id.allchoice_ck1);
        allchoice_ck2 = (CheckBox) view.findViewById(R.id.allchoice_ck2);
        allchoice_ck1.setOnClickListener(this);
        allchoice_ck2.setOnClickListener(this);

        total_price_tv = (TextView) view.findViewById(R.id.total_price_tv);
        change_btn = (TextView) view.findViewById(R.id.change_btn);
        change_btn.setOnClickListener(this);
        bottom_relyt1 = (RelativeLayout) view.findViewById(R.id.bottom_relyt1);
        bottom_relyt2 = (RelativeLayout) view.findViewById(R.id.bottom_relyt2);
        change_btn.setTag(COMPLETE);
        change_btn.setText(getString(R.string.editor));
        bottom_relyt1.setVisibility(View.VISIBLE);
        bottom_relyt2.setVisibility(View.GONE);
        mRecyclerview = (RecyclerView) view.findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        mLinearLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerview.setLayoutManager(mLinearLayoutManager);
        mRecyclerview.addItemDecoration(new DividerItemDecoration(mActivity, LinearLayoutManager.VERTICAL));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerview.setItemAnimator(new DefaultItemAnimator());
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue);
        mDatas = new ArrayList<>();

        mAdapter = new BaseQuickAdapter<ShoppingcartModel.GoodsListBean, BaseViewHolder>(R.layout.shoppingcart_item, mDatas) {
            @Override
            protected void convert(BaseViewHolder helper, final ShoppingcartModel.GoodsListBean item) {
                int action = (int) change_btn.getTag();
                if (EDIT == action) {
                    helper.setVisible(R.id.number_add_sub_view, false);
                    helper.setVisible(R.id.delete_iv, true);
                } else if (COMPLETE == action) {
                    helper.setVisible(R.id.number_add_sub_view, true);
                    helper.setVisible(R.id.delete_iv, false);
                }
                helper.setText(R.id.price_tv, "¥ " + item.getPrice());
                helper.setText(R.id.name_tv, item.getName());
                NumberAddSubView mNumberAddSubView = helper.getView(R.id.number_add_sub_view);
                mNumberAddSubView.setValue(Integer.valueOf(item.getNum()));
                mNumberAddSubView.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
                    @Override
                    public void onButtonSubClick(View v, int value) {
                        item.setNum(String.valueOf(value));
                        showTotalPrice();
                    }

                    @Override
                    public void onButtonAddClick(View v, int value) {
                        item.setNum(String.valueOf(value));
                        showTotalPrice();
                    }
                });
                final CheckBox mCheckBox = helper.getView(R.id.checkbox);
                if (item.getCheck() == null || !item.getCheck()) {
                    mCheckBox.setChecked(false);
                } else {
                    mCheckBox.setChecked(true);
                }
                mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        item.setCheck(isChecked);
                        checkAll();
                        showTotalPrice();
                    }
                });
                ImageView delete_iv = helper.getView(R.id.delete_iv);
                delete_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        del_arr.clear();
                        del_arr.add(item.getGood_id());
                        delete();
                    }
                });
            }
        };

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });
        mAdapter.bindToRecyclerView(mRecyclerview);

        mAdapter.disableLoadMoreIfNotFullPage(mRecyclerview);
    }

    @Override
    protected void initDatas() {
        getData(REFRESH);
    }

    private void getData(int refresh) {
        if (refresh == REFRESH) {
            mDatas.clear();
            for (int i = 0; i < 2; i++) {
                ShoppingcartModel.GoodsListBean good = new ShoppingcartModel.GoodsListBean();
                good.setCheck(false);
                good.setName("Test" + i);
                good.setPrice("100");
                good.setNum("1");
                good.setGood_id(String.valueOf(i));
                mDatas.add(good);
            }
            mAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(false);
        } else if (refresh == LOAD_MORE) {
            for (int i = 0; i < 2; i++) {
                ShoppingcartModel.GoodsListBean good = new ShoppingcartModel.GoodsListBean();
                good.setCheck(false);
                good.setName("Test" + i);
                good.setPrice("100");
                good.setNum("1");
                good.setGood_id(String.valueOf(i));
                mDatas.add(good);
            }
            mAdapter.notifyDataSetChanged();
            mAdapter.loadMoreComplete();
        }
    }

    private void showTotalPrice() {
        float sum = 0;
        for (ShoppingcartModel.GoodsListBean cart :
                mDatas) {
            if (cart.getCheck())
                sum += Integer.valueOf(cart.getNum()) * Float.valueOf(cart.getPrice());
        }
        total_price_tv.setText("¥ " + sum);
    }

    /**
     * 全选按钮判断
     */
    private void checkAll() {
        int action = (int) change_btn.getTag();
        int count = 0;
        int checkNum = 0;
        if (mDatas != null) {
            count = mDatas.size();
            for (ShoppingcartModel.GoodsListBean cart : mDatas) {
                if (cart.getCheck() == null || !cart.getCheck()) {
                    if (EDIT == action) {
                        allchoice_ck2.setChecked(false);
                    } else if (COMPLETE == action) {
                        allchoice_ck1.setChecked(false);
                    }
                    break;
                } else {
                    checkNum = checkNum + 1;
                }
            }

            if (count == checkNum) {
                if (EDIT == action) {
                    allchoice_ck2.setChecked(true);
                } else if (COMPLETE == action) {
                    allchoice_ck1.setChecked(true);
                }
            }

        }
    }

    /**
     * 获取选中项
     */
    private void getChecks() {
        choice_arr.clear();
        del_arr.clear();
        int action = (int) change_btn.getTag();
        if (mDatas != null) {
            for (ShoppingcartModel.GoodsListBean cart : mDatas) {
                if (cart.getCheck() != null && cart.getCheck()) {
                    if (EDIT == action) {
                        del_arr.add(cart.getGood_id());
                    } else if (COMPLETE == action) {
                        choice_arr.add(cart.getGood_id());
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_btn:
                int action = (int) v.getTag();
                if (EDIT == action) {
                    change_btn.setText(getString(R.string.editor));
                    bottom_relyt1.setVisibility(View.VISIBLE);
                    bottom_relyt2.setVisibility(View.GONE);
                    checkAllNone(false);
                    allchoice_ck1.setChecked(false);
                    change_btn.setTag(COMPLETE);
                } else if (COMPLETE == action) {
                    change_btn.setText(R.string.complete);
                    bottom_relyt1.setVisibility(View.GONE);
                    bottom_relyt2.setVisibility(View.VISIBLE);
                    checkAllNone(false);
                    allchoice_ck1.setChecked(false);
                    change_btn.setTag(EDIT);
                }//按钮切换恢复初始数据
                break;
            case R.id.allchoice_ck1:
                checkAllNone(allchoice_ck1.isChecked());
                break;
            case R.id.allchoice_ck2:
                checkAllNone(allchoice_ck2.isChecked());
                break;
            case R.id.delete_btn:
                getChecks();
                delete();
                Toast.makeText(mActivity, del_arr.toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.pay_btn:
                getChecks();
                Toast.makeText(mActivity, choice_arr.toString(), Toast.LENGTH_SHORT).show();
                break;


        }
    }

    private void delete() {
        for (String good_id : del_arr) {
            Iterator<ShoppingcartModel.GoodsListBean> it = mDatas.iterator();
            while (it.hasNext()) {
                ShoppingcartModel.GoodsListBean good = it.next();
                if (good.getGood_id().equals(good_id)) {
                    it.remove();
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private void checkAllNone(boolean checked) {
        for (int i = 0; i < mDatas.size(); i++) {
            mDatas.get(i).setCheck(checked);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        getData(REFRESH);
    }
}
