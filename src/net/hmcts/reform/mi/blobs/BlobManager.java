package net.hmcts.reform.mi.blobs;

import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.Iterator;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobInputStream;
import com.microsoft.azure.storage.blob.BlobOutputStream;
import com.microsoft.azure.storage.blob.CloudAppendBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;

import net.hmcts.reform.mi.params.StagingProperties;
import net.hmcts.reform.mi.utils.KeyVaultHandler;
import net.hmcts.reform.mi.utils.MILogger;



public class BlobManager {

	private String storageAccountName;
	private String connectionString;
	private String containerName;
	private String containerURI;
	private String blobName;
	private String blobURI;
	private boolean newAppendBlob = false;
	private String contentType = "";
	private HashMap<String,String> metadata;
	
	private CloudBlobContainer blobContainer;
	
	public BlobManager(String connection_string) {
		this.setConnectionString(connection_string);
	}
	
	public BlobOutputStream getBlockBlobOutputStream()  throws StorageException, URISyntaxException, MIBlobException, InvalidKeyException {

		if (this.blobName == null || this.blobName.equals("")) throw new MIBlobException("ERROR: Blob Name not set in BlobManager!");
		
		this.blobContainer.createIfNotExists();

		CloudBlockBlob blockBlob = this.blobContainer.getBlockBlobReference(this.blobName);
		if (blockBlob.exists()) throw new MIBlobException("ERROR: Blob "+this.blobName+" already exists in container "+this.containerName);
		if (!this.contentType.isEmpty()) blockBlob.getProperties().setContentType(this.contentType);
		if (this.metadata != null) blockBlob.setMetadata(metadata);
		
		this.blobURI = blockBlob.getUri().toString();
		
		return blockBlob.openOutputStream();
	}

	public BlobOutputStream getAppendBlobOutputStream()  throws StorageException, URISyntaxException, MIBlobException, InvalidKeyException {

		if (this.blobName == null || this.blobName.equals("")) throw new MIBlobException("ERROR: Blob Name not set in BlobManager!");
		
		this.blobContainer.createIfNotExists();

		CloudAppendBlob appendBlob = this.blobContainer.getAppendBlobReference(blobName);		
		if (!appendBlob.exists()) { 
			appendBlob.createOrReplace();
			this.newAppendBlob = true;
			if (!this.contentType.isEmpty()) appendBlob.getProperties().setContentType(this.contentType);
			if (this.metadata != null) appendBlob.setMetadata(metadata);
		}
		
		this.blobURI = appendBlob.getUri().toString();

		return appendBlob.openWriteExisting();
	}

	public BlobInputStream getBlockBlobInputStream() throws MIBlobException, StorageException, URISyntaxException {
	
		if (this.blobContainer == null || !this.blobContainer.exists()) throw new MIBlobException("ERROR: Container " + this.blobContainer.getUri() +" does not exist!");
		if (this.blobName == null || this.blobName.equals("")) throw new MIBlobException("ERROR: Blob Name not set in BlobManager!");
		
		CloudBlockBlob blockBlob = this.blobContainer.getBlockBlobReference(this.blobName);
		if (!blockBlob.exists()) throw new MIBlobException("ERROR: Blob "+blockBlob.getUri()+" does not exist!");
		this.blobURI = blockBlob.getUri().toString();

		return blockBlob.openInputStream();		
	}

	public BlobInputStream getAppendBlobInputStream() throws URISyntaxException, StorageException, MIBlobException {
		
		if (this.blobContainer == null || !this.blobContainer.exists()) throw new MIBlobException("ERROR: Container " + this.blobContainer.getUri() +" does not exist!");
		if (this.blobName == null || this.blobName.equals("")) throw new MIBlobException("ERROR: Blob Name not set in BlobManager!");

		CloudAppendBlob appendBlob = this.blobContainer.getAppendBlobReference(this.blobName);
		if (!appendBlob.exists()) throw new MIBlobException("ERROR: Blob "+appendBlob.getUri()+" does not exist!");
		this.blobURI = appendBlob.getUri().toString();
				
		return appendBlob.openInputStream();
	}

	
	public void deleteBlockBlob() throws StorageException, MIBlobException, URISyntaxException {
		
		if (this.blobContainer == null || !this.blobContainer.exists()) throw new MIBlobException("ERROR: Container " + this.blobContainer.getUri() +" does not exist!");
		if (this.blobName == null || this.blobName.equals("")) throw new MIBlobException("ERROR: Blob Name not set in BlobManager!");
		
		CloudBlockBlob blockBlob = this.blobContainer.getBlockBlobReference(this.blobName);
		if (!blockBlob.exists()) throw new MIBlobException("ERROR: Blob "+blockBlob.getUri()+" does not exist!");

		blockBlob.delete();
		
		this.blobURI = "";
	}
	
	public void purgeContainerBlobs() throws StorageException, URISyntaxException, MIBlobException {
		
		if (this.blobContainer == null || !this.blobContainer.exists()) throw new MIBlobException("ERROR: Container " + this.blobContainer.getUri() +" does not exist!");
		
		Iterator<ListBlobItem> containerBlobList = this.blobContainer.listBlobs().iterator();
		
		while(containerBlobList.hasNext()) {
			
			String blobURI = containerBlobList.next().getUri().toString();
			String blobName = blobURI.substring(blobURI.lastIndexOf('/')+1, blobURI.length());

			CloudBlockBlob blockBlob = this.blobContainer.getBlockBlobReference(blobName);
			if (!blockBlob.exists()) throw new MIBlobException("ERROR: Blob "+blockBlob.getUri()+" does not exist!");

			blockBlob.delete();			
		}
	}
	
	private void initialiseStorageContainer() throws StorageException, InvalidKeyException, URISyntaxException, MIBlobException {
		
		if (this.containerName == null || this.containerName.equals("")) throw new MIBlobException("ERROR: Container name not set in BlobManager!");

        CloudStorageAccount storageAccount = CloudStorageAccount.parse(this.connectionString);

    	CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
    	MILogger.debugLine("[BlobManager] Storage account name = "+blobClient.getCredentials().getAccountName());
    	this.setStorageAccountName(blobClient.getCredentials().getAccountName());

    	this.blobContainer = blobClient.getContainerReference(this.containerName);
    	MILogger.debugLine("[BlobManager] Container name = "+this.blobContainer.getName());
    	this.containerURI = this.blobContainer.getUri().toString();
    	MILogger.debugLine("[BlobManager] Container URI = "+this.blobContainer.getUri());
    	MILogger.debugLine("[BlobManager] (Storage URI = "+this.blobContainer.getStorageUri()+")");
	}
	
	public void resetAppendBlob() {
		this.blobName = "";
		this.blobURI = "";
		this.newAppendBlob = false;
	}

	public void setAppendBlobOld() {
		this.newAppendBlob = false;
	}
	
	public static void main(String[] args) {

		try {
			
			String keyVaultName = "p1p-kv-test";
			KeyVaultHandler kvh = new KeyVaultHandler(keyVaultName);

			// Need to uncomment this to run locally, make sure 'HMCTS-MI-PARAMS Project is on the build path - NOT Maven!!
			//			kvh.setSpCredentials(KeyVaultProperties.getSPCredentials());
			
			BlobManager bm = new BlobManager(new StagingProperties(StagingProperties.STAGING_TEST,StagingProperties.NOTIFY_DIVORCE_API,kvh).getStorageConnectionString());
			bm.setContainerName("notifystagingdivorce");	
			bm.setBlobName("20190107");
			BlobOutputStream bos = bm.getBlockBlobOutputStream();

			String header = "service,type,template_id,template_version,template_name,status,created_timestamp,sent_timestamp,completed_timestamp,estimated_timestamp\n";
			
			String row1 = "Cmc,email,23db60df-1dbb-405a-ab95-988b635455d1,2,staffMoreTimeRequested20170904,delivered,2019-01-01 11:36:52.409,2019-01-01 11:36:52.654,2019-01-01 11:36:54.299,\n";
			String row2 = "Cmc,email,07ffde84-b1d7-4ec0-b2db-406b73cd9080,12,defendantMoreTimeRequested20170904,delivered,2019-01-01 11:36:52.280,2019-01-01 11:36:52.528,2019-01-01 11:36:53.830,\n";
			String row3 = "Cmc,email,f325bc6b-2169-4014-9a3c-32100317c6ed,13,claimantMoreTimeRequested 20170904,delivered,2019-01-01 11:36:52.092,2019-01-01 11:36:52.346,2019-01-01 11:36:53.714,\n";
						
			PrintWriter pw = new PrintWriter(bos);
			
		    pw.write(header);

		    pw.write(row1);
		    pw.write(row2);
		    pw.write(row3);
		    pw.flush();
		    pw.close();
		    		    
		    System.out.println("ALL DONE!!!");
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public String getStorageAccountName() {
		return storageAccountName;
	}


	public void setStorageAccountName(String storageAccountName) {
		this.storageAccountName = storageAccountName;
	}


	public String getConnectionString() {
		return connectionString;
	}


	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}

	public String getContainerName() {
		return containerName;
	}

	public void setContainerName(String containerName) throws URISyntaxException, InvalidKeyException, StorageException, MIBlobException {
		this.containerName = containerName;
		this.initialiseStorageContainer();
	}

	public boolean containerExists() throws StorageException {
		return (this.blobContainer != null && this.blobContainer.exists());
	}	
	
	public Iterable<ListBlobItem> listContainerBlobs() {
		return this.blobContainer.listBlobs();
	}

	public String getBlobName() {
		return blobName;
	}

	public void setBlobName(String blobName) {
		this.blobName = blobName;
	}
	
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public HashMap<String, String> getMetadata() {
		return metadata;
	}

	public void setMetadata(HashMap<String, String> metadata) {
		this.metadata = metadata;
	}

	public String getBlobURI() {
		return blobURI;
	}

	public String getContainerURI() {
		return containerURI;
	}


	public boolean isNewAppendBlob() {
		return newAppendBlob;
	}


}
