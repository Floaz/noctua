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
package net.noctuasource.noctua.core.settings;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.apache.log4j.Logger;





public class SettingsEditor {
	
	private static final Logger logger = Logger.getLogger(SettingsEditor.class);
	
	
	private List<SettingsPage> settingsPages;
	
	
	
	
	public SettingsEditor(List<SettingsPage> settingsPages) {
		this.settingsPages = settingsPages;
	}


	protected JComponent getEditorUI() {
		
		if(settingsPages.size() == 1) {
			return settingsPages.get(0).getUI();
		}

		
		JPanel contentPane = new SettingsPanel();
		return contentPane;
	}
    
	
	protected void saveSettings() {
		logger.debug("Save settings...");
		
		for (SettingsPage page : settingsPages) {
			page.savePreferences();
		}
	}

	
	
	
	private class SettingsPanel extends JPanel {

		private static final long serialVersionUID = 1225768212325153077L;
		
		
		
		//private Component	currentComponent = null;
		
		
		public SettingsPanel() {
			
			setLayout(new BorderLayout());
			
			JTabbedPane topTabbedPane = new JTabbedPane();
			add(topTabbedPane, BorderLayout.CENTER);
			
//			JButtonBar buttonBar = new JButtonBar(JButtonBar.HORIZONTAL);
//			buttonBar.setUI(new BlueishButtonBarUI());
//			add("Nord", buttonBar);
//			
//			ButtonGroup group = new ButtonGroup();
			
			
			
			Map<SettingsCategory, List<SettingsPage>> catMap =
						new HashMap<SettingsCategory, List<SettingsPage>>();
			
			List<SettingsCategory> categories = new LinkedList<SettingsCategory>();
			
			
			
			for(SettingsPage page : settingsPages) {
				SettingsCategory category = page.getCategory();
				
				if(!catMap.containsKey(category)) {
					List<SettingsPage> list = new LinkedList<SettingsPage>();
					list.add(page);
					catMap.put(category, list);
				} else {
					catMap.get(category).add(page);
				}
				
				if(!categories.contains(category)) {
					categories.add(category);
				}
			}
			
			
			Comparator<SettingsCategory> catComp = new Comparator<SettingsCategory>() {
				@Override
				public int compare(SettingsCategory arg0, SettingsCategory arg1) {
					return arg0.getPriority() - arg1.getPriority();
				}
			};
			
			Collections.sort(categories, catComp);
			
			
			
			Comparator<SettingsPage> pageComp = new Comparator<SettingsPage>() {
				@Override
				public int compare(SettingsPage arg0, SettingsPage arg1) {
					return arg0.getPriority() - arg1.getPriority();
				}
			};
			
			
			for(SettingsCategory category : categories) {

				List<SettingsPage> pageList = catMap.get(category);
				
				Collections.sort(pageList, pageComp);

				
				JTabbedPane tabbedPane = new JTabbedPane();
				
				int idx = 0;
				
				for(SettingsPage page : pageList) {
					JComponent editorGui = page.getUI();
					tabbedPane.addTab(page.getTitle(), editorGui);
					tabbedPane.setMnemonicAt(idx, page.getMnemonic());
					
					++idx;
				}
				
				addButton(category.getName(), category.getIcon(),
							tabbedPane, topTabbedPane);
				
//				addButton(category.getName(), category.getIcon(),
//							tabbedPane, buttonBar, group);
			}
		}
		
		
		
		
		
	
	
	
//		private void addButton(String title, ImageIcon icon,
//								final Component component,
//								JButtonBar bar, ButtonGroup group) {
//
//			Action action = new AbstractAction(title, icon) {
//				private static final long serialVersionUID = 5353463146463456L;
//	
//				public void actionPerformed(ActionEvent e) {
//					showTab(component);
//				}
//			};
//			
//			JToggleButton button = new JToggleButton(action);
//			
//			bar.add(button);
//	
//			group.add(button);
//			
//			if (group.getSelection() == null) {
//				button.setSelected(true);
//				showTab(component);
//			}
//		}
		
		private void addButton(String title, ImageIcon icon,
				final Component component, JTabbedPane pane) {

			pane.add(title, component);
		}
	
//	    private void showTab(Component component) {
//	        if(currentComponent != null) {
//	        	remove(currentComponent);
//	        }
//	        
//	        add("Center", currentComponent = component);
//	        
//	        revalidate();
//	        repaint();
//	    }
	}

}
