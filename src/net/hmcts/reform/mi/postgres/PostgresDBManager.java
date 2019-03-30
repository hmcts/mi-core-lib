package net.hmcts.reform.mi.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import net.hmcts.reform.mi.params.MIParamException;
import net.hmcts.reform.mi.utils.KeyVaultHandler;
import net.hmcts.reform.mi.utils.MILogger;


public class PostgresDBManager {

	public static final int MIPRESENTATION_PROFILE = 0;
	public static final int EXPORT_PROFILE = 1;
	public static final int RORDEV_TEST_PROFILE = 2;
	public static final int CTSC_MI_PRESENTATION_PROFILE = 3;

	private int connectionProfile = -1;

    // Variables for handling the properties stored in KeyVault
	private KeyVaultHandler keyVaultHandler;
    
	private Connection connection = null;

	private String dbHostKey = "";
	private String dbUserKey = "";
	private String dbPasswordKey = "";
	
	private String dbPort = "";
	private String dbName = "";


	public PostgresDBManager(int CONNECTION_PROFILE, KeyVaultHandler kvh) throws MIParamException {
		
		this.connectionProfile = CONNECTION_PROFILE;
		this.keyVaultHandler = kvh;
		
		this.initialiseProperties();
	}
	
	private void initialiseProperties() throws MIParamException {
		
		switch (this.connectionProfile) {
			case PostgresDBManager.MIPRESENTATION_PROFILE:
				this.dbHostKey = "mi-postgresdb-phaseone-host-key";
				this.dbUserKey = "mi-postgresdb-phaseone-mipres-user-key";
				this.dbPasswordKey = "mi-postgresdb-phaseone-mipres-pw-key";
				this.dbPort = "5432";
				this.dbName = "miphaseone";
				break;
			case PostgresDBManager.EXPORT_PROFILE:
				this.dbHostKey = "mi-postgresdb-phaseone-host-key";
				this.dbUserKey = "mi-postgresdb-phaseone-export-user-key";
				this.dbPasswordKey = "mi-postgresdb-phaseone-export-pw-key";
				this.dbPort = "5432";
				this.dbName = "miphaseone";
				break;
			case PostgresDBManager.RORDEV_TEST_PROFILE:
				this.dbHostKey = "mi-postgresdb-misandbox-host-key";
				this.dbUserKey = "mi-postgresdb-misandbox-rordev-user-key";
				this.dbPasswordKey = "mi-postgresdb-misandbox-rordev-pw-key";
				this.dbPort = "5432";
				this.dbName = "ror_db";
				break;
			case PostgresDBManager.CTSC_MI_PRESENTATION_PROFILE:
				this.dbHostKey = "mi-postgresdb-ctsc-host-key";
				this.dbUserKey = "mi-postgresdb-ctsc-mipres-user-key";
				this.dbPasswordKey = "mi-postgresdb-ctsc-mipres-pw-key";
				this.dbPort = "5432";
				this.dbName = "ctsc";
				break;
			default:
				throw new MIParamException(
						"Atttempt to configure DB connection with unknown profile '" + this.connectionProfile + "'.");

		}
	}
	
	private String getConnectionString() throws MIParamException {

		String url = "jdbc:postgresql://"+this.getDbHost()+":"+this.dbPort+"/"+this.dbName+"?user="+this.getDbUser()+"&password="+this.getDbPassword()+"&ssl=true";
		return url;		
	}

	
	
	public Connection getConnection(int CONNECTION_PROFILE) throws SQLException, MIParamException {

		if (this.connection == null || this.connection.isClosed() || this.connectionProfile != CONNECTION_PROFILE) {
			MILogger.debugLine("Initialise connection to Azure PostgreSQL!");
			this.connectionProfile = CONNECTION_PROFILE;
			this.initialiseProperties();
			String url = this.getConnectionString();

			this.connection = DriverManager.getConnection(url);
		} 

		return connection;
	}

	

	public String getDbHost() {
		return this.keyVaultHandler.getSecret(this.dbHostKey);
	}

	public String getDbUser() {
		return this.keyVaultHandler.getSecret(this.dbUserKey);
	}

	private String getDbPassword() {
		return this.keyVaultHandler.getSecret(this.dbPasswordKey);
	}

	public String getDbPort() {
		return dbPort;
	}

	public String getDbName() {
		return dbName;
	}	
	
}
