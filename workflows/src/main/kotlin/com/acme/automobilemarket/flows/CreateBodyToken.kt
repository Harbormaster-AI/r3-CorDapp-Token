package com.acme.automobilemarket.flows

import co.paralleluniverse.fibers.Suspendable
import com.r3.corda.lib.tokens.contracts.utilities.withNotary
import com.r3.corda.lib.tokens.workflows.flows.rpc.CreateEvolvableTokens
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.StartableByRPC
import net.corda.core.identity.CordaX500Name
import net.corda.core.utilities.ProgressTracker

import java.util.UUID

import com.acme.automobilemarket.states.*
import com.acme.api.*;

// *********
// * Flows *
// *********
@StartableByRPC
class CreateBodyToken(val bodyId: UUID, val name: String? = null, val plant: Plant? = null) : FlowLogic<String>() {
    override val progressTracker = ProgressTracker()

    @Suspendable
    override fun call():String {

        // Obtain a reference from a notary we wish to use.
        val notary = serviceHub.networkMapCache.getNotary(CordaX500Name.parse("O=Notary,L=London,C=GB"))

        //Create non-fungible body token
        val uuid = UniqueIdentifier( "Body", bodyId )
        val body = BodyTokenState(ourIdentity, uuid,
        									0,
        									name, plant
        									)

        //warp it with transaction state specifying the notary
        val transactionState = body withNotary notary!!

        subFlow(CreateEvolvableTokens(transactionState))

        return "\nCreated a body token for automobile body. (Id" + uuid + ")."
    }
}
