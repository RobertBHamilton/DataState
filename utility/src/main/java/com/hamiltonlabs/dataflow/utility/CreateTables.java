package com.hamiltonlabs.dataflow.utility;

import com.hamiltonlabs.dataflow.core.*;
import com.hamiltonlabs.dataflow.service.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/** List all jobs registered in the DataFlow */
public class CreateTables{

    static String sqltext="select * from  job";

    public static String run(String passkey) throws Exception{	
	return DataFlow.createTables(passkey);
    }
    public static void main(String[] args) throws Exception{
	System.out.println(run(args[0]));
    }
    
}

