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
 * Implements Spring Controller query CQRS processing for entity Body.
 *
 * @author Dev Team
 */
@CrossOrigin
@RestController
@RequestMapping("/Body")
public class BodyQueryRestController extends BaseSpringRestController {

	
    /**
     * Handles loading a Body using a UUID
     * @param		UUID uuid 
     * @return		Body
     */    
    @GetMapping("/load")
    public Body load( @RequestParam(required=true) UUID uuid ) {    	
    	Body entity = null;

    	try {  
    		entity = BodyBusinessDelegate.getBodyInstance().getBody( new BodyFetchOneSummary( uuid ) );   
        }
        catch( Throwable exc ) {
            LOGGER.log( Level.WARNING, "failed to load Body using Id " + uuid );
            return null;
        }

        return entity;
    }

    /**
     * Handles loading all Body business objects
     * @return		Set<Body>
     */
    @GetMapping("/")
    public List<Body> loadAll() {                
    	List<Body> bodyList = null;
        
    	try {
            // load the Body
            bodyList = BodyBusinessDelegate.getBodyInstance().getAllBody();
            
            if ( bodyList != null )
                LOGGER.log( Level.INFO,  "successfully loaded all Bodys" );
        }
        catch( Throwable exc ) {
            LOGGER.log( Level.WARNING,  "failed to load all Bodys ", exc );
        	return null;
        }

        return bodyList;
                            
    }



//************************************************************************    
// Attributes
//************************************************************************
    protected Body body = null;
    private static final Logger LOGGER = Logger.getLogger(BodyQueryRestController.class.getName());
    
}
