package com.hamiltonlabs.dataflow.utility;

import com.hamiltonlabs.dataflow.service.*;

public class SetJobStart{

    /** update the status table indicating that this job is running */
    public static void main(String[] args) throws Exception{
	
	String passkey=args[0];
	String jobid=args[1];
	String dataid=args[2];
        DataFlow.setJobStart(passkey,jobid,dataid);

    }
    
}

