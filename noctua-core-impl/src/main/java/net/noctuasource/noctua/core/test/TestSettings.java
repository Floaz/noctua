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
package net.noctuasource.noctua.core.test;






public class TestSettings {
	
	enum FetchMethod {
		RANDOM,
		FIFO,
		KARTEIKASTEN
	}
	
	
	private Integer				numberOfWords = null;
	private FetchMethod			fetchMethod = FetchMethod.RANDOM;
	private boolean				timeLimit = false;
	private int					timeLimitSeconds = 30;
	private QuestionDirection	direction = QuestionDirection.CONTENT_TO_EXPLANATION;
	
	
	
	public Integer getNumberOfWords() {
		return numberOfWords;
	}
	public void setNumberOfWords(Integer numberOfWords) {
		this.numberOfWords = numberOfWords;
	}
	public FetchMethod getFetchMethod() {
		return fetchMethod;
	}
	public void setFetchMethod(FetchMethod fetchMethod) {
		this.fetchMethod = fetchMethod;
	}
	public boolean isTimeLimit() {
		return timeLimit;
	}
	public void setTimeLimit(boolean timeLimit) {
		this.timeLimit = timeLimit;
	}
	public int getTimeLimitSeconds() {
		return timeLimitSeconds;
	}
	public void setTimeLimitSeconds(int timeLimitSeconds) {
		this.timeLimitSeconds = timeLimitSeconds;
	}
	public QuestionDirection getDirection() {
		return direction;
	}
	public void setDirection(QuestionDirection direction) {
		this.direction = direction;
	}

}
