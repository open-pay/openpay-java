/*
 * COPYRIGHT © 2012-2014. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.core.client.full;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import mx.openpay.client.Plan;
import mx.openpay.client.enums.PlanRepeatUnit;
import mx.openpay.client.enums.PlanStatusAfterRetry;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
@Slf4j
public class PlansTest extends BaseTest {

    private List<Plan> plansToDelete;

    @Before
    public void setUp() throws Exception {
        this.plansToDelete = new ArrayList<Plan>();
    }

    @After
    public void tearDown() throws Exception {
        for (Plan plan : this.plansToDelete) {
            this.api.plans().delete(plan.getId());
        }
    }

    @Test
    public void testCreate() throws Exception {
        String name = "junit test plan " + System.currentTimeMillis();
        Plan request = new Plan()
                .name(name)
                .amount(BigDecimal.TEN)
                .repeatEvery(3, PlanRepeatUnit.WEEK)
                .retryTimes(100)
                .trialDays(4)
                .statusAfterRetry(PlanStatusAfterRetry.CANCELLED);
        Plan plan = this.api.plans().create(request);
        this.plansToDelete.add(plan);
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
    public void testDelete() throws Exception {
        Plan request = new Plan()
                .name("junit test plan " + System.currentTimeMillis())
                .amount(BigDecimal.TEN)
                .repeatEvery(3, PlanRepeatUnit.WEEK)
                .retryTimes(100)
                .trialDays(4)
                .statusAfterRetry(PlanStatusAfterRetry.CANCELLED);
        Plan plan = this.api.plans().create(request);
        this.api.plans().delete(plan.getId());
        try {
            assertNull(this.api.plans().get(plan.getId()));
        } catch (OpenpayServiceException e) {
            assertEquals(404, e.getHttpCode().intValue());
        }
    }

    @Test
    public void testCreateNoTrialDays() throws Exception {
        Plan request = new Plan()
                .name("somsodms")
                .amount(BigDecimal.TEN)
                .repeatEvery(3, PlanRepeatUnit.WEEK)
                .statusAfterRetry(PlanStatusAfterRetry.UNPAID)
                .retryTimes(100);
        Plan plan = this.api.plans().create(request);
        this.plansToDelete.add(plan);
        assertThat(plan.getTrialDays(), is(0));
    }

    @Test
    public void testCreate_AfterRetryUnpaid() throws OpenpayServiceException, ServiceUnavailableException {
        String name = "junit test plan " + System.currentTimeMillis();
        Plan request = new Plan()
                .name(name)
                .amount(BigDecimal.TEN)
                .repeatEvery(3, PlanRepeatUnit.WEEK)
                .retryTimes(100)
                .trialDays(4)
                .statusAfterRetry(PlanStatusAfterRetry.UNPAID);
        Plan plan = this.api.plans().create(request);
        this.plansToDelete.add(plan);
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
        assertThat(plan.getStatusAfterRetry(), is("unpaid"));
        assertThat(plan.getTrialDays(), is(4));
    }

    @Test
    public void testGet() throws Exception {
        Plan request = new Plan()
                .name("Premium Subscriptions")
                .amount(new BigDecimal("12.34"))
                .repeatEvery(1, PlanRepeatUnit.MONTH)
                .retryTimes(100)
                .statusAfterRetry(PlanStatusAfterRetry.UNPAID);
        Plan plan = this.api.plans().create(request);
        this.plansToDelete.add(plan);
        String id = plan.getId();
        plan = this.api.plans().get(plan.getId());
        log.info("{}", plan);
        assertThat(plan.getId(), is(id));
        assertThat(plan.getStatus(), is("active"));
        assertTrue(new BigDecimal("12.34").compareTo(plan.getAmount()) == 0);
        assertThat(plan.getCurrency(), is("MXN"));
        assertThat(plan.getName(), is("Premium Subscriptions"));
        assertThat(plan.getRepeatEvery(), is(1));
        assertThat(plan.getRepeatUnit(), is("month"));
        assertThat(plan.getRetryTimes(), is(100));
        assertThat(plan.getStatusAfterRetry(), is("unpaid"));
        assertThat(plan.getTrialDays(), is(0));
    }

    @Test
    public void testList_Empty() throws Exception {
        List<Plan> list = this.api.plans().list(null);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testList() throws Exception {
        this.plansToDelete.add(this.api.plans().create(new Plan()
                .name("Premium Subscriptions 1")
                .amount(new BigDecimal("1200.00"))
                .repeatEvery(1, PlanRepeatUnit.MONTH)
                .statusAfterRetry(PlanStatusAfterRetry.UNPAID)));
        this.plansToDelete.add(this.api.plans().create(new Plan()
                .name("Premium Subscriptions 2")
                .amount(new BigDecimal("1200.00"))
                .repeatEvery(1, PlanRepeatUnit.MONTH)
                .statusAfterRetry(PlanStatusAfterRetry.UNPAID)));
        this.plansToDelete.add(this.api.plans().create(new Plan()
                .name("Premium Subscriptions 3")
                .amount(new BigDecimal("1200.00"))
                .repeatEvery(1, PlanRepeatUnit.MONTH)
                .statusAfterRetry(PlanStatusAfterRetry.UNPAID)));
        this.plansToDelete.add(this.api.plans().create(new Plan()
                .name("Premium Subscriptions 4")
                .amount(new BigDecimal("1200.00"))
                .repeatEvery(1, PlanRepeatUnit.MONTH)
                .statusAfterRetry(PlanStatusAfterRetry.UNPAID)));
        List<Plan> list = this.api.plans().list(null);
        assertThat(list.size(), is(4));
        for (Plan plan : list) {
            assertNotNull(plan.getId());
        }
    }

    @Test
    public void testUpdate() throws Exception {
        Plan request = new Plan()
                .name("Premium Subscriptions")
                .amount(new BigDecimal("1200.00"))
                .repeatEvery(1, PlanRepeatUnit.MONTH)
                .retryTimes(100)
                .statusAfterRetry(PlanStatusAfterRetry.UNPAID);
        Plan plan = this.api.plans().create(request);
        this.plansToDelete.add(plan);
        String id = plan.getId();
        plan = this.api.plans().get(plan.getId());

        plan.setName("nombre");
        plan.setTrialDays(5);
        plan = this.api.plans().update(plan);
        assertThat(plan.getId(), is(id));
        assertThat(plan.getName(), is("nombre"));
        assertThat(plan.getTrialDays(), is(5));

        plan.setName("nombre nuevo");
        plan = this.api.plans().update(plan);
        assertThat(plan.getId(), is(id));
        assertThat(plan.getName(), is("nombre nuevo"));
        assertThat(plan.getTrialDays(), is(5));

        plan.setTrialDays(3);
        plan = this.api.plans().update(plan);
        assertThat(plan.getId(), is(id));
        assertThat(plan.getName(), is("nombre nuevo"));
        assertThat(plan.getTrialDays(), is(3));

        plan = this.api.plans().update(plan);
        assertThat(plan.getId(), is(id));
        assertThat(plan.getName(), is("nombre nuevo"));
        assertThat(plan.getTrialDays(), is(3));

    }
}
