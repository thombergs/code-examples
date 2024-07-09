# Java 9 Module System

Table of Contents

1. [Introduction](#introduction)

2. [Issues with Previous Java Versions](#issues-with-previous-java-versions)

3. [Benefits of Java 9 Module System](#benefits-of-java-9-module-system)

4. [What is a Module?](#what-is-a-module)

5. [Creating the Modules](#creating-the-modules)

6. [Structuring Our Project](#structuring-our-project)

7. [Creating the First Module](#creating-the-first-module)
8. [Creating the second module](#creating-the-second-module)
9. [Compiling our modules](#compiling-our-modules)
10. [Running our Modules](#running-our-modules)
11. [Updated Module Descriptor](#updated-module-descriptor)
12. [Conclusion](#conclusion)

Java 9 introduced one of the most important features called a module system that allows us to combine all of our code and packages into a single unit. In this tutorial, we’ll discuss what is this module system. why should we use it and how should we use it? We’ll be looking at the answers to all of these questions. Also, we’ll look at a practical example to add glitters to our understanding, so let’s begin.

## Introduction <a id='introduction'></a>

The Java 9 module system, also known as Java Platform Module System (JPMS), is a major feature introduced in Java 9 to address the challenges of scaling and maintaining large Java applications. It is also known as the Jigsaw project during the development phase.

Before Java 9, Java code was organized into packages and classes. However, there were limitations in how these packages and classes could be organized, leading to issues with encapsulation, modularity, and classpath collisions. The Java 9 module system introduces a new concept of modules that allows developers to organize code into logical units that can be managed and used more effectively.

## Issues with Previous Java Versions <a id='issues-with-previous-java-versions'></a>

Before we move to the usage of the module system, first we must know what are the flaws in previous versions of Java that make the Oracle team introduce this feature.

-   JDK Issue: In the previous versions, the Java Development Kit (JDK) is difficult to scale down to smaller devices and applications, as the JAR files it includes, such as rt.jar, are too large. Java SE 8 introduced compact profiles to try to address this issue, but they do not completely solve the problem of performance on smaller devices.
-   Encapsulation Issue: In previous versions, the public access modifiers allow anyone to access several internal APIs thus creating a security threat. The weak encapsulation also makes it difficult to test and maintain the application.
-   Modularity Issue: In previous versions, there was no clear way to define and manage modules. It makes the component of the application highly coupled which makes it difficult to organize and maintain large-scale Java applications.

## Benefits of Java 9 Module System <a id='benefits-of-java-9-module-system'></a>

As we have already seen the issues in the previous versions of Java let’s look at how the module system overcomes these issues.

-   Improved Modularity: In Java 9, JDK, JRE, and Jars are divided into several modules which makes it easy to scale down the Java application and hence improve performance.
-   Enhanced encapsulation: With modules, developers can have better control over which parts of the codebase are accessible to other modules, reducing the risks of unwanted dependencies and conflicts.
-   Dependable configuration: Modules have a clearly defined dependency hierarchy, making it easier to manage dependencies and ensure that the application has all the necessary modules at runtime.
-   Improved performance: The module system can optimize the loading and initialization of modules, resulting in reduced startup time and lower memory usage for the application.
-   Strengthened security: The module system enables developers to define and enforce access controls on code, making the application more secure against potential vulnerabilities.

>**NOTE:** In the module system instead of JAR, modular JAR is introduced having a format JMOD. It includes configuration files and native code. Also, Java, Jlink, and Javac now have additional options to specify module paths that can locate the module descriptor.

## What is a Module? <a id='what-is-a-module'></a>

The module can be considered as the collection of software and Java programs that can be treated as a single unit of deployment, execution, and reuse. Each module contains a file named module-info.java that defines the module’s name, dependencies, and exported packages. You can think of this module-info.java file as an ID card of the module as it describes the module.

The module system is different from the traditional package system. The packages group together the related classes but they don’t provide a way to control access to these classes. In simple words, you may have experienced that a package can be exported to any other package in a Java project without any control whereas in a module explicit control can be provided over what can be accessed from outside the module. This shows that modules provide a lot more encapsulation as compared to packages.

## Creating the Modules <a id='creating-the-modules'></a>

Now we have all the theoretical knowledge about Java 9 module system let’s get our hands dirty by doing some hands-on. Here we’ll create a mini-project that contains two modules. Here we’ll learn how to import a module and how to export a module. We’ll also learn how we can control the access of the packages which is not possible in Java’s previous versions.

## Structuring Our Project <a id='structuring-our-project'></a>

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

## Creating the First Module <a id='creating-the-first-module'></a>

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

## Creating the second module <a id='creating-the-second-module'></a>

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
## Compiling our modules <a id='compiling-our-modules'></a> 

To store the compiled modules we’ll create another directory called **compiledDir** inside the root directory. First, we’ll compile the first module using the below command in the terminal.
```shell
javac -d compiledDir --module-source-path project-modules/ --module first.modules
```
Now, similarly, we’ll compile our second module using the below command in terminal

```shell
$ javac -d compiledDir --module-source-path project-modules/ --module second.modules
```
Now, after running both of the above commands, we can see the compiled version of both of our modules inside our compiledDir directory.

## Running our Modules <a id='running-our-modules'></a>

Now that we have compiled modules with us, let’s run them using the command

```shell
$ java --module-path compiledDir -m second.modules/com.demo.main.Main
```
On executing the above command we’ll get the following output

```shell
Hello readers, I'm a Java Module!
```

## Updated Module Descriptor <a id='updated-module-descriptor'></a>

If we can recall, the module descriptor of our second module only contains the first module. But now, if we execute the below command and have a look at the module descriptor of second module, we can see a change.
```text
Compiled from "module-info.java"
module second.modules {  
  requires java.base;    
  requires first.modules;
}
```
Our second module now also contains one more module “java.base” that we didn’t add from our side. This java.base module is actually a default module and all modules are linked to it.

## Conclusion <a id='conclusion'></a>

In this tutorial, we had an extensive discussion on the Java 9 module system feature. At first, we saw what is Java 9 module system, then we saw what issues did Oracle team face which make them introduce this feature. Then, we saw the benefits of using the module system. There we learned how it provides better encapsulation, modularity, performance, and security. After that, we saw what is module and how it is different from traditional packages. Within this, we saw what is a module descriptor and its use. Then, we created a mini-project. In this mini-project, we created two modules and saw how we could export and import a module. In the end, we compiled our modules and ran them to get our desired output. Therefore we can say that in this tutorial we got both theoretical and practical knowledge of the Java 9 module system.
