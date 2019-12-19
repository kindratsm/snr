package com.github.kindratsm.snr.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Language Helper unit test
 * 
 * @author Stanislav Kindrat
 *
 */
public class LangHelperTest {

	Object nullObject;
	Object notNullObject;
	Integer[] array;
	Pattern pattern;
	String input1;
	String input2;

	@BeforeEach
	void init() {
		nullObject = null;
		notNullObject = Integer.MIN_VALUE;
		array = new Integer[] { 1, 2, 3 };
		pattern = Pattern.compile("\\d+");
		input1 = "1234567890";
		input2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	}

	@Test
	void test() {
		// Check data
		assertNull(nullObject);
		assertNotNull(notNullObject);
		assertNotNull(array);
		assertEquals(array.length, 3);
		assertNotNull(pattern);
		assertNotNull(input1);
		assertNotNull(input2);

		// isNull
		assertTrue(LangHelper.isNull(nullObject));
		assertFalse(LangHelper.isNull(notNullObject));

		// isNotNull
		assertTrue(LangHelper.isNotNull(notNullObject));
		assertFalse(LangHelper.isNotNull(nullObject));

		// notNull
		assertThrows(IllegalArgumentException.class, () -> {
			LangHelper.notNull(nullObject);
		});

		// isInRange
		assertTrue(LangHelper.isInRange(array, 1));
		assertFalse(LangHelper.isInRange(array, 3));

		// isOutOfRange
		assertTrue(LangHelper.isOutOfRange(array, 3));
		assertFalse(LangHelper.isOutOfRange(array, 1));

		// inRange
		assertThrows(IllegalArgumentException.class, () -> {
			LangHelper.inRange(array, 3);
		});

		// isMatch
		assertTrue(LangHelper.isMatch(pattern, input1));
		assertFalse(LangHelper.isMatch(pattern, input2));

		// isNotMatch
		assertTrue(LangHelper.isNotMatch(pattern, input2));
		assertFalse(LangHelper.isNotMatch(pattern, input1));
	}

}
