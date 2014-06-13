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

package org.javamoney.tck.tests.spi;

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.money.spi.MonetaryAmountFormatProviderSpi;
import java.util.ServiceLoader;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * Created by Anatole on 10.03.14.
 */
@SpecVersion(spec = "JSR 354", version = "1.0.0")
public class FormattingSPITest{

    // ********************************* C. Prodivding Amount Formats

    /**
     * Test registered MonetaryAmountFormatProviderSpi (one is
     required),
     especially bad case behaviour for
     invalid
     input.
     */
    @Test
    @SpecAssertion(id="452-A1", section="4.5.2")
    public void testMonetaryAmountFormatProviderSpiIsRegistered(){
        ServiceLoader l = null;
        try{
            l = ServiceLoader.load(MonetaryAmountFormatProviderSpi.class);
        }
        catch(Exception e){
            Assert.fail("Failure during check for loaded MonetaryAmountFormatProviderSpi.", e);
        }
        Assert.assertTrue(l.iterator().hasNext(),
                          "No instance of MonetaryAmountFormatProviderSpi provided by implementation.");
    }

}
