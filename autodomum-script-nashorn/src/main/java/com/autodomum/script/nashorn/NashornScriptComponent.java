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
package com.autodomum.script.nashorn;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.autodomum.core.event.EventComponent;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

/**
 * Component for loading Javascript events. Read more about Java's
 * <code>Nashorn</code> Javascript engine on what extensions that are available.
 * 
 * @author Kenny Colliander Nordin
 * @since 0.0.1
 */
@SuppressWarnings("restriction")
@Component
@Scope
public class NashornScriptComponent {

	private static final Logger LOG = LoggerFactory.getLogger(NashornScriptComponent.class);

	private ScriptEngine scriptEngine;

	private EventComponent eventComponent;

	/**
	 * Replace the current script engine and initialize with a new script
	 * 
	 * @param inputStream
	 *            the script
	 * @throws ScriptException
	 */
	public void replaceScript(final InputStream inputStream) throws ScriptException {
		this.scriptEngine = new NashornScriptEngineFactory().getScriptEngine("-strict");
		this.scriptEngine.put("eventComponent", this.eventComponent);
		this.scriptEngine.put("LOG", LOG);
		this.scriptEngine.eval(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
	}

	@Autowired
	public void setEventComponent(EventComponent eventComponent) {
		this.eventComponent = eventComponent;
	}
}
