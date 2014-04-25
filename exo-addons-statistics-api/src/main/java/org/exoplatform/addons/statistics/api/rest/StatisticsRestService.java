package org.exoplatform.addons.statistics.api.rest;


import org.exoplatform.addons.statistics.api.bo.StatisticBO;
import org.exoplatform.addons.statistics.api.services.StatisticsService;
import org.exoplatform.addons.statistics.api.utils.ServiceLookupManager;
import org.exoplatform.addons.statistics.api.utils.Util;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.rest.resource.ResourceContainer;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by menzli on 23/04/14.
 */

@Path("/statistics/api/")
public class StatisticsRestService implements ResourceContainer {

    private static final Log LOG = ExoLogger.getLogger(StatisticsRestService.class);

    StatisticsService statisticsService = null;

    private StatisticsList statistics = null;

    private static String[] mediaTypes = new String[] { "json", "xml" };

    @GET
    @Path("/cleanup")
    @RolesAllowed("administrators")
    public Response cleanupStatistics(@Context UriInfo uriInfo) throws Exception {

        statisticsService = ServiceLookupManager.getInstance().getStatisticsService();

        try {

            statisticsService.cleanupStatistics();

        } catch (Exception E) {

            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);

        }

        return Util.getResponse(null, uriInfo, MediaType.APPLICATION_JSON_TYPE, Response.Status.OK);

    }


    @POST
    @Path("/add/")
    //@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @RolesAllowed("users")
    public Response addEntry(@Context UriInfo uriInfo,
                         @FormParam("username") String username,
                         @FormParam("from") String from,
                         @FormParam("type") String type,
                         @FormParam("category") String category,
                         @FormParam("categoryId") String categoryId,
                         @FormParam("content") String content,
                         @FormParam("link") String link) throws Exception  {

        statisticsService = ServiceLookupManager.getInstance().getStatisticsService();

        StatisticBO statisticBO = null;

        try {

           statisticBO = statisticsService.addEntry(username,from,type,category,categoryId,content,link);

        } catch (Exception E) {

            LOG.error("Cannot add statistic ", E);

            return Util.getResponse(null, uriInfo, MediaType.APPLICATION_JSON_TYPE, Response.Status.INTERNAL_SERVER_ERROR);

        }

        return Util.getResponse(statisticBO, uriInfo, MediaType.APPLICATION_JSON_TYPE, Response.Status.OK);

    }


    @GET
    @Path("/user/{username}.{format}")
    public Response getStatisticsByUserName(@Context UriInfo uriInfo,
                                            @PathParam("username") String username,
                                            @PathParam("format") String format) throws Exception {

        statisticsService = ServiceLookupManager.getInstance().getStatisticsService();

        MediaType mediaType = Util.getMediaType(format, mediaTypes);

        List<StatisticBO> statisticBOs = null;

        statistics =  new StatisticsList();

        try {

            statisticBOs = statisticsService.getStatisticsByUserName(username);

            statistics.setStatistics(statisticBOs);

        } catch (Exception E) {

            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);

        }

        return Util.getResponse(statistics, uriInfo, mediaType, Response.Status.OK);
    }

    @GET
    @Path("/category/{category}.{format}")
    public Response getStatisticsByCategory(@Context UriInfo uriInfo,
                                            @PathParam("category") String category,
                                            @PathParam("format") String format) throws Exception {

        statisticsService = ServiceLookupManager.getInstance().getStatisticsService();

        MediaType mediaType = Util.getMediaType(format, mediaTypes);

        List<StatisticBO> statisticBOs = null;

        statistics =  new StatisticsList();

        try {

            statisticBOs = statisticsService.getStatisticsByCategory(category);

            statistics.setStatistics(statisticBOs);

        } catch (Exception E) {

            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);

        }

        return Util.getResponse(statistics, uriInfo, mediaType, Response.Status.OK);
    }

    @GET
    @Path("/type/{type}.{format}")
    public Response getStatisticsByType(@Context UriInfo uriInfo,
                                        @PathParam("type") String type,
                                        @PathParam("format") String format) throws Exception {

        statisticsService = ServiceLookupManager.getInstance().getStatisticsService();

        MediaType mediaType = Util.getMediaType(format, mediaTypes);

        List<StatisticBO> statisticBOs = null;

        statistics =  new StatisticsList();

        try {

            statisticBOs = statisticsService.getStatisticsByType(type);

            statistics.setStatistics(statisticBOs);

        } catch (Exception E) {

            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);

        }

        return Util.getResponse(statistics, uriInfo, mediaType, Response.Status.OK);

    }

    @GET
    @Path("/all.{format}")
    public Response getAllStatistics(@Context UriInfo uriInfo,
                                              @PathParam("format") String format) throws Exception {

        statisticsService = ServiceLookupManager.getInstance().getStatisticsService();

        MediaType mediaType = Util.getMediaType(format, mediaTypes);

        List<StatisticBO> statisticBOs = null;

        statistics =  new StatisticsList();


        try {

            statisticBOs = statisticsService.getAllStatistics();

            statistics.setStatistics(statisticBOs);

        } catch (Exception E) {

            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);

        }

        return Util.getResponse(statistics, uriInfo, mediaType, Response.Status.OK);
    }

    @XmlRootElement
    static public class StatisticsList {

       private List<StatisticBO> _statistics;

        /**
         * sets statistics list
         *
         * @param statistics list
         */
        public void setStatistics(List<StatisticBO> statistics) {
            _statistics = statistics;
        }

        /**
         * gets statistics list
         *
         * @return statistics list
         */
        public List<StatisticBO> getStatistics() {
            return _statistics;
        }

        /**
         * adds space to space list
         *
         * @param statisticBO
         * @see org.exoplatform.addons.statistics.api.bo.StatisticBO
         */
        public void addStatisticBO(StatisticBO statisticBO) {
            if (_statistics == null) {
                _statistics = new LinkedList<StatisticBO>();
            }
            _statistics.add(statisticBO);
        }
    }

}
