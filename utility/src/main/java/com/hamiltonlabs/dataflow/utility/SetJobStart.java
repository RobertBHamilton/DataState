package com.hamiltonlabs.dataflow.utility;

import com.hamiltonlabs.dataflow.service.*;

public class SetJobStart{

    public static void main(String[] args) throws Exception{
	
	String passkey=args[0];
	String jobid=args[1];
	String dataid=args[2];
        DataFlow.setJobStart(passkey,jobid,dataid);

    }
    
}

