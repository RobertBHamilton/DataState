
#!/bin/sh 

#This is a full fledged extract. Let us require that the extract is of the table datastatus, and the dataid will be a date in yyyy-mm-dd format. The extract will be for every record which has a modified timestamp corresponding to that date. Output will be a file in the configured output directory with the name status_$dataid.
In the previous example we have chosen 'extract_datastatus' to be the jobid.  In order for RunJob to work, we must name our script extract_datastatus.sh
The reason is that I'm lazy and want to supply only the script name to RunJob. Then RunJob  will infer the dataflow jobid from the scriptname. 

1) First, lets create the output directory. The path is arbitrary, but is derived from the dataset record. So if you change it dont forget to change the dataset record also. 

sudo mkdir -p /data/dataflow/datastatus
sudo chmod a+rws /data/dataflow/datastatus

2) create the extract_datastatus.sh ETL script.
   Techically this could just be two lines, the first to set PGPASS the way that postgres likes, the second is a one-liner to pull out the data. The script is in the examples directory.
   In real life you'd probably want to be more sophisticated, but we are just getting something simple to work today.

3) If the job is to run, we need to mark the input data set as ready. To do that, we add a line to datastatus for the dataset 'datastatus', with status READY and a dataid that matches the data chunk to extract.
   In real life, this would be automatic, but today we are just doing a quick and dirty run

    utility.sh sql  "insert into datastatus (dataid,datasetid,jobid,locktype,modified,status) values (current_date,'datastatus','fakejob', 'OUT',now(),'READY')

     We simply used current date as the dataid. This is the pattern I use to queue up datasets to process, even for large jobs having say 1000 daily sets to process.

4) So now we are ready to do a RunJob. The command line is simple.

       RunJob examples/extract_datastatus.sh

   The output is also simple:
   RunJob examples/extract_datastatus.sh  
   ----- output ------------------------------------------------------------------------------------
   Tue Nov 25 03:24:52 PM CST 2025: Launching examples/extract_datastatus.sh with dataid 2025-11-25
   Tue Nov 25 03:24:52 PM CST 2025: Job examples/extract_datastatus.sh is complete. Updating status
   1 rows updated to READY for extract_datastatus and 2025-11-25 1 IN file-local locks released

    Check the extracted data cat /data/dataflow/datastatus/status_2025-11-25 

       datasetid     |       jobid        |            dataid             | locktype | status  |          modified          
  -------------------+--------------------+-------------------------------+----------+---------+----------------------------
   datastatusextract | extract_datastatus | 2025-11-25T13:40:46.843659775 | OUT      | READY   | 2025-11-25 13:40:46.870497
   datastatus        | fakejob            | 2025-11-25                    | OUT      | READY   | 2025-11-25 15:12:28.315466
   datastatus        | extract_datastatus | 2025-11-25                    | IN       | RUNNING | 2025-11-25 15:24:51.448249
   datastatusextract | extract_datastatus | 2025-11-25                    | OUT      | RUNNING | 2025-11-25 15:24:51.448249
  (4 rows)

We have data!
For the record also check the datastatus table
dataid                         datasetid          jobid               locktype  modified                    status
------                         ---------          -----               --------  --------                    ------
2025-11-25                     datastatusextract  extract_datastatus  OUT       2025-11-25 15:24:51.448249  READY
2025-11-25                     datastatus         fakejob             OUT       2025-11-25 15:12:28.315466  READY

It shows that the job completed and the output data for datastatusextract for 2025-11-25 is READY for consumption.

5) suppose we run the job again. We expect dataflow to know that the job has already been done and won't try again.

       RunJob examples/extract_datastatus.sh

   ----- output ------------------------------------------------------------------------------------
    Tue Nov 25 03:31:14 PM CST 2025: no suitable data available for job. Not running it

    If there was more data to work on, like 2025-11-26, RunJob would have found it and do the extract. Here it says we are all caught up and nothing to do. You could schedule RunJob to run every hour if you wished. If the input data was late then a later run would automatically pick it up. If you had data backed up for multiply daily chunks then RunJob would do each one until you are caught up. Very convenient if you are dev ops because it means that you don't have to schedule any special runs. Just allow the system to recover and catch up automatically.

