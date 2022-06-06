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
 * Transmission business delegate class.
 * <p>
 * This class implements the Business Delegate design pattern for the purpose of:
 * <ol>
 * <li>Reducing coupling between the business tier and a client of the business tier by hiding all business-tier implementation details</li>
 * <li>Improving the available of Transmission related services in the case of a Transmission business related service failing.</li>
 * <li>Exposes a simpler, uniform Transmission interface to the business tier, making it easy for clients to consume a simple Java object.</li>
 * <li>Hides the communication protocol that may be required to fulfill Transmission business related services.</li>
 * </ol>
 * <p>
 * @author Dev Team
 */
public class TransmissionBusinessDelegate 
extends BaseBusinessDelegate {
//************************************************************************
// Public Methods
//************************************************************************
    /** 
     * Default Constructor 
     */
    public TransmissionBusinessDelegate()  {
    	queryGateway 		= applicationContext.getBean(QueryGateway.class);
    	commandGateway 		= applicationContext.getBean(CommandGateway.class);
    	queryUpdateEmitter  = applicationContext.getBean(QueryUpdateEmitter.class);
	}


   /**
	* Transmission Business Delegate Factory Method
	*
	* All methods are expected to be self-sufficient.
	*
	* @return 	TransmissionBusinessDelegate
	*/
	public static TransmissionBusinessDelegate getTransmissionInstance() {
		return( new TransmissionBusinessDelegate() );
	}
 
   /**
    * Creates the provided command.
    * 
    * @param		command ${class.getCreateCommandAlias()}
    * @exception    ProcessingException
    * @exception	IllegalArgumentException
    * @return		CompletableFuture<UUID>
    */
	public CompletableFuture<UUID> createTransmission( CreateTransmissionCommand command )
    throws ProcessingException, IllegalArgumentException {

		CompletableFuture<UUID> completableFuture = null;
				
		try {
			// --------------------------------------
        	// assign identity now if none
        	// -------------------------------------- 
			if ( command.getTransmissionId() == null )
				command.setTransmissionId( UUID.randomUUID() );
				
			// --------------------------------------
        	// validate the command
        	// --------------------------------------    	
        	TransmissionValidator.getInstance().validate( command );    

    		// ---------------------------------------
    		// issue the CreateTransmissionCommand - by convention the future return value for a create command
        	// that is handled by the constructor of an aggregate will return the UUID 
    		// ---------------------------------------
        	completableFuture = commandGateway.send( command );
        	
			LOGGER.log( Level.INFO, "return from Command Gateway for CreateTransmissionCommand of Transmission is " + command );
			
        }
        catch (Exception exc) {
            final String errMsg = "Unable to create Transmission - " + exc;
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        finally {
        }        
        
        return completableFuture;
    }

   /**
    * Update the provided command.
    * @param		command RefreshTransmissionCommand
    * @return		CompletableFuture<Void>
    * @exception    ProcessingException
    * @exception  	IllegalArgumentException
    */
    public CompletableFuture<Void> updateTransmission( RefreshTransmissionCommand command ) 
    throws ProcessingException, IllegalArgumentException {
    	CompletableFuture<Void> completableFuture = null;
    	
    	try {       

			// --------------------------------------
        	// validate 
        	// --------------------------------------    	
        	TransmissionValidator.getInstance().validate( command );    

        	// --------------------------------------
        	// issue the RefreshTransmissionCommand and return right away
        	// --------------------------------------    	
        	completableFuture = commandGateway.send( command );
    	}
        catch (Exception exc) {
            final String errMsg = "Unable to save Transmission - " + exc;
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        
    	return completableFuture;
    }
   
   /**
    * Deletes the associatied value object
    * @param		command CloseTransmissionCommand
    * @return		CompletableFuture<Void>
    * @exception 	ProcessingException
    */
    public CompletableFuture<Void> delete( CloseTransmissionCommand command ) 
    throws ProcessingException, IllegalArgumentException {	
    	
    	CompletableFuture<Void> completableFuture = null;
    	
        try {  
			// --------------------------------------
        	// validate the command
        	// --------------------------------------    	
        	TransmissionValidator.getInstance().validate( command );    
        	
        	// --------------------------------------
        	// issue the CloseTransmissionCommand and return right away
        	// --------------------------------------    	
        	completableFuture = commandGateway.send( command );
        }
        catch (Exception exc) {
            final String errMsg = "Unable to delete Transmission using Id = "  + command.getTransmissionId();
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        finally {
        }
        
        return completableFuture;
    }

    /**
     * Method to retrieve the Transmission via TransmissionFetchOneSummary
     * @param 	summary TransmissionFetchOneSummary 
     * @return 	TransmissionFetchOneResponse
     * @exception ProcessingException - Thrown if processing any related problems
     * @exception IllegalArgumentException 
     */
    public Transmission getTransmission( TransmissionFetchOneSummary summary ) 
    throws ProcessingException, IllegalArgumentException {
    	
    	if( summary == null )
    		throw new IllegalArgumentException( "TransmissionFetchOneSummary arg cannot be null" );
    	
    	Transmission entity = null;
    	
        try {
        	// --------------------------------------
        	// validate the fetch one summary
        	// --------------------------------------    	
        	TransmissionValidator.getInstance().validate( summary );    
        	
        	// --------------------------------------
        	// use queryGateway to send request to Find a Transmission
        	// --------------------------------------
        	CompletableFuture<Transmission> futureEntity = queryGateway.query(new FindTransmissionQuery( new LoadTransmissionFilter( summary.getTransmissionId() ) ), ResponseTypes.instanceOf(Transmission.class));
        	
        	entity = futureEntity.get();
        }
        catch( Exception exc ) {
            final String errMsg = "Unable to locate Transmission with id " + summary.getTransmissionId();
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        finally {
        }        
        
        return entity;
    }


    /**
     * Method to retrieve a collection of all Transmissions
     *
     * @return 	List<Transmission> 
     * @exception ProcessingException Thrown if any problems
     */
    public List<Transmission> getAllTransmission() 
    throws ProcessingException {
        List<Transmission> list = null;

        try {
        	CompletableFuture<List<Transmission>> futureList = queryGateway.query(new FindAllTransmissionQuery(), ResponseTypes.multipleInstancesOf(Transmission.class));
        	
        	list = futureList.get();
        }
        catch( Exception exc ) {
            String errMsg = "Failed to get all Transmission";
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
	 * @return		Transmission
	 */
	protected Transmission load( UUID id ) throws ProcessingException {
		transmission = TransmissionBusinessDelegate.getTransmissionInstance().getTransmission( new TransmissionFetchOneSummary(id) );	
		return transmission;
	}


//************************************************************************
// Attributes
//************************************************************************
	private final QueryGateway queryGateway;
	private final CommandGateway commandGateway;
	private final QueryUpdateEmitter queryUpdateEmitter;
	private Transmission transmission 	= null;
    private static final Logger LOGGER 			= Logger.getLogger(TransmissionBusinessDelegate.class.getName());
    
}
