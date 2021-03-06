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
// UTGBClientException.java
// Since: 2007/03/28
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client;

import java.io.Serializable;

/**
 * @author leo
 * 
 */
public class UTGBClientException extends Exception implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private UTGBClientErrorCode errorCode = UTGBClientErrorCode.UNKNOWN;

	/**
     * 
     */
	public UTGBClientException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UTGBClientException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public UTGBClientException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public UTGBClientException(Throwable cause) {
		super(cause);
	}

	public UTGBClientException(UTGBClientErrorCode code, String message) {
		super(message);
		this.errorCode = code;
	}

	public UTGBClientErrorCode getErrorCode() {
		return this.errorCode;
	}

}
