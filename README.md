JSR 354: Money and Currency TCK
===============================

JSR 354 provides an API for representing, transporting, and performing comprehensive calculations with Money and Currency. 
See the home page for more details:
http://jcp.org/en/jsr/detail?id=354

The current module contains the technical compatibility kit of JSR 354.

Basically the TCK can be run as a standalone application. Hereby you must ensure you add the following libraries
to your classpath:
- the TCK itself
- the JSR 354 API jar (either for Java 8 or for Java 7)
- your implementation under test and all its dependencies
- TestNG and all its dependencies
- your implementation and SE service registration of *org.javamoney.tck.JSR354TestConfiguration*

You can do this all manually, use your IDE or use maven, the ladder by performing the following tasks:

1) Create a new maven project.
2) Add this TCK, the target API (Java 7 or 8) and your implementation as dependency.
3) Implement a class of type *org.javamoney.tck.JSR354TestConfiguration*, read the Javadoc, what 
  you must provide with this class.
4) Add a single test class with a singlet test as follows:

    @Test
    public void runTCK(){
        TCKRunner.main(new String[0]);
    }
    
4) To execute the TCK, simply execute
  
    mvn clean test

5) Go to target - there you will find your TCK test results.


As jump-start you can clone
- https://github.com/JavaMoney/jsr354-ritest (Java 8), or
- https://github.com/JavaMoney/jsr354-ritest-bp (Java 7)

Additional details and options are also available src/main/asciidoc/userguide.adoc[here] .

The JSR 354 Team.


This module is licenced under the the [Apache 2 Licence](https://www.apache.org/licenses/LICENSE-2.0.html).

[![Build Status](https://api.travis-ci.org/JavaMoney/jsr354-tck.png?branch=master)](https://travis-ci.org/JavaMoney/jsr354-tck) [![License](http://img.shields.io/badge/license-Apache2-red.svg)](http://opensource.org/licenses/apache-2.0)

[![Built with Maven](http://maven.apache.org/images/logos/maven-feather.png)](http://maven.org/)
