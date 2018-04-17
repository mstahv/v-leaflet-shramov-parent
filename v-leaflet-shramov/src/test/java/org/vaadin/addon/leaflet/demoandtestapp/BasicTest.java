package org.vaadin.addon.leaflet.demoandtestapp;

import com.vaadin.server.ClassResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import java.util.ArrayList;
import java.util.Arrays;
import org.vaadin.addon.leaflet.AbstractLeafletLayer;
import org.vaadin.addon.leaflet.LCircle;
import org.vaadin.addon.leaflet.LCircleMarker;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LPolygon;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.LeafletClickEvent;
import org.vaadin.addon.leaflet.LeafletClickListener;
import org.vaadin.addon.leaflet.LeafletMoveEndEvent;
import org.vaadin.addon.leaflet.LeafletMoveEndListener;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.Control;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addon.leaflet.shramov.LGoogleLayer;
import org.vaadin.addonhelpers.AbstractTest;

// 
public class BasicTest extends AbstractTest {

	@Override
	public String getDescription() {
		return "A simple test";
	}

	LeafletClickListener listener = new LeafletClickListener() {

		@Override
		public void onClick(LeafletClickEvent event) {
			if (event.getPoint() != null) {
				Notification.show(String.format("Clicked %s @ %.4f,%.4f", event
						.getConnector().getClass().getSimpleName(), event
						.getPoint().getLat(), event.getPoint().getLon()));

				if (event.getSource() == leafletMap && addMarkers.getValue()) {
					LMarker leafletMarker = new LMarker(event.getPoint());
					leafletMarker.addClickListener(listener);
					leafletMap.addComponent(leafletMarker);
				}
			} else {
				Notification.show(String.format("Clicked %s", event
						.getConnector().getClass().getSimpleName()));
			}
			if (delete.getValue() && event.getSource() instanceof AbstractLeafletLayer) {
				leafletMap.removeComponent((Component) event.getConnector());
			}
		}
	};
	private LMap leafletMap;
	private CheckBox addMarkers;
	private CheckBox delete;
	private LMarker leafletMarker;

	@Override
	public Component getTestComponent() {
		leafletMap = new LMap();
		leafletMap.setCenter(60.4525, 22.301);
		leafletMap.setZoomLevel(15);

		leafletMap.setControls(new ArrayList<Control>(Arrays.asList(Control
				.values())));

		LPolyline leafletPolyline = new LPolyline(new Point(60.45, 22.295),
				new Point(60.4555, 22.301), new Point(60.45, 22.307));
		leafletPolyline.setColor("#FF00FF");
		leafletPolyline.setFill(true);
		leafletPolyline.setFillColor("#00FF00");
		leafletPolyline.setClickable(false);
		leafletPolyline.setWeight(8);
		leafletPolyline.setOpacity(0.5);
		leafletPolyline.setDashArray("15, 10, 5, 10, 15");
		leafletPolyline.addClickListener(listener);
		leafletMap.addComponent(leafletPolyline);

		LPolygon leafletPolygon = new LPolygon(new Point(60.455, 22.300),
				new Point(60.456, 22.302), new Point(60.50, 22.308));
		leafletPolygon.setColor("#FF00FF");
		leafletPolygon.setFill(true);
		leafletPolygon.setFillColor("#00FF00");
		leafletPolygon.addClickListener(listener);
		leafletMap.addComponent(leafletPolygon);

		LCircle leafletCircle = new LCircle(60.4525, 22.301, 300);
		leafletCircle.setColor("#00FFFF");
		// leafletCircle.addClickListener(listener);
		leafletMap.addComponent(leafletCircle);
		leafletCircle.addClickListener(listener);

		LCircleMarker leafletCircleMarker = new LCircleMarker(60.4525, 22.301, 5);
		leafletCircleMarker.setColor("#FFFF00");
		leafletMap.addComponent(leafletCircleMarker);
		leafletCircleMarker.addClickListener(listener);

		leafletMarker = new LMarker(60.4525, 22.301);
		leafletMarker.addClickListener(listener);
		leafletMarker.setTitle("this is marker two!");
//		leafletMarker
//				.setDivIcon("this is a <h1>fabulous</h1> <span style=\"color:red\">icon</span>");
		leafletMarker.setPopup("Hello <b>world</b>");
		leafletMap.addComponent(leafletMarker);

		leafletMarker = new LMarker(60.4525, 22.301);
		leafletMarker.setIcon(new ClassResource("testicon.png"));
		leafletMarker.setIconSize(new Point(57, 52));
		leafletMarker.setIconAnchor(new Point(57, 26));
		leafletMarker.addClickListener(listener);
		leafletMarker.setTitle("this is marker one!");
		leafletMarker.setPopup("Hello <b>Vaadin World</b>!");
		
		leafletMap.addComponent(leafletMarker);
		leafletMap.setAttributionPrefix("Powered by Leaflet with v-leaflet");

//        leafletMap.addBaseLayer(new LGoogleLayer(LGoogleLayer.Type.HYBRID), "Google Maps hybrid");
//        leafletMap.addBaseLayer(new LGoogleLayer(LGoogleLayer.Type.SATELLITE), "Google Maps sat");
//        leafletMap.addBaseLayer(new LGoogleLayer(LGoogleLayer.Type.TERRAIN), "Google Maps terrain");
        leafletMap.addBaseLayer(new LGoogleLayer(), "Google Maps layer");
  
		leafletMap.addClickListener(listener);

		leafletMap.addMoveEndListener(new LeafletMoveEndListener() {
			@Override
			public void onMoveEnd(LeafletMoveEndEvent event) {
				Bounds b = event.getBounds();
				Notification.show(
						String.format("New viewport (%.4f,%.4f ; %.4f,%.4f)",
								b.getSouthWestLat(), b.getSouthWestLon(),
								b.getNorthEastLat(), b.getNorthEastLon()),
						Type.TRAY_NOTIFICATION);
			}
		});

		return leafletMap;
	}

	@Override
	protected void setup() {
		super.setup();

		addMarkers = new CheckBox("Add markers");
		content.addComponentAsFirst(addMarkers);

		delete = new CheckBox("Delete on click");
		content.addComponentAsFirst(delete);

		Button openPopup = new Button("Open popup", new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				leafletMarker.openPopup();
			}
		});

		Button closePopup = new Button("Close popup", new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				leafletMarker.closePopup();
			}
		});
		content.addComponentAsFirst(closePopup);
		content.addComponentAsFirst(openPopup);

		Button button = new Button("Delete first component from map");
		button.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				Component next = leafletMap.iterator().next();
				leafletMap.removeComponent(next);
			}
		});
		content.addComponentAsFirst(button);

		button = new Button("Add polyline to map");
		button.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				LPolyline lPolyline = new LPolyline(new Point(60.44, 22.30),
						new Point(60.456, 22.304));
				lPolyline.addClickListener(listener);
				leafletMap.addComponent(lPolyline);
			}
		});
		content.addComponentAsFirst(button);

	}
}
