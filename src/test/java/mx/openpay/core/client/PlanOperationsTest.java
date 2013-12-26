/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.core.client;

import static mx.openpay.core.client.TestConstans.API_KEY;
import static mx.openpay.core.client.TestConstans.ENDPOINT;
import static mx.openpay.core.client.TestConstans.MERCHANT_ID;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import mx.openpay.client.Plan;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.core.operations.PlanOperations;
import mx.openpay.client.core.requests.subscription.CreatePlanParams;
import mx.openpay.client.core.requests.subscription.UpdatePlanParams;
import mx.openpay.client.enums.PlanRepeatUnit;
import mx.openpay.client.enums.PlanStatusAfterRetry;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

import org.junit.Before;
import org.junit.Test;

/**
 * @author elopez
 */
@Slf4j
public class PlanOperationsTest {

    private static final String EXISTING_PLAN_ID = "pqycd8nndru5jeav5lh7";

    private static final String UPDATE_PLAN_ID = "pa0n8gpvdehcgspum1du";

    private OpenpayAPI api;

    private PlanOperations plans;

    @Before
    public void setUp() throws Exception {
        this.api = new OpenpayAPI(ENDPOINT, API_KEY, MERCHANT_ID);
        this.plans = this.api.plans();
    }

    @Test
    public void testCreate() throws Exception {
        String name = "Plan de prueba junit " + System.currentTimeMillis();
        CreatePlanParams request = new CreatePlanParams()
                .name(name)
                .amount(BigDecimal.TEN)
                .repeatEvery(3, PlanRepeatUnit.WEEK)
                .retryTimes(100)
                .trialDays(4)
                .statusAfterRetry(PlanStatusAfterRetry.CANCELLED);
        Plan plan = this.plans.create(request);

        this.plans.delete(plan.getId());
        try {
            assertNull(this.plans.get(plan.getId()));
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
        }
        log.info("{}", plan);
        assertNotNull(plan.getId());
        assertNotNull(plan.getCreationDate());
        assertThat(plan.getStatus(), is("active"));
        assertTrue(BigDecimal.TEN.compareTo(plan.getAmount()) == 0);
        assertThat(plan.getCurrency(), is("MXN"));
        assertThat(plan.getName(), is(name));
        assertThat(plan.getRepeatEvery(), is(3));
        assertThat(plan.getRepeatUnit(), is("week"));
        assertThat(plan.getRetryTimes(), is(100));
        assertThat(plan.getStatusAfterRetry(), is("cancelled"));
        assertThat(plan.getTrialDays(), is(4));
    }

    @Test
    public void testCreateNoTrialDays() throws Exception {
        CreatePlanParams request = new CreatePlanParams()
                .name("somsodms")
                .amount(BigDecimal.TEN)
                .repeatEvery(3, PlanRepeatUnit.WEEK)
                .statusAfterRetry(PlanStatusAfterRetry.UNPAID)
                .retryTimes(100);
        Plan plan = this.plans.create(request);
        this.plans.delete(plan.getId());
    }

    @Test
    public void testCreateExample() throws OpenpayServiceException, ServiceUnavailableException {
        CreatePlanParams request = new CreatePlanParams()
                .name("Premium Subscriptions")
                .amount(new BigDecimal("1200.00"))
                .repeatEvery(1, PlanRepeatUnit.MONTH)
                .retryTimes(100)
                .statusAfterRetry(PlanStatusAfterRetry.UNPAID);
        Plan plan = this.plans.create(request);
        this.plans.delete(plan.getId());
    }

    @Test
    public void testGet() throws Exception {
        Plan plan = this.plans.get(EXISTING_PLAN_ID);
        log.info("{}", plan);
        assertThat(plan.getId(), is(EXISTING_PLAN_ID));

        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2013-12-26 10:25:42");
        assertThat(plan.getCreationDate(), is(date));
        assertThat(plan.getStatus(), is("active"));
        assertTrue(new BigDecimal("12.34").compareTo(plan.getAmount()) == 0);
        assertThat(plan.getCurrency(), is("MXN"));
        assertThat(plan.getName(), is("Existing plan"));
        assertThat(plan.getRepeatEvery(), is(1));
        assertThat(plan.getRepeatUnit(), is("month"));
        assertThat(plan.getRetryTimes(), is(3));
        assertThat(plan.getStatusAfterRetry(), is("unpaid"));
        assertThat(plan.getTrialDays(), is(243));
    }

    @Test
    public void testList() throws Exception {
        List<Plan> list = this.plans.list(null);
        assertTrue(list.size() > 0);
        for (Plan plan : list) {
            assertNotNull(plan.getId());
        }
    }

    @Test
    public void testUpdate() throws Exception {
        UpdatePlanParams updatePlan = new UpdatePlanParams().planId(UPDATE_PLAN_ID);
        updatePlan.name("nombre").trialDays(5);
        Plan plan = this.plans.update(updatePlan);
        assertThat(plan.getId(), is(UPDATE_PLAN_ID));
        assertThat(plan.getName(), is("nombre"));
        assertThat(plan.getTrialDays(), is(5));

        updatePlan.name("nombre nuevo").trialDays(null);
        plan = this.plans.update(updatePlan);
        assertThat(plan.getId(), is(UPDATE_PLAN_ID));
        assertThat(plan.getName(), is("nombre nuevo"));
        assertThat(plan.getTrialDays(), is(5));

        updatePlan.name(null).trialDays(3);
        plan = this.plans.update(updatePlan);
        assertThat(plan.getId(), is(UPDATE_PLAN_ID));
        assertThat(plan.getName(), is("nombre nuevo"));
        assertThat(plan.getTrialDays(), is(3));

        updatePlan.name(null).trialDays(null);
        plan = this.plans.update(updatePlan);
        assertThat(plan.getId(), is(UPDATE_PLAN_ID));
        assertThat(plan.getName(), is("nombre nuevo"));
        assertThat(plan.getTrialDays(), is(3));

        plan = this.plans.get(UPDATE_PLAN_ID);
        assertThat(plan.getId(), is(UPDATE_PLAN_ID));
        assertThat(plan.getName(), is("nombre nuevo"));
        assertThat(plan.getTrialDays(), is(3));
    }
}
