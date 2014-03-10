/*
 * CREDIT SUISSE IS WILLING TO LICENSE THIS SPECIFICATION TO YOU ONLY UPON THE
 * CONDITION THAT YOU ACCEPT ALL OF THE TERMS CONTAINED IN THIS AGREEMENT.
 * PLEASE READ THE TERMS AND CONDITIONS OF THIS AGREEMENT CAREFULLY. BY
 * DOWNLOADING THIS SPECIFICATION, YOU ACCEPT THE TERMS AND CONDITIONS OF THE
 * AGREEMENT. IF YOU ARE NOT WILLING TO BE BOUND BY IT, SELECT THE "DECLINE"
 * BUTTON AT THE BOTTOM OF THIS PAGE. Specification: JSR-354 Money and Currency
 * API ("Specification") Copyright (c) 2012-2013, Credit Suisse All rights
 * reserved.
 */

package org.javamoney.tck.tests;

import junit.extensions.TestSetup;
import org.javamoney.tck.TCKTestSetup;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.Test;

import javax.money.MonetaryOperator;
import java.util.Collection;

import static org.junit.Assert.fail;

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
            fail("To be implemented.");
        }
    }


}
