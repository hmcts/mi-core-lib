package net.hmcts.reform.mi.utils;

import java.text.SimpleDateFormat;

public class MIUtilsException extends Exception {

	private static final long serialVersionUID = 1461154459026457615L;

	public MIUtilsException(String message) {
		super("("+new SimpleDateFormat("yyyyMMdd-HHmmss").format(new java.util.Date())+") "+message);
	}

	public MIUtilsException(String message, Throwable cause) {
		super("("+new SimpleDateFormat("yyyyMMdd-HHmmss").format(new java.util.Date())+") "+message, cause);
	}


}
