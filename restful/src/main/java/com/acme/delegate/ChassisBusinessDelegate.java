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
 * Chassis business delegate class.
 * <p>
 * This class implements the Business Delegate design pattern for the purpose of:
 * <ol>
 * <li>Reducing coupling between the business tier and a client of the business tier by hiding all business-tier implementation details</li>
 * <li>Improving the available of Chassis related services in the case of a Chassis business related service failing.</li>
 * <li>Exposes a simpler, uniform Chassis interface to the business tier, making it easy for clients to consume a simple Java object.</li>
 * <li>Hides the communication protocol that may be required to fulfill Chassis business related services.</li>
 * </ol>
 * <p>
 * @author Dev Team
 */
public class ChassisBusinessDelegate 
extends BaseBusinessDelegate {
//************************************************************************
// Public Methods
//************************************************************************
    /** 
     * Default Constructor 
     */
    public ChassisBusinessDelegate()  {
    	queryGateway 		= applicationContext.getBean(QueryGateway.class);
    	commandGateway 		= applicationContext.getBean(CommandGateway.class);
    	queryUpdateEmitter  = applicationContext.getBean(QueryUpdateEmitter.class);
	}


   /**
	* Chassis Business Delegate Factory Method
	*
	* All methods are expected to be self-sufficient.
	*
	* @return 	ChassisBusinessDelegate
	*/
	public static ChassisBusinessDelegate getChassisInstance() {
		return( new ChassisBusinessDelegate() );
	}
 
   /**
    * Creates the provided command.
    * 
    * @param		command ${class.getCreateCommandAlias()}
    * @exception    ProcessingException
    * @exception	IllegalArgumentException
    * @return		CompletableFuture<UUID>
    */
	public CompletableFuture<UUID> createChassis( CreateChassisCommand command )
    throws ProcessingException, IllegalArgumentException {

		CompletableFuture<UUID> completableFuture = null;
				
		try {
			// --------------------------------------
        	// assign identity now if none
        	// -------------------------------------- 
			if ( command.getChassisId() == null )
				command.setChassisId( UUID.randomUUID() );
				
			// --------------------------------------
        	// validate the command
        	// --------------------------------------    	
        	ChassisValidator.getInstance().validate( command );    

    		// ---------------------------------------
    		// issue the CreateChassisCommand - by convention the future return value for a create command
        	// that is handled by the constructor of an aggregate will return the UUID 
    		// ---------------------------------------
        	completableFuture = commandGateway.send( command );
        	
			LOGGER.log( Level.INFO, "return from Command Gateway for CreateChassisCommand of Chassis is " + command );
			
        }
        catch (Exception exc) {
            final String errMsg = "Unable to create Chassis - " + exc;
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        finally {
        }        
        
        return completableFuture;
    }

   /**
    * Update the provided command.
    * @param		command RefreshChassisCommand
    * @return		CompletableFuture<Void>
    * @exception    ProcessingException
    * @exception  	IllegalArgumentException
    */
    public CompletableFuture<Void> updateChassis( RefreshChassisCommand command ) 
    throws ProcessingException, IllegalArgumentException {
    	CompletableFuture<Void> completableFuture = null;
    	
    	try {       

			// --------------------------------------
        	// validate 
        	// --------------------------------------    	
        	ChassisValidator.getInstance().validate( command );    

        	// --------------------------------------
        	// issue the RefreshChassisCommand and return right away
        	// --------------------------------------    	
        	completableFuture = commandGateway.send( command );
    	}
        catch (Exception exc) {
            final String errMsg = "Unable to save Chassis - " + exc;
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        
    	return completableFuture;
    }
   
   /**
    * Deletes the associatied value object
    * @param		command CloseChassisCommand
    * @return		CompletableFuture<Void>
    * @exception 	ProcessingException
    */
    public CompletableFuture<Void> delete( CloseChassisCommand command ) 
    throws ProcessingException, IllegalArgumentException {	
    	
    	CompletableFuture<Void> completableFuture = null;
    	
        try {  
			// --------------------------------------
        	// validate the command
        	// --------------------------------------    	
        	ChassisValidator.getInstance().validate( command );    
        	
        	// --------------------------------------
        	// issue the CloseChassisCommand and return right away
        	// --------------------------------------    	
        	completableFuture = commandGateway.send( command );
        }
        catch (Exception exc) {
            final String errMsg = "Unable to delete Chassis using Id = "  + command.getChassisId();
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        finally {
        }
        
        return completableFuture;
    }

    /**
     * Method to retrieve the Chassis via ChassisFetchOneSummary
     * @param 	summary ChassisFetchOneSummary 
     * @return 	ChassisFetchOneResponse
     * @exception ProcessingException - Thrown if processing any related problems
     * @exception IllegalArgumentException 
     */
    public Chassis getChassis( ChassisFetchOneSummary summary ) 
    throws ProcessingException, IllegalArgumentException {
    	
    	if( summary == null )
    		throw new IllegalArgumentException( "ChassisFetchOneSummary arg cannot be null" );
    	
    	Chassis entity = null;
    	
        try {
        	// --------------------------------------
        	// validate the fetch one summary
        	// --------------------------------------    	
        	ChassisValidator.getInstance().validate( summary );    
        	
        	// --------------------------------------
        	// use queryGateway to send request to Find a Chassis
        	// --------------------------------------
        	CompletableFuture<Chassis> futureEntity = queryGateway.query(new FindChassisQuery( new LoadChassisFilter( summary.getChassisId() ) ), ResponseTypes.instanceOf(Chassis.class));
        	
        	entity = futureEntity.get();
        }
        catch( Exception exc ) {
            final String errMsg = "Unable to locate Chassis with id " + summary.getChassisId();
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        finally {
        }        
        
        return entity;
    }


    /**
     * Method to retrieve a collection of all Chassiss
     *
     * @return 	List<Chassis> 
     * @exception ProcessingException Thrown if any problems
     */
    public List<Chassis> getAllChassis() 
    throws ProcessingException {
        List<Chassis> list = null;

        try {
        	CompletableFuture<List<Chassis>> futureList = queryGateway.query(new FindAllChassisQuery(), ResponseTypes.multipleInstancesOf(Chassis.class));
        	
        	list = futureList.get();
        }
        catch( Exception exc ) {
            String errMsg = "Failed to get all Chassis";
            LOGGER.log( Level.WARNING, errMsg, exc );
            throw new ProcessingException( errMsg, exc );
        }
        finally {
        }        
        
        return list;
    }




    /**
     * finder method to findByNameLike
     * @param 		String name
     * @return		Chassis
     */     
	public Chassis findByNameLike( FindByNameLikeQuery query ) {
		Chassis result = null;
        try {  
		    CompletableFuture<Chassis> futureResult = queryGateway.query(query, ResponseTypes.instanceOf(Chassis.class));
        	result = futureResult.get();
        }
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING, "Failed to execute findByNameLike", exc );
        }
        return result;
	}

    /**
     * finder method to findBySerialNum
     * @param 		String serialNum
     * @return		Chassis
     */     
	public Chassis findBySerialNum( FindBySerialNumQuery query ) {
		Chassis result = null;
        try {  
		    CompletableFuture<Chassis> futureResult = queryGateway.query(query, ResponseTypes.instanceOf(Chassis.class));
        	result = futureResult.get();
        }
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING, "Failed to execute findBySerialNum", exc );
        }
        return result;
	}

    /**
     * finder method to findByType
     * @param 		ChassisType type
     * @return		List<Chassis>
     */     
	public List<Chassis> findByType( FindByTypeQuery query ) {
		List<Chassis> result = null;
        try {  
		    CompletableFuture<List<Chassis>> futureResult = queryGateway.query(query, ResponseTypes.multipleInstancesOf(Chassis.class));
        	result = futureResult.get();
        }
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING, "Failed to execute findByType", exc );
        }
        return result;
	}

	/**
	 * Internal helper method to load the root 
	 * 
	 * @param		id	UUID
	 * @return		Chassis
	 */
	protected Chassis load( UUID id ) throws ProcessingException {
		chassis = ChassisBusinessDelegate.getChassisInstance().getChassis( new ChassisFetchOneSummary(id) );	
		return chassis;
	}


//************************************************************************
// Attributes
//************************************************************************
	private final QueryGateway queryGateway;
	private final CommandGateway commandGateway;
	private final QueryUpdateEmitter queryUpdateEmitter;
	private Chassis chassis 	= null;
    private static final Logger LOGGER 			= Logger.getLogger(ChassisBusinessDelegate.class.getName());
    
}
