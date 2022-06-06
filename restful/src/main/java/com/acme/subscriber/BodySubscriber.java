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
 * Subscriber for Body related events.  .
 * 
 * @author Dev Team
 *
 */
@Component("body-subscriber")
public class BodySubscriber extends BaseSubscriber {

	public BodySubscriber() {
		queryGateway = applicationContext.getBean(QueryGateway.class);
	}
	
    public SubscriptionQueryResult<List<Body>, Body> bodySubscribe() {
        return queryGateway
                .subscriptionQuery(new FindAllBodyQuery(), 
                		ResponseTypes.multipleInstancesOf(Body.class),
                		ResponseTypes.instanceOf(Body.class));
    }

    public SubscriptionQueryResult<Body, Body> bodySubscribe(@DestinationVariable UUID bodyId) {
        return queryGateway
                .subscriptionQuery(new FindBodyQuery(new LoadBodyFilter(bodyId)), 
                		ResponseTypes.instanceOf(Body.class),
                		ResponseTypes.instanceOf(Body.class));
    }




    // -------------------------------------------------
    // attributes
    // -------------------------------------------------
	@Autowired
    private final QueryGateway queryGateway;
}

