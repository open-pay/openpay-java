package mx.openpay.core.client.full.groups;

import java.util.TimeZone;

import org.junit.Before;

import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.core.groups.OpenpayGroupAPI;
import mx.openpay.core.client.test.OpenpayTestCredentials;
import mx.openpay.core.client.test.TestUtils;

public class GroupBaseTest {
    
    protected OpenpayTestCredentials groupCredentials = TestUtils.testGroupCredentials();

    protected OpenpayGroupAPI groupApi;

    protected OpenpayTestCredentials firstMerchantCredentials = TestUtils.testMerchantCredentials();

    protected OpenpayAPI firstMerchantApi;

    protected OpenpayTestCredentials secondMerchantCredentials = TestUtils.testSecondaryMerchantCredentials();

    protected OpenpayAPI secondMerchantApi;

    @Before
    public void setupAPI() throws Exception {
        this.groupApi = new OpenpayGroupAPI(TestUtils.TEST_ENVIRONMENT, groupCredentials.getPrivateKey(),
                groupCredentials.getId());
        TimeZone.setDefault(TimeZone.getTimeZone("Mexico/General"));
        this.firstMerchantApi = new OpenpayAPI(TestUtils.TEST_ENVIRONMENT, firstMerchantCredentials.getPrivateKey(),
                firstMerchantCredentials.getId());
        this.secondMerchantApi = new OpenpayAPI(TestUtils.TEST_ENVIRONMENT, secondMerchantCredentials.getPrivateKey(),
                secondMerchantCredentials.getId());
    }

}
