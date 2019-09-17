package android.support.viewpager2.adapter;


import android.support.v4.view.ViewCompat;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.support.v7.widget.RecyclerView.*;


/**
 * {@link ViewHolder} implementation for handling {@link }s. Used in
 * {@link FragmentStateAdapter}.
 */
public final class FragmentViewHolder extends ViewHolder {
    private FragmentViewHolder(FrameLayout container) {
        super(container);
    }

    static FragmentViewHolder create(ViewGroup parent) {
        FrameLayout container = new FrameLayout(parent.getContext());
        container.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        container.setId(ViewCompat.generateViewId());
        container.setSaveEnabled(false);
        return new FragmentViewHolder(container);
    }

    FrameLayout getContainer() {
        return (FrameLayout) itemView;
    }
}
