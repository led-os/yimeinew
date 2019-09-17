package com.handong.framework.base;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


public abstract class BaseFragment<T extends ViewDataBinding, VM extends BaseViewModel> extends Fragment implements DataBindingProvider {

    protected T binding;
    protected VM viewModel;
    private QMUITipDialog tipDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = initViewModel();
        getLifecycle().addObserver(viewModel);

        initView(savedInstanceState);

        BaseViewModel parentViewModel = ((BaseActivity) getActivity()).mViewModel;
        if (parentViewModel != viewModel && viewModel != null) {

            viewModel.tokenExpir.observe(this, message -> {
                parentViewModel.tokenExpir.postValue(message);
            });
            viewModel.multipleDevice.observe(this, message -> {
                parentViewModel.multipleDevice.postValue(message);
            });
            viewModel.accountFrozen.observe(this, message -> {
                parentViewModel.accountFrozen.postValue(message);
            });
            viewModel.otherFrozen.observe(this,message -> {
                parentViewModel.otherFrozen.postValue(message);
            });
            viewModel.error.observe(this, a -> {
                dismissLoading();
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //TODO:友盟
//        MobclickAgent.onPageStart(getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        //TODO:友盟
//        MobclickAgent.onPageEnd(getClass().getSimpleName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getLifecycle().removeObserver(viewModel);
    }

    public void showLoading(CharSequence msg) {
        tipDialog = new QMUITipDialog.Builder(getActivity())
                .setTipWord(msg)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .create(true);
        tipDialog.show();
    }

    public void dismissLoading() {
        if (tipDialog != null) {
            tipDialog.dismiss();
        }
    }


    protected VM initViewModel() {
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
            Type argument = actualTypeArguments[1];
            if (initalizeViewModelFromActivity()) {
                viewModel = ViewModelProviders.of(getActivity()).get((Class<VM>) argument);
            } else {
                viewModel = ViewModelProviders.of(this).get((Class<VM>) argument);
            }
        } else {
            viewModel = (VM) new BaseViewModel();
        }
        return viewModel;
    }

    protected boolean initalizeViewModelFromActivity() {
        return false;
    }

    protected void goActivity(Class<? extends Activity> clazz) {
        startActivity(new Intent(getContext(), clazz));
    }

    public void toast(String message) {
        Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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

}