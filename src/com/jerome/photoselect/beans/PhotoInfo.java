/**
 * PhotoInfo.java
 * com.jerome.photoselect.beans
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-12-26 		Administrator
 *
 * Copyright (c) 2013, JEROME All Rights Reserved.
 */

package com.jerome.photoselect.beans;

import java.io.Serializable;

/**
 * ClassName:PhotoInfo Function: TODO ADD FUNCTION
 * 
 * @author Administrator
 * @version
 * @Date 2013-12-26 上午9:55:08
 * 
 * @see
 */
public class PhotoInfo implements Serializable {

	private static final long serialVersionUID = 3023835118039305363L;
	private String path;
	private boolean state;

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public PhotoInfo(String path) {
		super();
		this.path = path;
	}

}
