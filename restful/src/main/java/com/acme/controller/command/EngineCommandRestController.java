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
 * Implements Spring Controller command CQRS processing for entity Engine.
 *
 * @author Dev Team
 */
@CrossOrigin
@RestController
@RequestMapping("/Engine")
public class EngineCommandRestController extends BaseSpringRestController {

    /**
     * Handles create a Engine.  if not key provided, calls create, otherwise calls save
     * @param		Engine	engine
     * @return		CompletableFuture<UUID> 
     */
	@PostMapping("/create")
    public CompletableFuture<UUID> create( @RequestBody(required=true) CreateEngineCommand command ) {
		CompletableFuture<UUID> completableFuture = null;
		try {       
        	
			completableFuture = EngineBusinessDelegate.getEngineInstance().createEngine( command );
        }
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING, exc.getMessage(), exc );        	
        }
		
		return completableFuture;
    }

    /**
     * Handles updating a Engine.  if no key provided, calls create, otherwise calls save
     * @param		Engine engine
     * @return		CompletableFuture<Void>
     */
	@PutMapping("/update")
    public CompletableFuture<Void> update( @RequestBody(required=true) RefreshEngineCommand command ) {
		CompletableFuture<Void> completableFuture = null;
		try {                        	        
			// -----------------------------------------------
			// delegate the RefreshEngineCommand
			// -----------------------------------------------
			completableFuture = EngineBusinessDelegate.getEngineInstance().updateEngine(command);;
	    }
	    catch( Throwable exc ) {
	    	LOGGER.log( Level.WARNING, "EngineController:update() - successfully update Engine - " + exc.getMessage());        	
	    }		
		
		return completableFuture;
	}
 
    /**
     * Handles deleting a Engine entity
     * @param		command ${class.getDeleteCommandAlias()}
     * @return		CompletableFuture<Void>
     */
    @DeleteMapping("/delete")    
    public CompletableFuture<Void> delete( @RequestBody(required=true) CloseEngineCommand command ) {                
    	CompletableFuture<Void> completableFuture = null;
    	try {
        	EngineBusinessDelegate delegate = EngineBusinessDelegate.getEngineInstance();

        	completableFuture = delegate.delete( command );
    		LOGGER.log( Level.WARNING, "Successfully deleted Engine with key " + command.getEngineId() );
        }
        catch( Throwable exc ) {
        	LOGGER.log( Level.WARNING, exc.getMessage() );
        }
        
        return completableFuture;
	}        
	



//************************************************************************    
// Attributes
//************************************************************************
    protected Engine engine = null;
    private static final Logger LOGGER = Logger.getLogger(EngineCommandRestController.class.getName());
    
}
