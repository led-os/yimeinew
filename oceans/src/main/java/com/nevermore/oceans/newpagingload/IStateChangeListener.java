package com.nevermore.oceans.newpagingload;

/**
 * Created by Administrator on 2018/1/29 0029.
 */

public interface IStateChangeListener {


    void showNetError(CharSequence message);

    void showNoData(CharSequence message);

    void showContent();

}
