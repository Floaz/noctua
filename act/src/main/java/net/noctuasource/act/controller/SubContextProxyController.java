/**
 * This file is part of Noctua.
 *
 * Copyright (C) 2013 Philipp Rene Thomas <info@noctuasource.net>
 *
 * Noctua is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Noctua is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Noctua.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.noctuasource.act.controller;

import java.lang.reflect.Method;
import net.noctuasource.act.annotation.ControllerContext;

/**
 * Abstract superclass for building a context-oriented application controller tree (ACT).
 *
 * http://www.contextualprogramming.org/articles/the-controller-model-view-architecture-and-the-application-controller-tree/
 *
 * @author Philipp Thomas
 */
public class SubContextProxyController extends AbstractContextController {

	private final Object controllerObject;


    /**
     * Contructs a SubContextController.
	 * @param controllerObject
     */
    public SubContextProxyController(Object controllerObject) {
		this.controllerObject = controllerObject;
	}


	/**
	 * Injects this controller node into the controller object.
	 */
	@Override
	protected void onCreate() {
		injectControllerNode();
	}


	@Override
	public Object getController() {
		return controllerObject;
	}


	public void injectControllerNode() {
		Method[] methods = controllerObject.getClass().getMethods();
		for(Method method : methods) {
			if(method.getAnnotation(ControllerContext.class) != null) {
				try {
					method.invoke(controllerObject, this);
				}
				catch(Exception ex) {
					throw new RuntimeException("Exception while invoking method. ", ex);
				}
			}
		}
	}

}

