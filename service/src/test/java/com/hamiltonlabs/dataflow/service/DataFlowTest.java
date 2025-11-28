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

    DataProvider p;

    @BeforeEach
    void init()throws Exception{
        p=new DataProvider().open("plugh","dataflow.properties");
	assertEquals(p.getConnection().isClosed(),false);

        String tables=DataFlow.createTables("plugh");

	assertEquals(tables,"0 rows updated");

	int rows=p.runUpdate("delete from datastatus where dataid in ('1.0','1.1','1.2') and jobid=? ","loadbob");
	rows=p.runUpdate("delete from datastatus where dataid in ('1.0','1.1','1.2') and jobid=? ","otherjob");

	/* 1.0 should be skipped because it has no source already run and status is already READY */
	int inserts=p.runUpdate("insert into datastatus (dataid,datasetid,jobid,locktype,modified,status) values (?,?,?,'OUT',now(),'READY')","1.0","bobout","loadbob");

         System.out.printf("inserts %d\n",inserts);
	assertEquals(inserts,1);
	/* 1.1 should be skipped because it has already RUNNING on the IN file */
	inserts=p.runUpdate("insert into datastatus (dataid,datasetid,jobid,locktype,modified,status) values (?,?,?,'OUT',now(),'READY')","1.1","bobin","otherjob");
         System.out.printf("inserts %d\n",inserts);
	assertEquals(inserts,1);
	inserts=p.runUpdate("insert into datastatus (dataid,datasetid,jobid,locktype,modified,status) values (?,?,?,'IN',now(),'RUNNING')","1.1","bobin","loadbob");
	assertEquals(inserts,1);
         System.out.printf("inserts %d\n",inserts);

	inserts=p.runUpdate("insert into job (datasetid,itemtype,jobid) values (?,'IN',?)","bobin","loadbob");
	assertEquals(inserts,1);
         System.out.printf("inserts %d\n",inserts);

	

	/* this should be good to go because the in file is ready and no locks exist */
	inserts=p.runUpdate("insert into datastatus (dataid,datasetid,jobid,locktype,modified,status) values (?,?,?,'OUT',now(),'READY')","1.2","bobin","loadbob");
         System.out.printf("inserts %d\n",inserts);
	assertEquals(inserts,1);
    }

    @Test
     void runSQL() throws SQLException {
	ResultSet rs =p.runSQL("select * from dataflow.datastatus where ?='1'","1");
	rs.next();
	System.out.printf("result: %s\n",rs.getString(1));;
	assertEquals(rs.getString(1),"bobout");
	
    }
    /* assumes that dataflow has been configured with test data and creds were encrypted with the test key "plugh" */
//     static String dataidSQL="select x.dataid,x.jobid from  (select j.datasetid,j.jobid,d.dataid  from job j left join datastatus d on j.datasetid=d.datasetid and d.locktype='OUT' and d.status='READY' where j.itemtype='IN' and j.jobid=?)x where not exists (select d.dataid from job j join datastatus d on j.jobid=x.jobid  and j.itemtype='IN' and d.locktype='IN' and j.datasetid=d.datasetid and d.dataid=x.dataid) and not exists (select d.dataid from job j join datastatus d on j.jobid=x.jobid and j.itemtype='OUT' and d.datasetid=j.datasetid and d.dataid=x.dataid where d.status != 'RESUBMIT')  order by x.dataid limit 1";
    @Test
    void getJobDataTest() throws Exception {
	//String jobdata=DataFlow.getJobData("plugh","loadbob");
	String jobdata=DataFlow.getJobData("loadbob",p);
	System.out.printf("debug %s\n",jobdata);
	//assertEquals( "[{\"dataid\":\"1.2\"},", jobdata).substring(0,18));
    }
    
}
