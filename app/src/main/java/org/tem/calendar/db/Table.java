package org.tem.calendar.db;

public class Table {

    public interface Rasi {
        String NAME = "DAY_RASI";
        String COL_DATE = "DATE";
        String COL_MESHAM = "MESHAM";
        String COL_RISHABAM = "RISHABAM";
        String COL_MITHUNAM = "MITHUNAM";
        String COL_KADAGAM = "KADAGAM";
        String COL_SIMMAM = "SIMMAM";
        String COL_KANNI = "KANNI";
        String COL_THULAM = "THULAM";
        String COL_VIRUCHAGAM = "VIRUCHAGAM";
        String COL_DHANUSU = "DHANUSU";
        String COL_MAGARAM = "MAGARAM";
        String COL_KUMBAM = "KUMBAM";
        String COL_MEENAM = "MEENAM";
    }

    public interface Yogam {
        String NAME = "DAY_YOGAM";
        String COL_DATE = "DATE";
        String COL_TIME1 = "TIME1";
        String COL_TIME2 = "TIME2";
        String COL_YOGAM1 = "YOGAM1";
        String COL_YOGAM2 = "YOGAM2";
        String COL_YOGAM3 = "YOGAM3";
        String COL_YOGAM = "YOGAM";
    }

    public interface Karanam {
        String NAME = "DAY_KARANAM";
        String COL_DATE = "DATE";
        String COL_TIME1 = "TIME1";
        String COL_TIME2 = "TIME2";
        String COL_TIME3 = "TIME3";
        String COL_KARA1 = "KARA1";
        String COL_KARA2 = "KARA2";
        String COL_KARA3 = "KARA3";
        String COL_KARA4 = "KARA4";
    }

    public interface Star {
        String NAME = "DAY_STAR";
        String COL_DATE = "DATE";
        String COL_TIME1 = "TIME1";
        String COL_TIME2 = "TIME2";
        String COL_STAR1 = "STAR1";
        String COL_STAR2 = "STAR2";
        String COL_STAR3 = "STAR3";
        String COL_STAR = "STAR";
        String COL_NOKKU = "NOKKU";
        String COL_CHANDRASTAMAM = "CHANDRASTAMAM";
    }

    public interface Thiti {
        String NAME = "DAY_THITI";
        String COL_DATE = "DATE";
        String COL_TIME1 = "TIME1";
        String COL_TIME2 = "TIME2";
        String COL_THITI1 = "THITI1";
        String COL_THITI2 = "THITI2";
        String COL_THITI3 = "THITI3";
        String COL_THITI = "THITI";
        String COL_PIRAI = "PIRAI";
    }

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
        String COL_TMONTH = "TMONTH";
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

    public interface NallaNeram {
        String NAME = "NALLA_NERAM";
        String COL_DATE = "DATE";
        String COL_NALLA_M = "NALLA_M";
        String COL_NALLA_E = "NALLA_E";
        String COL_GOWRI_M = "GOWRI_M";
        String COL_GOWRI_E = "GOWRI_E";
        String COL_LAKNAM = "LAKNAM";
        String COL_LAKNAM_TIME = "LAKNAM_TIME";
        String COL_SUN_RISE = "SUN_RISE";
    }
}
