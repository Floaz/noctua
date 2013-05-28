
package net.noctuasource.noctua.core.impl.launcher;

import net.noctuasource.act.controller.ContextController;
import net.noctuasource.act.util.ContextTreeUtil;




/**
 * NoctuaInstanceUtil.
 * @author Philipp Thomas
 */
public abstract class NoctuaInstanceUtil {


	/**
	 * Walk the tree up to NoctuaInstanceController and returns it.
	 */
	public static NoctuaInstanceController getNoctuaInstance(ContextController currentController) {
		return ContextTreeUtil.getFirstControllerType(NoctuaInstanceController.class, currentController);
	}


	/**
	 * Walk the tree up to NoctuaInstanceController and returns it.
	 */
	public static void destroyNoctuaInstance(ContextController currentController) {
		ContextTreeUtil.destroyByFirstControllerType(NoctuaInstanceController.class, currentController);
	}
}
