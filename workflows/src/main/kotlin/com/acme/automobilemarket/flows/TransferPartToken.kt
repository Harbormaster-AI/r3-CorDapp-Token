package com.acme.automobilemarket.flows

import co.paralleluniverse.fibers.Suspendable
import com.r3.corda.lib.tokens.contracts.types.TokenPointer
import com.r3.corda.lib.tokens.workflows.flows.rpc.MoveNonFungibleTokens
import com.r3.corda.lib.tokens.workflows.flows.rpc.MoveNonFungibleTokensHandler
import com.r3.corda.lib.tokens.workflows.internal.flows.distribution.UpdateDistributionListFlow
import com.r3.corda.lib.tokens.workflows.types.PartyAndToken
import net.corda.core.flows.*
import net.corda.core.identity.Party
import net.corda.core.node.services.queryBy
import net.corda.core.utilities.ProgressTracker

import java.util.UUID

import com.acme.automobilemarket.states.ChassisTokenState
import com.acme.automobilemarket.states.BodyTokenState
import com.acme.automobilemarket.states.EngineTokenState
import com.acme.automobilemarket.states.TransmissionTokenState
import com.acme.automobilemarket.states.BrakingTokenState
import com.acme.automobilemarket.states.InteriorTokenState

// *********
// * Flows *
// *********
@InitiatingFlow
@StartableByRPC
class TransferPartToken(val part: String,
                   val Id: UUID,
                   val holder: Party) : FlowLogic<String>() {
    override val progressTracker = ProgressTracker()

    @Suspendable
    override fun call():String {
        if (part.equals("chassis")){
            val chassisIdentifier = Id
            //transfer chassis token
            val chassisStateAndRef = serviceHub.vaultService.queryBy<ChassisTokenState>().states
                    .filter { it.state.data.linearId.id.equals(chassisIdentifier) }[0]

            //get the TokenType object
            val chassistokentype = chassisStateAndRef.state.data

            //get the pointer pointer to the chassis
            val chassistokenPointer: TokenPointer<*> = chassistokentype.toPointer(chassistokentype.javaClass)
            val partyAndChassisToken = PartyAndToken(holder,chassistokenPointer)

            val stx = subFlow(MoveNonFungibleTokens(partyAndChassisToken))
            /* Distribution list is a list of identities that should receive updates. For this mechanism to behave correctly we call the UpdateDistributionListFlow flow */
            subFlow(UpdateDistributionListFlow(stx))
            return ("Transfer ownership of the chassis (" + this.Id + ") to" + holder.name.organisation
                    + "\nTransaction ID: " + stx.id)
        }
        else if (part.equals("body")){
            val bodyIdentifier = Id
            //transfer body token
            val bodyStateAndRef = serviceHub.vaultService.queryBy<BodyTokenState>().states
                    .filter { it.state.data.linearId.id.equals(bodyIdentifier) }[0]

            //get the TokenType object
            val bodytokentype = bodyStateAndRef.state.data

            //get the pointer pointer to the body
            val bodytokenPointer: TokenPointer<*> = bodytokentype.toPointer(bodytokentype.javaClass)
            val partyAndBodyToken = PartyAndToken(holder,bodytokenPointer)

            val stx = subFlow(MoveNonFungibleTokens(partyAndBodyToken))
            /* Distribution list is a list of identities that should receive updates. For this mechanism to behave correctly we call the UpdateDistributionListFlow flow */
            subFlow(UpdateDistributionListFlow(stx))
            return ("Transfer ownership of the body (" + this.Id + ") to" + holder.name.organisation
                    + "\nTransaction ID: " + stx.id)
        }
        else if (part.equals("engine")){
            val engineIdentifier = Id
            //transfer engine token
            val engineStateAndRef = serviceHub.vaultService.queryBy<EngineTokenState>().states
                    .filter { it.state.data.linearId.id.equals(engineIdentifier) }[0]

            //get the TokenType object
            val enginetokentype = engineStateAndRef.state.data

            //get the pointer pointer to the engine
            val enginetokenPointer: TokenPointer<*> = enginetokentype.toPointer(enginetokentype.javaClass)
            val partyAndEngineToken = PartyAndToken(holder,enginetokenPointer)

            val stx = subFlow(MoveNonFungibleTokens(partyAndEngineToken))
            /* Distribution list is a list of identities that should receive updates. For this mechanism to behave correctly we call the UpdateDistributionListFlow flow */
            subFlow(UpdateDistributionListFlow(stx))
            return ("Transfer ownership of the engine (" + this.Id + ") to" + holder.name.organisation
                    + "\nTransaction ID: " + stx.id)
        }
        else if (part.equals("transmission")){
            val transmissionIdentifier = Id
            //transfer transmission token
            val transmissionStateAndRef = serviceHub.vaultService.queryBy<TransmissionTokenState>().states
                    .filter { it.state.data.linearId.id.equals(transmissionIdentifier) }[0]

            //get the TokenType object
            val transmissiontokentype = transmissionStateAndRef.state.data

            //get the pointer pointer to the transmission
            val transmissiontokenPointer: TokenPointer<*> = transmissiontokentype.toPointer(transmissiontokentype.javaClass)
            val partyAndTransmissionToken = PartyAndToken(holder,transmissiontokenPointer)

            val stx = subFlow(MoveNonFungibleTokens(partyAndTransmissionToken))
            /* Distribution list is a list of identities that should receive updates. For this mechanism to behave correctly we call the UpdateDistributionListFlow flow */
            subFlow(UpdateDistributionListFlow(stx))
            return ("Transfer ownership of the transmission (" + this.Id + ") to" + holder.name.organisation
                    + "\nTransaction ID: " + stx.id)
        }
        else if (part.equals("braking")){
            val brakingIdentifier = Id
            //transfer braking token
            val brakingStateAndRef = serviceHub.vaultService.queryBy<BrakingTokenState>().states
                    .filter { it.state.data.linearId.id.equals(brakingIdentifier) }[0]

            //get the TokenType object
            val brakingtokentype = brakingStateAndRef.state.data

            //get the pointer pointer to the braking
            val brakingtokenPointer: TokenPointer<*> = brakingtokentype.toPointer(brakingtokentype.javaClass)
            val partyAndBrakingToken = PartyAndToken(holder,brakingtokenPointer)

            val stx = subFlow(MoveNonFungibleTokens(partyAndBrakingToken))
            /* Distribution list is a list of identities that should receive updates. For this mechanism to behave correctly we call the UpdateDistributionListFlow flow */
            subFlow(UpdateDistributionListFlow(stx))
            return ("Transfer ownership of the braking (" + this.Id + ") to" + holder.name.organisation
                    + "\nTransaction ID: " + stx.id)
        }
        else if (part.equals("interior")){
            val interiorIdentifier = Id
            //transfer interior token
            val interiorStateAndRef = serviceHub.vaultService.queryBy<InteriorTokenState>().states
                    .filter { it.state.data.linearId.id.equals(interiorIdentifier) }[0]

            //get the TokenType object
            val interiortokentype = interiorStateAndRef.state.data

            //get the pointer pointer to the interior
            val interiortokenPointer: TokenPointer<*> = interiortokentype.toPointer(interiortokentype.javaClass)
            val partyAndInteriorToken = PartyAndToken(holder,interiortokenPointer)

            val stx = subFlow(MoveNonFungibleTokens(partyAndInteriorToken))
            /* Distribution list is a list of identities that should receive updates. For this mechanism to behave correctly we call the UpdateDistributionListFlow flow */
            subFlow(UpdateDistributionListFlow(stx))
            return ("Transfer ownership of the interior (" + this.Id + ") to" + holder.name.organisation
                    + "\nTransaction ID: " + stx.id)
    	} else {
            return "Please enter a part parameter for one of the following: [chassis, body, engine, transmission, braking, interior] "
        }
    }
}

@InitiatedBy(TransferPartToken::class)
class TransferPartTokenResponder(val counterpartySession: FlowSession) : FlowLogic<Unit>() {
    @Suspendable
    override fun call() {
        // Responder flow logic goes here.
        subFlow(MoveNonFungibleTokensHandler(counterpartySession));
    }
}
