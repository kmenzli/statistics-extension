package org.exoplatform.addons.statistics.api;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.exoplatform.addons.statistics.api.bo.StatisticBO;
import org.exoplatform.addons.statistics.api.bootstrap.ServiceBootstrap;
import org.exoplatform.addons.statistics.api.services.StatisticsService;
import org.exoplatform.addons.statistics.api.web.listener.StatisticsLifecycleListener;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.api.Assertions.*;

/**
 * Created by menzli on 29/04/14.
 */
public class StatisticsTestCase extends AbstractTestCase {

    String user = "user";
    String from = "from";
    String type = "type";
    String category = "category";
    String categoryId = "categoryId";
    String content = "content";
    String link = "link";
    boolean isRead = true;


    StatisticsService statisticsService;

    List<StatisticBO> statisticBOs = null;

    @Rule
    public ContiPerfRule i = new ContiPerfRule();

    @Before
    public void setUp() {

        statisticBOs = new ArrayList<StatisticBO>();
        StatisticsLifecycleListener.bootstrapDB().getDB().getCollection(StatisticsService.M_STATISTICSS).drop();
        statisticsService = ServiceBootstrap.getStatisticsService();

    }
    @Test
    @PerfTest(invocations = 10, threads = 1)
    public void testAddEntry() throws Exception {

        addStatistics(20,null,null,null);

        statisticBOs = statisticsService.getAllStatistics(0);

        assertThat(statisticBOs.size()).isEqualTo(20);

        statisticsService.cleanupStatistics();
 
    }

    @Test
    @PerfTest(invocations = 10, threads = 1)
    public void testCleanupStatistics() throws Exception {

        addStatistics(20,null,null,null);

        statisticsService.cleanupStatistics();

        statisticBOs = statisticsService.getAllStatistics(0);

        assertThat(statisticBOs.size()).isEqualTo(0);

        statisticsService.cleanupStatistics();


    }
    @Test
    @PerfTest(invocations = 5, threads = 1)
    public void testGetAllStatistics() throws Exception {

        addStatistics(5,null,null,null);

        statisticBOs = statisticsService.getAllStatistics(0);

        assertThat(statisticBOs.size()).isEqualTo(5);

        statisticsService.cleanupStatistics();

        addStatistics(10,null,null,null);

        statisticBOs = statisticsService.getAllStatistics(0);

        assertThat(statisticBOs.size()).isEqualTo(10);

        statisticsService.cleanupStatistics();

        addStatistics(20,null,null,null);

        statisticBOs = statisticsService.getAllStatistics(0);

        assertThat(statisticBOs.size()).isEqualTo(20);

        statisticsService.cleanupStatistics();



    }
    @Test
    public void testExportStatistics() throws Exception {

    }

    @Test
    public void testImportStatistics() throws Exception {

    }


    @Test
    public void testSearch() throws Exception {

        addStatistics(5,"demo","presales","exoplatform");

        statisticBOs = statisticsService.search("demo","user",10,10,"ASC",null,0);

        assertThat(statisticBOs.size()).isEqualTo(5);

        statisticBOs = statisticsService.search("presales","category",10,10,"ASC",null,0);

        assertThat(statisticBOs.size()).isEqualTo(5);

        statisticBOs = statisticsService.search("exoplatform","type",10,10,"ASC",null,0);

        assertThat(statisticBOs.size()).isEqualTo(5);

        statisticBOs = statisticsService.search("exoplatform","DSE",10,10,"ASC",null,0);

        assertThat(statisticBOs.size()).isEqualTo(0);

        statisticBOs = statisticsService.search("sd","type",10,10,"ASC",null,0);

        assertThat(statisticBOs.size()).isEqualTo(0);

        statisticBOs = statisticsService.search("fer","ALL",10,10,"ASC",null,0);

        assertThat(statisticBOs.size()).isEqualTo(0);


    }

    private void addStatistics (int total, String theUser, String theCategory, String theType) throws Exception {

       for (int i = 0; i < total; i++) {

            if ( theCategory != null) {

                statisticsService.addEntry(user+i, from+i, type+i, theCategory, categoryId+i, content+i, link+i);

            }

            if ( theType != null) {

                statisticsService.addEntry(user+i, from+i, theType, category+i, categoryId+i, content+i, link+i);

            }

           if ( theUser != null) {

               statisticsService.addEntry(theUser, from+i, type+i, category+i, categoryId+i, content+i, link+i);

           }


            statisticsService.addEntry(user+i, from+i, type+i , category+i, categoryId+i, content+i, link+i);

        }

    }


}
