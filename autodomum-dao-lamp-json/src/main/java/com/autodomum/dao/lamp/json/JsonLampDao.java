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
package com.autodomum.dao.lamp.json;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.autodomum.core.dao.LampDao;
import com.autodomum.core.event.EventComponent;
import com.autodomum.core.event.LampStateChangedEvent;
import com.autodomum.core.model.Lamp;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This is a static JSON implementation of a data access object for lamps. All
 * lamps should be declared in /lamps.json in the resource directory.
 * 
 * @author Kenny Colliander Nordin
 * @since 0.0.1
 */
@Repository
@Scope
public class JsonLampDao implements LampDao {
	private static final Logger LOG = LoggerFactory.getLogger(JsonLampDao.class);

	private final Map<String, Lamp> lamps = new ConcurrentHashMap<>();

	private EventComponent eventComponent;

	/**
	 * Load an JSON array of <code>Lamp</code> objects into DAO
	 * 
	 * @param inputStream
	 *            the JSON array of <code>Lamp</code> objects.
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void load(InputStream inputStream) throws JsonParseException, JsonMappingException, IOException {
		final ObjectMapper objectMapper = new ObjectMapper();
		Lamp[] lamps = objectMapper.readValue(inputStream, Lamp[].class);
		if (lamps != null) {
			for (Lamp lamp : lamps) {
				this.lamps.put(lamp.getId(), lamp);
			}
		}
	}

	@Override
	public Lamp getLamp(final String id) {
		Lamp lamp = this.lamps.get(id);
		if (lamp != null) {
			return (Lamp) lamp.clone();
		}
		return null;
	}

	@Override
	public void updateLamp(final String id, final Lamp lamp) {
		final Lamp current = this.lamps.get(id);

		if (current != null) {
			boolean modified = false;
			if (lamp.getOn() != null && lamp.getOn() != current.getOn()) {
				current.setOn(lamp.getOn());
				modified = true;
			}
			if (lamp.getX() != null && lamp.getX() != current.getX()) {
				current.setX(lamp.getX());
				modified = true;
			}
			if (lamp.getY() != null && lamp.getY() != current.getY()) {
				current.setY(lamp.getY());
				modified = true;
			}

			if (modified) {
				LOG.info("Updated {}", current);
			}

			this.eventComponent.publish(new LampStateChangedEvent(current));
		}
	}

	@Override
	public Lamp[] getLamps() {
		final List<Lamp> lamps = new ArrayList<>();

		this.lamps.values().forEach(lamp -> {
			lamps.add((Lamp) lamp.clone());
		});

		return lamps.toArray(new Lamp[lamps.size()]);
	}

	@Autowired
	public void setEventComponent(EventComponent eventComponent) {
		this.eventComponent = eventComponent;
	}
}
