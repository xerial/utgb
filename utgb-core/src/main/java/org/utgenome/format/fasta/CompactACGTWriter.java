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
// CompactACGTWriter.java
// Since: Feb 22, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.fasta;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;

import org.utgenome.gwt.utgb.client.bio.ACGTEncoder;
import org.xerial.util.StringUtil;

/**
 * ACGT sequence compressor
 * 
 * @author leo
 * 
 */

public class CompactACGTWriter {

	private final OutputStream seqOut;
	private final OutputStream nSeqOut;
	private final int BUFFER_SIZE = 4096;
	private final byte[] seqBuffer = new byte[BUFFER_SIZE];
	private final byte[] nSeqBuffer = new byte[BUFFER_SIZE / 2];
	private int index = 0;
	private long length = 0;
	private final Random rand = new Random(17); // use fixed seed

	public CompactACGTWriter(OutputStream seqOut, OutputStream nSeqOut) {
		this.seqOut = seqOut;
		this.nSeqOut = nSeqOut;

		clearBuffer();
	}

	private void clearBuffer() {
		for (int i = 0; i < seqBuffer.length; ++i)
			seqBuffer[i] = 0;

		for (int i = 0; i < nSeqBuffer.length; ++i)
			nSeqBuffer[i] = 0;
	}

	public long getSequenceLength() {
		return length;
	}

	public void close() throws IOException {
		finish();
		seqOut.close();
		nSeqOut.close();
	}

	private void finish() throws IOException {
		if (index <= 0)
			return;

		seqOut.write(seqBuffer, 0, index / 4 + ((index % 4 > 0) ? 1 : 0));
		nSeqOut.write(nSeqBuffer, 0, index / 8 + ((index % 8 > 0) ? 1 : 0));
		index = 0;

		seqOut.flush();
		nSeqOut.flush();
	}

	void append2bit(byte code) throws IOException {

		if (index >= BUFFER_SIZE * 4) {
			// dump the buffer 
			seqOut.write(seqBuffer, 0, BUFFER_SIZE);
			nSeqOut.write(nSeqBuffer, 0, BUFFER_SIZE / 2);
			clearBuffer();
			index = 0;
		}

		int pos = index / 4;
		int offset = index % 4;

		if (code >= 4) {
			code = (byte) rand.nextInt(4);
			nSeqBuffer[index / 8] |= (byte) (0x01 << (7 - (index % 8)));
		}

		seqBuffer[pos] |= (byte) (code << (6 - offset * 2));
		index++;
		length++;
	}

	public void append(String sequence) throws IOException {
		String t = sequence.trim();
		for (int i = 0; i < t.length(); ++i) {
			append2bit(ACGTEncoder.to2bitCode(t.charAt(i)));
		}
	}

	public void append(char ch) throws IOException {
		append2bit(ACGTEncoder.to2bitCode(ch));
	}

	public static byte to2bitCode(char acgt) {
		return ACGTEncoder.to2bitCode(acgt);
	}

	/**
	 * This method is used to generate source code for the charToACGTCodeTable
	 */
	public static void generateCharTo2BitACGTTable() {

		ArrayList<Byte> buffer = new ArrayList<Byte>();
		for (char c = 0; c < 256; ++c) {
			char u = Character.toUpperCase(c);
			byte code = 0;
			switch (u) {
			case 'A':
				code = 0;
				break;
			case 'C':
				code = 1;
				break;
			case 'G':
				code = 2;
				break;
			case 'T':
			case 'U':
				code = 3;
				break;
			default:
				code = 4;
				break;
			}
			if (buffer.size() >= 16) {
				System.out.println(StringUtil.join(buffer, ", ") + ", ");
				buffer.clear();
			}
			buffer.add(code);
		}
		if (!buffer.isEmpty()) {
			System.out.println(StringUtil.join(buffer, ", "));
		}

	}

}
