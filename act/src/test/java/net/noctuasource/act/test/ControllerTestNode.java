
package net.noctuasource.act.test;

import net.noctuasource.act.controller.RunLater;
import net.noctuasource.act.controller.SubContextController;




/**
 *
 * @author Philipp Thomas
 */
public class ControllerTestNode extends SubContextController {

	static int uzuz = 0;

	private int id = -1;


	@Override
	protected void onCreate() {
		id = ++uzuz;
		System.out.println("ControllerTestNode create" + id);
	}

	@Override
	protected void onDestroy() {
		System.out.println("ControllerTestNode destroy " + id);
	}


	@RunLater
	public void laterDestroy() {
		System.out.println("ControllerTestNode laterDestroy " + id);
		destroy();
	}
}
