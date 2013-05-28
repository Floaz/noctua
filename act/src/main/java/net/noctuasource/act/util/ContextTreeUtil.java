
package net.noctuasource.act.util;

import net.noctuasource.act.controller.ContextController;




/**
 * ContextTreeUtil.
 * @author Philipp Thomas
 */
public abstract class ContextTreeUtil {



	/**
	 * Walk the tree up to ContextController and returns it.
	 */
	public static <T extends ContextController> T getFirstControllerType( Class<T> type,
															ContextController currentController) {
		while(currentController != null && !(type.isInstance(currentController))) {
			currentController = currentController.getParentController();
		}

		return (T) currentController;
	}


	/**
	 * Walk the tree up to specified ContextController and destroys it.
	 */
	public static void destroyByFirstControllerType(Class<? extends ContextController> type,
													ContextController currentController) {
		ContextController targetController = getFirstControllerType(type, currentController);

		if(targetController == null) {
			throw new RuntimeException("No controller found.");
		}

		targetController.destroy();
	}
}
