/*--------------------------------------------------------------------------
 *  Copyright 2008 utgenome.org
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
// Gene.java
// Since: Jul 8, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell.db;

import java.util.ArrayList;
import java.util.List;

/**
 * Gene with Exon and CDS regions
 * 
 * @author leo
 * 
 */
public class Gene extends Locus {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<Exon> exonList = new ArrayList<Exon>();
	ArrayList<CDS> cdsList = new ArrayList<CDS>();

	public Gene() {
	}

	public Gene(long start, long end) {
		super(start, end);
	}

	public Gene(String name, long start, long end) {
		super(name, start, end);
	}

	public void addExon(Exon exon) {
		this.exonList.add(exon);
	}

	public List<Exon> getExon() {
		return exonList;
	}

	public void addCDS(CDS cds) {
		cdsList.add(cds);
	}

	public List<CDS> getCDS() {
		return cdsList;
	}

}