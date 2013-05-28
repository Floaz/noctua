
package net.noctuasource.act.test;

import net.noctuasource.act.controller.SubContextController;
import org.junit.Assert;




/**
 *
 * @author Philipp Thomas
 */
public class TestNode extends SubContextController {


	@Override
	protected void onCreate() {
		Assert.assertEquals(getControllerData().get("test", Integer.class), new Integer(10));
	}

}
