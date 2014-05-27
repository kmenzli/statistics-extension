package org.exoplatform.addons.statistics.services;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

/**
 * Created by menzli on 22/05/14.
 */
public class Utils {
    public static String getData(String fileName)
    {
        String out = "";
        InputStream inputStream = getLocalFile(fileName, "/data");

        StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(inputStream, writer);
            out = writer.toString();

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return out;
    }

    private static InputStream getLocalFile(String fileName, String folder)
    {
        InputStream inputStream = Utils.class.getClassLoader().getResourceAsStream(folder+"/"+fileName);

        return inputStream;
    }
}
