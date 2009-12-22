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
// UTGBPortableConfig.java
// Since: Sep 2, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.shell;

import org.xerial.core.XerialException;
import org.xerial.json.JSONObject;
import org.xerial.util.bean.BeanUtil;
import org.xerial.util.opt.Option;

/**
 * The configuration parameters of the UTGBPortable
 * 
 * @author leo
 * 
 */
public class UTGBPortableConfig {

	@Option(symbol = "p", longName = "port", varName = "PORT_NUMBER", description = "specify the port number of the local web server (default = 8989)")
	int portNumber = 8989;

	@Option(symbol = "c", longName = "contextPath", varName = "PATH", description = "context path. default=/utgb")
	String contextPath = null;

	String projectRoot = null;

	@Option(symbol = "w", longName = "webapp", varName = "DIR", description = "webapp (web application) directory. default = src/main/webapp")
	String webAppDir = UTGBShellCommand.WEBAPP_FOLDER;

	@Option(symbol = "t", longName = "workdir", varName = "DIR", description = "working directory (tomcat base). default= target/utgb")
	String workingDir = UTGBShellCommand.EXPLODED_WEBAPP_DIR;

	String projectConfigDir = "config";

	@Option(symbol = "v", longName = "verbose", description = "display verbose log message")
	boolean isVerbose = false;

	@Option(symbol = "g", description = "launch in GUI mode")
	boolean useGUI = false;

	@Option(symbol = "h", longName = "help", description = "display help message")
	boolean displayHelp = false;

	public UTGBPortableConfig() {
	}

	public UTGBPortableConfig(int portNumber, String contextPath, String projectRoot) {
		this.portNumber = portNumber;
		this.contextPath = contextPath;
		this.projectRoot = projectRoot;
	}

	public String getServerURL() {
		return String.format("http://localhost:%d%s", portNumber, contextPath);
	}

	public String generateServerURLLinkHTML() {
		String serverURL = getServerURL();
		return String.format("<html><a href=\"%s\">%s</a></html>", serverURL, serverURL);
	}

	@Override
	public String toString() {
		try {
			JSONObject json = BeanUtil.toJSONObject(this);
			return json.toString();
		}
		catch (XerialException e) {
			throw new IllegalStateException(e);
		}
	}

	public int getPortNumber() {
		return portNumber;
	}

	public String getContextPath() {
		return contextPath;
	}

	public String getProjectRoot() {
		return projectRoot;
	}

	public String getWebAppDir() {
		return webAppDir;
	}

	public String getWorkingDir() {
		return workingDir;
	}

	public String getProjectConfigDir() {
		return projectConfigDir;
	}

	public boolean isVerbose() {
		return isVerbose;
	}

	public boolean isUseGUI() {
		return useGUI;
	}

	public boolean isDisplayHelp() {
		return displayHelp;
	}

}