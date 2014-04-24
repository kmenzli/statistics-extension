package org.exoplatform.addons.statistics.api.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by menzli on 21/04/14.
 */
//@WebListener
class StatisticsWebListener /*implements ServletContextListener**/ {

    //@Override
    public void contextDestroyed(ServletContextEvent arg0) {
        System.out.println("ServletContextListener destroyed");
    }

    //@Override
    public void contextInitialized(ServletContextEvent arg0) {
        System.out.println("ServletContextListener started");
    }
}
