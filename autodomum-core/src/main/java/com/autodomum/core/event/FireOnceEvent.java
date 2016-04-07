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
package com.autodomum.core.event;

import java.time.Clock;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Delayed event that is fired once at a specified time with a specific name
 * 
 * @author Kenny Colliander Nordin
 * @since 0.0.1
 */
public class FireOnceEvent implements DelayedEvent {
	private final long timestamp;

	private final String name;

	private final Clock clock;

	public FireOnceEvent(Clock clock, long timestamp, String name) {
		super();
		this.clock = clock;
		this.timestamp = timestamp;
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
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
		FireOnceEvent other = (FireOnceEvent) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (timestamp != other.timestamp)
			return false;
		return true;
	}

	/**
	 * @return the timestamp when the event should occur
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @return the name of the event
	 */
	public String getName() {
		return name;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		long diff = this.timestamp - clock.millis();
		return unit.convert(diff, TimeUnit.MILLISECONDS);
	}

	@Override
	public int compareTo(final Delayed o) {
		if (this.timestamp < ((FireOnceEvent) o).timestamp) {
			return -1;
		}

		if (this.timestamp > ((FireOnceEvent) o).timestamp) {
			return 1;
		}

		return 0;
	}

	@Override
	public String toString() {
		return "FireOnceEvent [timestamp=" + timestamp + ", name=" + name + "]";
	}
}
