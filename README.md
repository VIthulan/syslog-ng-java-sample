# syslog-ng Java file destination

This is a sample implemantation of syslog-ng Java destination for File. You can send your source logs to another file destination using syslog-ng Java destinations.  

##How to 

Run `gradle fatJar` in syslog-ng-java-sample root folder.    
Create a configuration file for syslog-ng Java destination. 

#### Sample configuration file 

```
@version: 3.7

source sample_text {
	file("/x/y/z/sample.log"
	follow_freq(1));	
};

destination d_java_file {
	java(
	 class_name("FileDestination")
	 #class_path("$Projec_Home/syslogng-java-destination/src/main/java/")
	 class_path("$Project_Home/syslogng-java-destination/build/libs/javadestination-all-1.0-SNAPSHOT.jar")
	 option("name","fileDest")
	 option("filename","test.txt")
	 option("filepath","$Project_Home/Tests_Files/")
	);
};

log {
	source(sample_text);
	destination(d_java_file);
	flags(flow-control);
};
```
Now run
`syslog-ng -Fe -f /x/y/z/sample.conf`
