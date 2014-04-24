package org.exoplatform.addons.statistics.bean;

/**
 * Created by menzli on 16/04/14.
 */
public class StatisticBean {
    private String id;
    private String category;
    private String categoryId;
    private String username;
    private String content;
    private String url;
    private boolean active;

    public StatisticBean() {
    }

    public StatisticBean(String id, String category, String categoryId, String username, String content, String url, boolean active) {
        this.id = id;
        this.category = category;
        this.categoryId = categoryId;
        this.username = username;
        this.content = content;
        this.url = url;
        this.active = active;
    }

    public StatisticBean(String category, String categoryId, String username, String content, String url, boolean active) {
        this(null,category,  categoryId,  username,  content,  url,  active);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
