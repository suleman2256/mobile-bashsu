package ru.bsu.application.dto;

public class RecyclerItemNotification {

    private String title;
    private String description;
    private int visible;

    public RecyclerItemNotification(String title, String description, Integer visible) {
        this.title = title;
        this.description = description;
        this.visible = visible;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getVisible() {
        return visible;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
    }
}
