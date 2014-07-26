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
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import javax.money.MonetaryOperator;
import java.util.Collection;

/**
 * Created by Anatole on 10.03.14.
 */
@SpecVersion(spec = "JSR 354", version = "1.0.0")
public class FunctionalExtensionPointsTest{

    // *************************** A. Monetary Operator Implementation Requirements ***************

    /**
     * The return type of apply must be the same type as the
     parameter
     (amount.getClass() == result.getClass()).
     */
    @SpecAssertion(section = "4.2.4", id = "424-A1")
    @Test
    public void testOperatorReturnTypeEqualsParameter(){
        Collection<MonetaryOperator> operators = TCKTestSetup.getTestConfiguration().getMonetaryOperators4Test();
        for(MonetaryOperator op: operators){
            AssertJUnit.fail("To be implemented.");
        }
    }


}
