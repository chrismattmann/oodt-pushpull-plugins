# oodt-pushpull-plugins

A set of plugins for the [Apache OODT Push Pull System](http://svn.apache.org/repos/asf/oodt/trunk/pushpull) that include dependencies on LGPL libraries.

# Building and Installing the plugin Jars

Follow the steps below to install the oodt-pushpull-plugins Jar file.
 1. git clone https://github.com/chrismattmann/oodt-pushpull-plugins.git
 2. cd oodt-pushpull-plugins
 3. mvn install

Then you'll have a Jar file called oodt-pushpull-plugins-core-0.6-SNAPSHOT.jar. You can use it 2 ways:

 1. drop that Jar file into $PUSHPULL_HOME/lib, and then reference the new ftp plugins (Ftp4CheFtpClientFactory and JvFtpClientFactory) in the PushPull ProtocolFactoryInfo.xml file (which is in the $PUSHPULL_HOME/policy directory). 
Notes: (1) Please also comment out the old ftp plugins (CogJGlobusFtpProtocolFactory and CommonsNetFtpProtocolFactory) in the PushPull ProtocolFactoryInfo.xml file, so that the PushPull will use the new ftp plugins instead of the old ones.
[PUSHPULL_HOME]/policy/ProtocolFactoryInfo.xml
```
<protocol type="ftp">
    <!-- <protocolFactory class="org.apache.oodt.cas.protocol.ftp.CogJGlobusFtpProtocolFactory"/> -->
    <!-- <protocolFactory class="org.apache.oodt.cas.protocol.ftp.CommonsNetFtpProtocolFactory"/> -->
    <protocolFactory class="org.apacheextras.oodt.cas.pushpull.protocol.ftp.Ftp4CheFtpClientFactory"/>
    <protocolFactory class="org.apacheextras.oodt.cas.pushpull.protocol.ftp.JvFtpClientFactory"/>
</protocol>
```
 2. Please also download the Ftp4Che and JvFtp LGPL licensed libraries, and drop all the jar files into $PUSHPULL_HOME/lib directory, so that the new ftp plugins (Ftp4CheFtpClientFactory and JvFtpClientFactory) could leverage these two libraries. 
use it in your Maven2 project by adding a dependency on:
```
<dependency>
   <groupId>org.apacheextras.oodt</groupId>
   <artifactId>oodt-pushpull-plugins-core</artifactId>
   <version>0.6-SNAPSHOT</version>
 </dependency>
 ```
