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
 * Aggregate handler for Engine as outlined for the CQRS pattern, all write responsibilities 
 * related to Engine are handled here.
 * 
 * @author Dev Team
 * 
 */
@Aggregate(snapshotTriggerDefinition = "engineAggregateSnapshotTriggerDefinition")
public class EngineAggregate {  

	// -----------------------------------------
	// Axon requires an empty constructor
    // -----------------------------------------
    public EngineAggregate() {
    }

	// ----------------------------------------------
	// intrinsic command handlers
	// ----------------------------------------------
    @CommandHandler
    public EngineAggregate(CreateEngineCommand command) throws Exception {
    	LOGGER.info( "Handling command CreateEngineCommand" );
    	CreateEngineEvent event = new CreateEngineEvent(command.getEngineId(), command.getName(), command.getSerialNum(), command.getPlant(), command.getType());
    	
        apply(event);
    }

    @CommandHandler
    public void handle(RefreshEngineCommand command) throws Exception {
    	LOGGER.info( "handling command RefreshEngineCommand" );
    	RefreshedEngineEvent event = new RefreshedEngineEvent(command.getEngineId(), command.getName(), command.getSerialNum(), command.getPlant(), command.getType());        
    	
        apply(event);
    }

    @CommandHandler
    public void handle(CloseEngineCommand command) throws Exception {
    	LOGGER.info( "Handling command CloseEngineCommand" );
        apply(new ClosedEngineEvent(command.getEngineId()));
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
    void on(CreateEngineEvent event) {	
    	LOGGER.info( "Event sourcing CreateEngineEvent" );
    	this.engineId = event.getEngineId();
        this.name = event.getName();
        this.serialNum = event.getSerialNum();
        this.plant = event.getPlant();
        this.type = event.getType();
    }
    
    @EventSourcingHandler
    void on(RefreshedEngineEvent event) {
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
    private UUID engineId;
    
    private String name;
    private String serialNum;
    private Plant plant;
    private EngineType type;

    private static final Logger LOGGER 	= Logger.getLogger(EngineAggregate.class.getName());
}
