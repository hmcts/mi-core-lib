package net.hmcts.reform.mi.params;

import net.hmcts.reform.mi.utils.KeyVaultHandler;

public class StagingProperties {

	private int stagingProfileID;
	private int dsProfileID;

	private KeyVaultHandler keyVaultHandler;
	
	// Define the identifiers for properties profiles
    public static final int STAGING_PROD = 1; 
    public static final int STAGING_TEST = 2;

	// Define the identifiers for the different data sources in staging
    public static final int CCD_METADATA_EMAIL = 100; 
    public static final int CCD_DIVORCE_EMAIL = 101; 
    public static final int CCD_PROBATE_GOR_EMAIL = 102; 
    public static final int CCD_PROBATE_CAV_EMAIL = 103; 

    public static final int PAYMENT_CARD_EMAIL = 301; 
    public static final int PAYMENT_CREDIT_DIVORCE_EMAIL = 302; 
    public static final int PAYMENT_CREDIT_CMC_EMAIL = 303;  
    
    public static final int NOTIFY_DIVORCE_API = 401; 
    public static final int NOTIFY_PROBATE_API = 402; 
    public static final int NOTIFY_CMC_API = 403; 
    public static final int NOTIFY_SSCS_API = 404; 
    public static final int NOTIFY_TIDAM_API = 405; 

    public static final int EXE_CTSC_INTERACTIONS_API = 501;

    public static final int BILL_API = 999;
    
    
    private String stagingContainerName;
    
	
    private String STORAGE_NAME_KEY = "mi-staging-storage-name-key";
    private String STORAGE_TOKEN_KEY = "mi-staging-storage-token-key";
    private String TEST_STORAGE_NAME_KEY = "mi-staging-storage-name-test-key";
    private String TEST_STORAGE_TOKEN_KEY = "mi-staging-storage-token-test-key";
	
	private String insertTimeFormat;	
	private int retentionDays = 30;
	private String blobNameFormat = "yyyyMMdd";
	private String blobNameRegex = "[0-9]{8}"; 
	private boolean overwriteBlobs = false;
	
	
	public StagingProperties(int stagingProfileID, int dsProfileID, KeyVaultHandler kvh) throws MIParamException {
		
		this.stagingProfileID = stagingProfileID;
		this.dsProfileID = dsProfileID;
		this.keyVaultHandler = kvh;
		
		this.setStagingProfileProperties();
		this.setDSProfileProperties();

	}
	
	
	private void setStagingProfileProperties() throws MIParamException {

		this.insertTimeFormat = "yyyy-MM-dd HH:mmZZ";
		
		switch (this.stagingProfileID) {
        case StagingProperties.STAGING_PROD: 
        	this.blobNameFormat = "yyyyMMdd";
        	this.blobNameRegex = "[0-9]{8}";
            break;
        case StagingProperties.STAGING_TEST:  
        	this.blobNameFormat = "yyyyMMdd";
        	this.blobNameRegex = "[0-9]{8}";
//        	this.blobNameFormat = "yyyyMMddHH";
//        	this.blobNameRegex = "[0-9]{10}";
            break;
        default: 
        	throw new MIParamException("Atttempt to configure Staging Properties with unknown Staging Profile ID '"+this.stagingProfileID+"'.");
		}

	}
	
	private void setDSProfileProperties() throws MIParamException {
		
		
		switch (this.dsProfileID) {
        case StagingProperties.CCD_METADATA_EMAIL: 
        	this.stagingContainerName = "ccdstagingmetadata";
            break;
        case StagingProperties.CCD_DIVORCE_EMAIL:  
        	this.stagingContainerName = "ccdstagingdivorce";
            break;
        case StagingProperties.CCD_PROBATE_GOR_EMAIL:  
        	this.stagingContainerName = "ccdstagingprobategor";
            break;
        case StagingProperties.CCD_PROBATE_CAV_EMAIL:  
        	this.stagingContainerName = "ccdstagingprobatecav";
            break;
        case StagingProperties.EXE_CTSC_INTERACTIONS_API:  
        	this.stagingContainerName = "exestaging";
            break;
        case StagingProperties.NOTIFY_DIVORCE_API:  
        	this.stagingContainerName = "notifystagingdivorce";
            break;
        case StagingProperties.NOTIFY_PROBATE_API:  
        	this.stagingContainerName = "notifystagingprobate";
            break;
        case StagingProperties.NOTIFY_CMC_API:  
        	this.stagingContainerName = "notifystagingcmc";
            break;
        case StagingProperties.NOTIFY_SSCS_API:  
        	this.stagingContainerName = "notifystagingsscs";
            break;
        case StagingProperties.NOTIFY_TIDAM_API:  
        	this.stagingContainerName = "notifystagingtidam";
            break;
        case StagingProperties.PAYMENT_CARD_EMAIL:  
        	this.stagingContainerName = "paymentstagingcard";
            break;
        case StagingProperties.PAYMENT_CREDIT_DIVORCE_EMAIL:  
        	this.stagingContainerName = "paymentstagingcreditdivorce";
            break;
        case StagingProperties.PAYMENT_CREDIT_CMC_EMAIL:  
        	this.stagingContainerName = "paymentstagingcreditcmc";
            break;
        case StagingProperties.BILL_API:  
        	this.stagingContainerName = "billapidata";
            break;
        default: 
        	throw new MIParamException("Atttempt to configure Staging Data Source Properties with unknown DS Profile ID '"+this.dsProfileID+"'.");
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
	
	public String getInsertTimeFormat() {
		return insertTimeFormat;
	}
	public int getRetentionDays() {
		return retentionDays;
	}
	public String getStagingContainerName() {
		return stagingContainerName;
	}
	public String getBlobNameFormat() {
		return blobNameFormat;
	}
	public String getBlobNameRegex() {
		return blobNameRegex;
	}
	public boolean isOverwriteBlobs() {
		return overwriteBlobs;
	} 
	
	  	
}
