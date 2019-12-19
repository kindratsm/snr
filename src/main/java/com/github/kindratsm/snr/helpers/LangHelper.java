package com.github.kindratsm.snr.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Language Helper to unify common methods
 * 
 * @author Stanislav Kindrat
 *
 */
public class LangHelper {

	/**
	 * Method to check is value is null
	 * 
	 * @param value the value
	 * @return is value is null
	 */
	public static boolean isNull(Object value) {
		return value == null;
	}

	/**
	 * Method to check is value is not null
	 * 
	 * @param value the value
	 * @return is value is not null
	 */
	public static boolean isNotNull(Object value) {
		return value != null;
	}

	/**
	 * Validate method to ensure that value is not null
	 * 
	 * @param value the value
	 */
	public static void notNull(Object value) {
		if (LangHelper.isNull(value)) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Method to check does the index is in range of array
	 * 
	 * @param array the array
	 * @param index the index
	 * @return does the index is in range of array
	 */
	public static boolean isInRange(Object[] array, int index) {
		LangHelper.notNull(array);

		return index >= 0 && index < array.length;
	}

	/**
	 * Method to check does the index is out of range of array
	 * 
	 * @param array the array
	 * @param index the index
	 * @return does the index is out of range of array
	 */
	public static boolean isOutOfRange(Object[] array, int index) {
		LangHelper.notNull(array);

		return index < 0 || index >= array.length;
	}

	/**
	 * Validate method to ensure that index is in range of array
	 * 
	 * @param array the array
	 * @param index the index
	 */
	public static void inRange(Object[] array, int index) {
		if (LangHelper.isOutOfRange(array, index)) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Method to check does input is match for regex pattern
	 * 
	 * @param pattern the regex pattern
	 * @param input   the input
	 * @return does input is match for regex pattern
	 */
	public static boolean isMatch(Pattern pattern, String input) {
		LangHelper.notNull(pattern);
		LangHelper.notNull(input);

		Matcher m = pattern.matcher(input);
		return m.matches();
	}

	/**
	 * Method to check does input is not match for regex pattern
	 * 
	 * @param pattern the regex pattern
	 * @param input   the input
	 * @return does input is not match for regex pattern
	 */
	public static boolean isNotMatch(Pattern pattern, String input) {
		LangHelper.notNull(pattern);
		LangHelper.notNull(input);

		Matcher m = pattern.matcher(input);
		return !m.matches();
	}

}
