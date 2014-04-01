/*
 * Copyright 1999,2006 The Apache Software Foundation.
 *
 * This file was taken from the Jakarta Feedparser sources and was modified
 * to change the package and for formatting reasons.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package mx.openpay.client.serialization;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * ISO 8601 date parsing utility. <br>
 * Designed for parsing the ISO subset used in Dublin Core, RSS 1.0, and Atom.
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id: ISO8601DateParser.java 373572 2006-01-30 19:28:41Z mvdb $
 */
public class ISO8601DateParser {

    // 2004-06-14T19:GMT20:30Z
    // 2004-06-20T06:GMT22:01Z

    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssz", Locale.ENGLISH);

	/**
	 * ID to represent the 'GMT' string
	 */
	private static final String GMT_ID = "GMT";

	/**
	 * The GMT timezone
	 */
	private static final TimeZone TIMEZONE_GMT = TimeZone.getTimeZone(GMT_ID);

    // http://www.cl.cam.ac.uk/~mgk25/iso-time.html
    //
    // http://www.intertwingly.net/wiki/pie/DateTime
    //
    // http://www.w3.org/TR/NOTE-datetime
    //
    // Different standards may need different levels of granularity in the date and
    // time, so this profile defines six levels. Standards that reference this
    // profile should specify one or more of these granularities. If a given
    // standard allows more than one granularity, it should specify the meaning of
    // the dates and times with reduced precision, for example, the result of
    // comparing two dates with different precisions.

    // The formats are as follows. Exactly the components shown here must be
    // present, with exactly this punctuation. Note that the "T" appears literally
    // in the string, to indicate the beginning of the time element, as specified in
    // ISO 8601.

    // Year:
    // YYYY (eg 1997)
    // Year and month:
    // YYYY-MM (eg 1997-07)
    // Complete date:
    // YYYY-MM-DD (eg 1997-07-16)
    // Complete date plus hours and minutes:
    // YYYY-MM-DDThh:mmTZD (eg 1997-07-16T19:20+01:00)
    // Complete date plus hours, minutes and seconds:
    // YYYY-MM-DDThh:mm:ssTZD (eg 1997-07-16T19:20:30+01:00)
    // Complete date plus hours, minutes, seconds and a decimal fraction of a
    // second
    // YYYY-MM-DDThh:mm:ss.sTZD (eg 1997-07-16T19:20:30.45+01:00)

    // where:

    // YYYY = four-digit year
    // MM = two-digit month (01=January, etc.)
    // DD = two-digit day of month (01 through 31)
    // hh = two digits of hour (00 through 23) (am/pm NOT allowed)
    // mm = two digits of minute (00 through 59)
    // ss = two digits of second (00 through 59)
    // s = one or more digits representing a decimal fraction of a second
    // TZD = time zone designator (Z or +hh:mm or -hh:mm)
    public static Date parse(String input) throws java.text.ParseException {

        // NOTE: SimpleDateFormat uses GMT[-+]hh:mm for the TZ which breaks
        // things a bit. Before we go on we have to repair this.

        // this is zero time so we need to add that TZ indicator for
        if (input.endsWith("Z")) {
            input = input.substring(0, input.length() - 1) + "GMT-00:00";
        } else {
            int inset = 6;

            String s0 = input.substring(0, input.length() - inset);
            String s1 = input.substring(input.length() - inset, input.length());

            input = s0 + "GMT" + s1;
        }

        return df.parse(input);

    }

    public static String toString(final Date date) {

        TimeZone tz = TimeZone.getTimeZone("UTC");

        df.setTimeZone(tz);

        String output = df.format(date);

        int inset0 = 9;
        int inset1 = 6;

        String s0 = output.substring(0, output.length() - inset0);
        String s1 = output.substring(output.length() - inset1, output.length());

        String result = s0 + s1;

        result = result.replaceAll("UTC", "+00:00");

        return result;

    }

	/**
	 * Format a date into 'yyyy-MM-ddThh:mm:ss[.sss]Z' (GMT timezone)
	 * 
	 * @param date
	 *            the date to format
	 * @param millis
	 *            true to include millis precision otherwise false
	 * @return the date formatted as 'yyyy-MM-ddThh:mm:ss[.sss]Z'
	 */
	public static String format(final Date date) {
		return format(date, false, TIMEZONE_GMT);
	}

	/**
	 * Format date into yyyy-MM-ddThh:mm:ss[.sss][Z|[+-]hh:mm]
	 * 
	 * @param date
	 *            the date to format
	 * @param millis
	 *            true to include millis precision otherwise false
	 * @param tz
	 *            timezone to use for the formatting (GMT will produce 'Z')
	 * @return the date formatted as yyyy-MM-ddThh:mm:ss[.sss][Z|[+-]hh:mm]
	 */
	public static String format(final Date date, final boolean millis, final TimeZone tz) {
		Calendar calendar = new GregorianCalendar(tz, Locale.US);
		calendar.setTime(date);

		// estimate capacity of buffer as close as we can (yeah, that's pedantic
		// ;)
		int capacity = "yyyy-MM-ddThh:mm:ss".length();
		capacity += millis ? ".sss".length() : 0;
		capacity += tz.getRawOffset() == 0 ? "Z".length() : "+hh:mm".length();
		StringBuilder formatted = new StringBuilder(capacity);

		padInt(formatted, calendar.get(Calendar.YEAR), "yyyy".length());
		formatted.append('-');
		padInt(formatted, calendar.get(Calendar.MONTH) + 1, "MM".length());
		formatted.append('-');
		padInt(formatted, calendar.get(Calendar.DAY_OF_MONTH), "dd".length());
		formatted.append('T');
		padInt(formatted, calendar.get(Calendar.HOUR_OF_DAY), "hh".length());
		formatted.append(':');
		padInt(formatted, calendar.get(Calendar.MINUTE), "mm".length());
		formatted.append(':');
		padInt(formatted, calendar.get(Calendar.SECOND), "ss".length());
		if (millis) {
			formatted.append('.');
			padInt(formatted, calendar.get(Calendar.MILLISECOND), "sss".length());
		}

		int offset = tz.getOffset(calendar.getTimeInMillis());
		if (offset != 0) {
			int hours = Math.abs((offset / (60 * 1000)) / 60);
			int minutes = Math.abs((offset / (60 * 1000)) % 60);
			formatted.append(offset < 0 ? '-' : '+');
			padInt(formatted, hours, "hh".length());
			formatted.append(':');
			padInt(formatted, minutes, "mm".length());
		} else {
			formatted.append('Z');
		}

		return formatted.toString();
	}

	/**
	 * Zero pad a number to a specified length
	 * 
	 * @param buffer
	 *            buffer to use for padding
	 * @param value
	 *            the integer value to pad if necessary.
	 * @param length
	 *            the length of the string we should zero pad
	 */
	private static void padInt(final StringBuilder buffer, final int value, final int length) {
		String strValue = Integer.toString(value);
		for (int i = length - strValue.length(); i > 0; i--) {
			buffer.append('0');
		}
		buffer.append(strValue);
	}

    public static void main(final String[] args) throws Exception {

        System.out.println(parse("2004-05-31T09:19:31-06:00"));
        System.out.println(parse("2004-06-23T17:25:31-00:00"));
        System.out.println(parse("2004-06-23T17:25:31Z"));

        // 2002-10-02T10:00:00-05:00
		System.out.println("v: " + toString(new Date(System.currentTimeMillis())));
		System.out.println("v: " + toString(new Date(1396314300000L)));
    }

}
