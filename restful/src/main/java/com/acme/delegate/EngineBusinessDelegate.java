/*******************************************************************************
  Oldsmobile Motor Corporation Confidential
  
  2018 Oldsmobile Motor Corporation
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Oldsmobile Motor Corporation - General Release
 ******************************************************************************/
package com.acme.delegate;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;
import java.util.concurrent.*;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryUpdateEmitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.acme.api.*;
import com.acme.entity.*;
import com.acme.exception.*;
import com.acme.validator.*;

/**
 * Engine business delegate class.
 * <p>
 * This class implements the Business Delegate design pattern for the purpose of:
 * <ol>
 * <li>Reducing coupling between the business tier and a client of the business tier by hiding all business-tier implementation details</li>
 * <li>Improving the available of Engine related services in the case of a Engine business related service failing.</li>
 * <li>Exposes a simpler, uniform Engine interface to the business tier, making it easy for clients to consume a simple Java object.</li>
 * <li>Hides the communication protocol that may be required to fulfill Engine business related services.</li>
 * </ol>
 * <p>
 * @author Dev Team
 */
public class EngineBusinessDelegate 
extends BaseBusinessDelegate {
//************************************************************************
// Public Methods
//************************************************************************
    /** 
     * Default Constructor 
     */
    public EngineBusinessDelegate()  {
    	queryGateway 		= applicationContext.getBean(QueryGateway.class);
    	commandGateway 		= applicationContext.getBean(CommandGateway.class);
    	queryUpdateEmitter  = applicationContext.getBean(QueryUpdateEmitter.class);
	}


   /**
	* Engine Business Delegate Factory Method
	*
	* All methods are expected to be self-sufficient.
	*
	* @return 	EngineBusinessDelegate
	*/
	public static EngineBusinessDelegate getEngineInstance() {
		return( new EngineBusinessDelegate() );
	}
 
   /**
    * Creates the provided command.
    * 
    * @param		command ${class.getCreateCommandAlias()}
    * @exception    ProcessingException
    * @exception	IllegalArgumentException
    * @return		CompletableFuture<UUID>
    */
	public CompletableFuture<UUID> createEngine( CreateEngineCommand command )
    throws ProcessingException, IllegalArgumentException {

		CompletableFuture<UUID> completableFuture = null;
				
		try {
			// --------------------------------------
        	// assign identity now if none
        	// -------------------------------------- 
			if ( command.getEngineId() == null )
				command.setEngineId( UUID.randomUUID() );
				
			// --------------------------------------
        	// validate the command
        	// --------------------------------------    	
        	EngineValidator.getInstance().validate( command );    

    		// ---------------------------------------
    		// issue the CreateEngineCommand - by convention the future return value for a create command
        	// that is handled by the constructor of an aggregate will return the UUID 
    		// ---------------------------------------
        	completableFuture = commandGateway.send( command );
        	
			LOGGER.log( Level.INFO, "return from Command Gateway for CreateEngineCommand of Engine is " + command );
			
        }
        catch (Exception exc) {
            final String errMsg = "Unable to create Engine - " + exc;
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        finally {
        }        
        
        return completableFuture;
    }

   /**
    * Update the provided command.
    * @param		command RefreshEngineCommand
    * @return		CompletableFuture<Void>
    * @exception    ProcessingException
    * @exception  	IllegalArgumentException
    */
    public CompletableFuture<Void> updateEngine( RefreshEngineCommand command ) 
    throws ProcessingException, IllegalArgumentException {
    	CompletableFuture<Void> completableFuture = null;
    	
    	try {       

			// --------------------------------------
        	// validate 
        	// --------------------------------------    	
        	EngineValidator.getInstance().validate( command );    

        	// --------------------------------------
        	// issue the RefreshEngineCommand and return right away
        	// --------------------------------------    	
        	completableFuture = commandGateway.send( command );
    	}
        catch (Exception exc) {
            final String errMsg = "Unable to save Engine - " + exc;
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        
    	return completableFuture;
    }
   
   /**
    * Deletes the associatied value object
    * @param		command CloseEngineCommand
    * @return		CompletableFuture<Void>
    * @exception 	ProcessingException
    */
    public CompletableFuture<Void> delete( CloseEngineCommand command ) 
    throws ProcessingException, IllegalArgumentException {	
    	
    	CompletableFuture<Void> completableFuture = null;
    	
        try {  
			// --------------------------------------
        	// validate the command
        	// --------------------------------------    	
        	EngineValidator.getInstance().validate( command );    
        	
        	// --------------------------------------
        	// issue the CloseEngineCommand and return right away
        	// --------------------------------------    	
        	completableFuture = commandGateway.send( command );
        }
        catch (Exception exc) {
            final String errMsg = "Unable to delete Engine using Id = "  + command.getEngineId();
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        finally {
        }
        
        return completableFuture;
    }

    /**
     * Method to retrieve the Engine via EngineFetchOneSummary
     * @param 	summary EngineFetchOneSummary 
     * @return 	EngineFetchOneResponse
     * @exception ProcessingException - Thrown if processing any related problems
     * @exception IllegalArgumentException 
     */
    public Engine getEngine( EngineFetchOneSummary summary ) 
    throws ProcessingException, IllegalArgumentException {
    	
    	if( summary == null )
    		throw new IllegalArgumentException( "EngineFetchOneSummary arg cannot be null" );
    	
    	Engine entity = null;
    	
        try {
        	// --------------------------------------
        	// validate the fetch one summary
        	// --------------------------------------    	
        	EngineValidator.getInstance().validate( summary );    
        	
        	// --------------------------------------
        	// use queryGateway to send request to Find a Engine
        	// --------------------------------------
        	CompletableFuture<Engine> futureEntity = queryGateway.query(new FindEngineQuery( new LoadEngineFilter( summary.getEngineId() ) ), ResponseTypes.instanceOf(Engine.class));
        	
        	entity = futureEntity.get();
        }
        catch( Exception exc ) {
            final String errMsg = "Unable to locate Engine with id " + summary.getEngineId();
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        finally {
        }        
        
        return entity;
    }


    /**
     * Method to retrieve a collection of all Engines
     *
     * @return 	List<Engine> 
     * @exception ProcessingException Thrown if any problems
     */
    public List<Engine> getAllEngine() 
    throws ProcessingException {
        List<Engine> list = null;

        try {
        	CompletableFuture<List<Engine>> futureList = queryGateway.query(new FindAllEngineQuery(), ResponseTypes.multipleInstancesOf(Engine.class));
        	
        	list = futureList.get();
        }
        catch( Exception exc ) {
            String errMsg = "Failed to get all Engine";
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        finally {
        }        
        
        return list;
    }




	/**
	 * Internal helper method to load the root 
	 * 
	 * @param		id	UUID
	 * @return		Engine
	 */
	protected Engine load( UUID id ) throws ProcessingException {
		engine = EngineBusinessDelegate.getEngineInstance().getEngine( new EngineFetchOneSummary(id) );	
		return engine;
	}


//************************************************************************
// Attributes
//************************************************************************
	private final QueryGateway queryGateway;
	private final CommandGateway commandGateway;
	private final QueryUpdateEmitter queryUpdateEmitter;
	private Engine engine 	= null;
    private static final Logger LOGGER 			= Logger.getLogger(EngineBusinessDelegate.class.getName());
    
}
