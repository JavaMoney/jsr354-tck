JSR 354: Money and Currency TCK
===============================

JSR 354 provides an API for representing, transporting, and performing comprehensive calculations with Money and Currency. 
See the home page for more details:
http://jcp.org/en/jsr/detail?id=354

The current module contains the technical compatibility kit of JSR 354.

To setup the TCK with your implementation you must follow the following steps:

1) Create a new maven project.
2) Add this TCK and your implementation as dependency.
3) Implement a class of type org.javamoney.tck.TestSetup, read the Javadoc, what 
  you must provide with this class.
  
To execute the TCK, simply execute
  
    mvn clean test site
       
5) Go to target/site - there you will find your TCK test results.


As jump-start you can clone https://github.com/JavaMoney/jsr354-ritest .


The JSR 354 Team.

This module is licenced under the the [Apache 2 Licence](https://www.apache.org/licenses/LICENSE-2.0.html).

[![Build Status](https://api.travis-ci.org/JavaMoney/jsr354-tck.png?branch=master)](https://travis-ci.org/JavaMoney/jsr354-tck) [![License](http://img.shields.io/badge/license-Apache2-red.svg)](http://opensource.org/licenses/apache-2.0)

[![Built with Maven](http://maven.apache.org/images/logos/maven-feather.png)](http://maven.org/)
