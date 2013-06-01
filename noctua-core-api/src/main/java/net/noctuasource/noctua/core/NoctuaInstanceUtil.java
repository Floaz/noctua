
package net.noctuasource.noctua.core;

import net.noctuasource.act.controller.ContextController;
import net.noctuasource.act.util.ContextTreeUtil;




/**
 * NoctuaInstanceUtil.
 * @author Philipp Thomas
 */
public abstract class NoctuaInstanceUtil {

	public static final String NOCTUA_INSTANCE_CONTROLLER = "NoctuaInstanceController";



	/**
	 * Walk the tree up to NoctuaInstanceController and returns it.
	 */
	public static ContextController getNoctuaInstance(ContextController currentController) {
		return ContextTreeUtil.getFirstControllerByName(NOCTUA_INSTANCE_CONTROLLER, currentController);
	}


	/**
	 * Walk the tree up to NoctuaInstanceController and returns it.
	 */
	public static void destroyNoctuaInstance(ContextController currentController) {
		ContextTreeUtil.destroyByFirstControllerByName(NOCTUA_INSTANCE_CONTROLLER, currentController);
	}
}
