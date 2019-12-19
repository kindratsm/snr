package com.github.kindratsm.snr.enums;

/**
 * SNR Language enum
 * 
 * @author Stanislav Kindrat
 *
 */
public enum SNRLanguage {

	ENGLISH(19),
	GREEK(12),
	RUSSIAN(19);

	/**
	 * Number limit e.g. in English pronounce 15 can be interpreted only as 15 while
	 * in Greek pronounce 15 can be interpreted as 15 and 10 5
	 */
	private final int limit;

	/**
	 * SNR Language constructor
	 * 
	 * @param limit the limit
	 */
	private SNRLanguage(int limit) {
		this.limit = limit;
	}

	/**
	 * Limit getter
	 * 
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

}
