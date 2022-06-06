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
 * Projector for Chassis as outlined for the CQRS pattern.
 * 
 * Commands are handled by ChassisAggregate
 * 
 * @author Dev Team
 *
 */
@Component("chassis-entity-projector")
public class ChassisEntityProjector {
		
	// core constructor
	public ChassisEntityProjector(ChassisRepository repository ) {
        this.repository = repository;
    }	

	/*
	 * Insert a Chassis
	 * 
     * @param	entity Chassis
     */
    public Chassis create( Chassis entity) {
	    LOGGER.info("creating " + entity.toString() );
	   
    	// ------------------------------------------
    	// persist a new one
    	// ------------------------------------------ 
	    return repository.save(entity);
        
    }

	/*
	 * Update a Chassis
	 * 
     * @param	entity Chassis
     */
    public Chassis update( Chassis entity) {
	    LOGGER.info("updating " + entity.toString() );

	    // ------------------------------------------
    	// save with an existing instance
    	// ------------------------------------------ 
		return repository.save(entity);

    }
    
	/*
	 * Delete a Chassis
	 * 
     * @param	id		UUID
     */
    public Chassis delete( UUID id ) {
	    LOGGER.info("deleting " + id.toString() );

    	// ------------------------------------------
    	// locate the entity by the provided id
    	// ------------------------------------------
	    Chassis entity = repository.findById(id).get();
	    
    	// ------------------------------------------
    	// delete what is discovered 
    	// ------------------------------------------
    	repository.delete( entity );
    	
    	return entity;
    }    




    /**
     * Method to retrieve the Chassis via an FindChassisQuery
     * @return 	query	FindChassisQuery
     */
    @SuppressWarnings("unused")
    public Chassis find( UUID id ) {
    	LOGGER.info("handling find using " + id.toString() );
    	try {
    		return repository.findById(id).get();
    	}
    	catch( IllegalArgumentException exc ) {
    		LOGGER.log( Level.WARNING, "Failed to find a Chassis - {0}", exc.getMessage() );
    	}
    	return null;
    }
    
    /**
     * Method to retrieve a collection of all Chassiss
     *
     * @param	query	FindAllChassisQuery 
     * @return 	List<Chassis> 
     */
    @SuppressWarnings("unused")
    public List<Chassis> findAll( FindAllChassisQuery query ) {
    	LOGGER.info("handling findAll using " + query.toString() );
    	try {
    		return repository.findAll();
    	}
    	catch( IllegalArgumentException exc ) {
    		LOGGER.log( Level.WARNING, "Failed to find all Chassis - {0}", exc.getMessage() );
    	}
    	return null;
    }

    //--------------------------------------------------
    // attributes
    // --------------------------------------------------
	@Autowired
    protected final ChassisRepository repository;

    private static final Logger LOGGER 	= Logger.getLogger(ChassisEntityProjector.class.getName());

}
