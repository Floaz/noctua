
package net.noctuasource.noctua.core.impl.launcher;

import com.google.common.eventbus.Subscribe;
import java.io.IOException;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import net.noctuasource.act.annotation.ControllerContext;
import net.noctuasource.act.controller.ContextController;
import net.noctuasource.act.annotation.RunLater;
import net.noctuasource.act.data.ControllerParamsBuilder;
import net.noctuasource.act.factory.ControllerFactoryRegistry;
import net.noctuasource.act.spring.SpringAutowireControllerEventListener;
import net.noctuasource.act.spring.SpringDefaultConstants;
import net.noctuasource.act.util.AfterDestroyRunnable;
import net.noctuasource.noctua.core.ExecutorIdentifiers;
import net.noctuasource.noctua.core.NoctuaInstanceUtil;
import net.noctuasource.noctua.core.impl.ProfileChosenEvent;
import net.noctuasource.noctua.core.impl.SignOffProfileEvent;
import net.noctuasource.noctua.core.ui.loading.LoadingScreenManager;
import net.noctuasource.util.ApplicationLockFile;
import net.noctuasource.util.LockException;
import org.apache.log4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;




/**
 * NoctuaInstanceController.
 * @author Philipp Thomas
 */
public class NoctuaInstanceController {

	// -- Basic Static Members ------------------------

	static final Logger		logger = Logger.getLogger(NoctuaInstanceController.class);


	// -- Static Members ------------------------------

	private static final String		CONTEXT_FILE = "/spring/application-context.xml";

	private static final String		LOCK_KEY = "Noctua4jkhk390fmlsdf904943h94";
	private static final boolean	LOCK_SEPERATE_USERS = true;

	private static final String		LOCK_FILE_DATA_KEY = "lockFile";



	// -- Members -------------------------------------

	private ContextController					contextController;

	private ConfigurableApplicationContext		applicationContext;

	private ApplicationLockFile					lockFile = null;




	@PostConstruct
	public void onCreation() {
		contextController.setControllerName(NoctuaInstanceUtil.NOCTUA_INSTANCE_CONTROLLER);
		contextController.registerEventListener(this);
	}


	@PreDestroy
	public void onDestroying() {
		contextController.unregisterEventListener(this);

		if(applicationContext != null) {
			applicationContext.close();
		}

		if(lockFile != null) {
			try {
				lockFile.release();
			} catch (IOException ex) {
				logger.error("Can't release application lock!");
			}
		}
	}




	@RunLater(executor=ExecutorIdentifiers.BACKGROUND_EXECUTOR)
	public void init() {
		if(!lockRun()) {
			return;
		}


		// Load and refresh application context.
		applicationContext = new ClassPathXmlApplicationContext(CONTEXT_FILE);
		String contextDataKey = SpringDefaultConstants.DEFAULT_CONTEXT_DATA_KEY;
		contextController.getControllerData().set(contextDataKey, applicationContext);
		contextController.addControllerEventListener(new SpringAutowireControllerEventListener(contextDataKey));

		ControllerFactoryRegistry controllerLookupRegistry = applicationContext.getBean(ControllerFactoryRegistry.class);
		contextController.addControllerLookupRegistry(controllerLookupRegistry);


		ContextController datastoreInitController = contextController.createController("datastoreInit");

		AfterDestroyRunnable.create(datastoreInitController, new Runnable() {
			@Override
			public void run() {
				onDatastoreInitialized();
			}
		});
	}


	private void onDatastoreInitialized() {
		contextController.createController("profileChooseController");
	}


	@Subscribe
	public void onProfileChosen(ProfileChosenEvent event) {
		LoadingScreenManager.get().showNormalLoadingScreen();

		ControllerParamsBuilder builder = ControllerParamsBuilder.create();
		builder.add("profile", event.getProfile());
		contextController.createController("profileContextController", builder.build());
	}


	@Subscribe
	public void onSignOffProfile(SignOffProfileEvent event) {
		ControllerParamsBuilder builder = ControllerParamsBuilder.create();
		builder.add("resetDefaultProfile", Boolean.TRUE);
		contextController.createController("profileChooseController", builder.build());
	}



	protected boolean lockRun() {
		Properties lockProps = new Properties();
		lockProps.setProperty("pid", "0000");

        lockFile = new ApplicationLockFile(LOCK_KEY, LOCK_SEPERATE_USERS);

        try {
        	lockFile.lock(lockProps);

			contextController.getControllerData().set(LOCK_FILE_DATA_KEY, lockFile);
			return true;
        } catch(LockException e) {
        	logger.error("Lock failed! An instance of Noctua already running!");
        	//lockProps = lockFile.readProperties();
			// Show dialog;
			contextController.destroy();
			return false;
        }
	}


	@ControllerContext
	public void setContextController(ContextController contextController) {
		this.contextController = contextController;
	}


}
