package com.github.kindratsm.snr.helpers;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.kindratsm.snr.models.SNRGroup;

/**
 * String Number Recognition (SNR) Helper to unify validation and split logic
 * 
 * @author Stanislav Kindrat
 *
 */
public class SNRHelper {

	private static final Pattern INPUT_PATTERN = Pattern.compile("^\\d+(\\s\\d+)*$");
	private static final Pattern INPUT_SPLIT_PATTERN = Pattern.compile("\\d+");
	private static final Pattern GROUP_PATTERN = Pattern.compile("^\\d+$");
	private static final Pattern MODULUS_PATTERN = Pattern.compile("^[1-9]+[0]+$");
	private static final Pattern ZEROS_PATTERN = Pattern.compile("^[0]+$");
	private static final Pattern LEADING_ZEROS_PATTERN = Pattern.compile("^[0]+[1-9]+$");
	private static final char ZERO = '0';

	/**
	 * Validate method to ensure that input is match system format
	 * 
	 * @param input the input
	 */
	public static void validateInput(String input) {
		LangHelper.notNull(input);

		if (LangHelper.isNotMatch(INPUT_PATTERN, input)) {
			throw new IllegalArgumentException(String.format("Invalid inpupt [%s]", input));
		}
	}

	/**
	 * Method to split input to SNR groups
	 * 
	 * @param input the input
	 * @return array of SNR groups
	 */
	public static SNRGroup[] splitInputToGroups(String input) {
		LangHelper.notNull(input);

		// Trim input
		input = input.trim();

		SNRHelper.validateInput(input);

		final List<SNRGroup> groups = new LinkedList<SNRGroup>();

		final Matcher m = INPUT_SPLIT_PATTERN.matcher(input);
		while (m.find()) {
			groups.add(new SNRGroup(m.group()));
		}

		return groups.toArray(new SNRGroup[groups.size()]);
	}

	/**
	 * Validate method to ensure that input is match system group format
	 * 
	 * @param input the input
	 */
	public static void validateGroup(String input) {
		LangHelper.notNull(input);

		if (LangHelper.isNotMatch(GROUP_PATTERN, input)) {
			throw new IllegalArgumentException(String.format("Invalid group [%s]", input));
		}
	}

	/**
	 * Method to check does input is modulus (e.g. 10, 2000, 3330)
	 * 
	 * @param input the input
	 * @return does input is modulus
	 */
	public static boolean isModulusInput(String input) {
		LangHelper.notNull(input);

		return LangHelper.isMatch(MODULUS_PATTERN, input);
	}

	/**
	 * Method to check does input is zero or combination of zeros
	 * 
	 * @param input the input
	 * @return does input is zero or combination of zeros
	 */
	public static boolean isZerosInput(String input) {
		LangHelper.notNull(input);

		return LangHelper.isMatch(ZEROS_PATTERN, input);
	}

	/**
	 * Method to count trailing zeros in input
	 * 
	 * @param input the input
	 * @return count of trailing zeros in SNR group
	 */
	public static int countTrailingZeros(String input) {
		LangHelper.notNull(input);

		int zerosCount = 0;

		final char[] array = input.toCharArray();
		for (int i = array.length - 1; i >= 0; i--) {
			if (array[i] == ZERO) {
				zerosCount++;
			} else {
				break;
			}
		}

		return zerosCount;
	}

	/**
	 * Method to add trailing zeros to input
	 * 
	 * @param input the input
	 * @param count trailing zeros count
	 * @return input with provided amount of trailing zeros
	 */
	public static String addTrailingZeros(String input, int count) {
		LangHelper.notNull(input);

		if (count <= 0) {
			throw new IllegalArgumentException();
		}

		StringBuilder sb = new StringBuilder(input);

		for (int i = 0; i < count; i++) {
			sb.append(ZERO);
		}

		return sb.toString();
	}

	/**
	 * Method to check does input has leading zeros
	 * 
	 * @param input the input
	 * @return does input has leading zeros
	 */
	public static boolean hasLeadingZeros(String input) {
		LangHelper.notNull(input);

		return LangHelper.isMatch(LEADING_ZEROS_PATTERN, input);
	}

}
