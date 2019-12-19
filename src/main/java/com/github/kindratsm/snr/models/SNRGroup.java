package com.github.kindratsm.snr.models;

import com.github.kindratsm.snr.helpers.LangHelper;
import com.github.kindratsm.snr.helpers.SNRHelper;

/**
 * SNR Group
 * 
 * @author Stanislav Kindrat
 *
 */
public class SNRGroup {

	private final String input;
	private final char[] array;

	/**
	 * SNR Group constructor based on input
	 * 
	 * @param input the input
	 */
	public SNRGroup(String input) {
		SNRHelper.validateGroup(input);

		this.input = input;
		this.array = input.toCharArray();
	}

	/**
	 * Input getter
	 * 
	 * @return the input
	 */
	public String getInput() {
		return input;
	}

	/**
	 * Input char array getter
	 * 
	 * @return the input char array
	 */
	public char[] getArray() {
		return array;
	}

	/**
	 * Overridden equals method with additional validation of input equality
	 */
	@Override
	public boolean equals(Object obj) {
		if (LangHelper.isNull(obj)) {
			return false;
		}

		// Standard reference equality check
		if (this == obj) {
			return true;
		}

		// Validate equality of input
		if (obj instanceof SNRGroup) {
			return this.input.equals(((SNRGroup) obj).input);
		}

		return false;
	}

}
