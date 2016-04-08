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
package com.autodomum.example;

import java.io.InputStream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import com.autodomum.core.daylight.Coordinate;
import com.autodomum.core.event.EventComponent;
import com.autodomum.core.event.EventContext;
import com.autodomum.core.event.LampStateChangedEvent;
import com.autodomum.dao.lamp.json.JsonLampDao;
import com.autodomum.provider.telldus.TelldusComponent;
import com.autodomum.script.nashorn.NashornScriptComponent;

/**
 * Example1 application using Spring Boot.
 * Initializes with ai.js which contains all event rules.
 * 
 * @author Kenny Colliander Nordin
 * @since 0.0.1
 */
@Configuration
@ComponentScan(basePackages = "com.autodomum.*")
@Import(RepositoryRestMvcAutoConfiguration.class)
@EnableAutoConfiguration
@PropertySource("application.properties")
public class Example1 {

	public static void main(String[] args) throws Exception {
		final ConfigurableApplicationContext context = SpringApplication.run(Example1.class, args);
		final JsonLampDao jsonLampDao = context.getBean(JsonLampDao.class);
		final EventComponent eventComponent = context.getBean(EventComponent.class);
		final EventContext eventContext = context.getBean(EventContext.class);
		final TelldusComponent telldusComponent = context.getBean(TelldusComponent.class);
		final NashornScriptComponent nashornScriptComponent = context.getBean(NashornScriptComponent.class);

		final Thread eventComponentThread = new Thread(eventComponent);
		eventComponentThread.setDaemon(true);
		eventComponentThread.setPriority(Thread.MIN_PRIORITY);
		eventComponentThread.setName("EventComponent");
		eventComponentThread.start();

		// Coordinates of Stockholm
		eventContext.setCoordinate(new Coordinate(18.063240d, 59.334591d));

		eventComponent.updateEventContext();
		eventComponent.register(LampStateChangedEvent.class, telldusComponent);

		final Thread telldusComponentThread = new Thread(telldusComponent);
		telldusComponentThread.setDaemon(true);
		telldusComponentThread.setPriority(Thread.MIN_PRIORITY);
		telldusComponentThread.setName("Telldus");
		telldusComponentThread.start();

		try (InputStream inputStream = Example1.class.getResourceAsStream("/lamps.json")) {
			jsonLampDao.load(inputStream);
		}

		try (InputStream inputStream = Example1.class.getResourceAsStream("/ai.js")) {
			nashornScriptComponent.replaceScript(inputStream);
		}
	}
}
