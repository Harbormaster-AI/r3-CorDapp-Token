/*******************************************************************************
  Oldsmobile Motor Corporation Confidential
  
  2018 Oldsmobile Motor Corporation
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Oldsmobile Motor Corporation - General Release
 ******************************************************************************/
package com.acme.controller.query;

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
 * Implements Spring Controller query CQRS processing for entity Braking.
 *
 * @author Dev Team
 */
@CrossOrigin
@RestController
@RequestMapping("/Braking")
public class BrakingQueryRestController extends BaseSpringRestController {

	
    /**
     * Handles loading a Braking using a UUID
     * @param		UUID uuid 
     * @return		Braking
     */    
    @GetMapping("/load")
    public Braking load( @RequestParam(required=true) UUID uuid ) {    	
    	Braking entity = null;

    	try {  
    		entity = BrakingBusinessDelegate.getBrakingInstance().getBraking( new BrakingFetchOneSummary( uuid ) );   
        }
        catch( Throwable exc ) {
            LOGGER.log( Level.WARNING, "failed to load Braking using Id " + uuid );
            return null;
        }

        return entity;
    }

    /**
     * Handles loading all Braking business objects
     * @return		Set<Braking>
     */
    @GetMapping("/")
    public List<Braking> loadAll() {                
    	List<Braking> brakingList = null;
        
    	try {
            // load the Braking
            brakingList = BrakingBusinessDelegate.getBrakingInstance().getAllBraking();
            
            if ( brakingList != null )
                LOGGER.log( Level.INFO,  "successfully loaded all Brakings" );
        }
        catch( Throwable exc ) {
            LOGGER.log( Level.WARNING,  "failed to load all Brakings ", exc );
        	return null;
        }

        return brakingList;
                            
    }



//************************************************************************    
// Attributes
//************************************************************************
    protected Braking braking = null;
    private static final Logger LOGGER = Logger.getLogger(BrakingQueryRestController.class.getName());
    
}
