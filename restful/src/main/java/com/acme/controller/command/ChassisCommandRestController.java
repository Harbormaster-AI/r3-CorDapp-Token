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
 * Implements Spring Controller command CQRS processing for entity Chassis.
 *
 * @author Dev Team
 */
@CrossOrigin
@RestController
@RequestMapping("/Chassis")
public class ChassisCommandRestController extends BaseSpringRestController {

    /**
     * Handles create a Chassis.  if not key provided, calls create, otherwise calls save
     * @param		Chassis	chassis
     * @return		CompletableFuture<UUID> 
     */
	@PostMapping("/create")
    public CompletableFuture<UUID> create( @RequestBody(required=true) CreateChassisCommand command ) {
		CompletableFuture<UUID> completableFuture = null;
		try {       
        	
			completableFuture = ChassisBusinessDelegate.getChassisInstance().createChassis( command );
        }
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING, exc.getMessage(), exc );        	
        }
		
		return completableFuture;
    }

    /**
     * Handles updating a Chassis.  if no key provided, calls create, otherwise calls save
     * @param		Chassis chassis
     * @return		CompletableFuture<Void>
     */
	@PutMapping("/update")
    public CompletableFuture<Void> update( @RequestBody(required=true) RefreshChassisCommand command ) {
		CompletableFuture<Void> completableFuture = null;
		try {                        	        
			// -----------------------------------------------
			// delegate the RefreshChassisCommand
			// -----------------------------------------------
			completableFuture = ChassisBusinessDelegate.getChassisInstance().updateChassis(command);;
	    }
	    catch( Throwable exc ) {
	    	LOGGER.log( Level.WARNING, "ChassisController:update() - successfully update Chassis - " + exc.getMessage());        	
	    }		
		
		return completableFuture;
	}
 
    /**
     * Handles deleting a Chassis entity
     * @param		command ${class.getDeleteCommandAlias()}
     * @return		CompletableFuture<Void>
     */
    @DeleteMapping("/delete")    
    public CompletableFuture<Void> delete( @RequestBody(required=true) CloseChassisCommand command ) {                
    	CompletableFuture<Void> completableFuture = null;
    	try {
        	ChassisBusinessDelegate delegate = ChassisBusinessDelegate.getChassisInstance();

        	completableFuture = delegate.delete( command );
    		LOGGER.log( Level.WARNING, "Successfully deleted Chassis with key " + command.getChassisId() );
        }
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING, exc.getMessage() );
        }
        
        return completableFuture;
	}        
	



//************************************************************************    
// Attributes
//************************************************************************
    protected Chassis chassis = null;
    private static final Logger LOGGER = Logger.getLogger(ChassisCommandRestController.class.getName());
    
}
