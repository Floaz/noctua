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
package net.noctuasource.noctua.core.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;






@Entity
@Table(name="TreeNodes")
@GenericGenerator(name="TREE_NODE_GEN", strategy="uuid2", parameters={})
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "TreeNodeType", discriminatorType = DiscriminatorType.INTEGER)
public abstract class TreeNode {

    @Id
    @GeneratedValue(generator="TREE_NODE_GEN")
    @Column(name="TreeNodeId")
    private String id;

    @Column(name = "TreeNodeName")
    private String name;

    @ManyToOne
    @JoinColumn(name = "TreeNodeParent", nullable = true)
    private TreeNode parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @BatchSize(size = 5) // Optimizes tree navigation (expanding nodes)
    private List<TreeNode> children = new ArrayList<>();


//    @ManyToMany(mappedBy = "vocabulary", fetch = FetchType.LAZY)
//    private Set<Vocable> items = new HashSet<Vocable>();

//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name = "CreatedTimestamp", nullable = false, updatable = false)
//    private Date created = new Date();






	public TreeNode() {
	}


    // ********************** Accessor Methods ****************************** //


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public TreeNode getParent() {
		return parent;
	}


	public void setParent(TreeNode parent) {
		this.parent = parent;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


    public List<TreeNode> getChildren() {
		return children;
	}


	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}



//	public Date getCreated() {
//		return created;
//	}
//
//
//	public void setCreated(Date created) {
//		this.created = created;
//	}



    // ********************** Common Methods ******************************** //


	public void addChildren(TreeNode child) {
		child.setParent(this);
		children.add(child);
	}


	public void removeChildren(TreeNode child) {
		child.setParent(null);
		children.remove(child);
	}



	@Override
	public String toString() {
        return  getName();
    }

}
