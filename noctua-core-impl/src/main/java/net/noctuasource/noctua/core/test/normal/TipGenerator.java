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
package net.noctuasource.noctua.core.test.normal;

import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import net.noctuasource.noctua.core.model.FlashCardElement;
import net.noctuasource.noctua.core.model.FlashCardElementType;
import net.noctuasource.noctua.core.test.QuestionContext;
import net.noctuasource.noctua.core.test.QuestionDirection;

public class TipGenerator {

	// ***** Basic Static Members ******************************************* //

	private static Logger logger = Logger.getLogger(TipGenerator.class);

	// ***** Static Members ************************************************* //

	// ***** Members ******************************************************** //

	private QuestionContext qc;
	
	
	// ***** Constructor **************************************************** //

	public TipGenerator(QuestionContext qc) {
		this.qc = qc;
	}
	
	
	// ***** Methods ******************************************************** //

	public String generate() {
		List<FlashCardElement> elements = qc.getFlashCard().getElementsOfType(
				qc.getDirection() == QuestionDirection.CONTENT_TO_EXPLANATION
				? FlashCardElementType.EXPLANATION
				: FlashCardElementType.CONTENT);
		
		if(elements.size() <= 0) {
			return "";
		}
		
		Random rand = new Random();
		FlashCardElement element = elements.get(rand.nextInt(elements.size()));
		
		return toStars(element.getValue());
	}
	
	
	public String toStars(String s) {
	    StringBuilder sb = new StringBuilder(s.length());
	    for (int i = 0; i < s.length(); i++) {
	    	if(s.charAt(i) == ' ' || s.charAt(i) == '\t' || s.charAt(i) == '\n'
	    		|| i < s.length() / 4) {
	    		sb.append(s.charAt(i));
	    	} else {
	    		sb.append('*');
	    	}
	    }
	    return sb.toString();
	}


}
