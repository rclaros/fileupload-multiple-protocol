package org.sotech.fileupload.util;

import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.PeriodType;

public class DateUtils {

    public static final DateTimeZone dateTimeZone = DateTimeZone.forID("America/Lima");

    public static int getYear(Date base) {
        DateTime aux = new DateTime(base);
        if (aux != null) {
            return aux.getYear();
        }
        return 0;
    }

    public static DateTime getDateTime() {
        DateTime d = new DateTime(dateTimeZone);
        return d;
    }

    public static Date getToday() {
        DateTime d = new DateTime(dateTimeZone);
        return d.toDate();
    }

    public static Date getDateforMiliseconds(String mil) {
        Date d = null;
        if (mil != null) {
            String nwmil = mil.trim();
            try {
                long m = Long.parseLong(nwmil);
                DateTime date = new DateTime(m, dateTimeZone);
                d = date.toDate();
            } catch (Exception e) {
                d = getToday();
            }
        }
        return d;
    }

    public static Date plusMinutes(Date date, int num) {
        if (date != null) {
            DateTime dateTime = getDateTime();
            dateTime = dateTime.plusMinutes(num);
            return dateTime.toDate();
        } else {
            return null;
        }

    }

    public static Date plusDays(Date date, int num) {
        if (date != null) {
            DateTime dateTime = new DateTime(date);
            dateTime = dateTime.plusDays(num);
            return dateTime.toDate();
        } else {
            return null;
        }
    }
   public static DateTime minusDays(Date date, int num) {
        if (date != null) {
            DateTime dateTime = new DateTime(date);
            dateTime = dateTime.minusDays(num);
            return dateTime;
        } else {
            return null;
        }
    }

    public static DateTime plusDays(DateTime date, int num) {
        if (date != null) {
            DateTime dateTime = new DateTime(date);
            dateTime = dateTime.plusDays(num);
            return dateTime;
        } else {
            return null;
        }
    }

    public static DateTime plusHours(DateTime date, int num) {
        if (date != null) {
            DateTime dateTime = new DateTime(date);
            dateTime = dateTime.plusHours(num);
            return dateTime;
        } else {
            return null;
        }
    }

    /**
     *
     * @param dateStart
     * @return
     */
    public static int getDays(Date dateStart) {
        if (dateStart != null) {
            DateTime dateTime = new DateTime(dateStart);
            DateTime today = getDateTime();
            Instant instToDay = today.toInstant();
            Instant instToStart = dateTime.toInstant();
            if (instToDay.getMillis() > instToStart.getMillis()) {
                Interval intervalTask = new Interval(instToStart, instToDay);
                Period period = intervalTask.toPeriod(PeriodType.dayTime());
                return period.getDays();
            }
        }
        return 0;
    }
}
