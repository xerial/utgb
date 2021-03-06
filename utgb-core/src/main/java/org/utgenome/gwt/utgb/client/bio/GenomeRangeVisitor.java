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
// OnGenomeDataVisitor.java
// Since: May 16, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.bio;

/**
 * Visitor interface for traversing data mapped onto a genome sequence
 * 
 * @author leo
 * 
 */
public interface GenomeRangeVisitor {

	public void visitDefault(GenomeRange g);

	public void visitInterval(Interval interval);

	public void visitRead(Read r);

	public void visitGap(Gap p);

	public void visitGene(Gene g);

	public void visitBEDGene(BEDGene g);

	public void visitSAMRead(SAMRead r);

	public void visitSAMReadLight(SAMReadLight r);

	public void visitSAMReadPair(SAMReadPair pair);

	public void visitSAMReadPairFragment(SAMReadPairFragment fragment);

	public void visitSequence(ReferenceSequence referenceSequence);

	public void visitReadCoverage(ReadCoverage readCoverage);

	public void visitGraph(GraphData graph);

	public void visitReadList(ReadList readList);
}
