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
package net.noctuasource.noctua.core.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;


public final class StyleConstants {

	public static String FRAME_ICON = "/images/FrameIcon.png";

	public static Color[] HEADER_PANEL = new Color[] {new Color(51,62,102),
													  new Color(115,175,212)};

	public static Color BUTTON_PANEL_BG = new Color(201,213,223);
	public static Color BUTTON_PANEL_BORDER = new Color(136,136,136);

	public static Color DESCRIPTION_FIELD = new Color(210,223,234);

	public static Color WINDOW_BACKGROUND = new Color(226,237,248);

	public static Border INNER_BORDER = new EmptyBorder(new Insets(5, 10, 5, 10));


	public static Font MAIN_TAB_FONT = new Font("Verdana", Font.BOLD, 11);

	public static Font HEADER_TITLE_FONT = new Font("Verdana", Font.BOLD, 24);

	public static Font BOLD_DESC_FONT = new Font("Verdana", Font.BOLD, 11);

}
