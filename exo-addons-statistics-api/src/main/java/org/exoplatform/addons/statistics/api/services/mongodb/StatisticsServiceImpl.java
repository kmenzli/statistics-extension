package org.exoplatform.addons.statistics.api.services.mongodb;

import com.mongodb.*;
import org.exoplatform.addons.statistics.api.bo.StatisticBO;
import org.exoplatform.addons.statistics.api.exception.StatisticsException;
import org.exoplatform.addons.statistics.api.services.StatisticsService;
import org.exoplatform.addons.statistics.api.utils.Util;
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
    public List<StatisticBO> search(String criteria, String scope, int offset, int limit, String sort, String order) throws Exception {

        List<StatisticBO> statistics = new ArrayList<StatisticBO>();

        DBCollection coll = db().getCollection(M_STATISTICSS);

        BasicDBObject query = new BasicDBObject();

        DBCursor cursor = null;

        //TODO when the search is for content,the like operator should be used
        if (!scope.equalsIgnoreCase("ALL")) {
            query.put(scope,criteria);
        }


        cursor = coll.find(query);

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



    @Override
    public List<StatisticBO> getAllStatistics() throws Exception {
        return search(null, "ALL", 0 , 0, null, null);

    }

    @Override
    public void exportStatistics() throws Exception {
        throw new UnsupportedOperationException( "Method not yet implemented" );
    }

    @Override
    public boolean importStatistics() throws Exception {
        throw new UnsupportedOperationException( "Method not yet implemented" );
    }
}
