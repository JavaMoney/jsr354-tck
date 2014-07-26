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

import org.javamoney.tck.tests.*;
import org.javamoney.tck.tests.conversion.ConvertingAmountsTest;
import org.javamoney.tck.tests.conversion.ExchangeRatesAndRateProvidersTest;
import org.javamoney.tck.tests.conversion.MonetaryConversionsTest;
import org.javamoney.tck.tests.conversion.ProviderChainsTest;
import org.javamoney.tck.tests.format.FormattingMonetaryAmountsTest;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.reporters.VerboseReporter;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
        List<XmlClass> classes = new ArrayList<>();
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

    public static void main(String... args){
        List<XmlSuite> suites = new ArrayList<>();
        suites.add(new TCKRunner());
        TestNG tng = new TestNG();
        tng.setXmlSuites(suites);
        tng.setOutputDirectory("./tck-results");
        tng.addListener(new VerboseReporter());
        TCKReporter rep = new TCKReporter(new File("c:/temp/tck-results.txt"));
        tng.addListener(rep);
        tng.run();
        rep.writeSummary();
    }

    public static final class TCKReporter extends TestListenerAdapter{
        private int count = 0;
        private int skipped = 0;
        private int failed = 0;
        private int success = 0;

        private FileWriter w;

        public TCKReporter(File file){
            try{
                w = new FileWriter(file);
            }
            catch(IOException e){
                e.printStackTrace();
                System.exit(-1);
            }
        }

        @Override
        public void onTestFailure(ITestResult tr){
            failed++;
            try{
                log("[FAILED] " + tr.toString());
            }
            catch(IOException e){
                throw new IllegalStateException("IO Error", e);
            }
        }

        @Override
        public void onTestSkipped(ITestResult tr){
            skipped++;
            try{
                log("[SKIPPED] " + tr.toString());
            }
            catch(IOException e){
                throw new IllegalStateException("IO Error", e);
            }
        }

        @Override
        public void onTestSuccess(ITestResult tr){
            success++;
            try{
                log("[SUCCESS] " + tr.getTestName());
            }
            catch(IOException e){
                throw new IllegalStateException("IO Error", e);
            }
        }

        private void log(String string) throws IOException{
            count++;
            w.write(string + '\n');
        }

        public void writeSummary(){
            try{
                log("\nJSR 354 TCP version 1.0 Summary");
                log("-------------------------------");
                log("\nTOTAL TESTS EXECUTED : " + count);
                log("TOTAL TESTS SKIPPED  : " + skipped);
                log("TOTAL TESTS SUCCESS  : " + success);
                log("TOTAL TESTS FAILED   : " + failed);
                w.flush();
                w.close();
            }
            catch(IOException e){
                throw new IllegalStateException("IO Error", e);
            }
        }
    }


}
