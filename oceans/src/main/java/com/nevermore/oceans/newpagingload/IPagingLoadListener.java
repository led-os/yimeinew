package com.nevermore.oceans.newpagingload;

/**
 * 分页加载监听，包括刷新和加载更多，
 * currentPage=1是刷新，否则是加载更多
 * @see #onPageChange(int, int) 更简洁
 *
 * @author XuNeverMore
 * @QQ 1045530120
 * @create on 2018/2/7 0007
 * @github https://github.com/XuNeverMore
 */
public interface IPagingLoadListener {

    void onPageChange(int currentPage, int pageSize);

}
