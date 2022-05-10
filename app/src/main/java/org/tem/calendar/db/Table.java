package org.tem.calendar.db;

public class Table {

    public interface WeekData {
        String NAME = "WEEK_DATA";
        String COL_WEEK_DAY = "WEEK_DAY";
        String COL_RAAGHU = "RAAGHU";
        String COL_EMA = "EMA";
        String COL_KULIGAI = "KULIGAI";
        String COL_SOOLAM = "SOOLAM";
        String COL_PARIGARAM = "PARIGARAM";
        String COL_SOOLAM_TIME = "SOOLAM_TIME";
        String COL_KARANAN = "KARANAN";
    }

    public interface Master {
        String NAME = "MASTER";
        String COL_DATE = "DATE";
        String COL_YEAR = "YEAR";
        String COL_MONTH = "MONTH";
        String COL_DAY = "DAY";
        String COL_TYEAR = "TYEAR";
        String COL_TMONTH= "TMONTH";
        String COL_TDAY = "TDAY";
        String COL_WEEKDAY = "WEEKDAY";
    }

    public interface Panchangam {
        String NAME_GOWRI = "GOWRI_PANCHANGAM";
        String NAME_ORAI = "GRAHA_ORAI_PANCHANGAM";
        String COL_WEEKDAY = "WEEKDAY";
        String COL_MORNING_INDEX = "MORNING_INDEX";
        String COL_TIME_INDEX = "TIME_INDEX";
        String COL_EVENING_INDEX = "EVENING_INDEX";
    }

    public interface Muhurtham {
        String NAME = "MUHURTHAM";
        String COL_DATE = "DATE";
        String COL_THITI = "THITI";
        String COL_STAR = "STAR";
        String COL_YOGAM = "YOGAM";
        String COL_TIME = "TIME";
        String COL_LAKNAM = "LAKNAM";
    }
}
