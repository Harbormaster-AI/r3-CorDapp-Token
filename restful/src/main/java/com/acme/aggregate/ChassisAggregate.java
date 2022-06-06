package com.acme.aggregate;

import com.acme.api.*;
import com.acme.entity.*;
import com.acme.exception.*;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.modelling.command.AggregateMember;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import static org.axonframework.modelling.command.AggregateLifecycle.apply;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.context.annotation.Profile;

/**
 * Aggregate handler for Chassis as outlined for the CQRS pattern, all write responsibilities 
 * related to Chassis are handled here.
 * 
 * @author Dev Team
 * 
 */
@Aggregate(snapshotTriggerDefinition = "chassisAggregateSnapshotTriggerDefinition")
public class ChassisAggregate {  

	// -----------------------------------------
	// Axon requires an empty constructor
    // -----------------------------------------
    public ChassisAggregate() {
    }

	// ----------------------------------------------
	// intrinsic command handlers
	// ----------------------------------------------
    @CommandHandler
    public ChassisAggregate(CreateChassisCommand command) throws Exception {
    	LOGGER.info( "Handling command CreateChassisCommand" );
    	CreateChassisEvent event = new CreateChassisEvent(command.getChassisId(), command.getName(), command.getSerialNum(), command.getPlant(), command.getType());
    	
        apply(event);
    }

    @CommandHandler
    public void handle(RefreshChassisCommand command) throws Exception {
    	LOGGER.info( "handling command RefreshChassisCommand" );
    	RefreshedChassisEvent event = new RefreshedChassisEvent(command.getChassisId(), command.getName(), command.getSerialNum(), command.getPlant(), command.getType());        
    	
        apply(event);
    }

    @CommandHandler
    public void handle(CloseChassisCommand command) throws Exception {
    	LOGGER.info( "Handling command CloseChassisCommand" );
        apply(new ClosedChassisEvent(command.getChassisId()));
    }

	// ----------------------------------------------
	// association command handlers
	// ----------------------------------------------

    // single association commands

    // multiple association commands

	// ----------------------------------------------
	// intrinsic event source handlers
	// ----------------------------------------------
    @EventSourcingHandler
    void on(CreateChassisEvent event) {	
    	LOGGER.info( "Event sourcing CreateChassisEvent" );
    	this.chassisId = event.getChassisId();
        this.name = event.getName();
        this.serialNum = event.getSerialNum();
        this.plant = event.getPlant();
        this.type = event.getType();
    }
    
    @EventSourcingHandler
    void on(RefreshedChassisEvent event) {
    	LOGGER.info( "Event sourcing classObject.getUpdateEventAlias()}" );
        this.name = event.getName();
        this.serialNum = event.getSerialNum();
        this.plant = event.getPlant();
        this.type = event.getType();
    }   

	// ----------------------------------------------
	// association event source handlers
	// ----------------------------------------------


    // ------------------------------------------
    // attributes
    // ------------------------------------------
	
    @AggregateIdentifier
    private UUID chassisId;
    
    private String name;
    private String serialNum;
    private Plant plant;
    private ChassisType type;

    private static final Logger LOGGER 	= Logger.getLogger(ChassisAggregate.class.getName());
}
