package org.exoplatform.addons.statistics.portlet.statistics;

import juzu.*;
import juzu.template.Template;
import org.exoplatform.addons.statistics.api.services.StatisticsService;
import org.exoplatform.addons.statistics.api.web.listener.StatisticsLifecycleListener;
import org.exoplatform.addons.statistics.bean.StatisticBean;
import org.exoplatform.commons.juzu.ajax.Ajax;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * Created by menzli on 10/04/14.
 */
@SessionScoped
public class StatisticsApplication {

    private static Log log = ExoLogger.getLogger(StatisticsApplication.class);

    StatisticsService statisticsService;

    public StatisticsApplication () {

        statisticsService = StatisticsLifecycleListener.getInstance().getInstance(StatisticsService.class);

    }

    @Inject
    @Path("index.gtmpl")
    Template index;

    @View
    public void index() {
        index("marseille");
    }

    @View
    public void index(String location) {
        index.render();
    }

    @Action
    public Response add(String location) {
        return StatisticsApplication_.index(location);
    }
    @Ajax
    @juzu.Resource
    @Route("/statistics")
    public Response getStatistics() {
        List<StatisticBean> statisticBeanList;
        try {
            // statisticBeanList = statisticsService.getAllStats();
            statisticBeanList = getAllStats();
        } catch (Exception e) {
            log.error("Error while fetching synchronization target servers", e);
            statisticBeanList = new ArrayList<StatisticBean>();
        }

        StringBuilder jsonServers = new StringBuilder(50);
        jsonServers.append("{\"statisticBeanList\":[");
        for(StatisticBean statisticBean : statisticBeanList) {
            jsonServers.append("{\"id\":\"")
                    .append(statisticBean.getId())
                    .append("\",\"category\":\"")
                    .append(statisticBean.getCategory())
                    .append("\",\"categoryId\":\"")
                    .append(statisticBean.getCategoryId())
                    .append("\",\"content\":\"")
                    .append(statisticBean.getContent())
                    .append("\",\"username\":\"")
                    .append(statisticBean.getUsername())
                    .append("\",\"url\":\"")
                    .append(statisticBean.getUrl())
                    .append("\",\"active\":")
                    .append(statisticBean.isActive())
                    .append("},");
        }
        if(!statisticBeanList.isEmpty()) {
            jsonServers.deleteCharAt(jsonServers.length()-1);
        }
        jsonServers.append("]}");

        return Response.ok(jsonServers.toString());
    }
    private static List<StatisticBean> getAllStats () {
        List<StatisticBean> statisticBeanList = new ArrayList<StatisticBean>();

        StatisticBean statisticBean = new StatisticBean ("1","catA","catA_1","Puzzle","khemais","explatform.com",true);

        statisticBeanList.add(statisticBean);

        statisticBean = new StatisticBean ("2","catB","catB_1","Lopo","Esslem","google.com",true);

        statisticBeanList.add(statisticBean);

        statisticBean = new StatisticBean ("3","catC","catC_1","Logo","BnjP","community.com",true);

        statisticBeanList.add(statisticBean);

        return statisticBeanList;
    }

}


