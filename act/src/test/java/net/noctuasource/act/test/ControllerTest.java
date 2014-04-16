/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.noctuasource.act.test;

import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import net.noctuasource.act.controller.ContextController;
import net.noctuasource.act.controller.RootContextController;
import net.noctuasource.act.events.DefaultControllerEventListener;
import net.noctuasource.act.util.WholeControllerTreePrinter;
import org.junit.Test;




/**
 * Test of controller data.
 * @author Philipp Thomas
 */
public class ControllerTest {


	@Test
	public void dataTreeTest() {
		Integer testDataObject = new Integer(0);

		final RootContextController root = RootContextController.createRootController();
		root.addExecutor("default", Executors.newSingleThreadExecutor());
		root.setDefaultExecutor("default");
		root.getLocalControllerData().set("counter", testDataObject);

		final Semaphore endLock = new Semaphore(0);
		root.addControllerEventListener(new DefaultControllerEventListener() {
			@Override
			public void onAfterControllerDestroyed(ContextController destroyedController) {
				if(root == destroyedController) {
					endLock.release();
				}
			}
		});

		root.createController(TestSequenceController.class);

		WholeControllerTreePrinter printer = new WholeControllerTreePrinter(root, true);
		printer.printToConsole();

		try {
			endLock.acquire();
		} catch (InterruptedException ex) {
			// nothing
		}
	}
}