package org.javamoney.tck;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.javamoney.moneta.FastMoney;
import org.javamoney.moneta.Money;

public final class TCKTestSetup {

	private static JSRTestSetup TEST_SETUP = new JSRTestSetup();

	private TCKTestSetup() {
	}

	public static JSR354TestConfiguration getTestConfiguration() {
		// TODO load dynamicylly
		return TEST_SETUP;
	}

	private static final class JSRTestSetup implements JSR354TestConfiguration {

		@Override
		public Collection<Class> getExceptionClasses() {
			return Collections.emptySet();
			// return Arrays
			// .asList(new Class[] { CurrencyMismatchException.class,
			// UnknownCurrencyException.class });
		}

		@Override
		public Collection<Class> getAmountClasses() {
			return Arrays
					.asList(new Class[] { Money.class, FastMoney.class });
		}

		@Override
		public Collection<Class> getCurrencyClasses() {
			try {
				return Arrays
						.asList(new Class[] { Class.forName("org.javamoney.moneta.impl.JDKCurrencyAdapter") });
			} catch (ClassNotFoundException e) {
				return Collections.emptySet();
			}
		}

	}

}
