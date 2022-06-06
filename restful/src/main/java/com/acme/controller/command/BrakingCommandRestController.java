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
 * Implements Spring Controller command CQRS processing for entity Braking.
 *
 * @author Dev Team
 */
@CrossOrigin
@RestController
@RequestMapping("/Braking")
public class BrakingCommandRestController extends BaseSpringRestController {

    /**
     * Handles create a Braking.  if not key provided, calls create, otherwise calls save
     * @param		Braking	braking
     * @return		CompletableFuture<UUID> 
     */
	@PostMapping("/create")
    public CompletableFuture<UUID> create( @RequestBody(required=true) CreateBrakingCommand command ) {
		CompletableFuture<UUID> completableFuture = null;
		try {       
        	
			completableFuture = BrakingBusinessDelegate.getBrakingInstance().createBraking( command );
        }
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING, exc.getMessage(), exc );        	
        }
		
		return completableFuture;
    }

    /**
     * Handles updating a Braking.  if no key provided, calls create, otherwise calls save
     * @param		Braking braking
     * @return		CompletableFuture<Void>
     */
	@PutMapping("/update")
    public CompletableFuture<Void> update( @RequestBody(required=true) RefreshBrakingCommand command ) {
		CompletableFuture<Void> completableFuture = null;
		try {                        	        
			// -----------------------------------------------
			// delegate the RefreshBrakingCommand
			// -----------------------------------------------
			completableFuture = BrakingBusinessDelegate.getBrakingInstance().updateBraking(command);;
	    }
	    catch( Throwable exc ) {
	    	LOGGER.log( Level.WARNING, "BrakingController:update() - successfully update Braking - " + exc.getMessage());        	
	    }		
		
		return completableFuture;
	}
 
    /**
     * Handles deleting a Braking entity
     * @param		command ${class.getDeleteCommandAlias()}
     * @return		CompletableFuture<Void>
     */
    @DeleteMapping("/delete")    
    public CompletableFuture<Void> delete( @RequestBody(required=true) CloseBrakingCommand command ) {                
    	CompletableFuture<Void> completableFuture = null;
    	try {
        	BrakingBusinessDelegate delegate = BrakingBusinessDelegate.getBrakingInstance();

        	completableFuture = delegate.delete( command );
    		LOGGER.log( Level.WARNING, "Successfully deleted Braking with key " + command.getBrakingId() );
        }
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING, exc.getMessage() );
        }
        
        return completableFuture;
	}        
	



//************************************************************************    
// Attributes
//************************************************************************
    protected Braking braking = null;
    private static final Logger LOGGER = Logger.getLogger(BrakingCommandRestController.class.getName());
    
}
