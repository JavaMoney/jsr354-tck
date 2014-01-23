package org.javamoney.tck;

import java.util.Arrays;
import java.util.Collection;
import java.util.ServiceLoader;

public final class TCKTestSetup {

	private static JSR354TestConfiguration TEST_CONFIG = loadTestConfiguration();

	private TCKTestSetup() {
	}

	private static JSR354TestConfiguration loadTestConfiguration() {
		try {
			return ServiceLoader.load(JSR354TestConfiguration.class).iterator()
					.next();
		} catch (Exception e) {
			try {
				Class.forName("org.javamoney.moneta.Money");
				return new JSRTestSetup();
			} catch (Exception e2) {
				throw new IllegalStateException("No valid implementation of "
						+ JSR354TestConfiguration.class.getName()
						+ " is registered with the ServiceLoader.");
			}
		}
	}

	public static JSR354TestConfiguration getTestConfiguration() {
		return TEST_CONFIG;
	}

	private static final class JSRTestSetup implements JSR354TestConfiguration {

		@Override
		public Collection<Class> getAmountClasses() {
			try {
				return Arrays
						.asList(new Class[] {
								Class.forName("org.javamoney.moneta.Money"),
								Class.forName("org.javamoney.moneta.FastMoney") });
			} catch (ClassNotFoundException e) {
				throw new IllegalStateException("No valid implementation of "
						+ JSR354TestConfiguration.class.getName()
						+ " is registered with the ServiceLoader.");
			}
		}

		@Override
		public Collection<Class> getCurrencyClasses() {
			try {
				return Arrays
						.asList(new Class[] { Class
								.forName("org.javamoney.moneta.internal.JDKCurrencyAdapter") });
			} catch (ClassNotFoundException e) {
				throw new IllegalStateException("No valid implementation of "
						+ JSR354TestConfiguration.class.getName()
						+ " is registered with the ServiceLoader.");
			}
		}

	}

}
