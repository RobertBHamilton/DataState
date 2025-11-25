#  
#  
#  Job table is a descryptor of a job, and since DataFlow is purely data oriented tool, the Job table is little more than a descriptor of which datasets it consumes or produces.
#  We have one row in Job for each such dataset, identified by the datasetid and an itemtype telling us whether it will be used by this job as IN or OUT.
#  
#  Technically we should call it JobData, but we stick with Job because when we use it we are actually looking for data associate with a particular job.
#  
#  
#  Lets create a job called extract_datastatus, having input and output datasest that we just registered.
#  
#  
  utility.sh dml "insert into job (datasetid,itemtype,jobid) values ('datastatus','IN','extract_datastatus')";
  utility.sh dml "insert into job (datasetid,itemtype,jobid) values ('datastatusextract','OUT','extract_datastatus')";
#  
#  # job is completely registered in the utility.  We are not quite ready to use RunJob (but see the next exmample, but we would like to have a sneak peak at the automatic variables this job gets.  We use ForceJob for that. ForceJob is kind of evil because it runs regardless of whether the data is ready, and so it could interfere with other jobs. 
#  ForceJob has two parameters, the jobid (extract_datastatus) and the command. Normally that command would be the script to run, but in this case we cheat and use the shell 'set' command to look at the environment. We grep it through the with datastatus because we happened to notice that our datasets have that string in the name.
#  
#  
  ForceJob  extract_datastatus 'set|grep datastatus'

#  Notice that we have dataid, the force job provided that for us, using a timestamp down to the nanosecond. In real life, it is more common to use the date instead, assuming that each job runs over one day of data. 
#  
#  All the dataset fields are there. And notice the utility.sh decrypted the password. Don't let that get into logs, but it is there for the etl scripts.
#  
#  
#  ------   Output of ForceJob command -----------------------------------
#   
#  Jobid is extract_datastatus, command is set|grep datastatus 
#  
#  Tue Nov 25 01:40:47 PM CST 2025: Launching extract_datastatus, set|grep datastatus with dataid 2025-11-25T13:40:46.843659775
#  _='Tue Nov 25 01:40:47 PM CST 2025: Launching extract_datastatus, set|grep datastatus with dataid 2025-11-25T13:40:46.843659775'
#  cmd='set|grep datastatus'
#  datastatus_database=dataflow
#  datastatus_datasetid=datastatus
#  datastatus_encryptedpass=dKpgTdzYpGVezNffmg912Wjk223NjTmoEr2L6WRxptXH
#  datastatus_hostname=localhost
#  datastatus_itemtype=IN
#  datastatus_password=plugh
#  datastatus_schemaname=dataflow
#  datastatus_tablename=datastatus
#  datastatus_username=etl
#  datastatusextract_database=file:
#  datastatusextract_datasetid=datastatusextract
#  datastatusextract_hostname=localhost
#  datastatusextract_itemtype=OUT
#  datastatusextract_schemaname=/data/dataflow/
#  datastatusextract_tablename=datastatus
#  datastatusextract_username=etl
#  env=$'declare -x dataid="2025-11-25T13:40:46.843659775";\ndeclare -x datastatus_username="etl";\ndeclare -x datastatus_datasetid="datastatus";\ndeclare -x datastatus_database="dataflow";\ndeclare -x datastatus_hostname="localhost";\ndeclare -x datastatus_tablename="datastatus";\ndeclare -x datastatus_schemaname="dataflow";\ndeclare -x datastatus_encryptedpass="dKpgTdzYpGVezNffmg912Wjk223NjTmoEr2L6WRxptXH";\ndeclare -x datastatus_password="plugh";\ndeclare -x datastatus_itemtype="IN";\ndeclare -x datastatusextract_datasetid="datastatusextract";\ndeclare -x datastatusextract_itemtype="OUT";\ndeclare -x datastatusextract_username="etl";\ndeclare -x datastatusextract_schemaname="/data/dataflow/";\ndeclare -x datastatusextract_tablename="datastatus";\ndeclare -x datastatusextract_hostname="localhost";\ndeclare -x datastatusextract_database="file:";'
#  jobid=extract_datastatus
#  Tue Nov 25 01:40:47 PM CST 2025: Job set|grep datastatus is complete. Updating status
#  1 rows updated to READY for extract_datastatus and 2025-11-25T13:40:46.843659775 1 IN file-local locks released
#  --------------------------------------------------------------------------------------------------------------------
#  
#  The job actually ran and dataflow recorded the result. The output of "utility.sh runs" shows it.

  ./utility.sh runs

#  
#  dataid                         datasetid          jobid               locktype  modified                    status
#  ------                         ---------          -----               --------  --------                    ------
#  2025-11-25T13:40:46.843659775  datastatusextract  extract_datastatus  OUT       2025-11-25 13:40:46.870497  READY
#  
#  
#  
#  Next example, we create a script that actually performs an extract.
