/*******************************************************************************
  Oldsmobile Motor Corporation Confidential
  
  2018 Oldsmobile Motor Corporation
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Oldsmobile Motor Corporation - General Release
 ******************************************************************************/
package com.acme.test;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.util.Assert.state;

import com.acme.delegate.*;
import com.acme.entity.*;
import com.acme.api.*;
import com.acme.subscriber.*;

/**
 * Test Transmission class.
 *
 * @author Dev Team
 */
public class TransmissionTest{

    // ------------------------------------
	// default constructor
    // ------------------------------------
	public TransmissionTest() {
		subscriber = new TransmissionSubscriber();
	}

	// test methods
	@Test
	/*
	 * Initiate TransmissionTest.
	 */
	public void startTest() throws Throwable {
		try {
			LOGGER.info("**********************************************************");
			LOGGER.info("Beginning test on Transmission...");
			LOGGER.info("**********************************************************\n");
			
			// ---------------------------------------------
			// jumpstart process
			// ---------------------------------------------
			jumpStart();
			
		} catch (Throwable e) {
			throw e;
		} finally {
		}
	}

	/** 
	 * jumpstart the process by instantiating2 Transmission
	 */
	protected void jumpStart() throws Throwable {
		LOGGER.info( "\n======== create instances to get the ball rolling  ======== ");

		transmissionId = TransmissionBusinessDelegate.getTransmissionInstance()
		.createTransmission( generateNewCommand() )
		.get();

		// ---------------------------------------------
		// set up query subscriptions after the 1st create
		// ---------------------------------------------
		testingStep = "create";
		setUpQuerySubscriptions();

		TransmissionBusinessDelegate.getTransmissionInstance()
				.createTransmission( generateNewCommand() )
				.get();

	}

	/** 
	 * Set up query subscriptions
	 */
	protected void setUpQuerySubscriptions() throws Throwable {
		LOGGER.info( "\n======== Setting Up Query Subscriptions ======== ");
			
		try {            
			subscriber.transmissionSubscribe().updates().subscribe(
					  successValue -> {
						  LOGGER.info(successValue.toString());
						  try {
							  LOGGER.info("GetAll update received for Transmission : " + successValue.getTransmissionId());
							  if (successValue.getTransmissionId().equals(transmissionId)) {
								  if (testingStep.equals("create")) {
									  testingStep = "update";
									  update();
								  } else if (testingStep.equals("delete")) {
									  testingStep = "complete";
									  state( getAll().size() == sizeOfTransmissionList - 1 , "value not deleted from list");
									  LOGGER.info("**********************************************************");
									  LOGGER.info("Transmission test completed successfully...");
									  LOGGER.info("**********************************************************\n");
								  }
							  }
						  } catch( Throwable exc ) {
							  LOGGER.warning( exc.getMessage() );
						  }
					  },
					  error -> LOGGER.warning(error.getMessage()),
					  () -> LOGGER.info("Subscription on transmission consumed")
					);
			subscriber.transmissionSubscribe( transmissionId ).updates().subscribe(
					  successValue -> {
						  LOGGER.info(successValue.toString());
						  try {
							  LOGGER.info("GetOne update received for Transmission : " + successValue.getTransmissionId() + " in step " + testingStep);
							  testingStep = "delete";
							  sizeOfTransmissionList = getAll().size();
							  delete();
						  } catch( Throwable exc ) {
							  LOGGER.warning( exc.getMessage() );
						  }
					  },
					  error -> LOGGER.warning(error.getMessage()),
					  () -> LOGGER.info("Subscription on transmission for transmissionId consumed")

					);
			

			}
			catch (Exception e) {
				LOGGER.warning( e.getMessage() );
				throw e;
			}
		}
		
		/** 
	 * read a Transmission. 
	 */
	protected Transmission read() throws Throwable {
		LOGGER.info( "\n======== READ ======== ");
		LOGGER.info( "-- Reading a previously created Transmission" );

		Transmission entity = null;
		StringBuilder msg = new StringBuilder( "-- Failed to read Transmission with primary key" );
		msg.append( transmissionId );
		
		TransmissionFetchOneSummary fetchOneSummary = new TransmissionFetchOneSummary( transmissionId );

		try {
			entity = TransmissionBusinessDelegate.getTransmissionInstance().getTransmission( fetchOneSummary );

			assertNotNull( entity,msg.toString() );

			LOGGER.info( "-- Successfully found Transmission " + entity.toString() );
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( msg.toString() + " : " + e );

			throw e;
		}
		
		return entity;
	}

	/** 
	 * updating a Transmission.
	 */
	protected void update() throws Throwable {
		LOGGER.info( "\n======== UPDATE ======== ");
		LOGGER.info( "-- Attempting to update a Transmission." );

		StringBuilder msg = new StringBuilder( "Failed to update a Transmission : " );        
		Transmission entity = read();
		RefreshTransmissionCommand command = generateUpdateCommand();
		command.setTransmissionId(entity.getTransmissionId());

		try {            
			assertNotNull( entity, msg.toString() );

			LOGGER.info( "-- Now updating the created Transmission." );

			// for use later on...
			transmissionId = entity.getTransmissionId();

			TransmissionBusinessDelegate proxy = TransmissionBusinessDelegate.getTransmissionInstance();  

			proxy.updateTransmission( command ).get();

			LOGGER.info( "-- Successfully saved Transmission - " + entity.toString() );
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( msg.toString() + " : primarykey = " + transmissionId + " : command -" +  command + " : " + e );

			throw e;
		}
	}

	/** 
	 * delete a Transmission.
	 */
	protected void delete() throws Throwable {
		LOGGER.info( "\n======== DELETE ======== ");
		LOGGER.info( "-- Deleting a previously created Transmission." );

		Transmission entity = null;
		
		try{
		    entity = read(); 
			LOGGER.info( "-- Successfully read Transmission with id " + transmissionId );            
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( "-- Failed to read Transmission with id " + transmissionId );

			throw e;
		}

		try{
			TransmissionBusinessDelegate.getTransmissionInstance().delete( new CloseTransmissionCommand( entity.getTransmissionId() ) ).get();
			LOGGER.info( "-- Successfully deleted Transmission with id " + transmissionId );            
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( "-- Failed to delete Transmission with id " + transmissionId );

			throw e;
		}
	}

	/**
	 * get all Transmissions.
	 */
	protected List<Transmission> getAll() throws Throwable {    
		LOGGER.info( "======== GETALL ======== ");
		LOGGER.info( "-- Retrieving Collection of Transmissions:" );

		StringBuilder msg = new StringBuilder( "-- Failed to get all Transmission : " );        
		List<Transmission> collection  = new ArrayList<>();

		try {
			// call the static get method on the TransmissionBusinessDelegate
			collection = TransmissionBusinessDelegate.getTransmissionInstance().getAllTransmission();
			assertNotNull( collection, "An Empty collection of Transmission was incorrectly returned.");
			
			// Now print out the values
			Transmission entity = null;            
			Iterator<Transmission> iter = collection.iterator();
			int index = 1;

			while( iter.hasNext() ) {
				// Retrieve the entity   
				entity = iter.next();

				assertNotNull( entity,"-- null entity in Collection." );
				assertNotNull( entity.getTransmissionId(), "-- entity in Collection has a null primary key" );        

				LOGGER.info( " - " + String.valueOf(index) + ". " + entity.toString() );
				index++;
			}
		}
		catch ( Throwable e ) {
			LOGGER.warning( unexpectedErrorMsg );
			LOGGER.warning( msg.toString() + " : " + e );

			throw e;
		}
		
		return collection;			
	}

	/**
	 * Assigns a common log handler for each test class in the suite 
	 * in the event log output needs to go elsewhere
	 * 
	 * @param		handler	Handler
	 * @return		TransmissionTest
	 */
	protected TransmissionTest setHandler(Handler handler) {
		if ( handler != null )
			LOGGER.addHandler(handler); 
		return this;
	}

	/**
	 * Returns a new populated Transmission
	 * 
	 * @return CreateTransmissionCommand alias
	 */
	protected CreateTransmissionCommand generateNewCommand() {
        CreateTransmissionCommand command = new CreateTransmissionCommand( null,  org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(16),  org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(16),  new Plant(),  TransmissionType.values()[0] );
		
		return( command );
	}

		/**
		 * Returns a new populated Transmission
		 * 
		 * @return RefreshTransmissionCommand alias
		 */
	protected RefreshTransmissionCommand generateUpdateCommand() {
	        RefreshTransmissionCommand command = new RefreshTransmissionCommand( null,  org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(16),  org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(16),  new Plant(),  TransmissionType.values()[0] );
			
			return( command );
		}
	//-----------------------------------------------------
	// attributes 
	//-----------------------------------------------------
	protected UUID transmissionId = null;
	protected TransmissionSubscriber subscriber = null;
	private final String unexpectedErrorMsg = ":::::::::::::: Unexpected Error :::::::::::::::::";
	private final Logger LOGGER = Logger.getLogger(TransmissionTest.class.getName());
	private String testingStep = "";
	private Integer sizeOfTransmissionList = 0;
}
