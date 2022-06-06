package com.acme.automobilemarket.flows

import co.paralleluniverse.fibers.Suspendable
import com.r3.corda.lib.tokens.contracts.types.TokenPointer
import com.r3.corda.lib.tokens.workflows.flows.rpc.MoveNonFungibleTokensHandler
import com.r3.corda.lib.tokens.workflows.flows.rpc.RedeemNonFungibleTokens
import com.r3.corda.lib.tokens.workflows.flows.rpc.RedeemNonFungibleTokensHandler
import net.corda.core.flows.*
import net.corda.core.identity.Party
import net.corda.core.node.services.queryBy
import net.corda.core.utilities.ProgressTracker

import java.util.UUID

import  com.acme.automobilemarket.states.ChassisTokenState
import  com.acme.automobilemarket.states.BodyTokenState
import  com.acme.automobilemarket.states.EngineTokenState
import  com.acme.automobilemarket.states.TransmissionTokenState
import  com.acme.automobilemarket.states.BrakingTokenState
import  com.acme.automobilemarket.states.InteriorTokenState

// *********
// * Flows *
// *********
@InitiatingFlow
@StartableByRPC
class TotalPart(val part: String,
                   val Id: UUID) : FlowLogic<String>() {
    override val progressTracker = ProgressTracker()

    @Suspendable
    override fun call():String {
        if (part.equals("chassis")){
            val chassisId = Id
            //transfer chassis token
            val chassisStateAndRef = serviceHub.vaultService.queryBy<ChassisTokenState>().states
                    .filter { it.state.data.linearId.id.equals(chassisId) }[0]

            //get the TokenType object
            val chassisTokenType = chassisStateAndRef.state.data
            val issuer = chassisTokenType.maintainer

            //get the pointer pointer to the chassis
            val chassisTokenPointer: TokenPointer<*> = chassisTokenType.toPointer(chassisTokenType.javaClass)

            val stx = subFlow(RedeemNonFungibleTokens(chassisTokenPointer,issuer))
            return "\nThe chassis part is totaled, and the token is redeem to Oldsmobile Motor Corporation" + "\nTransaction ID: " + stx.id
        }
        else if (part.equals("body")){
            val bodyId = Id
            //transfer body token
            val bodyStateAndRef = serviceHub.vaultService.queryBy<BodyTokenState>().states
                    .filter { it.state.data.linearId.id.equals(bodyId) }[0]

            //get the TokenType object
            val bodyTokenType = bodyStateAndRef.state.data
            val issuer = bodyTokenType.maintainer

            //get the pointer pointer to the body
            val bodyTokenPointer: TokenPointer<*> = bodyTokenType.toPointer(bodyTokenType.javaClass)

            val stx = subFlow(RedeemNonFungibleTokens(bodyTokenPointer,issuer))
            return "\nThe body part is totaled, and the token is redeem to Oldsmobile Motor Corporation" + "\nTransaction ID: " + stx.id
        }
        else if (part.equals("engine")){
            val engineId = Id
            //transfer engine token
            val engineStateAndRef = serviceHub.vaultService.queryBy<EngineTokenState>().states
                    .filter { it.state.data.linearId.id.equals(engineId) }[0]

            //get the TokenType object
            val engineTokenType = engineStateAndRef.state.data
            val issuer = engineTokenType.maintainer

            //get the pointer pointer to the engine
            val engineTokenPointer: TokenPointer<*> = engineTokenType.toPointer(engineTokenType.javaClass)

            val stx = subFlow(RedeemNonFungibleTokens(engineTokenPointer,issuer))
            return "\nThe engine part is totaled, and the token is redeem to Oldsmobile Motor Corporation" + "\nTransaction ID: " + stx.id
        }
        else if (part.equals("transmission")){
            val transmissionId = Id
            //transfer transmission token
            val transmissionStateAndRef = serviceHub.vaultService.queryBy<TransmissionTokenState>().states
                    .filter { it.state.data.linearId.id.equals(transmissionId) }[0]

            //get the TokenType object
            val transmissionTokenType = transmissionStateAndRef.state.data
            val issuer = transmissionTokenType.maintainer

            //get the pointer pointer to the transmission
            val transmissionTokenPointer: TokenPointer<*> = transmissionTokenType.toPointer(transmissionTokenType.javaClass)

            val stx = subFlow(RedeemNonFungibleTokens(transmissionTokenPointer,issuer))
            return "\nThe transmission part is totaled, and the token is redeem to Oldsmobile Motor Corporation" + "\nTransaction ID: " + stx.id
        }
        else if (part.equals("braking")){
            val brakingId = Id
            //transfer braking token
            val brakingStateAndRef = serviceHub.vaultService.queryBy<BrakingTokenState>().states
                    .filter { it.state.data.linearId.id.equals(brakingId) }[0]

            //get the TokenType object
            val brakingTokenType = brakingStateAndRef.state.data
            val issuer = brakingTokenType.maintainer

            //get the pointer pointer to the braking
            val brakingTokenPointer: TokenPointer<*> = brakingTokenType.toPointer(brakingTokenType.javaClass)

            val stx = subFlow(RedeemNonFungibleTokens(brakingTokenPointer,issuer))
            return "\nThe braking part is totaled, and the token is redeem to Oldsmobile Motor Corporation" + "\nTransaction ID: " + stx.id
        }
        else if (part.equals("interior")){
            val interiorId = Id
            //transfer interior token
            val interiorStateAndRef = serviceHub.vaultService.queryBy<InteriorTokenState>().states
                    .filter { it.state.data.linearId.id.equals(interiorId) }[0]

            //get the TokenType object
            val interiorTokenType = interiorStateAndRef.state.data
            val issuer = interiorTokenType.maintainer

            //get the pointer pointer to the interior
            val interiorTokenPointer: TokenPointer<*> = interiorTokenType.toPointer(interiorTokenType.javaClass)

            val stx = subFlow(RedeemNonFungibleTokens(interiorTokenPointer,issuer))
            return "\nThe interior part is totaled, and the token is redeem to Oldsmobile Motor Corporation" + "\nTransaction ID: " + stx.id
        }
    	else{
            return "Please enter a part parameter for one of the following: [chassis, body, engine, transmission, braking, interior] "
        }
    }

}

@InitiatedBy(TotalPart::class)
class TotalPartResponder(val counterpartySession: FlowSession) : FlowLogic<Unit>() {
    @Suspendable
    override fun call() {
        // Responder flow logic goes here.
        subFlow(RedeemNonFungibleTokensHandler(counterpartySession));
    }
}

