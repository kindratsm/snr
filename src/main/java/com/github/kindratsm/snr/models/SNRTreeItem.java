package com.github.kindratsm.snr.models;

import java.util.HashSet;
import java.util.Set;

import com.github.kindratsm.snr.helpers.LangHelper;
import com.github.kindratsm.snr.helpers.SNRHelper;

/**
 * SNR Tree Item which represents a tree of output items
 * 
 * @author Stanislav Kindrat
 *
 */
public class SNRTreeItem {

	private final String input;
	private final Set<SNRTreeItem> children = new HashSet<SNRTreeItem>();

	/**
	 * Root SNR Tree Item constructor
	 */
	public SNRTreeItem() {
		this.input = null;
	}

	/**
	 * SNR Tree Item constructor based on input
	 * 
	 * @param input the input
	 */
	public SNRTreeItem(String input) {
		SNRHelper.validateGroup(input);

		this.input = input;
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
	 * Children getter
	 * 
	 * @return set of children
	 */
	public Set<SNRTreeItem> getChildren() {
		return children;
	}

	/**
	 * Method to add child based on input
	 * 
	 * @param input the input
	 */
	public void addChild(String input) {
		children.add(new SNRTreeItem(input));
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
		if (obj instanceof SNRTreeItem) {
			return this.input.equals(((SNRTreeItem) obj).input);
		}

		return false;
	}

}
