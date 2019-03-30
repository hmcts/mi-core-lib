package net.hmcts.reform.mi.params;

import net.hmcts.reform.mi.utils.KeyVaultHandler;

public class StagingProperties {

	private int stagingProfileID;

	// Define the identifiers for properties profiles
    public static final int STAGING_PROD = 1; 
    public static final int STAGING_TEST = 2;

    // Variables for handling the properties stored in KeyVault
	private KeyVaultHandler keyVaultHandler;
	
    private String STORAGE_NAME_KEY = "mi-staging-storage-name-key";
    private String STORAGE_TOKEN_KEY = "mi-staging-storage-token-key";
    private String TEST_STORAGE_NAME_KEY = "mi-staging-storage-name-test-key";
    private String TEST_STORAGE_TOKEN_KEY = "mi-staging-storage-token-test-key";

    
	private double stagingVersion = 1.0;
	
	private String insertTimeFormat;	
	private int retentionDays = 30;
	private String blobNameFormat = "yyyyMMdd";
	
	private boolean overwriteBlobs = false;
	
	
	public StagingProperties(int stagingProfileID, KeyVaultHandler kvh) throws MIParamException {
		
		this.stagingProfileID = stagingProfileID;
		this.keyVaultHandler = kvh;
		
		this.insertTimeFormat = "yyyy-MM-dd HH:mmZZ";
		
		switch (stagingProfileID) {
        case StagingProperties.STAGING_PROD: 
        	this.blobNameFormat = "yyyyMMdd";
            break;
        case StagingProperties.STAGING_TEST:  
        	this.blobNameFormat = "yyyyMMddHHmm";
            break;
        default: 
        	throw new MIParamException("Atttempt to configure Staging Properties with unknown profile ID '"+stagingProfileID+"'.");
		}

	}
	
	public String getStorageAccountName() throws MIParamException {

		String storageAccountName = "NOTSET";
				
		switch (stagingProfileID) {
        case StagingProperties.STAGING_PROD: 
        	storageAccountName = this.keyVaultHandler.getSecret(STORAGE_NAME_KEY);
            break;
        case StagingProperties.STAGING_TEST:  
        	storageAccountName = this.keyVaultHandler.getSecret(TEST_STORAGE_NAME_KEY);
            break;
        default: 
        	throw new MIParamException("Atttempt to retrieve Staging storage account name with unknown profile ID '"+stagingProfileID+"'.");
		}

		return storageAccountName;
	}

	public String getStorageConnectionString() throws MIParamException {
		
		String storageTokenName = "NOTSET";
		
		switch (stagingProfileID) {
        case StagingProperties.STAGING_PROD: 
        	storageTokenName = this.keyVaultHandler.getSecret(STORAGE_TOKEN_KEY);
            break;
        case StagingProperties.STAGING_TEST:  
        	storageTokenName = this.keyVaultHandler.getSecret(TEST_STORAGE_TOKEN_KEY);
            break;
        default: 
        	throw new MIParamException("Atttempt to retrieve Staging storage account token with unknown profile ID '"+stagingProfileID+"'.");
		}

		return storageTokenName;
	}
	
	public double getStagingVersion() {
		return stagingVersion;
	}
	public String getInsertTimeFormat() {
		return insertTimeFormat;
	}
	public int getRetentionDays() {
		return retentionDays;
	}
	public String getBlobNameFormat() {
		return blobNameFormat;
	}

	public boolean isOverwriteBlobs() {
		return overwriteBlobs;
	} 
	

	
	
	  	
}
