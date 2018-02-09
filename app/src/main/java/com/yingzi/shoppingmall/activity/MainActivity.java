package com.yingzi.shoppingmall.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.yingzi.shoppingmall.R;
import com.yingzi.shoppingmall.base.BaseActivity;
import com.yingzi.shoppingmall.fragment.CategoryFragment;
import com.yingzi.shoppingmall.fragment.HomepageFragment;
import com.yingzi.shoppingmall.fragment.MineFragment;
import com.yingzi.shoppingmall.fragment.ShoppingcartFragment;

/**
 * Created by yingzi on 2018/2/8.
 */

public class MainActivity extends BaseActivity {
    private Button[] mTabs;
    private int currentTabIndex, index;
    private Fragment[] fragments;
    private HomepageFragment mHomepageFragment;
    private CategoryFragment mCategoryFragment;
    private ShoppingcartFragment mShoppingcartFragment;
    private MineFragment mMineFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        mTabs = new Button[5];
        mTabs[0] = (Button) findViewById(R.id.btn_homepage);
        mTabs[1] = (Button) findViewById(R.id.btn_category);
        mTabs[2] = (Button) findViewById(R.id.btn_shoppingcart);
        mTabs[3] = (Button) findViewById(R.id.btn_mine);
        mHomepageFragment = new HomepageFragment();
        mCategoryFragment = new CategoryFragment();
        mShoppingcartFragment = new ShoppingcartFragment();
        mMineFragment = new MineFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        currentTabIndex = 0;
        fragments = new Fragment[]{mHomepageFragment, mCategoryFragment, mShoppingcartFragment, mMineFragment};
        fragmentTransaction.add(R.id.fragment_container, mHomepageFragment);
        fragmentTransaction
                .show(mHomepageFragment)
                .commit();
        mTabs[0].setSelected(true);
    }

    @Override
    protected void initDatas() {

    }

    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_homepage:
                index = 0;
                break;
            case R.id.btn_category:
                index = 1;
                break;
            case R.id.btn_shoppingcart:
                index = 2;
                break;
            case R.id.btn_mine:
                index = 3;
                break;
        }
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commitAllowingStateLoss();
            mTabs[currentTabIndex].setSelected(false);
            mTabs[index].setSelected(true);
            currentTabIndex = index;
        }
    }
}
