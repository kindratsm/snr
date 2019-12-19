package com.github.kindratsm.snr.enums;

import java.util.regex.Pattern;

/**
 * SNR Country enum
 * 
 * @author Stanislav Kindrat
 *
 */
public enum SNRCountry {

	USA("^[\\d]{10}$|^001[\\d]{10}$"),
	GREECE("^[\\d]{10}$|^0030[\\d]{10}$"),
	RUSSIA("^[\\d]{10}$|^007[\\d]{10}$|^8[\\d]{10}$");

	private final Pattern phonePattern;

	/**
	 * SNR Country constructor based on phone format
	 * 
	 * @param phoneFormat the phone format
	 */
	private SNRCountry(String phoneFormat) {
		this.phonePattern = Pattern.compile(phoneFormat);
	}

	/**
	 * Phone pattern getter
	 * 
	 * @return the phone pattern
	 */
	public Pattern getPhonePattern() {
		return phonePattern;
	}

}
