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
