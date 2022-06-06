/*******************************************************************************
  Oldsmobile Motor Corporation Confidential
  
  2018 Oldsmobile Motor Corporation
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Oldsmobile Motor Corporation - General Release
 ******************************************************************************/
package com.acme.controller.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.acme.api.*;
import com.acme.exception.*;
import com.acme.projector.*;
import com.acme.delegate.*;
import com.acme.entity.*;

import com.acme.automobilemarket.flows.CreateTransmissionToken;
import com.acme.automobilemarket.flows.TransferPartToken;
import com.acme.automobilemarket.flows.TotalPart;

import com.acme.helper.corda.CordaHelper;
import com.acme.helper.corda.api.PartyEnum;

import com.acme.controller.BaseCordaSpringRestController;

import net.corda.core.identity.Party;

/** 
 * Implements Struts action processing for business entity Transmission.
 *
 * @author Dev Team
 */
@CrossOrigin
@RestController
@RequestMapping("/TransmissionToken")
public class TransmissionTokenCommandRestController extends BaseCordaSpringRestController {
	
    /**
     * Handles create a Transmission token.  
     * @param		UUID	transmissionId
     * @return		CordaFuture
     */
	@PostMapping("/createToken")
    public net.corda.core.concurrent.CordaFuture createToken( @RequestParam(required=true) UUID transmissionId ) {

		// ------------------------------------------------
		//  the identifier must be associated with an existing Transmission
		// ------------------------------------------------
		Transmission transmission = getTransmission( transmissionId );

		if ( transmission == null ) {
			LOGGER.log( Level.WARNING, "A Transmission with Id " + transmissionId + " does not exist in the persistent store" );
			return null;
		}
		
		LOGGER.log( Level.INFO, "Successfully found a Transmission in the entity store for UUID {0}", transmissionId );

		net.corda.core.messaging.CordaRPCOps proxy = proxy(PartyEnum.Notary);
		net.corda.core.concurrent.CordaFuture future = null;
		
		if ( proxy != null ) {
			LOGGER.info( "Located a Corda Notary" );
			LOGGER.log( Level.INFO, "Starting a flow for CreateTransmissionToken for UUID {0}", transmission );
			future = proxy.startFlowDynamic(CreateTransmissionToken.class, 
									transmission.getTransmissionId(), transmission.getName(), transmission.getSerialNum(), transmission.getPlant(), transmission.getType() )
									.getReturnValue();
		}
		else {
			LOGGER.warning( "Failed to acquire an RPC proxy to a Corda Notary" );
		}
	
		return future;
    }

 
    /**
     * Handles deleting a Transmission entity as a token
     * @param		transmissionId	UUID 
     * @return		net.corda.core.concurrent.CordaFuture 
     */
    @DeleteMapping("/destroyToken")    
    public net.corda.core.concurrent.CordaFuture destroyToken( @RequestBody(required=true) UUID transmissionId, PartyEnum partyEnum) {                

    	// -------------------------------------------------------
    	// validate the existenence of engineId in the entity store
    	// -------------------------------------------------------
    	// TO DO
    	
		net.corda.core.messaging.CordaRPCOps proxy = proxy( partyEnum );
		net.corda.core.concurrent.CordaFuture future = null;
    	Party party = null;
    	boolean exactMatch = true;
		
		if ( proxy != null ) {
			party = party( partyEnum );

			if( party != null ) {
				LOGGER.log( Level.INFO, "Located a Corda Party for {0}", partyEnum.toString());
				LOGGER.info( "Starting a flow for TotalPart" );

				future = proxy.startFlowDynamic(TotalPart.class, 
								"Transmission",
								transmissionId).getReturnValue();
			}
			else {
				LOGGER.log( Level.WARNING, "Failed to locate a Corda Party for node {0}", partyEnum.toString() );
			}
		}
		else {
			LOGGER.log( Level.WARNING, "Failed to acquire an RPC Proxy to node {0}", partyEnum.toString() );
		}
	
		return future;

	}        
    
    /**
     * Handles transfering a token to a given Party
     * @param		transmissionId	UUID 
     * @param		partyEnum					PartyEnum
     * @return		net.corda.core.concurrent.CordaFuture 
     */
    @PostMapping("/transferToken")    
    public net.corda.core.concurrent.CordaFuture transferToken( @RequestBody(required=true) UUID transmissionId, @RequestBody(required=true) PartyEnum partyEnum) {                

    	// -------------------------------------------------------
    	// validate the existenence of engineId in the entity store
    	// -------------------------------------------------------
    	// TO DO

		net.corda.core.messaging.CordaRPCOps proxy = proxy( partyEnum );
		net.corda.core.concurrent.CordaFuture future = null;
    	Party party = null;
    	boolean exactMatch = true;
		
		if ( proxy != null ) {
			party = party( partyEnum );

			if( party != null ) {
	    		
				LOGGER.log( Level.INFO, "Located a Corda Party for {0}", partyEnum.toString());
				LOGGER.info( "Starting a flow for TransferPartToken" );

				future = proxy.startFlowDynamic(TransferPartToken.class, 
								"Transmission", 
								transmissionId, 
								party).getReturnValue();
			}
			else {
				LOGGER.log( Level.WARNING, "Failed to locate a Corda Party for node {0}", partyEnum.toString() );
			}
		}
		else {
			LOGGER.log( Level.WARNING, "Failed to acquire an RPC Proxy to node {0}", partyEnum.toString() );
		}
	
		return future;
    }
	
    
    /**
     * Helper method to return a Transmission entity from the entity store
     * 
     * @param	transmission UUID
     * @return	Transmission
     */
    protected Transmission getTransmission( UUID transmissionId ) {

		// ------------------------------------------------
		//  the identifier must be associated with an existing Transmission
		// ------------------------------------------------
		Transmission transmission = null;

    	try {  
    		transmission = TransmissionBusinessDelegate.getTransmissionInstance().getTransmission( new TransmissionFetchOneSummary( transmissionId ) );   
        }
        catch( Throwable exc ) {
            LOGGER.log( Level.WARNING, "failed to load Transmission using Id " + transmissionId );
            return null;
        }
    	
    	return transmission;

    }
  
//************************************************************************    
// Attributes
//************************************************************************
    private static final Logger LOGGER = Logger.getLogger(TransmissionTokenCommandRestController.class.getName());
    
}
