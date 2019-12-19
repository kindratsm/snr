package com.github.kindratsm.snr.engines;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.github.kindratsm.snr.enums.SNRCountry;
import com.github.kindratsm.snr.enums.SNRLanguage;
import com.github.kindratsm.snr.models.SNRPhoneNumber;

/**
 * SNR Engine unit tests
 * 
 * @author Stanislav Kindrat
 *
 */
public class SNREngineTest {

	@Test
	void simpleInput1() {
		String input = "15";

		String[] output = SNREngine.getInstance(SNRLanguage.ENGLISH)
				.recognize(input);

		assertNotNull(output);
		assertEquals(output.length, 1);
		assertTrue(Arrays.asList(output).contains("15"));
	}

	@Test
	void simpleInput2() {
		String input = "15";

		String[] output = SNREngine.getInstance(SNRLanguage.GREEK)
				.recognize(input);

		assertNotNull(output);
		assertEquals(output.length, 2);
		assertTrue(Arrays.asList(output).contains("15"));
		assertTrue(Arrays.asList(output).contains("105"));
	}

	@Test
	void simpleInput3() {
		String input = "25";

		String[] output = SNREngine.getInstance(SNRLanguage.ENGLISH)
				.recognize(input);

		assertNotNull(output);
		assertEquals(output.length, 2);
		assertTrue(Arrays.asList(output).contains("25"));
		assertTrue(Arrays.asList(output).contains("205"));
	}

	@Test
	void simpleInput4() {
		String input = "305";

		String[] output = SNREngine.getInstance(SNRLanguage.ENGLISH)
				.recognize(input);

		assertNotNull(output);
		assertEquals(output.length, 2);
		assertTrue(Arrays.asList(output).contains("305"));
		assertTrue(Arrays.asList(output).contains("3005"));
	}

	@Test
	void simpleInput5() {
		String input = "350";

		String[] output = SNREngine.getInstance(SNRLanguage.ENGLISH)
				.recognize(input);

		assertNotNull(output);
		assertEquals(output.length, 2);
		assertTrue(Arrays.asList(output).contains("350"));
		assertTrue(Arrays.asList(output).contains("30050"));
	}

	@Test
	void simpleInput6() {
		String input = "350 5";

		String[] output = SNREngine.getInstance(SNRLanguage.ENGLISH)
				.recognize(input);

		assertNotNull(output);
		assertEquals(output.length, 4);
		assertTrue(Arrays.asList(output).contains("3505"));
		assertTrue(Arrays.asList(output).contains("355"));
		assertTrue(Arrays.asList(output).contains("300505"));
		assertTrue(Arrays.asList(output).contains("30055"));
	}

	@Test
	void simpleInput7() {
		String input = "305 5";

		String[] output = SNREngine.getInstance(SNRLanguage.ENGLISH)
				.recognize(input);

		assertNotNull(output);
		assertEquals(output.length, 2);
		assertTrue(Arrays.asList(output).contains("3055"));
		assertTrue(Arrays.asList(output).contains("30055"));
	}

	@Test
	void complexInput1() {
		String input = "2 10 6 9 30 6 6 4";

		String[] output = SNREngine.getInstance(SNRLanguage.ENGLISH)
				.recognize(input);

		assertNotNull(output);
		assertEquals(output.length, 2);
		assertTrue(Arrays.asList(output).contains("2106930664"));
		assertTrue(Arrays.asList(output).contains("210693664"));
	}

	@Test
	void complexInput2() {
		String input = "2 10 69 30 6 6 4";

		String[] output = SNREngine.getInstance(SNRLanguage.ENGLISH)
				.recognize(input);

		assertNotNull(output);
		assertEquals(output.length, 4);
		assertTrue(Arrays.asList(output).contains("2106930664"));
		assertTrue(Arrays.asList(output).contains("210693664"));
		assertTrue(Arrays.asList(output).contains("2106093664"));
		assertTrue(Arrays.asList(output).contains("21060930664"));
	}

	@Test
	void complexInput3() {
		String input = "0 0 30 69 700 24 1 3 50 2";

		String[] output = SNREngine.getInstance(SNRLanguage.ENGLISH)
				.recognize(input);

		assertNotNull(output);
		assertEquals(output.length, 16);
		assertTrue(Arrays.asList(output).contains("0030697002413502"));
		assertTrue(Arrays.asList(output).contains("003069700241352"));
		assertTrue(Arrays.asList(output).contains("00306972413502"));
		assertTrue(Arrays.asList(output).contains("00306097241352"));
	}
	
	SNRPhoneNumber findPhoneInArray(SNRPhoneNumber[] array, String interpretation) {
		return Arrays.stream(array)
				.filter(phone -> phone.getInterpretation().equals(interpretation))
				.findFirst()
				.orElse(null);
	}

	@Test
	void phoneInput1() {
		String input = "2 10 6 9 30 6 6 4";

		SNRPhoneNumber[] output = SNREngine.getInstance(SNRLanguage.ENGLISH)
				.recognize(SNRCountry.GREECE, input);

		assertNotNull(output);
		assertEquals(output.length, 2);
		
		SNRPhoneNumber phone1 = findPhoneInArray(output, "2106930664");
		SNRPhoneNumber phone2 = findPhoneInArray(output, "210693664");
		
		assertNotNull(phone1);
		assertNotNull(phone2);
		assertTrue(phone1.isValid());
		assertFalse(phone2.isValid());
	}
	
	@Test
	void phoneInput2() {
		String input = "2 10 69 30 6 6 4";

		SNRPhoneNumber[] output = SNREngine.getInstance(SNRLanguage.ENGLISH)
				.recognize(SNRCountry.GREECE, input);

		assertNotNull(output);
		assertEquals(output.length, 4);
		
		SNRPhoneNumber phone1 = findPhoneInArray(output, "2106930664");
		SNRPhoneNumber phone2 = findPhoneInArray(output, "210693664");
		SNRPhoneNumber phone3 = findPhoneInArray(output, "2106093664");
		SNRPhoneNumber phone4 = findPhoneInArray(output, "21060930664");

		assertNotNull(phone1);
		assertNotNull(phone2);
		assertNotNull(phone3);
		assertNotNull(phone4);
		assertTrue(phone1.isValid());
		assertFalse(phone2.isValid());
		assertTrue(phone3.isValid());
		assertFalse(phone4.isValid());
	}

}
