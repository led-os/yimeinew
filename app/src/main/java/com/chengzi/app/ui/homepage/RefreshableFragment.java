package com.chengzi.app.ui.homepage;

import android.content.Context;
import android.databinding.ViewDataBinding;

import com.handong.framework.base.BaseFragment;
import com.handong.framework.base.BaseViewModel;

public abstract class RefreshableFragment<T extends ViewDataBinding, VM extends BaseViewModel>
        extends BaseFragment<T, VM> implements OnRefreshListener {

    protected boolean refreshComplete = true;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() != null && (getParentFragment() instanceof RefreshListenerRegistry)) {
            ((RefreshListenerRegistry) getParentFragment()).register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getParentFragment() != null && (getParentFragment() instanceof RefreshListenerRegistry)) {
            ((RefreshListenerRegistry) getParentFragment()).unRegister(this);
        }
    }

    public void finishRefresh() {
        if (getParentFragment() != null) {
            if (!(getParentFragment() instanceof RefreshListenerRegistry)) {
                throw new IllegalStateException();
            }
            ((RefreshListenerRegistry) getParentFragment()).finishRefresh();
        }
    }

    @Override
    public boolean isRefreshFinished() {
        return refreshComplete;
    }

}
