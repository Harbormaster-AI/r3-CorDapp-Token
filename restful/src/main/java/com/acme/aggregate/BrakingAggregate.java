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
 * Aggregate handler for Braking as outlined for the CQRS pattern, all write responsibilities 
 * related to Braking are handled here.
 * 
 * @author Dev Team
 * 
 */
@Aggregate(snapshotTriggerDefinition = "brakingAggregateSnapshotTriggerDefinition")
public class BrakingAggregate {  

	// -----------------------------------------
	// Axon requires an empty constructor
    // -----------------------------------------
    public BrakingAggregate() {
    }

	// ----------------------------------------------
	// intrinsic command handlers
	// ----------------------------------------------
    @CommandHandler
    public BrakingAggregate(CreateBrakingCommand command) throws Exception {
    	LOGGER.info( "Handling command CreateBrakingCommand" );
    	CreateBrakingEvent event = new CreateBrakingEvent(command.getBrakingId(), command.getSerialNum(), command.getName(), command.getPlant(), command.getType());
    	
        apply(event);
    }

    @CommandHandler
    public void handle(RefreshBrakingCommand command) throws Exception {
    	LOGGER.info( "handling command RefreshBrakingCommand" );
    	RefreshedBrakingEvent event = new RefreshedBrakingEvent(command.getBrakingId(), command.getSerialNum(), command.getName(), command.getPlant(), command.getType());        
    	
        apply(event);
    }

    @CommandHandler
    public void handle(CloseBrakingCommand command) throws Exception {
    	LOGGER.info( "Handling command CloseBrakingCommand" );
        apply(new ClosedBrakingEvent(command.getBrakingId()));
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
    void on(CreateBrakingEvent event) {	
    	LOGGER.info( "Event sourcing CreateBrakingEvent" );
    	this.brakingId = event.getBrakingId();
        this.serialNum = event.getSerialNum();
        this.name = event.getName();
        this.plant = event.getPlant();
        this.type = event.getType();
    }
    
    @EventSourcingHandler
    void on(RefreshedBrakingEvent event) {
    	LOGGER.info( "Event sourcing classObject.getUpdateEventAlias()}" );
        this.serialNum = event.getSerialNum();
        this.name = event.getName();
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
    private UUID brakingId;
    
    private String serialNum;
    private String name;
    private Plant plant;
    private BrakingType type;

    private static final Logger LOGGER 	= Logger.getLogger(BrakingAggregate.class.getName());
}
