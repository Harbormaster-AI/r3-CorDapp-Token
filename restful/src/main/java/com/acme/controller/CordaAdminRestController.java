/*******************************************************************************
  Oldsmobile Motor Corporation Confidential
  
  2018 Oldsmobile Motor Corporation
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Oldsmobile Motor Corporation - General Release
 ******************************************************************************/
package com.acme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;

import com.acme.api.*;
import com.acme.exception.*;

import com.acme.helper.corda.CordaHelper;
import com.acme.helper.corda.api.PartyEnum;
import com.acme.helper.corda.api.Automobile;

import com.acme.controller.BaseCordaSpringRestController;

import com.acme.automobilemarket.flows.IssueNewAutomobile;
import com.acme.automobilemarket.flows.TransferAutomobileToken;

import com.acme.automobilemarket.flows.*;

/** 
 * Implements Spring Controller for Automobile level actions
 *
 * @author Dev Team
 */
@CrossOrigin
@RestController
@RequestMapping("/Automobile")
public class CordaAdminRestController extends BaseCordaSpringRestController {


	/**
	 * Will transfer the provided tokens as a new Automobile to the specified party
	 * @param		 Automobile app
	 * @param		PartyEnum	party
	 * @return		net.corda.core.concurrent.CordaFuture
	 */
	@PutMapping("/bulkCreateTokens")
	public net.corda.core.concurrent.CordaFuture bulkCreateTokens( @RequestBody(required=true) Automobile app, PartyEnum partyEnum ) {
	
		net.corda.core.messaging.CordaRPCOps proxy = proxy( partyEnum );
		net.corda.core.concurrent.CordaFuture future = null;
		boolean exactMatch = true;
		
		if ( proxy != null ) {
			LOGGER.log( Level.INFO, "Located a Corda Party for {0}", partyEnum.toString());
				
            LOGGER.info( "Starting a flow for CreateNewChassis" );
            future = proxy.startFlowDynamic( CreateChassisToken.class, app.chassisId ).getReturnValue();

            LOGGER.info( "Starting a flow for CreateNewBody" );
            future = proxy.startFlowDynamic( CreateBodyToken.class, app.bodyId ).getReturnValue();

            LOGGER.info( "Starting a flow for CreateNewEngine" );
            future = proxy.startFlowDynamic( CreateEngineToken.class, app.engineId ).getReturnValue();

            LOGGER.info( "Starting a flow for CreateNewTransmission" );
            future = proxy.startFlowDynamic( CreateTransmissionToken.class, app.transmissionId ).getReturnValue();

            LOGGER.info( "Starting a flow for CreateNewBraking" );
            future = proxy.startFlowDynamic( CreateBrakingToken.class, app.brakingId ).getReturnValue();

            LOGGER.info( "Starting a flow for CreateNewInterior" );
            future = proxy.startFlowDynamic( CreateInteriorToken.class, app.interiorId ).getReturnValue();

		}
		else {
			LOGGER.log( Level.WARNING, "Failed to acquire an RPC proxy to node {0}", partyEnum.toString() );
		}
	
		return future;
		
	}


	/**
	 * Handles bulk deleting to a target party
	 * @param		Id	String 
	 * @param		partyEnum					PartyEnum
	 * @return		net.corda.core.concurrent.CordaFuture 
	 */
	@DeleteMapping("/bulkDestroyTokens")    
	public net.corda.core.concurrent.CordaFuture bulkDestroyTokens( @RequestBody(required=true) Automobile app, PartyEnum partyEnum) {                

		net.corda.core.messaging.CordaRPCOps proxy = proxy( partyEnum );
		net.corda.core.concurrent.CordaFuture future = null;
		boolean exactMatch = true;
		
		
		if ( proxy != null ) {
			Party party = party( partyEnum );

			if( party != null ) {
				LOGGER.log( Level.INFO, "Located a Corda Party for {0}", partyEnum.toString());
					// only start the flow if a chassisId has been provided
				if ( app.chassisId != null ) {
					LOGGER.info( "Starting a flow for TotalPart on partyEnum.toString() for a Chassis" );
					future = proxy.startFlowDynamic( TotalPart.class, app.chassisId, party ).getReturnValue();
				}
					// only start the flow if a bodyId has been provided
				if ( app.bodyId != null ) {
					LOGGER.info( "Starting a flow for TotalPart on partyEnum.toString() for a Body" );
					future = proxy.startFlowDynamic( TotalPart.class, app.bodyId, party ).getReturnValue();
				}
					// only start the flow if a engineId has been provided
				if ( app.engineId != null ) {
					LOGGER.info( "Starting a flow for TotalPart on partyEnum.toString() for a Engine" );
					future = proxy.startFlowDynamic( TotalPart.class, app.engineId, party ).getReturnValue();
				}
					// only start the flow if a transmissionId has been provided
				if ( app.transmissionId != null ) {
					LOGGER.info( "Starting a flow for TotalPart on partyEnum.toString() for a Transmission" );
					future = proxy.startFlowDynamic( TotalPart.class, app.transmissionId, party ).getReturnValue();
				}
					// only start the flow if a brakingId has been provided
				if ( app.brakingId != null ) {
					LOGGER.info( "Starting a flow for TotalPart on partyEnum.toString() for a Braking" );
					future = proxy.startFlowDynamic( TotalPart.class, app.brakingId, party ).getReturnValue();
				}
					// only start the flow if a interiorId has been provided
				if ( app.interiorId != null ) {
					LOGGER.info( "Starting a flow for TotalPart on partyEnum.toString() for a Interior" );
					future = proxy.startFlowDynamic( TotalPart.class, app.interiorId, party ).getReturnValue();
				}
			}
		}
		else {
			LOGGER.log( Level.WARNING, "Failed to acquire an RPC proxy to node {0}", partyEnum.toString() );
		}
	
		return future;
	
	}   

    /**
     * Handles bulk transfering tokens to a given Party
     * @param		Id	String 
     * @param		partyEnum					PartyEnum
     * @return		net.corda.core.concurrent.CordaFuture 
     */
    @PostMapping("/bulkTransferTokens")    
    public net.corda.core.concurrent.CordaFuture bulkTransferTokens( @RequestBody(required=true) Automobile app, @RequestBody(required=true) PartyEnum partyEnum) {                
		net.corda.core.messaging.CordaRPCOps proxy = proxy( partyEnum );
		net.corda.core.concurrent.CordaFuture future = null;
		boolean exactMatch = true;
		
		if ( proxy != null ) {
			LOGGER.log( Level.INFO, "Located a Corda Party for {0}", partyEnum.toString());
			Party party = party( partyEnum );

			if( party != null ) {
			// only start the flow if a chassis${identifierFieldName_lc} has been provided
				if ( app.chassisId != null ) {
					LOGGER.info( "Starting a flow for TransferPartToken to partyEnum.toString() for a Chassis" );
					future = proxy.startFlowDynamic( TransferPartToken.class, app.chassisId, party ).getReturnValue();
				}
			// only start the flow if a body${identifierFieldName_lc} has been provided
				if ( app.bodyId != null ) {
					LOGGER.info( "Starting a flow for TransferPartToken to partyEnum.toString() for a Body" );
					future = proxy.startFlowDynamic( TransferPartToken.class, app.bodyId, party ).getReturnValue();
				}
			// only start the flow if a engine${identifierFieldName_lc} has been provided
				if ( app.engineId != null ) {
					LOGGER.info( "Starting a flow for TransferPartToken to partyEnum.toString() for a Engine" );
					future = proxy.startFlowDynamic( TransferPartToken.class, app.engineId, party ).getReturnValue();
				}
			// only start the flow if a transmission${identifierFieldName_lc} has been provided
				if ( app.transmissionId != null ) {
					LOGGER.info( "Starting a flow for TransferPartToken to partyEnum.toString() for a Transmission" );
					future = proxy.startFlowDynamic( TransferPartToken.class, app.transmissionId, party ).getReturnValue();
				}
			// only start the flow if a braking${identifierFieldName_lc} has been provided
				if ( app.brakingId != null ) {
					LOGGER.info( "Starting a flow for TransferPartToken to partyEnum.toString() for a Braking" );
					future = proxy.startFlowDynamic( TransferPartToken.class, app.brakingId, party ).getReturnValue();
				}
			// only start the flow if a interior${identifierFieldName_lc} has been provided
				if ( app.interiorId != null ) {
					LOGGER.info( "Starting a flow for TransferPartToken to partyEnum.toString() for a Interior" );
					future = proxy.startFlowDynamic( TransferPartToken.class, app.interiorId, party ).getReturnValue();
				}
			}
		}
		else {
			LOGGER.log( Level.WARNING, "Failed to acquire an RPC proxy to node {0}", partyEnum.toString() );
		}
	
		return future;
	
	}   
    
    /**
     * Will transfer the provided tokens as a new Automobile to the specified party
     * @param		InteriorParts interiorParts
     * @param		PartyEnum partyEnum
     * @return		net.corda.core.concurrent.CordaFuture
     */
	@PutMapping("/issueNew")
    public net.corda.core.concurrent.CordaFuture issueNewAutomobile( @RequestBody(required=true) Automobile app, @RequestBody(required=true) PartyEnum partyEnum ) {

		net.corda.core.messaging.CordaRPCOps proxy = proxy( partyEnum );
		net.corda.core.concurrent.CordaFuture future = null;
    	Party party = null;
    	boolean exactMatch = true;
		
		if ( proxy != null ) {
			party = party( partyEnum );

			if( party != null ) {
				LOGGER.log( Level.INFO, "Located a Corda Party for {0}", partyEnum.toString());
				LOGGER.info( "Starting a flow for IssueNewAutomobile" );

				future = proxy.startFlowDynamic(IssueNewAutomobile.class, app.chassisId, app.bodyId, app.engineId, app.transmissionId, app.brakingId, app.interiorId, party).getReturnValue();
			}
			else {
				LOGGER.log( Level.WARNING, "Failed to locate a Corda Party for node {0}", partyEnum.toString() );
			}
		}
		else {
			LOGGER.log( Level.WARNING, "Failed to acquire an RPC proxy to node {0}", partyEnum.toString() );
		}
	
		return future;
    	
	}

    /**
     * Will sell the Automobile to the specified party
     * @param		InteriorParts interiorParts
     * @param		PartyEnum partyEnum
     * @return		net.corda.core.concurrent.CordaFuture
     */
	@PutMapping("/transferToken")
    public net.corda.core.concurrent.CordaFuture transferAutomobileToken(@RequestBody(required=true) Automobile app, @RequestBody(required=true) PartyEnum partyEnum ) {

		net.corda.core.messaging.CordaRPCOps proxy = proxy( partyEnum );
		net.corda.core.concurrent.CordaFuture future = null;
    	Party party = null;
    	boolean exactMatch = true;
		
		if ( proxy != null ) {
			party = party( partyEnum );

			if( party != null ) {
				LOGGER.log( Level.INFO, "Located a Corda Party for {0}", partyEnum.toString());
				LOGGER.info( "Starting a flow for TransferAutomobileToken " );

				future = proxy.startFlowDynamic(TransferAutomobileToken.class, app.chassisId, app.bodyId, app.engineId, app.transmissionId, app.brakingId, app.interiorId, party).getReturnValue();
			}
			else {
				LOGGER.log( Level.WARNING, "Failed to locate a Corda Party for node {0}", partyEnum.toString() );
			}
		}
		else {
			LOGGER.log( Level.WARNING, "Failed to acquire an RPC proxy to node {0}", partyEnum.toString() );
		}
	
		return future;

	}    
    
    /**
     * Returns the flows for the associated node
     * 
     * @param		partyEnum		PartyEnum
     * @return		List<String>
     */
    @GetMapping("/flows")
    public java.util.List<java.lang.String> flows( PartyEnum partyEnum) {
    	LOGGER.log( Level.INFO, "Calling registeredFlows for Party: {0}", partyEnum.toString() );
    	return proxy(partyEnum).registeredFlows();
    }
    
    /**
     * Returns the NetworkParameters for the associated node
     * 
     * @param		partyEnum		PartyEnum
     * @return		String
     */
    @GetMapping("/networkParameters")
    public String networkParameters( PartyEnum partyEnum) {
    	LOGGER.log( Level.INFO, "Calling getNetworkParameters for Party: {0}", partyEnum.toString() );
    	return proxy(partyEnum).getNetworkParameters().toString();
    }
    
    /**
     * Returns the notary identies for the associated node
     * 
     * @param		partyEnum		PartyEnum
     * @return		java.util.List<java.lang.String>
     */
    @GetMapping("/notaryIdentities")
    public java.util.List<java.lang.String> notaryIdentities( PartyEnum partyEnum) {
    	LOGGER.log( Level.INFO, "Calling notaryIdentities for Party: {0}", partyEnum.toString() );
    	
    	List<Party> parties 					= proxy(partyEnum).notaryIdentities();
    	java.util.List<java.lang.String> list 	= new ArrayList<>();
    	
    	if ( parties != null && parties.size() > 0 ) {
    		list = parties.stream().
    				map( p -> p.getName().toString() ).
    				collect(Collectors.toList());
    	}
    	
    	LOGGER.log( Level.INFO, "NotaryIdentities for {0} is: {1}", new Object[] { partyEnum.toString(), list.toString() } );
    	
    	return list;
    }
 
    /**
     * Returns the notary identies for the associated node
     * 
     * @param		partyEnum		PartyEnum
     * @return		String
     */
    @GetMapping("/nodeInfo")
    public String nodeInfo( PartyEnum partyEnum) {
    	LOGGER.log( Level.INFO, "Calling nodeInfo for Party: {0}", partyEnum.toString() );
    	final String retVal =  proxy(partyEnum).nodeInfo().toString();
    	LOGGER.log( Level.INFO, "NodeInfo for {0} is: {1}", new Object[] { partyEnum.toString(), retVal } );
    	return( retVal );
    }
    
    /**
     * Returns the notary identies for the associated node
     * 
     * @param		partyEnum		PartyEnum
     * @return		String
     */
    @GetMapping("/nodeDiagnosticInfo")
    public String nodeDiagnosticInfo( PartyEnum partyEnum ) {
    	LOGGER.log( Level.INFO, "Calling nodeDiagnosticInfo for Party: {0}", partyEnum.toString() );
    	final String retVal = proxy(partyEnum).nodeDiagnosticInfo().toString();
    	LOGGER.log( Level.INFO, "NodeDiagnosticInfo for {0} is: {1}", new Object[] {partyEnum.toString(), retVal} );
    	return( retVal );
    }
    
    
    /**
     * Returns the notary identies for the associated node
     * @return		String
     */
    @GetMapping("/vaultQuery")
    public String vaultQuery( PartyEnum partyEnum ) {
    	LOGGER.log( Level.INFO, "Calling vaultQuery for Party: {0}", partyEnum.toString() );
    	final String retVal =  proxy(partyEnum).vaultQuery(com.r3.corda.lib.tokens.contracts.states.EvolvableTokenType.class).toString();
    	LOGGER.log( Level.INFO, "VaultQuery for {0} is: {1}", new Object[] {partyEnum.toString(), retVal} );
    	return( retVal );
    }
    
    
    //************************************************************************    
	// Attributes
	//************************************************************************
	private static final Logger LOGGER = Logger.getLogger(CordaAdminRestController.class.getName());
}
