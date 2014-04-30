package org.exoplatform.addons.statistics.api.web.listener;

/**
 * Created by menzli on 22/04/14.
 */

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.exoplatform.addons.statistics.api.module.ESModule;
import org.exoplatform.addons.statistics.api.module.JDBCModule;
import org.exoplatform.addons.statistics.api.module.MongoModule;
import org.exoplatform.addons.statistics.api.services.mongodb.MongoBootstrap;
import org.exoplatform.commons.utils.PropertyManager;

import java.util.logging.Logger;

public class StatisticsLifecycleListener implements LifecycleListener {

    private static Logger log = Logger.getLogger("StatisticsLifecycleListener");

    private static final String  DB_SERVICES_IMPLEMENTATION = "db.services.implementation";

    private static final String  DB_SERVICES_IMPLEMENTATION_MONGODB = "mongodb";

    private static final String  DB_SERVICES_IMPLEMENTATION_ES = "elasticsearch";

    private static final String  DB_SERVICES_IMPLEMENTATION_JDBC = "jdbc";

    private static String CONTAINER_START_ATTRIBUTE = "start";

    private static String implementation ="mongodb";

    private static Injector injector_;

    private static MongoBootstrap mongoBootstrap;

    public void lifecycleEvent(LifecycleEvent event) {
        if (event.getType().equals(CONTAINER_START_ATTRIBUTE)) {

            log.info("INITIALIZING GUICE");

            mongoBootstrap =  new MongoBootstrap ();

            StatisticsLifecycleListener.forceNew();

        }
    }

    public static MongoBootstrap init() {
        log.warning("ConnectionManager.forceNew has been used : this should never happen in Production!");
        if (mongoBootstrap!=null)
            mongoBootstrap.close();
        mongoBootstrap = new MongoBootstrap();
        return mongoBootstrap;
    }


    public static Injector getInstance() {
        return injector_;
    }

    public static MongoBootstrap bootstrapDB() {

        return mongoBootstrap;
    }

    public static void forceNew() {

        if (PropertyManager.getProperty(DB_SERVICES_IMPLEMENTATION) != null) {

            implementation = PropertyManager.getProperty(DB_SERVICES_IMPLEMENTATION);

        }

        if (injector_== null) {

            if (implementation.equalsIgnoreCase(DB_SERVICES_IMPLEMENTATION_MONGODB)) {

                injector_ = Guice.createInjector(new MongoModule());

            } else if (implementation.equalsIgnoreCase (DB_SERVICES_IMPLEMENTATION_ES)) {

                injector_ = Guice.createInjector(new ESModule());

            } else if (implementation.equalsIgnoreCase (DB_SERVICES_IMPLEMENTATION_JDBC)) {

                injector_ = Guice.createInjector(new JDBCModule());

            } else {

                injector_ = Guice.createInjector(new MongoModule());

            }

        }
    }

}
