/*
################################################################################
#                                                                               
# Name : Cache.java       
# Author: Zheng Zhang                                             
# Desc : POJO object class
#                                                                               
#                                                                               
# (C) COPYRIGHT IBM Corporation 2012                                      
# All Rights Reserved.                                                          
#                                                                               
# Licensed Materials-Property of IBM                                            
#                                                                               
################################################################################ 
*/
package org.marker.mushroom.servlet;

import java.io.Serializable;

public class Cache implements Serializable {

	private String key;
	private Object value;
	private long timeOut;
	private boolean expired;

	public Cache() {
		super();
	}

	public Cache(String key, String value, long timeOut, boolean expired) {
		this.key = key;
		this.value = value;
		this.timeOut = timeOut;
		this.expired = expired;
	}

	public String getKey() {
		return key;
	}

	public long getTimeOut() {
		return timeOut;
	}

	public Object getValue() {
		return value;
	}

	public void setKey(String string) {
		key = string;
	}

	public void setTimeOut(long l) {
		timeOut = l;
	}

	public void setValue(Object object) {
		value = object;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean b) {
		expired = b;
	}
}