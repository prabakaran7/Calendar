package org.tem.calendar.library;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PanchangamManager {

	public enum Direction {
		NORTH, EAST, SOUTH, WEST
	}

	public enum Parigaram {
		JAGGERY, CURD, MILK, OIL
	}

	// MON---SUN 1-7
	// WED->TUE->MON->SUN->SAT->FRI->THU
	private static final List<String> KARNAN_LIST = Arrays.asList("9:00 AM - 10:30 AM", "7:30 AM - 9:00 AM",
			"6:00 AM - 7:30 AM", "3:00 PM - 4:30 PM", "1:30 PM - 3:00 PM", "12:00 PM - 1:30 PM", "10:30 AM - 12:00 PM");

	private static final List<String> RAGHU_LIST = Arrays.asList("7:30 AM - 9:00 AM", "3:00 PM - 4:30 PM",
			"12:00 PM - 1:30 PM", "1:30 PM - 3:00 PM", "10:30 AM - 12:00 PM", "9:00 AM - 10:30 AM",
			"4:30 PM - 6:00 PM");

	private static final List<String> KULIGAI_LIST = Arrays.asList("1:30 PM - 3:00 PM", "12:00 PM - 1:30 PM",
			"10:30 AM - 12:00 PM", "9:00 AM - 10:30 AM", "7:30 AM - 9:00 AM", "6:00 AM - 7:30 AM", "3:00 PM - 4:30 PM");

	private static final List<String> EMAKANDAM_LIST = Arrays.asList("10:30 AM - 12:00 PM", "9:00 AM - 10:30 AM",
			"7:30 AM - 9:00 AM", "6:00 AM - 7:30 AM", "3:00 PM - 4:30 PM", "1:30 PM - 3:00 PM", "12:00 PM - 1:30 PM");

	private static final List<Direction> SOOLAM_DIRECTION = Arrays.asList(Direction.EAST, Direction.NORTH, Direction.NORTH,
			Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.WEST);

	private static final List<Parigaram> SOOLAM_PARIGARAM = Arrays.asList(Parigaram.CURD, Parigaram.MILK, Parigaram.MILK,
			Parigaram.OIL, Parigaram.JAGGERY, Parigaram.CURD, Parigaram.JAGGERY);

	private static final List<Integer> SOOLAM_TIME = Arrays.asList(8, 12, 16, 20, 12, 8, 12);

	private static final Map<Integer, Set<Integer>> KARI_NAAL = new HashMap<>();
	static {
		KARI_NAAL.put(1, new HashSet<>(Arrays.asList(6, 15)));
		KARI_NAAL.put(2, new HashSet<>(Arrays.asList(7, 16, 17)));
		KARI_NAAL.put(3, new HashSet<>(Arrays.asList(1, 6)));
		KARI_NAAL.put(4, new HashSet<>(Arrays.asList(2, 10, 20)));
		KARI_NAAL.put(5, new HashSet<>(Arrays.asList(2, 9, 28)));
		KARI_NAAL.put(6, new HashSet<>(Arrays.asList(16, 29)));
		KARI_NAAL.put(7, new HashSet<>(Arrays.asList(6, 20)));
		KARI_NAAL.put(8, new HashSet<>(Arrays.asList(1, 10, 17)));
		KARI_NAAL.put(9, new HashSet<>(Arrays.asList(6, 9, 11)));
		KARI_NAAL.put(10, new HashSet<>(Arrays.asList(1, 2, 3, 11, 17)));
		KARI_NAAL.put(11, new HashSet<>(Arrays.asList(15, 16, 17)));
		KARI_NAAL.put(12, new HashSet<>(Arrays.asList(6, 15, 19)));
	}

	public static String getKarnan(int dayOfWeek) {
		return KARNAN_LIST.get(dayOfWeek - 1);
	}

	public static String getRaghu(int dayOfWeek) {
		return RAGHU_LIST.get(dayOfWeek - 1);
	}

	public static String getKuligai(int dayOfWeek) {
		return KULIGAI_LIST.get(dayOfWeek - 1);
	}

	public static String getEmakandam(int dayOfWeek) {
		return EMAKANDAM_LIST.get(dayOfWeek - 1);
	}

	public static Direction getSoolamDirection(int dayOfWeek) {
		return SOOLAM_DIRECTION.get(dayOfWeek - 1);
	}

	public static Parigaram getSoolamParigaram(int dayOfWeek) {
		return SOOLAM_PARIGARAM.get(dayOfWeek - 1);
	}

	public static int getSoolamTime(int dayOfWeek) {
		return SOOLAM_TIME.get(dayOfWeek - 1);
	}

	public static boolean isKariNaal(int tamizhMonth, int tamilDay) {
		return KARI_NAAL.containsKey(tamizhMonth) && KARI_NAAL.get(tamizhMonth).contains(tamilDay);
	}

}
