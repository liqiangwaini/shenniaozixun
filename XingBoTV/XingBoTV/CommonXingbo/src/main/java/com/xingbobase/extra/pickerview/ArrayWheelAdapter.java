package com.xingbobase.extra.pickerview;

import java.util.ArrayList;

/**
 * The simple Array wheel adapter
 */
public class ArrayWheelAdapter implements WheelAdapter {
	
	/** The default items length */
	public static final int DEFAULT_LENGTH = 4;
	
	// items
	private ArrayList<WheelItem> items;
	// length
	private int length;

	/**
	 * Constructor
	 * @param items the items
	 * @param length the max items length
	 */
	public ArrayWheelAdapter(ArrayList<WheelItem> items, int length) {
		this.items = items;
		this.length = length;
	}
	
	/**
	 * Contructor
	 * @param items the items
	 */
	public ArrayWheelAdapter(ArrayList<WheelItem> items) {
		this(items, DEFAULT_LENGTH);
	}

	@Override
	public String getItem(int index) {
		if (index >= 0 && index < items.size()) {
			return items.get(index).getTitle();
		}
		return null;
	}

	@Override
	public int getItemsCount() {
		return items.size();
	}

	@Override
	public int getMaximumLength() {
		return length;
	}

}
