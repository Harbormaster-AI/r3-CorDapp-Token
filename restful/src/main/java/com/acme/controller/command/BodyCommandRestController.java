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
 * Implements Spring Controller command CQRS processing for entity Body.
 *
 * @author Dev Team
 */
@CrossOrigin
@RestController
@RequestMapping("/Body")
public class BodyCommandRestController extends BaseSpringRestController {

    /**
     * Handles create a Body.  if not key provided, calls create, otherwise calls save
     * @param		Body	body
     * @return		CompletableFuture<UUID> 
     */
	@PostMapping("/create")
    public CompletableFuture<UUID> create( @RequestBody(required=true) CreateBodyCommand command ) {
		CompletableFuture<UUID> completableFuture = null;
		try {       
        	
			completableFuture = BodyBusinessDelegate.getBodyInstance().createBody( command );
        }
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING, exc.getMessage(), exc );        	
        }
		
		return completableFuture;
    }

    /**
     * Handles updating a Body.  if no key provided, calls create, otherwise calls save
     * @param		Body body
     * @return		CompletableFuture<Void>
     */
	@PutMapping("/update")
    public CompletableFuture<Void> update( @RequestBody(required=true) RefreshBodyCommand command ) {
		CompletableFuture<Void> completableFuture = null;
		try {                        	        
			// -----------------------------------------------
			// delegate the RefreshBodyCommand
			// -----------------------------------------------
			completableFuture = BodyBusinessDelegate.getBodyInstance().updateBody(command);;
	    }
	    catch( Throwable exc ) {
	    	LOGGER.log( Level.WARNING, "BodyController:update() - successfully update Body - " + exc.getMessage());        	
	    }		
		
		return completableFuture;
	}
 
    /**
     * Handles deleting a Body entity
     * @param		command ${class.getDeleteCommandAlias()}
     * @return		CompletableFuture<Void>
     */
    @DeleteMapping("/delete")    
    public CompletableFuture<Void> delete( @RequestBody(required=true) CloseBodyCommand command ) {                
    	CompletableFuture<Void> completableFuture = null;
    	try {
        	BodyBusinessDelegate delegate = BodyBusinessDelegate.getBodyInstance();

        	completableFuture = delegate.delete( command );
    		LOGGER.log( Level.WARNING, "Successfully deleted Body with key " + command.getBodyId() );
        }
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING, exc.getMessage() );
        }
        
        return completableFuture;
	}        
	



//************************************************************************    
// Attributes
//************************************************************************
    protected Body body = null;
    private static final Logger LOGGER = Logger.getLogger(BodyCommandRestController.class.getName());
    
}
