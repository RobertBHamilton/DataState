# OpenDataFlow.core
This is the core component of OpenDataFlow utilities. It handles all accesses to the underlying data store of the framework. 

OpenDataFlow treats the data handoff between ETL processes in a transactional model. The handshake requires certain guarantees made by the producer in order that the consumer may accept the data. This is a data centric model in that these guarantees involve characteristics or status of the data being handed off. It is a departure from traditional job schedulers which are typically process centric instead. Where the traditional schedulers make assumptions about the data status, DataFlow instead specifically tracks the status and permits processes to rely on reliable data status.

The DataFlow core components manage three sets of information.

1. Real time status of all data sets tracked by the tool
2. Metadata for all tracked data sources
3. Metadata for fully registered jobs



# History and acknowledgements.
The concepts and design pattern concepts that I have been evolving and refining over several decades during my career as an ETL software engineer. It arises from career experience that spans several fortune 50 companies and a few smaller ones.  This design pattern is designed specifically to address the issues and headaches encountered during this time while performing tasks in involing erprise scale data movement. We are startng this program from scratch, for the first time designing a framework from the ground up.  It a core framework modeled on concepts which have accumulated over the years with experience and learning. It is also a reference implementation built on the framework. The reference framework itself will be fully functional tools useful for enterprise class applications.

# Motivation

After doing root cause analysis, impact analysis, tracing data lineage and other data forensics, the questions often asked are "was this task more tedious and less reliable than it should be?" "Are there ways to speed up the investigation and make it more reliable?"  
Once the outage has been root caused, the question that follows is "What can be done to reduce the occurence of outage? Once failure modes are identified and root caused the natural question is can more be done to reduce the likelihood of the outage to occure again? Can we minimize factors that contribute to the root cause.  
While recovoring from the outage, are there manual steps involved in restarting processes, determining if data dependencies are met, cleaning up stale files or flags?
Can we reduce the amount of specialized knowledge needed to restart the entire flow, and reduce the complexity of the tasks.
 
The answer to all these questions is most certainly "yes".  However, traditional job schedulers however sophisticated they are, may not provide the kind of functionality needed to deal with these specific questions. As evidence of that, I would cite many incidents in which the principle investigator manually performs all of data mining, root cause and correction, manual cleanups and data dependency analysis, and when each job is ready for a relatively clean start they then ask scheduling services to restart failed jobs, possibly one by one.

These tend to be expensive exercises. The more that we can automate the capture of useful information at runtime, automate the dependency tracking, automate or systematize the logic, and make job starts/restarts nearly touchless, the more reliable the enterprise data/analytics platform will become.

In this author's experience over nearly three decades of ETL developement and support, almost all operational issues with data flow come about because of inaccurate assumptions made with respect to these 5 questions:

# The transactional model


Our approach is to treat each data set consumed or produced by a job as an indivisible unit, and one that is passed from output of one job to input of the next with the same kind of checks and controls that you would apply to any data transfer protocol.

Simply put, in order for a job to accept a chunk of data for consumption, it needs to confirm the answer "yes" to each of these five questions.

1. Is the data ready
2. Is it the correct data
3. Is the data in the physical location the consumer expects
4. Is the data validated
5. Is the data safe to access or modify

All data transfer protocols are built around these questions one way or another. It is quite reasonable to posit that ETL building on larger blocks of data have comparable requirements.  These are also the first questions the analyst must answer when tasked with finding root cause of job failures. 

Both the task of the analyst and the logic used by the consumer are simplified if this information is systematically captured at run time and directly used for coordination between jobs having data dependencies.



