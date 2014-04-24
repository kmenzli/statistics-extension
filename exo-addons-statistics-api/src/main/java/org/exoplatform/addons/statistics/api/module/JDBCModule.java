package org.exoplatform.addons.statistics.api.module;

import com.google.inject.AbstractModule;
import org.exoplatform.addons.statistics.api.services.StatisticsService;
import org.exoplatform.addons.statistics.api.services.jdbc.StatisticsServiceImpl;

/**
 * Created by menzli on 22/04/14.
 */
public class JDBCModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(StatisticsService.class).to(StatisticsServiceImpl.class);
    }
}
