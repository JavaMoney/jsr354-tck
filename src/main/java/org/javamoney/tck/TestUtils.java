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

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;

import junit.framework.Assert;
import org.testng.AssertJUnit;

import javax.money.*;

import static org.testng.Assert.fail;
import static org.testng.AssertJUnit.assertNotNull;


public class TestUtils{

	private static final StringBuffer warnings = new StringBuffer();

	private TestUtils() {

	}


    public static final BigDecimal createNumberWithPrecision(MonetaryAmountFactory f, int precision){
        StringBuilder b = new StringBuilder(precision + 1);
        for(int i = 0; i < precision; i++){
            b.append(String.valueOf(i % 10));
        }
        return new BigDecimal(b.toString(), MathContext.UNLIMITED);
    }

    public static final BigDecimal createNumberWithScale(MonetaryAmountFactory f, int scale){
        StringBuilder b = new StringBuilder(scale + 2);
        b.append("9.");
        for(int i = 0; i < scale; i++){
            b.append(String.valueOf(i % 10));
        }
        return new BigDecimal(b.toString(), MathContext.UNLIMITED);
    }


	public static void testSerializable(String section, Class c) {
		if (!Serializable.class.isAssignableFrom(c)) {
			throw new TCKValidationException(section +": Class must be serializable: "
					+ c.getName());
		}
	}

	public static void testImmutable(String section, Class c) {
		if (c.isInterface()) {
			throw new TCKValidationException(
                    section +": Class is an interface, instead of a class: "
							+ c.getName());
		}
		if (c.isPrimitive()) {
			return;
		}
		if ((c.getModifiers() & Modifier.FINAL) == 0) {
			throw new TCKValidationException(
					"Class must be immutable, so it must be final: "
							+ c.getName());
		}
		Class current = c;
		while (current != null) {
			for (Field f : current.getDeclaredFields()) {
				if ((f.getModifiers() & Modifier.PRIVATE) == 0) {
					if ((f.getModifiers() & Modifier.FINAL) == 0) {
						throw new TCKValidationException(
								"Class must be immutable, but field is not private: "
										+ current.getName() + '#' + f.getName());
					}
				}
			}
			for (Method m : current.getDeclaredMethods()) {
				if ((m.getModifiers() & Modifier.PRIVATE) == 0) {
					checkImmutableMethod(m);
				}
			}
			current = current.getSuperclass();
		}
	}

	private static void checkImmutableMethod(Method m) {
		if (m.getParameterTypes().length == 0) {
			return;
		}
		if (m.getName().startsWith("set")) {
			throw new TCKValidationException(
					"Class must be immutable, but suspicious method was found: "
							+ m.getDeclaringClass().getName() + '#'
							+ m.getName()
							+ Arrays.toString(m.getParameterTypes()));
		}
	}

	private static <T> void checkReturnTypeIsConcrete(Method m,
			Class<T> ifaceType, Class<? extends T> concreteType) {
		Class c = m.getReturnType();
		if (Void.class.equals(c)) {
			return;
		}
		if (ifaceType.equals(c)) {
			throw new TCKValidationException(
					"Class must return concrete type(" + concreteType.getName()
							+ "), not interface type(" + ifaceType.getName()
							+ "): "
							+ m.getDeclaringClass().getName() + '#'
							+ m.getName()
							+ Arrays.toString(m.getParameterTypes()));
		}
	}

	public static void testSerializable(String section, Object o) {
		if (!(o instanceof Serializable)) {
			throw new TCKValidationException(section + ": Class must be serializable: "
					+ o.getClass().getName());
		}
		try (ObjectOutputStream oos = new ObjectOutputStream(
				new ByteArrayOutputStream())) {
			oos.writeObject(o);
		} catch (Exception e) {
			throw new TCKValidationException(
					"Class must be serializable, but serialization failed: "
							+ o.getClass().getName(), e);
		}
	}

	public static void testImplementsInterface(String section, Class type, Class iface) {
		Class current = type;
		for (Class ifa : type.getInterfaces()) {
			if (ifa.equals(iface)) {
				return;
			}
		}
		Assert.fail(section +": Class must implement " + iface.getName() + ", but does not: " + type.getName());
	}

	public static void testHasPublicMethod(String section, Class type, Class returnType,
			String name, Class... paramTypes) {
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
                section +": Class must implement method " + name + '('
						+ Arrays.toString(paramTypes) + "): "
						+ returnType.getName() + ", but does not: "
						+ type.getName());
	}

	public static void testHasPublicStaticMethod(String section, Class type, Class returnType,
			String name, Class... paramTypes) {
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
                section +": Class must implement method " + name + '('
						+ Arrays.toString(paramTypes) + "): "
						+ returnType.getName() + ", but does not: "
						+ type.getName());
	}

	public static void testHasNotPublicMethod(String section, Class type, Class returnType,
			String name, Class... paramTypes) {
		Class current = type;
		while (current != null) {
			for (Method m : current.getDeclaredMethods()) {
				if (returnType.equals(returnType) &&
						m.getName().equals(name) &&
						Arrays.equals(m.getParameterTypes(), paramTypes)) {
					throw new TCKValidationException(
                            section +": Class must NOT implement method " + name + '('
									+ Arrays.toString(paramTypes) + "): "
									+ returnType.getName() + ", but does: "
									+ type.getName());
				}
			}
			current = current.getSuperclass();
		}
	}

	public static void testComparable(String section, Class type) {
		testImplementsInterface(section, type, Comparable.class);
	}

	public static void assertValue(String section, Object value, String methodName,
			Object instance) throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		Method m = instance.getClass().getDeclaredMethod(methodName);
		Assert.assertEquals(section +": " + m.getName() + '(' + instance + ") returned invalid value:", value, m.invoke(instance));
	}

	public static boolean testImmutableOpt(String section, Class type) {
		try {
			testImmutable(section, type);
			return true;
		} catch (Exception e) {
			warnings.append(section +": Recommendation failed: Class should be immutable: "
					+ type.getName() + ", details: " + e.getMessage() + "\n");
			return false;
		}
	}

	public static boolean testSerializableOpt(String section, Class type) {
		try {
			testSerializable(section, type);
			return true;
		} catch (Exception e) {
			warnings.append(section +": Recommendation failed: Class should be serializable: "
					+ type.getName() + ", details: " + e.getMessage() + "\n");
			return false;
		}
	}

	public static boolean testHasPublicStaticMethodOpt(String section, Class type,
			Class returnType,
			String methodName, Class... paramTypes) {
		try {
			testHasPublicStaticMethod(section, type, returnType, methodName, paramTypes);
			return true;
		} catch (Exception e) {
			warnings.append(section +": Recommendation failed: Missing method [public static "
					+ methodName
					+ '('
					+ Arrays.toString(paramTypes) + "):" + returnType.getName()
					+ "] on: "
					+ type.getName() + "\n");
			return false;
		}
	}

	public static boolean testSerializableOpt(String section, Object instance) {
		try {
			testSerializable(section, instance);
			return true;
		} catch (Exception e) {
			warnings.append(section +": Recommendation failed: Class is serializable, but serialization failed: "
					+ instance.getClass().getName() + "\n");
			return false;
		}
	}

	public static void resetWarnings() {
		warnings.setLength(0);
	}

	public static String getWarnings() {
		return warnings.toString();
	}

    public static MonetaryAmount createAmountWithScale(int scale){
        MonetaryContext tgtContext =
                new MonetaryContext.Builder().setMaxScale(scale).build();
        Class<? extends MonetaryAmount> exceedingType = null;
        try{
            exceedingType = MonetaryAmounts.queryAmountType(tgtContext);
            AssertJUnit.assertNotNull(exceedingType);
            MonetaryAmountFactory<? extends MonetaryAmount> bigFactory =
                    MonetaryAmounts.getAmountFactory(exceedingType);
            return bigFactory.setCurrency("CHF").setNumber(createNumberWithScale(bigFactory, scale))
                    .create();
        }
        catch(MonetaryException e){
            return null;
        }
    }

    public static MonetaryAmount createAmountWithPrecision(int precision){
        MonetaryContext tgtContext =
                new MonetaryContext.Builder().setPrecision(precision).build();
        Class<? extends MonetaryAmount> exceedingType = null;
        try{
            exceedingType = MonetaryAmounts.queryAmountType(tgtContext);
            AssertJUnit.assertNotNull(exceedingType);
            MonetaryAmountFactory<? extends MonetaryAmount> bigFactory =
                    MonetaryAmounts.getAmountFactory(exceedingType);
            return bigFactory.setCurrency("CHF").setNumber(createNumberWithPrecision(bigFactory, precision))
                    .create();
        }
        catch(MonetaryException e){
            return null;
        }
    }
}
