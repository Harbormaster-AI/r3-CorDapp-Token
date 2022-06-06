/*******************************************************************************
  Oldsmobile Motor Corporation Confidential
  
  2018 Oldsmobile Motor Corporation
  All Rights Reserved.
  
  This file is subject to the terms and conditions defined in
  file 'license.txt', which is part of this source code package.
   
  Contributors :
        Oldsmobile Motor Corporation - General Release
 ******************************************************************************/
package com.acme.subscriber;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.axonframework.messaging.responsetypes.ResponseTypes;

import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.stereotype.Component;


import com.acme.api.*;
import com.acme.entity.*;
import com.acme.exception.*;

/**
 * Subscriber for Transmission related events.  .
 * 
 * @author Dev Team
 *
 */
@Component("transmission-subscriber")
public class TransmissionSubscriber extends BaseSubscriber {

	public TransmissionSubscriber() {
		queryGateway = applicationContext.getBean(QueryGateway.class);
	}
	
    public SubscriptionQueryResult<List<Transmission>, Transmission> transmissionSubscribe() {
        return queryGateway
                .subscriptionQuery(new FindAllTransmissionQuery(), 
                		ResponseTypes.multipleInstancesOf(Transmission.class),
                		ResponseTypes.instanceOf(Transmission.class));
    }

    public SubscriptionQueryResult<Transmission, Transmission> transmissionSubscribe(@DestinationVariable UUID transmissionId) {
        return queryGateway
                .subscriptionQuery(new FindTransmissionQuery(new LoadTransmissionFilter(transmissionId)), 
                		ResponseTypes.instanceOf(Transmission.class),
                		ResponseTypes.instanceOf(Transmission.class));
    }




    // -------------------------------------------------
    // attributes
    // -------------------------------------------------
	@Autowired
    private final QueryGateway queryGateway;
}

