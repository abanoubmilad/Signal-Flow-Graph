/*
 Implemented by : Abanoub Milad Nassif
 e-mail: abanoubcs@gmail.com 
 data  : 11/5/2015
 */
public class Valid {

	public static boolean isValidInt(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public static boolean isValidDouble(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
