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
package mx.openpay.client.serialization;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonPrimitive;

/**
 * @author elopez
 */
@Slf4j
public class DateFormatDeserializerTest {

    private DateFormatDeserializer deserializer;

    @Before
    public void setUp() throws Exception {
        this.deserializer = new DateFormatDeserializer();
        TimeZone.setDefault(TimeZone.getTimeZone("Mexico/General"));
    }

    @Test
    public void testParse() throws Exception {
        Date expected = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-01-06 13:14:09");
        Date date = this.parse("2014-01-06T13:14:09-06:00");
        assertEquals(expected, date);
    }

    @Test
    public void testParse_12Hours() throws Exception {
        Date expected = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-01-06 12:14:09");
        Date date = this.parse("2014-01-06T12:14:09-06:00");
        assertEquals(expected, date);
    }

    @Test
    public void testParse_ZeroHours() throws Exception {
        Date expected = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-01-06 00:14:09");
        Date date = this.parse("2014-01-06T00:14:09-06:00");
        assertEquals(expected, date);
    }

    @Test
    public void testParseDifferentZone() throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Date expected = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-01-06 19:14:09");
        Date date = this.parse("2014-01-06T13:14:09-06:00");
        assertEquals(expected, date);
    }

    private Date parse(final String date) throws ParseException {
        JsonPrimitive primitive = new JsonPrimitive(date);
        return this.deserializer.deserialize(primitive, Date.class, null);
    }

    @Test
    public void testMultithread() throws Exception {
        int totalThreads = 20;
        ParserThread[] threads = new ParserThread[totalThreads];
        CyclicBarrier barrier = new CyclicBarrier(totalThreads);
        CountDownLatch latch = new CountDownLatch(totalThreads);
        for (int i = 0; i < totalThreads; i++) {
            threads[i] = new ParserThread("2014-01-06T13:14:20-06:00", barrier, latch);
            new Thread(threads[i]).start();
        }
        latch.await(10, TimeUnit.SECONDS);
        Date expected = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-01-06 13:14:20");
        for (ParserThread thread : threads) {
            assertEquals(expected, thread.result);
        }
    }

    private class ParserThread implements Runnable {

        Date result;

        String dateToParse;

        CyclicBarrier barrier;

        CountDownLatch latch;

        ParserThread(final String date, final CyclicBarrier barrier, final CountDownLatch latch) {
            this.dateToParse = date;
            this.barrier = barrier;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                this.barrier.await(10, TimeUnit.SECONDS);
                this.result = DateFormatDeserializerTest.this.parse(this.dateToParse);
            } catch (Exception e) {
                log.error("error", e);
            } finally {
                this.latch.countDown();
            }
        }

    }

}
