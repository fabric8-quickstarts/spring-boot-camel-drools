# Spring-Boot Camel-Drools QuickStart

This example demonstrates how you can use Apache Camel to integrate a Spring-Boot application running on OpenShift with a remote Kie Server.

### Kie Server Configuration
The Kie Server should be deployed before running the application and the classic `hellorule` example should be installed and activated. 
The example is based on the `Person` and `Greeting` facts. The Camel route will periodically add a `Person` fact into the remote 
knowledge base and retrieve a `Greeting` created by the rule.

The `hellorule` example is installed by default into the OpenShift Decision Server xPaaS Image when using the `decisionserver64-basic-s2i` template 
with the default configuration (the `hellorule` source code is contained in the default git repository used by the template).
If the Decision Server xPaaS image is used, access to the rest API is restricted to authenticated users, so the same _username_ and _password_
combination chosen in the Decision Server xPaaS template must be used in the configuration of the current this quickstart.

### Quickstart Configuration
Username and password as well as the service name of the Kie Server for the current quickstart can be set in the `application.properties` file 
(especially when running this quickstart using the fabric8-maven-plugin) or from the OpenShift creation wizard when the _S2I template_ is used.

Configuration through the `application.properties` file can also be useful when running the quickstart locally.

### Building

The example can be built with

    mvn clean install

### Running the example in OpenShift

It is assumed that:
- OpenShift platform is already running, if not you can find details how to [Install OpenShift at your site](https://docs.openshift.com/container-platform/3.3/install_config/index.html).
- Your system is configured for Fabric8 Maven Workflow, if not you can find a [Get Started Guide](https://access.redhat.com/documentation/en/red-hat-jboss-middleware-for-openshift/3/single/red-hat-jboss-fuse-integration-services-20-for-openshift/)

The example can be built and run on OpenShift using a single goal:

    mvn fabric8:deploy

To list all the running pods:

    oc get pods

Then find the name of the pod that runs this quickstart, and output the logs from the running pods with:

    oc logs <name of pod>

You can also use the OpenShift [web console](https://docs.openshift.com/container-platform/3.3/getting_started/developers_console.html#developers-console-video) to manage the running pods, and view logs and much more.

### Running via an S2I Application Template

Application templates allow you deploy applications to OpenShift by filling out a form in the OpenShift console that allows you to adjust deployment parameters.  This template uses an S2I source build so that it handle building and deploying the application for you.

First, import the Fuse image streams:

    oc create -f https://raw.githubusercontent.com/jboss-fuse/application-templates/GA/fis-image-streams.json

Then create the quickstart template:

    oc create -f https://raw.githubusercontent.com/jboss-fuse/application-templates/GA/quickstarts/spring-boot-camel-drools-template.json

Now when you use "Add to Project" button in the OpenShift console, you should see a template for this quickstart. 
