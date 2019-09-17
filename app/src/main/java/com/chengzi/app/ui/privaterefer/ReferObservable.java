package com.chengzi.app.ui.privaterefer;

import android.arch.lifecycle.GenericLifecycleObserver;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static android.arch.lifecycle.Lifecycle.State.DESTROYED;

public class ReferObservable {

    public static void interval(LifecycleOwner owner, Runnable runnable) {

        ReferObserver observer = new ReferObserver(runnable);
        owner.getLifecycle().addObserver(observer);
        Observable.interval(0, 1, TimeUnit.SECONDS)
//                .takeWhile(new Predicate<Long>() {
//                    @Override
//                    public boolean test(Long aLong) throws Exception {
//                        Lifecycle.State currentState = owner.getLifecycle().getCurrentState();
//                        Log.d("aaa", "test: "+currentState.name()+"   "+Lifecycle.State.CREATED.name());
//                        boolean atLeast = owner.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED);
//                        Log.d("aaa", "test: " + atLeast);
//                        return atLeast;
//                    }
//                })
                .subscribe(observer);
    }

    private static class ReferObserver<T> implements Observer<T>, GenericLifecycleObserver {

        private Disposable d;
        private Runnable action;

        public ReferObserver(Runnable action) {
            this.action = action;
        }

        @Override
        public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
            boolean b = source.getLifecycle().getCurrentState() == DESTROYED;
            if (source.getLifecycle().getCurrentState() == DESTROYED) {
                source.getLifecycle().removeObserver(this);
                if (d != null && !d.isDisposed()) {
                    d.dispose();
                }
            }
        }

        @Override
        public void onSubscribe(Disposable d) {
            this.d = d;
        }

        @Override
        public void onNext(T o) {
            if (action != null) {
                action.run();
            }
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    }

}
