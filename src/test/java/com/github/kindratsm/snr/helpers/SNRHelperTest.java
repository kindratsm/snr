package com.github.kindratsm.snr.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.kindratsm.snr.models.SNRGroup;

/**
 * SNR Helper unit test
 * 
 * @author Stanislav Kindrat
 *
 */
public class SNRHelperTest {

	String validInput;
	String invalidInput;
	String validGroupInput;
	String modulusGroupInput;
	String zeroInput;
	String invalidGroupInput;
	String suffix;

	@BeforeEach
	void init() {
		validInput = "2 10 6 9 30 6 6 4";
		invalidInput = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		validGroupInput = "305";
		modulusGroupInput = "350";
		zeroInput = "0";
		invalidGroupInput = "2 10";
		suffix = "05";
	}

	@Test
	void test() {
		// Check data
		assertNotNull(validInput);
		assertNotNull(invalidInput);
		assertNotNull(validGroupInput);
		assertNotNull(modulusGroupInput);
		assertNotNull(zeroInput);
		assertNotNull(invalidGroupInput);
		assertNotNull(suffix);

		// validateInput
		assertThrows(IllegalArgumentException.class, () -> {
			SNRHelper.validateInput(invalidInput);
		});

		// splitInputToGroups
		SNRGroup[] array = SNRHelper.splitInputToGroups(validInput);

		assertNotNull(array);
		assertEquals(array.length, 8);
		assertEquals(array[0], new SNRGroup("2"));
		assertEquals(array[1], new SNRGroup("10"));
		assertEquals(array[2], new SNRGroup("6"));
		assertEquals(array[3], new SNRGroup("9"));
		assertEquals(array[4], new SNRGroup("30"));
		assertEquals(array[5], new SNRGroup("6"));
		assertEquals(array[6], new SNRGroup("6"));
		assertEquals(array[7], new SNRGroup("4"));

		// validateGroup
		assertThrows(IllegalArgumentException.class, () -> {
			SNRHelper.validateGroup(invalidGroupInput);
		});

		// isModulusInput
		assertTrue(SNRHelper.isModulusInput(modulusGroupInput));
		assertFalse(SNRHelper.isModulusInput(validGroupInput));

		// isZerosInput
		assertTrue(SNRHelper.isZerosInput(zeroInput));
		assertFalse(SNRHelper.isZerosInput(validInput));

		// countTrailingZeros
		assertEquals(SNRHelper.countTrailingZeros(modulusGroupInput), 1);

		// addTrailingZeros
		String output = SNRHelper.addTrailingZeros(validGroupInput, 2);

		assertEquals(output, "30500");

		// hasLeadingZeros
		assertTrue(SNRHelper.hasLeadingZeros(suffix));
		assertFalse(SNRHelper.hasLeadingZeros(validGroupInput));
	}

}
