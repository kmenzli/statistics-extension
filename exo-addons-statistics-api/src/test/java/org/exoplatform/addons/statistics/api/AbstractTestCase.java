package org.exoplatform.addons.statistics.api;

import org.exoplatform.addons.statistics.api.bootstrap.ServiceBootstrap;
import org.exoplatform.addons.statistics.api.services.mongodb.MongoBootstrap;
import org.exoplatform.addons.statistics.api.web.listener.StatisticsLifecycleListener;
import org.exoplatform.commons.utils.PropertyManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.IOException;

/**
 * Created by menzli on 29/04/14.
 */
public class AbstractTestCase {

    private static MongoBootstrap mongoBootstrap;

    @BeforeClass
    public static void before() throws IOException {
        PropertyManager.setProperty(Constants.PROPERTY_SERVER_TYPE, "embed");
        PropertyManager.setProperty(Constants.PROPERTY_SERVER_PORT, "27777");

        StatisticsLifecycleListener.init();
        StatisticsLifecycleListener.bootstrapDB().getDB("unittest");

        StatisticsLifecycleListener.forceNew();
        ServiceBootstrap.init();

    }

    @AfterClass
    public static void teardown() throws Exception {
        StatisticsLifecycleListener.bootstrapDB().close();
    }
     private static MongoBootstrap createStorage () {

         if (mongoBootstrap!=null) {

             mongoBootstrap.close();

         }

         mongoBootstrap = new MongoBootstrap();

         return mongoBootstrap;

     }
    public MongoBootstrap getMongoBootstrap () {
        return this.mongoBootstrap;
    }

}
