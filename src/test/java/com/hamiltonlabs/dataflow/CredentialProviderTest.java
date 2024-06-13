package com.hamiltonlabs.dataflow.core;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import java.util.Properties;
import java.security.GeneralSecurityException;
import java.io.IOException;

public class CredentialProviderTest{

   public final static String TESTUSER="etl";
   public final static String TESTENC="ZpLfE+uTYE2mdmjOPrukol3yu+cpAHBnmL6trHa9PHGj";


    @Test
    void getCredentials() throws GeneralSecurityException,IOException {
	String s=CredentialProvider.getPass("plugh",TESTENC);
	assertEquals(s,"plugh");

       
        /* the method tested here is  brazenly stubbed for now. Just so that DataProvider can operate  */
	try{
        Properties p=CredentialProvider.getCredentials("plugh","dataflow.properties");
	assertEquals(p.get("password"),"plugh");
	}catch(Exception e){
            e.printStackTrace();
	    throw e;
        }
    }

}
