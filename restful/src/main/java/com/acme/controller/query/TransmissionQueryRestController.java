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
 * Implements Spring Controller query CQRS processing for entity Transmission.
 *
 * @author Dev Team
 */
@CrossOrigin
@RestController
@RequestMapping("/Transmission")
public class TransmissionQueryRestController extends BaseSpringRestController {

	
    /**
     * Handles loading a Transmission using a UUID
     * @param		UUID uuid 
     * @return		Transmission
     */    
    @GetMapping("/load")
    public Transmission load( @RequestParam(required=true) UUID uuid ) {    	
    	Transmission entity = null;

    	try {  
    		entity = TransmissionBusinessDelegate.getTransmissionInstance().getTransmission( new TransmissionFetchOneSummary( uuid ) );   
        }
        catch( Throwable exc ) {
            LOGGER.log( Level.WARNING, "failed to load Transmission using Id " + uuid );
            return null;
        }

        return entity;
    }

    /**
     * Handles loading all Transmission business objects
     * @return		Set<Transmission>
     */
    @GetMapping("/")
    public List<Transmission> loadAll() {                
    	List<Transmission> transmissionList = null;
        
    	try {
            // load the Transmission
            transmissionList = TransmissionBusinessDelegate.getTransmissionInstance().getAllTransmission();
            
            if ( transmissionList != null )
                LOGGER.log( Level.INFO,  "successfully loaded all Transmissions" );
        }
        catch( Throwable exc ) {
            LOGGER.log( Level.WARNING,  "failed to load all Transmissions ", exc );
        	return null;
        }

        return transmissionList;
                            
    }



//************************************************************************    
// Attributes
//************************************************************************
    protected Transmission transmission = null;
    private static final Logger LOGGER = Logger.getLogger(TransmissionQueryRestController.class.getName());
    
}
