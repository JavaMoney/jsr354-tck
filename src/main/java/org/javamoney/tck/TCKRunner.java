/*
 * Copyright (c) 2012, 2020, Werner Keil, Anatole Tresch. Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License. Contributors: Anatole Tresch - initial version.
 */
package org.javamoney.tck;

import org.javamoney.tck.tests.AccessingCurrenciesAmountsRoundingsTest;
import org.javamoney.tck.tests.CreatingMonetaryAmountsTest;
import org.javamoney.tck.tests.ExternalizingNumericValueTest;
import org.javamoney.tck.tests.FunctionalExtensionPointsTest;
import org.javamoney.tck.tests.ModellingCurrenciesTest;
import org.javamoney.tck.tests.ModellingMonetaryAmountsTest;
import org.javamoney.tck.tests.conversion.ConvertingAmountsTest;
import org.javamoney.tck.tests.conversion.ExchangeRatesAndRateProvidersTest;
import org.javamoney.tck.tests.conversion.MonetaryConversionsTest;
import org.javamoney.tck.tests.conversion.ProviderChainsTest;
import org.javamoney.tck.tests.format.FormattingMonetaryAmountsTest;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.annotations.Test;
import org.testng.internal.ConstructorOrMethod;
import org.testng.reporters.VerboseReporter;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.SourceVersion;
import javax.tools.Tool;

/**
 * Main class for executing the JSR 354 TCK.
 * Created by Anatole on 12.06.2014.
 */
public final class TCKRunner extends XmlSuite implements Tool {
    /**
     * Constructor.
     */
    public TCKRunner() {
        setName("JSR354-TCK, version 1.1");
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

    /**
     * Main method to start the TCK. Optional arguments are:
     * <ul>
     *     <li>-DoutputDir for defining the output directory TestNG uses (default: ./target/tck-output).</li>
     *     <li>-Dverbose=true to enable TestNG verbose mode.</li>
     *     <li>-DreportFile=targetFile.txt for defining the TCK result summary report target file
     *     (default: ./target/tck-results.txt).</li>
     * </ul>
     * @param in input stream
     * @param out output stream
     * @param err error output
     * @param args arguments
     */
    @Override
    public int run(InputStream in, OutputStream out, OutputStream err,
    			String... args) {
        System.out.println("-- JSR 354 TCK started --");
        List<XmlSuite> suites = new ArrayList<>();
        suites.add(new TCKRunner());
        TestNG tng = new TestNG();
        tng.setXmlSuites(suites);
        String outDir = System.getProperty("outputDir");
        if(outDir!=null) {
            tng.setOutputDirectory(outDir);
        }
        else{
            tng.setOutputDirectory("./target/tck-output");
        }
        String verbose = System.getProperty("verbose");
        if("true".equalsIgnoreCase(verbose)){
            tng.setVerbose(1);
        }
        String reportFile = System.getProperty("reportFile");
        File file = null;
        if(reportFile!=null) {
            file = new File(reportFile);
        }
        else{
            file = new File("./target/tck-results.txt");
        }
        TCKReporter rep = new TCKReporter(file);
        System.out.println("Writing to file " + file.getAbsolutePath() + " ...");
        tng.addListener(rep);
        tng.run();
        rep.writeSummary();
        System.out.println("-- JSR 354 TCK  finished --");
        return 0;
    }
    
    @Override
    public final Set<SourceVersion> getSourceVersions() {
	return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            new SourceVersion[]{SourceVersion.RELEASE_5, SourceVersion.RELEASE_6,
                    SourceVersion.RELEASE_7})));
    }
    
    public static void main(String... args) {
	 TCKRunner runner = new TCKRunner();
	 runner.run(System.in, System.out, System.err, args);
    }
    
    /**
     * Reporter implementation.
     */
    public static final class TCKReporter extends TestListenerAdapter {
        private int count = 0;
        private int skipped = 0;
        private int failed = 0;
        private int success = 0;

        private StringWriter internalBuffer = new StringWriter(3000);
        private FileWriter w;

        /**
         * Constructor of the TCK reporter, writing to the given file.
         * @param file the target file, not null.
         */
        public TCKReporter(File file) {
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                w = new FileWriter(file);
                w.write("********************************************************************************************\n");
                w.write("**** JSR 354 - Money & Currency, Technical Compatibility Kit, version 1.1\n");
                w.write("********************************************************************************************\n\n");
                w.write("Executed on " + new java.util.Date() + "\n\n");

                // System.out:
                internalBuffer.write("********************************************************************************\n");
                internalBuffer.write("**** JSR 354 - Money & Currency, Technical Compatibility Kit, version 1.1.\n");
                internalBuffer.write("********************************************************************************\n\n");
                internalBuffer.write("Executed on " + new java.util.Date() + "\n\n");
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }

        @Override
        public void onTestFailure(ITestResult tr) {
            failed++;
            count++;
            String location = tr.getTestClass().getRealClass().getSimpleName() + '#' + tr.getMethod().getMethodName();
            try {
                Method realTestMethod = tr.getMethod().getConstructorOrMethod().getMethod();
                Test testAnnot = realTestMethod.getAnnotation(Test.class);
                if (!testAnnot.description().isEmpty()) {
                    if (tr.getThrowable() != null) {
                        StringWriter sw = new StringWriter();
                        PrintWriter w = new PrintWriter(sw);
                        tr.getThrowable().printStackTrace(w);
                        w.flush();
                        log("[FAILED]  " + testAnnot.description() + "(" + location + "):\n" + sw.toString());
                    } else {
                        log("[FAILED]  " + testAnnot.description() + "(" + location + ")");
                    }
                } else {

                    if (tr.getThrowable() != null) {
                        StringWriter sw = new StringWriter();
                        PrintWriter w = new PrintWriter(sw);
                        tr.getThrowable().printStackTrace(w);
                        w.flush();
                        log("[FAILED]  " + location + ":\n" + sw.toString());
                    } else {
                        log("[FAILED]  " + location);
                    }
                }
            } catch (IOException e) {
                throw new IllegalStateException("IO Error", e);
            }
        }

        @Override
        public void onTestSkipped(ITestResult tr) {
            skipped++;
            count++;
            String location = tr.getTestClass().getRealClass().getSimpleName() + '#' + tr.getMethod().getMethodName();
            try {
                Method realTestMethod = tr.getMethod().getConstructorOrMethod().getMethod();
                Test specAssert = realTestMethod.getAnnotation(Test.class);
                if (specAssert != null && !specAssert.description().isEmpty()) {
                    log("[SKIPPED] " + specAssert.description() + "(" + location + ")");
                } else {
                    log("[SKIPPED] " + location);
                }
            } catch (IOException e) {
                throw new IllegalStateException("IO Error", e);
            }
        }

        @Override
        public void onTestSuccess(ITestResult tr) {
            success++;
            count++;
            String location = tr.getTestClass().getRealClass().getSimpleName() + '#' + tr.getMethod().getMethodName();
            try {
                Method realTestMethod = tr.getMethod().getConstructorOrMethod().getMethod();
                Test specAssert = realTestMethod.getAnnotation(Test.class);
                if (specAssert != null && !specAssert.description().isEmpty()) {
                    log("[SUCCESS] " + specAssert.description() + "(" + location + ")");
                } else {
                    log("[SUCCESS] " + location);
                }
            } catch (IOException e) {
                throw new IllegalStateException("IO Error", e);
            }
        }

        private void log(String text) throws IOException {
            w.write(text);
            w.write('\n');
            internalBuffer.write(text);
            internalBuffer.write('\n');
        }

        public void writeSummary() {
            try {
                log("\nJSR 354 TCK, version 1.1 Summary"); // FIXME this should not be hardcoded here
                log("------------------------------------------");
                log("\nTOTAL TESTS EXECUTED : " + count);
                log("TOTAL TESTS SKIPPED  : " + skipped);
                log("TOTAL TESTS SUCCESS  : " + success);
                log("TOTAL TESTS FAILED   : " + failed);
                w.flush();
                w.close();
                internalBuffer.flush();
                System.out.println();
                System.out.println(internalBuffer);
            } catch (IOException e) {
                throw new IllegalStateException("IO Error", e);
            }
        }
    }
}
