package net.hmcts.reform.mi.blobs;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.BlobInputStream;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;


import com.microsoft.azure.storage.blob.CloudAppendBlob;



public class AppendBlobCSVReader implements MICSVReader {

	private String storageConnectionStr; 
	private String containerName; 
	private String blobName;
	
	private BlobInputStream bis = null;
	private CloudAppendBlob appendBlob = null;

	private Reader reader = null;
	private CSVParser blobCSVParser = null;
	
	private Iterator<CSVRecord> csvIterator = null;
	
	public AppendBlobCSVReader(String storageConnectionStr, String containerName, String blobName) throws MIBlobException {

		if (containerName == null || containerName.equals("")) throw new MIBlobException("ERROR: Container name not set!");
    	if (blobName == null || blobName.equals("")) throw new MIBlobException("ERROR: Blob Name not set!");

    	this.storageConnectionStr = storageConnectionStr;
    	this.containerName = containerName;
    	this.blobName = blobName;
    				
	}
	
	public boolean containerExists() throws MIBlobException {
		
    	CloudStorageAccount storageAccount = null;
    	CloudBlobClient blobClient = null;
    	CloudBlobContainer blobContainer = null;

    	try {
        	storageAccount = CloudStorageAccount.parse(this.storageConnectionStr);
        	blobClient = storageAccount.createCloudBlobClient();
        	
        	blobContainer = blobClient.getContainerReference(this.containerName);
        	return blobContainer.exists();
        			
    	} catch (Exception ex) {
    		throw new MIBlobException("Error checking if container exists.", ex);
    	} 

	}
	
	public boolean blobExists() throws MIBlobException {
		
    	CloudStorageAccount storageAccount = null;
    	CloudBlobClient blobClient = null;
    	CloudBlobContainer blobContainer = null;

    	try {
        	storageAccount = CloudStorageAccount.parse(this.storageConnectionStr);
        	blobClient = storageAccount.createCloudBlobClient();
        	
        	blobContainer = blobClient.getContainerReference(this.containerName);
        	return blobContainer.getAppendBlobReference(this.blobName).exists();
        			
    	} catch (Exception ex) {
    		throw new MIBlobException("Error checking if blob exists.", ex);
    	} 

	}
	
	public String getContainerURI() throws MIBlobException {
		
    	CloudStorageAccount storageAccount = null;
    	CloudBlobClient blobClient = null;
    	CloudBlobContainer blobContainer = null;

    	try {
        	storageAccount = CloudStorageAccount.parse(this.storageConnectionStr);
        	blobClient = storageAccount.createCloudBlobClient();
        	
        	blobContainer = blobClient.getContainerReference(this.containerName);
        	return blobContainer.getUri().toString();
        			
    	} catch (Exception ex) {
    		throw new MIBlobException("Error retrieving container uri.", ex);
    	} 

	}
	
	private void initCSVParser() throws MIBlobException {
		
    	CloudStorageAccount storageAccount = null;
    	CloudBlobClient blobClient = null;
    	CloudBlobContainer blobContainer = null;

    	try {
        	storageAccount = CloudStorageAccount.parse(this.storageConnectionStr);
        	blobClient = storageAccount.createCloudBlobClient();
        	
        	blobContainer = blobClient.getContainerReference(this.containerName);
        	if (!blobContainer.exists()) throw new MIBlobException("ERROR: Container "+containerName+" does not exist!");
        	
    		this.appendBlob = blobContainer.getAppendBlobReference(this.blobName);
        	if (!this.appendBlob.exists()) throw new MIBlobException("ERROR: Blob "+blobName+" does not exist!");
    		
    		this.bis = this.appendBlob.openInputStream();
    		this.reader = new InputStreamReader(bis);
    		this.blobCSVParser = new CSVParser(this.reader, CSVFormat.EXCEL.withHeader());
    		this.csvIterator = this.blobCSVParser.iterator();
        	
    	} catch (Exception ex) {
    		this.appendBlob = null;
        	storageAccount = null;
        	blobClient = null;
        	blobContainer = null;
			// We don't want this object being reused once closed so clear the params as well
	    	this.storageConnectionStr = "";
	    	this.containerName = "";
	    	this.blobName = "";			
    		throw new MIBlobException("Error initialising CSVParser in AppendBlobCSVReader.", ex);
    	} 

	}
	
	public boolean hasNext() throws MIBlobException {

		if (this.blobCSVParser == null) this.initCSVParser();
		
		if (this.blobCSVParser.isClosed()) throw new MIBlobException("CSVParser is closed.");
		if (this.csvIterator == null) throw new MIBlobException("CSVIterator is not initialised.");
		
		return this.csvIterator.hasNext();
	}
	
	public CSVRecord next() throws MIBlobException {
		
		if (this.blobCSVParser == null) this.initCSVParser();
		
		if (this.blobCSVParser.isClosed()) throw new MIBlobException("CSVParser is closed.");
		if (this.csvIterator == null) throw new MIBlobException("CSVIterator is not initialised.");
		
		return this.csvIterator.next();		
	}
	
	public void close() throws MIBlobException {
		
		try {
			if (this.blobCSVParser != null && !this.blobCSVParser.isClosed()) this.blobCSVParser.close(); this.blobCSVParser = null;
			if (this.reader != null) this.reader.close(); this.reader = null;
			if (this.bis != null) this.bis.close(); this.bis = null;			
			this.csvIterator = null;
		} catch (IOException ioEx) {
			throw new MIBlobException("Wrapped exception caught trying to close resources in "+this.getClass().getSimpleName(),ioEx);
		} finally {
			// We don't want this object being reused once closed so clear the params as well
	    	this.storageConnectionStr = "";
	    	this.containerName = "";
	    	this.blobName = "";			
		}
		

	}
	

}
