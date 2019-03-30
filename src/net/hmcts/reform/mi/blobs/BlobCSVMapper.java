package net.hmcts.reform.mi.blobs;

import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

public interface BlobCSVMapper {

	public void printMappedCSVRecord(CSVPrinter printer, CSVRecord srcCSVRecord) throws MIBlobException;
	
}
