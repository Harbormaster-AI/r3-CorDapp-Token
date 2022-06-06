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
import com.acme.delegate.*;
import com.acme.entity.*;
import com.acme.exception.*;
import com.acme.projector.*;

import com.acme.controller.*;

/** 
 * Implements Spring Controller command CQRS processing for entity Transmission.
 *
 * @author Dev Team
 */
@CrossOrigin
@RestController
@RequestMapping("/Transmission")
public class TransmissionCommandRestController extends BaseSpringRestController {

    /**
     * Handles create a Transmission.  if not key provided, calls create, otherwise calls save
     * @param		Transmission	transmission
     * @return		CompletableFuture<UUID> 
     */
	@PostMapping("/create")
    public CompletableFuture<UUID> create( @RequestBody(required=true) CreateTransmissionCommand command ) {
		CompletableFuture<UUID> completableFuture = null;
		try {       
        	
			completableFuture = TransmissionBusinessDelegate.getTransmissionInstance().createTransmission( command );
        }
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING, exc.getMessage(), exc );        	
        }
		
		return completableFuture;
    }

    /**
     * Handles updating a Transmission.  if no key provided, calls create, otherwise calls save
     * @param		Transmission transmission
     * @return		CompletableFuture<Void>
     */
	@PutMapping("/update")
    public CompletableFuture<Void> update( @RequestBody(required=true) RefreshTransmissionCommand command ) {
		CompletableFuture<Void> completableFuture = null;
		try {                        	        
			// -----------------------------------------------
			// delegate the RefreshTransmissionCommand
			// -----------------------------------------------
			completableFuture = TransmissionBusinessDelegate.getTransmissionInstance().updateTransmission(command);;
	    }
	    catch( Throwable exc ) {
	    	LOGGER.log( Level.WARNING, "TransmissionController:update() - successfully update Transmission - " + exc.getMessage());        	
	    }		
		
		return completableFuture;
	}
 
    /**
     * Handles deleting a Transmission entity
     * @param		command ${class.getDeleteCommandAlias()}
     * @return		CompletableFuture<Void>
     */
    @DeleteMapping("/delete")    
    public CompletableFuture<Void> delete( @RequestBody(required=true) CloseTransmissionCommand command ) {                
    	CompletableFuture<Void> completableFuture = null;
    	try {
        	TransmissionBusinessDelegate delegate = TransmissionBusinessDelegate.getTransmissionInstance();

        	completableFuture = delegate.delete( command );
    		LOGGER.log( Level.WARNING, "Successfully deleted Transmission with key " + command.getTransmissionId() );
        }
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING, exc.getMessage() );
        }
        
        return completableFuture;
	}        
	



//************************************************************************    
// Attributes
//************************************************************************
    protected Transmission transmission = null;
    private static final Logger LOGGER = Logger.getLogger(TransmissionCommandRestController.class.getName());
    
}
