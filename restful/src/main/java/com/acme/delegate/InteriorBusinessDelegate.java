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
 * Interior business delegate class.
 * <p>
 * This class implements the Business Delegate design pattern for the purpose of:
 * <ol>
 * <li>Reducing coupling between the business tier and a client of the business tier by hiding all business-tier implementation details</li>
 * <li>Improving the available of Interior related services in the case of a Interior business related service failing.</li>
 * <li>Exposes a simpler, uniform Interior interface to the business tier, making it easy for clients to consume a simple Java object.</li>
 * <li>Hides the communication protocol that may be required to fulfill Interior business related services.</li>
 * </ol>
 * <p>
 * @author Dev Team
 */
public class InteriorBusinessDelegate 
extends BaseBusinessDelegate {
//************************************************************************
// Public Methods
//************************************************************************
    /** 
     * Default Constructor 
     */
    public InteriorBusinessDelegate()  {
    	queryGateway 		= applicationContext.getBean(QueryGateway.class);
    	commandGateway 		= applicationContext.getBean(CommandGateway.class);
    	queryUpdateEmitter  = applicationContext.getBean(QueryUpdateEmitter.class);
	}


   /**
	* Interior Business Delegate Factory Method
	*
	* All methods are expected to be self-sufficient.
	*
	* @return 	InteriorBusinessDelegate
	*/
	public static InteriorBusinessDelegate getInteriorInstance() {
		return( new InteriorBusinessDelegate() );
	}
 
   /**
    * Creates the provided command.
    * 
    * @param		command ${class.getCreateCommandAlias()}
    * @exception    ProcessingException
    * @exception	IllegalArgumentException
    * @return		CompletableFuture<UUID>
    */
	public CompletableFuture<UUID> createInterior( CreateInteriorCommand command )
    throws ProcessingException, IllegalArgumentException {

		CompletableFuture<UUID> completableFuture = null;
				
		try {
			// --------------------------------------
        	// assign identity now if none
        	// -------------------------------------- 
			if ( command.getInteriorId() == null )
				command.setInteriorId( UUID.randomUUID() );
				
			// --------------------------------------
        	// validate the command
        	// --------------------------------------    	
        	InteriorValidator.getInstance().validate( command );    

    		// ---------------------------------------
    		// issue the CreateInteriorCommand - by convention the future return value for a create command
        	// that is handled by the constructor of an aggregate will return the UUID 
    		// ---------------------------------------
        	completableFuture = commandGateway.send( command );
        	
			LOGGER.log( Level.INFO, "return from Command Gateway for CreateInteriorCommand of Interior is " + command );
			
        }
        catch (Exception exc) {
            final String errMsg = "Unable to create Interior - " + exc;
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        finally {
        }        
        
        return completableFuture;
    }

   /**
    * Update the provided command.
    * @param		command RefreshInteriorCommand
    * @return		CompletableFuture<Void>
    * @exception    ProcessingException
    * @exception  	IllegalArgumentException
    */
    public CompletableFuture<Void> updateInterior( RefreshInteriorCommand command ) 
    throws ProcessingException, IllegalArgumentException {
    	CompletableFuture<Void> completableFuture = null;
    	
    	try {       

			// --------------------------------------
        	// validate 
        	// --------------------------------------    	
        	InteriorValidator.getInstance().validate( command );    

        	// --------------------------------------
        	// issue the RefreshInteriorCommand and return right away
        	// --------------------------------------    	
        	completableFuture = commandGateway.send( command );
    	}
        catch (Exception exc) {
            final String errMsg = "Unable to save Interior - " + exc;
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        
    	return completableFuture;
    }
   
   /**
    * Deletes the associatied value object
    * @param		command CloseInteriorCommand
    * @return		CompletableFuture<Void>
    * @exception 	ProcessingException
    */
    public CompletableFuture<Void> delete( CloseInteriorCommand command ) 
    throws ProcessingException, IllegalArgumentException {	
    	
    	CompletableFuture<Void> completableFuture = null;
    	
        try {  
			// --------------------------------------
        	// validate the command
        	// --------------------------------------    	
        	InteriorValidator.getInstance().validate( command );    
        	
        	// --------------------------------------
        	// issue the CloseInteriorCommand and return right away
        	// --------------------------------------    	
        	completableFuture = commandGateway.send( command );
        }
        catch (Exception exc) {
            final String errMsg = "Unable to delete Interior using Id = "  + command.getInteriorId();
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        finally {
        }
        
        return completableFuture;
    }

    /**
     * Method to retrieve the Interior via InteriorFetchOneSummary
     * @param 	summary InteriorFetchOneSummary 
     * @return 	InteriorFetchOneResponse
     * @exception ProcessingException - Thrown if processing any related problems
     * @exception IllegalArgumentException 
     */
    public Interior getInterior( InteriorFetchOneSummary summary ) 
    throws ProcessingException, IllegalArgumentException {
    	
    	if( summary == null )
    		throw new IllegalArgumentException( "InteriorFetchOneSummary arg cannot be null" );
    	
    	Interior entity = null;
    	
        try {
        	// --------------------------------------
        	// validate the fetch one summary
        	// --------------------------------------    	
        	InteriorValidator.getInstance().validate( summary );    
        	
        	// --------------------------------------
        	// use queryGateway to send request to Find a Interior
        	// --------------------------------------
        	CompletableFuture<Interior> futureEntity = queryGateway.query(new FindInteriorQuery( new LoadInteriorFilter( summary.getInteriorId() ) ), ResponseTypes.instanceOf(Interior.class));
        	
        	entity = futureEntity.get();
        }
        catch( Exception exc ) {
            final String errMsg = "Unable to locate Interior with id " + summary.getInteriorId();
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        finally {
        }        
        
        return entity;
    }


    /**
     * Method to retrieve a collection of all Interiors
     *
     * @return 	List<Interior> 
     * @exception ProcessingException Thrown if any problems
     */
    public List<Interior> getAllInterior() 
    throws ProcessingException {
        List<Interior> list = null;

        try {
        	CompletableFuture<List<Interior>> futureList = queryGateway.query(new FindAllInteriorQuery(), ResponseTypes.multipleInstancesOf(Interior.class));
        	
        	list = futureList.get();
        }
        catch( Exception exc ) {
            String errMsg = "Failed to get all Interior";
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
	 * @return		Interior
	 */
	protected Interior load( UUID id ) throws ProcessingException {
		interior = InteriorBusinessDelegate.getInteriorInstance().getInterior( new InteriorFetchOneSummary(id) );	
		return interior;
	}


//************************************************************************
// Attributes
//************************************************************************
	private final QueryGateway queryGateway;
	private final CommandGateway commandGateway;
	private final QueryUpdateEmitter queryUpdateEmitter;
	private Interior interior 	= null;
    private static final Logger LOGGER 			= Logger.getLogger(InteriorBusinessDelegate.class.getName());
    
}
