## Step 5: Securing your Service

\Implement Spring Security

Need to alter context handling to introduce contextloaderlistener without creating a seperate context
 - along the lines of http://www.rockhoppertech.com/blog/spring-mvc-configuration-without-xml/
 - potentially, this removes the need for a web.xml completely. which would be nice indeed.

Create security context xml (java config is not supported until spring 3.2!)
 - Choose URls to secure.
 - enable http-basic
 - create auth manager, for users.  we have a very lightweight one here, more complex ones are beyond the scope, see other tutorial?
 - wire into main context

 create the spring security filter and filter mapping.




