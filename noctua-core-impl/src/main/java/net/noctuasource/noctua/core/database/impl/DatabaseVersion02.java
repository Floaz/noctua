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
package net.noctuasource.noctua.core.database.impl;

import java.sql.Connection;
import java.sql.Statement;

import net.noctuasource.noctua.core.database.AbstractDatabaseVersion;




public class DatabaseVersion02 extends AbstractDatabaseVersion {

	private static final int VERSION_NUMBER = 2;


	public DatabaseVersion02() {
		super(VERSION_NUMBER);
	}



	@Override
	public void updateDatabase(Connection conn) throws Exception {
		Statement statement = null;

		try {

			statement = conn.createStatement();
			String cCreateTreeNodeTable = "";
			cCreateTreeNodeTable += "DROP TABLE languages;";
			statement.execute( cCreateTreeNodeTable);
			statement.close();

			statement = conn.createStatement();
			String cDropTable = "";
			cDropTable += "DROP TABLE wordtypes;";
			statement.execute( cDropTable);
			statement.close();

			statement = conn.createStatement();
			cDropTable = "";
			cDropTable += "DROP TABLE sentences;";
			statement.execute( cDropTable);
			statement.close();

			statement = conn.createStatement();
			cDropTable = "";
			cDropTable += "DROP TABLE additionalinfo;";
			statement.execute( cDropTable);
			statement.close();

			statement = conn.createStatement();
			cDropTable = "";
			cDropTable += "DROP TABLE vocabulary;";
			statement.execute( cDropTable);
			statement.close();

			statement = conn.createStatement();
			cDropTable = "";
			cDropTable += "DROP TABLE words;";
			statement.execute( cDropTable);
			statement.close();

			statement = conn.createStatement();
			cDropTable = "";
			cDropTable += "DROP TABLE vocableprotocols;";
			statement.execute( cDropTable);
			statement.close();

			statement = conn.createStatement();
			cDropTable = "";
			cDropTable += "DROP TABLE vocabletestprotocols;";
			statement.execute( cDropTable);
			statement.close();

			statement = conn.createStatement();
			cDropTable = "";
			cDropTable += "DROP TABLE testprotocols;";
			statement.execute( cDropTable);
			statement.close();

			statement = conn.createStatement();
			cDropTable = "";
			cDropTable += "DROP TABLE treenodetestprotocols;";
			statement.execute( cDropTable);
			statement.close();

			statement = conn.createStatement();
			cDropTable = "";
			cDropTable += "DROP TABLE vocgroupfolders;";
			statement.execute( cDropTable);
			statement.close();

			statement = conn.createStatement();
			cDropTable = "";
			cDropTable += "DROP TABLE vocgroups;";
			statement.execute( cDropTable);
			statement.close();

			statement = conn.createStatement();
			cDropTable = "";
			cDropTable += "DROP TABLE roottreenodes;";
			statement.execute( cDropTable);
			statement.close();

			statement = conn.createStatement();
			cDropTable = "";
			cDropTable += "DROP TABLE treenodes;";
			statement.execute( cDropTable);
			statement.close();



			statement = conn.createStatement();
			String cCreateTable = "";
			cCreateTable += "CREATE TABLE TreeNodes (";
			cCreateTable += "TreeNodeId VARCHAR(64) PRIMARY KEY, ";
			cCreateTable += "TreeNodeName TEXT, ";
			cCreateTable += "TreeNodeParent INTEGER, ";
			cCreateTable += "TreeNodeType INTEGER";
			cCreateTable += ")";
			statement.execute(cCreateTable);
			statement.close();

			statement = conn.createStatement();
			cCreateTable = "";
			cCreateTable += "CREATE TABLE Languages ( ";
			cCreateTable += "TreeNodeId VARCHAR(64) PRIMARY KEY, ";
			cCreateTable += "LanguageCode VARCHAR(5) )";
			statement.execute( cCreateTable);
			statement.close();

			statement = conn.createStatement();
			cCreateTable = "";
			cCreateTable += "CREATE TABLE Folders (";
			cCreateTable += "TreeNodeId VARCHAR(64) PRIMARY KEY, ";
			cCreateTable += "FolderExpanded BOOLEAN ";
			cCreateTable += ")";
			statement.execute( cCreateTable);
			statement.close();

			statement = conn.createStatement();
			cCreateTable = "";
			cCreateTable += "CREATE TABLE Flashcardgroups (";
			cCreateTable += " TreeNodeId VARCHAR(64) PRIMARY KEY, ";
			cCreateTable += " MaxFlashCardGroups INTEGER ";
			cCreateTable += ")";
			statement.execute( cCreateTable);
			statement.close();

			statement = conn.createStatement();
			cCreateTable = "";
			cCreateTable += "CREATE TABLE Flashcards (";
			cCreateTable += " FlashCardId VARCHAR(64) PRIMARY KEY, ";
			cCreateTable += " TreeNodeId INTEGER, ";
			cCreateTable += " FlashCardLevel INTEGER ";
			cCreateTable += ")";
			statement.execute( cCreateTable);
			statement.close();

			statement = conn.createStatement();
			cCreateTable = "";
			cCreateTable += "CREATE TABLE Flashcardelements (";
			cCreateTable += " FlashCardElementId VARCHAR(64) PRIMARY KEY, ";
			cCreateTable += " FlashCardId INTEGER, ";
			cCreateTable += " Type INTEGER, ";
			cCreateTable += " Value TEXT ";
			cCreateTable += ")";
			statement.execute( cCreateTable);
			statement.close();

			statement = conn.createStatement();
			cCreateTable = "";
			cCreateTable += "CREATE TABLE Vocabulary (";
			cCreateTable += " FlashCardId VARCHAR(64) PRIMARY KEY, ";
			cCreateTable += " Gender INTEGER, ";
			cCreateTable += " WordType INTEGER ";
			cCreateTable += ")";
			statement.execute( cCreateTable);
			statement.close();







//			Integer languageTreeNodeType = 3;
//
//			statement = conn.createStatement();
//			String cCreateLanguagesTable = "";
//			cCreateLanguagesTable += "INSERT INTO treenodes (TreeNodeName, TreeNodeType)";
//			cCreateLanguagesTable += "SELECT LanguageName, \"" + languageTreeNodeType.toString() + "\"";
//			cCreateLanguagesTable += "FROM languages";
//			statement.execute( cCreateLanguagesTable);
//			statement.close();




		}
		finally {
			if(statement != null) {
				statement.close();
			}
		}
	}

}
