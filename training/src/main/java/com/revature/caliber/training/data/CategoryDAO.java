package com.revature.caliber.training.data;

import java.util.List;

import com.revature.caliber.training.beans.Category;

public interface CategoryDAO {

	/**
	 * 
	 * Returns an individual category
	 * 
	 */
	public Category getCategory(String category);
	
	/**
	 * 
	 * Returns a list of all categories
	 * 
	 */
	public List<Category> getAllCategories();
	
}
