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
package com.autodomum.core.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Lamp resource
 * 
 * @author Kenny Colliander Nordin
 * @since 0.0.1
 */
public class Lamp implements Cloneable {
	private String id;

	private String name;

	private Boolean on;

	private Integer x;

	private Integer y;

	private List<String> callIds;

	/**
	 * Default constructor
	 */
	public Lamp() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            the id of the lamp
	 * @param name
	 *            the name
	 * @param on
	 *            true if power is on, otherwise false
	 * @param x
	 *            the x coordinate in the overview
	 * @param y
	 *            the y coordinate in the overview
	 * @param callIds
	 *            all the callIds that are assigned to this lamp
	 */
	public Lamp(String id, String name, Boolean on, Integer x, Integer y, List<String> callIds) {
		super();
		this.id = id;
		this.name = name;
		this.on = on;
		this.x = x;
		this.y = y;
		this.callIds = callIds;
	}

	/**
	 * @return the lamp id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the lamp id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name of the lamp
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name of the lamp
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return true if the power is on, otherwise false
	 */
	public Boolean getOn() {
		return on;
	}

	/**
	 * @param on
	 *            true if the power is on, otherwise false
	 */
	public void setOn(Boolean on) {
		this.on = on;
	}

	/**
	 * @return the x coordinate in the overview
	 */
	public Integer getX() {
		return x;
	}

	/**
	 * @param x
	 *            the x coordinate in the overview
	 */
	public void setX(Integer x) {
		this.x = x;
	}

	/**
	 * @return the y coordinate in the overview
	 */
	public Integer getY() {
		return y;
	}

	/**
	 * @param y
	 *            the y coordinate in the overview
	 */
	public void setY(Integer y) {
		this.y = y;
	}

	/**
	 * @return all call ids that are associated to the lamp for power on and off
	 */
	public List<String> getCallIds() {
		return callIds;
	}

	/**
	 * @param callIds
	 *            all call ids that are associated to the lamp for power on and
	 *            off
	 */
	public void setCallIds(List<String> callIds) {
		this.callIds = callIds;
	}

	@Override
	public Object clone() {
		final Lamp copy = new Lamp();
		copy.setId(id);
		copy.setName(name);
		copy.setOn(on);
		copy.setX(x);
		copy.setY(y);

		final List<String> ids = this.callIds;
		if (ids != null) {
			copy.setCallIds(new ArrayList<>(ids));
		}
		return copy;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((callIds == null) ? 0 : callIds.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((on == null) ? 0 : on.hashCode());
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		result = prime * result + ((y == null) ? 0 : y.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lamp other = (Lamp) obj;
		if (callIds == null) {
			if (other.callIds != null)
				return false;
		} else if (!callIds.equals(other.callIds))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (on == null) {
			if (other.on != null)
				return false;
		} else if (!on.equals(other.on))
			return false;
		if (x == null) {
			if (other.x != null)
				return false;
		} else if (!x.equals(other.x))
			return false;
		if (y == null) {
			if (other.y != null)
				return false;
		} else if (!y.equals(other.y))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Lamp [id=" + id + ", name=" + name + ", on=" + on + ", x=" + x + ", y=" + y + ", callIds=" + callIds
				+ "]";
	}
}