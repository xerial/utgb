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
// utgb-shell Project
//
// KeywordTest.java
// Since: May 20, 2010
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import java.io.File;

import org.junit.Test;
import org.utgenome.shell.ProjectGenerator.ProjectInfo;
import org.utgenome.util.TestHelper;
import org.xerial.util.FileResource;
import org.xerial.util.FileUtil;

public class KeywordTest {

	@Test
	public void testExecuteStringArray() throws Exception {

		ProjectInfo tmpProject = ProjectGenerator.createTemporatyProject();

		// import keywords in BED file
		File bed = File.createTempFile("bed", ".bed");
		FileUtil.copy(FileResource.openByteStream(KeywordTest.class, "wormbase-keyword.bed"), bed);
		UTGBShell.runCommand(new String[] { "-d", tmpProject.projectRoot, "keyword", "import", "-r", "ce6", bed.getAbsolutePath() });

		// import alias
		File alias = File.createTempFile("alias", ".txt");
		FileUtil.copy(FileResource.openByteStream(KeywordTest.class, "alias-sample.txt"), alias);
		UTGBShell.runCommand(new String[] { "-d", tmpProject.projectRoot, "keyword", "alias", alias.getAbsolutePath() });

		// try keyword search
		UTGBShell.runCommand(new String[] { "-d", tmpProject.projectRoot, "keyword", "search", "Y74C9" });

	}

	@Test
	public void testSAMKeyword() throws Exception {

		ProjectInfo tmpProject = ProjectGenerator.createTemporatyProject();

		// import keywords in BED file

		File sam = TestHelper.createTempFileFrom(KeywordTest.class, "sample.sam");
		UTGBShell.runCommand(new String[] { "-d", tmpProject.projectRoot, "keyword", "import", "-r", "HG18", sam.getAbsolutePath() });

		// try keyword search
		UTGBShell.runCommand(new String[] { "-d", tmpProject.projectRoot, "keyword", "search", "read_5_2" });
	}

	@Test
	public void testBAMKeyword() throws Exception {

		ProjectInfo tmpProject = ProjectGenerator.createTemporatyProject();

		// import keywords in BED file
		File bam = TestHelper.createTempFileFrom(KeywordTest.class, "sample.bam");
		UTGBShell.runCommand(new String[] { "-d", tmpProject.projectRoot, "keyword", "import", "-r", "HG18", bam.getAbsolutePath() });

		// try keyword search
		UTGBShell.runCommand(new String[] { "-d", tmpProject.projectRoot, "keyword", "search", "read_5_2" });
	}

}
