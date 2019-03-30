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
import com.microsoft.azure.storage.blob.CloudBlockBlob;



public class BlockBlobCSVReader {

	private BlobInputStream bis = null;
	private CloudBlockBlob blockBlob = null;
	private String blobURI;

	private Reader reader = null;
	private CSVParser blobCSVParser = null;
	
	private Iterator<CSVRecord> csvIterator = null;
	
	public BlockBlobCSVReader(String storageConnectionStr, String containerName, String blobName) throws MIBlobException {

		if (containerName == null || containerName.equals("")) throw new MIBlobException("ERROR: Container name not set!");
    	if (blobName == null || blobName.equals("")) throw new MIBlobException("ERROR: Blob Name not set!");

    	CloudStorageAccount storageAccount = null;
    	CloudBlobClient blobClient = null;
    	CloudBlobContainer blobContainer = null;

    	try {
        	storageAccount = CloudStorageAccount.parse(storageConnectionStr);
        	blobClient = storageAccount.createCloudBlobClient();
        	
        	blobContainer = blobClient.getContainerReference(containerName);
        	if (!blobContainer.exists()) throw new MIBlobException("ERROR: Container "+containerName+" does not exist!");
        	
    		this.blockBlob = blobContainer.getBlockBlobReference(blobName);
        	if (!this.blockBlob.exists()) throw new MIBlobException("ERROR: Blob "+blobName+" does not exist!");
    		
        	this.initCSVParser();
        	
    		this.blobURI = this.blockBlob.getUri().toString();

    		
    	} catch (MIBlobException miBlobEx) {
    		throw miBlobEx;
    	} catch (Exception ex) {
    		this.blockBlob = null;
        	storageAccount = null;
        	blobClient = null;
        	blobContainer = null;
    		throw new MIBlobException("Error creating BlockBlobCSVReader.", ex);
    	} 
			
	}
	
	private void initCSVParser() throws MIBlobException {
		
    	try {
    		this.bis = this.blockBlob.openInputStream();
    		this.reader = new InputStreamReader(bis);
    		this.blobCSVParser = new CSVParser(this.reader, CSVFormat.EXCEL.withHeader());
    		this.csvIterator = this.blobCSVParser.iterator();
        	
    	} catch (Exception ex) {
    		throw new MIBlobException("Error initialising CSVParser in BlockBlobCSVReader.", ex);
    	} 
	}
	
	public boolean hasNext() throws MIBlobException {
		if (this.blobCSVParser == null) throw new MIBlobException("CSVParser is not initialised.");
		if (this.blobCSVParser.isClosed()) throw new MIBlobException("CSVParser is not initialised.");
		if (this.csvIterator == null) throw new MIBlobException("CSVIterator is not initialised.");
		
		return this.csvIterator.hasNext();
	}
	
	public CSVRecord next() throws MIBlobException {
		if (this.blobCSVParser == null) throw new MIBlobException("CSVParser is not initialised.");
		if (this.blobCSVParser.isClosed()) throw new MIBlobException("CSVParser is not initialised.");
		if (this.csvIterator == null) throw new MIBlobException("CSVIterator is not initialised.");
		
		return this.csvIterator.next();		
	}
	
	public void close() throws MIBlobException {
		if (this.bis == null) throw new MIBlobException("BlobInputStream is not initialised.");
		if (this.blobCSVParser == null) throw new MIBlobException("CSVParser is not initialised.");
		if (this.blobCSVParser.isClosed()) throw new MIBlobException("CSVParser is not initialised.");
		if (this.csvIterator == null) throw new MIBlobException("CSVIterator is not initialised.");

		try {
			this.blobCSVParser.close();
			this.reader.close();
			this.bis.close();
			this.blobCSVParser = null;
			this.reader = null;
			this.bis = null;
		} catch (IOException ioEx) {
			throw new MIBlobException("Wrapped exception caught trying to close resources in "+this.getClass().getSimpleName(),ioEx);
		}
	}
	
	

	public String getBlobURI() {
		return blobURI;
	}


}
