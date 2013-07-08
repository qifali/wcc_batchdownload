package com.qifa;

import intradoc.common.ExecutionContext;

import intradoc.data.DataBinder;
import intradoc.data.Workspace;

import intradoc.shared.FilterImplementor;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

public class CleanFilter implements FilterImplementor {
    public int doFilter(Workspace workspace, DataBinder dataBinder,
                        ExecutionContext executionContext) {
        String cleanTime = dataBinder.getEnvironmentValue("cleanTime");
        boolean flag = isTimeSendEmail(cleanTime);
        if (flag) {
            FileUtil.delAllFile(dataBinder.getEnvironmentValue("Domaindri") +
                                "ucm\\cs\\weblayout\\resources\\temp");
        }

        return 0;
    }

    public static boolean isTimeSendEmail(String time) {
        Date b = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String bs = sdf1.format(b);
        String bsN = bs + " " + time + ":00:00";
        String bsNe = bs + " " + time + ":05:00";
        try {
            Date d1 = sdf2.parse(bsN);
            Date d2 = sdf2.parse(bsNe);
            if ((d1.getTime() < System.currentTimeMillis()) &&
                (d2.getTime() > System.currentTimeMillis())) {
                return true;
            }
        } catch (ParseException e) {
            return false;
        }
        return false;
    }
}
