package org.javamoney.tck;

import org.javamoney.tck.tests.*;
import org.javamoney.tck.tests.conversion.ConvertingAmountsTest;
import org.javamoney.tck.tests.conversion.ExchangeRatesAndRateProvidersTest;
import org.javamoney.tck.tests.conversion.MonetaryConversionsTest;
import org.javamoney.tck.tests.conversion.ProviderChainsTest;
import org.javamoney.tck.tests.format.FormattingMonetaryAmountsTest;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anatole on 12.06.2014.
 */
public class TCKRunner extends XmlSuite{
    public TCKRunner(){
        setName("JSR354-TCK 1.0");
        XmlTest test = new XmlTest(this);
        test.setName("TCK/Test Setup");
        List<XmlClass> classes = new ArrayList<XmlClass>();
        classes.add(new XmlClass(TCKTestSetup.class));
        classes.add(new XmlClass(ModellingCurrenciesTest.class));
        classes.add(new XmlClass(ModellingMonetaryAmountsTest.class));
        classes.add(new XmlClass(CreatingMonetaryAmountsTest.class));
        classes.add(new XmlClass(ExternalizingNumericValueTest.class));
        classes.add(new XmlClass(FunctionalExtensionPointsTest.class));
        classes.add(new XmlClass(AccessingCurrenciesAmountsRoundingsTest.class));
        classes.add(new XmlClass(MonetaryConversionsTest.class));
        classes.add(new XmlClass(ExchangeRatesAndRateProvidersTest.class));
        classes.add(new XmlClass(ConvertingAmountsTest.class));
        classes.add(new XmlClass(ProviderChainsTest.class));
        classes.add(new XmlClass(FormattingMonetaryAmountsTest.class));
        test.setXmlClasses(classes);
    }

    public static final void main(String... args){
        List<XmlSuite> suites = new ArrayList<XmlSuite>();
        suites.add(new TCKRunner());
        TestNG tng = new TestNG();
        tng.setXmlSuites(suites);
        tng.run();
    }

}
