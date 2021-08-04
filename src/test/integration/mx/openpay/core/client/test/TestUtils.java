/*
 * Copyright 2013 Opencard Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mx.openpay.core.client.test;

import java.math.BigDecimal;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import mx.openpay.client.Address;

/**
 * @author elopez
 */
public class TestUtils {
    
    public static final String TEST_ENVIRONMENT = "https://dev-api.openpay.mx/";
    
    public static Address prepareAddress() {
        Address address = new Address();
        address.setCity("Querétaro");
        address.setLine1("Camino #11 - 01");
        address.setPostalCode("76090");
        address.setState("Queretaro");
        address.setCountryCode("MX");
        return address;
    }

    public static Matcher<BigDecimal> equalsAmount(final BigDecimal amount) {
        return new BaseMatcher<BigDecimal>() {

            @Override
            public boolean matches(final Object arg0) {
                return ((BigDecimal) arg0).compareTo(amount) == 0;
            }

            @Override
            public void describeTo(final Description arg0) {
                arg0.appendText("Amount doesn't have the expected value. Expected: ")
                        .appendText(amount.toPlainString());

            }

            @Override
            public void describeMismatch(final Object obj, final Description desc) {
                desc.appendText("was ").appendValue(((BigDecimal) obj).toPlainString());
            }

        };
    }
    
    /** Credentials for a second merchant belonging to the same group. */
    public static OpenpayTestCredentials testMerchantCredentials() {
        return new OpenpayTestCredentials()
                .id("mioppzc1kgz2cj21s8sp")
                .privateKey("sk_fe7d3759f4514f4b8fe11cf0404812de");
    }

    
    /** Credentials for a second merchant belonging to the same group. */
    public static OpenpayTestCredentials testSecondaryMerchantCredentials() {
        return new OpenpayTestCredentials()
                .id("mxml0yv7ju1klmzzwhnu")
                .privateKey("sk_5ca11fcff28145c999c8c4df6ac42562");
    }

    /** Group Credentials for testing group operations. */
    public static OpenpayTestCredentials testGroupCredentials() {
        return new OpenpayTestCredentials()
                .id("gvtdrtkxh2gygdneq7vz")
                .privateKey("sk_e92dsvx4r42ugbandpmywcus5jqjm7jd");
    }
    
    

}
