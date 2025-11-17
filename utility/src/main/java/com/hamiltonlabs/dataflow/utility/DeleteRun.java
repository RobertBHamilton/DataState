package com.hamiltonlabs.dataflow.utility;

import com.hamiltonlabs.dataflow.service.*;

/* upsert a data status row for given dataid,datasetid,jobid,locktype */
public class DeleteRun{

    /* blow away status for any dataset, IN or OUT held by this job */
    static String deleteSQL="delete from datastatus where dataid=? and datasetid in (select datasetid from job where jobid=?) ";
    static String deleteRun="delete from datastatus where dataid=? and jobid=? ";


    public static String run(String passkey,String jobid,String dataid)
    throws Exception{
        int rows=DataFlow.runUpdate(passkey,deleteRun,dataid,jobid);
        rows+=DataFlow.runUpdate(passkey,deleteSQL,dataid,jobid);
	return String.format("[{\"message\":\"%d rows affected\"}]",rows);
    }
    
    public static void main(String[] args) throws Exception{
	System.out.println(run(	args[0],args[1],args[2]));
    }
}

