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
package mx.openpay.core.client.full;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mx.openpay.client.Card;
import mx.openpay.client.Customer;
import mx.openpay.client.Plan;
import mx.openpay.client.Subscription;
import mx.openpay.client.enums.PlanRepeatUnit;
import mx.openpay.client.enums.PlanStatusAfterRetry;
import mx.openpay.core.client.test.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public class SubscriptionsTest extends BaseTest {

    private Customer customer;

    private Card card;

    private Card secondCard;

    private Plan planWithoutTrial;

    private Plan planWithTrial;

    private List<Subscription> subscriptionsToDelete;

    @Before
    public void setUp() throws Exception {
        this.subscriptionsToDelete = new ArrayList<Subscription>();
        this.customer = this.api.customers().create(new Customer()
                .name("Juan").email("juan.perez@gmail.com")
                .phoneNumber("55-25634013"));
        this.card = this.api.cards().create(this.customer.getId(), new Card()
                .cardNumber("4111111111111111")
                .holderName("Juanito Pérez Nuñez")
                .cvv2("111")
                .expirationMonth(9)
                .expirationYear(14)
                .address(TestUtils.prepareAddress()));
        this.secondCard = this.api.cards().create(this.customer.getId(), new Card()
                .cardNumber("4242424242424242")
                .holderName("Juanito Pérez Nuñez")
                .cvv2("111")
                .expirationMonth(9)
                .expirationYear(14)
                .address(TestUtils.prepareAddress()));
        this.planWithTrial = this.api.plans().create(
                new Plan().name("Test plan").amount(BigDecimal.TEN).statusAfterRetry(PlanStatusAfterRetry.CANCELLED)
                        .trialDays(30).repeatEvery(1, PlanRepeatUnit.WEEK));
        this.planWithoutTrial = this.api.plans().create(
                new Plan().name("Test plan").amount(BigDecimal.TEN).statusAfterRetry(PlanStatusAfterRetry.CANCELLED)
                        .repeatEvery(1, PlanRepeatUnit.WEEK));
    }

    @After
    public void tearDown() throws Exception {
        for (Subscription subscription : this.subscriptionsToDelete) {
            this.api.subscriptions().delete(this.customer.getId(), subscription.getId());
        }
        if (this.planWithTrial != null) {
            this.api.plans().delete(this.planWithTrial.getId());
        }
        if (this.planWithoutTrial != null) {
            this.api.plans().delete(this.planWithoutTrial.getId());
        }
        if (this.card != null) {
            this.api.cards().delete(this.customer.getId(), this.card.getId());
        }
        if (this.secondCard != null) {
            this.api.cards().delete(this.customer.getId(), this.secondCard.getId());
        }
        this.api.customers().delete(this.customer.getId());
    }

    @Test
    public void testCreateTrial_ExistingCard() throws Exception {
        Subscription createSubscription = new Subscription()
                .planId(this.planWithTrial.getId())
                .cardId(this.card.getId());
        Subscription subscription = this.api.subscriptions().create(this.customer.getId(), createSubscription);
        this.subscriptionsToDelete.add(subscription);
        assertNotNull(subscription.getId());
        assertThat(subscription.getCard().getId(), is(this.card.getId()));
        assertThat(subscription.getCard().getCardNumber(), is("1111"));
        assertNotNull(subscription.getCreationDate());
        assertNotNull(subscription.getChargeDate());
        assertThat(subscription.getCurrentPeriodNumber(), is(0));
        assertThat(subscription.getCustomerId(), is(this.customer.getId()));
        assertNotNull(subscription.getPeriodEndDate());
        assertThat(subscription.getPlanId(), is(this.planWithTrial.getId()));
        assertThat(subscription.getStatus(), is("trial"));
        assertNotNull(subscription.getTrialEndDate());
    }

    @Test
    public void testCreateTrial_NewCard() throws Exception {
        Card card = this.getCard();
        Subscription createSubscription = new Subscription()
                .planId(this.planWithTrial.getId())
                .card(card);
        Subscription subscription = this.api.subscriptions().create(this.customer.getId(), createSubscription);
        this.subscriptionsToDelete.add(subscription);
        assertNotNull(subscription.getId());
        assertNull(subscription.getCard().getId());
        assertThat(subscription.getCard().getCardNumber(), is("4444"));
        assertNotNull(subscription.getCreationDate());
        assertNotNull(subscription.getChargeDate());
        assertThat(subscription.getCurrentPeriodNumber(), is(0));
        assertThat(subscription.getCustomerId(), is(this.customer.getId()));
        assertNotNull(subscription.getPeriodEndDate());
        assertThat(subscription.getPlanId(), is(this.planWithTrial.getId()));
        assertThat(subscription.getStatus(), is("trial"));
        assertNotNull(subscription.getTrialEndDate());
    }

    @Test
    public void testCreateNoTrial_NewCard() throws Exception {
        Card card = this.getCard();
        Subscription createSubscription = new Subscription()
                .planId(this.planWithoutTrial.getId()).card(card);
        Subscription subscription = this.api.subscriptions().create(this.customer.getId(), createSubscription);
        this.subscriptionsToDelete.add(subscription);
        Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, -1);
        assertNotNull(subscription.getId());
        assertNull(subscription.getCard().getId());
        assertThat(subscription.getCard().getCardNumber(), is("4444"));
        assertNotNull(subscription.getCreationDate());
        assertNotNull(subscription.getChargeDate());
        assertThat(subscription.getCurrentPeriodNumber(), is(1));
        assertThat(subscription.getCustomerId(), is(this.customer.getId()));
        assertNotNull(subscription.getPeriodEndDate());
        assertThat(subscription.getPlanId(), is(this.planWithoutTrial.getId()));
        assertThat(subscription.getStatus(), is("active"));
        assertThat(subscription.getTrialEndDate(), is(calendar.getTime()));
    }

    @Test
    public void testGet() throws Exception {
        Date start = new Date();
        Thread.sleep(1200);
        Subscription subscription = this.api.subscriptions().create(this.customer.getId(), new Subscription()
                .planId(this.planWithTrial.getId()).card(this.getCard()));
        this.subscriptionsToDelete.add(subscription);
        subscription = this.api.subscriptions().get(this.customer.getId(), subscription.getId());
        assertThat(subscription.getId(), is(subscription.getId()));
        assertNull(subscription.getCard().getId());
        assertThat(subscription.getCard().getCardNumber(), is("4444"));
        // SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(subscription.getCreationDate());
        System.out.println(start);
        assertTrue(subscription.getCreationDate().compareTo(start) >= 0);
        // assertThat(subscription.getChargeDate(), is(chargeDate));
        assertThat(subscription.getCurrentPeriodNumber(), is(0));
        assertThat(subscription.getCustomerId(), is(this.customer.getId()));
        // assertThat(subscription.getPeriodEndDate(), is(periodEndDate));
        assertThat(subscription.getPlanId(), is(this.planWithTrial.getId()));
        assertThat(subscription.getStatus(), is("trial"));
        // Date trialEndDate = simpleDateFormat.parse("2014-08-25 00:00:00");
        // assertThat(subscription.getTrialEndDate(), is(trialEndDate));
    }

    @Test
    public void testUpdate() throws Exception {
        Subscription subscription = this.api.subscriptions().create(this.customer.getId(), new Subscription()
                .planId(this.planWithTrial.getId()).card(this.getCard()));
        String id = subscription.getId();
        this.subscriptionsToDelete.add(subscription);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date trialEndDate = simpleDateFormat.parse("2015-03-16 01:12:55");
        Date trialEndDateNoMinutes = simpleDateFormat.parse("2015-03-16 00:00:00");
        subscription = this.api.subscriptions().get(this.customer.getId(), id);

        subscription.setTrialEndDate(trialEndDate);
        subscription.setCancelAtPeriodEnd(true);
        subscription.setCard(this.getCard());

        subscription = this.api.subscriptions().update(subscription);

        assertThat(subscription.getId(), is(id));
        assertNull(subscription.getCardId());
        assertNull(subscription.getCard().getId());
        assertThat(subscription.getCard().getCardNumber(), is("4444"));
        assertThat(subscription.getTrialEndDate(), is(trialEndDateNoMinutes));
        assertThat(subscription.getCancelAtPeriodEnd(), is(true));

        trialEndDate = simpleDateFormat.parse("2017-05-21 06:12:55");
        trialEndDateNoMinutes = simpleDateFormat.parse("2017-05-21 00:00:00");
        subscription.setTrialEndDate(trialEndDate);
        subscription = this.api.subscriptions().update(subscription);

        assertThat(subscription.getId(), is(id));
        assertNull(subscription.getCardId());
        assertNull(subscription.getCard().getId());
        assertThat(subscription.getCard().getCardNumber(), is("4444"));
        assertThat(subscription.getTrialEndDate(), is(trialEndDateNoMinutes));
        assertThat(subscription.getCancelAtPeriodEnd(), is(true));

        subscription.setCancelAtPeriodEnd(false);
        subscription = this.api.subscriptions().update(subscription);

        assertThat(subscription.getId(), is(id));
        assertNull(subscription.getCardId());
        assertNull(subscription.getCard().getId());
        assertThat(subscription.getCard().getCardNumber(), is("4444"));
        assertThat(subscription.getTrialEndDate(), is(trialEndDateNoMinutes));
        assertThat(subscription.getCancelAtPeriodEnd(), is(false));

        subscription.setCardId(this.secondCard.getId());
        subscription.setCard(null);
        subscription = this.api.subscriptions().update(subscription);

        assertThat(subscription.getId(), is(id));
        assertThat(subscription.getCardId(), is(nullValue()));
        assertThat(subscription.getCard().getId(), is(this.secondCard.getId()));
        assertThat(subscription.getCard().getCardNumber(), is("4242"));
        assertThat(subscription.getTrialEndDate(), is(trialEndDateNoMinutes));
        assertThat(subscription.getCancelAtPeriodEnd(), is(false));
    }

    @Test
    public void testList() throws Exception {
        this.subscriptionsToDelete.add(this.api.subscriptions().create(this.customer.getId(), new Subscription()
                .planId(this.planWithTrial.getId()).cardId(this.card.getId())));
        this.subscriptionsToDelete.add(this.api.subscriptions().create(this.customer.getId(), new Subscription()
                .planId(this.planWithTrial.getId()).cardId(this.card.getId())));
        this.subscriptionsToDelete.add(this.api.subscriptions().create(this.customer.getId(), new Subscription()
                .planId(this.planWithTrial.getId()).cardId(this.card.getId())));
        this.subscriptionsToDelete.add(this.api.subscriptions().create(this.customer.getId(), new Subscription()
                .planId(this.planWithTrial.getId()).cardId(this.card.getId())));
        this.subscriptionsToDelete.add(this.api.subscriptions().create(this.customer.getId(), new Subscription()
                .planId(this.planWithTrial.getId()).cardId(this.card.getId())));
        List<Subscription> subscription = this.api.subscriptions().list(this.customer.getId(), null);
        assertThat(subscription.size(), is(5));
    }

    private Card getCard() {
        Card card = new Card()
                .cardNumber("5555555555554444")
                .holderName("Holder")
                .expirationMonth(12)
                .expirationYear(15)
                .cvv2("123")
                .address(TestUtils.prepareAddress());
        return card;
    }

}
