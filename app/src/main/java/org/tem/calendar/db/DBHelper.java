package org.tem.calendar.db;

import android.annotation.SuppressLint;
import android.content.Context;

import net.sqlcipher.Cursor;
import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import org.tem.calendar.custom.DateUtil;
import org.tem.calendar.custom.StringUtils;
import org.tem.calendar.model.FestivalDayData;
import org.tem.calendar.model.KaranamData;
import org.tem.calendar.model.KuralData;
import org.tem.calendar.model.MonthData;
import org.tem.calendar.model.MuhurthamData;
import org.tem.calendar.model.NallaNeramData;
import org.tem.calendar.model.PalliPalanData;
import org.tem.calendar.model.PanchangamData;
import org.tem.calendar.model.RasiChartData;
import org.tem.calendar.model.RasiData;
import org.tem.calendar.model.StarData;
import org.tem.calendar.model.StarMatching;
import org.tem.calendar.model.StarsData;
import org.tem.calendar.model.ThitiData;
import org.tem.calendar.model.VasthuData;
import org.tem.calendar.model.VirathamData;
import org.tem.calendar.model.VirathamMonthData;
import org.tem.calendar.model.WeekData;
import org.tem.calendar.model.YogamData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 5;
    private static final String DB_NAME = "tamizh_calendar.db";
    @SuppressLint("StaticFieldLeak")
    private static DBHelper dbHelper;
    private final File DB_FILE;
    private final Context mContext;
    private SQLiteDatabase mDatabase;

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        DB_FILE = context.getDatabasePath(DB_NAME);
        this.mContext = context;

        createDataBase();
        getReadableDatabase();
        if (mDatabase.getVersion() != DB_VERSION) {
            mContext.deleteDatabase(DB_NAME);
            mDatabase = null;
        }
        createDataBase();
    }

    public static DBHelper getInstance(Context context) {
        if (null == dbHelper) {
            dbHelper = new DBHelper(context);
        }

        return dbHelper;
    }

    public void createDataBase() {
        // If the database does not exist, copy it from the assets.
        boolean mDataBaseExist = checkDataBase();
        if (!mDataBaseExist) {
            this.getReadableDatabase("123");
            this.close();
            try {
                // Copy the database from assets
                copyDataBase();
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    // Check that the database file exists in databases folder
    private boolean checkDataBase() {
        return DB_FILE.exists();
    }

    // Copy the database from assets
    private void copyDataBase() throws IOException {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        OutputStream mOutput = new FileOutputStream(DB_FILE);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    // Open the database, so we can query it
    public SQLiteDatabase getReadableDatabase() throws SQLException {

        if (null == mDatabase) {
            // Log.v("DB_PATH", DB_FILE.getAbsolutePath());
            mDatabase = SQLiteDatabase.openDatabase(DB_FILE.getPath(), "123", null, SQLiteDatabase.OPEN_READWRITE);
            // mDataBase = SQLiteDatabase.openDatabase(DB_FILE, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        }
        return mDatabase;
    }

    @Override
    public synchronized void close() {
        if (mDatabase != null) {
            mDatabase.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        checkDataBase();
    }

    public WeekData getWeekData(DayOfWeek dayOfWeek) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor c = db.query(Table.WeekData.NAME, null, Table.WeekData.COL_WEEK_DAY + "= ?", new String[]{dayOfWeek.getValue() + ""}, null, null, null);
            if (c != null && c.moveToFirst()) {
                WeekData data = new WeekData();
                data.setDayOfWeek(dayOfWeek);
                data.setRaaghu(c.getString(c.getColumnIndexOrThrow(Table.WeekData.COL_RAAGHU)));
                data.setEma(c.getString(c.getColumnIndexOrThrow(Table.WeekData.COL_EMA)));
                data.setKuligai(c.getString(c.getColumnIndexOrThrow(Table.WeekData.COL_KULIGAI)));
                data.setSoolam(c.getInt(c.getColumnIndexOrThrow(Table.WeekData.COL_SOOLAM)));
                data.setParigaram(c.getInt(c.getColumnIndexOrThrow(Table.WeekData.COL_PARIGARAM)));
                data.setSoolamTime(c.getInt(c.getColumnIndexOrThrow(Table.WeekData.COL_SOOLAM_TIME)));
                data.setKaranan(c.getString(c.getColumnIndexOrThrow(Table.WeekData.COL_KARANAN)));
                return data;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<MonthData> getDates(int year, int month) {
        SQLiteDatabase db = getReadableDatabase();
        List<MonthData> monthData = new ArrayList<>();
        Cursor c = db.query(Table.Master.NAME, null, "YEAR = ? AND MONTH = ?", new String[]{year + "", month + ""}, null, null, null);

        if (c.moveToFirst()) {
            do {
                MonthData data = new MonthData();
                data.setDate(c.getString(c.getColumnIndexOrThrow(Table.Master.COL_DATE)));
                data.setYear(c.getInt(c.getColumnIndexOrThrow(Table.Master.COL_YEAR)));
                data.setMonth(c.getInt(c.getColumnIndexOrThrow(Table.Master.COL_MONTH)));
                data.setDay(c.getInt(c.getColumnIndexOrThrow(Table.Master.COL_DAY)));
                data.setTyear(c.getInt(c.getColumnIndexOrThrow(Table.Master.COL_TYEAR)));
                data.setTmonth(c.getInt(c.getColumnIndexOrThrow(Table.Master.COL_TMONTH)));
                data.setTday(c.getInt(c.getColumnIndexOrThrow(Table.Master.COL_TDAY)));
                data.setWeekday(c.getInt(c.getColumnIndexOrThrow(Table.Master.COL_WEEKDAY)));
                monthData.add(data);
            } while (c.moveToNext());
        }

        return monthData;
    }

    public List<PanchangamData> getGowriPanchangamWeekData(DayOfWeek dayOfWeek) {
        SQLiteDatabase db = getReadableDatabase();
        List<PanchangamData> dataList = new ArrayList<>();
        Cursor c = db.query(Table.Panchangam.NAME_GOWRI, null, Table.Panchangam.COL_WEEKDAY + "=?",
                new String[]{dayOfWeek.getValue() + ""}, null, null, null);
        if (c != null && c.moveToFirst()) {
            do {
                PanchangamData gd = new PanchangamData(dayOfWeek);
                gd.setTimeIndex(c.getInt(c.getColumnIndexOrThrow(Table.Panchangam.COL_TIME_INDEX)));
                gd.setMorningIndex(c.getInt(c.getColumnIndexOrThrow(Table.Panchangam.COL_MORNING_INDEX)));
                gd.setEveningIndex(c.getInt(c.getColumnIndexOrThrow(Table.Panchangam.COL_EVENING_INDEX)));
                dataList.add(gd);
            } while (c.moveToNext());
        }
        return dataList;
    }

    public List<PanchangamData> getGrahaOraiPanchangamWeekData(DayOfWeek dayOfWeek) {
        SQLiteDatabase db = getReadableDatabase();
        List<PanchangamData> dataList = new ArrayList<>();
        Cursor c = db.query(Table.Panchangam.NAME_ORAI, null, Table.Panchangam.COL_WEEKDAY + "=?",
                new String[]{dayOfWeek.getValue() + ""}, null, null, null);
        if (c != null && c.moveToFirst()) {
            do {
                PanchangamData gd = new PanchangamData(dayOfWeek);
                gd.setTimeIndex(c.getInt(c.getColumnIndexOrThrow(Table.Panchangam.COL_TIME_INDEX)));
                gd.setMorningIndex(c.getInt(c.getColumnIndexOrThrow(Table.Panchangam.COL_MORNING_INDEX)));
                gd.setEveningIndex(c.getInt(c.getColumnIndexOrThrow(Table.Panchangam.COL_EVENING_INDEX)));
                dataList.add(gd);
            } while (c.moveToNext());
        }
        return dataList;
    }

    public List<MuhurthamData> getMuhurthamList(int year, int month) {
        SQLiteDatabase db = getReadableDatabase();
        String qry = "SELECT mm.DATE as DATE, TMONTH, TDAY, thiti,star,yogam,time,laknam, PIRAI FROM MUHURTHAM m, MASTER mm where m.date=mm.date and mm.year=? and mm.MONTH=?";
        List<MuhurthamData> mdList = new ArrayList<>();

        Cursor c = db.rawQuery(qry, new String[]{year + "", month + ""});
        if (c != null && c.moveToFirst()) {
            do {
                MuhurthamData md = new MuhurthamData(c.getString(c.getColumnIndexOrThrow(Table.Muhurtham.COL_DATE)));
                md.setTday(c.getInt(c.getColumnIndexOrThrow(Table.Master.COL_TDAY)));
                md.setTmonth(c.getInt(c.getColumnIndexOrThrow(Table.Master.COL_TMONTH)));
                md.setYogam(c.getInt(c.getColumnIndexOrThrow(Table.Muhurtham.COL_YOGAM)));
                md.setThiti(c.getInt(c.getColumnIndexOrThrow(Table.Muhurtham.COL_THITI)));
                md.setStar(c.getInt(c.getColumnIndexOrThrow(Table.Muhurtham.COL_STAR)));
                md.setTime(c.getString(c.getColumnIndexOrThrow(Table.Muhurtham.COL_TIME)));
                md.setLaknam(c.getInt(c.getColumnIndexOrThrow(Table.Muhurtham.COL_LAKNAM)));
                md.setPirai(c.getInt(c.getColumnIndexOrThrow(Table.Muhurtham.COL_PIRAI)));
                mdList.add(md);
            } while (c.moveToNext());
        }
        return mdList;
    }

    public MonthData getDate(LocalDate date) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(Table.Master.NAME, null,
                Table.Master.COL_YEAR + "=? AND " +
                        Table.Master.COL_MONTH + "=? AND " +
                        Table.Master.COL_DAY + "=?",
                new String[]{date.getYear() + "",
                        date.getMonthValue() + "",
                        date.getDayOfMonth() + ""},
                null, null, null
        );

        MonthData md;

        if (null != c && c.moveToFirst()) {
            md = new MonthData();
            md.setDate(c.getString(c.getColumnIndexOrThrow(Table.Master.COL_DATE)));
            md.setDay(c.getInt(c.getColumnIndexOrThrow(Table.Master.COL_DAY)));
            md.setMonth(c.getInt(c.getColumnIndexOrThrow(Table.Master.COL_MONTH)));
            md.setYear(c.getInt(c.getColumnIndexOrThrow(Table.Master.COL_YEAR)));
            md.setWeekday(c.getInt(c.getColumnIndexOrThrow(Table.Master.COL_WEEKDAY)));
            md.setTyear(c.getInt(c.getColumnIndexOrThrow(Table.Master.COL_TYEAR)));
            md.setTmonth(c.getInt(c.getColumnIndexOrThrow(Table.Master.COL_TMONTH)));
            md.setTday(c.getInt(c.getColumnIndexOrThrow(Table.Master.COL_TDAY)));
        } else {
            md = new MonthData();
            md.setDate(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            md.setYear(date.getYear());
            md.setMonth(date.getMonthValue());
            md.setDay(date.getDayOfMonth());
            md.setWeekday(date.getDayOfWeek().getValue());
        }

        return md;
    }

    public NallaNeramData getNallaNeram(String date) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(Table.NallaNeram.NAME, null, Table.NallaNeram.COL_DATE + "=?",
                new String[]{date}, null, null, null);
        NallaNeramData nnd = null;
        if (c != null && c.moveToFirst()) {
            nnd = new NallaNeramData(date);
            nnd.setNallaNeramM(c.getString(c.getColumnIndexOrThrow(Table.NallaNeram.COL_NALLA_M)));
            nnd.setNallaNeramE(c.getString(c.getColumnIndexOrThrow(Table.NallaNeram.COL_NALLA_E)));
            nnd.setGowriM(c.getString(c.getColumnIndexOrThrow(Table.NallaNeram.COL_GOWRI_M)));
            nnd.setGowriE(c.getString(c.getColumnIndexOrThrow(Table.NallaNeram.COL_GOWRI_E)));
            nnd.setLaknam(c.getInt(c.getColumnIndexOrThrow(Table.NallaNeram.COL_LAKNAM)));
            nnd.setLaknamTime(c.getString(c.getColumnIndexOrThrow(Table.NallaNeram.COL_LAKNAM_TIME)));
            nnd.setSunRise(c.getString(c.getColumnIndexOrThrow(Table.NallaNeram.COL_SUN_RISE)));
        }
        return nnd;
    }

    public ThitiData getThiti(String dateString) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(Table.Thiti.NAME, null, Table.Thiti.COL_DATE + "=?",
                new String[]{dateString}, null, null, null);
        if (null != c && c.moveToFirst()) {
            ThitiData td = new ThitiData(dateString);
            td.setTime1(c.getString(c.getColumnIndexOrThrow(Table.Thiti.COL_TIME1)));
            td.setTime2(c.getString(c.getColumnIndexOrThrow(Table.Thiti.COL_TIME2)));
            td.setThiti1(c.getInt(c.getColumnIndexOrThrow(Table.Thiti.COL_THITI1)));
            td.setThiti2(c.getInt(c.getColumnIndexOrThrow(Table.Thiti.COL_THITI2)));
            td.setThiti3(c.getInt(c.getColumnIndexOrThrow(Table.Thiti.COL_THITI3)));
            td.setThiti(c.getInt(c.getColumnIndexOrThrow(Table.Thiti.COL_THITI)));
            td.setPirai(c.getInt(c.getColumnIndexOrThrow(Table.Thiti.COL_PIRAI)));
            return td;

        }
        return null;
    }

    public StarData getStar(String dateString) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(Table.Star.NAME, null,
                Table.Star.COL_DATE + "=?",
                new String[]{dateString}, null, null, null);

        if (null != c && c.moveToFirst()) {
            StarData sd = new StarData(dateString);
            sd.setTime1(c.getString(c.getColumnIndexOrThrow(Table.Star.COL_TIME1)));
            sd.setTime2(c.getString(c.getColumnIndexOrThrow(Table.Star.COL_TIME2)));
            sd.setStar1(c.getInt(c.getColumnIndexOrThrow(Table.Star.COL_STAR1)));
            sd.setStar2(c.getInt(c.getColumnIndexOrThrow(Table.Star.COL_STAR2)));
            sd.setStar3(c.getInt(c.getColumnIndexOrThrow(Table.Star.COL_STAR3)));
            sd.setStar(c.getInt(c.getColumnIndexOrThrow(Table.Star.COL_STAR)));
            sd.setNokku(c.getInt(c.getColumnIndexOrThrow(Table.Star.COL_NOKKU)));
            sd.setChandrastamam(c.getString(c.getColumnIndexOrThrow(Table.Star.COL_CHANDRASTAMAM)));
            return sd;
        }
        return null;
    }

    public KaranamData getKaranam(String dateString) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(Table.Karanam.NAME, null,
                Table.Karanam.COL_DATE + "=?",
                new String[]{dateString}, null, null, null);
        if (null != c && c.moveToFirst()) {
            KaranamData kd = new KaranamData(dateString);
            kd.setTime1(c.getString(c.getColumnIndexOrThrow(Table.Karanam.COL_TIME1)));
            kd.setTime2(c.getString(c.getColumnIndexOrThrow(Table.Karanam.COL_TIME2)));
            kd.setTime3(c.getString(c.getColumnIndexOrThrow(Table.Karanam.COL_TIME3)));
            kd.setKara1(c.getInt(c.getColumnIndexOrThrow(Table.Karanam.COL_KARA1)));
            kd.setKara2(c.getInt(c.getColumnIndexOrThrow(Table.Karanam.COL_KARA2)));
            kd.setKara3(c.getInt(c.getColumnIndexOrThrow(Table.Karanam.COL_KARA3)));
            kd.setKara4(c.getInt(c.getColumnIndexOrThrow(Table.Karanam.COL_KARA4)));
            return kd;
        }
        return null;
    }

    public YogamData getYogam(String dateString) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(Table.Yogam.NAME, null,
                Table.Yogam.COL_DATE + "=?",
                new String[]{dateString}, null, null, null);
        if (null != c && c.moveToFirst()) {
            YogamData yd = new YogamData(dateString);
            yd.setTime1(c.getString(c.getColumnIndexOrThrow(Table.Yogam.COL_TIME1)));
            yd.setTime2(c.getString(c.getColumnIndexOrThrow(Table.Yogam.COL_TIME2)));
            yd.setYogam1(c.getInt(c.getColumnIndexOrThrow(Table.Yogam.COL_YOGAM1)));
            yd.setYogam2(c.getInt(c.getColumnIndexOrThrow(Table.Yogam.COL_YOGAM2)));
            yd.setYogam3(c.getInt(c.getColumnIndexOrThrow(Table.Yogam.COL_YOGAM3)));
            yd.setYogam(c.getInt(c.getColumnIndexOrThrow(Table.Yogam.COL_YOGAM)));
            return yd;
        }
        return null;
    }

    public RasiData getRasi(String dateString) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(Table.Rasi.NAME, null,
                Table.Rasi.COL_DATE + "=?",
                new String[]{dateString}, null, null, null);
        if (null != c && c.moveToFirst()) {
            RasiData rd = new RasiData(dateString);
            rd.setMesham(c.getInt(c.getColumnIndexOrThrow(Table.Rasi.COL_MESHAM)));
            rd.setRishabam(c.getInt(c.getColumnIndexOrThrow(Table.Rasi.COL_RISHABAM)));
            rd.setMithunam(c.getInt(c.getColumnIndexOrThrow(Table.Rasi.COL_MITHUNAM)));
            rd.setKadagam(c.getInt(c.getColumnIndexOrThrow(Table.Rasi.COL_KADAGAM)));
            rd.setSimmam(c.getInt(c.getColumnIndexOrThrow(Table.Rasi.COL_SIMMAM)));
            rd.setKanni(c.getInt(c.getColumnIndexOrThrow(Table.Rasi.COL_KANNI)));
            rd.setThulam(c.getInt(c.getColumnIndexOrThrow(Table.Rasi.COL_THULAM)));
            rd.setViruchagam(c.getInt(c.getColumnIndexOrThrow(Table.Rasi.COL_VIRUCHAGAM)));
            rd.setDhanusu(c.getInt(c.getColumnIndexOrThrow(Table.Rasi.COL_DHANUSU)));
            rd.setMagaram(c.getInt(c.getColumnIndexOrThrow(Table.Rasi.COL_MAGARAM)));
            rd.setKumbam(c.getInt(c.getColumnIndexOrThrow(Table.Rasi.COL_KUMBAM)));
            rd.setMeenam(c.getInt(c.getColumnIndexOrThrow(Table.Rasi.COL_MEENAM)));
            return rd;
        }
        return null;
    }

    public RasiChartData getRasiChart(String dateString) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(Table.RasiChart.NAME, null,
                Table.RasiChart.COL_DATE + "=?",
                new String[]{dateString}, null, null, null);

        if (null != c && c.moveToFirst()) {
            RasiChartData rcd = new RasiChartData(dateString);
            rcd.setH1(c.getString(c.getColumnIndexOrThrow(Table.RasiChart.COL_H1)));
            rcd.setH2(c.getString(c.getColumnIndexOrThrow(Table.RasiChart.COL_H2)));
            rcd.setH3(c.getString(c.getColumnIndexOrThrow(Table.RasiChart.COL_H3)));
            rcd.setH4(c.getString(c.getColumnIndexOrThrow(Table.RasiChart.COL_H4)));
            rcd.setH5(c.getString(c.getColumnIndexOrThrow(Table.RasiChart.COL_H5)));
            rcd.setH6(c.getString(c.getColumnIndexOrThrow(Table.RasiChart.COL_H6)));
            rcd.setH7(c.getString(c.getColumnIndexOrThrow(Table.RasiChart.COL_H7)));
            rcd.setH8(c.getString(c.getColumnIndexOrThrow(Table.RasiChart.COL_H8)));
            rcd.setH9(c.getString(c.getColumnIndexOrThrow(Table.RasiChart.COL_H9)));
            rcd.setH10(c.getString(c.getColumnIndexOrThrow(Table.RasiChart.COL_H10)));
            rcd.setH11(c.getString(c.getColumnIndexOrThrow(Table.RasiChart.COL_H11)));
            rcd.setH12(c.getString(c.getColumnIndexOrThrow(Table.RasiChart.COL_H12)));
            rcd.setC1(c.getString(c.getColumnIndexOrThrow(Table.RasiChart.COL_C1)));
            rcd.setC2(c.getString(c.getColumnIndexOrThrow(Table.RasiChart.COL_C2)));
            rcd.setC3(c.getString(c.getColumnIndexOrThrow(Table.RasiChart.COL_C3)));
            rcd.setC4(c.getString(c.getColumnIndexOrThrow(Table.RasiChart.COL_C4)));
            rcd.setC5(c.getString(c.getColumnIndexOrThrow(Table.RasiChart.COL_C5)));
            rcd.setC6(c.getString(c.getColumnIndexOrThrow(Table.RasiChart.COL_C6)));
            rcd.setC7(c.getString(c.getColumnIndexOrThrow(Table.RasiChart.COL_C7)));
            return rcd;
        }

        return null;

    }

    public MuhurthamData getMuhurtham(String dateString) {

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(Table.Muhurtham.NAME, null,
                Table.Muhurtham.COL_DATE + "=?",
                new String[]{dateString}, null, null, null);
        if (null != c && c.moveToFirst()) {
            MuhurthamData md = new MuhurthamData(c.getString(c.getColumnIndexOrThrow(Table.Muhurtham.COL_DATE)));
            //md.setTday(c.getInt(c.getColumnIndexOrThrow(Table.Master.COL_TDAY)));
            //md.setTmonth(c.getInt(c.getColumnIndexOrThrow(Table.Master.COL_TMONTH)));
            md.setYogam(c.getInt(c.getColumnIndexOrThrow(Table.Muhurtham.COL_YOGAM)));
            md.setThiti(c.getInt(c.getColumnIndexOrThrow(Table.Muhurtham.COL_THITI)));
            md.setStar(c.getInt(c.getColumnIndexOrThrow(Table.Muhurtham.COL_STAR)));
            md.setTime(c.getString(c.getColumnIndexOrThrow(Table.Muhurtham.COL_TIME)));
            md.setLaknam(c.getInt(c.getColumnIndexOrThrow(Table.Muhurtham.COL_LAKNAM)));
            return md;
        }


        return null;
    }

    public List<VirathamMonthData> getVirathamList(int year, int month) {
        List<VirathamMonthData> dataList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT m.DATE, TMONTH, TDAY, VIRATHAM, PIRAI FROM MASTER m, DAY_VIRATHAM dv, DAY_THITI dt WHERE m.DATE=dv.DATE AND dv.DATE=dt.DATE AND m.YEAR=? AND m.MONTH=? ORDER BY m.DAY",
                new String[]{year + "", month + ""});
        if (null != c && c.moveToFirst()) {
            do {
                VirathamMonthData vd = new VirathamMonthData(c.getString(c.getColumnIndexOrThrow(Table.Master.COL_DATE)));
                vd.setTmonth(c.getInt(c.getColumnIndexOrThrow(Table.Master.COL_TMONTH)));
                vd.setTday(c.getInt(c.getColumnIndexOrThrow(Table.Master.COL_TDAY)));
                vd.setViratham(c.getInt(c.getColumnIndexOrThrow(Table.Viratham.COL_VIRATHAM)));
                vd.setPirai(c.getInt(c.getColumnIndexOrThrow(Table.Thiti.COL_PIRAI)));
                dataList.add(vd);
            } while (c.moveToNext());
        }
        return dataList;
    }

    public List<VirathamData> getVirathamList(String dateString) {
        List<VirathamData> dataList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(Table.Viratham.NAME, null,
                Table.Viratham.COL_DATE + "=?",
                new String[]{dateString}, null, null, null
        );
        if (null != c && c.moveToFirst()) {
            do {
                VirathamData vd = new VirathamData(dateString);
                vd.setViratham(c.getInt(c.getColumnIndexOrThrow(Table.Viratham.COL_VIRATHAM)));
                vd.setTiming(c.getString(c.getColumnIndexOrThrow(Table.Viratham.COL_TIMING)));
                dataList.add(vd);
            } while (c.moveToNext());
        }
        return dataList;
    }

    public FestivalDayData getFestivalDays(String dateString) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(Table.Festival.NAME, null,
                Table.Festival.COL_DATE + "=?",
                new String[]{dateString}, null, null, null, null);
        if (null != c && c.moveToFirst()) {
            FestivalDayData fd = new FestivalDayData(dateString);
            fd.setHindhu(c.getString(c.getColumnIndexOrThrow(Table.Festival.COL_HINDHU)));
            fd.setMuslims(c.getString(c.getColumnIndexOrThrow(Table.Festival.COL_MUSLIM)));
            fd.setChrist(c.getString(c.getColumnIndexOrThrow(Table.Festival.COL_CHRIST)));
            fd.setGovt(c.getString(c.getColumnIndexOrThrow(Table.Festival.COL_GOVT)));
            fd.setLeave(1 == c.getInt(c.getColumnIndexOrThrow(Table.Festival.COL_LEAVE)));
            fd.setImportant(c.getString(c.getColumnIndexOrThrow(Table.Festival.COL_IMPORTANT_DAYS)));
            return fd;
        }

        return null;
    }

    public boolean isKariNaal(int tmonth, int tday) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT KARI FROM KARI_NAAL WHERE TMONTH=? AND TDAY=?", new Object[]{tmonth, tday});

        if (null != c && c.moveToFirst()) {
            return c.getInt(c.getColumnIndexOrThrow("KARI")) == 1;
        }

        return false;
    }

    public Map<LocalDate, String> getHinduFestivalDays(int year, int month) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT fd.DATE, fd.HINDHU FROM FESTIVAL_DAYS fd, MASTER m where fd.DATE = m.DATE and m.YEAR=? and m.MONTH=? and fd.HINDHU <>'-' and fd.HINDHU is not NULL",
                new Object[]{year, month});
        Map<LocalDate, String> map = new TreeMap<>();
        if (c != null && c.moveToFirst()) {
            do {
                map.put(
                        DateUtil.ofLocalDate(c.getString(0)),
                        c.getString(1)
                );
            } while (c.moveToNext());
        }
        return map;
    }

    public Map<LocalDate, String> getHolidays(int year, int month) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT fd.DATE, fd.GOVT FROM FESTIVAL_DAYS fd, MASTER m where fd.DATE = m.DATE and m.YEAR=? and m.MONTH=? and fd.GOVT <>'-'",
                new Object[]{year, month});
        Map<LocalDate, String> map = new TreeMap<>();
        if (c != null && c.moveToFirst()) {
            do {
                map.put(
                        DateUtil.ofLocalDate(c.getString(0)),
                        c.getString(1)
                );
            } while (c.moveToNext());
        }
        return map;
    }

    public List<LocalDate> KariNaalList(int year, int month) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT DATE FROM MASTER m, KARI_NAAL kn where m.TDAY=kn.TDAY and m.TMONTH=kn.TMONTH and m.YEAR=? and m.MONTH=?",
                new Object[]{year, month});
        List<LocalDate> knList = new ArrayList<>();
        if (c != null && c.moveToFirst()) {
            do {
                knList.add(DateUtil.ofLocalDate(c.getString(0)));
            } while (c.moveToNext());
        }
        return knList;
    }

    public Collection<Integer> getVasthuYearList() {
        List<Integer> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT DISTINCT YEAR  from MASTER m, VASTHU_DAYS vd where m.DATE = vd.DATE", null);
        if (null != c && c.moveToFirst()) {
            do {
                result.add(c.getInt(0));
            } while (c.moveToNext());
        }
        return result;
    }

    public List<VasthuData> getVasthuList(int year) {
        List<VasthuData> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT m.DATE, TMONTH,TDAY,TIME  from MASTER m, VASTHU_DAYS vd where m.DATE = vd.DATE and m.YEAR=?", new Object[]{year});
        if (c != null && c.moveToFirst()) {
            do {
                VasthuData vd = new VasthuData();
                vd.setDate(c.getString(c.getColumnIndexOrThrow(Table.Master.COL_DATE)));
                vd.setTmonth(c.getInt(c.getColumnIndexOrThrow(Table.Master.COL_TMONTH)));
                vd.setTday(c.getInt(c.getColumnIndexOrThrow(Table.Master.COL_TDAY)));
                vd.setTime(c.getString(c.getColumnIndexOrThrow(Table.Vasthu.COL_TIME)));
                result.add(vd);
            } while (c.moveToNext());
        }
        return result;
    }

    public List<Integer> getMuhurthamYearList() {
        List<Integer> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT DISTINCT YEAR FROM MASTER m, MUHURTHAM mm where m.DATE = mm.DATE ORDER BY YEAR DESC", null);
        if (c != null && c.moveToFirst()) {
            do {
                result.add(c.getInt(0));
            } while (c.moveToNext());
        }
        return result;
    }

    public Collection<Integer> getVirathamYearList() {
        List<Integer> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT DISTINCT YEAR from MASTER m, DAY_VIRATHAM dv where m.DATE = dv.DATE ORDER BY YEAR DESC", null);
        if (c != null && c.moveToFirst()) {
            do {
                result.add(c.getInt(0));
            } while (c.moveToNext());
        }
        return result;
    }

    public List<Integer> getFestivalYears() {
        List<Integer> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT DISTINCT YEAR FROM MASTER m, FESTIVAL_DAYS fd where m.DATE = fd.DATE ORDER BY YEAR DESC", null);
        if (c != null && c.moveToFirst()) {
            do {
                result.add(c.getInt(0));
            } while (c.moveToNext());
        }
        return result;
    }

    public Map<LocalDate, String> getChristFestivalDays(int year, int month) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT fd.DATE, fd.CHRIST FROM FESTIVAL_DAYS fd, MASTER m where fd.DATE = m.DATE and m.YEAR=? and m.MONTH=? and fd.CHRIST <>'-' and fd.CHRIST is not NULL",
                new Object[]{year, month});
        Map<LocalDate, String> map = new TreeMap<>();
        if (c != null && c.moveToFirst()) {
            do {
                map.put(
                        DateUtil.ofLocalDate(c.getString(0)),
                        c.getString(1)
                );
            } while (c.moveToNext());
        }
        return map;
    }

    public Map<LocalDate, String> getMuslimFestivalDays(int year, int month) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT fd.DATE, fd.MUSLIM FROM FESTIVAL_DAYS fd, MASTER m where fd.DATE = m.DATE and m.YEAR=? and m.MONTH=? and fd.MUSLIM <>'-' and fd.MUSLIM is not NULL",
                new Object[]{year, month});
        Map<LocalDate, String> map = new TreeMap<>();
        if (c != null && c.moveToFirst()) {
            do {
                map.put(
                        DateUtil.ofLocalDate(c.getString(0)),
                        c.getString(1)
                );
            } while (c.moveToNext());
        }
        return map;
    }

    public int getQuoteMaxNumber() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) as COUNT FROM QUOTES", null);
        if (c != null && c.moveToFirst()) {
            return c.getInt(c.getColumnIndexOrThrow("COUNT"));
        }
        return -1;
    }

    public String getQuote(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT QUOTE FROM QUOTES WHERE ID=?", new Integer[]{id});
        if (c != null && c.moveToFirst()) {
            return c.getString(c.getColumnIndexOrThrow("QUOTE"));
        }
        return StringUtils.EMPTY;
    }

    public KuralData getKural(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(Table.Kural.NAME, null, Table.Kural.COL_ID + "=?",
                new String[]{id + ""}, null, null, null);
        if (c != null && c.moveToFirst()) {
            KuralData kd = new KuralData();
            kd.setId(id);
            kd.setIyalkal(c.getString(c.getColumnIndexOrThrow(Table.Kural.COL_IYALKAL)));
            kd.setPaakal(c.getString(c.getColumnIndexOrThrow(Table.Kural.COL_PAAKAL)));
            kd.setAthigaram(c.getString(c.getColumnIndexOrThrow(Table.Kural.COL_ATHIGARAM)));
            kd.setAthigaramNum(c.getInt(c.getColumnIndexOrThrow(Table.Kural.COL_ATHIGARAM_NUM)));
            kd.setKural(c.getString(c.getColumnIndexOrThrow(Table.Kural.COL_KURAL)));

            kd.setMuVaUrai(c.getString(c.getColumnIndexOrThrow(Table.Kural.COL_MU_VA_URAI)));
            kd.setSalamanUrai(c.getString(c.getColumnIndexOrThrow(Table.Kural.COL_SALAMAN_URAI)));
            kd.setKarunaUrai(c.getString(c.getColumnIndexOrThrow(Table.Kural.COL_KARUNA_URAI)));
            kd.setParimelUrai(c.getString(c.getColumnIndexOrThrow(Table.Kural.COL_PARIMEL_URAI)));

            kd.setIyalkalEn(c.getString(c.getColumnIndexOrThrow(Table.Kural.COL_IYALKAL_EN)));
            kd.setPaakalEn(c.getString(c.getColumnIndexOrThrow(Table.Kural.COL_PAAKAL_EN)));
            kd.setAthigaramEn(c.getString(c.getColumnIndexOrThrow(Table.Kural.COL_ATHIGARAM_EN)));
            kd.setKuralEn(c.getString(c.getColumnIndexOrThrow(Table.Kural.COL_KURAL_EN)));
            kd.setExplanation(c.getString(c.getColumnIndexOrThrow(Table.Kural.COL_EXPLAINATION)));
            c.close();
            return kd;
        }
        return null;
    }

    public Map<Integer, String> getManaiyadiByType(int type) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(Table.Manaiyadi.NAME, null, Table.Manaiyadi.COL_TYPE + "=?", new String[]{type + ""}, null, null, null);
        Map<Integer, String> result = new TreeMap<>();
        if (c != null && c.moveToFirst()) {
            do {
                result.put(c.getInt(c.getColumnIndexOrThrow(Table.Manaiyadi.COL_ADI)),
                        c.getString(c.getColumnIndexOrThrow(Table.Manaiyadi.COL_EXPLANATION)));
            } while (c.moveToNext());
            c.close();
        }

        return result;
    }

    public List<PalliPalanData> getPalliPalans() {
        List<PalliPalanData> ppdList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.query(Table.PalliPalan.NAME, null, null, null, null, null, null, null)) {
            if (c != null && c.moveToFirst()) {
                do {
                    PalliPalanData ppd = new PalliPalanData();
                    ppd.setId(c.getInt(c.getColumnIndexOrThrow(Table.PalliPalan.COL_ID)));
                    ppd.setPart(c.getString(c.getColumnIndexOrThrow(Table.PalliPalan.COL_PART)));
                    ppd.setLeft(c.getString(c.getColumnIndexOrThrow(Table.PalliPalan.COL_LEFT)));
                    ppd.setRight(c.getString(c.getColumnIndexOrThrow(Table.PalliPalan.COL_RIGHT)));
                    ppdList.add(ppd);
                } while (c.moveToNext());
            }
        }

        return ppdList;
    }

    public List<StarsData> getStarMap() {
        SQLiteDatabase db = getReadableDatabase();
        List<StarsData> result = new ArrayList<>();
        try (Cursor c = db.query(Table.StarRasi.NAME, null, null, null, null, null, Table.StarRasi.COL_ID)) {
            if (c != null && c.moveToFirst()) {
                do {
                    StarsData sd = new StarsData();
                    sd.setId(c.getInt(c.getColumnIndexOrThrow(Table.StarRasi.COL_ID)));
                    sd.setStar(c.getString(c.getColumnIndexOrThrow(Table.StarRasi.COL_STAR)));
                    sd.setRasi(c.getString(c.getColumnIndexOrThrow(Table.StarRasi.COL_RASI)));
                    result.add(sd);
                } while (c.moveToNext());
            }
        }

        return result;
    }

    public StarMatching getStarPorutham(int boyIndex, int girlIndex) {
        SQLiteDatabase db = getReadableDatabase();
        StarMatching sm = null;
        try (Cursor c = db.query(Table.StarMatching.NAME, null,
                Table.StarMatching.COL_BOY_STAR_INDEX + "=? AND " + Table.StarMatching.COL_GIRL_STAR_INDEX + "=?",
                new String[]{boyIndex + "", girlIndex + ""}, null, null, null)) {
            if (c != null && c.moveToFirst()) {

                sm = new StarMatching(boyIndex, girlIndex);
                sm.setP1(c.getInt(c.getColumnIndexOrThrow(Table.StarMatching.COL_P1)));
                sm.setP2(c.getInt(c.getColumnIndexOrThrow(Table.StarMatching.COL_P2)));
                sm.setP3(c.getInt(c.getColumnIndexOrThrow(Table.StarMatching.COL_P3)));
                sm.setP4(c.getInt(c.getColumnIndexOrThrow(Table.StarMatching.COL_P4)));
                sm.setP5(c.getInt(c.getColumnIndexOrThrow(Table.StarMatching.COL_P5)));
                sm.setP6(c.getInt(c.getColumnIndexOrThrow(Table.StarMatching.COL_P6)));
                sm.setP7(c.getInt(c.getColumnIndexOrThrow(Table.StarMatching.COL_P7)));
                sm.setP8(c.getInt(c.getColumnIndexOrThrow(Table.StarMatching.COL_P8)));
                sm.setP9(c.getInt(c.getColumnIndexOrThrow(Table.StarMatching.COL_P9)));
                sm.setP10(c.getInt(c.getColumnIndexOrThrow(Table.StarMatching.COL_P10)));
                sm.setP11(c.getInt(c.getColumnIndexOrThrow(Table.StarMatching.COL_P11)));
                sm.setP12(c.getInt(c.getColumnIndexOrThrow(Table.StarMatching.COL_P12)));
                sm.setTotal(c.getInt(c.getColumnIndexOrThrow(Table.StarMatching.COL_TOTAL)));


            }
        }

        return sm;
    }

    public List<Integer> getMasterYearList() {

        List<Integer> years = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.rawQuery("select DISTINCT YEAR from MASTER", null)) {
            if (null != c && c.moveToFirst()) {
                do {
                    years.add(c.getInt(c.getColumnIndexOrThrow("YEAR")));
                } while (c.moveToNext());
            }
        }

        return years;
    }
}
