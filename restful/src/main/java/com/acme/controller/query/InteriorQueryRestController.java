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
 * Implements Spring Controller query CQRS processing for entity Interior.
 *
 * @author Dev Team
 */
@CrossOrigin
@RestController
@RequestMapping("/Interior")
public class InteriorQueryRestController extends BaseSpringRestController {

	
    /**
     * Handles loading a Interior using a UUID
     * @param		UUID uuid 
     * @return		Interior
     */    
    @GetMapping("/load")
    public Interior load( @RequestParam(required=true) UUID uuid ) {    	
    	Interior entity = null;

    	try {  
    		entity = InteriorBusinessDelegate.getInteriorInstance().getInterior( new InteriorFetchOneSummary( uuid ) );   
        }
        catch( Throwable exc ) {
            LOGGER.log( Level.WARNING, "failed to load Interior using Id " + uuid );
            return null;
        }

        return entity;
    }

    /**
     * Handles loading all Interior business objects
     * @return		Set<Interior>
     */
    @GetMapping("/")
    public List<Interior> loadAll() {                
    	List<Interior> interiorList = null;
        
    	try {
            // load the Interior
            interiorList = InteriorBusinessDelegate.getInteriorInstance().getAllInterior();
            
            if ( interiorList != null )
                LOGGER.log( Level.INFO,  "successfully loaded all Interiors" );
        }
        catch( Throwable exc ) {
            LOGGER.log( Level.WARNING,  "failed to load all Interiors ", exc );
        	return null;
        }

        return interiorList;
                            
    }



//************************************************************************    
// Attributes
//************************************************************************
    protected Interior interior = null;
    private static final Logger LOGGER = Logger.getLogger(InteriorQueryRestController.class.getName());
    
}
