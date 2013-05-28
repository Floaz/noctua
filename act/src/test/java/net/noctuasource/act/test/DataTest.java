/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.noctuasource.act.test;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import net.noctuasource.act.controller.RootContextController;
import org.junit.Test;




/**
 * Test of controller data.
 * @author Philipp Thomas
 */
public class DataTest {


	@Test
	public void dataTreeTest() {
		Integer testDataObject = new Integer(10);

		RootContextController root = RootContextController.createRootController();
		root.setExecutor(new ScheduledThreadPoolExecutor(1));
		root.getLocalControllerData().set("test", testDataObject);

		root.executeController(TestNode.class);
	}
}