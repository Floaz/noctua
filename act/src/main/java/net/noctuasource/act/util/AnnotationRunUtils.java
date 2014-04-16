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
package net.noctuasource.act.util;

import java.lang.reflect.Method;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;



/**
 * Run PostConstruct and PreDestroy-Methods.
 *
 * @author Philipp Thomas
 */
public class AnnotationRunUtils {


	public static void runPostConstructMethods(Object object) {
		Method[] methods = object.getClass().getMethods();
		for(Method method : methods) {
			if(method.getAnnotation(PostConstruct.class) != null) {
				try {
					method.invoke(object);
				}
				catch(Exception ex) {
					throw new RuntimeException("Exception while invoking method. ", ex);
				}
			}
		}
	}


	public static void runPreDestroyMethods(Object object) {
		Method[] methods = object.getClass().getMethods();
		for(Method method : methods) {
			if(method.getAnnotation(PreDestroy.class) != null) {
				try {
					method.invoke(object);
				}
				catch(Exception ex) {
					throw new RuntimeException("Exception while invoking method. ", ex);
				}
			}
		}
	}


}

