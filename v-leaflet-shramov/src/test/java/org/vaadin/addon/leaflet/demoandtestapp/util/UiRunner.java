package org.vaadin.addon.leaflet.demoandtestapp.util;

import com.vaadin.annotations.Widgetset;
import org.vaadin.addonhelpers.TServer;

@Widgetset("org.vaadin.addon.leaflet.demoandtestapp.TestWidgetset")
public class UiRunner extends TServer {

    public static void main(String[] args) throws Exception {
        new UiRunner().startServer();
    }
}
