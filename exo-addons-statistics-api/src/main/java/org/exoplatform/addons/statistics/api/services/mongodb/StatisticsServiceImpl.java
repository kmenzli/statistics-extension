package org.exoplatform.addons.statistics.api.services.mongodb;

import com.mongodb.*;
import org.exoplatform.addons.statistics.api.bo.StatisticBO;
import org.exoplatform.addons.statistics.api.services.StatisticsService;
import org.exoplatform.addons.statistics.api.web.listener.StatisticsLifecycleListener;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by menzli on 21/04/14.
 */
@Named("statisticsService")
@ApplicationScoped
public class StatisticsServiceImpl implements StatisticsService {
    private DB db() {
        return StatisticsLifecycleListener.bootstrapDB().getDB();
    }

    @Override
    public void cleanupStatistics() throws Exception {
        DBCollection coll = StatisticsLifecycleListener.bootstrapDB().getDB().getCollection(M_STATISTICSS);
        BasicDBObject query = new BasicDBObject();
        query.put("timestamp", new BasicDBObject("$lt", System.currentTimeMillis()-24*60*60*1000));
//    query.put("isRead", true);
        DBCursor cursor = coll.find(query);
        while (cursor.hasNext())
        {
            DBObject doc = cursor.next();
            coll.remove(doc);
        }
    }

    @Override
    public StatisticBO addEntry(String user, String from, String type, String category, String categoryId, String content, String link) {
        DBCollection coll = db().getCollection(M_STATISTICSS);
        BasicDBObject doc = new BasicDBObject();
        doc.put("timestamp", System.currentTimeMillis());
        doc.put("user", user);
        doc.put("from", from);
        doc.put("type", type);
        doc.put("category", category);
        doc.put("categoryId", categoryId);
        doc.put("content", content);
        doc.put("link", link);
        doc.put("isRead", false);

        coll.insert(doc);

        return null;
    }


    public List<StatisticBO> getStatisticsByCriteria(String username, String category, String type, boolean cached) throws Exception {

        List<StatisticBO> statistics = new ArrayList<StatisticBO>();

        DBCollection coll = db().getCollection(M_STATISTICSS);

        BasicDBObject query = new BasicDBObject();

        if (username != null) {

            query.put("user", username);

        }
        if (category != null) {

            query.put("category", category);

        }
        if (type != null) {

            query.put("category", type);

        }

        DBCursor cursor = coll.find(query);

        while (cursor.hasNext()) {

            DBObject doc = cursor.next();
            StatisticBO statisticBO = new StatisticBO();
            statisticBO.setTimestamp((Long)doc.get("timestamp"));
            statisticBO.setUser(doc.get("user").toString());
            if (doc.containsField("from")) {
                statisticBO.setFrom(doc.get("from").toString());
            }
            statisticBO.setCategory(doc.get("category").toString());
            statisticBO.setCategoryId(doc.get("categoryId").toString());
            statisticBO.setType(doc.get("type").toString());
            statisticBO.setContent(doc.get("content").toString());
            statisticBO.setLink(doc.get("link").toString());

            statistics.add(statisticBO);

        }

        return statistics;

    }

    @Override
    public List<StatisticBO> getStatisticsByUserName(String username) throws Exception {

       return getStatisticsByCriteria(username, null, null,false);
    }

    @Override
    public List<StatisticBO> getStatisticsByCategory(String category) throws Exception {
        return getStatisticsByCriteria(null, category, null, false);
    }

    @Override
    public List<StatisticBO> getStatisticsByType(String type) throws Exception {
        return getStatisticsByCriteria(null, null, type, false);

    }

    @Override
    public List<StatisticBO> getAllStatistics() throws Exception {
        return getStatisticsByCriteria(null, null, null, false);

    }
}
