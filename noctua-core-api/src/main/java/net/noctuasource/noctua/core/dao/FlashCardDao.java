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
package net.noctuasource.noctua.core.dao;


import net.noctuasource.noctua.core.model.FlashCard;
import net.noctuasource.noctua.core.model.FlashCardElement;

public interface FlashCardDao {

	public FlashCard getFlashCardById(String id);

	public void insert(FlashCard treeNode);

	public void update(FlashCard treeNode);

	public void delete(FlashCard treeNode);


	public void insertElement(FlashCardElement element);

	public void updateElement(FlashCardElement element);

	public void deleteElement(FlashCardElement element);

}



