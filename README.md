JSR 354: Money and Currency TCK
===============================

JSR 354 provides an API for representing, transporting, and performing comprehensive calculations with Money and Currency. 
For more details see [JavaMoney.org](http://javamoney.org) and [JSR 354: Money and Currency API](https://jcp.org/en/jsr/detail?id=354)

The current module contains the technical compatibility kit of JSR 354.

Basically the TCK can be run as a standalone application. Hereby you must ensure you add the following libraries
to your classpath:
- the TCK itself
- the JSR 354 API jar (for Java 8 and above)
- your implementation under test and all its dependencies
- TestNG and all its dependencies
- your implementation and SE service registration of `org.javamoney.tck.JSR354TestConfiguration`

You can do this all manually, use your IDE or use Maven, the latter by performing the following tasks:

1. Create a new project Maven or Gradle.
2. Add dependencies:
   * Add this TCK [org.javamoney:javamoney-tck](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.javamoney%22%20AND%20a%3A%22javamoney-tck%22)
   * Add the target API [javax.money:money-api](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22javax.money%22%20AND%20a%3A%22money-api%22) for Java 8 and above
   * and your implementation as dependency
3. Implement a class of type [org.javamoney.tck.JSR354TestConfiguration](./src/main/java/org/javamoney/tck/JSR354TestConfiguration.java), read the Javadoc, what you must provide with this class.
4. Add a single test class with a single test as follows:

     ```java
     @Test
     public void runTCK(){
         TCKRunner.main();
     }
     ```

5. To execute the TCK, simply execute

   ```
       mvn clean test
   ```

6. Go to target - there you will find your TCK test results.


As jump-start you can clone [javamoney-tck-usage-example](https://github.com/JavaMoney/javamoney-tck-usage-example) (master: Java 8 and above, backport: Java 7)

Additional details and options are also available in [user guide](src/main/asciidoc/userguide.adoc).

The JSR 354 Team.


This module is licenced under the the [Apache 2 Licence](https://www.apache.org/licenses/LICENSE-2.0.html).

[![Maven Central](https://img.shields.io/maven-central/v/org.javamoney/javamoney-tck.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.javamoney%22%20AND%20a%3A%22javamoney-tck%22)
[![Build Status](https://api.travis-ci.org/JavaMoney/jsr354-tck.png?branch=master)](https://travis-ci.org/JavaMoney/jsr354-tck) [![License](http://img.shields.io/badge/license-Apache2-red.svg)](http://opensource.org/licenses/apache-2.0)

[![Built with Maven](http://maven.apache.org/images/logos/maven-feather.png)](http://maven.org/)
