export CLASSPATH=bin/postgresql-42.7.3.jar:bin/json-20250517.jar:app/target/app-1.0.0.jar
if [ $# -lt 1 ];then
cat<<DONE
usage: $0 cmd args
currently supported commands with required args are:

	SetJobEndStatus jobid dataid status 
	Cryptor  [-d|-e] text   
        GetJobData jobid 
        sql sql
        update sql
        SetDataStatus dataid datasetid jobid lockType status
	jobs  
	datasets 
	runs 

if the PASSKEY environment variable is set then you can omit passkey from the arguments above.
Otherwise the passkey is expected as the FIRST argument to utility.sh, followed by the command ant then the arguments
DONE
    exit
fi


if [ -z "$PASSKEY" ];then
	export PASSKEY=$1
	shift
fi
cmd=$1
shift
# useful for debug  
#echo passkey $PASSKEY
#echo command $cmd
#echo args "$@"
export args="$@"
export util="com.hamiltonlabs.dataflow.utility"
# Special case the runsql and other SQLs because we can and should make the result readable
case  "$cmd" in 
    "sql" ) 
        java $util.RunSql $PASSKEY "$@"|./tablemaker.sh
        ;;
    "dml" ) 
        java $util.RunUpdate $PASSKEY "$@"|./tablemaker.sh
        ;;
    "Cryptor" )
        # out of order args for cryptor 
	java $util.$cmd $1 $PASSKEY $2
        ;;
    "runs" ) 
	java $util.RunSql $PASSKEY "select * from  datastatus where locktype='OUT' order by dataid desc limit 20"|./tablemaker.sh
        ;; 
    "job" ) 
	echo "select * from  datastatus where locktype='OUT' and jobid=$@ order by dataid desc limit 20"
	java $util.RunSql $PASSKEY "select * from  datastatus where locktype='OUT' and jobid='$args' order by dataid desc limit 20"|./tablemaker.sh
        ;; 
    "jobs" )
	java $util.RunSql $PASSKEY "select * from  job"|./tablemaker.sh
        ;; 
    "datasets" )
	java $util.RunSql $PASSKEY "select datasetid,hostname,database,schemaname,tablename,username,case when length(encryptedpass)>10 then concat(substring(encryptedpass,1,10),'...') else encryptedpass end  as encryptedpass from dataset"|./tablemaker.sh
        ;; 
    *)
        java $util.$cmd $PASSKEY $@
        ;;
esac

