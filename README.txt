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
   
   
   
