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

    public StatisticBO addEntry(String user, String from, String type, String category, String categoryId, String content, String link) throws Exception;

    public List<StatisticBO> getStatisticsByUserName(String user) throws Exception;

    public List<StatisticBO> getStatisticsByCategory(String category) throws Exception;

    public List<StatisticBO> getStatisticsByType(String type) throws Exception;

    public List<StatisticBO> getAllStatistics() throws Exception;
}
