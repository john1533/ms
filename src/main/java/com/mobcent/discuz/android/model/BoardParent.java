package com.mobcent.discuz.android.model;

import java.util.List;

public class BoardParent extends BaseModel {
    private static final long serialVersionUID = 1;
    private long boardCategoryId;
    private String boardCategoryName;
    private int boardCategoryType;
    private List<BoardChild> childList;

    public long getBoardCategoryId() {
        return this.boardCategoryId;
    }

    public void setBoardCategoryId(long boardCategoryId) {
        this.boardCategoryId = boardCategoryId;
    }

    public String getBoardCategoryName() {
        return this.boardCategoryName;
    }

    public void setBoardCategoryName(String boardCategoryName) {
        this.boardCategoryName = boardCategoryName;
    }

    public int getBoardCategoryType() {
        return this.boardCategoryType;
    }

    public void setBoardCategoryType(int boardCategoryType) {
        this.boardCategoryType = boardCategoryType;
    }

    public List<BoardChild> getChildList() {
        return this.childList;
    }

    public void setChildList(List<BoardChild> childList) {
        this.childList = childList;
    }
}
