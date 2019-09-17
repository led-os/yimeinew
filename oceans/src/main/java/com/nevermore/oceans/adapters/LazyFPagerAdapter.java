package com.nevermore.oceans.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/30 0030.
 */

public class LazyFPagerAdapter extends LazyFragmentAdapter {
    private List<Fragment> fragmentList;

    private List<String> titles = new ArrayList<>();

    public LazyFPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        fragmentList = list;
    }


    public void setFragmentList(List<Fragment> fragmentList) {
        this.fragmentList = fragmentList;
        notifyDataSetChanged();
    }

    @SafeVarargs
    public static LazyFPagerAdapter createAdapter(FragmentManager fm, Class<? extends Fragment>... fragmentClazzs) {

        if (fragmentClazzs != null && fm != null) {
            List<Fragment> fragmentList = new ArrayList<>();
            for (Class<? extends Fragment> fragmentClazz : fragmentClazzs) {
                try {
                    fragmentList.add(fragmentClazz.newInstance());

                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return new LazyFPagerAdapter(fm, fragmentList);
        }
        return null;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList == null ? null : fragmentList.get(position);
    }

    @Override
    public int getCount() {

        return fragmentList == null ? 0 : fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null && titles.size() > position) {
            return titles.get(position);
        }

        return super.getPageTitle(position);
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

}
