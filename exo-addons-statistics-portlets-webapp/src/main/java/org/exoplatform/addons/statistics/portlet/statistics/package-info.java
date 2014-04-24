/*
 * Copyright (C) 2012 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

@Application(defaultController = StatisticsApplication.class)
@Portlet(name="StatisticsPortlet")
@Bindings(
        {
                @Binding(value = org.exoplatform.services.jcr.RepositoryService.class),
                @Binding(value = org.exoplatform.services.jcr.ext.app.SessionProviderService.class),
                @Binding(value = org.exoplatform.services.jcr.ext.hierarchy.NodeHierarchyCreator.class),
                @Binding(value = org.exoplatform.services.listener.ListenerService.class)
        }
)

@Assets(
        //location = AssetLocation.SERVER,
        scripts = {

                // AngularJS is still global, should be AMDified
                @Script(id = "angularjs", src = "js/fwk/angular.min.js"),
                // services and controllers js are AMD modules, required by messaging.js
                @Script(id = "services", src = "js/statistics/services.js"),
                @Script(id = "controllers", src = "js/statistics/controllers.js"),
                @Script(id = "statistics", src = "js/statistics/statistics.js")

        },
        stylesheets = {
                @Stylesheet(src = "/org/exoplatform/addons/statistics/portlet/statistics/assets/css/bootstrap.css",location = AssetLocation.APPLICATION, id = "bootstrap")
        }
)

@Less(value = "css/bootstrap.less", minify = true)


package org.exoplatform.addons.statistics.portlet.statistics;

import juzu.Application;
import juzu.asset.AssetLocation;
import juzu.plugin.asset.Assets;
import juzu.plugin.asset.Script;
import juzu.plugin.asset.Stylesheet;
import juzu.plugin.binding.Binding;
import juzu.plugin.binding.Bindings;
import juzu.plugin.less.Less;
import juzu.plugin.portlet.Portlet;
import org.exoplatform.addons.statistics.provider.GateInMetaProvider;
