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
package net.noctuasource.noctua.core.ui.profiles;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import net.noctuasource.profiles.Observer;
import net.noctuasource.profiles.Profile;
import net.noctuasource.profiles.ProfileManager;


public class ProfilesListModel extends AbstractListModel
								implements Observer {


	private static final long serialVersionUID = -5717860834307745227L;

	
	
	ProfileManager		profilesManager;
	
	List<Profile>		profiles = new ArrayList<Profile>();
	
	
	
	public ProfilesListModel(ProfileManager profilesManager) {
		this.profilesManager = profilesManager;
		
		profilesManager.registerObserver(this);
		
		updateData();
	}
	
	
	
	@Override
	public Object getElementAt(int n) {
		return profiles.get(n);
	}

	@Override
	public int getSize() {
		return profiles.size();
	}



	@Override
	public void updateData() {
		
		Iterable<Profile> given = profilesManager.getAllProfiles();
		
		profiles.clear();
		
		for( Profile p : given) {
			profiles.add(p);
		}
		
		fireContentsChangedEvent(0, profiles.size());
	}
	
	
	
    private void fireContentsChangedEvent(int index0, int index1) {
        ListDataEvent lde = new ListDataEvent( this,
									            ListDataEvent.CONTENTS_CHANGED,
									            index0,
									            index1 );
        
        ListDataListener[] listeners = (ListDataListener[]) getListeners(ListDataListener.class);
        
        for (ListDataListener listener : listeners) {
            listener.contentsChanged(lde);
        }
    } 

    
    public void removeAssociations() {
    	profilesManager.removeObserver(this);
    }
    
}
