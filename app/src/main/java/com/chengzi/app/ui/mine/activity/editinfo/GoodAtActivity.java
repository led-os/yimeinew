package com.chengzi.app.ui.mine.activity.editinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityGoodAtBindingImpl;
import com.chengzi.app.ui.mine.bean.GoodAtBean;
import com.chengzi.app.ui.mine.viewmodel.EditInfoUserViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 擅长
 *
 * @ClassName:GoodAtActivity
 * @PackageName:com.yimei.app.ui.mine.activity.editinfo
 * @Create On 2019/4/18 0018   18:39
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/18 0018 handongkeji All rights reserved.
 */

public class GoodAtActivity extends BaseActivity<ActivityGoodAtBindingImpl, EditInfoUserViewModel> {

    private List<GoodAtBean> chooseList = new ArrayList<>();
    //选中的数量  最多3个
    private int num = 0;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_good_at;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(clickListener);
        List<GoodAtBean> chooseTrueList = (List<GoodAtBean>) getIntent().getSerializableExtra("chooseList");
//        setFlow(chooseList);
        mBinding.mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        GoodAtAdapter adapter = new GoodAtAdapter();
        mBinding.mRecyclerView.setAdapter(adapter);

        showLoading(Sys.LOADING);
        mViewModel.parentLevelList();
        mViewModel.parentLevelListLiveData.observe(this, goodAtBeans -> {
            dismissLoading();
            if (goodAtBeans != null && goodAtBeans.size() > 0) {
                for (int i = 0; i < goodAtBeans.size(); i++) {
                    for (int j = 0; j < chooseTrueList.size(); j++) {
                        if (chooseTrueList.get(j).getId().equals(goodAtBeans.get(i).getId())) {
                            goodAtBeans.get(i).setChoose(true);
                        }
                    }
                }
                chooseList = goodAtBeans;
                adapter.setNewData(chooseList);
                for (int i = 0; i < chooseList.size(); i++) {
                    if (chooseList.get(i).isChoose())
                        num++;
                }
            }
        });
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            GoodAtBean dataBean = chooseList.get(position);
            boolean choose = dataBean.isChoose();
            if (choose) {    //取消
                dataBean.setChoose(false);
                num--;
            } else {
                if (num == 3) {
                    toast("最多选择3个擅长");
                } else {
                    dataBean.setChoose(true);
                    num++;
                }
            }
            adapter.notifyDataSetChanged();
        });
        //更改擅长成功
        mViewModel.parentLevelUpdateLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
                Intent intent = new Intent();
                intent.putExtra("chooseList", (Serializable) chooseList);
                setResult(0x392, intent);
                finish();
            }
        });
    }

    //最多选3个 选中的再次点击则为取消 上个页面带回来的数据则要回显
    public class GoodAtAdapter extends BaseQuickAdapter<GoodAtBean, BaseViewHolder> {
        public GoodAtAdapter() {
            super(R.layout.item_good_at_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, GoodAtBean item) {
            TextView view = helper.getView(R.id.tv_name);
            view.setText(item.getName());
            if (item.isChoose()) {
                view.setSelected(true);
            } else {
                view.setSelected(false);
            }
        }
    }

    public ClickEventHandler<Object> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.tv_save:  //确定
                showLoading(Sys.LOADING);
                String str = "";
                for (int i = 0; i < chooseList.size(); i++) {
                    GoodAtBean goodAtBean = chooseList.get(i);
                    if (goodAtBean.isChoose()) {
                        str = str + goodAtBean.getId() + ",";
                    }
                }
                if (!TextUtils.isEmpty(str)) {
                    str = str.substring(0, str.length() - 1);
                }
                mViewModel.parentLevelUpdate(str);
                break;
        }
    };
}