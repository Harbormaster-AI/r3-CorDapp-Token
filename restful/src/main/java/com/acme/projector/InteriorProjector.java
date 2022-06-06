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
 * Projector for Interior as outlined for the CQRS pattern.  All event handling and query handling related to Interior are invoked here
 * and dispersed as an event to be handled elsewhere.
 * 
 * Commands are handled by InteriorAggregate
 * 
 * @author Dev Team
 *
 */
@ProcessingGroup("interior")
@Component("interior-projector")
public class InteriorProjector extends InteriorEntityProjector {
		
	// core constructor
	public InteriorProjector(InteriorRepository repository, QueryUpdateEmitter queryUpdateEmitter ) {
        super(repository);
        this.queryUpdateEmitter = queryUpdateEmitter;
    }	

	/*
     * @param	event CreateInteriorEvent
     */
    @EventHandler( payloadType=CreateInteriorEvent.class )
    public void handle( CreateInteriorEvent event) {
	    LOGGER.info("handling CreateInteriorEvent - " + event );
	    Interior entity = new Interior();
        entity.setInteriorId( event.getInteriorId() );
        entity.setSerialNum( event.getSerialNum() );
        entity.setName( event.getName() );
        entity.setPlant( event.getPlant() );
	    
    	// ------------------------------------------
    	// persist a new one
    	// ------------------------------------------ 
	    create(entity);
        
        // ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllInterior( entity );
    }

    /*
     * @param	event RefreshedInteriorEvent
     */
    @EventHandler( payloadType=RefreshedInteriorEvent.class )
    public void handle( RefreshedInteriorEvent event) {
    	LOGGER.info("handling RefreshedInteriorEvent - " + event );
    	
	    Interior entity = new Interior();
        entity.setInteriorId( event.getInteriorId() );
        entity.setSerialNum( event.getSerialNum() );
        entity.setName( event.getName() );
        entity.setPlant( event.getPlant() );
 
    	// ------------------------------------------
    	// save with an existing instance
    	// ------------------------------------------ 
		update(entity);

    	// ------------------------------------------
    	// emit to subscribers that find one
    	// ------------------------------------------    	
        emitFindInterior( entity );

    	// ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllInterior( entity );        
    }
    
    /*
     * @param	event ClosedInteriorEvent
     */
    @EventHandler( payloadType=ClosedInteriorEvent.class )
    public void handle( ClosedInteriorEvent event) {
    	LOGGER.info("handling ClosedInteriorEvent - " + event );
    	
    	// ------------------------------------------
    	// delete delegation
    	// ------------------------------------------
    	Interior entity = delete( event.getInteriorId() );

    	// ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllInterior( entity );

    }    




    /**
     * Method to retrieve the Interior via an InteriorPrimaryKey.
     * @param 	id Long
     * @return 	Interior
     * @exception ProcessingException - Thrown if processing any related problems
     * @exception IllegalArgumentException 
     */
    @SuppressWarnings("unused")
    @QueryHandler
    public Interior handle( FindInteriorQuery query ) 
    throws ProcessingException, IllegalArgumentException {
    	return find( query.getFilter().getInteriorId() );
    }
    
    /**
     * Method to retrieve a collection of all Interiors
     *
     * @param	query	FindAllInteriorQuery 
     * @return 	List<Interior> 
     * @exception ProcessingException Thrown if any problems
     */
    @SuppressWarnings("unused")
    @QueryHandler
    public List<Interior> handle( FindAllInteriorQuery query ) throws ProcessingException {
    	return findAll( query );
    }


	/**
	 * emit to subscription queries of type FindInterior, 
	 * but only if the id matches
	 * 
	 * @param		entity	Interior
	 */
	protected void emitFindInterior( Interior entity ) {
		LOGGER.info("handling emitFindInterior" );
		
	    queryUpdateEmitter.emit(FindInteriorQuery.class,
	                            query -> query.getFilter().getInteriorId().equals(entity.getInteriorId()),
	                            entity);
	}
	
	/**
	 * unconditionally emit to subscription queries of type FindAllInterior
	 * 
	 * @param		entity	Interior
	 */
	protected void emitFindAllInterior( Interior entity ) {
		LOGGER.info("handling emitFindAllInterior" );
		
	    queryUpdateEmitter.emit(FindAllInteriorQuery.class,
	                            query -> true,
	                            entity);
	}


	//--------------------------------------------------
    // attributes
    // --------------------------------------------------
	@Autowired
	private final QueryUpdateEmitter queryUpdateEmitter;
    private static final Logger LOGGER 	= Logger.getLogger(InteriorProjector.class.getName());

}
