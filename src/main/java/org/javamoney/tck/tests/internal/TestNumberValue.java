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

import javax.money.NumberValue;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Objects;

/**
 * Created by Anatole on 26.04.2014.
 */
public final class TestNumberValue extends NumberValue {
    private static final long serialVersionUID = 1L;

    private BigDecimal value;

    public TestNumberValue(Number value) {
        Objects.requireNonNull(value);
        this.value = new BigDecimal(String.valueOf(value));
    }

    @Override
    public long longValue() {
        return value.longValue();
    }

    @Override
    public int intValue() {
        return value.intValue();
    }

    @Override
    public float floatValue() {
        return value.floatValue();
    }

    @Override
    public double doubleValue() {
        return value.doubleValue();
    }

    @Override
    public <T extends Number> T numberValueExact(Class<T> numberType) {
        return null;
    }

    @Override
    public long getAmountFractionNumerator() {
        return 0;
    }

    @Override
    public long getAmountFractionDenominator() {
        return 0;
    }

    @Override
    public <T extends Number> T numberValue(Class<T> numberType) {
        if (numberType.equals(Integer.class)) {
            return (T) Integer.valueOf(value.intValue());
        }
        if (numberType.equals(BigInteger.class)) {
            return (T) BigInteger.valueOf(value.intValue());
        }
        if (numberType.equals(BigDecimal.class)) {
            return (T) BigDecimal.valueOf(value.doubleValue());
        }
        throw new UnsupportedOperationException(numberType.getCanonicalName());
    }

    @Override
    public NumberValue round(MathContext mathContext) {
        return new TestNumberValue(this.value.round(mathContext));
    }

    @Override
    public long longValueExact() {
        return value.longValue();
    }

    @Override
    public int intValueExact() {
        return value.intValue();
    }

    @Override
    public int getScale() {
        return value.scale();
    }

    @Override
    public int getPrecision() {
        return value.precision();
    }

    @Override
    public Class<?> getNumberType() {
        return BigDecimal.class;
    }

    @Override
    public double doubleValueExact() {
        return value.doubleValue();
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}
