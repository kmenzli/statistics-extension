package org.exoplatform.addons.statistics.api.rest;


import org.exoplatform.addons.statistics.api.bo.StatisticBO;
import org.exoplatform.addons.statistics.api.exception.StatisticsException;
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
    @Path("/query.{format}")
    @RolesAllowed("users")
    public Response search(@Context UriInfo uriInfo,
                           @QueryParam("criteria") String criteria,
                           @DefaultValue("ALL") @QueryParam("scope") String scope,
                           @DefaultValue("10") @QueryParam("offset") String offset,
                           @DefaultValue("100") @QueryParam("limit") String limit,
                           @DefaultValue("relevancy") @QueryParam("sort") String sort,
                           @DefaultValue("desc") @QueryParam("order") String order,
                           @PathParam("format") String format) throws Exception {

        MediaType mediaType = Util.getMediaType(format, mediaTypes);

        List<StatisticBO> statisticBOs = null;

        statistics =  new StatisticsList();


        try {

            String searchScope = Util.computeSearchParameters(criteria,scope);

            statisticsService = ServiceLookupManager.getInstance().getStatisticsService();


            statisticBOs = statisticsService.search(criteria, searchScope, Integer.parseInt(offset), Integer.parseInt(limit), sort, order);

            statistics.setStatistics(statisticBOs);

        } catch (StatisticsException E) {

            LOG.warn("The search parameters used has not been filled out correctly please check your request, scope ["+scope+"] and criteria ["+criteria+"]");

            return Util.getResponse(statistics, uriInfo, mediaType, Response.Status.OK);


       } catch (Exception E) {

            LOG.warn("The search parameters used has not been filled out correctly please check your request, scope ["+scope+"] and criteria ["+criteria+"]");

            return Util.getResponse(statistics, uriInfo, mediaType, Response.Status.OK);

        }

        return Util.getResponse(statistics, uriInfo, mediaType, Response.Status.OK);
    }

    @GET
    @Path("/export/.{format}")
    @RolesAllowed("administrators")
    public Response exportStatistics() throws Exception {
        throw new WebApplicationException(Response.Status.NOT_ACCEPTABLE);
    }

    @GET
    @Path("/query/.{format}")
    @RolesAllowed("administrators")
    public Response importStatistics() throws Exception {
        throw new WebApplicationException(Response.Status.NOT_ACCEPTABLE);
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
