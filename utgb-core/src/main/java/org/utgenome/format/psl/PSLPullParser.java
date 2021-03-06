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
// UTGB Common Project
//
// PSLPullParser.java
// Since: Jun 5, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.psl;

import java.io.BufferedReader;

/**
 * PSL lines represent alignments, and are typically taken from files generated by BLAT or psLayout. See the BLAT documentation for more details. All of the following fields are required on each data
 * line within a PSL file:
 * 
 * <pre>
 *  1. matches - Number of bases that match that aren't repeats
 *  2. misMatches - Number of bases that don't match
 *  3. repMatches - Number of bases that match but are part of repeats
 *  4. nCount - Number of 'N' bases
 *  5. qNumInsert - Number of inserts in query
 *  6. qBaseInsert - Number of bases inserted in query
 *  7. tNumInsert - Number of inserts in target
 *  8. tBaseInsert - Number of bases inserted in target
 *  9. strand - '+' or '-' for query strand. In mouse, second '+'or '-' is for genomic strand
 *  10. qName - Query sequence name
 *  11. qSize - Query sequence size
 *  12. qStart - Alignment start position in query
 *  13. qEnd - Alignment end position in query
 *  14. tName - Target sequence name
 *  15. tSize - Target sequence size
 *  16. tStart - Alignment start position in target
 *  17. tEnd - Alignment end position in target
 *  18. blockCount - Number of blocks in the alignment
 *  19. blockSizes - Comma-separated list of sizes of each block
 *  20. qStarts - Comma-separated list of starting positions of each block in query
 *  21. tStarts - Comma-separated list of starting positions of each block in target
 * </pre>
 * 
 * @author leo
 * 
 */
public class PSLPullParser {
	
	BufferedReader _reader;
	
}
