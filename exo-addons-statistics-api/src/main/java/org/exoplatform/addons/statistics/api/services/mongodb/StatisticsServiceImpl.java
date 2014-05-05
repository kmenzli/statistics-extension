package org.exoplatform.addons.statistics.api.services.mongodb;

import com.mongodb.*;
import org.exoplatform.addons.statistics.api.bo.StatisticBO;
import org.exoplatform.addons.statistics.api.services.StatisticsService;
import org.exoplatform.addons.statistics.api.web.listener.StatisticsLifecycleListener;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.rmi.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import java.sql.Timestamp;

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
    public void cleanupStatistics(long timestamp) throws Exception {

        DBCollection coll = StatisticsLifecycleListener.bootstrapDB().getDB().getCollection(M_STATISTICSS);

        DBCursor cursor = null;

        BasicDBObject query = new BasicDBObject();

        if (timestamp > 0) {

            query.put("timestamp", new BasicDBObject("$lt", timestamp));

        }

        try {

            cursor = coll.find(query);

            while (cursor.hasNext())
            {
                DBObject doc = cursor.next();

                coll.remove(doc);
            }
        } finally {

            cursor.close();

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
    public List<StatisticBO> search(String criteria, String scope, int offset, int limit, int sort, int order, long timestamp) throws Exception {

        LinkedList<StatisticBO> statistics = new LinkedList<StatisticBO>();

        DBCollection coll = db().getCollection(M_STATISTICSS);

        BasicDBObject sortQuery = new BasicDBObject("_id", sort);

        //BasicDBObject orderQuery = new BasicDBObject("_id", order);

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

        cursor = coll.find(query).limit(limit).sort(sortQuery);

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
            statisticBO.setSite(doc.get("site").toString());
            statisticBO.setSiteType(doc.get("siteType").toString());

            statistics.add(statisticBO);

        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("##### ["+statistics.size()+"] tuples fetched ####### ");
        }

        return statistics;
    }

    @Override
    public List<StatisticBO> filter(String user, String category, String categoryId, String type, String site, String siteType, String content, boolean isPrivate, long timestamp) throws Exception {

        LinkedList<StatisticBO> statistics = new LinkedList<StatisticBO>();

        DBCollection collection = db().getCollection(M_STATISTICSS);

        BasicDBObject query = new BasicDBObject("isPrivate", isPrivate);

        //TODO : replace if blocs by a design pattern
        if (content != null) {

            query.put("content", new BasicDBObject("$regex", content));

        }
        if (user != null) {

            query.put("user",user);

        }

        if (category != null) {

            query.put("category",category);

        }

        if (categoryId != null) {

            query.put("categoryId",categoryId);

        }

        if (type != null) {

            query.put("type",type);

        }

        if (timestamp > 0) {

            query.put("timestamp",new BasicDBObject("$gt", timestamp));

        }

        if ( site!= null ) {

            query.put("site",site);

        }

        if (siteType != null) {

            query.put("siteType",siteType);

        }

        //--- invoke the find method
        DBCursor cursor = collection.find(query);

        try {
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
                statisticBO.setSite(doc.get("site").toString());
                statisticBO.setSiteType(doc.get("siteType").toString());

                //--- Add the statistic tupe to the collection
                statistics.add(statisticBO);
            }
         
        } catch (Exception E) {

            LOG.error("Connexion impossible : " + E.getMessage(), E);

        } finally {

            //--- close the cursor after each invocation
            cursor.close();
        }

        return statistics;

    }

    @Override
    public StatisticBO addEntry(String user, String from, String type, String category, String categoryId, String content, String link, String site, String siteType) {
        DBCollection coll = db().getCollection(M_STATISTICSS);
        BasicDBObject doc = new BasicDBObject();
        doc.put("timestamp", System.currentTimeMillis());
        doc.put("user", user);
        doc.put("from", from);
        doc.put("type", type);
        doc.put("category", category);
        doc.put("categoryId", categoryId);
        doc.put("site", site);
        doc.put("siteType", siteType);
        doc.put("content", content);
        doc.put("link", link);
        doc.put("isPrivate", false);

        //--- add new line to the dababase (what's the difference with save() method)
        coll.insert(doc);

        return null;
    }

    @Override
    public List<StatisticBO> getStatistics(long timestamp) throws Exception {

        LinkedList<StatisticBO> statistics = new LinkedList<StatisticBO>();

        DBCollection coll = db().getCollection(M_STATISTICSS);

        DBCursor cursor = null;

        BasicDBObject query = new BasicDBObject();

        if (timestamp > 0 ) {

            //--- get only tuples which timestap >= {{timestamp}}
            query.put("timestamp", new BasicDBObject("$gte", timestamp));

        }
        try {

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
                statisticBO.setSite(doc.get("site").toString());
                statisticBO.setSiteType(doc.get("siteType").toString());

                //--- Add the statistic tupe to the collection
                statistics.add(statisticBO);

            }
            return statistics;
        } finally {

            cursor.close();

        }

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
