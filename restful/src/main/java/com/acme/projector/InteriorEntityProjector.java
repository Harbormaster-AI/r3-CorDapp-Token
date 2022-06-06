/*******************************************************************************
  Oldsmobile Motor Corporation Confidential
  
  2018 Oldsmobile Motor Corporation
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Oldsmobile Motor Corporation - General Release
 ******************************************************************************/
package com.acme.projector;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


import com.acme.api.*;
import com.acme.entity.*;
import com.acme.exception.*;
import com.acme.repository.*;

/**
 * Projector for Interior as outlined for the CQRS pattern.
 * 
 * Commands are handled by InteriorAggregate
 * 
 * @author Dev Team
 *
 */
@Component("interior-entity-projector")
public class InteriorEntityProjector {
		
	// core constructor
	public InteriorEntityProjector(InteriorRepository repository ) {
        this.repository = repository;
    }	

	/*
	 * Insert a Interior
	 * 
     * @param	entity Interior
     */
    public Interior create( Interior entity) {
	    LOGGER.info("creating " + entity.toString() );
	   
    	// ------------------------------------------
    	// persist a new one
    	// ------------------------------------------ 
	    return repository.save(entity);
        
    }

	/*
	 * Update a Interior
	 * 
     * @param	entity Interior
     */
    public Interior update( Interior entity) {
	    LOGGER.info("updating " + entity.toString() );

	    // ------------------------------------------
    	// save with an existing instance
    	// ------------------------------------------ 
		return repository.save(entity);

    }
    
	/*
	 * Delete a Interior
	 * 
     * @param	id		UUID
     */
    public Interior delete( UUID id ) {
	    LOGGER.info("deleting " + id.toString() );

    	// ------------------------------------------
    	// locate the entity by the provided id
    	// ------------------------------------------
	    Interior entity = repository.findById(id).get();
	    
    	// ------------------------------------------
    	// delete what is discovered 
    	// ------------------------------------------
    	repository.delete( entity );
    	
    	return entity;
    }    




    /**
     * Method to retrieve the Interior via an FindInteriorQuery
     * @return 	query	FindInteriorQuery
     */
    @SuppressWarnings("unused")
    public Interior find( UUID id ) {
    	LOGGER.info("handling find using " + id.toString() );
    	try {
    		return repository.findById(id).get();
    	}
    	catch( IllegalArgumentException exc ) {
    		LOGGER.log( Level.WARNING, "Failed to find a Interior - {0}", exc.getMessage() );
    	}
    	return null;
    }
    
    /**
     * Method to retrieve a collection of all Interiors
     *
     * @param	query	FindAllInteriorQuery 
     * @return 	List<Interior> 
     */
    @SuppressWarnings("unused")
    public List<Interior> findAll( FindAllInteriorQuery query ) {
    	LOGGER.info("handling findAll using " + query.toString() );
    	try {
    		return repository.findAll();
    	}
    	catch( IllegalArgumentException exc ) {
    		LOGGER.log( Level.WARNING, "Failed to find all Interior - {0}", exc.getMessage() );
    	}
    	return null;
    }

    //--------------------------------------------------
    // attributes
    // --------------------------------------------------
	@Autowired
    protected final InteriorRepository repository;

    private static final Logger LOGGER 	= Logger.getLogger(InteriorEntityProjector.class.getName());

}
