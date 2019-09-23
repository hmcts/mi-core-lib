package net.hmcts.reform.mi.params;

import net.hmcts.reform.mi.utils.KeyVaultHandler;

public class LakeProperties {
	
	private int lakeProfileID;
	private int dataProfileID;

	private KeyVaultHandler keyVaultHandler;

	// Define the identifiers for properties profiles
    public static final int LAKE_PROD = 1; 
    public static final int LAKE_TEST = 2;

	// Define the identifiers for the different data sources in lake
    public static final int CCD_METADATA = 100; 
    public static final int CCD_CASE_DIVORCE = 101; 
    public static final int CCD_CASE_PROBATE_GOR = 102; 
    public static final int CCD_CASE_PROBATE_CAV = 103; 
    public static final int CCD_CASE_PROBATE_STS = 104; 
    public static final int CCD_CASE_PROBATE_WLL = 105; 
    public static final int CCD_CASE_ET_SINGLE = 106; 
    public static final int CCD_CASE_ET_MULTIPLE = 107; 

    
    public static final int PAYMENTS = 301;   
    
    public static final int NOTIFY = 401; 

    public static final int EXE_CTSC_INTERACTIONS = 501;

	
    private String LAKE_NAME_KEY = "mi-lake-storage-name-key";
    private String LAKE_TOKEN_KEY = "mi-lake-storage-token-key";
    private String TEST_LAKE_NAME_KEY = "mi-lake-storage-name-test-key";
    private String TEST_LAKE_TOKEN_KEY = "mi-lake-storage-token-test-key";


	private String blobNameFormat = "yyyyMM";
	private String blobNameRegex = "[0-9]{6}"; 
	public static final String LAKE_CSV_CONTENT_TYPE = "text/csv";
	
    private String lakeContainerName;
	private String dataSortFieldKey;
	private String dataSortFieldFormat;
	private String[] dataCSVHeaders;

	private final String[] CCD_METADATA_LAKE_CSVHEADERS = { "md_uuid","md_staging_version","md_insert_time","md_source_id","md_retention","md_retention_exempt","extraction_date","ce_id","ce_case_data_id","ce_created_date","ce_case_type_id","ce_case_type_version","ce_event_id","ce_event_name","ce_state_id","ce_state_name","cd_created_date","cd_last_modified","cd_jurisdiction","cd_reference" };
	private final String[] CCD_DIVORCE_LAKE_CSVHEADERS = { "md_uuid","md_staging_version","md_insert_time","md_source_id","md_retention","md_retention_exempt","extraction_date","case_metadata_event_id","ce_case_data_id","ce_created_date","ce_case_type_id","ce_case_type_version","ce_divorce_unit","ce_contact_confidential","ce_need_help_with_fees","ce_financial_order","ce_reason_for_divorce","ce_divorce_claim_from","ce_adultery_wish_to_name","ce_link_case_reference" };
	private final String[] CCD_PROBATE_GOR_LAKE_CSVHEADERS = { "md_uuid","md_staging_version","md_insert_time","md_source_id","md_retention","md_retention_exempt", "extraction_date","case_metadata_event_id","ce_case_data_id","ce_created_date","ce_case_type_id","ce_case_type_version","ce_app_type","ce_app_sub_date","ce_reg_location","ce_will_exists","ce_iht_net_value","ce_iht_gross_value","ce_deceased_dod","ce_deceased_other_names","ce_case_stop_reason","ce_case_stop_reason_cnt","ce_gor_case_type","ce_paperform_ind","ce_grantissued_date","ce_leg_record_id" };

	private final String[] CCD_PROBATE_CAV_LAKE_CSVHEADERS = { "md_uuid","md_staging_version","md_insert_time","md_source_id","md_retention","md_retention_exempt", "extraction_date","case_metadata_event_id","ce_case_data_id","ce_created_date","ce_case_type_id","ce_case_type_version","ce_expiry_date","ce_app_type","ce_reg_location","ce_deceased_dod","ce_leg_record_id","ce_app_sub_date","ce_paperform" };
	private final String[] CCD_PROBATE_STS_LAKE_CSVHEADERS = { "md_uuid","md_staging_version","md_insert_time","md_source_id","md_retention","md_retention_exempt", "extraction_date","case_metadata_event_id","ce_case_data_id","ce_created_date","ce_case_type_id","ce_case_type_version","ce_app_type","ce_reg_location","ce_expiry_date","ce_leg_record_id","ce_app_sub_date" };
	private final String[] CCD_PROBATE_WLL_LAKE_CSVHEADERS = { "md_uuid","md_staging_version","md_insert_time","md_source_id","md_retention","md_retention_exempt", "extraction_date","case_metadata_event_id","ce_case_data_id","ce_created_date","ce_case_type_id","ce_case_type_version","ce_app_type","ce_reg_location","ce_lodgement_type","ce_lodgement_date","ce_withdrawal_reason","ce_leg_record_id" };

	private final String[] CCD_ET_SINGLE_LAKE_CSVHEADERS = { "md_uuid","md_staging_version","md_insert_time","md_source_id","md_retention","md_retention_exempt", "extraction_date","case_metadata_event_id","ce_case_data_id","ce_created_date","ce_case_type_id","ce_case_type_version","ce_case_state","ce_case_type","ce_receipt_date","ce_position_type","ce_multiple_ref","ce_ethos_case_ref" };
	private final String[] CCD_ET_MULTIPLE_LAKE_CSVHEADERS = { "md_uuid","md_staging_version","md_insert_time","md_source_id","md_retention","md_retention_exempt", "extraction_date","case_metadata_event_id","ce_case_data_id","ce_created_date","ce_case_type_id","ce_case_type_version","ce_blk_case_title","ce_multiple_ref","ce_multiple_coll_cnt" };

	private final String[] PAYMENTS_LAKE_CSVHEADERS = { "md_uuid","md_staging_version","md_insert_time","md_source_id","md_retention","md_retention_exempt","service","payment_group_reference","payment_reference","ccd_reference","case_reference","organisation_name","customer_internal_reference","pba_number","payment_created_date","payment_status_updated_date","payment_status","payment_channel","payment_method","payment_amount","site_id","fee_code","version","calculated_amount","memo_line","nac","fee_volume" };
	private final String[] NOTIFY_LAKE_CSVHEADERS = { "md_uuid","md_staging_version","md_insert_time","md_source_id","md_retention","md_retention_exempt","notification_id","service","reference","type","template_id","template_version","template_name","status","created_timestamp","sent_timestamp","completed_timestamp","estimated_timestamp" };
	private final String[] EXE_LAKE_CSVHEADERS = { "md_uuid","md_staging_version","md_insert_time","md_source_id","md_retention","md_retention_exempt", "media-type","channel-obj-id","queue-name","interaction-id","orig-interaction-id","origination","destination-original","destination-translated","customer-name","case-id","interaction-direction","interaction-type","dial-code","dial-text","create-timestamp","agent-id","agent-name","accept-timestamp","process-time","post-process-time","total-time","abandon-timestamp","voice-message-left","recording-filename","ivr-treatment-time","transfer-from","conference-from","max-hold-time","hold-count","total-hold-time","call-leg1-post-dial-delay","call-leg2-post-dial-delay","call-leg1-answer-time","call-leg2-answer-time","call-leg1-sip-id","call-leg2-sip-id","inbound-sip-id","notes","campaign-name","record-id","record-status","disposition-code","ext-trans-data","wrap-up-code","wrap-up-text","agent-accept-timestamp","agent-abandon-timestamp","trans-rec-num","is-interaction-completed","null-header" };

	
	public LakeProperties(int lakeProfileID, int dataProfileID, KeyVaultHandler kvh) throws MIParamException {
		
		this.lakeProfileID = lakeProfileID;
		this.dataProfileID = dataProfileID;
		this.keyVaultHandler = kvh;
		
		this.setLakeProfileProperties();
		this.setDataProfileProperties();

	}

	private void setLakeProfileProperties() throws MIParamException {
		
		switch (this.lakeProfileID) {
        case LakeProperties.LAKE_PROD: 
        	this.blobNameFormat = "yyyyMM";
        	this.blobNameRegex = "[0-9]{6}";
            break;
        case LakeProperties.LAKE_TEST:  
        	this.blobNameFormat = "yyyyMMdd";
        	this.blobNameRegex = "[0-9]{8}";
            break;
        default: 
        	throw new MIParamException("Atttempt to configure Lake Properties with unknown Lake Profile ID '"+this.lakeProfileID+"'.");
		}

	}
	
	private void setDataProfileProperties() throws MIParamException {
		

		switch (this.dataProfileID) {
        case LakeProperties.CCD_METADATA: 
        	this.lakeContainerName = "ccdmetadatap1plus";
        	this.dataSortFieldKey = "ce_created_date";
        	this.dataSortFieldFormat = "yyyy-MM";
        	this.dataCSVHeaders = CCD_METADATA_LAKE_CSVHEADERS;
            break;
        case LakeProperties.CCD_CASE_DIVORCE:  
        	this.lakeContainerName = "ccddivorcep1plus";
        	this.dataSortFieldKey = "ce_created_date";
        	this.dataSortFieldFormat = "yyyy-MM";
        	this.dataCSVHeaders = CCD_DIVORCE_LAKE_CSVHEADERS;
            break;
        case LakeProperties.CCD_CASE_PROBATE_GOR:  
        	this.lakeContainerName = "ccdprobategorp1plus";
        	this.dataSortFieldKey = "ce_created_date";
        	this.dataSortFieldFormat = "yyyy-MM";
        	this.dataCSVHeaders = CCD_PROBATE_GOR_LAKE_CSVHEADERS;
            break;
        case LakeProperties.CCD_CASE_PROBATE_CAV:  
        	this.lakeContainerName = "ccdprobatecavp1plus";
        	this.dataSortFieldKey = "ce_created_date";
        	this.dataSortFieldFormat = "yyyy-MM";
        	this.dataCSVHeaders = CCD_PROBATE_CAV_LAKE_CSVHEADERS;
            break;
        case LakeProperties.CCD_CASE_PROBATE_STS:  
        	this.lakeContainerName = "ccdprobatestsp1plus";
        	this.dataSortFieldKey = "ce_created_date";
        	this.dataSortFieldFormat = "yyyy-MM";
        	this.dataCSVHeaders = CCD_PROBATE_STS_LAKE_CSVHEADERS;
            break;
        case LakeProperties.CCD_CASE_PROBATE_WLL:  
        	this.lakeContainerName = "ccdprobatewllp1plus";
        	this.dataSortFieldKey = "ce_created_date";
        	this.dataSortFieldFormat = "yyyy-MM";
        	this.dataCSVHeaders = CCD_PROBATE_WLL_LAKE_CSVHEADERS;
            break;
        case LakeProperties.CCD_CASE_ET_SINGLE:  
        	this.lakeContainerName = "ccdetsinglep1plus";
        	this.dataSortFieldKey = "ce_created_date";
        	this.dataSortFieldFormat = "yyyy-MM";
        	this.dataCSVHeaders = CCD_ET_SINGLE_LAKE_CSVHEADERS;
            break;
        case LakeProperties.CCD_CASE_ET_MULTIPLE:  
        	this.lakeContainerName = "ccdetmultiplep1plus";
        	this.dataSortFieldKey = "ce_created_date";
        	this.dataSortFieldFormat = "yyyy-MM";
        	this.dataCSVHeaders = CCD_ET_MULTIPLE_LAKE_CSVHEADERS;
            break;
        case LakeProperties.PAYMENTS:  
        	this.lakeContainerName = "paymentsp1plus";
        	this.dataSortFieldKey = "payment_created_date";
        	this.dataSortFieldFormat = "dd MMM yyyy HH:mm:ss z"; //09 Jan 2019 15:36:07 UTC
        	this.dataCSVHeaders = PAYMENTS_LAKE_CSVHEADERS;
            break;
        case LakeProperties.NOTIFY:  
        	this.lakeContainerName = "notifyp1plus";
        	this.dataSortFieldKey = "created_timestamp";
        	this.dataSortFieldFormat = "yyyy-MM-dd HH:mm:ss.SSSX";
        	this.dataCSVHeaders = NOTIFY_LAKE_CSVHEADERS;
            break;
        case LakeProperties.EXE_CTSC_INTERACTIONS:  
        	this.lakeContainerName = "exep1plus";
        	this.dataSortFieldKey = "create-timestamp";
        	this.dataSortFieldFormat = "yyyy-MM-dd'T'HH:mm:ssXXX";
        	this.dataCSVHeaders = EXE_LAKE_CSVHEADERS;
            break;
        default: 
        	throw new MIParamException("Atttempt to configure Lake Data Source Properties with unknown Data Profile ID '"+this.dataProfileID+"'.");
		}
		
	}

	
	public String getStorageAccountName() throws MIParamException {

		String storageAccountName = "NOTSET";
		
		switch (lakeProfileID) {
        case LakeProperties.LAKE_PROD: 
        	storageAccountName = this.keyVaultHandler.getSecret(this.LAKE_NAME_KEY);
            break;
        case LakeProperties.LAKE_TEST:  
        	storageAccountName = this.keyVaultHandler.getSecret(this.TEST_LAKE_NAME_KEY);
            break;
        default: 
        	throw new MIParamException("Atttempt to retrieve Lake storage account name with unknown profile ID '"+lakeProfileID+"'.");
		}

		return storageAccountName;
	}

	public String getStorageConnectionString() throws MIParamException {
		
		String storageTokenName = "NOTSET";
		
		switch (lakeProfileID) {
        case LakeProperties.LAKE_PROD: 
        	storageTokenName = this.keyVaultHandler.getSecret(this.LAKE_TOKEN_KEY);
            break;
        case LakeProperties.LAKE_TEST:  
        	storageTokenName = this.keyVaultHandler.getSecret(this.TEST_LAKE_TOKEN_KEY);
            break;
        default: 
        	throw new MIParamException("Atttempt to retrieve Lake storage account token with unknown profile ID '"+lakeProfileID+"'.");
		}

		return storageTokenName;
	}

	public int getLakeProfileID() {
		return lakeProfileID;
	}

	public int getDataProfileID() {
		return dataProfileID;
	}

	public String getBlobNameFormat() {
		return blobNameFormat;
	}

	public String getBlobNameRegex() {
		return blobNameRegex;
	}

	public String getLakeContainerName() {
		return lakeContainerName;
	}

	public String getDataSortFieldKey() {
		return dataSortFieldKey;
	}

	public String getDataSortFieldFormat() {
		return dataSortFieldFormat;
	}

	public String[] getDataCSVHeaders() {
		return dataCSVHeaders;
	}


}
