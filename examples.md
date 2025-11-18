# Examples

parameter check:
	##preparation. 
	   ###Register your job and input/output data sets

```
	 export PASSKEY=mysecretkey

         ./utility.sh RunUpdate "insert into job values ('paramjob', 'OUT','paramsfile','',now())"
         ./utility.sh RunUpdate "insert into job values ('paramjob', 'IN','sometable','',now())"
	 ./utility.sh RunUpdate "insert into dataset values('sometable','localhost','dataflow','testdata','testtable','etl',null)";
	 ./utility.sh RunUpdate "insert into dataset values('paramsfile','localhost','file:','testdata','testtable','etl',null)"
```		
	 ### force the depency sometable to be met
	 ./utility.sh RunUpdate "delete from datastatus where tableid='sometable'"
	 ./utility.sh RunUpdate "insert into datastatus values('sometable','otherjob','1.0','OUT','READY',now())"

	### what we just did
	  Setting the environment with PASSKEY allows us to run these utilities without passing it on the command line

	  We registered a job with an id of 'paramjob', assigned the dataset named 'sometable' to the input and the dataset named 'paramsfile' to output
	  We associated some values for hostname,schema,tablename,user and pssword to the 'sometable' data set
	  We associated some values for hostname,schema,tablename,user and pssword to the 'paramfile' data set
	  Our script could use the values for 'sometable' to  connect to a jdbc database.
	  Our script could use the values for 'paramfile' to specify a path and basefilename to the output.
	  We cleaned up the datastatus to make this test repeatable. 
	  We added a datastatus with arbitrary dataid, status READY and OUT for data set sometable so that the partition 1.0 can be consumed.
	  

        ## Running the job
	  ``` ./RunJob paramjob 'bash -c "set|grep -e sometable -e paramsfile -e ^dataid"' ```

	### what we just did  

	  We provided the jobid='paramjob' so that the framework can look up the associated data sets.
	  We provided a command to execute in that environment.
	  In real life, we would put a path to a shell script or a binary, but the command works as well and we use it to peek into the environment.
	  The automatic variables will always be prefixed by the datasetid, so we grep for those. 

	 
         ### What we expect to see:

	   All the automatic variables that are supplied to this jobid when it runs.	
	   This lets us know what variables we can use in our script and to smoketest that they were assigned properly

	## Variation. Second test

	  Run the RunJob command a second time without touching any tables. Now it correctly refuses to run, because it has already run for this dataid.
	  Verify this by listing the datastatus table.

      ```      ./utility.sh RunSQL "select * from datastatus where jobid='paramjob'" ```

	  See that we now have a line indicating that job finished successfully 

	## Variation.  Writing to the output file

         We can use the output file parameters to simulate a real extract and see how one might use the OUT dataset variables:

	  ``` ./RunJob paramjob 'bash -c "set|grep -e sometable -e paramsfile -e ^dataid" >  ${paramsfile_tablename}_${dataid}.dat"' ```

	 Now we see a file named 'testtable_1.0.dat' after the run.

	   
-------------------------------
 
