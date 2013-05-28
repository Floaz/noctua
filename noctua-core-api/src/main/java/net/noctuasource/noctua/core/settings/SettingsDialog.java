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
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;



public final class SettingsDialog extends JDialog {
	
	private static final long serialVersionUID = 2477559122299472086L;
	
	

	SettingsDialog(	JFrame					aParent,
					String					aTitle,
					boolean					modal,
					final SettingsEditor	editor) {
		
		super(aParent, aTitle, modal);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);

		panel.add(editor.getEditorUI());

        

        JButton okButton = new JButton("Ok");
        okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				editor.saveSettings();
				dispose();
			}
		});
        
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				editor.saveSettings();
			}
		});
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
        
        JPanel buttonPanel = new JPanel();

        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(new JSeparator(), BorderLayout.NORTH);

        Box buttonBox = new Box(BoxLayout.X_AXIS);
        buttonBox.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10))); 
        buttonBox.add(okButton);
        buttonBox.add(Box.createHorizontalStrut(10));
        buttonBox.add(saveButton);
        buttonBox.add(Box.createHorizontalStrut(30));
        buttonBox.add(cancelButton);
      
        buttonPanel.add(buttonBox, BorderLayout.EAST);
        
		panel.add(buttonPanel, BorderLayout.PAGE_END);

	
        pack();
        setLocationRelativeTo(aParent);
        setVisible(true);
	}
	
}	