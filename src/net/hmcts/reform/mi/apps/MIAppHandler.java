package net.hmcts.reform.mi.apps;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;

import net.hmcts.reform.mi.utils.KeyVaultHandler;
import net.hmcts.reform.mi.utils.MILogger;


public abstract class MIAppHandler {
	
	public static final String TEST_MODE_VAR_NAME = "testmode";
	public static final String KEYVAULT_VAR_NAME = "keyvaultname";
	
	protected KeyVaultHandler keyVaultHandler;
	protected boolean TEST_MODE = true;

	protected HashMap<String,Object> params = new HashMap<String,Object>(10);
	protected String resultMessage = "";
	protected final String tab = "   ";
	protected final String nl = "\n";
	
	
	public MIAppHandler(KeyVaultHandler kvh, boolean testMode) {
		this.keyVaultHandler = kvh;
		this.TEST_MODE = testMode;
	}
	
	public abstract void runHandler();

	public abstract void runTestHandler();
		
	protected void initResultMessage() {
		String timestamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new java.util.Date());
		this.resultMessage = nl+this.getClass().getName()+" executed at "+timestamp +" (UTC) "+nl;

		if (this.params != null && this.params.size() > 0) {
			
			this.resultMessage += tab+"Parameters: "+nl;
			this.resultMessage += tab+tab+"(using KeyVault "+this.keyVaultHandler.getKeyVaultURL()+")" +nl;

			for (Map.Entry<String, Object> entry : this.params.entrySet()) {
				this.resultMessage += tab+tab+entry.getKey()+" "+entry.getValue()+nl;
			}					
		}
	}
	
	
	protected void updateResultMessage(String msgUpdate) {
		this.resultMessage += msgUpdate;
	}
	
	protected void updateResultMessage(Throwable ex) {
		this.resultMessage += nl+nl+"Exception occurred:"+nl
				+ ExceptionUtils.getFullStackTrace(ex)
				+ nl+nl+nl;
	}
	
	public String getResultMessage() {
		return resultMessage;
	}

	public void setParam(String paramName, Object paramValue) {
		this.params.put(paramName,paramValue);
	}

	public Object getParam(String paramName) {
		return this.params.get(paramName);
	}

	public boolean isTEST_MODE() {
		return TEST_MODE;
	}

	public static boolean getTestMode() {
		String testModeVar = System.getenv(TEST_MODE_VAR_NAME);
		// Unless the test mode is explicitly set to false assume that it is true
		boolean testMode = (testModeVar != null && testModeVar.equalsIgnoreCase("false")) ? false : true; 
		MILogger.debugLine("TestMode is "+testMode);
		return testMode;
	}
	
	public static String getKeyVaultName() throws MIAppException {
		String keyVaultNameVar = System.getenv(KEYVAULT_VAR_NAME);
		if (keyVaultNameVar == null || keyVaultNameVar.equals("")) throw new MIAppException("KeyVaultName Environment varible not set, cannot continue."); 
		MILogger.debugLine("KeyVaultName is "+keyVaultNameVar);
		return keyVaultNameVar;
	}
	
	
	
}
