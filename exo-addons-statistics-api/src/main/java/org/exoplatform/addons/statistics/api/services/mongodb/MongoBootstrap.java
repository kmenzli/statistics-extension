package org.exoplatform.addons.statistics.api.services.mongodb;

import com.mongodb.*;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.exoplatform.addons.statistics.api.Constants;
import org.exoplatform.addons.statistics.api.services.IBootstrap;
import org.exoplatform.commons.utils.PropertyManager;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by menzli on 22/04/14.
 */
public class MongoBootstrap implements IBootstrap {

    private static MongodExecutable mongodExe;
    private static MongodProcess mongod;
    private Mongo m;
    private DB db;
    private static final Log LOG = ExoLogger.getLogger(MongoBootstrap.class);


    public DB getDB() {
        return getDB(null);
    }

    public DB getDB(String dbName) {
        if (db==null || dbName!=null)
        {
            if (dbName!=null)
                db = mongo().getDB(dbName);
            else
                db = mongo().getDB(PropertyManager.getProperty(Constants.PROPERTY_DB_NAME));
            boolean authenticate = "true".equals(PropertyManager.getProperty(Constants.PROPERTY_DB_AUTHENTICATION));
            if (authenticate)
            {
                db.authenticate(PropertyManager.getProperty(Constants.PROPERTY_DB_USER), PropertyManager.getProperty(Constants.PROPERTY_DB_PASSWORD).toCharArray());
            }
            initCollection("notifications");
            dropTokenCollectionIfExists();

        }
        return db;
    }

    private Mongo mongo() {
        if (m==null) {
            try {
                if (Constants.PROPERTY_SERVER_TYPE_EMBED.equals(PropertyManager.getProperty(Constants.PROPERTY_SERVER_TYPE))) {
                    LOG.warn("WE WILL NOW USE MONGODB IN EMBED MODE...");
                    LOG.warn("BE AWARE...");
                    LOG.warn("EMBED MODE SHOULD NEVER BE USED IN PRODUCTION!");
                    setupEmbedMongo();
                }

                MongoOptions options = new MongoOptions();
                options.connectionsPerHost = 200;
                options.connectTimeout = 60000;
                options.threadsAllowedToBlockForConnectionMultiplier = 10;
                options.autoConnectRetry = true;
                String host = PropertyManager.getProperty(Constants.PROPERTY_SERVER_HOST);
                int port = Integer.parseInt(PropertyManager.getProperty(Constants.PROPERTY_SERVER_PORT));
                m = new Mongo(new ServerAddress(host, port), options);
                m.setWriteConcern(WriteConcern.SAFE);
            } catch (UnknownHostException e) {
            } catch (IOException e) {
            }
        }

        return m;
    }

    private void initCollection(String name) {
        initCollection(name, false, 0);
    }

    private void initCollection(String name, boolean isCapped, int size) {
        if (getDB().collectionExists(name)) return;

        BasicDBObject doc = new BasicDBObject();
        doc.put("capped", isCapped);
        if (isCapped)
            doc.put("size", size);
        getDB().createCollection(name, doc);

    }

    private void dropTokenCollectionIfExists() {
        if (getDB().collectionExists("tokens")) {
            DBCollection tokens = getDB().getCollection("tokens");
            tokens.drop();
        }
    }

    private static Mongo setupEmbedMongo() throws IOException {
        MongodStarter runtime = MongodStarter.getDefaultInstance();
        int port = Integer.parseInt(PropertyManager.getProperty(Constants.PROPERTY_SERVER_PORT));
        mongodExe = runtime.prepare(new MongodConfig(Version.V2_3_0, port, Network.localhostIsIPv6()));
        mongod = mongodExe.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage());
        }
        String host = PropertyManager.getProperty(Constants.PROPERTY_SERVER_HOST);

        return new Mongo(new ServerAddress(host, port));
    }

}
