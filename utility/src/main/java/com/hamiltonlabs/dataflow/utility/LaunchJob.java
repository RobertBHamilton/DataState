package com.hamiltonlabs.dataflow.utility;

import com.hamiltonlabs.dataflow.service.*;

public class LaunchJob{

    /** update the status table indicating that this job is running */
    public static void main(String[] args) throws Exception{

        String passkey=args[0];
        String jobid=args[1];
        String s=DataFlow.launchJob(passkey,jobid);
	System.out.print(s);
    }

}
