package org.exoplatform.addons.statistics.api.utils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.regex.Pattern;

/**
 * Created by menzli on 23/04/14.
 */
public final class Util {

    private static final Pattern URL_PATTERN = Pattern
            .compile("^(?i)" +
                    "(" +
                    "((?:(?:ht)tp(?:s?)\\:\\/\\/)?" +                                                       // protolcol
                    "(?:\\w+:\\w+@)?" +                                                                       // username password
                    "(((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +  // IPAddress
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))|" +     // IPAddress
                    "(?:(?:[-\\p{L}\\p{Digit}\\+\\$\\-\\*\\=]+\\.)+" +
                    "(?:com|org|net|edu|gov|mil|biz|info|mobi|name|aero|jobs|museum|travel|asia|cat|coop|int|pro|tel|xxx|[a-z]{2}))))|" + //Domain
                    "(?:(?:(?:ht)tp(?:s?)\\:\\/\\/)(?:\\w+:\\w+@)?(?:[-\\p{L}\\p{Digit}\\+\\$\\-\\*\\=]+))" + // Protocol with hostname
                    ")" +
                    "(?::[\\d]{1,5})?" +                                                                        // port
                    "(?:[\\/|\\?|\\#].*)?$");                                                               // path and query



    /**
     * Prevents constructing a new instance.
     */
    private Util() {
    }

    public static boolean isValidURL(String link) {
        if (link == null || link.length() == 0) return false;
        return URL_PATTERN.matcher(link).matches();
    }

    /**
     * Gets the media type from an expected format string (usually the input) and an array of supported format strings.
     * If expectedFormat is not found in the supported format array, Status.UNSUPPORTED_MEDIA_TYPE is thrown.
     * The supported format must include one of those format: json, xml, atom or rss, otherwise Status.NOT_ACCEPTABLE
     * could be thrown.
     *
     * @param expectedFormat the expected input format
     * @param supportedFormats the supported format array
     * @return the associated media type
     */
    public static MediaType getMediaType(String expectedFormat, String[] supportedFormats) {

        if (!isSupportedFormat(expectedFormat, supportedFormats)) {
            throw new WebApplicationException(Response.Status.UNSUPPORTED_MEDIA_TYPE);
        }

        if (expectedFormat.equals("json") && isSupportedFormat("json", supportedFormats)) {
            return MediaType.APPLICATION_JSON_TYPE;
        } else if (expectedFormat.equals("xml") && isSupportedFormat("xml", supportedFormats)) {
            return MediaType.APPLICATION_XML_TYPE;
        } else if (expectedFormat.equals("atom") && isSupportedFormat("atom", supportedFormats)) {
            return MediaType.APPLICATION_ATOM_XML_TYPE;
        }
        //TODO What's about RSS format?
        throw new WebApplicationException(Response.Status.NOT_ACCEPTABLE);
    }

    /**
     * Checks if an expected format is supported not not.
     *
     * @param expectedFormat  the expected format
     * @param supportedFormats the array of supported format
     * @return true or false
     */
    private static boolean isSupportedFormat(String expectedFormat, String[] supportedFormats) {
        for (String supportedFormat : supportedFormats) {
            if (supportedFormat.equals(expectedFormat)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the response object constructed from the provided params.
     *
     * @param entity the identity
     * @param uriInfo the uri request info
     * @param mediaType the media type to be returned
     * @param status the status code
     * @return response the response object
     */
    public static Response getResponse(Object entity, UriInfo uriInfo, MediaType mediaType, Response.Status status) {
        return Response.created(uriInfo.getAbsolutePath())
                .entity(entity)
                .type(mediaType.toString() + "; charset=utf-8")
                .status(status)
                .build();
    }


}
