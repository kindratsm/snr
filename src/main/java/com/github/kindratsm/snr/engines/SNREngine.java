package com.github.kindratsm.snr.engines;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.github.kindratsm.snr.enums.SNRCountry;
import com.github.kindratsm.snr.enums.SNRLanguage;
import com.github.kindratsm.snr.helpers.LangHelper;
import com.github.kindratsm.snr.helpers.SNRHelper;
import com.github.kindratsm.snr.models.SNRGroup;
import com.github.kindratsm.snr.models.SNRPhoneNumber;
import com.github.kindratsm.snr.models.SNRTreeItem;

/**
 * String Number Recognition (SNR) Engine Contain logic how to recognize
 * provided input and return any possible number interpretations
 * 
 * @author Stanislav Kindrat
 *
 */
public class SNREngine {

	private static final Map<SNRLanguage, SNREngine> SNR_ENGINE_CACHE = new ConcurrentHashMap<>();

	private final SNRLanguage language;

	/**
	 * SNR Engine internal constructor based on language
	 * 
	 * @param language the language
	 */
	private SNREngine(SNRLanguage language) {
		this.language = language;
	}

	/**
	 * Entry point to process SNR group Recognize does a group is single number,
	 * zeros input, modulus or complex and process group based on type
	 * 
	 * @param groups the SNR groups array
	 * @param index  the current SNR group index
	 * @return array of possible number interpretations on current SNR group
	 */
	private String[] processGroup(SNRGroup[] groups, int index) {
		LangHelper.notNull(groups);
		LangHelper.inRange(groups, index);

		// Get current group
		final SNRGroup currentGroup = groups[index];

		LangHelper.notNull(currentGroup);

		if (currentGroup.getInput().length() == 1 || SNRHelper.isZerosInput(currentGroup.getInput())) {
			return new String[] { currentGroup.getInput() };
		} else if (SNRHelper.isModulusInput(currentGroup.getInput())) {
			return processModulusGroup(groups, index, currentGroup);
		} else {
			return processComplexGroup(currentGroup.getInput());
		}
	}

	/**
	 * Method to process modulus group (e.g. 10, 2000, 3330) First step: process
	 * modulus group as complex group Second step: build a set of possible modulus
	 * interpretations based on next group
	 * 
	 * @param groups       the SNR groups array
	 * @param index        the current SNR group index
	 * @param currentGroup the current SNR group
	 * @return possible number interpretations for modulus group
	 */
	private String[] processModulusGroup(SNRGroup[] groups, int index, SNRGroup currentGroup) {
		LangHelper.notNull(groups);
		LangHelper.inRange(groups, index);
		LangHelper.notNull(currentGroup);

		// Process modulus group as complex group
		final String[] modulusItems = processComplexGroup(currentGroup.getInput());

		final Set<String> items = new HashSet<>();

		// Get next group if exists
		final int nextIndex = index + 1;
		final SNRGroup nextGroup;
		if (LangHelper.isInRange(groups, nextIndex)) {
			nextGroup = groups[nextIndex];
		} else {
			nextGroup = null;
		}

		final boolean hasNextGroup = LangHelper.isNotNull(nextGroup) && !SNRHelper.isZerosInput(nextGroup.getInput());

		for (String modulus : modulusItems) {
			items.add(modulus);

			// Check does modulus have other possible interpretations based on next group
			if (hasNextGroup) {
				final int modulusLength = modulus.length();
				final int nextGroupLength = nextGroup.getInput().length();
				final int zerosCount = SNRHelper.countTrailingZeros(modulus);
				if (zerosCount >= nextGroupLength) {
					final String extraItem = modulus.substring(0,
							modulusLength - Math.min(zerosCount, nextGroupLength));
					final int value = Integer.parseInt(String.format("%s%s", extraItem, nextGroup.getInput()));

					// Check modulus limit
					// e.g. in English pronounce 15 can be interpreted only as 15
					// while in Greek pronounce 15 can be interpreted as 15 and 10 5
					if (value > language.getLimit()) {
						items.add(extraItem);
					}
				}
			}
		}

		return items.toArray(new String[items.size()]);
	}

	/**
	 * Method to process complex group (e.g. 35, 305, 1025)
	 * 
	 * @param input the complex group input
	 * @return possible number interpretations for complex group
	 */
	private String[] processComplexGroup(String input) {
		LangHelper.notNull(input);

		final Set<String> items = new HashSet<String>();
		items.add(input);

		// Check input limit
		// e.g. in English pronounce 15 can be interpreted only as 15
		// while in Greek pronounce 15 can be interpreted as 15 and 10 5
		final int value = Integer.parseInt(input);
		if (value > language.getLimit()) {
			final int length = input.length();
			for (int i = length - 1; i > 0; i--) {
				String prefix = SNRHelper.addTrailingZeros(input.substring(0, i), length - i);
				String suffix = input.substring(i);

				if (SNRHelper.isZerosInput(suffix) || SNRHelper.hasLeadingZeros(suffix)) {
					continue;
				} else if (suffix.length() == 1) {
					// Suffix is single number
					items.add(String.format("%s%s", prefix, suffix));
				} else {
					// Suffix is complex group
					String[] subItems = processComplexGroup(suffix);
					for (String subItem : subItems) {
						items.add(String.format("%s%s", prefix, subItem));
					}
				}
			}
		}

		return items.toArray(new String[items.size()]);
	}

	/**
	 * Method to add children to latest level of SNR tree
	 * 
	 * @param item     the SNR tree item
	 * @param children the array of children
	 */
	private void addChildrenToLatestLevel(SNRTreeItem item, String[] children) {
		LangHelper.notNull(item);
		LangHelper.notNull(children);

		if (item.getChildren().size() > 0) {
			for (SNRTreeItem child : item.getChildren()) {
				addChildrenToLatestLevel(child, children);
			}
		} else {
			for (String child : children) {
				item.addChild(child);
			}
		}
	}

	/**
	 * Method to build all possible output combinations
	 * 
	 * @param combinations the set of combinations
	 * @param item         the current SNR tree item
	 * @param combination  the current combination sequence
	 */
	private void buildCombinations(Set<String> combinations, SNRTreeItem item, String combination) {
		LangHelper.notNull(combinations);
		LangHelper.notNull(item);

		if (LangHelper.isNotNull(item.getInput())) {
			if (LangHelper.isNull(combination)) {
				combination = item.getInput();
			} else {
				combination += item.getInput();
			}
		}

		if (item.getChildren().size() > 0) {
			for (SNRTreeItem child : item.getChildren()) {
				buildCombinations(combinations, child, combination);
			}
		} else {
			combinations.add(combination);
		}
	}

	/**
	 * Method to build array of any possible number interpretations based on SNR
	 * tree
	 * 
	 * @param root the SNR root tree item
	 * @return array of any possible number interpretations based on SNR tree
	 */
	private String[] buildOutput(SNRTreeItem root) {
		LangHelper.notNull(root);

		final Set<String> combinations = new HashSet<>();

		buildCombinations(combinations, root, null);

		if (combinations.size() > 0) {
			return combinations.toArray(new String[combinations.size()]);
		}

		return null;
	}

	/**
	 * Method to recognize input and build array of any possible number
	 * interpretations
	 * 
	 * @param input the input
	 * @return array of any possible number interpretations based on SNR tree
	 */
	public String[] recognize(String input) {
		LangHelper.notNull(input);

		SNRTreeItem root = new SNRTreeItem();

		SNRGroup[] groups = SNRHelper.splitInputToGroups(input);
		for (int i = 0; i < groups.length; i++) {
			final String[] children = processGroup(groups, i);
			if (LangHelper.isNotNull(children)) {
				addChildrenToLatestLevel(root, children);
			}
		}

		return buildOutput(root);
	}

	/**
	 * Method to recognize input and build array of any possible phone numbers
	 * interpretations
	 * 
	 * @param country the country
	 * @param input the input
	 * @return array of any possible phone numbers interpretations
	 */
	public SNRPhoneNumber[] recognize(SNRCountry country, String input) {
		LangHelper.notNull(country);

		// Recognize string combinations
		String[] combinations = recognize(input);

		if (LangHelper.isNull(combinations)) {
			return null;
		}

		SNRPhoneNumber[] phoneNumbers = new SNRPhoneNumber[combinations.length];

		// Fill phone numbers array based on combinations
		for (int i = 0; i < combinations.length; i++) {
			phoneNumbers[i] = new SNRPhoneNumber(country, combinations[i]);
		}

		return phoneNumbers;
	}

	/**
	 * Thread-safe method to get SNR Engine instance based on language
	 * 
	 * @param language the language
	 * @return SNR Engine
	 */
	public static SNREngine getInstance(SNRLanguage language) {
		LangHelper.notNull(language);

		synchronized (language) {
			SNREngine engine = SNR_ENGINE_CACHE.get(language);
			if (engine == null) {
				engine = new SNREngine(language);

				// Cache SNR engine
				SNR_ENGINE_CACHE.put(language, engine);
			}

			return engine;
		}
	}

}
