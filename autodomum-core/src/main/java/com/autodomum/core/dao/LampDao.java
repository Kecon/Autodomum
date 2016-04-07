/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Kenny Colliander Nordin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.autodomum.core.dao;

import com.autodomum.core.model.Lamp;

/**
 * Interface for the lamp data access object implementation.
 * 
 * @author Kenny Colliander Nordin
 * @since 0.0.1
 */
public interface LampDao {

	/**
	 * Get lamp for id
	 * 
	 * @param id
	 * @return the lamp or null
	 */
	Lamp getLamp(String id);

	/**
	 * Update a lamp
	 * 
	 * @param id
	 *            the id of the lamp
	 * @param lamp
	 *            the lamp to update, note that only fields that are non-null
	 *            will be updated, and id may not be updated.
	 */
	void updateLamp(String id, Lamp lamp);

	/**
	 * Get all available lamps
	 * 
	 * @return all lamps
	 */
	Lamp[] getLamps();

}