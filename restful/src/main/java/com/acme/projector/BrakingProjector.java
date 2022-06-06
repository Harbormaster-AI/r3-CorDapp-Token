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

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.acme.api.*;
import com.acme.entity.*;
import com.acme.exception.*;
import com.acme.repository.*;

/**
 * Projector for Braking as outlined for the CQRS pattern.  All event handling and query handling related to Braking are invoked here
 * and dispersed as an event to be handled elsewhere.
 * 
 * Commands are handled by BrakingAggregate
 * 
 * @author Dev Team
 *
 */
@ProcessingGroup("braking")
@Component("braking-projector")
public class BrakingProjector extends BrakingEntityProjector {
		
	// core constructor
	public BrakingProjector(BrakingRepository repository, QueryUpdateEmitter queryUpdateEmitter ) {
        super(repository);
        this.queryUpdateEmitter = queryUpdateEmitter;
    }	

	/*
     * @param	event CreateBrakingEvent
     */
    @EventHandler( payloadType=CreateBrakingEvent.class )
    public void handle( CreateBrakingEvent event) {
	    LOGGER.info("handling CreateBrakingEvent - " + event );
	    Braking entity = new Braking();
        entity.setBrakingId( event.getBrakingId() );
        entity.setSerialNum( event.getSerialNum() );
        entity.setName( event.getName() );
        entity.setPlant( event.getPlant() );
        entity.setType( event.getType() );
	    
    	// ------------------------------------------
    	// persist a new one
    	// ------------------------------------------ 
	    create(entity);
        
        // ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllBraking( entity );
    }

    /*
     * @param	event RefreshedBrakingEvent
     */
    @EventHandler( payloadType=RefreshedBrakingEvent.class )
    public void handle( RefreshedBrakingEvent event) {
    	LOGGER.info("handling RefreshedBrakingEvent - " + event );
    	
	    Braking entity = new Braking();
        entity.setBrakingId( event.getBrakingId() );
        entity.setSerialNum( event.getSerialNum() );
        entity.setName( event.getName() );
        entity.setPlant( event.getPlant() );
        entity.setType( event.getType() );
 
    	// ------------------------------------------
    	// save with an existing instance
    	// ------------------------------------------ 
		update(entity);

    	// ------------------------------------------
    	// emit to subscribers that find one
    	// ------------------------------------------    	
        emitFindBraking( entity );

    	// ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllBraking( entity );        
    }
    
    /*
     * @param	event ClosedBrakingEvent
     */
    @EventHandler( payloadType=ClosedBrakingEvent.class )
    public void handle( ClosedBrakingEvent event) {
    	LOGGER.info("handling ClosedBrakingEvent - " + event );
    	
    	// ------------------------------------------
    	// delete delegation
    	// ------------------------------------------
    	Braking entity = delete( event.getBrakingId() );

    	// ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllBraking( entity );

    }    




    /**
     * Method to retrieve the Braking via an BrakingPrimaryKey.
     * @param 	id Long
     * @return 	Braking
     * @exception ProcessingException - Thrown if processing any related problems
     * @exception IllegalArgumentException 
     */
    @SuppressWarnings("unused")
    @QueryHandler
    public Braking handle( FindBrakingQuery query ) 
    throws ProcessingException, IllegalArgumentException {
    	return find( query.getFilter().getBrakingId() );
    }
    
    /**
     * Method to retrieve a collection of all Brakings
     *
     * @param	query	FindAllBrakingQuery 
     * @return 	List<Braking> 
     * @exception ProcessingException Thrown if any problems
     */
    @SuppressWarnings("unused")
    @QueryHandler
    public List<Braking> handle( FindAllBrakingQuery query ) throws ProcessingException {
    	return findAll( query );
    }


	/**
	 * emit to subscription queries of type FindBraking, 
	 * but only if the id matches
	 * 
	 * @param		entity	Braking
	 */
	protected void emitFindBraking( Braking entity ) {
		LOGGER.info("handling emitFindBraking" );
		
	    queryUpdateEmitter.emit(FindBrakingQuery.class,
	                            query -> query.getFilter().getBrakingId().equals(entity.getBrakingId()),
	                            entity);
	}
	
	/**
	 * unconditionally emit to subscription queries of type FindAllBraking
	 * 
	 * @param		entity	Braking
	 */
	protected void emitFindAllBraking( Braking entity ) {
		LOGGER.info("handling emitFindAllBraking" );
		
	    queryUpdateEmitter.emit(FindAllBrakingQuery.class,
	                            query -> true,
	                            entity);
	}


	//--------------------------------------------------
    // attributes
    // --------------------------------------------------
	@Autowired
	private final QueryUpdateEmitter queryUpdateEmitter;
    private static final Logger LOGGER 	= Logger.getLogger(BrakingProjector.class.getName());

}
