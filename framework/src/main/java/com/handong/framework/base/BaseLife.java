package com.handong.framework.base;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by Administrator on 2018/2/5 0005.
 */

public class BaseLife implements Consumer<LifecycleEvent>{

    private final Disposable disposable;

    public BaseLife() {
        disposable = subject.subscribe(this);
    }

    /**
     * 这是将RxJava流与Activity/Fragment的生命周期绑定的办法
     */
    protected BehaviorSubject<LifecycleEvent> subject = BehaviorSubject.create();

    public <T> ObservableTransformer<T, T> bindToLifecycle() {
        return upstream -> upstream.takeUntil(subject.skipWhile(event ->
                event != LifecycleEvent.DESTROY && event != LifecycleEvent.DETACH));
    }

    /**
     * 统一线程切换
     *
     * @param <T> 被观察者
     * @return 切换线程后的被观察者
     */
    public static <T> ObservableTransformer<T, T> getObservableScheduler() {

        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    @Override
    public void accept(LifecycleEvent lifecycleEvent) throws Exception {
        if (lifecycleEvent.compareTo(LifecycleEvent.CREATE) < 0) {
            if (disposable != null) {
                disposable.dispose();
            }
        }

    }

    public void destory(){

    }
}
