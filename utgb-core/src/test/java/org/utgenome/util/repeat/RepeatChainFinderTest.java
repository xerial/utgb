/*--------------------------------------------------------------------------
 *  Copyright 2010 utgenome.org
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
// RepeatChainFinderTest.java
// Since: 2010/10/19
//
//--------------------------------------
package org.utgenome.util.repeat;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.utgenome.util.repeat.RepeatChainFinder.Interval2D;
import org.xerial.util.IndexedSet;

public class RepeatChainFinderTest {

	@Test
	public void testInterval2DHash() {

		Interval2D i1 = new Interval2D(0, 1, 4, 5);
		Interval2D i2 = new Interval2D(0, 1, 4, 5);
		assertEquals(i1.hashCode(), i2.hashCode());
		assertEquals(0, i1.compareTo(i2));

		IndexedSet<Interval2D> s = new IndexedSet<Interval2D>();
		assertEquals(true, s.add(i1));
		assertEquals(false, s.add(i2));

		assertEquals(0, s.getID(i1));
		assertEquals(0, s.getID(i2));
	}

}
