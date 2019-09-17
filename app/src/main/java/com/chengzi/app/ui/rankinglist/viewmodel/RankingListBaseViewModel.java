package com.chengzi.app.ui.rankinglist.viewmodel;

import com.handong.framework.api.Api;
import com.chengzi.app.ui.cases.viewmodel.CaseBaseViewModel;
import com.chengzi.app.ui.rankinglist.model.RankingService;

public class RankingListBaseViewModel extends CaseBaseViewModel {

    protected final RankingService rankingService;

    private String categoryId;
    private String categoryName;

    public RankingListBaseViewModel() {
        rankingService = Api.getApiService(RankingService.class);
    }


    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
