package com.hamiltonlabs.dataflow.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;


import org.junit.jupiter.api.Test;

import java.security.GeneralSecurityException;
import java.sql.SQLException;
import com.hamiltonlabs.dataflow.core.*;
import java.sql.ResultSet;

@TestInstance(Lifecycle.PER_CLASS)
public class DataFlowTest{

    @BeforeEach
    void init()throws Exception{
	DataProvider p=new DataProvider().open("plugh","dataflow.properties");
	assertEquals(p.getConnection().isClosed(),false);
	int rows=p.runUpdate("delete from datastatus where dataid in ('1.0','1.1','1.2') and jobid=? ","loadbob");
	rows=p.runUpdate("delete from datastatus where dataid in ('1.0','1.1','1.2') and jobid=? ","otherjob");

	/* 1.0 should be skipped because it has no source already run and status is already READY */
	int inserts=p.runUpdate("insert into datastatus (dataid,datasetid,jobid,locktype,modified,status) values (?,?,?,'OUT',now(),'READY')","1.0","boout","loadbob");

	/* 1.1 should be skipped because it has already RUNNING on the IN file */
	inserts=p.runUpdate("insert into datastatus (dataid,datasetid,jobid,locktype,modified,status) values (?,?,?,'OUT',now(),'READY')","1.1","bobin","otherjob");
	inserts=p.runUpdate("insert into datastatus (dataid,datasetid,jobid,locktype,modified,status) values (?,?,?,'IN',now(),'RUNNING')","1.1","bobin","loadbob");

	/* this should be good to go because the in file is ready and no locks exist */
	inserts=p.runUpdate("insert into datastatus (dataid,datasetid,jobid,locktype,modified,status) values (?,?,?,'OUT',now(),'READY')","1.2","bobin","loadbob");
    }


    /* assumes that dataflow has been configured with test data and creds were encrypted with the test key "plugh" */
    @Test
    void getJobDataTest() throws Exception {
	assertEquals( "[{\"dataid\":\"1.2\"},", DataFlow.getJobData("loadbob","plugh").substring(0,18));
	//assertEquals( "[{\"dataid\":\"1.2\"},", DataFlow.getJobData("loadbob","plugh"));
    }
    
}
