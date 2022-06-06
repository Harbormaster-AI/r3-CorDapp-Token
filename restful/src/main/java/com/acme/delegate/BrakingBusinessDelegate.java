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
 * Braking business delegate class.
 * <p>
 * This class implements the Business Delegate design pattern for the purpose of:
 * <ol>
 * <li>Reducing coupling between the business tier and a client of the business tier by hiding all business-tier implementation details</li>
 * <li>Improving the available of Braking related services in the case of a Braking business related service failing.</li>
 * <li>Exposes a simpler, uniform Braking interface to the business tier, making it easy for clients to consume a simple Java object.</li>
 * <li>Hides the communication protocol that may be required to fulfill Braking business related services.</li>
 * </ol>
 * <p>
 * @author Dev Team
 */
public class BrakingBusinessDelegate 
extends BaseBusinessDelegate {
//************************************************************************
// Public Methods
//************************************************************************
    /** 
     * Default Constructor 
     */
    public BrakingBusinessDelegate()  {
    	queryGateway 		= applicationContext.getBean(QueryGateway.class);
    	commandGateway 		= applicationContext.getBean(CommandGateway.class);
    	queryUpdateEmitter  = applicationContext.getBean(QueryUpdateEmitter.class);
	}


   /**
	* Braking Business Delegate Factory Method
	*
	* All methods are expected to be self-sufficient.
	*
	* @return 	BrakingBusinessDelegate
	*/
	public static BrakingBusinessDelegate getBrakingInstance() {
		return( new BrakingBusinessDelegate() );
	}
 
   /**
    * Creates the provided command.
    * 
    * @param		command ${class.getCreateCommandAlias()}
    * @exception    ProcessingException
    * @exception	IllegalArgumentException
    * @return		CompletableFuture<UUID>
    */
	public CompletableFuture<UUID> createBraking( CreateBrakingCommand command )
    throws ProcessingException, IllegalArgumentException {

		CompletableFuture<UUID> completableFuture = null;
				
		try {
			// --------------------------------------
        	// assign identity now if none
        	// -------------------------------------- 
			if ( command.getBrakingId() == null )
				command.setBrakingId( UUID.randomUUID() );
				
			// --------------------------------------
        	// validate the command
        	// --------------------------------------    	
        	BrakingValidator.getInstance().validate( command );    

    		// ---------------------------------------
    		// issue the CreateBrakingCommand - by convention the future return value for a create command
        	// that is handled by the constructor of an aggregate will return the UUID 
    		// ---------------------------------------
        	completableFuture = commandGateway.send( command );
        	
			LOGGER.log( Level.INFO, "return from Command Gateway for CreateBrakingCommand of Braking is " + command );
			
        }
        catch (Exception exc) {
            final String errMsg = "Unable to create Braking - " + exc;
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        finally {
        }        
        
        return completableFuture;
    }

   /**
    * Update the provided command.
    * @param		command RefreshBrakingCommand
    * @return		CompletableFuture<Void>
    * @exception    ProcessingException
    * @exception  	IllegalArgumentException
    */
    public CompletableFuture<Void> updateBraking( RefreshBrakingCommand command ) 
    throws ProcessingException, IllegalArgumentException {
    	CompletableFuture<Void> completableFuture = null;
    	
    	try {       

			// --------------------------------------
        	// validate 
        	// --------------------------------------    	
        	BrakingValidator.getInstance().validate( command );    

        	// --------------------------------------
        	// issue the RefreshBrakingCommand and return right away
        	// --------------------------------------    	
        	completableFuture = commandGateway.send( command );
    	}
        catch (Exception exc) {
            final String errMsg = "Unable to save Braking - " + exc;
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        
    	return completableFuture;
    }
   
   /**
    * Deletes the associatied value object
    * @param		command CloseBrakingCommand
    * @return		CompletableFuture<Void>
    * @exception 	ProcessingException
    */
    public CompletableFuture<Void> delete( CloseBrakingCommand command ) 
    throws ProcessingException, IllegalArgumentException {	
    	
    	CompletableFuture<Void> completableFuture = null;
    	
        try {  
			// --------------------------------------
        	// validate the command
        	// --------------------------------------    	
        	BrakingValidator.getInstance().validate( command );    
        	
        	// --------------------------------------
        	// issue the CloseBrakingCommand and return right away
        	// --------------------------------------    	
        	completableFuture = commandGateway.send( command );
        }
        catch (Exception exc) {
            final String errMsg = "Unable to delete Braking using Id = "  + command.getBrakingId();
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        finally {
        }
        
        return completableFuture;
    }

    /**
     * Method to retrieve the Braking via BrakingFetchOneSummary
     * @param 	summary BrakingFetchOneSummary 
     * @return 	BrakingFetchOneResponse
     * @exception ProcessingException - Thrown if processing any related problems
     * @exception IllegalArgumentException 
     */
    public Braking getBraking( BrakingFetchOneSummary summary ) 
    throws ProcessingException, IllegalArgumentException {
    	
    	if( summary == null )
    		throw new IllegalArgumentException( "BrakingFetchOneSummary arg cannot be null" );
    	
    	Braking entity = null;
    	
        try {
        	// --------------------------------------
        	// validate the fetch one summary
        	// --------------------------------------    	
        	BrakingValidator.getInstance().validate( summary );    
        	
        	// --------------------------------------
        	// use queryGateway to send request to Find a Braking
        	// --------------------------------------
        	CompletableFuture<Braking> futureEntity = queryGateway.query(new FindBrakingQuery( new LoadBrakingFilter( summary.getBrakingId() ) ), ResponseTypes.instanceOf(Braking.class));
        	
        	entity = futureEntity.get();
        }
        catch( Exception exc ) {
            final String errMsg = "Unable to locate Braking with id " + summary.getBrakingId();
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        finally {
        }        
        
        return entity;
    }


    /**
     * Method to retrieve a collection of all Brakings
     *
     * @return 	List<Braking> 
     * @exception ProcessingException Thrown if any problems
     */
    public List<Braking> getAllBraking() 
    throws ProcessingException {
        List<Braking> list = null;

        try {
        	CompletableFuture<List<Braking>> futureList = queryGateway.query(new FindAllBrakingQuery(), ResponseTypes.multipleInstancesOf(Braking.class));
        	
        	list = futureList.get();
        }
        catch( Exception exc ) {
            String errMsg = "Failed to get all Braking";
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
	 * @return		Braking
	 */
	protected Braking load( UUID id ) throws ProcessingException {
		braking = BrakingBusinessDelegate.getBrakingInstance().getBraking( new BrakingFetchOneSummary(id) );	
		return braking;
	}


//************************************************************************
// Attributes
//************************************************************************
	private final QueryGateway queryGateway;
	private final CommandGateway commandGateway;
	private final QueryUpdateEmitter queryUpdateEmitter;
	private Braking braking 	= null;
    private static final Logger LOGGER 			= Logger.getLogger(BrakingBusinessDelegate.class.getName());
    
}
