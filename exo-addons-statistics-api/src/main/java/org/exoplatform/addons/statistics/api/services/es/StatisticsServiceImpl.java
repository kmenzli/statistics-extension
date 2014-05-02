package org.exoplatform.addons.statistics.api.services.es;

import org.exoplatform.addons.statistics.api.bo.StatisticBO;
import org.exoplatform.addons.statistics.api.services.StatisticsService;
import org.exoplatform.services.rest.resource.ResourceContainer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.List;

/**
 * Created by menzli on 21/04/14.
 */
@Named("statisticsService")
@ApplicationScoped
public class StatisticsServiceImpl implements StatisticsService {


    @Override
    public void cleanupStatistics(long timestamp) throws Exception {
        throw new UnsupportedOperationException( "Method not yet implemented" );
    }

    @Override
    public List<StatisticBO> search(String word, String type, int offset, int limit, int sort, int order, long timestamp) throws Exception {
        throw new UnsupportedOperationException( "Method not yet implemented" );
    }

    @Override
    public List<StatisticBO> filter(String user, String category, String categoryId, String type, boolean isPrivate, long timestamp) throws Exception {
        throw new UnsupportedOperationException( "Method not yet implemented" );
    }

    @Override
    public StatisticBO addEntry(String user, String from, String type, String category, String categoryId, String content, String link) throws Exception {
        throw new UnsupportedOperationException( "Method not yet implemented" );

    }

    @Override
    public List<StatisticBO> getStatistics(long timestamp) throws Exception {
        throw new UnsupportedOperationException( "Method not yet implemented" );
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
