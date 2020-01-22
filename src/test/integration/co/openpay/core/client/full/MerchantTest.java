/*
 * Copyright 2014 Opencard Inc.
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
package co.openpay.core.client.full;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import co.openpay.client.Merchant;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public class MerchantTest extends BaseTest {

    @Test
    public void testGetMerchant() throws Exception {
        Merchant merchant = this.api.merchant().get();
        assertNotNull(merchant.getCreationDate());
        assertNotNull(merchant.getBalance());
        assertNotNull(merchant.getEmail());
        assertNotNull(merchant.getName());
        assertNotNull(merchant.getStatus());
        assertNotNull(merchant.getPhone());
        assertNotNull(merchant.getId());
    }
}
