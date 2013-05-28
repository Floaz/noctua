
package net.noctuasource.noctua.core.impl.launcher;

import com.google.common.eventbus.Subscribe;
import java.io.IOException;
import java.util.Properties;
import net.noctuasource.act.controller.ContextController;
import net.noctuasource.act.controller.RunLater;
import net.noctuasource.act.controller.SubContextController;
import net.noctuasource.act.data.ControllerParamsBuilder;
import net.noctuasource.act.util.AfterDestroyRunnable;
import net.noctuasource.noctua.core.impl.ProfileChosenEvent;
import net.noctuasource.util.ApplicationLockFile;
import net.noctuasource.util.LockException;
import org.apache.log4j.Logger;




/**
 * NoctuaInstanceController.
 * @author Philipp Thomas
 */
public class NoctuaInstanceController extends SubContextController {

	// -- Basic Static Members ------------------------

	static final Logger		logger = Logger.getLogger(NoctuaInstanceController.class);


	// -- Static Members ------------------------------

	private static final String		LOCK_KEY = "Noctua4jkhk390fmlsdf904943h94";
	private static final boolean	LOCK_SEPERATE_USERS = true;

	private static final String		LOCK_FILE_DATA_KEY = "lockFile";



	// -- Members -------------------------------------

	private ApplicationLockFile		lockFile = null;




	@Override
	protected void onCreate() {
		registerEventListener(this);
	}


	@Override
	protected void onDestroy() {
		unregisterEventListener(this);

		if(lockFile == null) {
			try {
				lockFile.release();
			} catch (IOException ex) {
				logger.error("Can't release application lock!");
			}
		}
	}




	@RunLater
	public void init() {
		lockRun();


		ContextController datastoreInitController = executeController("datastoreInit");

		AfterDestroyRunnable.create(datastoreInitController, new Runnable() {
			@Override
			public void run() {
				onDatastoreInitialized();
			}
		});
	}


	private void onDatastoreInitialized() {
		executeController("profileChooseController");
	}


	@Subscribe
	public void onProfileChosen(ProfileChosenEvent event) {
		ControllerParamsBuilder builder = ControllerParamsBuilder.create();
		builder.add("profile", event.getProfile());
		executeController("profileContextController", builder.build());
	}



	protected void lockRun() {
		Properties lockProps = new Properties();
		lockProps.setProperty("pid", "0000");

        lockFile = new ApplicationLockFile(LOCK_KEY, LOCK_SEPERATE_USERS);

        try {
        	lockFile.lock(lockProps);

			getControllerData().set(LOCK_FILE_DATA_KEY, lockFile);
        } catch(LockException e) {
        	logger.error("Lock failed! An instance of Noctua already running!");
        	//lockProps = lockFile.readProperties();
			// Show dialog;
        }
	}

}
