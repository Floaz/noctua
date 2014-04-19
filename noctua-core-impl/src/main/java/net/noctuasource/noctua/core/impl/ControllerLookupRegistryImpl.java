/**
 * This file is part of Noctua.
 *
 * Copyright (C) 2013 Philipp Rene Thomas <info@noctuasource.net>
 *
 * Noctua is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Noctua is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Noctua.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.noctuasource.noctua.core.impl;


import net.noctuasource.act.factory.DefaultControllerLookupRegistry;
import net.noctuasource.noctua.core.datastore.impl.DatastoreInitContextController;
import net.noctuasource.noctua.core.test.impl.TestInitController;
import net.noctuasource.noctua.core.ui.editor.EditorView;
import net.noctuasource.noctua.core.ui.mainwindow.AboutView;
import net.noctuasource.noctua.core.ui.mainwindow.AddFlashCardGroupView;
import net.noctuasource.noctua.core.ui.mainwindow.AddFolderView;
import net.noctuasource.noctua.core.ui.mainwindow.AddLanguageView;
import net.noctuasource.noctua.core.ui.mainwindow.DeleteTreeNodeView;
import net.noctuasource.noctua.core.ui.mainwindow.MainWindowView;
import net.noctuasource.noctua.core.ui.mainwindow.MoveTreeNodeView;
import net.noctuasource.noctua.core.ui.mainwindow.RenameTreeNodeView;
import net.noctuasource.noctua.core.ui.mainwindow.UnitBrowserTabView;
import net.noctuasource.noctua.core.ui.profiles.ProfileChooseView;
import net.noctuasource.noctua.core.ui.test.MultipleChoiceTestView;
import net.noctuasource.noctua.core.ui.test.NormalTestView;
import net.noctuasource.noctua.core.ui.test.SchoolTestViewImpl;
import net.noctuasource.noctua.core.ui.test.TooFewFlashCardsMessageDialog;
import net.noctuasource.noctua.core.ui.vocable.AddVocabularyView;
import net.noctuasource.noctua.core.ui.vocable.DeleteVocabularyView;
import net.noctuasource.noctua.core.ui.vocable.MoveVocabularyView;
import net.noctuasource.noctua.core.ui.vocable.UnitMenuView;
import net.noctuasource.noctua.update.oldversion.CopyOldDatastoreToOld;





public class ControllerLookupRegistryImpl extends DefaultControllerLookupRegistry {



	public ControllerLookupRegistryImpl() {
		addControllerClass("testInit", TestInitController.class);

		addControllerClass("datastoreInit", DatastoreInitContextController.class);
		addControllerClass("copyOldData", CopyOldDatastoreToOld.class);



		addControllerClass("profileChooseController", ProfileChooseController.class);
		addControllerClass("profileContextController", ProfileContextController.class);


		//
		// Views
		//
		addControllerClass("profileChooseView", ProfileChooseView.class);

		addControllerClass("mainWindowView", MainWindowView.class);
		addControllerClass("aboutView", AboutView.class);
		addControllerClass("unitBrowserTabView", UnitBrowserTabView.class);

		addControllerClass("addLanguageView", AddLanguageView.class);
		addControllerClass("addFolderView", AddFolderView.class);
		addControllerClass("addFlashCardGroupView", AddFlashCardGroupView.class);
		addControllerClass("renameTreeNodeView", RenameTreeNodeView.class);
		addControllerClass("moveTreeNodeView", MoveTreeNodeView.class);
		addControllerClass("deleteTreeNodeView", DeleteTreeNodeView.class);

		addControllerClass("normalTestView", NormalTestView.class);
		addControllerClass("multipleChoiceTestView", MultipleChoiceTestView.class);
		addControllerClass("schoolTestView", SchoolTestViewImpl.class);

		addControllerClass("unitMenuView", EditorView.class);
		addControllerClass("addVocabularyView", AddVocabularyView.class);
		addControllerClass("editVocableView", UnitMenuView.class);
		addControllerClass("moveVocabularyView", MoveVocabularyView.class);
		addControllerClass("deleteVocabularyView", DeleteVocabularyView.class);

		addControllerClass("tooFewFlashCardsMessageDialog", TooFewFlashCardsMessageDialog.class);


	}

}
