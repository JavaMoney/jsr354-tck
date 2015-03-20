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

import javax.money.MonetaryOperator;
import java.util.Collection;

/**
 * Libraries that implement this JSR and want to be tested with this TCK must implement this
 * interface and register it using the {@link java.util.ServiceLoader}.
 *
 * @author Anatole Tresch
 */
public interface JSR354TestConfiguration {

    /**
     * Return a collection with all {@link javax.money.MonetaryAmount} classes that are implemented. The list
     * must not be empty and should contain <b>every</b> amount class implemented.<p>
     * This enables the TCK to check in addition to the basic implementation compliance, if
     * according {@link javax.money.spi.MonetaryAmountFactoryProviderSpi} are registered/available correctly.
     *
     * @return a collection with all implemented amount classes, not null.
     */
    Collection<Class> getAmountClasses();

    /**
     * List a collection of {@link javax.money.CurrencyUnit} implementation.<p>
     * This enables the TCK to check the basic implementation compliance.
     *
     * @return a collection with CurrencyUnit implementations to be tested.
     */
    Collection<Class> getCurrencyClasses();


    /**
     * This method allows to let instances of MonetaryOperator to be tested for requirements and recommendations.
     *
     * @return the list of operators to be checked, not null. It is allowed to return an empty list here, which will
     * disable TCK tests for MonetaryOperator instances.
     */
    Collection<MonetaryOperator> getMonetaryOperators4Test();

}
