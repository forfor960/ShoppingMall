package com.yingzi.shoppingmall.fragment;

import android.view.View;
import android.widget.LinearLayout;

import com.yingzi.shoppingmall.R;
import com.yingzi.shoppingmall.activity.AboutMeActivity;
import com.yingzi.shoppingmall.base.BaseFragment;

/**
 * Created by yingzi on 2018/2/8.
 */

public class MineFragment extends BaseFragment implements View.OnClickListener {
    private LinearLayout about_me_lyt;

    @Override
    protected void initViews(View view) {
        about_me_lyt = (LinearLayout) view.findViewById(R.id.about_me_lyt);
        about_me_lyt.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initDatas() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_me_lyt:
                startActivity(AboutMeActivity.class);
                break;
        }
    }
}
