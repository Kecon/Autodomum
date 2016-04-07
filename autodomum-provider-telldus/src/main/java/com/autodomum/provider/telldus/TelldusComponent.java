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
package com.autodomum.provider.telldus;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.autodomum.core.event.Event;
import com.autodomum.core.event.EventCallback;
import com.autodomum.core.event.EventContext;
import com.autodomum.core.event.LampStateChangedEvent;
import com.autodomum.core.model.Lamp;

/**
 * Interface for using USB-devices from <a href="http://telldus.se/">Telldus</a>
 * for managing power switches. Please read
 * <a href="http://developer.telldus.se/wiki/tellstickController">Telldus
 * developer forums</a> on how to install the <code>tdtool</code> that this
 * component is using for changing states on power switches.
 * <p>
 * Telldus is registered to Telldus Technologies AB, Sweden
 * </p>
 * 
 * @author Kenny Colliander Nordin
 * @since 0.0.1
 */
@Component
@Scope
public class TelldusComponent implements Runnable, EventCallback {

	private static final Logger LOG = LoggerFactory.getLogger(TelldusComponent.class);

	private BlockingQueue<Lamp> lampStates = new LinkedBlockingQueue<>();

	@Override
	public void work(final EventContext eventContext, final Event event) {
		if (event instanceof LampStateChangedEvent) {
			this.lampStates.add(((LampStateChangedEvent) event).getLamp());
		}
	}

	@Override
	public void run() {
		LOG.info("Started!");
		try {
			while (true) {
				final Lamp lamp = lampStates.poll(1, TimeUnit.HOURS);

				if (lamp != null && lamp.getCallIds() != null) {
					for (String callId : lamp.getCallIds()) {
						try {
							for (int i = 0; i < 3; i++) {
								if (lamp.getOn()) {
									Runtime.getRuntime().exec("tdtool --on " + callId);
									LOG.debug("Call: tdtool --on {}", callId);
								} else {
									Runtime.getRuntime().exec("tdtool --off " + callId);
									LOG.debug("Call: tdtool --off {}", callId);
								}
							}
						} catch (IOException e) {
							LOG.warn("Failed to call tdtool for {}", lamp, e);
						}
						Thread.sleep(1500);
					}
				}
			}
		} catch (InterruptedException e) {
		}
		LOG.info("Exit!");
	}
}
