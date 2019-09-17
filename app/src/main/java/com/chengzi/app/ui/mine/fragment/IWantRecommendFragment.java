package com.chengzi.app.ui.mine.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.zxing.WriterException;
import com.handong.framework.base.BaseFragment;
import com.handong.framework.base.BaseViewModel;
import com.nevermore.oceans.google.zxing.encoding.EncodingHandler;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentIwantRecommendBindingImpl;
import com.chengzi.app.ui.mine.bean.PromoteRoutingBean;

/**
 * 我要推荐
 *
 * @ClassName:IWantRecommendFragment
 * @PackageName:com.yimei.app.ui.mine.fragment
 * @Create On 2019/4/15 0015   14:55
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/15 0015 handongkeji All rights reserved.
 */

public class IWantRecommendFragment extends BaseFragment<FragmentIwantRecommendBindingImpl, BaseViewModel> {

    private PromoteRoutingBean data;

    public static IWantRecommendFragment newInstance(PromoteRoutingBean data) {
        IWantRecommendFragment fragment = new IWantRecommendFragment();
        Bundle args = new Bundle();
        args.putSerializable("data", data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_iwant_recommend;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            data = (PromoteRoutingBean) arguments.getSerializable("data");
            binding.setBean(data);
            binding.setUrl(data.getUser_image());
            binding.setUrlhospital(data.getBinding_img());

            //根据医疗机构 生成二维码
            try {
                String binding_id = data.getBinding_id();
                if (!TextUtils.isEmpty(binding_id)) {
                    //根据输入的文本生成对应的二维码并且显示出来
                    Bitmap mBitmap = EncodingHandler.createQRCode(binding_id, 230);
                    if (mBitmap != null) {
//                    Toast.makeText(this, "二维码生成成功！", Toast.LENGTH_SHORT).show();;
                        binding.ivQrCode.setImageBitmap(mBitmap);
                    }
                }
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
    }
}
