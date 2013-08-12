## Step 3: Wiring Up and Deploying your Service

Create a basic Spring JavaConfig context in the new config domain - CoreConfig
Create a integration test of the integrated core/ persistence domains - CoreDomainIntegrationTest

Create a Config covering the rest components. - MVCConfig
Create an integration that runs up both configurations together and exercises the MVC stack fully integrated with the core - RestDomainIntegrationTest.  
THis is outside of a web container, but includes the DispatcherServlet (as did the controller integration tests written on page 2)

Setting up a web.xml - we use JavaCOnfig, so use AnnotationConfigWebApplicationContext as contextClass init param
 - we have created a configuration domain, com.yummynoodlebar.core.config

to be able to build a war 

build.gradle
apply plugin: 'war'

to be able to run up in dev mode

build.gradle
apply plugin: 'tomcat'

Change context path otherwise the default is the directory name */
build.gradle
tomcatRunWar.contextPath = ''

We can now make a working war file 

./gradlew war


We can start the app in dev mode using gradle

./gradlew tomcatRunWar

Then visit http://localhost:8080/aggregators/orders

You will get a response JSON that is an empty list 

We need to check that this is all set up validly, taking us on to the next subject, functional testing using RestTemplate

[Next… Testing your Service using RESTTemplate](../4/)
