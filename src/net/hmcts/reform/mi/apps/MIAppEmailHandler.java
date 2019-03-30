package net.hmcts.reform.mi.apps;

import java.text.SimpleDateFormat;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.mail.EmailException;

import net.hmcts.reform.mi.params.MIParamException;
import net.hmcts.reform.mi.utils.EmailClientProperties;
import net.hmcts.reform.mi.utils.EmailHandler;
import net.hmcts.reform.mi.utils.KeyVaultHandler;

public class MIAppEmailHandler {

	private EmailHandler eh;
	private String emailMsg = "";
	
	public final String tab = "   ";
	public final String nl = "\n";

	
	public MIAppEmailHandler(String appName, KeyVaultHandler kvh) throws MIParamException {

		String timestamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new java.util.Date());

		this.eh = new EmailHandler(new EmailClientProperties(kvh));
		this.eh.setSubject(appName+" ("+timestamp+")");
		this.emailMsg = appName+" executed at "+timestamp+nl+nl;
	}
		
	public void addRecipient(String recipient) {
		eh.addRecipient(recipient);		
	}
	
	public void updateMessage(String msgUpdate) {
		this.emailMsg += msgUpdate;
	}
	
	public void updateMessage(Throwable ex) {
		this.emailMsg += nl+nl+"Exception occurred:"+nl
				+ ExceptionUtils.getFullStackTrace(ex)
				+ nl+nl+nl;
	}

	public void send() throws EmailException {
		this.eh.setMessage(emailMsg);
		this.eh.send();
	}
}
