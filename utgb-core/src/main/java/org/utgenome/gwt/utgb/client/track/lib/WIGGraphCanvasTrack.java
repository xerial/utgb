/*--------------------------------------------------------------------------
 *  Copyright 2009 utgenome.org
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *--------------------------------------------------------------------------*/
//--------------------------------------
// utgb-core Project
//
// WIGGraphCanvasTrack.java
// Since: Dec. 8, 2009
//
// $URL$ 
// $Author$ 
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import java.util.ArrayList;
import java.util.List;

import org.utgenome.gwt.utgb.client.bio.ChrLoc;
import org.utgenome.gwt.utgb.client.bio.CompactWIGData;
import org.utgenome.gwt.utgb.client.canvas.GWTGraphCanvas;
import org.utgenome.gwt.utgb.client.canvas.GWTGraphCanvas.GraphStyle;
import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;
import org.utgenome.gwt.utgb.client.track.TrackConfig;
import org.utgenome.gwt.utgb.client.track.TrackConfigChange;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyChange;
import org.utgenome.gwt.utgb.client.track.TrackWindow;
import org.utgenome.gwt.utgb.client.track.UTGBProperty;
import org.utgenome.gwt.widget.client.Style;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

/**
 * WIG Track
 * 
 * @author yoshimura
 * @author leo
 * 
 */
public class WIGGraphCanvasTrack extends TrackBase {

	public static TrackFactory factory() {
		return new TrackFactory() {
			@Override
			public Track newInstance() {
				return new WIGGraphCanvasTrack();
			}
		};
	}

	public WIGGraphCanvasTrack() {
		super("WIG Graph Canvas");

		layoutTable.setBorderWidth(0);
		layoutTable.setCellPadding(0);
		layoutTable.setCellSpacing(0);
		Style.margin(layoutTable, 0);
		Style.padding(layoutTable, 0);

		layoutTable.setWidget(0, 1, graphCanvas);

		// prepare prefetched canvas
		for (int i = 0; i < numPrefetch; ++i)
			wigData.add(WIGDataHolder.emptyHolder());

	}

	private final FlexTable layoutTable = new FlexTable();
	private final GWTGraphCanvas graphCanvas = new GWTGraphCanvas();
	private final int numPrefetch = 3;

	/**
	 * Graph data holder
	 * 
	 * @author leo
	 * 
	 */
	private static class WIGDataHolder {
		private List<CompactWIGData> wigData;
		private TrackWindow window;

		public WIGDataHolder(List<CompactWIGData> wigData, TrackWindow window) {
			this.wigData = wigData;
			this.window = window;
		}

		public boolean isReady() {
			return wigData != null && window != null;
		}

		public void clear() {
			wigData = null;
			window = null;
		}

		public List<CompactWIGData> getWigData() {
			return wigData;
		}

		public TrackWindow getTrackWindow() {
			return window;
		}

		public static WIGDataHolder emptyHolder() {
			return new WIGDataHolder(null, null);
		}
	}

	private ArrayList<WIGDataHolder> wigData = new ArrayList<WIGDataHolder>(numPrefetch);

	public Widget getWidget() {
		return layoutTable;
	}

	@Override
	public void onChangeTrackWindow(TrackWindow newWindow) {
		update(newWindow);
	}

	@Override
	public void onChangeTrackGroupProperty(TrackGroupPropertyChange change) {

		if (change.containsOneOf(new String[] { UTGBProperty.TARGET })) {
			update(change.getTrackWindow());
		}
	}

	private GraphStyle style = new GraphStyle();

	/**
	 * Configuration parameter names
	 */
	private final static String CONFIG_FILENAME = "fileName";

	@Override
	public void setUp(TrackFrame trackFrame, TrackGroup group) {

		graphCanvas.setTrackGroup(group);
		TrackConfig config = getConfig();
		config.addConfigString("Path", CONFIG_FILENAME, "");
		style.setup(config);

		update(group.getTrackWindow());
	}

	@Override
	public void draw() {
		graphCanvas.clear();
		style.load(getConfig());
		graphCanvas.setStyle(style);

		// draw data graph
		//graphCanvas.drawWigGraph(wigDataList);
		getFrame().loadingDone();
	}

	public void update(TrackWindow newWindow) {
		// retrieve gene data from the API
		graphCanvas.setTrackWindow(newWindow);

		loadGraph(newWindow);

		// prefetch 
		//TrackWindow right = newWindow.newWindow(e, e + newWindow.getSequenceLength());
		//TrackWindow left = newWindow.newWindow(s - newWindow.getSequenceLength(), s);

	}

	public void loadGraph(TrackWindow queryWindow) {

		getFrame().setNowLoading();
		String fileName = getConfig().getString(CONFIG_FILENAME, "");
		int s = queryWindow.getStartOnGenome();
		int e = queryWindow.getEndOnGenome();
		ChrLoc l = new ChrLoc(getTrackGroupProperty(UTGBProperty.TARGET), s, e);
		getBrowserService().getCompactWigDataList(fileName, queryWindow.getPixelWidth(), l, new AsyncCallback<List<CompactWIGData>>() {
			public void onFailure(Throwable e) {
				GWT.log("failed to retrieve wig data", e);
				getFrame().loadingDone();
			}

			public void onSuccess(List<CompactWIGData> dataList) {
				//wigDataList = dataList;
				refresh();
			}
		});

	}

	@Override
	public void onChangeTrackConfig(TrackConfigChange change) {

		if (change.contains(CONFIG_FILENAME)) {
			update(getTrackWindow());
		}
		else {
			getFrame().setNowLoading();
			refresh();
		}
	}

}
