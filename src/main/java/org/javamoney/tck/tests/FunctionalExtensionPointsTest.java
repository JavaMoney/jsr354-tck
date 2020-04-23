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

import org.javamoney.tck.TCKTestSetup;
import org.javamoney.tck.tests.internal.TestAmount;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.MonetaryAmountFactory;
import javax.money.MonetaryOperator;
import java.util.Collection;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Tests the functional extension points.
 */
@SpecVersion(spec = "JSR 354", version = "1.1.0")
public class FunctionalExtensionPointsTest {

    // *************************** A. Monetary Operator Implementation Requirements ***************

    /**
     * The return type of apply must be the same type as the
     * parameter
     * (amount.getClass() == result.getClass()).
     */
    @SpecAssertion(section = "4.2.4", id = "424-A1")
    @Test(description = "4.2.4 Ensures the result of all operators under test is of the same class as the input.")
    public void testOperatorReturnTypeEqualsParameter() {
        Collection<MonetaryOperator> operators = TCKTestSetup.getTestConfiguration().getMonetaryOperators4Test();
        assertNotNull(operators,
                "No operators (null) to test returned from TestConfiguration.getMonetaryOperators4Test().");
        for (Class type : Monetary.getAmountTypes()) {
            if (type.equals(TestAmount.class)) {
                continue;
            }
            MonetaryAmountFactory<?> f = Monetary.getAmountFactory(type);
            f.setCurrency("CHF");
            f.setNumber(200.10);
            MonetaryAmount m = f.create();
            for (MonetaryOperator op : operators) {
                MonetaryAmount m2 = m.with(op);
                assertEquals(m2.getClass(), m.getClass(),
                        "Operator returned type with different type, which is not allowed: " +
                                op.getClass().getName());
            }
        }
    }


}
