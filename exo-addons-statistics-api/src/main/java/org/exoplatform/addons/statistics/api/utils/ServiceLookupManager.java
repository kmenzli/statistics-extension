package org.exoplatform.addons.statistics.api.utils;

import org.exoplatform.addons.statistics.api.services.StatisticsService;
import org.exoplatform.addons.statistics.api.web.listener.StatisticsLifecycleListener;

import java.io.Serializable;

/**
 * Created by menzli on 24/04/14.
 */
public class ServiceLookupManager implements Serializable {

    private static StatisticsService statisticsService;

    private static ServiceLookupManager INSTANCE = null;

    private ServiceLookupManager () {

        statisticsService = StatisticsLifecycleListener.getInstance().getInstance(StatisticsService.class);

    }

    /** Holder */
    private static class ServiceLookupManagerHolder {

        /** Instance unique non préinitialisée */
        private final static ServiceLookupManager instance = new ServiceLookupManager ();
    }

    /** Point d'accès pour l'instance unique du singleton */
    public static ServiceLookupManager getInstance() {

        return ServiceLookupManagerHolder.instance;
    }

    /** Sécurité anti-désérialisation */
    private Object readResolve() {

        return INSTANCE;
    }

    public static StatisticsService getStatisticsService () {

        return statisticsService;

    }



}
