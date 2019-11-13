package com.telekom.m2m.cot.restsdk.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateTimeUtilTest {
    private String getTimeOffsetStringFor(DateTime dt) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("ZZ");
        return dtf.print(dt);
    }

    private String getLocalTimeOffsetString() {
        return getTimeOffsetStringFor( DateTime.now() );
    }

    @Test
    public void testShouldSupportPatterns() {
        final String oneLetterString1 = "2017-09-05T15:19:32.601Z";
        final String oneLetterString2 = "2017-09-05T17:19:32.601+02";
        final String twoLetterString = "2017-09-05T17:19:32.601+0200";
        final String threeLetterString = "2017-09-05T17:19:32.601+02:00";

        final DateTime expected =
                new DateTime(2017, 9, 5, 17, 19, 32, 601, DateTimeZone.forOffsetHours(2));

        DateTime res1 = DateTimeUtil.parseDateTime(oneLetterString1);
        DateTime res2 = DateTimeUtil.parseDateTime(oneLetterString2);

        DateTime res3 = DateTimeUtil.parseDateTime(twoLetterString);

        DateTime res4 = DateTimeUtil.parseDateTime(threeLetterString);

        Assert.assertEquals(res1.getChronology().hours().getUnitMillis(), expected.getChronology().hours().getUnitMillis());

        Assert.assertEquals(res1.toLocalDateTime(), expected.toLocalDateTime());
        Assert.assertEquals(res2.toLocalDateTime(), expected.toLocalDateTime());
        Assert.assertEquals(res3.toLocalDateTime(), expected.toLocalDateTime());
        Assert.assertEquals(res4.toLocalDateTime(), expected.toLocalDateTime());
    }

    @Test
    public void testShouldRecognizeWrongPattern() {
        final String wrongDate1 = "42";
        final String wrongDate2 = "2017 i was an infant";
        final String wrongDate3 = "2017-09-05T15:19:32.601";
        final String wrongDate4 = "2017-09-05T15:19:32z";
        final String wrongDate5 = "2017-09-05 15:19:32.601Z";
        final String wrongDate6 = "2017-09-05x15:19:32.601Z";
        final String wrongDate7 = "2017-09-05T15:19Z";

        final List<String> wrongDates = new ArrayList<String>() {{
            add(wrongDate1); add(wrongDate2); add(wrongDate3); add(wrongDate4);
            add(wrongDate5); add(wrongDate6); add(wrongDate7);
        }};

        for (String wrongDate : wrongDates) {
            try {
                DateTimeUtil.parseDateTime(wrongDate);
                Assert.fail(String.format("parser should throw exception for '%s'", wrongDate));
            } catch (IllegalArgumentException e) {
                // it is cool!
            }
        }
    }

    @Test
    public void testSerializeDatetimeProperty() {
        final DateTime fifthApril_2005_100845_487 = new DateTime(
                2005, 4, 5,
                10, 8,45,487);

        Assert.assertEquals(
            DateTimeUtil.convertDateToString(fifthApril_2005_100845_487.toDate()),
                "2005-04-05T10:08:45.487" + getTimeOffsetStringFor(fifthApril_2005_100845_487));
    }

    @Test
    public void testSerializeNow() {
        final Date now = new Date();

        final String res = DateTimeUtil.convertDateToString(now);

        Assert.assertTrue( res.endsWith(getLocalTimeOffsetString()),
                String.format("Expecting %s at the end of '%s', converted date: %s", getLocalTimeOffsetString(), res, now.toString()));
    }

    @Test
    public void shouldHanleProperlyDatesWithoutTimeZone() throws ParseException {
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String nominalDateStr = "12-12-2012 12:12:12";
        String beforeDateStr = "11-12-2012 12:12:12";
        String afterDateStr = "13-12-2012 12:12:12";
        final Date date1 = dateformat.parse(nominalDateStr);
        final Date date2 = dateformat.parse(beforeDateStr);
        final Date date3 = dateformat.parse(afterDateStr);
        System.out.println(date1);

        final String res1 = DateTimeUtil.convertDateToString(date1);
        final String res2 = DateTimeUtil.convertDateToString(date2);
        final String res3 = DateTimeUtil.convertDateToString(date3);

        Assert.assertTrue( res1.endsWith( getLocalTimeOffsetString() ),
                String.format("Expecting suffix '%s' in '%s'", getLocalTimeOffsetString(), res1) );
        Assert.assertTrue( DateTimeUtil.convertDateToString(date2).endsWith( getLocalTimeOffsetString() ) ,
                String.format("Expecting suffix '%s' in '%s'", getLocalTimeOffsetString(), res2) );
        Assert.assertTrue( DateTimeUtil.convertDateToString(date3).endsWith( getLocalTimeOffsetString() ) ,
                String.format("Expecting suffix '%s' in '%s'", getLocalTimeOffsetString(), res3) );
    }
}
