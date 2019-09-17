package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;

public class HDViewSwitcher extends ViewAnimator {

    /**
     * The factory used to create the two children.
     */
    ViewFactory mFactory;

    public HDViewSwitcher(Context context) {
        super(context);
    }

    public HDViewSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException if this switcher already contains two children
     */
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return ViewSwitcher.class.getName();
    }

    /**
     * Returns the next view to be displayed.
     *
     * @return the view that will be displayed after the next views flip.
     */
    public View getNextView() {
        int which = getDisplayedChild() == 0 ? 1 : 0;
        return getChildAt(which);
    }

    private View obtainView() {
        View child = mFactory.makeView();
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        if (lp == null) {
            lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }
        addView(child, lp);
        return child;
    }

    /**
     * Sets the factory used to create the two views between which the
     * ViewSwitcher will flip. Instead of using a factory, you can call
     * {@link #addView(View, int, ViewGroup.LayoutParams)}
     * twice.
     *
     * @param factory the view factory used to generate the switcher's content
     */
    public void setFactory(ViewFactory factory) {
        mFactory = factory;
        obtainView();
        obtainView();
    }

    /**
     * Reset the ViewSwitcher to hide all of the existing views and to make it
     * think that the first time animation has not yet played.
     */
    public void reset() {
        try {
            Field mFirstTime = getClass().getSuperclass().getDeclaredField("mFirstTime");
            mFirstTime.setAccessible(true);
            mFirstTime.set(this,true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
//        mFirstTime = true;
        View v;
        v = getChildAt(0);
        if (v != null) {
            v.setVisibility(View.GONE);
        }
        v = getChildAt(1);
        if (v != null) {
            v.setVisibility(View.GONE);
        }
    }

    /**
     * Creates views in a ViewSwitcher.
     */
    public interface ViewFactory {
        /**
         * Creates a new {@link View} to be added in a
         * {@link ViewSwitcher}.
         *
         * @return a {@link View}
         */
        View makeView();
    }
}
