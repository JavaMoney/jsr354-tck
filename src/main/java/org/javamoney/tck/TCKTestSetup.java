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

import javax.money.MonetaryOperator;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
			throw new IllegalStateException("No valid implementation of "
						+ JSR354TestConfiguration.class.getName()
						+ " is registered with the ServiceLoader.");
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

        @Override
        public Collection<MonetaryOperator> getMonetaryOperators4Test(){
            return Collections.emptyList();
        }

    }

}
