package org.exoplatform.addons.statistics.portlet.statistics;

import juzu.*;
import juzu.template.Template;
import org.exoplatform.addons.statistics.api.bo.StatisticBO;
import org.exoplatform.addons.statistics.api.services.StatisticsService;
import org.exoplatform.addons.statistics.api.web.listener.StatisticsLifecycleListener;
import org.exoplatform.commons.juzu.ajax.Ajax;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import javax.inject.Inject;
import java.io.IOException;
import java.util.*;

/**
 * Created by menzli on 10/04/14.
 */
@SessionScoped
public class StatisticsApplication {

    private static Log log = ExoLogger.getLogger(StatisticsApplication.class);

    StatisticsService statisticsService;

    static List<String> resources = new ArrayList<String>();
    static List<Facet> facets = new ArrayList<Facet>();

    public StatisticsApplication () {

        statisticsService = StatisticsLifecycleListener.getInstance().getInstance(StatisticsService.class);

    }

    @Inject
    @Path("index.gtmpl")
    Template index;

    static {

        resources.add("User");
        resources.add("Category");
        facets.add(new Facet("User",false));
        facets.add(new Facet("Category",false));
        facets.add(new Facet("Categoryid",false));
        facets.add(new Facet("Type",false));
        facets.add(new Facet("Site",false));
        facets.add(new Facet("SiteType",false));
        facets.add(new Facet("Content",false));

    }

    @View
    public Response.Render index () {

        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put("resources", resources);

        return index.ok(parameters);

    }

    @Ajax
    @juzu.Resource
    @Route("/statistics")
    public Response getStatistics() {



        List<StatisticBO> statisticBeanList;
        try {
            statisticBeanList = statisticsService.getStatistics(0);

        } catch (Exception e) {
            log.error("Error while fetching synchronization target servers", e);
            statisticBeanList = new ArrayList<StatisticBO>();
        }

        return Response.ok(StatisticBO.statisticstoJSON(statisticBeanList));
    }

    @Ajax
    @juzu.Resource
    public Response search(String user, String content, String category) throws IOException {

        try {

            System.out.println("############## user ["+user+"] content ["+content+"] category ["+category+"] ");


        } catch (Exception E) {

        }

        return Response.ok("");
    }

    @Ajax
    @juzu.Resource
    public Response getFacets() {

        //TODO use an external component plugin to load facets

        return Response.ok(facetsJSON(facets));


    }

    public static String facetsJSON(List<Facet> facets)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"facets\": [");
        boolean first=true;
        for (Facet facet:facets) {
            if (!first) {
                sb.append(",");
            } else {
                first=false;
            }

            sb.append(facet.toJSON());

        }
        sb.append("]}");

        return sb.toString();
    }

    static public class Facet {

        private String label = "";

        private boolean activated = false;

        public Facet(String label, boolean activated) {
            this.label = label;
            this.activated = activated;
        }

        public String toJSON()
        {
            StringBuffer sb = new StringBuffer();

            sb.append("{");

            sb.append("\"label\": \""+this.getLabel()+"\",");
            sb.append("\"activate\": "+this.isActivated());

            sb.append("}");

            return sb.toString();
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public boolean isActivated() {
            return activated;
        }

        public void setActivated(boolean activated) {
            this.activated = activated;
        }
    }



}


