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
 * Projector for Transmission as outlined for the CQRS pattern.  All event handling and query handling related to Transmission are invoked here
 * and dispersed as an event to be handled elsewhere.
 * 
 * Commands are handled by TransmissionAggregate
 * 
 * @author Dev Team
 *
 */
@ProcessingGroup("transmission")
@Component("transmission-projector")
public class TransmissionProjector extends TransmissionEntityProjector {
		
	// core constructor
	public TransmissionProjector(TransmissionRepository repository, QueryUpdateEmitter queryUpdateEmitter ) {
        super(repository);
        this.queryUpdateEmitter = queryUpdateEmitter;
    }	

	/*
     * @param	event CreateTransmissionEvent
     */
    @EventHandler( payloadType=CreateTransmissionEvent.class )
    public void handle( CreateTransmissionEvent event) {
	    LOGGER.info("handling CreateTransmissionEvent - " + event );
	    Transmission entity = new Transmission();
        entity.setTransmissionId( event.getTransmissionId() );
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
        emitFindAllTransmission( entity );
    }

    /*
     * @param	event RefreshedTransmissionEvent
     */
    @EventHandler( payloadType=RefreshedTransmissionEvent.class )
    public void handle( RefreshedTransmissionEvent event) {
    	LOGGER.info("handling RefreshedTransmissionEvent - " + event );
    	
	    Transmission entity = new Transmission();
        entity.setTransmissionId( event.getTransmissionId() );
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
        emitFindTransmission( entity );

    	// ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllTransmission( entity );        
    }
    
    /*
     * @param	event ClosedTransmissionEvent
     */
    @EventHandler( payloadType=ClosedTransmissionEvent.class )
    public void handle( ClosedTransmissionEvent event) {
    	LOGGER.info("handling ClosedTransmissionEvent - " + event );
    	
    	// ------------------------------------------
    	// delete delegation
    	// ------------------------------------------
    	Transmission entity = delete( event.getTransmissionId() );

    	// ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllTransmission( entity );

    }    




    /**
     * Method to retrieve the Transmission via an TransmissionPrimaryKey.
     * @param 	id Long
     * @return 	Transmission
     * @exception ProcessingException - Thrown if processing any related problems
     * @exception IllegalArgumentException 
     */
    @SuppressWarnings("unused")
    @QueryHandler
    public Transmission handle( FindTransmissionQuery query ) 
    throws ProcessingException, IllegalArgumentException {
    	return find( query.getFilter().getTransmissionId() );
    }
    
    /**
     * Method to retrieve a collection of all Transmissions
     *
     * @param	query	FindAllTransmissionQuery 
     * @return 	List<Transmission> 
     * @exception ProcessingException Thrown if any problems
     */
    @SuppressWarnings("unused")
    @QueryHandler
    public List<Transmission> handle( FindAllTransmissionQuery query ) throws ProcessingException {
    	return findAll( query );
    }


	/**
	 * emit to subscription queries of type FindTransmission, 
	 * but only if the id matches
	 * 
	 * @param		entity	Transmission
	 */
	protected void emitFindTransmission( Transmission entity ) {
		LOGGER.info("handling emitFindTransmission" );
		
	    queryUpdateEmitter.emit(FindTransmissionQuery.class,
	                            query -> query.getFilter().getTransmissionId().equals(entity.getTransmissionId()),
	                            entity);
	}
	
	/**
	 * unconditionally emit to subscription queries of type FindAllTransmission
	 * 
	 * @param		entity	Transmission
	 */
	protected void emitFindAllTransmission( Transmission entity ) {
		LOGGER.info("handling emitFindAllTransmission" );
		
	    queryUpdateEmitter.emit(FindAllTransmissionQuery.class,
	                            query -> true,
	                            entity);
	}


	//--------------------------------------------------
    // attributes
    // --------------------------------------------------
	@Autowired
	private final QueryUpdateEmitter queryUpdateEmitter;
    private static final Logger LOGGER 	= Logger.getLogger(TransmissionProjector.class.getName());

}
