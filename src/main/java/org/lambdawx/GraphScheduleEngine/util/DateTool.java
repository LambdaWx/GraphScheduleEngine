package org.lambdawx.GraphScheduleEngine.util;

import org.apache.log4j.Logger;
import org.lambdawx.GraphScheduleEngine.model.ConfigStatic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 
 *         获得时间日期的工具类
 * 
 */
public class DateTool {
	static Logger logger = Logger.getLogger(DateTool.class.getName());

	public static long getDayStamp() {
		Calendar calendar = Calendar.getInstance();
		Date date = (Date) calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long tstoday = 0;
		try {
			tstoday = format.parse(format.format(date)).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tstoday;
	}
	public static List<Long> getStartAndEndTimeStamp(String startTime, String endTime) {
		List<Long> timestamps = new ArrayList<Long>();
		Long start = 0L;
		Long end = 0L;
		SimpleDateFormat format = new SimpleDateFormat(ConfigStatic.HOURDATEFORMAT);
		try {
			Calendar startHour = Calendar.getInstance();
			Calendar endHour = Calendar.getInstance();
			if (null != startTime && !"".equals(startTime) && null != endTime && !"".equals(endTime)) {
				// 开始时间与结束时间皆不为空,按照输入设置
				startHour.setTime(format.parse(startTime));
				startHour.set(Calendar.HOUR_OF_DAY, 0);
				start = startHour.getTime().getTime();
				endHour.setTime(format.parse(endTime));
				endHour.set(Calendar.HOUR_OF_DAY, 24);
				end = endHour.getTime().getTime();
			} else if ((null == startTime || "".equals(startTime)) && null != endTime && !"".equals(endTime)) {
				// 开始时间为空，结束时间不为空，将开始时间设置为结束时间所在天的0点
				startHour.setTime(format.parse(endTime));
				startHour.set(Calendar.HOUR_OF_DAY, 0);
				start = startHour.getTime().getTime();
				endHour.setTime(format.parse(endTime));
				endHour.set(Calendar.HOUR_OF_DAY, 24);
				end = endHour.getTime().getTime();
			} else if (null != startTime && !"".equals(startTime) && (null == endTime || "".equals(endTime))) {
				// 开始时间不为空，结束时间为空，将结束时间设置为开始时间所在天的23点
				endHour.setTime(format.parse(startTime));
				endHour.set(Calendar.HOUR_OF_DAY, 24);
				end = endHour.getTime().getTime();
				startHour.setTime(format.parse(startTime));
				startHour.set(Calendar.HOUR_OF_DAY, 0);
				start = startHour.getTime().getTime();
			} else {
				// 开始时间与结束时间皆为空，开始时间为当天0点，结束时间为当前小时
				startHour.set(Calendar.HOUR_OF_DAY, 0);
				start = startHour.getTime().getTime();
				end = endHour.getTime().getTime();
			}
			timestamps.add(start);
			timestamps.add(end);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return timestamps;
	}

	public static List<String> getStartAndEndTime(String startTime, String endTime) {
		List<String> time = new ArrayList<String>();
		SimpleDateFormat format = new SimpleDateFormat(ConfigStatic.HOURDATEFORMAT);
		try {
			Calendar startHour = Calendar.getInstance();
			Calendar endHour = Calendar.getInstance();
			if (null != startTime && !"".equals(startTime) && null != endTime && !"".equals(endTime)) {
				// 开始时间与结束时间皆不为空,按照输入设置
				startTime = format.format(format.parse(startTime));
				endTime = format.format(format.parse(endTime));
			} else if ((null == startTime || "".equals(startTime)) && null != endTime && !"".equals(endTime)) {
				// 开始时间为空，结束时间不为空，将开始时间设置为结束时间所在天的0点
				startHour.setTime(format.parse(endTime));
				startHour.set(Calendar.HOUR_OF_DAY, 0);
				startTime = format.format(startHour.getTime());
				endTime = format.format(format.parse(endTime));
			} else if (null != startTime && !"".equals(startTime) && (null == endTime || "".equals(endTime))) {
				// 开始时间不为空，结束时间为空，将结束时间设置为开始时间所在天的23点
				endHour.setTime(format.parse(startTime));
				endHour.set(Calendar.HOUR_OF_DAY, 23);
				endTime = format.format(endHour.getTime());
				startTime = format.format(format.parse(startTime));
			} else {
				// 开始时间与结束时间皆为空，开始时间为当天0点，结束时间为当前小时
				startHour.set(Calendar.HOUR_OF_DAY, 0);
				startTime = format.format(startHour.getTime());
				endTime = format.format(endHour.getTime());
			}
			time.add(startTime);
			time.add(endTime);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return time;
	}
	public static String getMySQLDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	public static long getLastDayTimeStamp() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1); // 得到前一天
		Date date = (Date) calendar.getTime();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		long tsLastDay = 0;
		try {

			tsLastDay = format.parse(format.format(date)).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return tsLastDay;
	}

	public static String getLastDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1); // 得到前一天
		Date date = (Date) calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}

	public static String getLastDay(String dateFormat) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1); // 得到前一天
		Date date = (Date) calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		return format.format(date);
	}

	public static long getLastWeekTimeStamp() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -7); // 得到前一天
		Date date = (Date) calendar.getTime();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		long tsLastDay = 0;
		try {

			tsLastDay = format.parse(format.format(date)).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return tsLastDay;
	}

	public static long getTimeStamp(String startDate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		long timeStamp = 0;
		try {
			timeStamp = format.parse(format.format(startDate)).getTime();
		} catch (ParseException e) {
			logger.error(e.toString());
		}

		return timeStamp;
	}

	public static String getNowTime() {
		Calendar calendar = Calendar.getInstance();
		Date date = (Date) calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String tstoday = "";
		try {
			String da = format.format(date);
			tstoday = String.valueOf(da);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tstoday;
	}

	public static String getToday() {
		Calendar calendar = Calendar.getInstance();
		Date date = (Date) calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String tstoday = "";
		try {
			String da = format.format(date);
			tstoday = String.valueOf(da);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tstoday;
	}

	public static Long getZeroTimeStamp() {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			return calendar.getTime().getTime();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static Long getEndTimeStamp() {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.HOUR_OF_DAY, 24);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			return calendar.getTime().getTime();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static List<String> getDays(String startTime, String endTime) {
		List<String> days = new ArrayList<String>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Calendar startDay = Calendar.getInstance();
			Calendar endDay = Calendar.getInstance();
			if (null != startTime && !"".equals(startTime) && null != endTime && !"".equals(endTime)) {
				// 开始时间与结束时间皆不为空,按照输入设置
				startDay.setTime(format.parse(startTime));
				endDay.setTime(format.parse(endTime));
			} else if ((null == startTime || "".equals(startTime)) && null != endTime && !"".equals(endTime)) {
				// 开始时间为空，结束时间不为空，将开始时间设置为结束时间前一周
				endDay.setTime(format.parse(endTime));
				startDay.setTime(format.parse(endTime));
				startDay.set(Calendar.DATE, startDay.get(Calendar.DATE) - 7);
			} else if (null != startTime && !"".equals(startTime) && (null == endTime || "".equals(endTime))) {
				// 开始时间不为空，结束时间为空，将结束时间设置为当前时间
				startDay.setTime(format.parse(startTime));
				endDay.setTime(format.parse(format.format(new Date())));
			} else {
				// 开始时间与结束时间皆为空，当前时间的前一周+当前天
				startDay.set(Calendar.DATE, startDay.get(Calendar.DATE) - 7);
			}
			while (startDay.getTime().before(endDay.getTime())) {
				days.add(format.format(startDay.getTime()));
				startDay.set(Calendar.DATE, startDay.get(Calendar.DATE) + 1);
			}
			days.add(format.format(startDay.getTime()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return days;
	}

	/**
	 * 获取当前天往前6天的0点时间
	 * @return
	 */
	public static String getBeforeWeekTime() {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -6);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MINUTE, 0);
			return format.format(calendar.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取当前天往前6天的日期字符串列表
	 * @return
	 */
	public static List<String> getBeforeWeekDays() {
		List<String> days = new ArrayList<String>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar startHour = Calendar.getInstance();
		startHour.add(Calendar.DATE, -6);
		Calendar endHour = Calendar.getInstance();
		while (startHour.getTime().before(endHour.getTime())) {
			days.add(format.format(startHour.getTime()));
			startHour.add(Calendar.DATE, 1);
		}
		days.add(format.format(startHour.getTime()));
		return days;
	}

	public static String getBeforeWeekDay() {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -7);
			return format.format(calendar.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] arg) {
//		String createSql = "CREATE TABLE IF NOT EXISTS msm_test_1(id string,createTime string) partitioned by (weekrange string)";
//		if (StringUtils.contains(createSql, "partitioned by")) {
//			createSql = StringUtils.substring(createSql, 0, StringUtils.indexOf(createSql, "partitioned by"));
//		}
//		System.out.println(createSql);
		System.out.println(getLastDay());
//		for(String d : getBeforeWeekDays()) {
//			System.out.println(d);
//		}
		System.out.println(getBeforeWeekDay());
	}
}
