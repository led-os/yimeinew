package android.support.viewpager2;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.NonNull;

/**
 * Class that can receive any lifecycle change and dispatch it to the receiver.
 * <p>
 * If a class implements both this interface and
 * {@link android.arch.lifecycle.DefaultLifecycleObserver}, then
 * methods of {@code DefaultLifecycleObserver} will be called first, and then followed by the call
 * of {@link LifecycleEventObserver#onStateChanged(LifecycleOwner, Lifecycle.Event)}
 * <p>
 * If a class implements this interface and in the same time uses {@link OnLifecycleEvent}, then
 * annotations will be ignored.
 */
public abstract class LifecycleEventObserver implements LifecycleObserver {
    /**
     * Called when a state transition event happens.
     *
     * @param source The source of the event
     * @param event The event
     */
    public abstract void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event);
}