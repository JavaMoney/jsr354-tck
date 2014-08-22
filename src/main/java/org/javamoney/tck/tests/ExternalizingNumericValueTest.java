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
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import javax.money.MonetaryAmount;
import javax.money.MonetaryAmounts;
import javax.money.MonetaryException;
import javax.money.NumberValue;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Testing Numeric Externalization for numeric values of MonetaryAmount instances.
 * Created by Anatole on 10.03.14.
 */
@SpecVersion(spec = "JSR 354", version = "1.0.0")
public class ExternalizingNumericValueTest {

    private final static String DEFAULT_CURRENCY = "CHF";

    private Class[] requiredJdkTykes =
            new Class[]{Integer.class, Long.class, Double.class, BigDecimal.class, BigInteger.class};


    /**
     * Checks if number type is not null.
     */
    @SpecAssertion(section = "4.2.3", id = "423-A1")
    @Test(description = "4.2.3 Amount types do not return a NumberValue of null.")
    public void testReturningNumberValueIsNotNull() {
        for (Class type : MonetaryAmounts.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmount mAmount1 =
                    MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(10).create();
            NumberValue result = mAmount1.getNumber();
            AssertJUnit.assertNotNull("Section 4.2.3: Amount type does not return a NumberValue (null); " + type.getName(), result);
        }
    }

    /**
     * Check if a correct integer value is returned, no truncation is
     * allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-A2")
    @Test(description = "4.2.3 Ensure NumberValue intValue(), intValueExact() provide correct values.")
    public void testValidInteger() {
        int[] nums = new int[]{-3, -1, 0, 1, 3};
        for (int num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1 =
                        MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num).create();
                NumberValue result = mAmount1.getNumber();
                AssertJUnit.assertNotNull("Section 4.2.3: Amount creation failed for " + type, result);
                AssertJUnit.assertEquals("Section 4.2.3: Number value (int) returned is not correct for " + type.getName(), num,
                        result.intValue());
                AssertJUnit.assertEquals("Section 4.2.3: Exact number value (int) returned is not correct for " + type.getName(), num,
                        result.intValueExact());
            }
        }
    }

    /**
     * Check if a correct long value is returned, no truncation is
     * allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-A3")
    @Test(description = "4.2.3 Ensure NumberValue longValue(), longValueExact() provide correct values.")
    public void testValidLong() {
        long[] nums = new long[]{1, 3, 11, 123, 12345, 1223345566, 1234523462532753243L};
        for (long num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1;
                try {
                    mAmount1 = MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num)
                            .create();
                } catch (ArithmeticException | MonetaryException e) {
                    // could be that the number exceeds the amount's capabilities...
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                AssertJUnit.assertNotNull("Section 4.2.3: Amount creation failed for " + type, result);
                AssertJUnit.assertEquals("Section 4.2.3: Number value (long) returned is not correct for " + type.getName(), num,
                        result.longValue());
                AssertJUnit
                        .assertEquals("Section 4.2.3: Exact number (long) (double) returned is not correct for " + type.getName(), num,
                                result.longValueExact());
            }
        }
    }


    /**
     * Check if a correct double value is returned, no truncation is
     * allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-A4")
    @Test(description = "4.2.3 Ensure NumberValue doubleValue(), doubleValueExact() provide correct values.")
    public void testValidDouble() {
        double[] nums = new double[]{0, 0.3, 1, 1.3453};
        for (double num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1 =
                        MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num).create();
                NumberValue result = mAmount1.getNumber();
                AssertJUnit.assertNotNull("Section 4.2.3: Amount creation failed for " + type, result);
                AssertJUnit.assertEquals("Section 4.2.3: Number value (double) returned is not correct for " + type.getName(), num,
                        result.doubleValue(), 0d);
                AssertJUnit
                        .assertEquals("Section 4.2.3: Exact number value (double) returned is not correct for " + type.getName(), num,
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
    @Test(description = "4.2.3 Ensure NumberValue asType(BigDecimal.class) provides correct values.")
    public void testValidNumberBD() {
        Number[] nums = new Number[]{-3, -3.5f - 1L, -1.2d, (short) 0, 0.3, (byte) 1, 1.3453};
        for (Number num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1 =
                        MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num).create();
                NumberValue result = mAmount1.getNumber();
                AssertJUnit.assertEquals("Section 4.2.3: Number value (BigDecimal) returned is not correct for " + type.getName(),
                        new BigDecimal(String.valueOf(num)).stripTrailingZeros(),
                        result.numberValue(BigDecimal.class).stripTrailingZeros());
                AssertJUnit
                        .assertEquals("Section 4.2.3: Exact number value (BigDecimal) returned is not correct for " + type.getName(),
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
    @Test(description = "4.2.3 Ensure NumberValue asType(BigInteger.class) provides correct values.")
    public void testValidNumberBI() {
        Number[] nums = new Number[]{-3, -1L, (short) 0, (byte) 1};
        for (Number num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1 =
                        MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num).create();
                NumberValue result = mAmount1.getNumber();
                AssertJUnit.assertEquals("Section 4.2.3: Number value (BigInteger) returned is not correct for " + type.getName(),
                        new BigInteger(String.valueOf(num)), result.numberValue(BigInteger.class));
                AssertJUnit
                        .assertEquals("Section 4.2.3: Exact number value (BigInteger) returned is not correct for " + type.getName(),
                                new BigInteger(String.valueOf(num)), result.numberValue(BigInteger.class));
            }
        }
    }

    /**
     * Check if a correct Integer value is returned, truncation is
     * allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-A6")
    @Test(description = "4.2.3 Ensure NumberValue intValue() is truncated.")
    public void testValidIntegerWithTruncation() {
        double[] nums = new double[]{-3.12334, -1.23345, 0.4343, 1.3343435, 5.345454};
        for (double num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1;
                try {
                    mAmount1 = MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num)
                            .create();
                } catch (MonetaryException | ArithmeticException e) {
                    // It is possible, that our test may exceed the capabilities, so in that case, we just continue
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                AssertJUnit.assertEquals("Section 4.2.3: Number value (int, truncated) returned is not correct for " + type.getName(),
                        (int) num, result.intValue());
                try {
                    result.intValueExact();
                    AssertJUnit
                            .fail("Section 4.2.3: Number value (int, exact -> truncated) must throw ArithemticException on truncation for " +
                                    type.getName());
                } catch (ArithmeticException e) {
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
    @Test(description = "4.2.3 Ensure NumberValue longValue() is truncated.")
    public void testValidLongWithTruncation() {
        double[] nums = new double[]{0.4343, 1.3343435, 5.345454};
        for (double num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1;
                try {
                    mAmount1 = MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num)
                            .create();
                } catch (MonetaryException | ArithmeticException e) {
                    // It is possible, that our test may exceed the capabilities, so in that case, we just continue
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                AssertJUnit.assertEquals("Section 4.2.3: Number value (long, truncated) returned is not correct for " + type.getName(),
                        (long) num, result.intValue());
                try {
                    result.longValueExact();
                    AssertJUnit
                            .fail("Section 4.2.3: Number value (long, exact -> truncated) must throw ArithemticException on truncation for " +
                                    type.getName());
                } catch (ArithmeticException e) {
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
    @Test(description = "4.2.3 Ensure NumberValue doubleValue() is truncated.")
    public void testValidDoubleWithTruncation() {
        Number[] nums = new Number[]{new BigDecimal("26353527352735725372357.287362873287362836283"), 3232232334423L,
                33434243242342342434.5d, 1L, 1.24355354543534545d, (short) 0, 0.3, (byte) 1, 1.3453, 32432532};
        for (Number num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1;
                try {
                    mAmount1 = MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num)
                            .create();
                } catch (ArithmeticException | MonetaryException e) {
                    // can happen if capabilities are exceeded
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                AssertJUnit
                        .assertEquals("Section 4.2.3: Number value (Double, truncating) returned is not correct for " + type.getName(),
                                new BigDecimal(String.valueOf(num)).doubleValue(), result.doubleValue(), 0.0d);
            }
        }
    }

    /**
     * Check if a correct Number value is returned, truncation is
     * allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-A9")
    @Test(description = "4.2.3 Ensure NumberValue byteValue() is truncated.")
    public void testValidNumberWithTruncation_Byte() {
        Number[] nums = new Number[]{-3232423, -3.5f - 1L, -1.2d, (short) 0, 0.3, (byte) 1, 1.3453, 32432532};
        for (Number num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1;
                try {
                    mAmount1 = MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num)
                            .create();
                } catch (ArithmeticException | MonetaryException e) {
                    // can happen if capabilities are exceeded
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                AssertJUnit
                        .assertEquals("Section 4.2.3: Number value (Byte, truncating) returned is not correct for " + type.getName(),
                                new BigDecimal(String.valueOf(num)).byteValue(),
                                result.numberValue(Byte.class).byteValue());
            }
        }
    }

    /**
     * Check if a correct Number value is returned, truncation is
     * allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-A9")
    @Test(description = "4.2.3 Ensure NumberValue shortValue() is truncated.")
    public void testValidNumberWithTruncation_Short() {
        Number[] nums = new Number[]{-3232423, -3.5f - 1L, -1.2d, (short) 0, 0.3, (byte) 1, 1.3453, 32432532};
        for (Number num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1;
                try {
                    mAmount1 = MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num)
                            .create();
                } catch (ArithmeticException | MonetaryException e) {
                    // can happen if capabilities are exceeded
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                AssertJUnit
                        .assertEquals("Section 4.2.3: Number value (Short, truncating) returned is not correct for " + type.getName(),
                                new BigDecimal(String.valueOf(num)).shortValue(),
                                result.numberValue(Short.class).shortValue());
            }
        }
    }

    /**
     * Check if a correct Number value is returned, truncation is
     * allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-A9")
    @Test(description = "4.2.3 Ensure NumberValue floatValue() is truncated.")
    public void testValidNumberWithTruncation_Float() {
        Number[] nums =
                new Number[]{-3232232334423L, -33434243242342342434.5d - 1L, -1.24355354543534545d, (short) 0, 0.3,
                        (byte) 1, 1.3453, 32432532};
        for (Number num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1;
                try {
                    mAmount1 = MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num)
                            .create();
                } catch (ArithmeticException | MonetaryException e) {
                    // can happen if capabilities are exceeded
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                AssertJUnit
                        .assertEquals("Section 4.2.3: Number value (Float, truncating) returned is not correct for " + type.getName(),
                                new BigDecimal(String.valueOf(num)).floatValue(),
                                result.numberValue(Float.class).floatValue(), 0.0f);
            }
        }
    }

    /**
     * Check if a correct Number value is returned, truncation is
     * allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-A9")
    @Test(description = "4.2.3 Ensure NumberValue doubleValue() is truncated.")
    public void testValidNumberWithTruncation_Double() {
        Number[] nums = new Number[]{new BigDecimal("26353527352735725372357.287362873287362836283"),
                new BigDecimal("-26353527352735725372357.287362873287362836283"), -3232232334423L,
                -33434243242342342434.5d - 1L, -1.24355354543534545d, (short) 0, 0.3, (byte) 1, 1.3453, 32432532};
        for (Number num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1;
                try {
                    mAmount1 = MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num)
                            .create();
                } catch (ArithmeticException | MonetaryException e) {
                    // can happen if capabilities are exceeded
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                AssertJUnit
                        .assertEquals("Section 4.2.3: Number value (Double, truncating) returned is not correct for " + type.getName(),
                                new BigDecimal(String.valueOf(num)).doubleValue(),
                                result.numberValue(Double.class), 0.0d);
            }
        }
    }


    /**
     * Check if a correct Number value is returned, truncation is
     * allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-A9")
    @Test(description = "4.2.3 Ensure NumberValue intValue() is truncated correctly.")
    public void testValidNumberWithTruncation_Integer() {
        Number[] nums = new Number[]{-3232423, -3.5f - 1L, -1.2d, (short) 0, 0.3, (byte) 1, 1.3453, 32432532};
        for (Number num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1;
                try {
                    mAmount1 = MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num)
                            .create();
                } catch (ArithmeticException | MonetaryException e) {
                    // can happen if capabilities are exceeded
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                AssertJUnit
                        .assertEquals("Section 4.2.3: Number value (short, truncating) returned is not correct for " + type.getName(),
                                new BigDecimal(String.valueOf(num)).intValue(),
                                result.numberValue(Integer.class).intValue());
            }
        }
    }

    /**
     * Test correct precision values, including border cases.
     */
    @SpecAssertion(section = "4.2.3", id = "423-A10")
    @Test(description = "4.2.3 Ensure NumberValue getPrecision() works correctly.")
    public void testPrecisionValues() {
        String[] nums =
                new String[]{"1.12", "1.12", "1.123", "1.1234", "1.12345", "1.123456", "1.1234567", "1.12345678",
                        "1.123456789", "12.12", "123.12", "1234.123", "12345.1234", "123456.12345", "123456.123456",
                        "12345678.1234567", "12345678.12345678", "-123456789.123456789", "1", "12", "123", "1234",
                        "12345", "123456", "1234567", "12345678", "123456789"};

        for (String num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1;
                BigDecimal bd = new BigDecimal(num);
                try {
                    mAmount1 =
                            MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(bd).create();
                } catch (MonetaryException | ArithmeticException e) {
                    // It is possible, that our test may exceed the capabilities, so in that case, we just continue
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                AssertJUnit
                        .assertEquals("Section 4.2.3: Amount's precision does not match for " + bd + " correct for " + type.getName(),
                                bd.precision(), result.getPrecision());
            }
        }
    }

    /**
     * Test correct precision values, including border cases.
     */
    @SpecAssertion(section = "4.2.3", id = "423-A11")
    @Test(description = "4.2.3 Ensure NumberValue getScale() works correctly.")
    public void testScaleValues() {
        String[] nums =
                new String[]{"1.12", "1.12", "1.123", "1.1234", "1.12345", "1.123456", "1.1234567", "1.12345678",
                        "1.123456789", "12.12", "123.12", "1234.123", "12345.1234", "123456.12345", "123456.123456",
                        "12345678.1234567", "12345678.12345678", "-123456789.123456789", "1", "12", "123", "1234",
                        "12345", "123456", "1234567", "12345678", "123456789"};
        for (String num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1;
                BigDecimal bd = new BigDecimal(num);
                try {
                    mAmount1 =
                            MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(bd).create();
                } catch (MonetaryException | ArithmeticException e) {
                    // It is possible, that our test may exceed the capabilities, so in that case, we just continue
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                AssertJUnit
                        .assertEquals("Section 4.2.3: Amount's precision does not match for " + bd + " correct for " + type.getName(),
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
    @Test(description = "4.2.3 Ensure NumberValue numberValue() works correnctly.")
    public void testNumberTypeNegative() {
        Number[] nums = new Number[]{-1213243544435L, -3, -3.5f - 1L, -1.2d, -21323234324324.23};
        for (Number num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1 =
                        MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num).create();
                NumberValue result = mAmount1.getNumber();
                AssertJUnit.assertEquals("Section 4.2.3: Number value (BigDecimal) returned is not correct for " + type.getName(),
                        new BigDecimal(String.valueOf(num)).stripTrailingZeros(),
                        result.numberValue(BigDecimal.class).stripTrailingZeros());
                AssertJUnit
                        .assertEquals("Section 4.2.3: Exact number value (BigDecimal) returned is not correct for " + type.getName(),
                                new BigDecimal(String.valueOf(num)).stripTrailingZeros(),
                                result.numberValue(BigDecimal.class).stripTrailingZeros());
            }
        }
    }

    /**
     * Checks if a correct negative Integer value is returned, no truncation is
     * allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-B2")
    @Test(description = "4.2.3 Checks if a correct Integer value is returned, no truncation is" +
            " allowed to be performed.")
    public void testIntegerNegative() {
        int[] nums = new int[]{-1, -3, -11, -123, -12345, -1223345566};
        for (long num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1 =
                        MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num).create();
                NumberValue result = mAmount1.getNumber();
                AssertJUnit.assertNotNull("Section 4.2.3: Amount creation failed for " + type, result);
                AssertJUnit.assertEquals("Section 4.2.3: Number value (int) returned is not correct for " + type.getName(), num,
                        result.intValue());
                AssertJUnit.assertEquals("Section 4.2.3: Exact number (int) returned is not correct for " + type.getName(), num,
                        result.intValueExact());
            }
        }
    }

    /**
     * Checks if a correct Long value is returned, no truncation is
     * allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-B3")
    @Test(description = "4.2.3 Checks if a correct negative long value is returned, no truncation is" +
            " allowed to be performed.")
    public void testLongNegative() {
        long[] nums = new long[]{-1, -3, -11, -123, -12345, -1223345566, -1234523462532753243L};
        for (long num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1;
                try {
                    mAmount1 = MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num)
                            .create();
                } catch (ArithmeticException | MonetaryException e) {
                    // can happen, if number exceeds capabilities.
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                AssertJUnit.assertNotNull("Section 4.2.3: Amount creation failed for " + type, result);
                AssertJUnit.assertEquals("Section 4.2.3: Number value (long) returned is not correct for " + type.getName(), num,
                        result.longValue());
                AssertJUnit.assertEquals("Section 4.2.3: Exact number (long) returned is not correct for " + type.getName(), num,
                        result.longValueExact());
            }
        }
    }

    /**
     * Checks if a correct Double value is returned, no truncation is
     * allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-B4")
    @Test(description = "Checks if a correct Double value is returned, no truncation is " +
            "allowed to be performed.")
    public void testDoubleNegative() {
        double[] nums = new double[]{-3.12334, -1.235, -0.43, -1.35, -52.4, -12345, 123, -1223243.342325435};
        for (double num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1;
                try {
                    mAmount1 = MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num)
                            .create();
                } catch (MonetaryException | ArithmeticException e) {
                    // It is possible, that our test may exceed the capabilities, so in that case, we just continue
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                AssertJUnit
                        .assertEquals("Section 4.2.3: Number value (double, truncated) returned is not correct for " + type.getName(),
                                num, result.doubleValue(), 0.0d);
            }
        }
    }

    /**
     * Check if a correct number value is returned, truncation is
     * allowed to be performed.
     * Check should be done for every JDK type
     * supported.
     */
    @SpecAssertion(section = "4.2.3", id = "423-B5")
    @Test(description = "4.2.3 Check if a correct number value is returned, truncation is " +
            " allowed to be performed. Check should be done for every JDK type supported.")
    public void testNumberWithTruncationNegative() {
        double[] nums = new double[]{-1, -1.1, -1111111111111111111111111111111111111111.11111111111111111111111d};
        for (double num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1;
                BigDecimal dec = new BigDecimal(String.valueOf(num));
                try {
                    mAmount1 = MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num)
                            .create();
                } catch (ArithmeticException | MonetaryException e) {
                    // can hhappen if number exceeds capabilities
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                for (Class numType : requiredJdkTykes) {
                    if (Byte.class.equals(numType)) {
                        AssertJUnit.assertEquals("Section 4.2.3: Truncating conversion to byte failed for type " + type.getName(),
                                dec.byteValue(), result.byteValue());
                    } else if (Short.class.equals(numType)) {
                        AssertJUnit.assertEquals("Section 4.2.3: Truncating conversion to short failed for type " + type.getName(),
                                dec.shortValue(), result.shortValue());
                    } else if (Integer.class.equals(numType)) {
                        AssertJUnit.assertEquals("Section 4.2.3: Truncating conversion to int failed for type " + type.getName(),
                                dec.intValue(), result.intValue());
                    } else if (Long.class.equals(numType)) {
                        AssertJUnit.assertEquals("Section 4.2.3: Truncating conversion to long failed for type " + type.getName(),
                                dec.longValue(), result.longValue());
                    } else if (Float.class.equals(numType)) {
                        AssertJUnit.assertEquals("Section 4.2.3: Truncating conversion to float failed for type " + type.getName(),
                                dec.floatValue(), result.floatValue(), 0.0f);
                    } else if (Double.class.equals(numType)) {
                        AssertJUnit.assertEquals("Section 4.2.3: Truncating conversion to double failed for type " + type.getName(),
                                dec.doubleValue(), result.doubleValue(), 0.0d);
                    } else if (BigDecimal.class.equals(numType)) {
                        AssertJUnit
                                .assertEquals("Section 4.2.3: Truncating conversion to BigDecimal failed for type " + type.getName(),
                                        dec.stripTrailingZeros(),
                                        result.numberValue(BigDecimal.class).stripTrailingZeros());
                    } else if (BigInteger.class.equals(numType)) {
                        AssertJUnit
                                .assertEquals("Section 4.2.3: Truncating conversion to BigInteger failed for type " + type.getName(),
                                        dec.toBigInteger(), result.numberValue(BigInteger.class));
                    }
                }
            }
        }
    }

    /**
     * Check if a correct integer value is returned, truncation is
     * allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-B6")
    @Test(description = "4.2.3 Check if a correct integer value is returned, truncation is" +
            " allowed to be performed..")
    public void testIntegerWithTruncationNegative() {
        double[] nums = new double[]{-1.1, -3.12, -11.123, -123.1234, -12345.12233, -1223345566.2332432};
        for (double num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1;
                try {
                    mAmount1 = MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num)
                            .create();
                } catch (ArithmeticException | MonetaryException e) {
                    // can hhappen if number exceeds capabilities
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                AssertJUnit.assertEquals("Section 4.2.3: Number value (int) returned is not correct for " + type.getName(), (int) num,
                        result.intValue());
            }
        }
    }

    /**
     * Check if a correct long value is returned, truncation is
     * allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-B7")
    @Test(description = "4.2.3 Checks if a correct long value is returned, truncation is" +
            " allowed to be performed.")
    public void testLongWithTruncationNegative() {
        double[] nums = new double[]{-3.12334, -1.23345, -1223234.23};
        for (double num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1;
                try {
                    mAmount1 = MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num)
                            .create();
                } catch (MonetaryException | ArithmeticException e) {
                    // It is possible, that our test may exceed the capabilities, so in that case, we just continue
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                AssertJUnit.assertEquals("Section 4.2.3: Number value (long, truncated) returned is not correct for " + type.getName(),
                        (long) num, result.intValue());
                try {
                    result.longValueExact();
                    AssertJUnit
                            .fail("Section 4.2.3: Number value (long, exact -> truncated) must throw ArithemticException on truncation for " +
                                    type.getName());
                } catch (ArithmeticException e) {
                    // OK
                }
            }
        }
    }

    /**
     * Check if a correct double value is returned, truncation is
     * allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-B8")
    @Test(description = "4.2.3 Checks if a correct double value is returned, truncation is" +
            " allowed to be performed.")
    public void testDoubleWithTruncationNegative() {
        Number[] nums = new Number[]{new BigDecimal("-26353527352735725372357.287362873287362836283"), -3232232334423L,
                -33434243242342342434.5d, -1L, -1.24355354543534545d, (short) -0, -0.3, (byte) -1, -1.3453, 32432532};
        for (Number num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1;
                try {
                    mAmount1 = MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num)
                            .create();
                } catch (ArithmeticException | MonetaryException e) {
                    // can happen if capabilities are exceeded
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                AssertJUnit
                        .assertEquals("Section 4.2.3: Number value (Double, truncating) returned is not correct for " + type.getName(),
                                new BigDecimal(String.valueOf(num)).doubleValue(), result.doubleValue(), 0.0d);
            }
        }
    }

    /**
     * Check if a correct double value is returned, truncation is
     * allowed to be performed. Check should be done for every JDK type
     * supported.
     */
    @SpecAssertion(section = "4.2.3", id = "423-B9")
    @Test(description = "4.2.3 Checks if a correct long value is returned, truncation is" +
            " allowed to be performed. Check should be done for every JDK type.")
    public void testNumberValueWithTruncationNegative() {
        Number[] nums = new Number[]{-1213243544435L, -3234, -3.5f - 1.1, -1.2d, -21323234324324.23};
        for (Number num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1 =
                        MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num).create();
                NumberValue result = mAmount1.getNumber();
                AssertJUnit
                        .assertEquals("Section 4.2.3: Number value (BigDecimal -> byte) returned is not correct for " + type.getName(),
                                (long) new BigDecimal(String.valueOf(num)).byteValue(),
                                (long) result.numberValue(Byte.class));
            }
        }
    }

    /**
     * Check if a correct double value is returned, truncation is
     * allowed to be performed. Check should be done for every JDK type
     * supported.
     */
    @SpecAssertion(section = "4.2.3", id = "423-B9")
    @Test(description = "4.2.3 Checks if a correct double value is returned, truncation is" +
            " allowed to be performed. Check should be done for every JDK type.")
    public void testNumberValueWithTruncationNegative_Short() {
        Number[] nums = new Number[]{-1213243544435L, -3234, -3.5f - 1.1, -1.2d, -21323234324324.23};
        for (Number num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1 =
                        MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num).create();
                NumberValue result = mAmount1.getNumber();
                AssertJUnit
                        .assertEquals("Section 4.2.3: Number value (BigDecimal -> byte) returned is not correct for " + type.getName(),
                                (long) new BigDecimal(String.valueOf(num)).shortValue(),
                                (long) result.numberValue(Short.class));
                AssertJUnit
                        .assertEquals("Section 4.2.3: Number value (BigDecimal -> byte) returned is not correct for " + type.getName(),
                                (long) new BigDecimal(String.valueOf(num)).shortValue(),
                                (long) result.shortValue());
            }
        }
    }

    /**
     * Check if a correct double value is returned, truncation is
     * allowed to be performed. Check should be done for every JDK type
     * supported.
     */
    @SpecAssertion(section = "4.2.3", id = "423-B9")
    @Test(description = "4.2.3 Checks if a correct int value is returned, truncation is" +
            " allowed to be performed. Check should be done for every JDK type.")
    public void testNumberValueWithTruncationNegative_Integer() {
        Number[] nums = new Number[]{-1213243544435L, -3234, -3.5f - 1.1, -1.2d, -21323234324324.23};
        for (Number num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1 =
                        MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num).create();
                NumberValue result = mAmount1.getNumber();
                AssertJUnit
                        .assertEquals("Section 4.2.3: Number value (BigDecimal -> byte) returned is not correct for " + type.getName(),
                                (long) new BigDecimal(String.valueOf(num)).intValue(),
                                (long) result.numberValue(Integer.class));
                AssertJUnit
                        .assertEquals("Section 4.2.3: Number value (BigDecimal -> byte) returned is not correct for " + type.getName(),
                                (long) new BigDecimal(String.valueOf(num)).intValue(), (long) result.intValue());
            }
        }
    }

    /**
     * Check if a correct number value is returned, truncation is
     * allowed to be performed. Check should be done for every JDK type
     * supported.
     */
    @SpecAssertion(section = "4.2.3", id = "423-B9")
    @Test(description = "4.2.3 Checks if a correct Number value is returned, truncation is" +
            " allowed to be performed. Check should be done for every JDK type.")
    public void testNumberValueWithTruncationNegative_Long() {
        Number[] nums = new Number[]{-1213243544435L, -3234, -3.5f - 1.1, -1.2d, -21323234324324.23};
        for (Number num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1 =
                        MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num).create();
                NumberValue result = mAmount1.getNumber();
                AssertJUnit
                        .assertEquals("Section 4.2.3: Number value (BigDecimal -> byte) returned is not correct for " + type.getName(),

                                new BigDecimal(String.valueOf(num)).longValue(),
                                (long) result.numberValue(Long.class));
                AssertJUnit
                        .assertEquals("Section 4.2.3: Number value (BigDecimal -> byte) returned is not correct for " + type.getName(),

                                new BigDecimal(String.valueOf(num)).longValue(), result.longValue());
            }
        }
    }

    /**
     * Check if a correct double value is returned, truncation is
     * allowed to be performed. Check should be done for every JDK type
     * supported.
     */
    @SpecAssertion(section = "4.2.3", id = "423-B9")
    @Test(description = "4.2.3 Checks if a correct double value is returned, truncation is" +
            " allowed to be performed. Check should be done for every JDK type.")
    public void testNumberValueWithTruncationNegative_Float() {
        Number[] nums = new Number[]{-1213243544435L, -3234, -3.5f - 1.1, -1.2d, -21323234324324.23};
        for (Number num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1 =
                        MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num).create();
                NumberValue result = mAmount1.getNumber();
                AssertJUnit.assertEquals(
                        "Section 4.2.3: Number value (BigDecimal -> float) returned is not correct for " + type.getName(),
                        new BigDecimal(String.valueOf(num)).floatValue(), result.numberValue(Float.class),
                        0.0d);
                AssertJUnit.assertEquals(
                        "Section 4.2.3: Number value (BigDecimal -> float) returned is not correct for " + type.getName(),
                        new BigDecimal(String.valueOf(num)).floatValue(), result.floatValue(), 0.0d);
            }
        }
    }

    /**
     * Check if a correct double value is returned, truncation is
     * allowed to be performed. Check should be done for every JDK type
     * supported.
     */
    @SpecAssertion(section = "4.2.3", id = "423-B9")
    @Test(description = "4.2.3 Checks if a correct double value is returned, truncation is" +
            " allowed to be performed. Check should be done for every JDK type.")
    public void testNumberValueWithTruncationNegative_Double() {
        Number[] nums = new Number[]{-1213243544435L, -3234, -3.5f - 1.1, -1.2d, -21323234324324.23};
        for (Number num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1 =
                        MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num).create();
                NumberValue result = mAmount1.getNumber();
                AssertJUnit.assertEquals(
                        "Section 4.2.3: Number value (BigDecimal -> double) returned is not correct for " + type.getName(),
                        new BigDecimal(String.valueOf(num)).doubleValue(), result.numberValue(Double.class), 0.0d);
                AssertJUnit.assertEquals(
                        "Section 4.2.3: Number value (BigDecimal -> double) returned is not correct for " + type.getName(),
                        new BigDecimal(String.valueOf(num)).doubleValue(), result.doubleValue(), 0.0d);
            }
        }
    }

    /**
     * Test correct precision values, including border cases.
     */
    @SpecAssertion(section = "4.2.3", id = "423-B10")
    @Test(description = "4.2.3 Test correct precision values, including border cases.")
    public void testPrecisionNegative() {
        String[] nums = new String[]{"-1.12", "-1.12", "-1.123", "-1.1234", "-1.12345", "-1.123456", "-1.1234567",
                "-1.12345678", "-1.123456789", "-12.12", "-123.12", "-1234.123", "-12345.1234", "-123456.12345",
                "-123456.123456", "-12345678.1234567", "-12345678.12345678", "-123456789.123456789", "-1", "-12",
                "-123", "-1234", "-12345", "-123456", "-1234567", "-12345678", "-123456789"};
        for (String num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1;
                BigDecimal bd = new BigDecimal(num);
                try {
                    mAmount1 =
                            MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(bd).create();
                } catch (MonetaryException | ArithmeticException e) {
                    // It is possible, that our test may exceed the capabilities, so in that case, we just continue
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                AssertJUnit
                        .assertEquals("Section 4.2.3: Amount's precision does not match for " + bd + " correct for " + type.getName(),
                                bd.precision(), result.getPrecision());
            }
        }
    }

    /**
     * Test correct scale values, including border cases.
     */
    @SpecAssertion(section = "4.2.3", id = "423-B11")
    @Test(description = "4.2.3 Test correct scale values, including border cases.")
    public void testScaleNegative() {
        String[] nums = new String[]{"-1.12", "-1.12", "-1.123", "-1.1234", "-1.12345", "-1.123456", "-1.1234567",
                "-1.12345678", "-1.123456789", "-12.12", "-123.12", "-1234.123", "-12345.1234", "-123456.12345",
                "-123456.123456", "-12345678.1234567", "-12345678.12345678", "-123456789.123456789", "-1", "-12",
                "-123", "-1234", "-12345", "-123456", "-1234567", "-12345678", "-123456789"};
        for (String num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1;
                BigDecimal bd = new BigDecimal(num);
                try {
                    mAmount1 =
                            MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(bd).create();
                } catch (MonetaryException | ArithmeticException e) {
                    // It is possible, that our test may exceed the capabilities, so in that case, we just continue
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                AssertJUnit.assertEquals("Section 4.2.3: Amount's scale does not match for " + bd + " correct for " + type.getName(),
                        bd.scale(), result.getScale());
            }
        }
    }

    // ********************* C. Testing Numeric Externalization for zero values **************

    /**
     * Checks if number type is not null and returning a concrete (no
     * abstract class or interface).
     */
    @SpecAssertion(section = "4.2.3", id = "423-C1")
    @Test(description = "4.2.3 Checks if number type is not null and returning a concrete (no" +
            " abstract class or interface).")
    public void testNumberTypeZero() {
        Number[] nums =
                new Number[]{new BigDecimal("-0.0"), new BigDecimal("0"), new BigInteger("0"), 0, 0L, (byte) 0, 0.0f,
                        0.0d};
        for (Number num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1;
                try {
                    mAmount1 = MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num)
                            .create();
                } catch (ArithmeticException | MonetaryException e) {
                    // can happen if capabilities are exceeded
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                AssertJUnit.assertEquals("Section 4.2.3: Number value (byte) returned is not correct for " + type.getName(),
                        new BigDecimal(String.valueOf(num)).byteValue(), (byte) 0,
                        result.numberValue(Byte.class).byteValue());
                AssertJUnit.assertEquals("Section 4.2.3: Number value (short) returned is not correct for " + type.getName(),
                        new BigDecimal(String.valueOf(num)).shortValue(), (short) 0,
                        result.numberValue(Short.class).shortValue());
                AssertJUnit.assertEquals("Section 4.2.3: Number value (int) returned is not correct for " + type.getName(),
                        new BigDecimal(String.valueOf(num)).intValue(), 0,
                        result.numberValue(Integer.class).intValue());
                AssertJUnit.assertEquals("Section 4.2.3: Number value (long) returned is not correct for " + type.getName(),
                        new BigDecimal(String.valueOf(num)).longValue(), (long) 0,
                        result.numberValue(Long.class).longValue());
                AssertJUnit.assertEquals("Section 4.2.3: Number value (float) returned is not correct for " + type.getName(),
                        new BigDecimal(String.valueOf(num)).floatValue(), 0.0f,
                        result.numberValue(Float.class).floatValue());
                AssertJUnit.assertEquals("Section 4.2.3: Number value (double) returned is not correct for " + type.getName(),
                        new BigDecimal(String.valueOf(num)).doubleValue(), 0.0f,
                        result.numberValue(Double.class));
            }
        }
    }

    /**
     * Check if a correct integer value is returned, no truncation is
     * allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-C2")
    @Test(description = "4.2.3 Check if a correct integer value is returned, no truncation is " +
            " allowed to be performed.")
    public void testIntegerZero() {
        Number[] nums = new Number[]{0, 0.0, -0.0, new BigDecimal("0.000000000000000000000000000001"),
                new BigDecimal("-0.000000000000000000000000000001"), new BigInteger("0")};
        for (Number num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1;
                try {
                    mAmount1 = MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num)
                            .create();
                } catch (MonetaryException | ArithmeticException e) {
                    // It is possible, that our test may exceed the capabilities, so in that case, we just continue
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                AssertJUnit
                        .assertEquals("Section 4.2.3: Number value (int, truncating) returned is not correct for " + num + ", type; " +
                                type.getName(), 0, result.intValue());
            }
        }
    }

    /**
     * Check if a correct long value is returned, no truncation is
     * allowed to be performed.
     */
    @SpecAssertion(section = "4.2.3", id = "423-C3")
    @Test(description = "4.2.3 Check if a correct long zero value is returned, no truncation is " +
            " allowed to be performed.")
    public void testLongZero() {
        Number[] nums = new Number[]{0, 0.0, -0.0, new BigDecimal("0.00000000000000000000000000000"),
                new BigDecimal("-0.00000000000000000000000000000"), new BigInteger("0")};
        for (Number num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1;
                try {
                    mAmount1 = MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num)
                            .create();
                } catch (MonetaryException | ArithmeticException e) {
                    // It is possible, that our test may exceed the capabilities, so in that case, we just continue
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                AssertJUnit.assertEquals(
                        "Section 4.2.3: Number value (long, truncating) returned is not correct for " + num + ", type; " +
                                type.getName(), 0L, result.longValue());
                AssertJUnit.assertEquals("Section 4.2.3: Number value (long, exact) returned is not correct for " + num + ", type; " +
                        type.getName(), 0L, result.longValue());
            }
        }
    }


    /**
     * Check if a correct number value is returned, no truncation is
     * allowed to be performed.
     * Check should be done for every JDK type
     * supported.
     */
    @SpecAssertion(section = "4.2.3", id = "423-C5")
    @Test(description = "4.2.3 Check if a correct long zero value is returned, no truncation is " +
            " allowed to be performed.")
    public void testNumberValueZero() {
        Number[] nums = new Number[]{0.0, -0.0, new BigDecimal("0.00000"), new BigDecimal("-0.000000000000000000000"),
                new BigInteger("0")};
        for (Number num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1;
                try {
                    mAmount1 = MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num)
                            .create();
                } catch (MonetaryException | ArithmeticException e) {
                    // It is possible, that our test may exceed the capabilities, so in that case, we just continue
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                AssertJUnit.assertEquals("Section 4.2.3: Number value (Number, long) returned is not correct for " + num + ", type; " +
                                type.getName(), 0L,
                        result.numberValue(BigDecimal.class).longValueExact());
                AssertJUnit
                        .assertEquals("Section 4.2.3: Number value (Number, short) returned is not correct for " + num + ", type; " +
                                type.getName(), 0.0f, result.numberValue(Short.class).floatValue(), 0.0f);
                AssertJUnit.assertEquals("Section 4.2.3: Number value (Number, int) returned is not correct for " + num + ", type; " +
                        type.getName(), 0, result.numberValue(Short.class).intValue());
                AssertJUnit
                        .assertEquals("Section 4.2.3: Number value (Number, double) returned is not correct for " + num + ", type; " +
                                        type.getName(), 0.0d, result.numberValue(Double.class),
                                0.0f);
                AssertJUnit.assertEquals(
                        "Section 4.2.3: Number value (Number, BigInteger) returned is not correct for " + num + ", type; " +
                                type.getName(), 0L, result.numberValue(BigInteger.class).longValueExact());
                AssertJUnit.assertEquals(
                        "Section 4.2.3: Number value (Number, BigDecimal) returned is not correct for " + num + ", type; " +
                                type.getName(), 0L, result.numberValue(BigDecimal.class).longValueExact());
                result.numberValueExact(BigDecimal.class);
            }
        }
    }

    /**
     * Check if a correct integer value is returned, truncation is
     * allowed to be performed.
     * Check should be done for every JDK type
     * supported.
     */
    @SpecAssertion(section = "4.2.3", id = "423-C6")
    @Test(description = "4.2.3 Check if a correct integer value is returned, truncation is " +
            "allowed to be performed. " +
            "Check should be done for every JDK type " +
            "supported.")
    public void testIntegerValueWithTruncationZero() {
        Number[] nums =
                new Number[]{0.01, -0.02, new BigDecimal("0.000001"), new BigDecimal("-0.0000000000000000000001")};
        for (Number num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1;
                try {
                    mAmount1 = MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num)
                            .create();
                } catch (MonetaryException | ArithmeticException e) {
                    // It is possible, that our test may exceed the capabilities, so in that case, we just continue
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                AssertJUnit
                        .assertEquals("Section 4.2.3: Number value (int, truncating) returned is not correct for " + num + ", type; " +
                                type.getName(), 0L, result.intValue());
                try {
                    result.intValueExact();
                    AssertJUnit
                            .fail("Section 4.2.3: Number value (int, exact) should throw ArithmeticException for " + num + ", type; " +
                                    type.getName());
                } catch (ArithmeticException e) {
                    // OK, as expected!
                }
            }
        }
    }

    /**
     * Check if a correct long value is returned, truncation is
     * allowed to be performed.
     * Check should be done for every JDK type
     * supported.
     */
    @SpecAssertion(section = "4.2.3", id = "423-C7")
    @Test(description = "4.2.3 Check if a correct long value is returned, truncation is " +
            "allowed to be performed. " +
            "Check should be done for every JDK type " +
            "supported.")
    public void testLongValueWithTruncationZero() {
        Number[] nums =
                new Number[]{0.01, -0.02, new BigDecimal("0.000001"), new BigDecimal("-0.0000000000000000000001")};
        for (Number num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1;
                try {
                    mAmount1 = MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num)
                            .create();
                } catch (MonetaryException | ArithmeticException e) {
                    // It is possible, that our test may exceed the capabilities, so in that case, we just continue
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                AssertJUnit.assertEquals(
                        "Section 4.2.3: Number value (long, truncating) returned is not correct for " + num + ", type; " +
                                type.getName(), 0L, result.longValue());
                try {
                    result.longValueExact();
                    AssertJUnit.fail("Section 4.2.3: Number value (long, exact) should throw ArithmeticException for " + num +
                            ", type; " +
                            type.getName());
                } catch (ArithmeticException e) {
                    // OK, as expected!
                }
            }
        }
    }

    /**
     * Check if a correct double value is returned, truncation is
     * allowed to be performed (but is not necessary).
     */
    @SpecAssertion(section = "4.2.3", id = "423-C8")
    @Test(description = "4.2.3 Check if a correct double value is returned, truncation is " +
            "allowed to be performed (but is not necessary).")
    public void testDoubleValueWithTruncationZero() {
        Number[] nums = new Number[]{new BigDecimal("-0.000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000001234")};
        for (Number num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1;
                try {
                    mAmount1 = MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num)
                            .create();
                } catch (ArithmeticException | MonetaryException e) {
                    // can happen if capabilities are exceeded
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                AssertJUnit
                        .assertEquals("Section 4.2.3: Number value (Double, truncating) returned is not correct for " + type.getName(),
                                new BigDecimal(String.valueOf(num)).doubleValue(), result.doubleValue(), 0.0d);
            }
        }
    }


    /**
     * Check if a correct Number value is returned, truncation is
     * allowed to be performed.
     * Check should be done for every JDK type
     * supported.
     */
    @SpecAssertion(section = "4.2.3", id = "423-C9")
    @Test(description = "4.2.3 Check if a correct Number value is returned, truncation is " +
            "allowed to be performed. " +
            "Check should be done for every JDK type " +
            "supported.")
    public void testNumberValueWithTruncationZero() {
        Number[] nums = new Number[]{new BigDecimal("-0000000000000000.00000000000000000000000000000000000001234")};
        for (Number num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1;
                try {
                    mAmount1 = MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(num)
                            .create();
                } catch (ArithmeticException | MonetaryException e) {
                    // can happen if capabilities are exceeded
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                AssertJUnit
                        .assertEquals("Section 4.2.3: Number value (Double, truncating) returned is not correct for " + type.getName(),
                                new BigDecimal(String.valueOf(num)).doubleValue(), result.doubleValue(), 0.0d);
            }
        }
    }

    /**
     * Check if a correct precision value is returned.
     * Check should be done for every JDK type
     * supported.
     */
    @SpecAssertion(section = "4.2.3", id = "423-C10")
    @Test(description = "4.2.3 Check if a correct precision value is returned. Check should be done for every JDK type " +
            "supported.")
    public void testPrecisionZero() {
        String[] nums =
                new String[]{"-0", "0", "-0.0", "0.0", "-0.00", "0.00", "-0.000", "0.0000", "0.00000", "-0.0000000",
                        "-0.000000000", "-0.00000000000"};
        for (String num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1;
                BigDecimal bd = new BigDecimal(num);
                try {
                    mAmount1 =
                            MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(bd).create();
                } catch (MonetaryException | ArithmeticException e) {
                    // It is possible, that our test may exceed the capabilities, so in that case, we just continue
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                AssertJUnit.assertEquals("Section 4.2.3: Amount's scale does not match for " + bd + " correct for " + type.getName(),
                        bd.precision(), result.getPrecision());
            }
        }
    }

    /**
     * Check if a correct scale value is returned. For 0 the scale should always be 0.
     */
    @SpecAssertion(section = "4.2.3", id = "423-C11")
    @Test(description = "4.2.3 Check if a correct scale value is returned. Check should be done for every JDK type " +
            "supported.")
    public void testScaleZero() {
        String[] nums =
                new String[]{"-0", "-0.0", "-0.00", "-0.000", "-0.0000", "-0.00000", "-0.000000", "-0.00000000"};
        for (String num : nums) {
            for (Class type : MonetaryAmounts.getAmountTypes()) {
                if (type.equals(TestAmount.class)) {
                    continue;
                }
                MonetaryAmount mAmount1;
                BigDecimal bd = new BigDecimal(num);
                try {
                    mAmount1 =
                            MonetaryAmounts.getAmountFactory(type).setCurrency(DEFAULT_CURRENCY).setNumber(bd).create();
                } catch (MonetaryException | ArithmeticException e) {
                    // It is possible, that our test may exceed the capabilities, so in that case, we just continue
                    continue;
                }
                NumberValue result = mAmount1.getNumber();
                AssertJUnit.assertTrue("Section 4.2.3: Amount's scale is < 0 for " + num + ", was " +
                        result.getScale() + " for " + type.getName(), 0 <= result.getScale());
            }
        }
    }

}
