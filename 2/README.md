## Step 2: Building Your First RESTful Service

It's time to implement your Yummy Noodle Bar RESTful service. To do that, open

(the below is copy and paste, rewrite)
NB.  Java Config, as set up in MVCConfig, will detect the existence of Jackson and JAXB 2 on the classpath and automatically creates and registers default JSON and XML converters. The functionality of the annotation is equivalent to the XML version:

<mvc:annotation-driven />

This is a shortcut, and though it may be useful in many situations, it’s not perfect. When more complex configuration is needed, remove the annotation and extend WebMvcConfigurationSupport directly.

^^^^^^^
The above has been moved to page3, leaving this page purely in the MockMVC tests

Mention the split of Commands from Queries across two controllers, for mutating requests and non-mutating ones.

[Next… Wiring Up and Deploying your Service](../3/)