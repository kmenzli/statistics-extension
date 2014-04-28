package org.exoplatform.addons.statistics.api.services;

import org.exoplatform.addons.statistics.api.bo.StatisticBO;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by menzli on 21/04/14.
 */
public interface StatisticsService {

    public static final String M_STATISTICSS = "statistics";

    public void cleanupStatistics() throws Exception;

    public List<StatisticBO> search(String word, String type, int offset, int limit, String sort, String order) throws Exception;

    public StatisticBO addEntry(String user, String from, String type, String category, String categoryId, String content, String link) throws Exception;

    public List<StatisticBO> getAllStatistics() throws Exception;

    public void exportStatistics() throws Exception;

    public boolean importStatistics() throws Exception;
}
