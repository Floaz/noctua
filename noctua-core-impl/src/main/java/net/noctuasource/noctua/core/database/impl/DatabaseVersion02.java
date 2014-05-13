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
			cCreateTable += "CREATE TABLE tree_nodes (";
			cCreateTable += "tree_node_id VARCHAR(64) PRIMARY KEY, ";
			cCreateTable += "tree_node_name TEXT, ";
			cCreateTable += "tree_node_parent INTEGER, ";
			cCreateTable += "tree_node_type INTEGER";
			cCreateTable += ")";
			statement.execute(cCreateTable);
			statement.close();

			statement = conn.createStatement();
			cCreateTable = "";
			cCreateTable += "CREATE TABLE languages ( ";
			cCreateTable += "tree_node_id VARCHAR(64) PRIMARY KEY, ";
			cCreateTable += "language_code VARCHAR(5) )";
			statement.execute( cCreateTable);
			statement.close();

			statement = conn.createStatement();
			cCreateTable = "";
			cCreateTable += "CREATE TABLE folders (";
			cCreateTable += "tree_node_id VARCHAR(64) PRIMARY KEY, ";
			cCreateTable += "folder_expanded BOOLEAN ";
			cCreateTable += ")";
			statement.execute( cCreateTable);
			statement.close();

			statement = conn.createStatement();
			cCreateTable = "";
			cCreateTable += "CREATE TABLE flash_card_groups (";
			cCreateTable += " tree_node_id VARCHAR(64) PRIMARY KEY, ";
			cCreateTable += " max_levels INTEGER ";
			cCreateTable += ")";
			statement.execute( cCreateTable);
			statement.close();

			statement = conn.createStatement();
			cCreateTable = "";
			cCreateTable += "CREATE TABLE vocabulary (";
			cCreateTable += " vocable_id VARCHAR(64) PRIMARY KEY, ";
			cCreateTable += " tree_node_id INTEGER, ";
			cCreateTable += " level INTEGER, ";
			cCreateTable += " vocable VARCHAR(250), ";
			cCreateTable += " native1 VARCHAR(250), ";
			cCreateTable += " native2 VARCHAR(250), ";
			cCreateTable += " native3 VARCHAR(250), ";
			cCreateTable += " example TEXT, ";
			cCreateTable += " example_translation TEXT, ";
			cCreateTable += " tip TEXT, ";
			cCreateTable += " info TEXT, ";
			cCreateTable += " gender INTEGER, ";
			cCreateTable += " word_type INTEGER ";
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
