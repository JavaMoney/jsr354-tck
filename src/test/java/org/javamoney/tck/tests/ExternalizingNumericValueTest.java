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

import org.javamoney.tck.tests.internal.TestAmount;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.Test;

import javax.money.MonetaryAmount;
import javax.money.MonetaryAmounts;
import javax.money.MonetaryException;
import javax.money.NumberValue;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

/**
 * Testing Numeric Externalization for numeric values of MonetaryAmount instances.
 * Created by Anatole on 10.03.14.
 */
@SpecVersion(spec = "JSR 354", version = "1.0.0")
public class ExternalizingNumericValueTest{

    private final static String DEFAULT_CURRENCY = "CHF";

    /**
     * Checks if number type is not null
     */
    @SpecAssertion(section = "4.2.3", id = "423-A1")
    @Test
    public void testReturningNumberValueIsNotNull(){
        for(Class type : MonetaryAmounts.getAmountTypes()){
            if(type.equals(TestAmount.class)){
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            NumberValue result = mAmount1.getNumber();
            assertThat("Amount type does not return a NumberValue (null); " + type.getName(), result, notNullValue());
        }
    }

    /**
     * Check if a correct integer value is returned, no truncation is
     * allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-A2")
    @Test
    public void testValidInteger(){
        int[] nums = new int[]{-3, -1, 0, 1, 3};
        for(int num : nums){
            for(Class type : MonetaryAmounts.getAmountTypes()){
                if(type.equals(TestAmount.class)){
                    continue;
                }
                MonetaryAmount mAmount1 =
                        MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num).create();
                NumberValue result = mAmount1.getNumber();
                assertThat("Amount creation failed for " + type, result, notNullValue());
                assertEquals("Number value (int) returned is not correct for " + type.getName(), num,
                             result.intValue());
                assertEquals("Exact number value (int) returned is not correct for " + type.getName(), num,
                             result.intValueExact());
            }
        }
    }

    /**
     * Check if a correct long value is returned, no truncation is
     * allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-A3")
    @Test
    public void testValidLong(){
        long[] nums = new long[]{-3, -1, 0, 1, 3};
        for(long num : nums){
            for(Class type : MonetaryAmounts.getAmountTypes()){
                if(type.equals(TestAmount.class)){
                    continue;
                }
                MonetaryAmount mAmount1 =
                        MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num).create();
                NumberValue result = mAmount1.getNumber();
                assertThat("Amount creation failed for " + type, result, notNullValue());
                assertEquals("Number value (long) returned is not correct for " + type.getName(), num,
                             result.longValue());
                assertEquals("Exact number (long) (double) returned is not correct for " + type.getName(), num,
                             result.longValueExact());
            }
        }
    }


    /**
     * Check if a correct double value is returned, no truncation is
     * allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-A4")
    @Test
    public void testValidDouble(){
        double[] nums = new double[]{-3, -3.5 - 1, -1.2, 0, 0.3, 1, 1.3453};
        for(double num : nums){
            for(Class type : MonetaryAmounts.getAmountTypes()){
                if(type.equals(TestAmount.class)){
                    continue;
                }
                MonetaryAmount mAmount1 =
                        MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num).create();
                NumberValue result = mAmount1.getNumber();
                assertThat("Amount creation failed for " + type, result, notNullValue());
                assertEquals("Number value (double) returned is not correct for " + type.getName(), num,
                             result.doubleValue(), 0d);
                assertEquals("Exact number value (double) returned is not correct for " + type.getName(), num,
                             result.doubleValueExact(), 0d);
            }
        }
    }

    /**
     * Check if a correct number value is returned, no truncation is
     * allowed to be performed.
     * Check should be done for every JDK type
     * supported.
     */
    @SpecAssertion(section = "4.2.3", id = "423-A5")
    @Test
    public void testValidNumberBD(){
        Number[] nums = new Number[]{-3, -3.5f - 1L, -1.2d, (short) 0, 0.3, (byte) 1, 1.3453};
        for(Number num : nums){
            for(Class type : MonetaryAmounts.getAmountTypes()){
                if(type.equals(TestAmount.class)){
                    continue;
                }
                MonetaryAmount mAmount1 =
                        MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num).create();
                NumberValue result = mAmount1.getNumber();
                assertEquals("Number value (BigDecimal) returned is not correct for " + type.getName(),
                             new BigDecimal(String.valueOf(num)).stripTrailingZeros(),
                             result.numberValue(BigDecimal.class).stripTrailingZeros());
                assertEquals("Exact number value (BigDecimal) returned is not correct for " + type.getName(),
                             new BigDecimal(String.valueOf(num)).stripTrailingZeros(),
                             result.numberValue(BigDecimal.class).stripTrailingZeros());
            }
        }
    }

    /**
     * Check if a correct number value is returned, no truncation is
     * allowed to be performed.
     * Check should be done for every JDK type
     * supported.
     */
    @SpecAssertion(section = "4.2.3", id = "423-A5")
    @Test
    public void testValidNumberBI(){
        Number[] nums = new Number[]{-3, -1L, (short) 0, (byte) 1};
        for(Number num : nums){
            for(Class type : MonetaryAmounts.getAmountTypes()){
                if(type.equals(TestAmount.class)){
                    continue;
                }
                MonetaryAmount mAmount1 =
                        MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num).create();
                NumberValue result = mAmount1.getNumber();
                assertEquals("Number value (BigInteger) returned is not correct for " + type.getName(),
                             new BigInteger(String.valueOf(num)), result.numberValue(BigInteger.class));
                assertEquals("Exact number value (BigInteger) returned is not correct for " + type.getName(),
                             new BigInteger(String.valueOf(num)), result.numberValue(BigInteger.class));
            }
        }
    }

    /**
     * Check if a correct Integer value is returned, truncation is
     * allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-A6")
    @Test
    public void testValidIntegerWithTruncation(){
        double[] nums = new double[]{-3.12334, -1.23345, 0.4343, 1.3343435, 5.345454};
        for(double num : nums){
            for(Class type : MonetaryAmounts.getAmountTypes()){
                if(type.equals(TestAmount.class)){
                    continue;
                }
                MonetaryAmount mAmount1 = null;
                try{
                    mAmount1 = MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num)
                            .create();
                }
                catch(MonetaryException | ArithmeticException e){
                    // It is possible, that our test may exceed the capabilities, so in that case, we just continue
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                assertEquals("Number value (int, truncated) returned is not correct for " + type.getName(), (int) num,
                             result.intValue());
                try{
                    result.intValueExact();
                    fail("Number value (int, exact -> truncated) must throw ArithemticException on truncation for " +
                                 type.getName());
                }
                catch(ArithmeticException e){
                    // OK
                }
            }
        }
    }

    /**
     * Check if a correct Long value is returned, truncation is
     * allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-A7")
    @Test
    public void testValidLongWithTruncation(){
        double[] nums = new double[]{-3.12334, -1.23345, 0.4343, 1.3343435, 5.345454};
        for(double num : nums){
            for(Class type : MonetaryAmounts.getAmountTypes()){
                if(type.equals(TestAmount.class)){
                    continue;
                }
                MonetaryAmount mAmount1 = null;
                try{
                    mAmount1 = MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num)
                            .create();
                }
                catch(MonetaryException | ArithmeticException e){
                    // It is possible, that our test may exceed the capabilities, so in that case, we just continue
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                assertEquals("Number value (long, truncated) returned is not correct for " + type.getName(), (long) num,
                             result.intValue());
                try{
                    result.longValueExact();
                    fail("Number value (long, exact -> truncated) must throw ArithemticException on truncation for " +
                                 type.getName());
                }
                catch(ArithmeticException e){
                    // OK
                }
            }
        }
    }

    /**
     * Check if a correct Double value is returned, truncation is
     * allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-A8")
    @Test
    public void testValidDoubleWithTruncation(){
        fail("Not yet implemented");
    }

    /**
     * Check if a correct Number value is returned, truncation is
     * allowed to be performed.
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
        String[] nums =
                new String[]{"1.12", "1.12", "1.123", "1.1234", "1.12345", "1.123456", "1.1234567", "1.12345678",
                        "1.123456789", "12.12", "123.12", "1234.123", "12345.1234", "123456.12345", "123456.123456",
                        "12345678.1234567", "12345678.12345678", "-123456789.123456789", "1", "12", "123", "1234",
                        "12345", "123456", "1234567", "12345678", "123456789"};

        for(String num : nums){
            for(Class type : MonetaryAmounts.getAmountTypes()){
                if(type.equals(TestAmount.class)){
                    continue;
                }
                MonetaryAmount mAmount1 = null;
                BigDecimal bd = new BigDecimal(num);
                try{
                    mAmount1 =
                            MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(bd).create();
                }
                catch(MonetaryException | ArithmeticException e){
                    // It is possible, that our test may exceed the capabilities, so in that case, we just continue
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                assertEquals("Amount's precision does not match for " + bd + " correct for " + type.getName(),
                             bd.precision(), result.getPrecision());
            }
        }
    }

    /**
     * Test correct precision values, including border cases.
     */
    @SpecAssertion(section = "4.2.3", id = "423-A11")
    @Test
    public void testScaleValues(){
        String[] nums =
                new String[]{"1.12", "1.12", "1.123", "1.1234", "1.12345", "1.123456", "1.1234567", "1.12345678",
                        "1.123456789", "12.12", "123.12", "1234.123", "12345.1234", "123456.12345", "123456.123456",
                        "12345678.1234567", "12345678.12345678", "-123456789.123456789", "1", "12", "123", "1234",
                        "12345", "123456", "1234567", "12345678", "123456789"};
        for(String num : nums){
            for(Class type : MonetaryAmounts.getAmountTypes()){
                if(type.equals(TestAmount.class)){
                    continue;
                }
                MonetaryAmount mAmount1 = null;
                BigDecimal bd = new BigDecimal(num);
                try{
                    mAmount1 =
                            MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(bd).create();
                }
                catch(MonetaryException | ArithmeticException e){
                    // It is possible, that our test may exceed the capabilities, so in that case, we just continue
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                assertEquals("Amount's precision does not match for " + bd + " correct for " + type.getName(),
                             bd.scale(), result.getScale());
            }
        }
    }

    // ********************** B. Testing Numeric Externalization for negative values *******************

    /**
     * Checks if number type is not null and returning a concrete (no
     * abstract class or interface).
     */
    @SpecAssertion(section = "4.2.3", id = "423-B1")
    @Test
    public void testNumberTypeNegative(){
        fail("Not yet implemented");
    }

    /**
     * Checks if a correct Integer value is returned, no truncation is
     * allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-B2")
    @Test
    public void testIntegerNegative(){
        fail("Not yet implemented");
    }

    /**
     * Checks if a correct Long value is returned, no truncation is
     * allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-B3")
    @Test
    public void testLongNegative(){
        fail("Not yet implemented");
    }

    /**
     * Checks if a correct Double value is returned, no truncation is
     * allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-B4")
    @Test
    public void testDoubleNegative(){
        double[] nums = new double[]{-3.12334, -1.235, -0.43, -1.35, -52.4, -12345,123, -1223243.342325435};
        for(double num : nums){
            for(Class type : MonetaryAmounts.getAmountTypes()){
                if(type.equals(TestAmount.class)){
                    continue;
                }
                MonetaryAmount mAmount1 = null;
                try{
                    mAmount1 = MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num)
                            .create();
                }
                catch(MonetaryException | ArithmeticException e){
                    // It is possible, that our test may exceed the capabilities, so in that case, we just continue
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                assertEquals("Number value (double, truncated) returned is not correct for " + type.getName(), (int) num,
                             result.doubleValue(), 0.0d);
                try{
                    result.doubleValueExact();
                    fail("Number value (double, exact -> truncated) must throw ArithemticException on truncation for " +
                                 type.getName());
                }
                catch(ArithmeticException e){
                    // OK
                }
            }
        }
    }

    /**
     * Check if a correct number value is returned, no truncation is
     * allowed to be performed.
     * Check should be done for every JDK type
     * supported.
     */
    @SpecAssertion(section = "4.2.3", id = "423-B5")
    @Test
    public void testNumberWithTruncationNegative(){
        fail("Not yet implemented");
    }

    /**
     * Check if a correct integer value is returned, truncation is
     * allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-B6")
    @Test
    public void testIntegerWithTruncationNegative(){
        fail("Not yet implemented");
    }

    /**
     * Check if a correct long value is returned, truncation is
     * allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-B7")
    @Test
    public void testLongWithTruncationNegative(){
        fail("Not yet implemented");
    }

    /**
     * Check if a correct double value is returned, truncation is
     * allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-B8")
    @Test
    public void testDoubleWithTruncationNegative(){
        fail("Not yet implemented");
    }

    /**
     * Check if a correct double value is returned, truncation is
     * allowed to be performed. Check should be done for every JDK type
     * supported.
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
        String[] nums = new String[]{"-1.12", "-1.12", "-1.123", "-1.1234", "-1.12345", "-1.123456", "-1.1234567",
                "-1.12345678", "-1.123456789", "-12.12", "-123.12", "-1234.123", "-12345.1234", "-123456.12345",
                "-123456.123456", "-12345678.1234567", "-12345678.12345678", "-123456789.123456789", "-1", "-12",
                "-123", "-1234", "-12345", "-123456", "-1234567", "-12345678", "-123456789"};
        for(String num : nums){
            for(Class type : MonetaryAmounts.getAmountTypes()){
                if(type.equals(TestAmount.class)){
                    continue;
                }
                MonetaryAmount mAmount1 = null;
                BigDecimal bd = new BigDecimal(num);
                try{
                    mAmount1 =
                            MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(bd).create();
                }
                catch(MonetaryException | ArithmeticException e){
                    // It is possible, that our test may exceed the capabilities, so in that case, we just continue
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                assertEquals("Amount's precision does not match for " + bd + " correct for " + type.getName(),
                             bd.precision(), result.getPrecision());
            }
        }
    }

    /**
     * Test correct scale values, including border cases.
     */
    @SpecAssertion(section = "4.2.3", id = "423-B11")
    @Test
    public void testScaleNegative(){
        String[] nums = new String[]{"-1.12", "-1.12", "-1.123", "-1.1234", "-1.12345", "-1.123456", "-1.1234567",
                "-1.12345678", "-1.123456789", "-12.12", "-123.12", "-1234.123", "-12345.1234", "-123456.12345",
                "-123456.123456", "-12345678.1234567", "-12345678.12345678", "-123456789.123456789", "-1", "-12",
                "-123", "-1234", "-12345", "-123456", "-1234567", "-12345678", "-123456789"};
        for(String num : nums){
            for(Class type : MonetaryAmounts.getAmountTypes()){
                if(type.equals(TestAmount.class)){
                    continue;
                }
                MonetaryAmount mAmount1 = null;
                BigDecimal bd = new BigDecimal(num);
                try{
                    mAmount1 =
                            MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(bd).create();
                }
                catch(MonetaryException | ArithmeticException e){
                    // It is possible, that our test may exceed the capabilities, so in that case, we just continue
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                assertEquals("Amount's scale does not match for " + bd + " correct for " + type.getName(), bd.scale(),
                             result.getScale());
            }
        }
    }

    // ********************* C. Testing Numeric Externalization for zero values **************

    /**
     * Checks if number type is not null and returning a concrete (no
     * abstract class or interface).
     */
    @SpecAssertion(section = "4.2.3", id = "423-C1")
    @Test
    public void testNumberTypeZero(){
        fail("Not yet implemented");
    }

    /**
     * Check if a correct integer value is returned, no truncation is
     * allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-C2")
    @Test
    public void testIntegerZero(){
        Number[] nums = new Number[]{0, 0.0, -0.0, new BigDecimal("0.000000000000000000000000000001"),
                new BigDecimal("-0.000000000000000000000000000001"), new BigInteger("0")};
        for(Number num : nums){
            for(Class type : MonetaryAmounts.getAmountTypes()){
                if(type.equals(TestAmount.class)){
                    continue;
                }
                MonetaryAmount mAmount1 = null;
                try{
                    mAmount1 = MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num)
                            .create();
                }
                catch(MonetaryException | ArithmeticException e){
                    // It is possible, that our test may exceed the capabilities, so in that case, we just continue
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                assertEquals("Number value (double, truncating) returned is not correct for " + num + ", type; " +
                                     type.getName(), 0.0d, result.doubleValue(), 0.0d);
            }
        }
    }

    /**
     * Check if a correct long value is returned, no truncation is
     * allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-C3")
    @Test
    public void testLongZero(){
        fail("Not yet implemented");
    }

    /**
     * Check if a correct double value is returned, no truncation is
     * allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-C4")
    @Test
    public void testDoubleZero(){
        fail("Not yet implemented");
    }

    /**
     * Check if a correct number value is returned, no truncation is
     * allowed to be performed.
     * Check should be done for every JDK type
     * supported.
     */
    @SpecAssertion(section = "4.2.3", id = "423-C5")
    @Test
    public void testNumberValueZero(){
        fail("Not yet implemented");
    }

    /**
     * Check if a correct integer value is returned, truncation is
     * allowed to be performed.
     * Check should be done for every JDK type
     * supported.
     */
    @SpecAssertion(section = "4.2.3", id = "423-C6")
    @Test
    public void testIntegerValueWithTruncationZero(){
        fail("Not yet implemented");
    }

    /**
     * Check if a correct long value is returned, truncation is
     * allowed to be performed.
     * Check should be done for every JDK type
     * supported.
     */
    @SpecAssertion(section = "4.2.3", id = "423-C7")
    @Test
    public void testLongValueWithTruncationZero(){
        fail("Not yet implemented");
    }

    /**
     * Check if a correct double value is returned, truncation is
     * allowed to be performed.
     * Check should be done for every JDK type
     * supported.
     */
    @SpecAssertion(section = "4.2.3", id = "423-C8")
    @Test
    public void testDoubleValueWithTruncationZero(){
        fail("Not yet implemented");
    }

    /**
     * Check if a correct Number value is returned, truncation is
     * allowed to be performed.
     * Check should be done for every JDK type
     * supported.
     */
    @SpecAssertion(section = "4.2.3", id = "423-C9")
    @Test
    public void testNumberValueWithTruncationZero(){
        fail("Not yet implemented");
    }

    /**
     * Check if a correct precision value is returned.
     * Check should be done for every JDK type
     * supported.
     */
    @SpecAssertion(section = "4.2.3", id = "423-C10")
    @Test
    public void testPrecisionZero(){
        String[] nums = new String[]{"-0", "0", "-0.0", "0.0", "-0.00", "0.00", "-0.000", "0.0000", "0.00000", "-0.0000000", "-0.000000000", "-0.00000000000"};
        for(String num : nums){
            for(Class type : MonetaryAmounts.getAmountTypes()){
                if(type.equals(TestAmount.class)){
                    continue;
                }
                MonetaryAmount mAmount1 = null;
                BigDecimal bd = new BigDecimal(num);
                try{
                    mAmount1 =
                            MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(bd).create();
                }
                catch(MonetaryException | ArithmeticException e){
                    // It is possible, that our test may exceed the capabilities, so in that case, we just continue
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                assertEquals("Amount's scale does not match for " + bd + " correct for " + type.getName(), bd.scale(),
                             result.getScale());
            }
        }
    }

    /**
     * Check if a correct precision value is returned.
     * Check should be done for every JDK type
     * supported.
     */
    @SpecAssertion(section = "4.2.3", id = "423-C11")
    @Test
    public void testScaleZero(){
        String[] nums = new String[]{"-0", "-0.0", "-0.00", "-0.000", "-0.0000", "-0.0000", "-0.000000", "-0.00000000"};
        for(String num : nums){
            for(Class type : MonetaryAmounts.getAmountTypes()){
                if(type.equals(TestAmount.class)){
                    continue;
                }
                MonetaryAmount mAmount1 = null;
                BigDecimal bd = new BigDecimal(num);
                try{
                    mAmount1 =
                            MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(bd).create();
                }
                catch(MonetaryException | ArithmeticException e){
                    // It is possible, that our test may exceed the capabilities, so in that case, we just continue
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                assertEquals("Amount's scale does not match for " + bd + " correct for " + type.getName(), bd.scale(),
                             result.getScale());
            }
        }
    }

}
