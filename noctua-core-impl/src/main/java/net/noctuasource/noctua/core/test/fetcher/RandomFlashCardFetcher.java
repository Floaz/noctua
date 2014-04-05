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
package net.noctuasource.noctua.core.test.fetcher;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.noctuasource.noctua.core.model.FlashCard;
import net.noctuasource.noctua.core.model.FlashCardGroup;
import net.noctuasource.noctua.core.test.impl.FlashCardFetcher;
import net.noctuasource.noctua.core.business.GroupList;
import net.noctuasource.noctua.core.test.TestData;
import net.noctuasource.noctua.core.test.impl.TestSettings;

public class RandomFlashCardFetcher implements FlashCardFetcher {

	// ***** Members ******************************************************** //

	private Set<FlashCard>	flashCards = new HashSet<FlashCard>();

	private int				counter = 0;





	// ***** Constructor **************************************************** //

	public RandomFlashCardFetcher(TestData data) {
		GroupList groups = (GroupList) data.get(TestData.GROUP_LIST);

//		for(FlashCardGroup group : groups) {
//			flashCards.addAll(group.getFlashCards());
//		}

		TestSettings settings = (TestSettings) data.get(TestData.TEST_SETTINGS);
		counter = settings.getNumberOfWords();
	}




	// ***** Methods ******************************************************** //

	@Override
	public FlashCard getNextFlashCard() {
		Random rand = new Random();
		int nextId = rand.nextInt(flashCards.size());
		FlashCard flashCard = (FlashCard) flashCards.toArray()[nextId];
		flashCards.remove(flashCard);
		--counter;
		return flashCard;
	}

	@Override
	public boolean hasNextFlashCard() {
		return counter > 0 && !flashCards.isEmpty();
		//return (flashCards.size() - history.getCount()) > 0;
	}




	@Override
	public int getRemainingCount() {
		return Math.min(counter, flashCards.size());
	}


}
