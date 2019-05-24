package net.hmcts.reform.mi.utils;

import org.springframework.util.StopWatch;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.MSICredentials;
import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.rest.credentials.ServiceClientCredentials;


public class KeyVaultHandler {

	public static final String MI_KV_CONN_TEST_KEY = "mi-kv-conn-test-key";

	
	private static final String KEYVAULT_URL_PATTERN = "https://######.vault.azure.net/"; 
	
	private String keyVaultURL;
	private KeyVaultClient kvClient;
	
	// This allows the KVH to be used with explicit Service Principal credentials, eg. if running locally. 
	private boolean spConnect = false;
	private ServiceClientCredentials spCredentials;
	
	
	public KeyVaultHandler(String keyVaultName) {	
		this.keyVaultURL = KeyVaultHandler.KEYVAULT_URL_PATTERN.replaceFirst("######", keyVaultName);
		
		MSICredentials credentials = new MSICredentials(AzureEnvironment.AZURE);
		this.kvClient = new KeyVaultClient(credentials);			
		 
	}
		
	/** 
	 * I had this create the client for each call previously but was having some problems so added it to the class in constructor
	 * instead. have left this method here in case need to switch back.
	 * 
	 */
	private KeyVaultClient getKeyVaultClient() {

		if (this.spConnect) {
			KeyVaultClient kvClient = new KeyVaultClient(this.spCredentials);
			return kvClient;			
		} else {
			MSICredentials credentials = new MSICredentials(AzureEnvironment.AZURE);
			KeyVaultClient kvClient = new KeyVaultClient(credentials);			
			
			return kvClient;			
		}
	}
	
	public String getSecret(String secretKey) {
		
		String secretURL = this.keyVaultURL+"secrets/"+secretKey;
//		MILogger.debugLine("Try to retrieve secret - "+secretURL);
		
		
		return this.kvClient.getSecret(secretURL).value();
//		return this.getKeyVaultClient().getSecret(secretURL).value();

	}
	
	
	public void testKeyVaultConnection() throws MIUtilsException {
		
		MILogger.debugLine("Testing connection to KeyVault ("+this.keyVaultURL+") Note, this may take a couple of minutes...");
		String kvConnTestString = "";
		try {
			kvConnTestString = this.getSecret(MI_KV_CONN_TEST_KEY);
		} catch (Exception ex) {
			throw new MIUtilsException("Failed to connect to KeyVault "+this.getKeyVaultURL()+". Check parameters and identity for authorisation.");
		}
		MILogger.debugLine("Connection established to KeyVault ("+this.keyVaultURL+") KV conn test string - "+kvConnTestString+"\n");
	}
	
	
	
	
	public static void main(String[] args) {

		
		
		// Note this is all very well as a basic test but unless it runs in an azure container it will always fail!
		// ********************************************************************************************************
		
		try {
			StopWatch stopwatch = new StopWatch("Main");
			stopwatch.start();
			MILogger.setDebugMode(true);
			
			String keyVaultURL = "https://p1p-kv-test.vault.azure.net/";

			KeyVaultHandler kvh = new KeyVaultHandler(keyVaultURL);

			String secret = kvh.getSecret("NotifyTestKey");
			MILogger.printLine("secret is - "+secret);

			stopwatch.stop();
			MILogger.printLine("Completed in "+stopwatch.getTotalTimeSeconds()+" seconds.");
			
			MILogger.printLine("ALL DONE!!!");
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}


	public void setSpCredentials(ServiceClientCredentials spCredentials) {

		this.spCredentials = spCredentials;
		this.spConnect = true;
		this.kvClient = null;
		this.kvClient = new KeyVaultClient(this.spCredentials);
	}

	public String getKeyVaultURL() {
		return this.keyVaultURL;
	}
}
