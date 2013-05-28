
package net.noctuasource.act.test;

import java.util.LinkedList;
import java.util.Queue;
import net.noctuasource.act.controller.SubContextController;
import net.noctuasource.act.routine.SequenceContextController;




/**
 *
 * @author Philipp Thomas
 */
public class TestSequenceController extends SequenceContextController {

	Queue<Class<? extends SubContextController>> queue = new LinkedList<>();

	public TestSequenceController() {
		queue.add(ControllerTestNode.class);
		queue.add(ControllerTestNode.class);
		queue.add(ControllerTestNode.class);
	}


	@Override
	protected Class<? extends SubContextController> getNextController() {
		return queue.poll();
	}

}
