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
package net.noctuasource.act.spring;



import net.noctuasource.act.controller.ContextController;
import net.noctuasource.act.events.DefaultControllerEventListener;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;






/**
 * SpringAutowireControllerEventListener.
 * @author Philipp Thomas
 */
public class SpringAutowireControllerEventListener extends DefaultControllerEventListener {

	// -- Basic Static Members ------------------------

	static final Logger	logger = Logger.getLogger(SpringAutowireControllerEventListener.class);


	// -- Members ------------------------------

	private String		contextDataKey;




	// -- Methods -------------------------------

	public SpringAutowireControllerEventListener(String contextDataKey) {
		this.contextDataKey = contextDataKey;
	}



	@Override
	public void onBeforeControllerCreated(ContextController createdController) {
		logger.debug("Inject spring beans to controller...");

		ApplicationContext context = getApplicationContext(createdController);
		if(context == null) {
			logger.debug("No ApplicationContext found in tree!");
			return;
		}

		SpringBeanInjector injector = new SpringBeanInjector();
		injector.setApplicationContext(context);
		injector.inject(createdController.getController());
	}


	private ApplicationContext getApplicationContext(ContextController createdController) {
		return createdController.getControllerData().get(contextDataKey, ApplicationContext.class);
	}

}
