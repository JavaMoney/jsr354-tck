/*
 * Copyright (c) 2012, 2013, Werner Keil, Credit Suisse (Anatole Tresch). Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License. Contributors: Anatole Tresch - initial version.
 */
package org.javamoney.tck.tests.spi;

import static org.testng.Assert.assertTrue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import javax.money.CurrencyQuery;
import javax.money.CurrencyQueryBuilder;
import javax.money.CurrencyUnit;
import javax.money.Monetary;

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests the CurrencyProviderSpi.
 */
@SpecVersion(spec = "JSR 354", version = "1.1.0")
public class CurrencyProviderSPITest {


  // ***************************************** A. Searching Currencies ***********************************

  /**
   * Test registered CurrencyProviderSpi supports regex for currency codes.
   */
  @Test(description = "4.5.1 Test if a currency code regex is supported.")
  @SpecAssertion(id = "451-A1", section = "4.5.1")
  public void testSeachByRegex() {
      String dollarRegex = "\\p{Upper}{2}D";
      Pattern dollarPattern = Pattern.compile(dollarRegex);
      Collection<CurrencyUnit> allCurrencies = Monetary.getCurrencies(CurrencyQueryBuilder.of().build());
      Set<String> availableDollarCodes = new HashSet<>();
      for (CurrencyUnit currencyUnit : allCurrencies) {
        String currencyCode = currencyUnit.getCurrencyCode();
        if (dollarPattern.matcher(currencyCode).matches()) {
          availableDollarCodes.add(currencyCode);
        }
      }

      if (availableDollarCodes.isEmpty()) {
        // no dollar currencies registered, this is ok
        return;
      }

      CurrencyQuery dollarQuery = CurrencyQueryBuilder.of().setCurrencyCodes(dollarRegex).build();
      final Collection<CurrencyUnit> dollarCurrencies = Monetary.getCurrencies(dollarQuery);
      for (CurrencyUnit dollarCurrency : dollarCurrencies) {
          availableDollarCodes.remove(dollarCurrency.getCurrencyCode());
      }

      assertTrue(availableDollarCodes.isEmpty(), String.format("Available Dollar codes are not empty but %s.",
              availableDollarCodes.size()));
  }

}
