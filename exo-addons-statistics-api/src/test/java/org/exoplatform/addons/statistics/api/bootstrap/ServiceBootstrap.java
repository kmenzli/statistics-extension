package org.exoplatform.addons.statistics.api.bootstrap;

import org.exoplatform.addons.statistics.api.services.StatisticsService;
import org.exoplatform.addons.statistics.api.web.listener.StatisticsLifecycleListener;

/**
 * Created by menzli on 29/04/14.
 */
public class ServiceBootstrap {

    private static StatisticsService statisticsService;

    public static void init () {

        statisticsService = StatisticsLifecycleListener.getInstance().getInstance(StatisticsService.class);

    }

    public static StatisticsService getStatisticsService()
    {
        return statisticsService;
    }

}
