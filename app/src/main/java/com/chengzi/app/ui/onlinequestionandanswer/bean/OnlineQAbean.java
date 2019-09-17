package com.chengzi.app.ui.onlinequestionandanswer.bean;

import com.chengzi.app.ui.search.bean.SearchBean;

import java.util.ArrayList;
import java.util.List;

public class OnlineQAbean {

    private List<SearchBean> history;
    private List<SearchBean> hot;

    public List<SearchBean> getHistory() {
        return history == null ? (history = new ArrayList<>()) : history;
    }

    public void setHistory(List<SearchBean> history) {
        this.history = history;
    }

    public List<SearchBean> getHot() {
        return hot == null ? (hot = new ArrayList<>()) : hot;
    }

    public void setHot(List<SearchBean> hot) {
        this.hot = hot;
    }
}
