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
// BEDViewer.java
// Since: 2009/05/19
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.server.app;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.utgenome.format.bed.BED2SilkReader;
import org.utgenome.graphics.GeneCanvas;
import org.utgenome.graphics.GenomeWindow;
import org.utgenome.gwt.utgb.client.bio.CDS;
import org.utgenome.gwt.utgb.client.bio.Exon;
import org.utgenome.gwt.utgb.client.bio.Gene;
import org.utgenome.gwt.utgb.server.WebTrackBase;
import org.xerial.db.sql.ResultSetHandler;
import org.xerial.db.sql.sqlite.SQLiteAccess;
import org.xerial.lens.Lens;
import org.xerial.util.log.Logger;

public class BEDViewer extends WebTrackBase implements Serializable{

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private static Logger _logger = Logger.getLogger(BEDViewer.class);

	private String species = "human";
	private String revision = "hg18";
	private String name = "chr22";
	private long start = 1;
	private long end = 1000000;
	private int width = 700;
	private String fileName;

	private List<Gene> geneList = new ArrayList<Gene>();

	@SuppressWarnings("unchecked")
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		BED2SilkReader in = null;
		
		long sqlStart = end >= start ? start : end;
		long sqlEnd   = end >= start ? end : start;

		try {

			File input = new File(getProjectRootPath(), fileName);
			File dbInput = new File(input.getAbsolutePath() + ".sqlite");
			if (dbInput.exists()) {
				// use db
				SQLiteAccess dbAccess = new SQLiteAccess(dbInput.getAbsolutePath());

				String sql = createSQLStatement("select start, end, name, score, strand, cds, exon, color from gene " +
						"where coordinate = '$1' and start <= $2 and end >= $3", name, sqlEnd, sqlStart);

				if (_logger.isDebugEnabled())
					_logger.debug(sql);
				dbAccess.query(sql, new ResultSetHandler() {
					@Override
					public Object handle(ResultSet rs) throws SQLException {
						BEDGene gene = new BEDGene();
						gene.coordinate = name;
						gene.setStart(rs.getLong(1));
						gene.setEnd(rs.getLong(2));

						gene.setName(rs.getString(3));
						gene.score = rs.getInt(4);
						gene.setStrand(rs.getString(5));

						ArrayList<long[]> regionList = readRegions(rs.getString(6));
						for(long[] region : regionList){
							CDS cds = new CDS(region[0], region[1]);
							gene.addCDS(cds);
						}
						
						regionList = readRegions(rs.getString(7));
						for(long[] region : regionList){
							Exon exon = new Exon(region[0], region[1]);
							gene.addExon(exon);
						}

						gene.setColor(rs.getString(8));

						geneList.add(gene);
						return null;
					}

					private ArrayList<long[]> readRegions(String string) {
						ArrayList<long[]> res = new ArrayList<long[]>();

						StringTokenizer st = new StringTokenizer(string,"[] ,");
						while (st.hasMoreTokens()) {
							String str = st.nextToken();
							
							// get start of region
							if(str.startsWith("(")){
								long[] region = new long[2];
								region[0] = Long.valueOf(str.substring(1)).longValue();

								// get end of region
								while(st.hasMoreTokens()){
									str = st.nextToken();
									if(str.endsWith(")")){
										region[1] = Long.valueOf(str.substring(0, str.length() - 1)).longValue();
										res.add(region);
										break;
									}
								}
							}
						}
						return res;
					}
				});
			}
			else {
				// use raw text
				in = new BED2SilkReader(new FileReader(input));
				BEDQuery query = new BEDQuery(name, start, end);

				Lens.loadSilk(query, in);

			}

			GeneCanvas geneCanvas = new GeneCanvas(width, 300, new GenomeWindow(start, end));
			geneCanvas.draw(geneList);

			response.setContentType("image/png");
			geneCanvas.toPNG(response.getOutputStream());

		}
		catch (Exception e) {
			_logger.error(e);
		}
		finally {
			if (in != null)
				in.close();
		}
	}

	public class BEDQuery {
		private String coordinate;
		private long start;
		private long end;

		public BEDQuery(String coordinate, long start, long end) {
			this.coordinate = coordinate;
			this.start = end >= start ? start : end;
			this.end = end >= start ? end : start;
		}

		public BEDTrack track;

		public void addGene(BEDGene gene) {
			long geneStart = gene.getEnd() >= gene.getStart() ? gene.getStart() : gene.getEnd();
			long geneEnd = gene.getEnd() >= gene.getStart() ? gene.getEnd() : gene.getStart();

			if (gene.coordinate.equals(coordinate) && (start <= geneEnd) && (end >= geneStart)) {
				geneList.add(gene);
			}
		}
	}

	public static class BEDTrack implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String name;
		public String description;
		public int visibility;
		public String color;
		public String itemRgb;
		public int useScore;
		public String group;
		public String priority;
		public String db;
		public long offset;
		public String url;
		public String htmlUrl;

		@Override
		public String toString() {
			return String
					.format(
							"track:name=%s, description=%s, visibility=%d, color=%s, itemRgb=%s, useScore=%d, group=%s, priority=%s, db=%s, offset=%d, url=%s, htmlUrl=%s\n",
							name, description, visibility, color, itemRgb, useScore, group, priority, db, offset, url, htmlUrl);
		}
	}

	public static class BEDGene extends Gene implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String coordinate;
		public int score;
		
		// public void addCDS(CDS cds)
		// {
		// this.cdsList.add(cds);
		// }
		//		
		// public void addExon(Exon exon)
		// {
		// this.exon.add(exon);
		// }
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return String.format("%s: %s:%d-%d\t%s\t%s\t%s\t%s\t%d", getName(), coordinate, getStart(), getEnd(), getStrand(), getCDS(), getExon(), getColor(), score);
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}