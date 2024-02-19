# Exloring the Java 9 Module System

Java 9 introduced one of the most important features called a module system that allows us to combine all of our code and packages into a single unit. In this tutorial, we’ll discuss what is this module system. why should we use it and how should we use it? We’ll be looking at the answers to all of these questions. Also, we’ll look at a practical example to add glitters to our understanding, so let’s begin.

## Introduction

The Java 9 module system, also known as Java Platform Module System (JPMS), is a major feature introduced in Java 9 to address the challenges of scaling and maintaining large Java applications. It is also known as the Jigsaw project during the development phase.

Before Java 9, Java code was organized into packages and classes. However, there were limitations in how these packages and classes could be organized, leading to issues with encapsulation, modularity, and classpath collisions. The Java 9 module system introduces a new concept of modules that allows developers to organize code into logical units that can be managed and used more effectively.

## Issues with Previous Java Versions

Before we move to the usage of the module system, first we must know what are the flaws in previous versions of Java due to which the module system is introduced in Java.

-   JDK Issue: In the previous versions, the Java Development Kit (JDK) is difficult to scale down to smaller devices and applications, as the JAR files it includes, such as rt.jar, are too large. Java SE 8 introduced compact profiles to try to address this issue, but they do not completely solve the problem of performance on smaller devices.
-   Encapsulation Issue: In previous versions, the public access modifiers allow anyone to access several internal APIs thus creating a security threat. The weak encapsulation also makes it difficult to test and maintain the application.
-   Modularity Issue: In previous versions, there was no clear way to define and manage modules. It makes the component of the application highly coupled which makes it difficult to organize and maintain large-scale Java applications.

## Benefits of Java 9 Module System

As we have already seen the issues in the previous versions of Java let’s look at how the module system overcomes these issues.

-   Improved Modularity: In Java 9, JDK, JRE, and Jars are divided into several modules which makes it easy to scale down the Java application and hence improve performance.
-   Enhanced encapsulation: With modules, developers can have better control over which parts of the codebase are accessible to other modules, reducing the risks of unwanted dependencies and conflicts.
-   Dependable configuration: Modules have a clearly defined dependency hierarchy, making it easier to manage dependencies and ensure that the application has all the necessary modules at runtime.
-   Improved performance: The module system can optimize the loading and initialization of modules, resulting in reduced startup time and lower memory usage for the application.
-   Strengthened security: The module system enables developers to define and enforce access controls on code, making the application more secure against potential vulnerabilities.

>**NOTE:** In the module system instead of JAR, modular JAR is introduced having a format JMOD. It includes configuration files and native code. Also, Java, Jlink, and Javac now have additional options to specify module paths that can locate the module descriptor.

## What is a Module?

The module can be considered as the collection of software and Java programs that can be treated as a single unit of deployment, execution, and reuse. Each module contains a file named module-info.java that defines the module’s name, dependencies, and exported packages. You can think of this module-info.java file as an ID card of the module as it describes the module.

The module system is different from the traditional package system. The packages group together the related classes but they don’t provide a way to control access to these classes. In simple words, you may have experienced that a package can be exported to any other package in a Java project without any control whereas in a module explicit control can be provided over what can be accessed from outside the module. This shows that modules provide a lot more encapsulation as compared to packages.

## Implementing the Module System

Now we have all the theoretical knowledge about Java 9 module system let’s get our hands dirty by doing some hands-on. Here we’ll create a mini-project that contains two modules. Here we’ll learn how to import a module and how to export a module. We’ll also learn how we can control the access of the packages which is not possible in Java’s previous versions.

### Structuring Our Project

Below is what our project structure looks like

![alt text](https://github.com/H11199/module-system-structure/blob/70c7d175c117e04d3e6e6a2b3bbbf9d1e9c5f019/Project-structure.JPG?raw=true)

First, we’ll create the structure of the project. Here we’ll be creating a lot of directories, therefore, it may seem a little bit confusing but we’ll cover it in the easiest way possible. Let’s start by creating our root project folder.

```shell
$ mkdir Java-9-modules
$ cd Java-9-modules
```

Now this Java-9-modules directory will be our root directory and contains all project-related directories. Inside this Java-9-modules directory, we’ll create one directory that contains all the modules associated with our project.

```shell
$ mkdir project-modules
```

### Creating the First Module

Inside the **project-modules** directory we'll create another directory named **first.modules**. This directory is nothing but our first module and contains packages. Create a package inside first.modules directory having a structure as

`com.demo.helloWorld.`

Create the Java class called HelloWorld.java inside this package. This class contains a static function perfromOperation that prints a hello message.

```java
package com.demo.helloWorld;
public class HelloWorld {
   public static void performOperation() {
       System.out.println("Hello readers, I'm a Java Module!");
   }
}
```
>NOTE: we can use any module name but we should also keep in mind that the name of the module should follow a reverse domain pattern i.e same naming convention as that of packages.

Now to have control over this module we need to create a **module-info.java file** inside it. As we have discussed above, this file is a module descriptor.

```java
module first.modules{
   exports com.demo.helloWorld;
}
```

### Creating the second module

This module depends on our first module as this module will use the perfromOperation function inside the HelloWorld class. To use the first module we should import it inside our second module using **“requires”** directive in the module descriptor of the second module.

```java
module second.modules{
   requires first.modules;
}
```
Now we’ll create an application that uses the imported module. For that create a package within the **second.modules** named **com.demo.main.**

Inside this package, we’ll create our **Main.java file** that contains the application code.

```java
package com.demo.main;
import com.demo.helloWorld.HelloWorld;
public class Main {
   public static void main(String[] args) {
       HelloWorld.performOperation();
   }
}
```
### Compiling our modules

To store the compiled modules we’ll create another directory called **compiledDir** inside the root directory. First, we’ll compile the first module using the below command in the terminal.
```shell
javac -d compiledDir --module-source-path project-modules/ --module first.modules
```
Now, similarly, we’ll compile our second module using the below command in terminal

```shell
$ javac -d compiledDir --module-source-path project-modules/ --module second.modules
```
Now, after running both of the above commands, we can see the compiled version of both of our modules inside our compiledDir directory.

### Running our Modules

Now that we have compiled modules with us, let’s run them using the command

```shell
$ java --module-path compiledDir -m second.modules/com.demo.main.Main
```
On executing the above command we’ll get the following output

```shell
Hello readers, I'm a Java Module!
```

### Updated Module Descriptor

If we can recall, the module descriptor of our second module only contains the first module. But now, if we execute the below command and have a look at the module descriptor of second module, we can see a change.
```text
Compiled from "module-info.java"
module second.modules {  
  requires java.base;    
  requires first.modules;
}
```
Our second module now also contains one more module “java.base” that we didn’t add from our side. This java.base module is actually a default module and all modules are linked to it.

### Packaging Our Modules
We know that to make a Java application deployment ready we package it to a JAR file. Similarly, a Java module is packaged as a modular JAR. This modular JAR is the same as normal JAR except it has module-info.class at its root directory. Each module should have separate JAR files for them. Here we have two modules in our project, therefore we’ll create two JARs. At first we’ll create a directory to store our JAR files.
```shell
mkdir myJAR
```
Now to create JAR file for module “first.modules” we’ll use the below command.
```shell
jar --create --file=myJAR/first.jar --module-version=1.0 -C compiledDir/first.modules .
```
Now, since our second.modules module contains the Main class, therefore the command to create a JAR for it changes a little.
```shell
jar --create --file=myJAR/second.jar --main-class=com.demo.main.Main -C compiledDir/second.modules
```
To get any help regarding the JAR creation we can use the below command.
```shell
jar --help
```
Suppose we need to get details of any JAR then the below command will do it for us.
```shell
jar --describe-module --file=myJAR/second.jar
```
The above command will return the following details of module “second.modules”.
```text
second.modules jar:file:///F:/Java-9-modules/myJAR/second.jar/!module-info.class
requires first.modules
requires java.base mandated
contains com.demo.main
main-class com.demo.main.Main
```
Once both of the above commands are successfully executed, then we’ll have two JARs inside our myJAR directory “first.jar” and “second.jar”.
Since the module “second.modules” contains our Main class, therefore, we can use the below command to run our application.
```shell
java -p myJAR -m second.modules
```

### Creating Custom JDK
It is one of the most important features of the Java 9 module system. As we have discussed above, in Java 9 we don’t need the monolithic JDK anymore. Instead here we can create a custom JDK that only has modules that we require in our application. This provides scalability to our application and hence improves application performance.

To achieve this, Java 9 provides us jlink tool. Using this we can link a set of modules along with their transitive dependencies to create a custom run-time image. Currently, jlink requires of modules to be packaged in modular JAR or JMOD format. Since we have already packaged our modules in modular JAR let’s start creating or custom JDK.
```shell
jlink --module-path myJAR --add-modules second.modules --output modulesAPP
```
In the above command the value of the “--module-path” is the path of the directory that contains our modular JARs. The “--add-modules” is used to add all the modules we need. The value of “--output” is the path of the directory inside which our custom run-time image will be created.

To run our application using the custom JDK, first, we need to move to the director that contains our custom JDK and run the following command.
```shell
cd modulesAPP/
bin/java --module-path ./modulesAPP --module second.modules/com.demo.main.Main
```
On executing the above command successfully, we’ll have the following output.
```text
Hello readers, I'm a Java Module!
```
## Why Module System is not Popular?
Although Java 9 module system provides some significant advantages still it is not widely used for application development. Some of the reasons could be:
* Firstly, the module system requires applications to be modular, which means that existing applications need to be restructured to work with the new system. This can be a significant effort, especially for large and complex applications.
* Secondly, developers need to learn new concepts, such as module-info.java files, modular jars, and modular class-loading. This learning curve can be a problem for some developers, especially those who are not familiar with the existing Java class-loading mechanisms.
* Thirdly, while many development tools have added support for the module system, some tools still lack support or have limited support. This can make it harder for developers to use the module system in their development workflow.

Since it’s a new feature and therefore it is obvious for it to take some time to get popular among developers. But now, many developers are using the module system for application development and we’ll see wider adoption of the module system in coming years.

## Conclusion

In this tutorial, we had an extensive discussion on the Java 9 module system feature. At first, we saw what is Java 9 module system, then we saw flaws in previous versions of Java due to which the module system is introduced. Then, we saw the benefits of using the module system. There we learned how it provides better encapsulation, modularity, performance, and security. After that, we saw what a module is and how it is different from traditional packages. Within this, we saw what is a module descriptor and its use. Then, we created a mini-project. In this mini-project, we created two modules and saw how we could export and import a module. Then, we compiled our modules and ran them to get our desired output. Then, we packaged our modules as modular JARs, and using jlink tool we created our custom JDK. In the end, we saw the reasons why the module system is not popular. Therefore, we can say that in this tutorial we got both theoretical and practical knowledge of the Java 9 module system.