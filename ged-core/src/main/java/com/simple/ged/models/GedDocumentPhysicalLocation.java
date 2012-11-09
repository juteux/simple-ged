package com.simple.ged.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Some document physical location
 * 
 * @author xavier
 *
 */
@Entity
@Table(name="document_location")
public class GedDocumentPhysicalLocation {

	/**
	 * The ID
	 */
    @Id
    @GeneratedValue
    @Column(name="rowid")
	private Integer id;
	
    
	/**
	 * The location
	 */
    @Column(name="label")
	private String label;

    
    public GedDocumentPhysicalLocation() {
    	id = null;
    }
    

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}

}
