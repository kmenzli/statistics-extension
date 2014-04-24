package org.exoplatform.addons.statistics.api.bo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by menzli on 22/04/14.
 */
public class StatisticBO implements Serializable {

    private String user, from, type, content, link, category, categoryId;

    private Long timestamp;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String toJSON()
    {
        StringBuffer sb = new StringBuffer();

        sb.append("{");

        sb.append("\"user\": \""+this.getUser()+"\",");
        sb.append("\"type\": \""+this.getType()+"\",");
        sb.append("\"from\": \""+this.getFrom()+"\",");
        sb.append("\"category\": \""+this.getCategory()+"\",");
        sb.append("\"categoryId\": \""+this.getCategoryId()+"\",");
        sb.append("\"content\": \""+this.getContent().replaceAll("\n", "<br/>")+"\",");
        sb.append("\"link\": \""+this.getLink()+"\",");
        sb.append("\"timestamp\": "+this.getTimestamp());

        sb.append("}");

        return sb.toString();
    }

    public static String notificationstoJSON(List<StatisticBO> statisticBOs)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("\"statistics\": [");
        boolean first=true;
        for (StatisticBO statisticBO:statisticBOs) {
            if (!first) {
                sb.append(",");
            } else {
                first=false;
            }

            sb.append(statisticBO.toJSON());

        }
        sb.append("]");

        return sb.toString();
    }
}
