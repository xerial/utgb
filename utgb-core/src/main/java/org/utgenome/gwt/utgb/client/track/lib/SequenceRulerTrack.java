/*--------------------------------------------------------------------------
 *  Copyright 2007 utgenome.org
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
// GenomeBrowser Project
//
// SequenceRulerTrack.java
// Since: Jun 13, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib;

import org.utgenome.gwt.utgb.client.db.Value;
import org.utgenome.gwt.utgb.client.db.ValueDomain;
import org.utgenome.gwt.utgb.client.db.datatype.IntegerType;
import org.utgenome.gwt.utgb.client.track.RangeSelectable;
import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackBase;
import org.utgenome.gwt.utgb.client.track.TrackConfigChange;
import org.utgenome.gwt.utgb.client.track.TrackFrame;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyChange;
import org.utgenome.gwt.utgb.client.track.TrackRangeSelector;
import org.utgenome.gwt.utgb.client.track.TrackWindow;
import org.utgenome.gwt.utgb.client.track.UTGBProperty;
import org.utgenome.gwt.utgb.client.ui.AbsoluteFocusPanel;
import org.utgenome.gwt.widget.client.Style;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * SequenceRulerTrack is a ruler on an entire sequence (a chromosome or scaffold). (corresponding to the old
 * OverviewTrack)
 * 
 * @author leo
 * 
 */
public class SequenceRulerTrack extends TrackBase implements RangeSelectable {
	private final TrackRangeSelector _rangeSelector;
	private final Grid _layoutPanel = new Grid(1, 2);
	private final AbsoluteFocusPanel _basePanel = new AbsoluteFocusPanel();
	private final Ruler _ruler;
	private final Label range = new Label();

	public static TrackFactory factory() {
		return new TrackFactory() {
			@Override
			public Track newInstance() {
				return new SequenceRulerTrack();
			}
		};
	}

	public SequenceRulerTrack() {
		super("Sequence Ruler");
		_basePanel.setStyleName("global-ruler");
		_basePanel.setTitle("click twice to change the focus area on the sequence");
		DOM.setStyleAttribute(_basePanel.getElement(), "cursor", "pointer");
		_rangeSelector = new TrackRangeSelector(this);
		_ruler = new Ruler(_rangeSelector, "global-ruler-tick");
		range.setStyleName("ruler-range");
		range.setWidth("0px");
		range.addMouseDownHandler(new MouseListenerOnRulerWidget(_rangeSelector));
		_layoutPanel.setCellPadding(0);
		_layoutPanel.setCellSpacing(0);
		Style.fontSize(_layoutPanel, 0);
	}

	public void clear() {
		_basePanel.clear();
	}

	@Override
	public int getDefaultWindowHeight() {
		return 14;
	}

	public Widget getWidget() {
		return _layoutPanel;
	}

	@Override
	public void onChangeTrackGroupProperty(TrackGroupPropertyChange change) {
		final String[] relatedProperties = { UTGBProperty.SPECIES, UTGBProperty.REVISION, UTGBProperty.TARGET };
		if (change.containsOneOf(relatedProperties)) {
			refresh();
		}

		if (change.contains(UTGBProperty.SEQUENCE_SIZE))
			refresh();
	}

	@Override
	public void onChangeTrackWindow(TrackWindow newWindow) {
		_rangeSelector.setWindowWidth(newWindow.getPixelWidth());
		refresh();
	}

	private int getSequenceSize() {
		return getConfig().getInt(UTGBProperty.SEQUENCE_SIZE, 1000000);
	}

	private void drawTrackSelectionRange(TrackWindow newWindow) {
		long startOnGenome = newWindow.getStartOnGenome();
		long endOnGenome = newWindow.getEndOnGenome();

		int windowWidth = newWindow.getPixelWidth();

		double pixelPerCode = windowWidth / (double) getSequenceSize();
		int x1 = (int) (startOnGenome * pixelPerCode);
		int x2 = (int) (endOnGenome * pixelPerCode);

		if (startOnGenome <= endOnGenome) {
			int rangeWidth = x2 - x1;
			if (rangeWidth <= 0)
				rangeWidth = 1;
			range.setWidth(rangeWidth + "px");
			_basePanel.add(range, x1, 0);
		}
		else {
			int rangeWidth = x1 - x2;
			if (rangeWidth <= 0)
				rangeWidth = 1;
			range.setWidth(rangeWidth + "px");
			_basePanel.add(range, windowWidth - x1, 0);
		}
	}

	@Override
	public void draw() {
		_basePanel.clear();
		//		if (_windowLeftMargin > 0)
		//			_layoutPanel.getCellFormatter().setWidth(0, 0, _windowLeftMargin + "px");
		_layoutPanel.setWidget(0, 1, _basePanel);

		TrackWindow w = getTrackGroup().getTrackWindow();
		int windowWidth = w.getPixelWidth();
		int sequenceSize = getSequenceSize();
		_ruler.updateTickUnit(windowWidth, 1, sequenceSize);
		_ruler.draw(_basePanel, windowWidth, 1, sequenceSize, w.getStartOnGenome() > w.getEndOnGenome());
		drawTrackSelectionRange(w);
	}

	public AbsoluteFocusPanel getAbsoluteFocusPanel() {
		return _basePanel;
	}

	public void onRangeSelect(int x1OnTrackWindow, int x2OnTrackWindow) {
		TrackWindow window = getTrackGroup().getTrackWindow();
		int width = window.getPixelWidth();
		int sequenceSize = getSequenceSize();
		double genomeLengthPerPixel = (double) sequenceSize / width;

		if (!window.isReverseStrand()) {
			int startOnGenome = (int) (x1OnTrackWindow * genomeLengthPerPixel);
			int endOnGenome = (int) (x2OnTrackWindow * genomeLengthPerPixel);
			getTrackGroup().getPropertyWriter().setTrackWindow(startOnGenome, endOnGenome);
		}
		else {
			int startOnGenome = (int) ((width - x1OnTrackWindow) * genomeLengthPerPixel);
			int endOnGenome = (int) ((width - x2OnTrackWindow) * genomeLengthPerPixel);
			getTrackGroup().getPropertyWriter().setTrackWindow(startOnGenome, endOnGenome);
		}
	}

	@Override
	public void setUp(TrackFrame trackFrame, TrackGroup group) {
		//retrieveSequenceLength();
		TrackWindow w = group.getTrackWindow();

		// set up the configuration panel
		//getConfig().addConfig("Input Window Size (BP)", new IntegerType(UTGBProperty.SEQUENCE_SIZE), Integer.toString(_sequenceSize));
		ValueDomain windowSizeDomain = new ValueDomain();
		windowSizeDomain.addValueList(new Value("1K", "1000"));
		windowSizeDomain.addValueList(new Value("10K", "10000"));
		windowSizeDomain.addValueList(new Value("100K", "100000"));
		windowSizeDomain.addValueList(new Value("1M", "1000000"));
		windowSizeDomain.addValueList(new Value("10M", "10000000"));
		windowSizeDomain.addValueList(new Value("100M", "100000000"));
		windowSizeDomain.addValueList(new Value("1G", "1000000000"));
		getConfig().addConfig("Sequence Ruler Size", new IntegerType(UTGBProperty.SEQUENCE_SIZE, windowSizeDomain), "1000000");

		// set icons 
		trackFrame.pack();
		trackFrame.disablePack();
		trackFrame.disableResize();

	}

	@Override
	public void onChangeTrackConfig(TrackConfigChange change) {
		if (change.contains(UTGBProperty.SEQUENCE_SIZE))
			refresh();
	}

	//	private class SequenceLengthUpdator implements Command {
	//		private int len;
	//
	//		public SequenceLengthUpdator(int sequenceLength) {
	//			this.len = sequenceLength;
	//		}
	//
	//		public void execute() {
	//			setSequenceSize(len);
	//		}
	//	};
	//
	//	private void retrieveSequenceLength() {
	//		TrackGroupProperty property = getTrackGroup().getPropertyReader();
	//		ArrayList<String> queryArg = new ArrayList<String>();
	//		queryArg.add(UTGBProperty.SPECIES + "=" + property.getProperty(UTGBProperty.SPECIES, ""));
	//		queryArg.add(UTGBProperty.REVISION + "=" + property.getProperty(UTGBProperty.REVISION, ""));
	//		queryArg.add(UTGBProperty.TARGET + "=" + property.getProperty(UTGBProperty.TARGET, ""));
	//		String apiURL = "http://utgenome.org/api/sequencelength?" + StringUtil.join(queryArg, "&");
	//		getBrowserService().getHTTPContent(apiURL, new AsyncCallback<String>() {
	//			public void onFailure(Throwable caught) {
	//				GWT.log("sequence size retrieval failed: ", caught);
	//			}
	//
	//			public void onSuccess(String length) {
	//				try {
	//					int sequenceLength = Integer.parseInt(length.trim());
	//					if (sequenceLength != -1) {
	//						DeferredCommand.addCommand(new SequenceLengthUpdator(sequenceLength));
	//					}
	//				}
	//				catch (NumberFormatException e) {
	//					GWT.log(length + " is not a number", null);
	//				}
	//			}
	//		});
	//	}
}
