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
package net.noctuasource.profiles.impl;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.noctuasource.profiles.ProfileXmlException;
import net.noctuasource.profiles.ProfileXmlFile;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import org.w3c.dom.Element;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class ProfileXmlFileImpl implements ProfileXmlFile {

	public static final Integer	VERSION		=	1;
	
	private static final String DEFAULT_PROFILE_DIR_ATTRIBUTE = "defaultDir";
	private static final String VERSION_ATTRIBUTE = "version";

	private static final String PATH_TAG = "path";
	private static final String ROOT_TAG = "profiles";
	
	
	
	private static Logger logger = Logger.getLogger(ProfileXmlFileImpl.class);
		
	private File			file;
	private Document		doc;
	
	
	public ProfileXmlFileImpl(String filename) throws ProfileXmlException {
		try {
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			file = new File(filename);
	
			if( file.exists()) {
								
				doc = dBuilder.parse(file);
				doc.getDocumentElement().normalize();
				
				if( !doc.getDocumentElement().getNodeName().equals(ROOT_TAG)) {
					
					throw new ProfileXmlException("Illegal profiles xml!");
				}	
				
				if( !doc.getDocumentElement().getAttribute(VERSION_ATTRIBUTE)
													.equals(VERSION.toString())) {
					throw new ProfileXmlException("Wrong version of profiles xml!");
				}
				
			} else {
				doc = dBuilder.newDocument();
				Element root = doc.createElement(ROOT_TAG);
				root.setAttribute(VERSION_ATTRIBUTE, VERSION.toString());
				root.setAttribute(DEFAULT_PROFILE_DIR_ATTRIBUTE, ".");
				doc.appendChild(root);
				
				writeFile();
			}

		} catch (Exception e) {
			logger.error("Exception: " + e.getMessage());
			throw new ProfileXmlException(e.getMessage());
		}	
	}
	
	
	
	
	
	
	@Override
	public String getDefaultProfilePath() {
		
		return doc.getDocumentElement().getAttribute(DEFAULT_PROFILE_DIR_ATTRIBUTE);
	}

	
	@Override
	public void setDefaultProfilePath(String path) throws ProfileXmlException {
		
		doc.getDocumentElement().setAttribute(DEFAULT_PROFILE_DIR_ATTRIBUTE, path);
		
		writeFile();
	}

	
	@Override
	public Iterable<String> getProfilePaths() {
		
		List<String> profilePaths = new LinkedList<String>();
		
		NodeList nodeList = doc.getElementsByTagName(PATH_TAG);
		 
		for (int temp = 0; temp < nodeList.getLength(); ++temp) {

		   Node nNode = nodeList.item(temp);
		   
		   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			   
			   String currentValue = nNode.getTextContent();
			   currentValue = currentValue.trim();
			   
			   if (!currentValue.isEmpty()) {
				   profilePaths.add(currentValue);
			   }
		   }
		}
		
		return profilePaths;
	}

	
	@Override
	public boolean addProfilePath(String path) throws ProfileXmlException {
		logger.debug("Add profile path: " + path);
		
		Iterable<String> profilePaths = getProfilePaths();
		
		for (String item: profilePaths) {
			if (item.equals(path.trim())) {
				return false;
			}
		}
		
		Node root = doc.getDocumentElement();
 
		Element newElement = doc.createElement(PATH_TAG);
		newElement.setTextContent(path.trim());
		
		root.appendChild(newElement);
		
		writeFile();
		
		return true;
	}

	
	@Override
	public boolean removeProfilePath(String path) throws ProfileXmlException {
		logger.debug("Remove profile path: " + path);
		
		NodeList nodeList = doc.getElementsByTagName(PATH_TAG);

		for(int temp = 0; temp < nodeList.getLength(); ++temp) {

		   Node nNode = nodeList.item(temp);
		   
		   if( (nNode.getNodeType() == Node.ELEMENT_NODE)
				   && (nNode.getTextContent().trim().equals(path))) {
			   
			   nNode.getParentNode().removeChild(nNode);
			   
			   writeFile();
			   
			   return true;
		   }
		}
		
		logger.warn("Could not find profile path in profiles file: " + path);
		
		return false;
	}

	
	public void writeFile() throws ProfileXmlException
	{
		try {
			Source source = new DOMSource(doc);
			
			file.getParentFile().mkdirs();
			
	        Result result = new StreamResult(file);
	        
	        TransformerFactory tf = TransformerFactory.newInstance();
	        tf.setAttribute("indent-number", new Integer(4));
	        
	        Transformer xformer = tf.newTransformer();
	        xformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        xformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
	        xformer.transform(source, result);
	    } catch (TransformerException e) {
	    	logger.warn("Could not write to profiles file!");
	    	throw new ProfileXmlException(e.getMessage());
	    }
	}
}
