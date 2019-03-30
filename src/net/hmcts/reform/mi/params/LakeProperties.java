package net.hmcts.reform.mi.params;

import net.hmcts.reform.mi.utils.KeyVaultHandler;

public class LakeProperties {
	
	private int lakeProfileID;

	// Define the identifiers for properties profiles
    public static final int LAKE_PROD = 1; 
    public static final int LAKE_TEST = 2;

    // Variables for handling the properties stored in KeyVault
	private KeyVaultHandler keyVaultHandler;
	
    private String LAKE_NAME_KEY = "mi-lake-storage-name-key";
    private String LAKE_TOKEN_KEY = "mi-lake-storage-token-key";
    private String TEST_LAKE_NAME_KEY = "mi-lake-storage-name-test-key";
    private String TEST_LAKE_TOKEN_KEY = "mi-lake-storage-token-test-key";


	public static final String LAKE_BLOB_NAME_FORMAT = "yyyyMM";
	public static final String LAKE_BLOB_NAME_REGEX = "[0-9]{6}"; 
	public static final String LAKE_CSV_CONTENT_TYPE = "text/csv";
	
	
	public static final String CCD_METADATA_LAKE_CONTAINER_NAME = "ccdmetadatap1plus";
	public static final String CCD_DIVORCE_LAKE_CONTAINER_NAME = "ccddivorcep1plus";
	public static final String CCD_PROBATE_GOR_LAKE_CONTAINER_NAME = "ccdprobategorp1plus";
	public static final String CCD_PROBATE_CAV_LAKE_CONTAINER_NAME = "ccdprobatecavp1plus";

	public static final String PAYMENTS_LAKE_CONTAINER_NAME = "paymentsp1plus";
	public static final String NOTIFY_LAKE_CONTAINER_NAME = "notifyp1plus";
	public static final String EXE_LAKE_CONTAINER_NAME = "exep1plus";

	
	public static final String CCD_METADATA_SORT_FIELD_KEY = "ce_created_date";
	public static final String CCD_METADATA_SORT_FIELD_FORMAT = "yyyy-MM";

	public static final String CCD_DIVORCE_SORT_FIELD_KEY = "ce_created_date";
	public static final String CCD_DIVORCE_SORT_FIELD_FORMAT = "yyyy-MM";

	public static final String CCD_PROBATE_GOR_SORT_FIELD_KEY = "ce_created_date";
	public static final String CCD_PROBATE_GOR_SORT_FIELD_FORMAT = "yyyy-MM";

	public static final String CCD_PROBATE_CAV_SORT_FIELD_KEY = "ce_created_date";
	public static final String CCD_PROBATE_CAV_SORT_FIELD_FORMAT = "yyyy-MM";

	public static final String CCD_SORT_FIELD_KEY = "cd_org_created_date";
	public static final String CCD_SORT_FIELD_FORMAT = "yyyy-MM";
	
	public static final String[] CCD_METADATA_LAKE_CSVHEADERS = { "md_uuid","md_staging_version","md_insert_time","md_source_id","md_retention","md_retention_exempt","extraction_date","ce_id","ce_case_data_id","ce_created_date","ce_case_type_id","ce_case_type_version","ce_event_id","ce_event_name","ce_state_id","ce_state_name","cd_created_date","cd_last_modified","cd_jurisdiction","cd_reference" };
	public static final String[] CCD_DIVORCE_LAKE_CSVHEADERS = { "md_uuid","md_staging_version","md_insert_time","md_source_id","md_retention","md_retention_exempt","extraction_date","case_metadata_event_id","ce_case_data_id","ce_created_date","ce_case_type_id","ce_case_type_version","ce_divorce_unit","ce_contact_confidential","ce_need_help_with_fees","ce_financial_order","ce_reason_for_divorce","ce_divorce_claim_from","ce_adultery_wish_to_name","ce_link_case_reference" };
	public static final String[] CCD_PROBATE_GOR_LAKE_CSVHEADERS = { "md_uuid","md_staging_version","md_insert_time","md_source_id","md_retention","md_retention_exempt", "extraction_date","case_metadata_event_id","ce_case_data_id","ce_created_date","ce_case_type_id","ce_case_type_version","ce_app_type","ce_app_sub_date","ce_reg_location","ce_will_exists","ce_iht_net_value","ce_iht_gross_value","ce_deceased_dod","ce_deceased_other_names","ce_case_stop_reason","ce_case_stop_reason_cnt" };
	public static final String[] CCD_PROBATE_CAV_LAKE_CSVHEADERS = { "md_uuid","md_staging_version","md_insert_time","md_source_id","md_retention","md_retention_exempt", "extraction_date","case_metadata_event_id","ce_case_data_id","ce_created_date","ce_case_type_id","ce_case_type_version","ce_expiry_date","ce_app_type","ce_reg_location","ce_deceased_dod" };


	public static final String NOTIFY_SORT_FIELD_KEY = "created_timestamp";
	public static final String NOTIFY_SORT_FIELD_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSX";
	
	public static final String[] NOTIFY_LAKE_CSVHEADERS = { "md_uuid","md_staging_version","md_insert_time","md_source_id","md_retention","md_retention_exempt","notification_id","service","reference","type","template_id","template_version","template_name","status","created_timestamp","sent_timestamp","completed_timestamp","estimated_timestamp" };

	public static final String EXE_SORT_FIELD_KEY = "create-timestamp";
	public static final String EXE_SORT_FIELD_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";
	
	public static final String[] EXE_LAKE_CSVHEADERS = { "md_uuid","md_staging_version","md_insert_time","md_source_id","md_retention","md_retention_exempt", "media-type","channel-obj-id","queue-name","interaction-id","orig-interaction-id","origination","destination-original","destination-translated","customer-name","case-id","interaction-direction","interaction-type","dial-code","dial-text","create-timestamp","agent-id","agent-name","accept-timestamp","process-time","post-process-time","total-time","abandon-timestamp","voice-message-left","recording-filename","ivr-treatment-time","transfer-from","conference-from","max-hold-time","hold-count","total-hold-time","call-leg1-post-dial-delay","call-leg2-post-dial-delay","call-leg1-answer-time","call-leg2-answer-time","call-leg1-sip-id","call-leg2-sip-id","inbound-sip-id","notes","campaign-name","record-id","record-status","disposition-code","ext-trans-data","wrap-up-code","wrap-up-text","agent-accept-timestamp","agent-abandon-timestamp","trans-rec-num","is-interaction-completed","null-header" };

	public static final String PAYMENTS_SORT_FIELD_KEY = "payment_created_date";
	public static final String PAYMENTS_SORT_FIELD_FORMAT = "dd MMM yyyy HH:mm:ss z"; //09 Jan 2019 15:36:07 UTC
	
	public static final String[] PAYMENTS_LAKE_CSVHEADERS = { "md_uuid","md_staging_version","md_insert_time","md_source_id","md_retention","md_retention_exempt","service","payment_group_reference","payment_reference","ccd_reference","case_reference","organisation_name","customer_internal_reference","pba_number","payment_created_date","payment_status_updated_date","payment_status","payment_channel","payment_method","payment_amount","site_id","fee_code","version","calculated_amount","memo_line","nac","fee_volume" };

	
	public LakeProperties(int lakeProfileID, KeyVaultHandler kvh) {
		
		this.lakeProfileID = lakeProfileID;
		this.keyVaultHandler = kvh;

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


}
