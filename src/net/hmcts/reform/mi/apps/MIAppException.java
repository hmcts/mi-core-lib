package net.hmcts.reform.mi.apps;

import java.text.SimpleDateFormat;

public class MIAppException extends Exception {


	private static final long serialVersionUID = -4504816528573651628L;

	public MIAppException(String message) {
		super("("+new SimpleDateFormat("yyyyMMdd-HHmmss").format(new java.util.Date())+") "+message);
	}

	public MIAppException(String message, Throwable cause) {
		super("("+new SimpleDateFormat("yyyyMMdd-HHmmss").format(new java.util.Date())+") "+message, cause);
	}


}
