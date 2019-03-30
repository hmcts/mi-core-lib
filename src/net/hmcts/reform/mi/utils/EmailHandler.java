package net.hmcts.reform.mi.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;


public class EmailHandler {

	private EmailClientProperties emailClientProperties;
		
	private String subject = "";
	private String message = "";
	private ArrayList<String> recipients = new ArrayList<String>(8);
	
	
	
	public EmailHandler(EmailClientProperties emailClientProperties) {
		this.emailClientProperties = emailClientProperties;
	}
	
	
	
	public void send() throws EmailException {
		
		Email email = new SimpleEmail();

		email.setHostName(this.emailClientProperties.getEmailConnHostName());
		email.setSmtpPort(this.emailClientProperties.getEmailConnPort());
		email.setAuthenticator(new DefaultAuthenticator(this.emailClientProperties.getMailboxUser(), this.emailClientProperties.getMailboxPW()));
		email.setStartTLSEnabled(true);
		email.setDebug(false);

		email.setFrom(this.emailClientProperties.getMailboxUser());
		for (int i=0; i<this.recipients.size(); i++) {
			email.addTo(this.recipients.get(i));			
		}

		email.setSubject(this.subject);
		email.setMsg(this.message);
		
		email.send();

	}

	public static void main(String[] args) {
		try {

			String keyVaultName = "p1p-kv-test";
			KeyVaultHandler kvh = new KeyVaultHandler(keyVaultName);

			// Need to uncomment this to run locally, make sure 'HMCTS-MI-PARAMS Project is on the build path - NOT Maven!!
			//			kvh.setSpCredentials(KeyVaultProperties.getSPCredentials());

			String timestamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new java.util.Date());
			
    		EmailHandler eh = new EmailHandler(new EmailClientProperties(kvh));
    		eh.addRecipient("tim.davies@hmcts.net");
    		eh.addRecipient("tim.davies@solirius.com");
    		eh.setSubject("Email Handler Test Message ("+timestamp+")");
    		
    		eh.setMessage("Blah Blah Blah");
    		eh.send();

	} catch (Exception e) {
	    e.printStackTrace();
	}

	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ArrayList<String> getRecipients() {
		return recipients;
	}

	public void setRecipient(ArrayList<String> recipientList) {
		this.recipients = recipientList;
	}

	public void addRecipient(String recipient) {
		this.recipients.add(recipient);
	}

}
