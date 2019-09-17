package android.support.viewpager2.widget;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.support.viewpager2.widget.ViewPager2.PageTransformer;

import java.util.Locale;

/**
 * Translates {@link ViewPager2.OnPageChangeCallback} events to {@link ViewPager2.PageTransformer} events.
 */
final class PageTransformerAdapter extends ViewPager2.OnPageChangeCallback {
    private final LinearLayoutManager mLayoutManager;

    private PageTransformer mPageTransformer;

    PageTransformerAdapter(LinearLayoutManager layoutManager) {
        mLayoutManager = layoutManager;
    }

    /**
     * Sets the PageTransformer. The page transformer will be called for each attached page whenever
     * the scroll position is changed.
     *
     * @param transformer The PageTransformer
     */
    void setPageTransformer(@Nullable PageTransformer transformer) {
        // TODO: add support for reverseDrawingOrder: b/112892792
        // TODO: add support for pageLayerType: b/112893074
        mPageTransformer = transformer;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mPageTransformer == null) {
            return;
        }

        float transformOffset = -positionOffset;
        for (int i = 0; i < mLayoutManager.getChildCount(); i++) {
            View view = mLayoutManager.getChildAt(i);
            if (view == null) {
                throw new IllegalStateException(String.format(Locale.US,
                        "LayoutManager returned a null child at pos %d/%d while transforming pages",
                        i, mLayoutManager.getChildCount()));
            }
            int currPos = mLayoutManager.getPosition(view);
            float viewOffset = transformOffset + (currPos - position);
            mPageTransformer.transformPage(view, viewOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
