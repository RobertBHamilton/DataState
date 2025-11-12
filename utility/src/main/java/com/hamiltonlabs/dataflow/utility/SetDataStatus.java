package com.hamiltonlabs.dataflow.utility;

import com.hamiltonlabs.dataflow.core.*;
import com.hamiltonlabs.dataflow.service.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/* upsert a data status row for given dataid,datasetid,jobid,locktype */
public class SetDataStatus{
    static String updateSQL="update datastatus set status=?,modified=now() where dataid=? and datasetid=? and jobid=? and locktype=?";
    static String insertSQL="insert into datastatus (dataid,datasetid,jobid,locktype,modified,status) values (?,?,?,?,now(),?)";

    public static void main(String[] args) throws Exception{
	
	String passkey=args[0];
        String status=args[1];
	String dataid=args[2];
	String datasetid=args[3];
	String jobid=args[4];
	String locktype=args[5];
        DataProvider p=new DataProvider().open(passkey,"dataflow.properties");
	int rows=p.runUpdate(updateSQL,status,dataid,datasetid,jobid,locktype);
	if (rows==0){
            rows=p.runUpdate(insertSQL,dataid,datasetid,jobid,locktype,status);
	}
	System.out.printf("[{\"message\":\"%d rows affected\"}]",rows);
    }
    
}

