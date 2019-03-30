package net.hmcts.reform.mi.blobs;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudAppendBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;



public class AppendBlobCSVWriter {

	public static final String CSV_CONTENT_TYPE = "text/csv";

	private CloudAppendBlob appendBlob = null;
	private String blobURI;

	private BufferedWriter bw = null;
	private CSVPrinter blobCSVPrinter = null;
	
	private String[] csvHeaders = null;
	
	private boolean blobIsNew = true;
	private boolean transferHeaders = false;
	
	
	public AppendBlobCSVWriter(String storageConnectionStr, String containerName, String blobName, boolean overwriteBlob) throws MIBlobException {

		if (containerName == null || containerName.equals("")) throw new MIBlobException("ERROR: Container name not set!");
    	if (blobName == null || blobName.equals("")) throw new MIBlobException("ERROR: Blob Name not set!");

    	CloudStorageAccount storageAccount = null;
    	CloudBlobClient blobClient = null;
    	CloudBlobContainer blobContainer = null;

    	try {
        	storageAccount = CloudStorageAccount.parse(storageConnectionStr);
        	blobClient = storageAccount.createCloudBlobClient();
        	blobContainer = blobClient.getContainerReference(containerName);
        	
			blobContainer.createIfNotExists();			
    		this.appendBlob = blobContainer.getAppendBlobReference(blobName);
    		this.blobURI = this.appendBlob.getUri().toString();

    		if (overwriteBlob) {
    			this.appendBlob.deleteIfExists();
    			this.appendBlob.createOrReplace();
    			this.blobIsNew = true;
    		} else {
    			if (this.appendBlob.exists()) this.blobIsNew = false;
    			this.appendBlob.createOrReplace();			
    		}

    		this.appendBlob.getProperties().setContentType(AppendBlobCSVWriter.CSV_CONTENT_TYPE);
        	
        	
    	} catch (Exception ex) {
    		this.appendBlob = null;
        	storageAccount = null;
        	blobClient = null;
        	blobContainer = null;
    		throw new MIBlobException("Error creating AppendBlobCSVWriter.", ex);
    	} 
			
	}
	
	private void initCSVPrinter() throws MIBlobException {
		
    	try {

			if (this.blobIsNew && this.csvHeaders != null) {
				// If the blob is new we should set headers if we have them, and then reset newBlob flag
	    		this.bw = new BufferedWriter(new OutputStreamWriter(this.appendBlob.openWriteExisting()));
	    		this.blobCSVPrinter = new CSVPrinter(this.bw,CSVFormat.EXCEL.withHeader(this.csvHeaders));
	    		this.blobIsNew = false;
			} else if (this.blobCSVPrinter == null && this.transferHeaders) {
				// If we need to transfer headers from inbound record then create printer accordingly
	    		this.bw = new BufferedWriter(new OutputStreamWriter(this.appendBlob.openWriteExisting()));
	    		this.blobCSVPrinter = new CSVPrinter(this.bw,CSVFormat.EXCEL.withHeader());				 
			} else if (this.blobCSVPrinter == null) {
				// Otherwise create a printer that ignores headers
	    		this.bw = new BufferedWriter(new OutputStreamWriter(this.appendBlob.openWriteExisting()));
	    		this.blobCSVPrinter = new CSVPrinter(this.bw,CSVFormat.EXCEL.withSkipHeaderRecord());				 				
			}     		        	
        	
    	} catch (Exception ex) {
    		throw new MIBlobException("Error initialising CSVPrinter in AppendBlobCSVWriter.", ex);
    	} 
	}
	
	public void writeRecord(CSVRecord csvRecord, BlobCSVMapper csvMapper) throws MIBlobException {
		
		try {
			this.initCSVPrinter();
			csvMapper.printMappedCSVRecord(this.blobCSVPrinter, csvRecord);
		} catch (Exception ex) {
    		throw new MIBlobException("Error writing record in BlockBlobCSVWriter.", ex);			
		}
	}
	
	public void writeRecord(CSVRecord csvRecord) throws MIBlobException {
		
		try {
			this.initCSVPrinter();
			this.blobCSVPrinter.printRecord(csvRecord);		
		} catch (Exception ex) {
    		throw new MIBlobException("Error writing record in AppendBlobCSVWriter.", ex);			
		}
	}
	
	
	public void flush() throws MIBlobException {
		
		try {
			this.blobCSVPrinter.flush();
		} catch (Exception ex) {
    		throw new MIBlobException("Error flushing CSVPrinter in AppendBlobCSVWriter.", ex);			
		}
	}
	
	public void close() throws MIBlobException {
		
		try {
			if (this.blobCSVPrinter != null) this.blobCSVPrinter.close(); this.blobCSVPrinter = null;
			if (this.bw != null) this.bw.close(); this.bw = null;
			this.appendBlob = null;
		} catch (Exception ex) {
    		throw new MIBlobException("Error flushing CSVPrinter in AppendBlobCSVWriter.", ex);			
		}		
	}
	
	
	public void setMetadata(HashMap<String, String> metadata) {
		this.appendBlob.setMetadata(metadata);
	}

	public void setCsvHeaders(String[] csvHeaders) {
		this.csvHeaders = csvHeaders;
	}

	public void setTransferHeaders(boolean transferHeaders) {
		this.transferHeaders = transferHeaders;
	}

	public String getBlobURI() {
		return blobURI;
	}


}
