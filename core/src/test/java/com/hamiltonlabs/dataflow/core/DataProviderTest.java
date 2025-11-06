package com.hamiltonlabs.dataflow.core;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import java.security.GeneralSecurityException;
import java.sql.SQLException;

@TestInstance(Lifecycle.PER_CLASS)
public class DataProviderTest{

    DataProvider p;

    /* this relies on a stub in the credentialProvider, which should be mocked here instead 
     *  it relies also on local postgress database listening for connections. Mock or use H2
     * To get credentials we need the dataflow.properties file which contains an encrypted field,
     * and the encryption key ("plugh"). The open() will pass these to the CredentialProvider and
     * if it successfully decrypts the password to the dataflow database then the open will succeed
     * We test this by checking that the connection is indeed open.  
     * in addetion we run a couple of SQL to verify the operation
     */
    @Test
    @BeforeAll
    void init()throws Exception{
	p=new DataProvider().open("plugh","dataflow.properties");
	assertEquals(p.getConnection().isClosed(),false);
    }

    /* run some sql and get expected result */
    @Test
    void runSQL() throws SQLException {
	assertEquals(p.runSQL("select user,1 as anint").get(0),"etl,1");
    }
    /* tests both header line and metadata to get the column list */
    @Test
    void runSQLHeader() throws SQLException{
	assertEquals(p.runSQLHeader("select user"),"user");
	assertEquals(p.runSQLHeader("select user,1 as anint"),"user,anint");
	
    }
    
}
