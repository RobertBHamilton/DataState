#!/bin/sh 

# Validation rule: dataset is valid if and only if number of records > 1

export PGPASSWORD=$datastatus_password
export sql="psql -U $datastatus_username  -d $datastatus_database -h $datastatus_hostname -c "
export output_path=${datastatusextract_schemaname}/${datastatusextract_tablename}/status_$dataid

export n=`$sql  "select count(*) from ${datastatus_schemaname}.${datastatus_tablename} where modified::date = to_date('$dataid','yyyy-mm-dd')" |head -3|tail -1`

if [ $n -gt 1 ];then 
    echo "n=$n valid"
    exit 0 
else
    
    echo "n=$n invalid"
    exit 1
fi
#No statements after this if you want dataflow to caputure the run status

