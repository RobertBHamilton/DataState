package com.hamiltonlabs.dataflow.utility;

import com.hamiltonlabs.dataflow.service.*;

/** Request a data id with input datasets locked for use by this client.  All environment information returned in JSON format */
public class ForceJob{

    /** update the status table indicating that this job is running */
    public static String run(String passkey,String jobid) throws Exception{
        return DataFlow.forceJob(passkey,jobid);
    }

    public static void main(String[] args) throws Exception{
	System.out.println(run(args[0],args[1]));
    }
}
