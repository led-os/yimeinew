package com.chengzi.app.event;

public class NormalCategoryEvent {

    private String categoryId;

    public NormalCategoryEvent(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }
}
