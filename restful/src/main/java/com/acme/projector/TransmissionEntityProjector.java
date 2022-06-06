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
 * Projector for Transmission as outlined for the CQRS pattern.
 * 
 * Commands are handled by TransmissionAggregate
 * 
 * @author Dev Team
 *
 */
@Component("transmission-entity-projector")
public class TransmissionEntityProjector {
		
	// core constructor
	public TransmissionEntityProjector(TransmissionRepository repository ) {
        this.repository = repository;
    }	

	/*
	 * Insert a Transmission
	 * 
     * @param	entity Transmission
     */
    public Transmission create( Transmission entity) {
	    LOGGER.info("creating " + entity.toString() );
	   
    	// ------------------------------------------
    	// persist a new one
    	// ------------------------------------------ 
	    return repository.save(entity);
        
    }

	/*
	 * Update a Transmission
	 * 
     * @param	entity Transmission
     */
    public Transmission update( Transmission entity) {
	    LOGGER.info("updating " + entity.toString() );

	    // ------------------------------------------
    	// save with an existing instance
    	// ------------------------------------------ 
		return repository.save(entity);

    }
    
	/*
	 * Delete a Transmission
	 * 
     * @param	id		UUID
     */
    public Transmission delete( UUID id ) {
	    LOGGER.info("deleting " + id.toString() );

    	// ------------------------------------------
    	// locate the entity by the provided id
    	// ------------------------------------------
	    Transmission entity = repository.findById(id).get();
	    
    	// ------------------------------------------
    	// delete what is discovered 
    	// ------------------------------------------
    	repository.delete( entity );
    	
    	return entity;
    }    




    /**
     * Method to retrieve the Transmission via an FindTransmissionQuery
     * @return 	query	FindTransmissionQuery
     */
    @SuppressWarnings("unused")
    public Transmission find( UUID id ) {
    	LOGGER.info("handling find using " + id.toString() );
    	try {
    		return repository.findById(id).get();
    	}
    	catch( IllegalArgumentException exc ) {
    		LOGGER.log( Level.WARNING, "Failed to find a Transmission - {0}", exc.getMessage() );
    	}
    	return null;
    }
    
    /**
     * Method to retrieve a collection of all Transmissions
     *
     * @param	query	FindAllTransmissionQuery 
     * @return 	List<Transmission> 
     */
    @SuppressWarnings("unused")
    public List<Transmission> findAll( FindAllTransmissionQuery query ) {
    	LOGGER.info("handling findAll using " + query.toString() );
    	try {
    		return repository.findAll();
    	}
    	catch( IllegalArgumentException exc ) {
    		LOGGER.log( Level.WARNING, "Failed to find all Transmission - {0}", exc.getMessage() );
    	}
    	return null;
    }

    //--------------------------------------------------
    // attributes
    // --------------------------------------------------
	@Autowired
    protected final TransmissionRepository repository;

    private static final Logger LOGGER 	= Logger.getLogger(TransmissionEntityProjector.class.getName());

}
