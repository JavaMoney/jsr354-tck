/*
 * Copyright (c) 2012, 2013, Werner Keil, Credit Suisse (Anatole Tresch). Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License. Contributors: Anatole Tresch - initial version.
 */
package org.javamoney.tck.tests;

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Testing Numeric Externalization for numeric values of MonetaryAmount instances.
 * Created by Anatole on 10.03.14.
 */
@SpecVersion(spec = "JSR 354", version = "1.0.0")
public class ExternalizingNumericValueTest{

    /**
     * Checks if number type is not null and returning a concrete (no
     abstract class or interface).
     */
    @SpecAssertion(section = "4.2.3", id = "423-A1")
    @Test
    public void testValidNumberType(){
        fail("Not yet implemented");
    }

    /**
     * Check if a correct integer value is returned, no truncation is
     allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-A2")
    @Test
    public void testValidInteger(){
        fail("Not yet implemented");
    }

    /**
     * Check if a correct long value is returned, no truncation is
     allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-A3")
    @Test
    public void testValidLong(){
        fail("Not yet implemented");
    }

    /**
     * Check if a correct double value is returned, no truncation is
     allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-A4")
    @Test
    public void testValidDouble(){
        fail("Not yet implemented");
    }

    /**
     * Check if a correct number value is returned, no truncation is
     allowed to be performed.
     Check should be done for every JDK type
     supported.
     */
    @SpecAssertion(section = "4.2.3", id = "423-A5")
    @Test
    public void testValidNumber(){
        fail("Not yet implemented");
    }

    /**
     * Check if a correct Integer value is returned, truncation is
     allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-A6")
    @Test
    public void testValidIntegerWithTruncation(){
        fail("Not yet implemented");
    }
    /**
     * Check if a correct Long value is returned, truncation is
     allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-A7")
    @Test
    public void testValidLongWithTruncation(){
        fail("Not yet implemented");
    }
    /**
     * Check if a correct Double value is returned, truncation is
     allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-A8")
    @Test
    public void testValidDoubleWithTruncation(){
        fail("Not yet implemented");
    }
    /**
     * Check if a correct Number value is returned, truncation is
     allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-A9")
    @Test
    public void testValidNumberWithTruncation(){
        fail("Not yet implemented");
    }
    /**
     * Test correct precision values, including border cases.
     */
    @SpecAssertion(section = "4.2.3", id = "423-A10")
    @Test
    public void testPrecisionValues(){
        fail("Not yet implemented");
    }
    /**
     * Test correct precision values, including border cases.
     */
    @SpecAssertion(section = "4.2.3", id = "423-A11")
    @Test
    public void testScaleValues(){
        fail("Not yet implemented");
    }

    // ********************** B. Testing Numeric Externalization for negative values *******************

    /**
     * Checks if number type is not null and returning a concrete (no
     abstract class or interface).
     */
    @SpecAssertion(section = "4.2.3", id = "423-B1")
    @Test
    public void testNumberTypeNegative(){
        fail("Not yet implemented");
    }
    /**
     * Checks if a correct Integer value is returned, no truncation is
     allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-B2")
    @Test
    public void testIntegerNegative(){
        fail("Not yet implemented");
    }

    /**
     * Checks if a correct Long value is returned, no truncation is
     allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-B3")
    @Test
    public void testLongNegative(){
        fail("Not yet implemented");
    }
    /**
     * Checks if a correct Double value is returned, no truncation is
     allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-B4")
    @Test
    public void testDoubleNegative(){
        fail("Not yet implemented");
    }

    /**
     * Check if a correct number value is returned, no truncation is
     allowed to be performed.
     Check should be done for every JDK type
     supported.
     */
    @SpecAssertion(section = "4.2.3", id = "423-B5")
    @Test
    public void testNumberWithTruncationNegative(){
        fail("Not yet implemented");
    }

    /**
     * Check if a correct integer value is returned, truncation is
     allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-B6")
    @Test
    public void testIntegerWithTruncationNegative(){
        fail("Not yet implemented");
    }

    /**
     * Check if a correct long value is returned, truncation is
     allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-B7")
    @Test
    public void testLongWithTruncationNegative(){
        fail("Not yet implemented");
    }
    /**
     * Check if a correct double value is returned, truncation is
     allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-B8")
    @Test
    public void testDoubleWithTruncationNegative(){
        fail("Not yet implemented");
    }

    /**
     * Check if a correct double value is returned, truncation is
     allowed to be performed. Check should be done for every JDK type
     supported.
     */
    @SpecAssertion(section = "4.2.3", id = "423-B9")
    @Test
    public void testNumberValueWithTruncationNegative(){
        fail("Not yet implemented");
    }

    /**
     * Test correct precision values, including border cases.
     */
    @SpecAssertion(section = "4.2.3", id = "423-B10")
    @Test
    public void testPrecisionNegative(){
        fail("Not yet implemented");
    }

    /**
     * Test correct scale values, including border cases.
     */
    @SpecAssertion(section = "4.2.3", id = "423-B11")
    @Test
    public void testScaleNegative(){
        fail("Not yet implemented");
    }

    // ********************* C. Testing Numeric Externalization for zero values **************

    /**
     * Checks if number type is not null and returning a concrete (no
     abstract class or interface).
     */
    @SpecAssertion(section = "4.2.3", id = "423-C1")
    @Test
    public void testNumberTypeZero(){
        fail("Not yet implemented");
    }

    /**
     * Check if a correct integer value is returned, no truncation is
     allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-C2")
    @Test
    public void testIntegerZero(){
        fail("Not yet implemented");
    }

    /**
     * Check if a correct long value is returned, no truncation is
     allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-C3")
    @Test
    public void testLongZero(){
        fail("Not yet implemented");
    }

    /**
     * Check if a correct double value is returned, no truncation is
     allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-C4")
    @Test
    public void testDoubleZero(){
        fail("Not yet implemented");
    }

    /**
     * Check if a correct number value is returned, no truncation is
     allowed to be performed.
     Check should be done for every JDK type
     supported.
     */
    @SpecAssertion(section = "4.2.3", id = "423-C5")
    @Test
    public void testNumberValueZero(){
        fail("Not yet implemented");
    }

    /**
     * Check if a correct integer value is returned, truncation is
     allowed to be performed.
     Check should be done for every JDK type
     supported.
     */
    @SpecAssertion(section = "4.2.3", id = "423-C6")
    @Test
    public void testIntegerValueWithTruncationZero(){
        fail("Not yet implemented");
    }

    /**
     * Check if a correct long value is returned, truncation is
     allowed to be performed.
     Check should be done for every JDK type
     supported.
     */
    @SpecAssertion(section = "4.2.3", id = "423-C7")
    @Test
    public void testLongValueWithTruncationZero(){
        fail("Not yet implemented");
    }
    /**
     * Check if a correct double value is returned, truncation is
     allowed to be performed.
     Check should be done for every JDK type
     supported.
     */
    @SpecAssertion(section = "4.2.3", id = "423-C8")
    @Test
    public void testDoubleValueWithTruncationZero(){
        fail("Not yet implemented");
    }
    /**
     * Check if a correct Number value is returned, truncation is
     allowed to be performed.
     Check should be done for every JDK type
     supported.
     */
    @SpecAssertion(section = "4.2.3", id = "423-C9")
    @Test
    public void testNumberValueWithTruncationZero(){
        fail("Not yet implemented");
    }
    /**
     * Check if a correct precision value is returned.
     Check should be done for every JDK type
     supported.
     */
    @SpecAssertion(section = "4.2.3", id = "423-C10")
    @Test
    public void testPrecisioZero(){
        fail("Not yet implemented");
    }
    /**
     * Check if a correct precision value is returned.
     Check should be done for every JDK type
     supported.
     */
    @SpecAssertion(section = "4.2.3", id = "423-C11")
    @Test
    public void testScaleZero(){
        fail("Not yet implemented");
    }

}
