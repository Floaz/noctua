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
package net.noctuasource.profiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

public class ProfileDataFile {

	static final String META_KEY = "profile.meta.";
	static final String NAME_KEY = "profile.name";
	static final String VERSION_KEY = "profile.version";
	
	
	public static ProfileData parse(File file, String dir) throws FileNotFoundException, IOException {
	      Properties p = new Properties();
	      p.load(new FileInputStream(file));
	      
	      ProfileData data = new ProfileData();
    	  
	      data.setName( p.getProperty(NAME_KEY));
	      data.setVersion(
    			  		Integer.parseInt( p.getProperty(VERSION_KEY)));
    	  	      
	      Enumeration<Object> keys = p.keys();
	      
	      while (keys.hasMoreElements()){
	    	  String key = (String)keys.nextElement();

	    	  if (key.startsWith(META_KEY)) {
	    		  String cutKey = key.substring(META_KEY.length());
	    		  String value = p.getProperty(key);
	    		  
	    		  data.setData( cutKey, value);
	    	  }
	      }
	      
	      return data;
	}
	
	
	public static void write(File file, ProfileData data) throws FileNotFoundException, IOException {
	      Properties p = new Properties();
	      //p.load(new FileInputStream(file));
	      
	      p.setProperty(NAME_KEY, data.getName().trim());
	      p.setProperty(VERSION_KEY, new Integer(data.getVersion()).toString());

	      Iterable<String> iterable = data.getAllDataKeys();
	      
	      for( String key : iterable) {
	    	  p.setProperty(META_KEY + key, data.getData(key));
	      }
	      
	      p.store(new FileOutputStream(file), "");
	}
	
	
}
