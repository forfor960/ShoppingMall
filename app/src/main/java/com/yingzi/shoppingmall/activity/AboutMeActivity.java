package com.yingzi.shoppingmall.activity;

import android.view.View;
import android.widget.Button;

import com.yingzi.shoppingmall.R;
import com.yingzi.shoppingmall.base.BaseActivity;

/**
 * Created by yingzi on 2018/2/9.
 */

public class AboutMeActivity extends BaseActivity implements View.OnClickListener {
    private Button go_back_btn;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_me;
    }

    @Override
    protected void initViews() {
        go_back_btn = findView(R.id.go_back_btn);
        go_back_btn.setOnClickListener(this);
    }

    @Override
    protected void initDatas() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.go_back_btn:
                finish();
                break;
        }
    }
}
