package utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StringUtils {

	public static String replaceWords(String phoneNumber) {

		String added_phoneNo = phoneNumber.replace(" ", "").replace("+", "")
				.replace("-", "").replace("(", "").replace(")", "");
		// if(added_phoneNo.length() > 10) {
		// added_phoneNo = added_phoneNo.substring(added_phoneNo.length() - 10);
		//
		// }
		return added_phoneNo;

	}
	
	/**
	 * Email verification
	 * @param paramString
	 * @return
	 */

	/*public static boolean verify(String paramString) {
		return paramString
				.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$");
	}*/
	
	/**
	 * Date Format conversion
	 * @param dateToConvert
	 * @return
	 */

	public static String DateConverter(String dateToConvert) {

		String dateConvert = dateToConvert;
		String DateConverted = "";

		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		Date myDate = new Date();
		try {
			myDate = dateFormat.parse(dateConvert);
			Log.e("***** myDate *****", "" + myDate);
			SimpleDateFormat formatter = new SimpleDateFormat("MM / dd / yyyy");
			 DateConverted = formatter.format(myDate);
			Log.e("*conveted date*", "" + DateConverted);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return DateConverted;

	}

	// Convert date String from one format to another

	public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate){

		Date parsed = null;
		String outputDate = "";

		SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
		SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

		try {
			parsed = df_input.parse(inputDate);
			outputDate = df_output.format(parsed);

		} catch (ParseException e) {
			Log.e("TAG Date", "ParseException - dateFormat");
		}

		return outputDate;

	}

	/**
	 * Find Time difference
	 */
public static String timeDifferenceInHours (String START_TIME , String END_TIME) {
	float diffHours = 0;
	float diffMinutes = 0;
	try {

		String string1 =START_TIME;
		Date time1 = new SimpleDateFormat("h:mm a").parse(string1);
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(time1);

		String string2 = END_TIME;
		Date time2 = new SimpleDateFormat("h:mm a").parse(string2);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(time2);
		//calendar2.add(Calendar.DATE, 1);

		Date x = calendar1.getTime();
		Date xy = calendar2.getTime();

		Log.e("x==>",""+x);
		Log.e("xy==>",""+xy);

		/*long diff = x.getTime() - xy.getTime();*/
        long diff =  xy.getTime()-x.getTime();
		Log.e("diff==>",""+diff);

		diffMinutes = diff / (60 * 1000);
		Log.e("diffMinutes==>",""+diffMinutes);

        diffHours = diffMinutes / 60;
		Log.e("diffHours==>",""+diffHours);

		System.out.println("diff hours" + diffHours);

	} catch(Exception e){
        e.printStackTrace();
	}

	return diffHours+"";
}

}
