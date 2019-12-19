package com.github.kindratsm.snr.models;

import com.github.kindratsm.snr.enums.SNRCountry;
import com.github.kindratsm.snr.helpers.LangHelper;

/**
 * SNR Phone Number model
 * 
 * @author Stanislav Kindrat
 *
 */
public class SNRPhoneNumber {

	private final SNRCountry country;
	private final String interpretation;
	private final boolean valid;

	/**
	 * SNR Phone Number constructor
	 * 
	 * @param country        the country
	 * @param interpretation the interpretation
	 */
	public SNRPhoneNumber(SNRCountry country, String interpretation) {
		LangHelper.notNull(country);
		LangHelper.notNull(interpretation);

		this.country = country;
		this.interpretation = interpretation;
		this.valid = LangHelper.isMatch(country.getPhonePattern(), interpretation);
	}

	/**
	 * Country getter
	 * 
	 * @return the country
	 */
	public SNRCountry getCountry() {
		return country;
	}

	/**
	 * Interpretation getter
	 * 
	 * @return the interpretation
	 */
	public String getInterpretation() {
		return interpretation;
	}

	/**
	 * Valid getter
	 * 
	 * @return is valid phone number
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * Overridden equals method with additional validation of country and
	 * interpretation equality
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

		// Validate equality of country and input
		if (obj instanceof SNRPhoneNumber) {
			SNRPhoneNumber item = (SNRPhoneNumber) obj;

			return this.country.equals(item.country) && this.interpretation.equals(item.interpretation);
		}

		return false;
	}

}
