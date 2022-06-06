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
 * Projector for Chassis as outlined for the CQRS pattern.  All event handling and query handling related to Chassis are invoked here
 * and dispersed as an event to be handled elsewhere.
 * 
 * Commands are handled by ChassisAggregate
 * 
 * @author Dev Team
 *
 */
@ProcessingGroup("chassis")
@Component("chassis-projector")
public class ChassisProjector extends ChassisEntityProjector {
		
	// core constructor
	public ChassisProjector(ChassisRepository repository, QueryUpdateEmitter queryUpdateEmitter ) {
        super(repository);
        this.queryUpdateEmitter = queryUpdateEmitter;
    }	

	/*
     * @param	event CreateChassisEvent
     */
    @EventHandler( payloadType=CreateChassisEvent.class )
    public void handle( CreateChassisEvent event) {
	    LOGGER.info("handling CreateChassisEvent - " + event );
	    Chassis entity = new Chassis();
        entity.setChassisId( event.getChassisId() );
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
        emitFindAllChassis( entity );
    }

    /*
     * @param	event RefreshedChassisEvent
     */
    @EventHandler( payloadType=RefreshedChassisEvent.class )
    public void handle( RefreshedChassisEvent event) {
    	LOGGER.info("handling RefreshedChassisEvent - " + event );
    	
	    Chassis entity = new Chassis();
        entity.setChassisId( event.getChassisId() );
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
        emitFindChassis( entity );

    	// ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllChassis( entity );        
    }
    
    /*
     * @param	event ClosedChassisEvent
     */
    @EventHandler( payloadType=ClosedChassisEvent.class )
    public void handle( ClosedChassisEvent event) {
    	LOGGER.info("handling ClosedChassisEvent - " + event );
    	
    	// ------------------------------------------
    	// delete delegation
    	// ------------------------------------------
    	Chassis entity = delete( event.getChassisId() );

    	// ------------------------------------------
    	// emit to subscribers that find all
    	// ------------------------------------------    	
        emitFindAllChassis( entity );

    }    




    /**
     * Method to retrieve the Chassis via an ChassisPrimaryKey.
     * @param 	id Long
     * @return 	Chassis
     * @exception ProcessingException - Thrown if processing any related problems
     * @exception IllegalArgumentException 
     */
    @SuppressWarnings("unused")
    @QueryHandler
    public Chassis handle( FindChassisQuery query ) 
    throws ProcessingException, IllegalArgumentException {
    	return find( query.getFilter().getChassisId() );
    }
    
    /**
     * Method to retrieve a collection of all Chassiss
     *
     * @param	query	FindAllChassisQuery 
     * @return 	List<Chassis> 
     * @exception ProcessingException Thrown if any problems
     */
    @SuppressWarnings("unused")
    @QueryHandler
    public List<Chassis> handle( FindAllChassisQuery query ) throws ProcessingException {
    	return findAll( query );
    }


	/**
	 * emit to subscription queries of type FindChassis, 
	 * but only if the id matches
	 * 
	 * @param		entity	Chassis
	 */
	protected void emitFindChassis( Chassis entity ) {
		LOGGER.info("handling emitFindChassis" );
		
	    queryUpdateEmitter.emit(FindChassisQuery.class,
	                            query -> query.getFilter().getChassisId().equals(entity.getChassisId()),
	                            entity);
	}
	
	/**
	 * unconditionally emit to subscription queries of type FindAllChassis
	 * 
	 * @param		entity	Chassis
	 */
	protected void emitFindAllChassis( Chassis entity ) {
		LOGGER.info("handling emitFindAllChassis" );
		
	    queryUpdateEmitter.emit(FindAllChassisQuery.class,
	                            query -> true,
	                            entity);
	}

    /**
     * query method to findByNameLike
     * @param 		String name
     * @return		Chassis
     */     
	@SuppressWarnings("unused")
	@QueryHandler
	public Chassis findByNameLike( FindByNameLikeQuery query ) {
		LOGGER.info("handling findByNameLike" );
		Chassis result = null;
		
		try {
		    result = repository.findByNameLike( query.getFilter().getName() );

        }
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING, "Failed to findByNameLike using " + query.getFilter(), exc );
        }
        
        return result;
	}
    /**
     * query method to findBySerialNum
     * @param 		String serialNum
     * @return		Chassis
     */     
	@SuppressWarnings("unused")
	@QueryHandler
	public Chassis findBySerialNum( FindBySerialNumQuery query ) {
		LOGGER.info("handling findBySerialNum" );
		Chassis result = null;
		
		try {
		    result = repository.findBySerialNum( query.getFilter().getSerialNum() );

        }
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING, "Failed to findBySerialNum using " + query.getFilter(), exc );
        }
        
        return result;
	}
    /**
     * query method to findByType
     * @param 		ChassisType type
     * @return		List<Chassis>
     */     
	@SuppressWarnings("unused")
	@QueryHandler
	public List<Chassis> findByType( FindByTypeQuery query ) {
		LOGGER.info("handling findByType" );
		List<Chassis> result = null;
		
		try {
            Pageable pageable = PageRequest.of(query.getOffset(), query.getLimit());
            result = repository.findByType( query.getFilter().getType(), pageable );

        }
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING, "Failed to findByType using " + query.getFilter(), exc );
        }
        
        return result;
	}

	//--------------------------------------------------
    // attributes
    // --------------------------------------------------
	@Autowired
	private final QueryUpdateEmitter queryUpdateEmitter;
    private static final Logger LOGGER 	= Logger.getLogger(ChassisProjector.class.getName());

}
