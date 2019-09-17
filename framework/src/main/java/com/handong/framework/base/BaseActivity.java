package com.handong.framework.base;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.WindowManager;
import android.widget.Toast;

import com.blankj.utilcode.util.ScreenUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.dialog.TokenDialog;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Administrator on 2017/10/11 0011.
 */

public abstract class BaseActivity<T extends ViewDataBinding, VM extends BaseViewModel> extends AppCompatActivity
        implements DataBindingProvider {

    public T mBinding;
    public VM mViewModel;
    private QMUITipDialog tipDialog;
    public int wdith;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ScreenUtils.adaptScreen4VerticalSlide(this, 420);

        mBinding = DataBindingUtil.setContentView(this, getLayoutRes());


//        StatusBarUtil.setLightMode(this);
//        mBinding.getRoot().setFitsSystemWindows(true);
        initViewModel();

        if (mViewModel != null) {
            mViewModel.tokenExpir.observe(this, message -> {
                AccountHelper.logout();

                TokenDialog tokenDialog = new TokenDialog();
                tokenDialog.setMessage(message);
                tokenDialog.setConfirmListener(v -> {
                    startLogin();
                });
                tokenDialog.show(getSupportFragmentManager(), "tokenExpir");
            });

            mViewModel.multipleDevice.observe(this, message -> {
                AccountHelper.logout();
                TokenDialog tokenDialog = new TokenDialog();
                tokenDialog.setMessage(message);
                tokenDialog.setConfirmListener(v -> {
                    startLogin();
                });
                tokenDialog.show(getSupportFragmentManager(), "multipleDevice");
            });

            mViewModel.accountFrozen.observe(this, message -> {
                AccountHelper.logout();
                TokenDialog tokenDialog = new TokenDialog();
                tokenDialog.setMessage(message);
                tokenDialog.setConfirmListener(v -> {
                    startLogin();
                });
                tokenDialog.show(getSupportFragmentManager(), "accountFrozen");
            });

            mViewModel.otherFrozen.observe(this, message -> {
                TokenDialog tokenDialog = new TokenDialog();
                tokenDialog.setMessage(message);
                tokenDialog.setConfirmListener(v -> {
                    finish();
                });
                tokenDialog.show(getSupportFragmentManager(), "otherFrozen");
            });

            mViewModel.error.observe(this, a -> {
                dismissLoading();
            });
        }
        initView(savedInstanceState);
        initWindow();
    }

    private void startLogin() {
        Intent intent = new Intent(getPackageName() + ".com.action.login");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            if (!isTaskRoot()) {
                finish();
            }
        }

    }

//    @Override
//    public Resources getResources() {
//        ScreenUtils.adaptScreen4VerticalSlide(this, 420);
//        return super.getResources();
//    }

    private void initWindow() {
        WindowManager wm = getWindowManager();
        wdith = wm.getDefaultDisplay().getWidth();
    }

    @Override
    public void setContentView(int res) {
        super.setContentView(res);
//        AndroidBug5497Workaround.assistActivity(this);    //兼容Edittext弹不起来
    }

    protected void initViewModel() {
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
            Type argument = actualTypeArguments[1];
            mViewModel = ViewModelProviders.of(this).get((Class<VM>) argument);
        } else {
            mViewModel = (VM) ViewModelProviders.of(this).get(BaseViewModel.class);
//            throw new RuntimeException("error type : " + type.getClass().getCanonicalName());
        }
    }

    public void showLoading(CharSequence msg) {
        tipDialog = new QMUITipDialog.Builder(this)
                .setTipWord(msg)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create(true);
        tipDialog.show();
    }

    public void dismissLoading() {
        if (tipDialog != null) {
            tipDialog.dismiss();
            tipDialog = null;
        }
    }

    protected void goActivity(Class<? extends Activity> clazz) {
        startActivity(new Intent(this, clazz));
    }

    public void toast(String message) {
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private SparseArray<ResultCallback> callbackArray = new SparseArray<>();

    public void startActivityForResult(Intent intent, int requestCode, ResultCallback callback) {
        if (callback == null) {
            throw new RuntimeException("callback is null");
        }
        callbackArray.put(requestCode, callback);
        startActivityForResult(intent, requestCode);
    }

    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options, ResultCallback callback) {
        if (callback == null) {
            throw new RuntimeException("callback is null");
        }
        callbackArray.put(requestCode, callback);
        startActivityForResult(intent, requestCode, options);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        ResultCallback resultCallback = callbackArray.get(requestCode);
        callbackArray.remove(requestCode);
        if (resultCallback != null) {
            resultCallback.onResult(data);
        }
    }

    protected void onError() {

    }
}
