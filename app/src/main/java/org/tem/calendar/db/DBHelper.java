package org.tem.calendar.db;

import android.content.Context;
import android.widget.Toast;

import net.sqlcipher.Cursor;
import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import org.tem.calendar.model.KaranamData;
import org.tem.calendar.model.MonthData;
import org.tem.calendar.model.MuhurthamData;
import org.tem.calendar.model.NallaNeramData;
import org.tem.calendar.model.PanchangamData;
import org.tem.calendar.model.RasiChartData;
import org.tem.calendar.model.RasiData;
import org.tem.calendar.model.StarData;
import org.tem.calendar.model.ThitiData;
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
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 12;
    private static final String DB_NAME = "tamizh_calendar.db";
    private static DBHelper dbHelper;
    private final File DB_FILE;
    private final Context mContext;
    private SQLiteDatabase mDatabase;

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        DB_FILE = context.getDatabasePath(DB_NAME);
        this.mContext = context;

        try {
            createDataBase();
        } catch (IOException e) {
            Toast.makeText(mContext, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
        getReadableDatabase();
        if (mDatabase.getVersion() != DB_VERSION) {
            mContext.deleteDatabase(DB_NAME);
            mDatabase = null;
        }
        try {
            createDataBase();
        } catch (IOException e) {
            Toast.makeText(mContext, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static DBHelper getInstance(Context context) {
        if (null == dbHelper) {
            dbHelper = new DBHelper(context);
        }

        return dbHelper;
    }

    public void createDataBase() throws IOException {
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
            mDatabase = SQLiteDatabase.openDatabase(DB_FILE.getPath(), "123", null, SQLiteDatabase.OPEN_READONLY);
            // mDataBase = SQLiteDatabase.openDatabase(DB_FILE, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        }
        System.out.println("---------->" + getDatabaseName() + ", " + mDatabase.getVersion());
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
        System.out.println("OLD: " + oldVersion + ", newVersion::" + newVersion);
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
        String qry = "SELECT mm.DATE as DATE, TMONTH, TDAY, thiti,star,yogam,time,laknam FROM MUHURTHAM m, MASTER mm where m.date=mm.date and mm.year=? and mm.MONTH=?";
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
}
