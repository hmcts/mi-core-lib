package net.hmcts.reform.mi.blobs;

import org.apache.commons.csv.CSVRecord;

public interface MICSVReader {

	public boolean hasNext() throws MIBlobException;
	
	public CSVRecord next() throws MIBlobException;
	
	public void close() throws MIBlobException; 	

	
}
