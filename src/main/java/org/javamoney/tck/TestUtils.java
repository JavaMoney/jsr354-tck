/*
 * Copyright (c) 2012, 2013, Werner Keil, Credit Suisse (Anatole Tresch). Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License. Contributors: Anatole Tresch - initial version.
 */
package org.javamoney.tck;

import junit.framework.Assert;
import org.mutabilitydetector.unittesting.AllowedReason;
import org.mutabilitydetector.unittesting.MutabilityAssert;
import org.mutabilitydetector.unittesting.MutabilityMatchers;
import org.testng.AssertJUnit;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.MonetaryAmountFactory;
import javax.money.MonetaryAmountFactoryQuery;
import javax.money.MonetaryAmountFactoryQueryBuilder;
import javax.money.MonetaryException;
import javax.money.MonetaryOperator;
import javax.money.MonetaryQuery;
import javax.money.NumberValue;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.Currency;
import java.util.Random;

/**
 * Test Utility class.
 */
public final class TestUtils {
    /**
     * Warnings collected.
     */
    private static final StringBuffer WARNINGS = new StringBuffer();

    private TestUtils() {
    }


    /**
     * Creates a new number with the given precision.
     *
     * @param precision the precision
     * @return a corresponding number.
     */
    public static BigDecimal createNumberWithPrecision(int precision) {
        if (precision == 0) {
            precision = new Random().nextInt(100);
        }
        StringBuilder b = new StringBuilder(precision + 1);
        for (int i = 0; i < precision; i++) {
            b.append(String.valueOf(i % 10));
        }
        return new BigDecimal(b.toString(), MathContext.UNLIMITED);
    }

    /**
     * Creates a corresponding number with the required scale.
     *
     * @param scale the target scale.
     * @return a corresponding number.
     */
    public static BigDecimal createNumberWithScale(int scale) {
        StringBuilder b = new StringBuilder(scale + 2);
        b.append("9.");
        for (int i = 0; i < scale; i++) {
            b.append(String.valueOf(i % 10));
        }
        return new BigDecimal(b.toString(), MathContext.UNLIMITED);
    }

    /**
     * Tests the given class being serializable.
     *
     * @param section the section of the spec under test
     * @param c       the class to be checked.
     * @throws org.javamoney.tck.TCKValidationException if test fails.
     */
    public static void testSerializable(String section, Class c) {
        if (!Serializable.class.isAssignableFrom(c)) {
            throw new TCKValidationException(section + ": Class must be serializable: " + c.getName());
        }
    }

    /**
     * Tests the given class being immutable.
     *
     * @param section the section of the spec under test
     * @param c       the class to be checked.
     * @throws org.javamoney.tck.TCKValidationException if test fails.
     */
    public static void testImmutable(String section, Class c) {
        try {
            MutabilityAssert.assertInstancesOf(c, MutabilityMatchers.areImmutable(), AllowedReason
                            .provided(Currency.class, MonetaryAmount.class,
                                    CurrencyUnit.class, NumberValue.class,
                                    MonetaryOperator.class, MonetaryQuery.class)
                            .areAlsoImmutable(), AllowedReason.allowingForSubclassing(),
                    AllowedReason.allowingNonFinalFields());
        } catch (Exception e) {
            throw new TCKValidationException(section + ": Class is not immutable: " + c.getName(), e);
        }
    }

    /**
     * Tests the given object being (effectively) serializable by serializing it.
     *
     * @param section the section of the spec under test
     * @param o       the object to be checked.
     * @throws org.javamoney.tck.TCKValidationException if test fails.
     */
    public static void testSerializable(String section, Object o) {
        if (!(o instanceof Serializable)) {
            throw new TCKValidationException(section + ": Class must be serializable: " + o.getClass().getName());
        }
        try (
                ObjectOutputStream oos = new ObjectOutputStream(new ByteArrayOutputStream())) {
            oos.writeObject(o);
        } catch (Exception e) {
            throw new TCKValidationException(
                    "Class must be serializable, but serialization failed: " + o.getClass().getName(), e);
        }
    }

    /**
     * Tests the given class implements a given interface.
     *
     * @param section the section of the spec under test
     * @param type    the type to be checked.
     * @param iface   the interface to be checked for.
     * @throws org.javamoney.tck.TCKValidationException if test fails.
     */
    public static void testImplementsInterface(String section, Class type, Class iface) {
        for (Class ifa : type.getInterfaces()) {
            if (ifa.equals(iface)) {
                return;
            }
        }
        Assert.fail(section + ": Class must implement " + iface.getName() + ", but does not: " + type.getName());
    }

    /**
     * Tests if the given type has a public method with the given signature.
     * @param section the section of the spec under test
     * @param type the type to be checked.
     * @param returnType the method return type.
     * @param name the method name
     * @param paramTypes the parametr types.
     *                   @throws org.javamoney.tck.TCKValidationException if test fails.
     */
    public static void testHasPublicMethod(String section, Class type, Class returnType, String name,
                                           Class... paramTypes) {
        Class current = type;
        while (current != null) {
            for (Method m : current.getDeclaredMethods()) {
                if (returnType.equals(returnType) &&
                        m.getName().equals(name) &&
                        ((m.getModifiers() & Modifier.PUBLIC) != 0) &&
                        Arrays.equals(m.getParameterTypes(), paramTypes)) {
                    return;
                }
            }
            current = current.getSuperclass();
        }
        throw new TCKValidationException(
                section + ": Class must implement method " + name + '(' + Arrays.toString(paramTypes) + "): " +
                        returnType.getName() + ", but does not: " + type.getName());
    }

    /**
     * Tests if the given type has a public static method with the given signature.
     * @param section the section of the spec under test
     * @param type the type to be checked.
     * @param returnType the method return type.
     * @param name the method name
     * @param paramTypes the parametr types.
     *                   @throws org.javamoney.tck.TCKValidationException if test fails.
     */
    public static void testHasPublicStaticMethod(String section, Class type, Class returnType, String name,
                                                 Class... paramTypes) {
        Class current = type;
        while (current != null) {
            for (Method m : current.getDeclaredMethods()) {
                if (returnType.equals(returnType) &&
                        m.getName().equals(name) &&
                        ((m.getModifiers() & Modifier.PUBLIC) != 0) &&
                        ((m.getModifiers() & Modifier.STATIC) != 0) &&
                        Arrays.equals(m.getParameterTypes(), paramTypes)) {
                    return;
                }
            }
            current = current.getSuperclass();
        }
        throw new TCKValidationException(
                section + ": Class must implement method " + name + '(' + Arrays.toString(paramTypes) + "): " +
                        returnType.getName() + ", but does not: " + type.getName());
    }

    /**
     * Tests if the given type has not a public method with the given signature.
     * @param section the section of the spec under test
     * @param type the type to be checked.
     * @param returnType the method return type.
     * @param name the method name
     * @param paramTypes the parametr types.
     *                   @throws org.javamoney.tck.TCKValidationException if test fails.
     */
    public static void testHasNotPublicMethod(String section, Class type, Class returnType, String name,
                                              Class... paramTypes) {
        Class current = type;
        while (current != null) {
            for (Method m : current.getDeclaredMethods()) {
                if (returnType.equals(returnType) &&
                        m.getName().equals(name) &&
                        Arrays.equals(m.getParameterTypes(), paramTypes)) {
                    throw new TCKValidationException(
                            section + ": Class must NOT implement method " + name + '(' + Arrays.toString(paramTypes) +
                                    "): " + returnType.getName() + ", but does: " + type.getName());
                }
            }
            current = current.getSuperclass();
        }
    }

    /**
     * Tests if the given type is comparable.
     * @param section the section of the spec under test
     * @param type the type to be checked.
     *             @throws org.javamoney.tck.TCKValidationException if test fails.
     */
    public static void testComparable(String section, Class type) {
        testImplementsInterface(section, type, Comparable.class);
    }

    /**
     * Checks the returned value, when calling a given method.
     * @param section the section of the spec under test
     * @param value the expected value
     * @param methodName the target method name
     * @param instance the instance to call
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws org.javamoney.tck.TCKValidationException if test fails.
     */
    public static void assertValue(String section, Object value, String methodName, Object instance)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        Method m = instance.getClass().getDeclaredMethod(methodName);
        Assert.assertEquals(section + ": " + m.getName() + '(' + instance + ") returned invalid value:", value,
                m.invoke(instance));
    }

    /**
     * Test for immutability (optional recommendation), writes a warning if not given.
     * @param section the section of the spec under test
     * @param type the type to be checked.
     * @return true, if the instance is probably mutable.
     */
    public static boolean testImmutableOpt(String section, Class type) {
        try {
            testImmutable(section, type);
            return true;
        } catch (Exception e) {
            WARNINGS.append(section).append(": Recommendation failed: Class should be immutable: ")
                    .append(type.getName()).append(", details: ").append(e.getMessage()).append("\n");
            return false;
        }
    }
    /**
     * Test for serializable (optional recommendation), writes a warning if not given.
     * @param section the section of the spec under test
     * @param type the type to be checked.
     * @return true, if the instance is probably mutable.
     */
    public static boolean testSerializableOpt(String section, Class type) {
        try {
            testSerializable(section, type);
            return true;
        } catch (Exception e) {
            WARNINGS.append(section).append(": Recommendation failed: Class should be serializable: ")
                    .append(type.getName()).append(", details: ").append(e.getMessage()).append("\n");
            return false;
        }
    }

    /**
     * Tests if instance has a pipublic static method.
     * @param section the section of the spec under test
     * @param type the type to be checked.
     * @param returnType the method return type.
     * @param methodName  the target method name
     * @param paramTypes the parametr types.
     * @return true, if test succeeded.
     */
    public static boolean testHasPublicStaticMethodOpt(String section, Class type, Class returnType, String methodName,
                                                       Class... paramTypes) {
        try {
            testHasPublicStaticMethod(section, type, returnType, methodName, paramTypes);
            return true;
        } catch (Exception e) {
            WARNINGS.append(section).append(": Recommendation failed: Missing method [public static ")
                    .append(methodName).append('(').append(Arrays.toString(paramTypes)).append("):")
                    .append(returnType.getName()).append("] on: ").append(type.getName()).append("\n");
            return false;
        }
    }

    /**
     * Tests if an instance is effectively serializable.
     * @param section the section of the spec under test
     * @param instance the instance to call
     * @return true, if test succeded.
     */
    public static boolean testSerializableOpt(String section, Object instance) {
        try {
            testSerializable(section, instance);
            return true;
        } catch (Exception e) {
            WARNINGS.append(section)
                    .append(": Recommendation failed: Class is serializable, but serialization failed: ")
                    .append(instance.getClass().getName()).append("\n");
            return false;
        }
    }

    /**
     * Reset all collected WARNINGS.
     */
    public static void resetWarnings() {
        WARNINGS.setLength(0);
    }

    /**
     * Get the collected WARNINGS.
     * @return
     */
    public static String getWarnings() {
        return WARNINGS.toString();
    }

    /**
     * Creates an amount with the given scale.
     * @param scale the target scale
     * @return the new amount.
     */
    public static MonetaryAmount createAmountWithScale(int scale) {
        MonetaryAmountFactoryQuery tgtContext = MonetaryAmountFactoryQueryBuilder.of().setMaxScale(scale).build();
        MonetaryAmountFactory<?> exceedingFactory;
        try {
            exceedingFactory = Monetary.getAmountFactory(tgtContext);
            AssertJUnit.assertNotNull(exceedingFactory);
            MonetaryAmountFactory<? extends MonetaryAmount> bigFactory =
                    Monetary.getAmountFactory(exceedingFactory.getAmountType());
            return bigFactory.setCurrency("CHF").setNumber(createNumberWithScale(scale)).create();
        } catch (MonetaryException e) {
            return null;
        }
    }

    /**
     * Creates an amount with the given precision.
     * @param precision the target precision
     * @return a corresponding amount.
     */
    public static MonetaryAmount createAmountWithPrecision(int precision) {
        MonetaryAmountFactoryQuery tgtContext = MonetaryAmountFactoryQueryBuilder.of().setPrecision(precision).build();
        MonetaryAmountFactory<?> exceedingFactory;
        try {
            exceedingFactory = Monetary.getAmountFactory(tgtContext);
            AssertJUnit.assertNotNull(exceedingFactory);
            MonetaryAmountFactory<? extends MonetaryAmount> bigFactory =
                    Monetary.getAmountFactory(exceedingFactory.getAmountType());
            return bigFactory.setCurrency("CHF").setNumber(createNumberWithPrecision(precision)).create();
        } catch (MonetaryException e) {
            return null;
        }
    }
}
