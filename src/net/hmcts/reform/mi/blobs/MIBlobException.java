package net.hmcts.reform.mi.blobs;

import java.text.SimpleDateFormat;

public class MIBlobException extends Exception {


	private static final long serialVersionUID = 416609024263879758L;

	public MIBlobException(String message) {
		super("("+new SimpleDateFormat("yyyyMMdd-HHmmss").format(new java.util.Date())+") "+message);
	}

	public MIBlobException(String message, Throwable cause) {
		super("("+new SimpleDateFormat("yyyyMMdd-HHmmss").format(new java.util.Date())+") "+message, cause);
	}


}
