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




public class DatabaseVersion01 extends AbstractDatabaseVersion {

	private static final int VERSION_NUMBER = 1;


	public DatabaseVersion01() {
		super(VERSION_NUMBER);
	}



	@Override
	public void updateDatabase(Connection conn) throws Exception {
		Statement statement = null;

		try {
			statement = conn.createStatement();
			String cCreateLanguagesTable = "";
			cCreateLanguagesTable += "CREATE TABLE languages (";
			cCreateLanguagesTable += "LanguageId INTEGER PRIMARY KEY, ";
			cCreateLanguagesTable += "LanguageName TEXT NOT NULL, ";
			cCreateLanguagesTable += "Flag TEXT";
			cCreateLanguagesTable += ")";
			statement.execute( cCreateLanguagesTable);
			statement.close();

			statement = conn.createStatement();
			String cCreateTreeNodeTable = "";
			cCreateTreeNodeTable += "CREATE TABLE treenodes (";
			cCreateTreeNodeTable += "TreeNodeId INTEGER PRIMARY KEY, ";
			cCreateTreeNodeTable += "TreeNodeName TEXT, ";
			cCreateTreeNodeTable += "TreeNodeParent INTEGER, ";
			cCreateTreeNodeTable += "TreeNodeType INTEGER";
			cCreateTreeNodeTable += ")";
			statement.execute( cCreateTreeNodeTable);
			statement.close();

			statement = conn.createStatement();
			String cCreateRootTreeNodeTable = "";
			cCreateRootTreeNodeTable += "CREATE TABLE roottreenodes (";
			cCreateRootTreeNodeTable += "LanguageId INTEGER, ";
			cCreateRootTreeNodeTable += "TreeNodeId INTEGER";
			cCreateRootTreeNodeTable += ")";
			statement.execute( cCreateRootTreeNodeTable);
			statement.close();

			statement = conn.createStatement();
			String cCreateVocGroupFoldersTable = "";
			cCreateVocGroupFoldersTable += "CREATE TABLE vocgroupfolders (";
			cCreateVocGroupFoldersTable += "TreeNodeId INTEGER PRIMARY KEY, ";
			cCreateVocGroupFoldersTable += "FolderExpanded BOOL";
			cCreateVocGroupFoldersTable += ")";
			statement.execute( cCreateVocGroupFoldersTable);
			statement.close();

			statement = conn.createStatement();
			String cCreateVocGroupsTable = "";
			cCreateVocGroupsTable += "CREATE TABLE vocgroups (";
			cCreateVocGroupsTable += "TreeNodeId INTEGER PRIMARY KEY, ";
			cCreateVocGroupsTable += "MaxFlashCardGroups INTEGER, ";
			cCreateVocGroupsTable += "VocGroupTested INTEGER";
			cCreateVocGroupsTable += ")";
			statement.execute( cCreateVocGroupsTable);
			statement.close();

			statement = conn.createStatement();
			String cCreateWordTypesTable = "";
			cCreateWordTypesTable += "CREATE TABLE wordtypes (";
			cCreateWordTypesTable += "WordTypeId INTEGER PRIMARY KEY, ";
			cCreateWordTypesTable += "WordType TEXT, ";
			cCreateWordTypesTable += "LanguageId INTEGER";
			cCreateWordTypesTable += ")";
			statement.execute( cCreateWordTypesTable);
			statement.close();

			statement = conn.createStatement();
			String cCreateVocablesTable = "";
			cCreateVocablesTable += "CREATE TABLE vocabulary (";
			cCreateVocablesTable += "VocableId INTEGER PRIMARY KEY, ";
			cCreateVocablesTable += "TreeNodeId INTEGER, ";
			cCreateVocablesTable += "FlashCardGroup INTEGER, ";
			cCreateVocablesTable += "Gender INTEGER, ";
			cCreateVocablesTable += "WordType INTEGER ";
			cCreateVocablesTable += ")";
			statement.execute( cCreateVocablesTable);
			statement.close();

			statement = conn.createStatement();
			String cSentenceTable = "";
			cSentenceTable += "CREATE TABLE sentences (";
			cSentenceTable += "SentenceId INTEGER PRIMARY KEY, ";
			cSentenceTable += "Sentence TEXT, ";
			cSentenceTable += "VocableId INTEGER ";
			cSentenceTable += ")";
			statement.execute( cSentenceTable);
			statement.close();

			statement = conn.createStatement();
			String cCreateAddInfoTable = "";
			cCreateAddInfoTable += "CREATE TABLE additionalinfo (";
			cCreateAddInfoTable += "AdditionalInfoId INTEGER PRIMARY KEY, ";
			cCreateAddInfoTable += "AdditionalInfo TEXT, ";
			cCreateAddInfoTable += "VocableId INTEGER ";
			cCreateAddInfoTable += ")";
			statement.execute( cCreateAddInfoTable);
			statement.close();

			statement = conn.createStatement();
			String cCreateWordsTable = "";
			cCreateWordsTable += "CREATE TABLE words (";
			cCreateWordsTable += "WordId INTEGER PRIMARY KEY, ";
			cCreateWordsTable += "Word TEXT, ";
			cCreateWordsTable += "VocableId INTEGER, ";
			cCreateWordsTable += "Language INTEGER CHECK( Language IN(1,2)) ";
			cCreateWordsTable += ")";
			statement.execute( cCreateWordsTable);
			statement.close();

			statement = conn.createStatement();
			String cCreateVocableProtocolsTable = "";
			cCreateVocableProtocolsTable += "CREATE TABLE vocableprotocols (";
			cCreateVocableProtocolsTable += "VocableProtocolId INTEGER PRIMARY KEY, ";
			cCreateVocableProtocolsTable += "VocableId INTEGER, ";
			cCreateVocableProtocolsTable += "CreateTimestamp INTEGER, ";
			cCreateVocableProtocolsTable += "VocableProtocolType INTEGER ";
			cCreateVocableProtocolsTable += ")";
			statement.execute( cCreateVocableProtocolsTable);
			statement.close();

			statement = conn.createStatement();
			String cVocableTestProtocols = "";
			cVocableTestProtocols += "CREATE TABLE vocabletestprotocols (";
			cVocableTestProtocols += "VocableProtocolId INTEGER PRIMARY KEY, ";
			cVocableTestProtocols += "AnsweredCorrectly BOOL";
			cVocableTestProtocols += ")";
			statement.execute( cVocableTestProtocols);
			statement.close();

			statement = conn.createStatement();
			String cTestProtocols = "";
			cTestProtocols += "CREATE TABLE testprotocols (";
			cTestProtocols += "TestProtocolId INTEGER PRIMARY KEY, ";
			cTestProtocols += "CreateTimestamp INTEGER, ";
			cTestProtocols += "NumberWords INTEGER, ";
			cTestProtocols += "NumberCorrectAnswers INTEGER ";
			cTestProtocols += ")";
			statement.execute( cTestProtocols);
			statement.close();

			statement = conn.createStatement();
			String cTestTreeNodeTable = "";
			cTestTreeNodeTable += "CREATE TABLE treenodetestprotocols (";
			cTestTreeNodeTable += "TestProtocolId INTEGER,";
			cTestTreeNodeTable += "TreeNodeId INTEGER";
			cTestTreeNodeTable += ")";
			statement.execute( cTestTreeNodeTable);
			statement.close();
		}
		finally {
			if(statement != null) {
				statement.close();
			}
		}
	}

}
