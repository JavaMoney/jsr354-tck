/*
 * Copyright (c) 2012, 2013, Werner Keil, Credit Suisse (Anatole Tresch). Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License. Contributors: Anatole Tresch - initial version.
 */
package org.javamoney.tck.tests.conversion;

import javax.money.CurrencyUnit;
import javax.money.NumberValue;
import javax.money.convert.ConversionContext;
import javax.money.convert.ExchangeRate;
import javax.money.convert.RateType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * This class models an exchange rate, which defines the factor the numeric value of a base amount in some currency
 * 'A' must be multiplied
 * to get the corresponding amount in the terminating currency 'B'. Hereby
 * <ul>
 * <li>an exchange rate always models one rate from a base (source) to a term
 * (target) {@link javax.money.CurrencyUnit}.</li>
 * <li>an exchange rate is always bound to a rate type, which typically matches
 * the data source of the conversion data, e.g. different credit card providers
 * may use different rates for the same conversion.</li>
 * <li>an exchange rate may restrict its validity. In most of the use cases a
 * rates' validity will be well defined, but it is also possible that the data
 * provider is not able to support the rate's validity, leaving it undefined-</li>
 * <li>an exchange rate has a provider, which is responsible for defining the
 * rate. A provider hereby may be, but must not be the same as the rate's data
 * source.</li>
 * <li>an exchange rate can be a <i>direct</i> rate, where its factor is
 * represented by a single conversion step. Or it can model a <i>derived</i>
 * rate, where multiple conversion steps are required to define the overall
 * base/term conversion. In case of derived rates the chained rates define the
 * overall factor, by multiplying the individual chain rate factors. Of course,
 * this also requires that each subsequent rate's base currency in the chain
 * does match the previous term currency (and vice versa):</li>
 * <li>Whereas the factor should be directly implied by the format rate chain
 * for derived rates, this is obviously not the case for the validity range,
 * since rates can have a undefined validity range. Nevertheless in many cases
 * also the validity range can (but must not) be derived from the rate chain.</li>
 * <li>Finally a conversion rate is always unidirectional. There might be cases
 * where the reciprocal value of {@link #factor} matches the correct reverse
 * rate. But in most use cases the reverse rate either has a different rate (not
 * equal to the reciprocal value), or might not be defined at all. Therefore for
 * reversing a ExchangeRate one must access an {@link javax.money.convert.ExchangeRateProvider} and
 * query for the reverse rate.</li>
 * </ul>
 * <p>
 * The class also implements {@link Comparable} to allow sorting of multiple
 * exchange rates using the following sorting order;
 * <ul>
 * <li>Exchange rate type</li>
 * <li>Exchange rate provider</li>
 * <li>base currency</li>
 * <li>term currency</li>
 * </ul>
 * <p>
 * Finally ExchangeRate is modeled as an immutable and thread safe type. Also
 * exchange rates are {@link java.io.Serializable}, hereby serializing in the following
 * form and order:
 * <ul>
 * <li>The base {@link javax.money.CurrencyUnit}
 * <li>The target {@link javax.money.CurrencyUnit}
 * <li>The factor (NumberValue)
 * <li>The {@link javax.money.convert.ConversionContext}
 * <li>The rate chain
 * </ul>
 *
 * @author Werner Keil
 * @author Anatole Tresch
 * @see <a
 * href="https://en.wikipedia.org/wiki/Exchange_rate#Quotations">Wikipedia:
 * Exchange Rate (Quotations)</a>
 */
@SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
public final class TestExchangeRate implements ExchangeRate, Serializable, Comparable<ExchangeRate> {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 5077295306570465837L;
    /**
     * The base currency.
     */
    private final CurrencyUnit base;
    /**
     * The terminating currency.
     */
    private final CurrencyUnit term;
    /**
     * The conversion factor.
     */
    private final NumberValue factor;
    /**
     * The {@link javax.money.convert.ConversionContext}
     */
    private final ConversionContext conversionContext;
    /**
     * The full chain, at least one instance long.
     */
    private List<ExchangeRate> chain = new ArrayList<>();


    /**
     * Creates a new instance with a custom chain of exchange rate type, e.g. or
     * creating <i>derived</i> rates.
     *
     * @param builder The Builder, never {@code null}.
     */
    private TestExchangeRate(Builder builder) {
        Objects.requireNonNull(builder.base, "base may not be null.");
        Objects.requireNonNull(builder.term, "term may not be null.");
        Objects.requireNonNull(builder.factor, "factor may not be null.");
        Objects.requireNonNull(builder.conversionContext, "exchangeRateType may not be null.");
        this.base = builder.base;
        this.term = builder.term;
        this.factor = builder.factor;
        this.conversionContext = builder.conversionContext;

        setExchangeRateChain(builder.rateChain);
    }

    /**
     * Internal method to set the rate chain, which also ensure that the chain
     * passed, when not null, contains valid elements.
     *
     * @param chain the chain to set.
     */
    private void setExchangeRateChain(List<ExchangeRate> chain) {
        this.chain.clear();
        if (chain == null || chain.isEmpty()) {
            this.chain.add(this);
        } else {
            for (ExchangeRate aChain : chain) {
                if (aChain == null) {
                    throw new IllegalArgumentException("Chain element can not be null.");
                }
            }
            this.chain.addAll(chain);
        }
    }

    /**
     * Access the {@link javax.money.convert.ConversionContext} of {@link javax.money.convert.ExchangeRate}.
     *
     * @return the conversion context, never null.
     */
    public final ConversionContext getContext() {
        return this.conversionContext;
    }

    /**
     * Get the base (source) {@link javax.money.CurrencyUnit}.
     *
     * @return the base {@link javax.money.CurrencyUnit}.
     */
    public final CurrencyUnit getBaseCurrency() {
        return this.base;
    }

    /**
     * Get the term (target) {@link javax.money.CurrencyUnit}.
     *
     * @return the term {@link javax.money.CurrencyUnit}.
     */
    public final CurrencyUnit getCurrency() {
        return this.term;
    }

    /**
     * Access the rate's bid factor.
     *
     * @return the bid factor for this exchange rate, or {@code null}.
     */
    public final NumberValue getFactor() {
        return this.factor;
    }

    /**
     * Access the chain of exchange rates.
     *
     * @return the chain of rates, in case of a derived rate, this may be
     * several instances. For a direct exchange rate, this equals to
     * <code>new ExchangeRate[]{this}</code>.
     */
    public final List<ExchangeRate> getExchangeRateChain() {
        return this.chain;
    }

    /**
     * Allows to evaluate if this exchange rate is a derived exchange rate.
     * Derived exchange rates are defined by an ordered list of subconversions
     * with intermediate steps, whereas a direct conversion is possible in one
     * steps.
     * <p>
     * This method always returns {@code true}, if the chain contains more than
     * one rate. Direct rates, have also a chain, but with exact one rate.
     *
     * @return true, if the exchange rate is derived.
     */
    public final boolean isDerived() {
        return this.chain.size() > 1;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(ExchangeRate o) {
        if (o == null) {
            return -1;
        }
        int compare = this.getBaseCurrency().getCurrencyCode().compareTo(o.getBaseCurrency().getCurrencyCode());
        if (compare == 0) {
            compare = this.getCurrency().getCurrencyCode().compareTo(o.getCurrency().getCurrencyCode());
        }
        if (compare == 0) {
            compare = this.getContext().getProviderName().compareTo(o.getContext().getProviderName());
        }
        return compare;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ExchangeRate [base=" + base + ", factor=" + factor + ", conversionContext=" + conversionContext + "]";
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((base == null) ? 0 : base.hashCode());
        result = prime * result + ((conversionContext == null) ? 0 : conversionContext.hashCode());
        result = prime * result + ((factor == null) ? 0 : factor.hashCode());
        result = prime * result + ((term == null) ? 0 : term.hashCode());
        result = prime * result + ((chain == null) ? 0 : chain.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TestExchangeRate other = (TestExchangeRate) obj;
        if (base == null) {
            if (other.base != null) {
                return false;
            }
        } else if (!base.equals(other.base)) {
            return false;
        }
        if (!chain.equals(other.getExchangeRateChain())) {
            return false;
        }
        if (conversionContext == null) {
            if (other.conversionContext != null) {
                return false;
            }
        } else if (!conversionContext.equals(other.conversionContext)) {
            return false;
        }
        if (factor == null) {
            if (other.factor != null) {
                return false;
            }
        } else if (!factor.equals(other.factor)) {
            return false;
        }
        if (term == null) {
            if (other.term != null) {
                return false;
            }
        } else if (!term.equals(other.term)) {
            return false;
        }
        return true;
    }

    /**
     * Builder for creating new instances of {@link javax.money.convert.ExchangeRate}. Note that
     * instances of this class are not thread-safe.
     *
     * @author Anatole Tresch
     * @author Werner Keil
     */
    public static class Builder {

        /**
         * The {@link javax.money.convert.ConversionContext}.
         */
        private ConversionContext conversionContext;
        /**
         * The base (source) currency.
         */
        private CurrencyUnit base;
        /**
         * The term (target) currency.
         */
        private CurrencyUnit term;
        /**
         * The conversion factor.
         */
        private NumberValue factor;
        /**
         * The chain of involved rates.
         */
        private List<ExchangeRate> rateChain = new ArrayList<>();

        /**
         * Sets the exchange rate type
         *
         * @param rateType the {@link javax.money.convert.RateType} contained
         */
        public Builder(String provider, RateType rateType) {
            this(ConversionContext.of(provider, rateType));
        }

        /**
         * Sets the exchange rate type
         *
         * @param context the {@link javax.money.convert.ConversionContext} to be applied
         */
        public Builder(ConversionContext context) {
            setContext(context);
        }

        /**
         * Sets the exchange rate type
         *
         * @param rate the {@link javax.money.convert.ExchangeRate} to be applied
         */
        public Builder(ExchangeRate rate) {
            setContext(rate.getContext());
            setFactor(rate.getFactor());
            setTerm(rate.getCurrency());
            setBase(rate.getBaseCurrency());
            setRateChain(rate.getExchangeRateChain());
        }

        /**
         * Sets the base {@link javax.money.CurrencyUnit}
         *
         * @param base to base (source) {@link javax.money.CurrencyUnit} to be applied
         * @return the builder instance
         */
        public Builder setBase(CurrencyUnit base) {
            this.base = base;
            return this;
        }

        /**
         * Sets the terminating (target) {@link javax.money.CurrencyUnit}
         *
         * @param term to terminating {@link javax.money.CurrencyUnit} to be applied
         * @return the builder instance
         */
        public Builder setTerm(CurrencyUnit term) {
            this.term = term;
            return this;
        }

        /**
         * Sets the {@link javax.money.convert.ExchangeRate} chain.
         *
         * @param exchangeRates the {@link javax.money.convert.ExchangeRate} chain to be applied
         * @return the builder instance
         */
        public Builder setRateChain(ExchangeRate... exchangeRates) {
            this.rateChain.clear();
            if (exchangeRates != null) {
                this.rateChain.addAll(Arrays.asList(exchangeRates.clone()));
            }
            return this;
        }

        /**
         * Sets the {@link javax.money.convert.ExchangeRate} chain.
         *
         * @param exchangeRates the {@link javax.money.convert.ExchangeRate} chain to be applied
         * @return the builder instance
         */
        public Builder setRateChain(List<ExchangeRate> exchangeRates) {
            this.rateChain.clear();
            if (exchangeRates != null) {
                this.rateChain.addAll(exchangeRates);
            }
            return this;
        }


        /**
         * Sets the conversion factor, as the factor
         * {@code base * factor = target}.
         *
         * @param factor the factor.
         * @return The builder instance.
         */
        public Builder setFactor(NumberValue factor) {
            this.factor = factor;
            return this;
        }

        /**
         * Sets the provider to be applied.
         *
         * @param conversionContext the {@link javax.money.convert.ConversionContext}, not null.
         * @return The builder.
         */
        public Builder setContext(ConversionContext conversionContext) {
            Objects.requireNonNull(conversionContext);
            this.conversionContext = conversionContext;
            return this;
        }

        /**
         * Builds a new instance of {@link javax.money.convert.ExchangeRate}.
         *
         * @return a new instance of {@link javax.money.convert.ExchangeRate}.
         * @throws IllegalArgumentException if the rate could not be built.
         */
        public TestExchangeRate build() {
            return new TestExchangeRate(this);
        }

        /**
         * Initialize the {@link Builder} with an {@link javax.money.convert.ExchangeRate}. This is
         * useful for creating a new rate, reusing some properties from an
         * existing one.
         *
         * @param rate the base rate
         * @return the Builder, for chaining.
         */
        public Builder setRate(ExchangeRate rate) {
            this.base = rate.getBaseCurrency();
            this.term = rate.getCurrency();
            this.conversionContext = rate.getContext();
            this.factor = rate.getFactor();
            this.rateChain = rate.getExchangeRateChain();
            this.term = rate.getCurrency();
            return this;
        }
    }

    /**
     * Create a {@link Builder} based on the current rate instance.
     *
     * @return a new {@link Builder}, never {@code null}.
     */
    public Builder toBuilder() {
        return new Builder(getContext()).setBase(getBaseCurrency()).setTerm(getCurrency())
                .setFactor(getFactor()).setRateChain(getExchangeRateChain());
    }
}
