package android.support.design.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import java.lang.reflect.Field;
import java.util.List;

public class ScrollingViewBehavior extends HeaderScrollingViewBehavior{

    public ScrollingViewBehavior() {
    }

    public ScrollingViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, android.support.design.R.styleable.ScrollingViewBehavior_Layout);
        this.setOverlayTop(a.getDimensionPixelSize(android.support.design.R.styleable.ScrollingViewBehavior_Layout_behavior_overlapTop, 0));
        a.recycle();
    }

    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        this.offsetChildAsNeeded(child, dependency);
        this.updateLiftedStateIfNeeded(child, dependency);
        return false;
    }

    public boolean onRequestChildRectangleOnScreen(CoordinatorLayout parent, View child, Rect rectangle, boolean immediate) {
        AppBarLayout header = this.findFirstDependency(parent.getDependencies(child));
        if (header != null) {
            rectangle.offset(child.getLeft(), child.getTop());
            Rect parentRect = this.tempRect1;
            parentRect.set(0, 0, parent.getWidth(), parent.getHeight());
            if (!parentRect.contains(rectangle)) {
                header.setExpanded(false, !immediate);
                return true;
            }
        }

        return false;
    }

    private void offsetChildAsNeeded(View child, View dependency) {
        android.support.design.widget.CoordinatorLayout.Behavior behavior = ((android.support.design.widget.CoordinatorLayout.LayoutParams)dependency.getLayoutParams()).getBehavior();
        if (behavior instanceof AppBarLayout.BaseBehavior) {
            AppBarLayout.BaseBehavior ablBehavior = (AppBarLayout.BaseBehavior)behavior;
            int offsetDelta = 0;
            try {
                Field field = AppBarLayout.BaseBehavior.class.getDeclaredField("offsetDelta");
                field.setAccessible(true);
                offsetDelta = (int) field.get(ablBehavior);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
            ViewCompat.offsetTopAndBottom(child, dependency.getBottom() - child.getTop() + offsetDelta + this.getVerticalLayoutGap() - this.getOverlapPixelsForOffset(dependency));
        }

    }

    float getOverlapRatioForOffset(View header) {
        if (header instanceof AppBarLayout) {
            AppBarLayout abl = (AppBarLayout)header;
            int totalScrollRange = abl.getTotalScrollRange();
            int preScrollDown = abl.getDownNestedPreScrollRange();
            int offset = getAppBarLayoutOffset(abl);
            if (preScrollDown != 0 && totalScrollRange + offset <= preScrollDown) {
                return 0.0F;
            }

            int availScrollRange = totalScrollRange - preScrollDown;
            if (availScrollRange != 0) {
                return 1.0F + (float)offset / (float)availScrollRange;
            }
        }

        return 0.0F;
    }

    private static int getAppBarLayoutOffset(AppBarLayout abl) {
        android.support.design.widget.CoordinatorLayout.Behavior behavior = ((android.support.design.widget.CoordinatorLayout.LayoutParams)abl.getLayoutParams()).getBehavior();
        return behavior instanceof AppBarLayout.BaseBehavior ? ((AppBarLayout.BaseBehavior)behavior).getTopBottomOffsetForScrollingSibling() : 0;
    }

    AppBarLayout findFirstDependency(List<View> views) {
        int i = 0;

        for(int z = views.size(); i < z; ++i) {
            View view = (View)views.get(i);
            if (view instanceof AppBarLayout) {
                return (AppBarLayout)view;
            }
        }

        return null;
    }

    int getScrollRange(View v) {
        return v instanceof AppBarLayout ? ((AppBarLayout)v).getTotalScrollRange() : super.getScrollRange(v);
//        return v instanceof AppBarLayout ? ((AppBarLayout)v).getTotalScrollRange() : 0;
    }

    private void updateLiftedStateIfNeeded(View child, View dependency) {
        if (dependency instanceof AppBarLayout) {
            AppBarLayout appBarLayout = (AppBarLayout)dependency;
            if (appBarLayout.isLiftOnScroll()) {
                appBarLayout.setLiftedState(child.getScrollY() > 0);
            }
        }

    }

}
