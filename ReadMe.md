# Spring-Boot Camel-Drools QuickStart

This example demonstrates how you can use Apache Camel to integrate a Spring-Boot application running on Kubernetes or OpenShift with a remote Kie Server.

### Kie Server Configuration
The Kie Server should be deployed before running the application and the classic `hellorule` example should be installed and activated. 
The example is based on the `Person` and `Greeting` facts. The Camel route will periodically add a `Person` fact into the remote 
knowledge base and retrieve a `Greeting` created by the rule.

The `hellorule` example is installed by default into the Openshift Decision Server xPaaS Image when using the `decisionserver63-basic-s2i` template 
with the default configuration (the `hellorule` source code is contained in the default git repository used by the template).
If the Decision Server xPaaS image is used, access to the rest API is restricted to authenticated users, so the same _username_ and _password_
combination chosen in the Decision Server xPaaS template must be used in the configuration of the current this quickstart.

### Quickstart Configuration
Username and password as well as the service name of the Kie Server for the current quickstart can be set in the `application.properties` file 
(especially when running this quickstart using the fabric8-maven-plugin) or from the Openshift creation wizard when the _S2I template_ is used.

Configuration through the `application.properties` file can also be useful when running the quickstart locally.

#### Configuration using ConfigMap

The quickstart can also be configured using a _ConfigMap_. 
A file named `quickstart-config-map.yml` should be created with the following content:

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: spring-boot-camel-drools-quickstart
data:
  application.properties: |-
    kieserver.service=kie-app-svc2
    kieserver.username=youruser
    kieserver.password=yourpassword
```

Any property listed in `src/main/resources/application.properties` can be overridden in the _ConfigMap_.

Note that the `metadata.name` property of the _ConfigMap_ must match the `spring.application.name` property inside the `src/main/resources/application.properties` file.  

The _ConfigMap_ should be deployed into Openshift using the following command (before deploying the quickstart):
```bash
oc create -f quickstart-config-map.yml
```

To allow the quickstart to read the new configuration, the `view` role should be granted to the service account responsible for running the pod. 
Pods run using the `default` service account unless otherwise specified. 
The following command can be used to asign the `view` role to the `default` service account into the current project:

```bash
oc policy add-role-to-user view system:serviceaccount:$(oc project -q):default
```

An alternative option is to create a new service account and grant roles to it.

```bash
echo '{"kind":"ServiceAccount","apiVersion":"v1","metadata":{"name":"fis"}}' | oc create -f -
oc policy add-role-to-user view system:serviceaccount:$(oc project -q):fis
```

The previous script creates the `fis` service account and grants the `view` role to it.

The quickstart pod needs an additional configuration to run using the `fis` service account. 
The fabric8 maven plugin provides a easy way to choose the service account 
 by adding the following snippet to the `src/main/fabric8/deployment.yml` file:

```yaml
spec:
  template:
    spec:
      serviceAccount: fis
```

When the quickstart is deployed (see below), it will read the _ConfigMap_ on boot and update its configuration. 

### Building

The example can be built with

    mvn clean install


### Running the example locally

The example can be run locally using the following Maven goal:

    mvn spring-boot:run


### Running the example on Kubernetes

It is assumed a running Kubernetes platform is already running. If not you can find details how to [get started](http://fabric8.io/guide/getStarted/index.html).

Assuming your current shell is connected to Kubernetes or OpenShift so that you can type a command like

```
kubectl get pods
```

or for OpenShift

```
oc get pods
```

Then the following command will package your app and run it on Kubernetes:

```
mvn fabric8:run
```

To list all the running pods:

    oc get pods

Then find the name of the pod that runs this quickstart, and output the logs from the running pods with:

    oc logs <name of pod>

You can also use the [fabric8 developer console](http://fabric8.io/guide/console.html) to manage the running pods, and view logs and much more.


### More details

You can find more details about running this [quickstart](http://fabric8.io/guide/quickstarts/running.html) on the website. This also includes instructions how to change the Docker image user and registry.
