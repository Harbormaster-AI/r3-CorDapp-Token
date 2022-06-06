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
 * Aggregate handler for Transmission as outlined for the CQRS pattern, all write responsibilities 
 * related to Transmission are handled here.
 * 
 * @author Dev Team
 * 
 */
@Aggregate(snapshotTriggerDefinition = "transmissionAggregateSnapshotTriggerDefinition")
public class TransmissionAggregate {  

	// -----------------------------------------
	// Axon requires an empty constructor
    // -----------------------------------------
    public TransmissionAggregate() {
    }

	// ----------------------------------------------
	// intrinsic command handlers
	// ----------------------------------------------
    @CommandHandler
    public TransmissionAggregate(CreateTransmissionCommand command) throws Exception {
    	LOGGER.info( "Handling command CreateTransmissionCommand" );
    	CreateTransmissionEvent event = new CreateTransmissionEvent(command.getTransmissionId(), command.getName(), command.getSerialNum(), command.getPlant(), command.getType());
    	
        apply(event);
    }

    @CommandHandler
    public void handle(RefreshTransmissionCommand command) throws Exception {
    	LOGGER.info( "handling command RefreshTransmissionCommand" );
    	RefreshedTransmissionEvent event = new RefreshedTransmissionEvent(command.getTransmissionId(), command.getName(), command.getSerialNum(), command.getPlant(), command.getType());        
    	
        apply(event);
    }

    @CommandHandler
    public void handle(CloseTransmissionCommand command) throws Exception {
    	LOGGER.info( "Handling command CloseTransmissionCommand" );
        apply(new ClosedTransmissionEvent(command.getTransmissionId()));
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
    void on(CreateTransmissionEvent event) {	
    	LOGGER.info( "Event sourcing CreateTransmissionEvent" );
    	this.transmissionId = event.getTransmissionId();
        this.name = event.getName();
        this.serialNum = event.getSerialNum();
        this.plant = event.getPlant();
        this.type = event.getType();
    }
    
    @EventSourcingHandler
    void on(RefreshedTransmissionEvent event) {
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
    private UUID transmissionId;
    
    private String name;
    private String serialNum;
    private Plant plant;
    private TransmissionType type;

    private static final Logger LOGGER 	= Logger.getLogger(TransmissionAggregate.class.getName());
}
