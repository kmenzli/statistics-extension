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
import java.util.List;

/**
 * Created by menzli on 23/04/14.
 */

@Path("/statistics/api/")
public class StatisticsRestService implements ResourceContainer {

    private static final Log LOG = ExoLogger.getLogger(StatisticsRestService.class);

    StatisticsService statisticsService = null;

    private static String[] mediaTypes = new String[] { "json", "xml" };

    @GET
    @Path("/cleanup")
    @RolesAllowed("administrators")
    public Response cleanupStatistics(@Context UriInfo uriInfo) throws Exception {

        statisticsService = ServiceLookupManager.getInstance().getStatisticsService();

        try {

            statisticsService.cleanupStatistics();

        } catch (Exception E) {

            LOG.error("Cannot cleanup statistics ", E);

            return Util.getResponse(null, uriInfo, MediaType.APPLICATION_JSON_TYPE, Response.Status.INTERNAL_SERVER_ERROR);

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

        try {

            statisticBOs = statisticsService.getStatisticsByUserName(username);

        } catch (Exception E) {

            LOG.error("Cannot load statistics when cretiria username=["+username+"] ", E);

            return Util.getResponse(statisticBOs, uriInfo, MediaType.APPLICATION_JSON_TYPE, Response.Status.INTERNAL_SERVER_ERROR);

        }

        return Util.getResponse(statisticBOs, uriInfo, mediaType, Response.Status.OK);
    }

    @GET
    @Path("/category/{category}.{format}")
    public Response getStatisticsByCategory(@Context UriInfo uriInfo,
                                            @PathParam("category") String category,
                                            @PathParam("format") String format) throws Exception {

        statisticsService = ServiceLookupManager.getInstance().getStatisticsService();

        MediaType mediaType = Util.getMediaType(format, mediaTypes);

        List<StatisticBO> statisticBOs = null;

        try {

            statisticBOs = statisticsService.getStatisticsByCategory(category);

        } catch (Exception E) {

            LOG.error("Cannot load statistics when cretiria category=["+category+"] ", E);

            return Util.getResponse(statisticBOs, uriInfo, MediaType.APPLICATION_JSON_TYPE, Response.Status.INTERNAL_SERVER_ERROR);

        }

        return Util.getResponse(statisticBOs, uriInfo, mediaType, Response.Status.OK);
    }

    @GET
    @Path("/type/{type}.{format}")
    public Response getStatisticsByType(@Context UriInfo uriInfo,
                                        @PathParam("type") String type,
                                        @PathParam("format") String format) throws Exception {

        statisticsService = ServiceLookupManager.getInstance().getStatisticsService();

        MediaType mediaType = Util.getMediaType(format, mediaTypes);

        List<StatisticBO> statisticBOs = null;

        try {

            statisticBOs = statisticsService.getStatisticsByType(type);

        } catch (Exception E) {

            LOG.error("Cannot load statistics when cretiria type=["+type+"] ", E);

            return Util.getResponse(statisticBOs, uriInfo, MediaType.APPLICATION_JSON_TYPE, Response.Status.INTERNAL_SERVER_ERROR);

        }

        return Util.getResponse(statisticBOs, uriInfo, mediaType, Response.Status.OK);

    }

    @GET
    @Path("/all.{format}")
    public Response getAllStatistics(@Context UriInfo uriInfo,
                                              @PathParam("format") String format) throws Exception {

        statisticsService = ServiceLookupManager.getInstance().getStatisticsService();

        MediaType mediaType = Util.getMediaType(format, mediaTypes);

        List<StatisticBO> statisticBOs = null;

        try {

            statisticBOs = statisticsService.getAllStatistics();

        } catch (Exception E) {

            LOG.error("Cannot load statistics ", E);

            return Util.getResponse(statisticBOs, uriInfo, MediaType.APPLICATION_JSON_TYPE, Response.Status.INTERNAL_SERVER_ERROR);

        }

        return Util.getResponse(statisticBOs, uriInfo, mediaType, Response.Status.OK);
    }

}
