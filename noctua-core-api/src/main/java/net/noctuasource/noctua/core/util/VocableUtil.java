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
package net.noctuasource.noctua.core.util;

import java.util.List;

import net.noctuasource.noctua.core.model.FlashCard;
import net.noctuasource.noctua.core.model.FlashCardElement;
import net.noctuasource.noctua.core.model.FlashCardElementType;







public class VocableUtil {

	public static void addWordsToVocable(FlashCard vocable,
										 String[] foreignWords,
										 String[] nativeWords) {
		for(String wordText : foreignWords) {
			if(!wordText.trim().isEmpty()) {
				FlashCardElement element = new FlashCardElement();
				element.setValue(wordText.trim());
				element.setType(FlashCardElementType.CONTENT);
				vocable.addElement(element);
			}
		}

		for(String wordText : nativeWords) {
			if(!wordText.trim().isEmpty()) {
				FlashCardElement element = new FlashCardElement();
				element.setValue(wordText.trim());
				element.setType(FlashCardElementType.EXPLANATION);
				vocable.addElement(element);
			}
		}
	}

//	public static void addSentencesToVocable(FlashCard vocable,
//											 String[] foreignSentences) {
//		for(String wordText : foreignSentences) {
//			if(!wordText.trim().isEmpty()) {
//				FlashCardElement element = new FlashCardElement();
//				element.setValue(wordText.trim());
//				element.setType(FlashCardElementType.SENTENCE_CONTENT);
//				vocable.addElement(element);
//			}
//		}
//	}

	public static void addAddInfoToVocable(FlashCard vocable,
											 String[] addInfo) {
		for(String wordText : addInfo) {
			if(!wordText.trim().isEmpty()) {
				FlashCardElement element = new FlashCardElement();
				element.setValue(wordText.trim());
				element.setType(FlashCardElementType.ADDITIONAL_INFO_CONTENT);
				vocable.addElement(element);
			}
		}
	}



	public static boolean validateVocable(FlashCard vocable) {
		List<FlashCardElement> elements = vocable.getElements();

		int foreignWords = 0;
		int nativeWords = 0;

		for(FlashCardElement element : elements) {
			if(element.getType() == FlashCardElementType.CONTENT) {
				++foreignWords;
			}
			else if(element.getType() == FlashCardElementType.EXPLANATION) {
				++nativeWords;
			}
		}

		return foreignWords != 0 && nativeWords != 0;
	}


	public static boolean validateVocableElements(List<FlashCardElement> elements) {
		int foreignWords = 0;
		int nativeWords = 0;

		for(FlashCardElement element : elements) {
			if(element.getType() == FlashCardElementType.CONTENT) {
				++foreignWords;
			}
			else if(element.getType() == FlashCardElementType.EXPLANATION) {
				++nativeWords;
			}
		}

		return foreignWords != 0 && nativeWords != 0;
	}




}
