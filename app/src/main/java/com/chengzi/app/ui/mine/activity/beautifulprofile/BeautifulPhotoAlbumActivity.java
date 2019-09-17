package com.chengzi.app.ui.mine.activity.beautifulprofile;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityBeautifulPhotoAlbumBindingImpl;
import com.chengzi.app.third.fileuplod.AliError;
import com.chengzi.app.third.fileuplod.AliUpload;
import com.chengzi.app.ui.mine.viewmodel.BeautifulPhotoViewModel;
import com.chengzi.app.utils.CertifiedHelp;
import com.chengzi.app.widget.NineGridView2;

import java.util.ArrayList;
import java.util.List;

import static com.chengzi.app.ui.discover.activity.PublishCameraActivity.PHOTO_PATH;


/**
 * 美丽相册
 *
 * @ClassName:BeautifulPhotoAlbumActivity
 * @PackageName:com.yimei.app.ui.mine.activity.beautifulprofile
 * @Create On 2019/4/4 0004   18:58
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/4 0004 handongkeji All rights reserved.
 */

public class BeautifulPhotoAlbumActivity extends BaseActivity<ActivityBeautifulPhotoAlbumBindingImpl, BeautifulPhotoViewModel> {

    private NineGridView2.NineGridAdapter adapter;
    //上个页面传过来的-->order_id 和 图片集合
    private ArrayList<String> strings = new ArrayList<>();
    private String order_id;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_beautiful_photo_album;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(clickListener);
        order_id = getIntent().getStringExtra("order_id");
        strings = getIntent().getStringArrayListExtra("strings");

        mBinding.nineGridView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new NineGridView2.NineGridAdapter(R.drawable.shangchuanzhaopian,
                R.drawable.close_circle, mViewModel.items);
        mBinding.nineGridView.setAdapter(adapter);
        if (strings != null && strings.size() > 0) {
            adapter.addItem(strings);
        }
        adapter.setListener(view -> {
            if (mViewModel.items.size() >= 9) {
                ToastUtils.showShort("最多九张图片！");
                return;
            }
            CertifiedHelp.showNineDialog(this, true, 9 - mViewModel.items.size());
        });

//        //上传图片成功
//        mViewModel.uploadLiveData.observe(this, listResponseBean -> {
//            dismissLoading();
//            if (listResponseBean != null && listResponseBean.getData() != null && listResponseBean.getData().size() > 0) {
//                //上传成功的图片
//                List<String> data = listResponseBean.getData();
//                //上个页面上传成功的图片
//                List<String> uploadImage = ImageIsUploadUtils.isUploadImage(mViewModel.items);
//                for (int i = 0; i < data.size(); i++) {
//                    uploadImage.add(data.get(i));
//                }
//                mViewModel.recordEdit(order_id, uploadImage);
//            }
//        });

        //修改成功
        mViewModel.recordEditLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean.getStatus() == Sys.SUCCESS_STATUS) {
                finish();
                toast("编辑美丽相片成功!");
            }
        });
    }

    private ClickEventHandler<Object> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.tv_submit:  //  确定
//                toast("确定");
                //上传图片--->区分一下是不是上传过的 如果上传过 则跳过上传 TODO:::::
//                List<String> notUploadImage = ImageIsUploadUtils.isNotUploadImage(mViewModel.items);
                showLoading(Sys.LOADING);
//                if (notUploadImage != null && notUploadImage.size() > 0) {
//                    //先上传未上传的图片
//                    mViewModel.uploadImage(notUploadImage);
//                } else {
//                    //上个页面上传成功的图片
//                    List<String> uploadImage = ImageIsUploadUtils.isUploadImage(mViewModel.items);
//                    mViewModel.recordEdit(order_id, uploadImage);
//                }
                mViewModel.recordEdit(order_id, mViewModel.items);
                break;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case Sys.REQUEST_CODE_PIC:
                List<ImageItem> complaintsitems = (List<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                ArrayList<String> list = new ArrayList<>();
                for (ImageItem item : complaintsitems) {
                    list.add(new String(item.path));
                }
                upload(list);
                break;
            case Sys.REQUEST_CODE_CAMERA:
                String picPath = data.getStringExtra(PHOTO_PATH);
                upload(picPath);
                break;
        }
    }

    private void upload(List<String> images) {
        showLoading(Sys.LOADING);
        AliUpload.multipleUpload(images, new AliUpload.MultipleCallBack() {
            @Override
            public ArrayList<String> onSuccess(List<String> paths) {
                dismissLoading();
                adapter.addItem((ArrayList<String>) paths);
                return null;
            }

            @Override
            public ArrayList<String> onError(List<AliError> errors) {
                dismissLoading();
                Log.d("ALI_UP", "onError: " + errors.toString());
                toast("上传失败");
                return null;
            }
        });
    }

    private void upload(String images) {
        showLoading(Sys.LOADING);
        AliUpload.upload( System.currentTimeMillis() + AccountHelper.getUserId(), images, new AliUpload.CallBack() {
            @Override
            public void onSuccess(String paths) {
                dismissLoading();
                adapter.addItem(paths);
            }

            @Override
            public void onError(AliError errors) {
                dismissLoading();
                Log.d("ALI_UP", "onError: " + errors.toString());
                toast("上传失败");
            }
        });
    }
}