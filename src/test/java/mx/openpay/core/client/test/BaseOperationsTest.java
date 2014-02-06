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

import static mx.openpay.core.client.TestConstans.API_KEY;
import static mx.openpay.core.client.TestConstans.ENDPOINT;
import static mx.openpay.core.client.TestConstans.MERCHANT_ID;

import java.util.TimeZone;

import lombok.Getter;
import mx.openpay.client.core.OpenpayAPI;

import org.junit.Before;

/**
 * @author elopez
 */
public class BaseOperationsTest {

    @Getter
    private OpenpayAPI api;

    @Before
    public void setUp() throws Exception {
        OpenpayAPI api = new OpenpayAPI(ENDPOINT, API_KEY, MERCHANT_ID);
        TimeZone.setDefault(TimeZone.getTimeZone("Mexico/General"));
    }

}
