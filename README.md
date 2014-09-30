login-modules:  Additional login-modules of JBoss/PicketLink
===============================
Author: Michael Cirioli
Level: Beginner
Technologies:  JBoss EAP/WildFly, PicketLink
Summary: Additional JBoss login-modules providing Radius OTP authentication, static logins, static roles, debugging info


What is it?
-----------
This project contains a number of JBoss EAP/WildFly login-modules that can be used to perform container level 
authentication and authorization.  

Included modules:

    JbossRadiusLoginModule - performs RADIUS OTP based authentication
    
    StaticLoginModule      - useful for test, this module can be configured to allow a static 
                             username and password, static username and any passowrd, or 
                             static password and any username
                             
    StaticRoleLoginModule  - this module performs no authentication, but can be configured to 
                             provide a static role for authenticated users.  This module is 
                             meant to be used in conjunction with password-stacking

    DebugLoginModule       - This module does no authentication or authorization, but will 
                             dump information about the jaas principle and roles known to 
                             the container.  This module is meant to be used in conjunction 
                             with password-stacking


System Requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or WildFly.

The Radius OTP login-module depends on the open source JRadius client.  You can find it here:
  
	https://github.com/bloihl/jradius-client

Configure Maven
---------------

If you have not yet done so, you must (http://www.jboss.org/jdf/quickstarts/jboss-as-quickstart/#configure_maven) before 
testing the quickstarts.


Configure JBoss
---------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat

Build and deploy the login-modules
----------------------------------
_NOTE: The following build command assumes you have configured your Maven user settings. 

1. Build and install the JRadius client library (https://github.com/detiber/jradius-client)
2. Open a command line and navigate to the root directory of this repo
3. Use this command to build the library:

    mvn clean install
    
4. Copy the resulting .jar file from `./target/login-modules-<version>.<version>.jar` to the classpath of your EAP instance

Configure your EAP/WildFly instance to use the login-modules
------------------------------------------------------------
All of the login modules are implemented as standard JBoss login-modules, and can be used in conjunction with 
other modules through the use of the password-stacking option.

Radius OTP Login
----------------
This module can be used to perform Radius OTP authentication when configured against an exist Radius server.  This module
is dependent on the open source jradius-client library, which can be found at https://github.com/bloihl/jradius-client

There is an issue with JBoss session replication that prevents SSO from working properly in a clustered environment when
one or more nodes become unavailable.  Although HTTP sessions are properly replicated, the SSO cache is not, and JBoss 
will try to replay the users credentials upon accessing a new node.  Since OTP is by nature a one-shot authentication, 
this replay will fail and the user will be requested to re-authenticate.  By setting the module option **"fixSessionReplication"** 
to **true**, the login module will recognize this condition and properly intialize the replicated user on the new node.


|Option               |Allowed Values   |Description                                   |
|---------------------|-----------------|----------------------------------------------|
|hostName             |*any string*     |The hostname or ip of the primary radius server|
|secondaryHostName    |*any string*     |The hostname or ip of the secondary radius server|
|sharedSecret         |*any string*     |The shared secret to use|
|authRoleName         |*any string*     |The name of the role you want to give authed users|
|authPort             |*any number*     |The authentication port number to use on the radius server|
|acctPort             |*any number*     |The accounting port number to use on the radius server|
|numRetries           |*any number*     |The number of retries allowed |
|fixSessionReplication|*true* or *false*|enable or disable session replication fix for sso, defaults to *false*|

###example
```
<login-module code="org.picketlink.contrib.loginModules.JbossRadiusLoginModule" flag="required">
    <module-option name="password-stacking" value="useFirstPass"/>
    <module-option name="hostName" value="192.168.1.42"/>
    <module-option name="secondaryHostName" value="192.168.1.142"/>
    <module-option name="sharedSecret" value="mysecret"/>
    <module-option name="authRoleName" value="authenticated"/>
    <module-option name="authPort" value="1812"/>
    <module-option name="acctPort" value="1813"/>
    <module-option name="numRetries" value="3"/>
</login-module>
```
        
Static Role
-----------
This module does not perform any auth, it only provides a statically defined java role for the user.  It is meant to be 
used in conjunction with another login-module that performs authentication.  You can define as many instances of this 
module as you have static roles to assign.        

|Option       |Allowed Values|Description                                   |
|-------------|--------------|----------------------------------------------|
|authRoleName |*any string*  |The name of the role you want to give the user|

###example
```
<login-module code="com.redhat.it.jboss.loginModules.StaticRoleLoginModule" flag="required">
    <module-option name="password-stacking" value="useFirstPass"/>
    <module-option name="authRoleName" value="authenticated"/>         
</login-module>
```

Static Login
------------
This module lets you configure a static username and password and then perform FORM based auth based on the values.  
You can specify it to allow any username with a hard-coded password, or allow any user and any password to successfully 
auth.  Combined with the static role module,this can be used to make standalone testing/development easier.

|Option           |Allowed Values |Description                               |
|-----------------|---------------|------------------------------------------|
|authAnyUser      |true, false    |if true, will accept any username as valid|
|authAnyPass      |true, false    |if true, will accept any password as valid|
|authUserName     |true, false    |if authAnyUser == false, then only the string specified here will be considered a valid username.  This option is ignored if authAnyUser == true|
|authUserPassword |true, false    |if authAnyPass == false, then only the string specified here will be considered a valid password.  This option is ignored if authAnyPass == true|


###example
```
<login-module code="com.redhat.it.jboss.loginModules.StaticLoginModule" flag="required">
        <module-option name="password-stacking" value="useFirstPass" />
        <module-option name="authAnyUser" value="true" />
        <module-option name="authAnyPass" value="false" />
        <module-option name="authUserName" value="testuser" />
        <module-option name="authUserPassword" value="redhat" />
</login-module> 
 ```
Debug Login
------------
This module performs no auth or role generation.  It can be added as the last login-module (using password stacking)
and will dump out a variety of information about the authenticated principal.  It does not take any options.


###example
```
<login-module code="com.redhat.it.jboss.loginModules.DebugLoginModule" flag="required">
        <module-option name="password-stacking" value="useFirstPass" />
</login-module> 
```


