package net.hmcts.reform.mi.utils;


import net.hmcts.reform.mi.params.MIParamException;
import net.hmcts.reform.mi.utils.KeyVaultHandler;

public class EmailClientProperties {


    // Variables for handling the properties stored in KeyVault
	private KeyVaultHandler keyVaultHandler;
    private String MAILBOX_USER_KEY = "mi-ds-mailbox-user-key";
    private String MAILBOX_PW_KEY = "mi-ds-mailbox-pw-key";

    // Fairly fixed email connection settings
	private String emailConnHostName = "smtp.office365.com";
	private int emailConnPort = 587;

	
    public EmailClientProperties(KeyVaultHandler kvh) throws MIParamException {
       	this.keyVaultHandler = kvh;
    }
    
	public String getMailboxUser() {
		return this.keyVaultHandler.getSecret(MAILBOX_USER_KEY);
	}

	public String getMailboxPW() {
		return this.keyVaultHandler.getSecret(MAILBOX_PW_KEY);
	}

	public String getEmailConnHostName() {
		return emailConnHostName;
	}

	public int getEmailConnPort() {
		return emailConnPort;
	}

	
}
