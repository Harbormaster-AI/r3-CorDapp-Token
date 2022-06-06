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
 * Projector for Body as outlined for the CQRS pattern.  All event handling and query handling related to Body are invoked here
 * and dispersed as an event to be handled elsewhere.
 * 
 * Commands are handled by BodyAggregate
 * 
 * @author Dev Team
 *
 */
@ProcessingGroup("body")
@Component("body-projector")
public class BodyProjector extends BodyEntityProjector {
		
	// core constructor
	public BodyProjector(BodyRepository repository, QueryUpdateEmitter queryUpdateEmitter ) {
        super(repository);
        this.queryUpdateEmitter = queryUpdateEmitter;
    }	

	/*
     * @param	event CreateBodyEvent
     */
    @EventHandler( payloadType=CreateBodyEvent.class )
    public void handle( CreateBodyEvent event) {
	    LOGGER.info("handling CreateBodyEvent - " + event );
	    Body entity = new Body();
        entity.setBodyId( event.getBodyId() );
        entity.setName( event.getName() );
        entity.setPlant( event.getPlant() );
	    
    	// ------------------------------------------
    	// persist a new one
    	// ------------------------------------------ 
	    create(entity);
        
        // ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllBody( entity );
    }

    /*
     * @param	event RefreshedBodyEvent
     */
    @EventHandler( payloadType=RefreshedBodyEvent.class )
    public void handle( RefreshedBodyEvent event) {
    	LOGGER.info("handling RefreshedBodyEvent - " + event );
    	
	    Body entity = new Body();
        entity.setBodyId( event.getBodyId() );
        entity.setName( event.getName() );
        entity.setPlant( event.getPlant() );
 
    	// ------------------------------------------
    	// save with an existing instance
    	// ------------------------------------------ 
		update(entity);

    	// ------------------------------------------
    	// emit to subscribers that find one
    	// ------------------------------------------    	
        emitFindBody( entity );

    	// ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllBody( entity );        
    }
    
    /*
     * @param	event ClosedBodyEvent
     */
    @EventHandler( payloadType=ClosedBodyEvent.class )
    public void handle( ClosedBodyEvent event) {
    	LOGGER.info("handling ClosedBodyEvent - " + event );
    	
    	// ------------------------------------------
    	// delete delegation
    	// ------------------------------------------
    	Body entity = delete( event.getBodyId() );

    	// ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllBody( entity );

    }    




    /**
     * Method to retrieve the Body via an BodyPrimaryKey.
     * @param 	id Long
     * @return 	Body
     * @exception ProcessingException - Thrown if processing any related problems
     * @exception IllegalArgumentException 
     */
    @SuppressWarnings("unused")
    @QueryHandler
    public Body handle( FindBodyQuery query ) 
    throws ProcessingException, IllegalArgumentException {
    	return find( query.getFilter().getBodyId() );
    }
    
    /**
     * Method to retrieve a collection of all Bodys
     *
     * @param	query	FindAllBodyQuery 
     * @return 	List<Body> 
     * @exception ProcessingException Thrown if any problems
     */
    @SuppressWarnings("unused")
    @QueryHandler
    public List<Body> handle( FindAllBodyQuery query ) throws ProcessingException {
    	return findAll( query );
    }


	/**
	 * emit to subscription queries of type FindBody, 
	 * but only if the id matches
	 * 
	 * @param		entity	Body
	 */
	protected void emitFindBody( Body entity ) {
		LOGGER.info("handling emitFindBody" );
		
	    queryUpdateEmitter.emit(FindBodyQuery.class,
	                            query -> query.getFilter().getBodyId().equals(entity.getBodyId()),
	                            entity);
	}
	
	/**
	 * unconditionally emit to subscription queries of type FindAllBody
	 * 
	 * @param		entity	Body
	 */
	protected void emitFindAllBody( Body entity ) {
		LOGGER.info("handling emitFindAllBody" );
		
	    queryUpdateEmitter.emit(FindAllBodyQuery.class,
	                            query -> true,
	                            entity);
	}


	//--------------------------------------------------
    // attributes
    // --------------------------------------------------
	@Autowired
	private final QueryUpdateEmitter queryUpdateEmitter;
    private static final Logger LOGGER 	= Logger.getLogger(BodyProjector.class.getName());

}
