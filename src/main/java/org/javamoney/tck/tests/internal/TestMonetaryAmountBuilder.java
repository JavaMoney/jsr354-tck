/*
 * Copyright (c) 2012, 2013, Werner Keil, Credit Suisse (Anatole Tresch). Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License. Contributors: Anatole Tresch - initial version.
 */
package org.javamoney.tck.tests.internal;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.money.MonetaryAmountFactory;
import javax.money.MonetaryContext;
import javax.money.MonetaryCurrencies;
import javax.money.NumberValue;

public final class TestMonetaryAmountBuilder implements MonetaryAmountFactory<TestAmount> {

    private Number value;
    private CurrencyUnit currency;

    public static TestAmount getAmount(final Number number, final CurrencyUnit currency) {
        return new TestAmount(number, currency);
    }

    @Override
    public Class<? extends MonetaryAmount> getAmountType() {
        return TestAmount.class;
    }

    @Override
    public MonetaryAmountFactory<TestAmount> setCurrency(String currencyCode) {
        this.currency = MonetaryCurrencies.getCurrency(currencyCode);
        return this;
    }

    @Override
    public MonetaryAmountFactory<TestAmount> setCurrency(CurrencyUnit currency) {
        this.currency = currency;
        return this;
    }

    @Override
    public MonetaryAmountFactory<TestAmount> setNumber(double number) {
        this.value = number;
        return this;
    }

    @Override
    public MonetaryAmountFactory<TestAmount> setNumber(long number) {
        this.value = number;
        return this;
    }

    @Override
    public MonetaryAmountFactory<TestAmount> setNumber(Number number) {
        this.value = number;
        return this;
    }

    @Override
    public NumberValue getMaxNumber() {
        return null;
    }

    @Override
    public NumberValue getMinNumber() {
        return null;
    }

    @Override
    public MonetaryAmountFactory<TestAmount> setContext(MonetaryContext monetaryContext) {
        return this;
    }

    @Override
    public MonetaryAmountFactory<TestAmount> setAmount(MonetaryAmount amount) {
        setCurrency(amount.getCurrency());
        setNumber(amount.getNumber());
        return this;
    }

    @Override
    public TestAmount create() {
        return new TestAmount(value, currency);
    }

    @Override
    public MonetaryContext getDefaultMonetaryContext() {
        return TestAmount.MONETARY_CONTEXT;
    }

    @Override
    public MonetaryContext getMaximalMonetaryContext() {
        return TestAmount.MONETARY_CONTEXT;
    }
}