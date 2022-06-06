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
 * Aggregate handler for Interior as outlined for the CQRS pattern, all write responsibilities 
 * related to Interior are handled here.
 * 
 * @author Dev Team
 * 
 */
@Aggregate(snapshotTriggerDefinition = "interiorAggregateSnapshotTriggerDefinition")
public class InteriorAggregate {  

	// -----------------------------------------
	// Axon requires an empty constructor
    // -----------------------------------------
    public InteriorAggregate() {
    }

	// ----------------------------------------------
	// intrinsic command handlers
	// ----------------------------------------------
    @CommandHandler
    public InteriorAggregate(CreateInteriorCommand command) throws Exception {
    	LOGGER.info( "Handling command CreateInteriorCommand" );
    	CreateInteriorEvent event = new CreateInteriorEvent(command.getInteriorId(), command.getSerialNum(), command.getName(), command.getPlant());
    	
        apply(event);
    }

    @CommandHandler
    public void handle(RefreshInteriorCommand command) throws Exception {
    	LOGGER.info( "handling command RefreshInteriorCommand" );
    	RefreshedInteriorEvent event = new RefreshedInteriorEvent(command.getInteriorId(), command.getSerialNum(), command.getName(), command.getPlant());        
    	
        apply(event);
    }

    @CommandHandler
    public void handle(CloseInteriorCommand command) throws Exception {
    	LOGGER.info( "Handling command CloseInteriorCommand" );
        apply(new ClosedInteriorEvent(command.getInteriorId()));
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
    void on(CreateInteriorEvent event) {	
    	LOGGER.info( "Event sourcing CreateInteriorEvent" );
    	this.interiorId = event.getInteriorId();
        this.serialNum = event.getSerialNum();
        this.name = event.getName();
        this.plant = event.getPlant();
    }
    
    @EventSourcingHandler
    void on(RefreshedInteriorEvent event) {
    	LOGGER.info( "Event sourcing classObject.getUpdateEventAlias()}" );
        this.serialNum = event.getSerialNum();
        this.name = event.getName();
        this.plant = event.getPlant();
    }   

	// ----------------------------------------------
	// association event source handlers
	// ----------------------------------------------


    // ------------------------------------------
    // attributes
    // ------------------------------------------
	
    @AggregateIdentifier
    private UUID interiorId;
    
    private String serialNum;
    private String name;
    private Plant plant;

    private static final Logger LOGGER 	= Logger.getLogger(InteriorAggregate.class.getName());
}
