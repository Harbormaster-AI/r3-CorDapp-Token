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
 * Projector for Engine as outlined for the CQRS pattern.  All event handling and query handling related to Engine are invoked here
 * and dispersed as an event to be handled elsewhere.
 * 
 * Commands are handled by EngineAggregate
 * 
 * @author Dev Team
 *
 */
@ProcessingGroup("engine")
@Component("engine-projector")
public class EngineProjector extends EngineEntityProjector {
		
	// core constructor
	public EngineProjector(EngineRepository repository, QueryUpdateEmitter queryUpdateEmitter ) {
        super(repository);
        this.queryUpdateEmitter = queryUpdateEmitter;
    }	

	/*
     * @param	event CreateEngineEvent
     */
    @EventHandler( payloadType=CreateEngineEvent.class )
    public void handle( CreateEngineEvent event) {
	    LOGGER.info("handling CreateEngineEvent - " + event );
	    Engine entity = new Engine();
        entity.setEngineId( event.getEngineId() );
        entity.setName( event.getName() );
        entity.setSerialNum( event.getSerialNum() );
        entity.setPlant( event.getPlant() );
        entity.setType( event.getType() );
	    
    	// ------------------------------------------
    	// persist a new one
    	// ------------------------------------------ 
	    create(entity);
        
        // ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllEngine( entity );
    }

    /*
     * @param	event RefreshedEngineEvent
     */
    @EventHandler( payloadType=RefreshedEngineEvent.class )
    public void handle( RefreshedEngineEvent event) {
    	LOGGER.info("handling RefreshedEngineEvent - " + event );
    	
	    Engine entity = new Engine();
        entity.setEngineId( event.getEngineId() );
        entity.setName( event.getName() );
        entity.setSerialNum( event.getSerialNum() );
        entity.setPlant( event.getPlant() );
        entity.setType( event.getType() );
 
    	// ------------------------------------------
    	// save with an existing instance
    	// ------------------------------------------ 
		update(entity);

    	// ------------------------------------------
    	// emit to subscribers that find one
    	// ------------------------------------------    	
        emitFindEngine( entity );

    	// ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllEngine( entity );        
    }
    
    /*
     * @param	event ClosedEngineEvent
     */
    @EventHandler( payloadType=ClosedEngineEvent.class )
    public void handle( ClosedEngineEvent event) {
    	LOGGER.info("handling ClosedEngineEvent - " + event );
    	
    	// ------------------------------------------
    	// delete delegation
    	// ------------------------------------------
    	Engine entity = delete( event.getEngineId() );

    	// ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllEngine( entity );

    }    




    /**
     * Method to retrieve the Engine via an EnginePrimaryKey.
     * @param 	id Long
     * @return 	Engine
     * @exception ProcessingException - Thrown if processing any related problems
     * @exception IllegalArgumentException 
     */
    @SuppressWarnings("unused")
    @QueryHandler
    public Engine handle( FindEngineQuery query ) 
    throws ProcessingException, IllegalArgumentException {
    	return find( query.getFilter().getEngineId() );
    }
    
    /**
     * Method to retrieve a collection of all Engines
     *
     * @param	query	FindAllEngineQuery 
     * @return 	List<Engine> 
     * @exception ProcessingException Thrown if any problems
     */
    @SuppressWarnings("unused")
    @QueryHandler
    public List<Engine> handle( FindAllEngineQuery query ) throws ProcessingException {
    	return findAll( query );
    }


	/**
	 * emit to subscription queries of type FindEngine, 
	 * but only if the id matches
	 * 
	 * @param		entity	Engine
	 */
	protected void emitFindEngine( Engine entity ) {
		LOGGER.info("handling emitFindEngine" );
		
	    queryUpdateEmitter.emit(FindEngineQuery.class,
	                            query -> query.getFilter().getEngineId().equals(entity.getEngineId()),
	                            entity);
	}
	
	/**
	 * unconditionally emit to subscription queries of type FindAllEngine
	 * 
	 * @param		entity	Engine
	 */
	protected void emitFindAllEngine( Engine entity ) {
		LOGGER.info("handling emitFindAllEngine" );
		
	    queryUpdateEmitter.emit(FindAllEngineQuery.class,
	                            query -> true,
	                            entity);
	}


	//--------------------------------------------------
    // attributes
    // --------------------------------------------------
	@Autowired
	private final QueryUpdateEmitter queryUpdateEmitter;
    private static final Logger LOGGER 	= Logger.getLogger(EngineProjector.class.getName());

}
