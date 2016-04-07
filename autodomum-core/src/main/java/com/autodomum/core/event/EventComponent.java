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
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.autodomum.core.daylight.Daylight;
import com.autodomum.core.holiday.Holiday;

/**
 * Handle all event execution via JavaScript and update
 * <code>EventContext</code>.
 * 
 * @author Kenny Colliander Nordin
 * @since 0.0.1
 */
@Component
@Scope
public class EventComponent implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(EventComponent.class);

	private final Map<Class<?>, List<EventCallback>> eventCallbacks = new ConcurrentHashMap<>();

	protected final DelayQueue<DelayedEvent> delayedEvents = new DelayQueue<>();

	private Clock clock = Clock.systemUTC();

	private EventContext eventContext;

	private Daylight daylight;

	private Holiday holiday;

	@Override
	public void run() {
		try {
			LOG.info("Started!");
			try {
				this.updateEventContext();
				this.publish(new StartupEvent());
				while (true) {
					this.updateEventContext();
					this.handleDelayedEvent();
				}
			} catch (InterruptedException e) {
			}
			this.publish(new ShutdownEvent());
			LOG.info("Exit!");
		} catch (Exception e) {
			LOG.error("Unknown error ocurred in EventComponent", e);
		}
	}

	/**
	 * Retrieve and handle events
	 * 
	 * @throws InterruptedException
	 */
	void handleDelayedEvent() throws InterruptedException {
		final DelayedEvent delayedEvent = this.delayedEvents.poll(1, TimeUnit.SECONDS);

		if (delayedEvent != null) {
			this.publish(delayedEvent);
		}
	}

	/**
	 * @return the current time
	 */
	public Date getNow() {
		return new Date();
	}

	/**
	 * @return an calendar instance
	 */
	public Calendar getCalendar() {
		return Calendar.getInstance();
	}

	/**
	 * Update the event context with correct parameters
	 */
	public void updateEventContext() {
		final Date now = this.getNow();
		final Calendar tomorrowCalendar = this.getCalendar();
		tomorrowCalendar.setTime(now);

		this.eventContext.setHoliday(holiday.isHoliday(tomorrowCalendar));
		this.eventContext.setHour(tomorrowCalendar.get(Calendar.HOUR_OF_DAY));
		this.eventContext.setMinute(tomorrowCalendar.get(Calendar.MINUTE));

		tomorrowCalendar.add(Calendar.DATE, 1);
		final Date tomorrow = tomorrowCalendar.getTime();

		final Date sunrise = this.daylight.sunrise(eventContext.getCoordinate(), now);
		final Date sunset = this.daylight.sunset(eventContext.getCoordinate(), now);
		final Date sunriseTomorrow = this.daylight.sunrise(eventContext.getCoordinate(), tomorrow);
		final Date sunsetTomorrow = this.daylight.sunset(eventContext.getCoordinate(), tomorrow);

		boolean newDaylight = false;
		if (now.after(sunset)) {
			newDaylight = false;
			this.eventContext.setNextSunrise(sunriseTomorrow.getTime());
			this.eventContext.setNextSunset(sunsetTomorrow.getTime());

		} else if (now.after(sunrise)) {
			newDaylight = true;
			this.eventContext.setNextSunrise(sunriseTomorrow.getTime());
			this.eventContext.setNextSunset(sunset.getTime());
		} else {
			this.eventContext.setNextSunrise(sunrise.getTime());
			this.eventContext.setNextSunset(sunset.getTime());
		}

		if (newDaylight != this.eventContext.isDaylight()) {
			this.eventContext.setDaylight(newDaylight);

			if (newDaylight) {
				this.publish(new SunriseEvent());
			} else {
				this.publish(new SunsetEvent());
			}
		}
	}

	/**
	 * Register callback to receive events for a specific type
	 * 
	 * @param eventType
	 *            the event type that the callback should be assigned to
	 * @param eventCallback
	 *            the callback
	 */
	public synchronized void register(final Class<?> eventType, final EventCallback eventCallback) {
		checkEventType(eventType);
		checkEventCallback(eventCallback);

		List<EventCallback> callbacks = this.eventCallbacks.get(eventType);

		if (callbacks == null) {
			callbacks = new CopyOnWriteArrayList<>();
			this.eventCallbacks.put(eventType, callbacks);
		}

		LOG.debug("Registered callback {} for event type {}", eventCallback, eventType);
		callbacks.add(eventCallback);
	}

	/**
	 * Publish an event; will call all callbacks that has been registered with
	 * the supplied event
	 * 
	 * @param event
	 *            the event
	 */
	public void publish(final Event event) {
		if (event == null) {
			throw new NullPointerException("Event may not be null");
		}

		final List<EventCallback> callbacks = this.eventCallbacks.getOrDefault(event.getClass(),
				Collections.emptyList());

		LOG.debug("Fire {} for {} callbacks", event);
		for (final EventCallback callback : callbacks) {
			callback.work(this.eventContext, event);
		}
	}

	/**
	 * Unregister callback for a specific event
	 * 
	 * @param eventType
	 *            the event type
	 * @param eventCallback
	 *            the worker that should be removed
	 */
	public synchronized void unregister(final Class<?> eventType, final EventCallback eventCallback) {
		checkEventType(eventType);
		checkEventCallback(eventCallback);

		final List<EventCallback> callbacks = this.eventCallbacks.get(eventType);

		if (callbacks != null) {
			callbacks.remove(eventCallback);

			LOG.debug("Unregistered callback {} for event type {}", eventCallback, eventType);
		}
	}

	/**
	 * Register an <code>FireOnceEvent</code> for tomorrow
	 * 
	 * @param hour
	 *            the hour tomorrow
	 * @param minute
	 *            the minute tomorrow
	 * @param name
	 *            the name of the event
	 */
	public void registerEventOnceTomorrow(final int hour, final int minute, final String name) {
		if (hour < 0 || hour > 23) {
			throw new IllegalArgumentException("Hour is a 24h clock, acceptable values are 0-23: " + hour);
		}

		if (minute < 0 || minute > 59) {
			throw new IllegalArgumentException("Minute, acceptable values are 0-59: " + minute);
		}

		checkName(name);

		final Calendar calendar = this.getCalendar();
		calendar.add(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		this.addDelayedEvent(new FireOnceEvent(this.clock, calendar.getTimeInMillis(), name));
		LOG.debug("Added event {} to run {}", name, calendar.getTime());
	}

	/**
	 * Register a <code>FireOnceEvent</code> in the future
	 * 
	 * @param milliseconds
	 *            the amount of milliseconds in the future
	 * @param name
	 *            the name of the event
	 */
	public void registerEventOnce(final long milliseconds, final String name) {
		if (milliseconds < 0) {
			throw new IllegalArgumentException("Event must be in the future");
		}

		checkName(name);
		final long time = this.clock.millis() + milliseconds;
		this.addDelayedEvent(new FireOnceEvent(this.clock, time, name));
		LOG.debug("Added event {} to run {}", name, time);
	}

	/**
	 * Add a delayed event
	 * 
	 * @param delayed
	 *            the delayed event
	 */
	public void addDelayedEvent(final DelayedEvent delayed) {
		if (delayed == null) {
			throw new NullPointerException("Delayed event may not be null");
		}
		this.delayedEvents.put(delayed);
	}

	@Autowired
	public void setEventContext(EventContext eventContext) {
		this.eventContext = eventContext;
	}

	@Autowired
	public void setDaylight(Daylight daylight) {
		this.daylight = daylight;
	}

	@Autowired
	public void setHoliday(Holiday holiday) {
		this.holiday = holiday;
	}

	public void setClock(Clock clock) {
		this.clock = clock;
	}

	/**
	 * Verify that a name is set and not null
	 * 
	 * @param name
	 *            the name
	 */
	private void checkName(final String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Name must be set: " + name);
		}
	}

	/**
	 * Verify that the callback not is null
	 * 
	 * @param eventCallback
	 *            the callback
	 */
	private void checkEventCallback(final EventCallback eventCallback) {
		if (eventCallback == null) {
			throw new NullPointerException("EventCallback may not be null");
		}
	}

	/**
	 * Check that the event type not is null
	 * 
	 * @param eventType
	 *            the event type
	 */
	private void checkEventType(final Class<?> eventType) {
		if (eventType == null) {
			throw new NullPointerException("EventType may not be null");
		}
	}
}
