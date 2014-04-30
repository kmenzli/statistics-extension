package org.exoplatform.addons.statistics.api.services.mongodb;

import com.mongodb.*;
import org.exoplatform.addons.statistics.api.bo.StatisticBO;
import org.exoplatform.addons.statistics.api.services.StatisticsService;
import org.exoplatform.addons.statistics.api.web.listener.StatisticsLifecycleListener;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by menzli on 21/04/14.
 */
@Named("statisticsService")
@ApplicationScoped
public class StatisticsServiceImpl implements StatisticsService {

    private static final Log LOG = ExoLogger.getLogger(StatisticsServiceImpl.class);

    private DB db() {
        return StatisticsLifecycleListener.bootstrapDB().getDB();
    }

    @Override
    public void cleanupStatistics() throws Exception {
        DBCollection coll = StatisticsLifecycleListener.bootstrapDB().getDB().getCollection(M_STATISTICSS);
        BasicDBObject query = new BasicDBObject();
//        query.put("timestamp", new BasicDBObject("$lt", System.currentTimeMillis()-24*60*60*1000));
//    query.put("isRead", true);
        DBCursor cursor = coll.find(query);
        while (cursor.hasNext())
        {
            DBObject doc = cursor.next();
            coll.remove(doc);
        }
    }

    /**
     *
     * @param criteria
     * @param scope : ALL, user, category, categoryId, type, content, link
     * @param offset
     * @param limit
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    @Override
    public List<StatisticBO> search(String criteria, String scope, int offset, int limit, String sort, String order, long timestamp) throws Exception {

        LinkedList<StatisticBO> statistics = new LinkedList<StatisticBO>();

        DBCollection coll = db().getCollection(M_STATISTICSS);

        BasicDBObject query = new BasicDBObject();

        DBCursor cursor = null;

        //TODO when the search is for content,the like operator should be used
        if (!scope.equalsIgnoreCase("ALL")) {

            query.put(scope,criteria);

        } else {

            //--- search statistics by user
            statistics.addAll(search(criteria, "user", offset, limit, sort, order, timestamp));
            if (LOG.isDebugEnabled()) {
                LOG.debug("##### [User Search Engine] load ["+statistics.size()+"] tuples ####### ");
            }

            //--- search statistics by category
            statistics.addAll(search(criteria, "category", offset, limit, sort, order, timestamp));
            if (LOG.isDebugEnabled()) {
                LOG.debug("##### [Category Search Engine] load ["+statistics.size()+"] tuples ####### ");
            }

            //--- search statistics by categoryId
            statistics.addAll(search(criteria, "categoryId", offset, limit, sort, order, timestamp));
            if (LOG.isDebugEnabled()) {
                LOG.debug("##### [CategoryId Search Engine] load ["+statistics.size()+"] tuples ####### ");
            }

            //--- search statistics by type
            statistics.addAll(search(criteria, "type", offset, limit, sort, order, timestamp));
            if (LOG.isDebugEnabled()) {
                LOG.debug("##### [Type Search Engine] load ["+statistics.size()+"] tuples ####### ");
            }

            //--- search statistics by content
            statistics.addAll(search(criteria, "content", offset, limit, sort, order, timestamp));
            if (LOG.isDebugEnabled()) {
                LOG.debug("##### [Content Search Engine] load ["+statistics.size()+"] tuples ####### ");
            }

            //--- search statistics by link
            statistics.addAll(search(criteria, "link", offset, limit, sort, order, timestamp));
            if (LOG.isDebugEnabled()) {
                LOG.debug("##### [Link Search Engine] load ["+statistics.size()+"] tuples ####### ");
            }

            return statistics;

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


        if (LOG.isDebugEnabled()) {
            LOG.debug("##### ["+statistics.size()+"] tuples fetched ####### ");
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
    public List<StatisticBO> getAllStatistics(long timestamp) throws Exception {

        LinkedList<StatisticBO> statistics = new LinkedList<StatisticBO>();

        DBCollection coll = db().getCollection(M_STATISTICSS);

        BasicDBObject query = new BasicDBObject();

        if (timestamp > 0 ) {

            query.put("timestamp0", new BasicDBObject("$lt", timestamp)); //check token not updated since 10sec + status interval (15 sec)

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
    public void exportStatistics() throws Exception {
        throw new UnsupportedOperationException( "Method not yet implemented" );
    }

    @Override
    public boolean importStatistics() throws Exception {
        throw new UnsupportedOperationException( "Method not yet implemented" );
    }
}
