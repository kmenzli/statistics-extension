package org.exoplatform.addons.statistics.api.services.hibernate;

import org.exoplatform.addons.statistics.api.bo.StatisticBO;
import org.exoplatform.addons.statistics.api.services.StatisticsService;
import org.exoplatform.services.rest.resource.ResourceContainer;

import java.util.List;

/**
 * Created by menzli on 21/04/14.
 */
public class StatisticsServiceImpl implements StatisticsService {

    @Override
    public void cleanupStatistics() throws Exception {
        throw new UnsupportedOperationException( "Method not yet implemented" );
    }

    @Override
    public StatisticBO addEntry(String user, String from, String type, String category, String categoryId, String content, String link) throws Exception {
        throw new UnsupportedOperationException( "Method not yet implemented" );

    }

    @Override
    public List<StatisticBO> getStatisticsByUserName(String user) throws Exception {
        throw new UnsupportedOperationException( "Method not yet implemented" );
    }

    @Override
    public List<StatisticBO> getStatisticsByCategory(String category) throws Exception {
        throw new UnsupportedOperationException( "Method not yet implemented" );
    }

    @Override
    public List<StatisticBO> getStatisticsByType(String type) throws Exception {
        throw new UnsupportedOperationException( "Method not yet implemented" );
    }

    @Override
    public List<StatisticBO> getAllStatistics() throws Exception {
        throw new UnsupportedOperationException( "Method not yet implemented" );
    }
}
