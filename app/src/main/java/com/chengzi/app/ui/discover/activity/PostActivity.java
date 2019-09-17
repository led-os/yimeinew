package com.chengzi.app.ui.discover.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityPostBinding;
import com.chengzi.app.dialog.SelectMediaDialog;
import com.chengzi.app.event.PostForumEvent;
import com.chengzi.app.ui.common.VideoItem;
import com.chengzi.app.ui.common.activity.VideoGridActivity;
import com.chengzi.app.ui.discover.viewmodel.PostViewModel;
import com.chengzi.app.ui.mine.bean.FollowBean;
import com.chengzi.app.widget.AtSpan;
import com.chengzi.app.widget.NineGridView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.chengzi.app.ui.common.activity.VideoGridActivity.VIDEO_ITEM;
import static com.chengzi.app.ui.discover.activity.PublishCameraActivity.PHOTO_PATH;
import static com.chengzi.app.ui.discover.activity.PublishCameraActivity.RECORD_TYPE;
import static com.chengzi.app.ui.discover.activity.PublishCameraActivity.VIDEO_PATH;

/**
 * 发表帖子
 *
 * @ClassName:PostActivity
 * @PackageName:com.yimei.app.ui.find.activity
 * @Create On 2019/4/1 0001   14:12
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/1 0001 handongkeji All rights reserved.
 */
public class PostActivity extends BaseActivity<ActivityPostBinding, PostViewModel> implements OnClickListener {

    public static final int REQUEST_CODE_PIC = 0x01;
    public static final int REQUEST_CODE_CAMERA = 0x02;
    public static final int REQUEST_CODE_VIDEO = 0x03;
    public static final int REQUEST_CODE_AT = 0x04;
    private NineGridView.NineGridAdapter adapter;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_post;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        mBinding.setListener(this);
        mBinding.setViewModel(mViewModel);

        mBinding.nineGridView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new NineGridView.NineGridAdapter(R.drawable.shangchuanzhaopian,
                R.drawable.fankui_guanbi, mViewModel.items);
        mBinding.nineGridView.setAdapter(adapter);
        adapter.setListener(view -> {
//            openPicture();
            showSelectMediaDialog();
        });

        mBinding.topBar.setOnRightClickListener(v -> {


            if (AccountHelper.shouldLogin(this)) {
                return;
            }

            if (TextUtils.isEmpty(mViewModel.content.get())) {
                ToastUtils.showShort("请填写您要发帖的内容");
                return;
            }

            if (mViewModel.items.isEmpty()) {
                ToastUtils.showShort("请添加图片或视频");
                return;
            }
            String s = mBinding.editText.generateContent();

            mViewModel.setContentJson(s);
            mViewModel.setAtUserIds(mBinding.editText.getAtUserIds());
            showLoading("");
            mViewModel.postForm();
        });

        mViewModel.liveData.observe(this, aBoolean -> {
            dismissLoading();
            ToastUtils.showShort("发布成功");
            EventBus.getDefault().post(new PostForumEvent(AccountHelper.getIdentity()));
            finish();
        });

        mBinding.editText.setListener(() -> {
            startActivityForResult(new Intent(this, AtOtherUserActivity.class), REQUEST_CODE_AT,
                    data -> {
                        FollowBean followBean = (FollowBean) data.getSerializableExtra("AtUser");
                        AtSpan atSpan = new AtSpan(getResources().getColor(R.color.color_47B6E2), followBean);
                        mBinding.editText.appendAt(atSpan);
                    }
            );
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_photo:
                showSelectMediaDialog();
                break;
        }
    }

    private void showSelectMediaDialog() {
        SelectMediaDialog optionsDialog = new SelectMediaDialog();
        int type = !mViewModel.items.isEmpty() ? mViewModel.items.get(0).getType() : 0;
        if (type == 1) {   // 1：图片 2：视频
            optionsDialog.setOptions(SelectMediaDialog.PICTURE);
        } else if (type == 2) {
            optionsDialog.setOptions(SelectMediaDialog.CAMERA);
        } else {
            optionsDialog.setOptions(0);
        }
        optionsDialog.show(getSupportFragmentManager(), SelectMediaDialog.class.getSimpleName());
        optionsDialog.setListener(options -> {
            switch (options) {
                case SelectMediaDialog.CAMERA:
                    List<String> permissions = new ArrayList<>();
                    if (!PermissionUtils.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                    if (!PermissionUtils.isGranted(Manifest.permission.RECORD_AUDIO)) {
                        permissions.add(Manifest.permission.RECORD_AUDIO);
                    }
                    if (!PermissionUtils.isGranted(Manifest.permission.CAMERA)) {
                        permissions.add(Manifest.permission.CAMERA);
                    }
                    PermissionUtils.permission(permissions.toArray(new String[permissions.size()]))
                            .callback(new PermissionUtils.SimpleCallback() {
                                @Override
                                public void onGranted() {
                                    openCamera();
                                }

                                @Override
                                public void onDenied() {

                                }
                            }).request();
                    break;
                case SelectMediaDialog.PICTURE:
                    openPicture();
                    break;
                case SelectMediaDialog.VIDEO:
                    openVideo();
                    break;
            }
        });
    }

    private void openCamera() {

        int type = 0;
        if (!mViewModel.items.isEmpty()) {
            type = mViewModel.items.get(0).getType();
        }

        if (type == 2) {  //  拍视频
            ToastUtils.showShort("最多一个视频！");
            return;
        }
        if (mViewModel.items.size() >= 9) {
            ToastUtils.showShort("最多九张图片！");
            return;
        }

        Intent intent = new Intent(this, PublishCameraActivity.class);
        intent.putExtra(RECORD_TYPE, type);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);

    }

    private void openPicture() {
        if (mViewModel.items.size() >= 9) {
            ToastUtils.showShort("最多九张图片！");
        }
        int needPicNum = 9 - mViewModel.items.size();
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setMultiMode(true);
        imagePicker.setCrop(true);
        imagePicker.setShowCamera(true);
        if (needPicNum < 0 || needPicNum > 9) {
            needPicNum = 1;
        }
        imagePicker.setSelectLimit(needPicNum);
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, REQUEST_CODE_PIC);
    }

    private void openVideo() {

        if (!mViewModel.items.isEmpty()) {
            ToastUtils.showShort("最多一个视频！");
            return;
        }
        startActivityForResult(new Intent(this, VideoGridActivity.class), REQUEST_CODE_VIDEO);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_PIC:
                List<ImageItem> items = (List<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                List<NineGridView.NineItem> list = new ArrayList<>();
                for (ImageItem item : items) {
                    list.add(new NineGridView.NineItem(item.path, 1));
                }
                adapter.addItem(list);
                break;
            case REQUEST_CODE_VIDEO:
                VideoItem videoItem = (VideoItem) data.getSerializableExtra(VIDEO_ITEM);
                adapter.addItem(new NineGridView.NineItem(videoItem.path, 2));
                break;
            case REQUEST_CODE_CAMERA:
                String videoPath = data.getStringExtra(VIDEO_PATH);
                String picPath = data.getStringExtra(PHOTO_PATH);
                if (TextUtils.isEmpty(videoPath)) {
                    adapter.addItem(new NineGridView.NineItem(picPath, 1));
                } else {
                    adapter.addItem(new NineGridView.NineItem(videoPath, 2));
                }
                break;
        }
    }
}
