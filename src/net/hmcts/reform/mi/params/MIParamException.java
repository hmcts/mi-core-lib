package net.hmcts.reform.mi.params;

import java.text.SimpleDateFormat;

public class MIParamException extends Exception {

	private static final long serialVersionUID = -8459560092325793429L;

	public MIParamException(String message) {
		super("("+new SimpleDateFormat("yyyyMMdd-HHmmss").format(new java.util.Date())+") "+message);
	}

	public MIParamException(String message, Throwable cause) {
		super("("+new SimpleDateFormat("yyyyMMdd-HHmmss").format(new java.util.Date())+") "+message, cause);
	}


}
