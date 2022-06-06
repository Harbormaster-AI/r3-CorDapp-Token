package com.acme.automobilemarket.flows

import co.paralleluniverse.fibers.Suspendable
import com.r3.corda.lib.tokens.contracts.types.TokenPointer
import com.r3.corda.lib.tokens.workflows.flows.move.addMoveNonFungibleTokens
import com.r3.corda.lib.tokens.workflows.internal.flows.distribution.UpdateDistributionListFlow
import com.r3.corda.lib.tokens.workflows.internal.flows.finality.ObserverAwareFinalityFlow
import com.r3.corda.lib.tokens.workflows.internal.flows.finality.ObserverAwareFinalityFlowHandler
import com.r3.corda.lib.tokens.workflows.utilities.getPreferredNotary
import net.corda.core.contracts.requireThat
import net.corda.core.flows.*
import net.corda.core.identity.CordaX500Name
import net.corda.core.identity.Party
import net.corda.core.node.services.queryBy
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
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

class TransferAutomobileToken(
		val chassisId: UUID,
		val bodyId: UUID,
		val engineId: UUID,
		val transmissionId: UUID,
		val brakingId: UUID,
		val interiorId: UUID,
        val holder: Party) : FlowLogic<String>() {
    override val progressTracker = ProgressTracker()

    @Suspendable
    override fun call():String {
    
        //Step 1 : Chassis Token
        //get chassis states on ledger
        val chassisStateAndRef = serviceHub.vaultService.queryBy<ChassisTokenState>().states
                .filter { it.state.data.linearId.id.equals(chassisId) }[0]

        //get the TokenType object
        val chassisTokenType = chassisStateAndRef.state.data

        //get the pointer pointer to the chassis
        val chassisTokenPointer: TokenPointer<*> = chassisTokenType.toPointer(chassisTokenType.javaClass)
    
        //Step 2 : Body Token
        //get body states on ledger
        val bodyStateAndRef = serviceHub.vaultService.queryBy<BodyTokenState>().states
                .filter { it.state.data.linearId.id.equals(bodyId) }[0]

        //get the TokenType object
        val bodyTokenType = bodyStateAndRef.state.data

        //get the pointer pointer to the body
        val bodyTokenPointer: TokenPointer<*> = bodyTokenType.toPointer(bodyTokenType.javaClass)
    
        //Step 3 : Engine Token
        //get engine states on ledger
        val engineStateAndRef = serviceHub.vaultService.queryBy<EngineTokenState>().states
                .filter { it.state.data.linearId.id.equals(engineId) }[0]

        //get the TokenType object
        val engineTokenType = engineStateAndRef.state.data

        //get the pointer pointer to the engine
        val engineTokenPointer: TokenPointer<*> = engineTokenType.toPointer(engineTokenType.javaClass)
    
        //Step 4 : Transmission Token
        //get transmission states on ledger
        val transmissionStateAndRef = serviceHub.vaultService.queryBy<TransmissionTokenState>().states
                .filter { it.state.data.linearId.id.equals(transmissionId) }[0]

        //get the TokenType object
        val transmissionTokenType = transmissionStateAndRef.state.data

        //get the pointer pointer to the transmission
        val transmissionTokenPointer: TokenPointer<*> = transmissionTokenType.toPointer(transmissionTokenType.javaClass)
    
        //Step 5 : Braking Token
        //get braking states on ledger
        val brakingStateAndRef = serviceHub.vaultService.queryBy<BrakingTokenState>().states
                .filter { it.state.data.linearId.id.equals(brakingId) }[0]

        //get the TokenType object
        val brakingTokenType = brakingStateAndRef.state.data

        //get the pointer pointer to the braking
        val brakingTokenPointer: TokenPointer<*> = brakingTokenType.toPointer(brakingTokenType.javaClass)
    
        //Step 6 : Interior Token
        //get interior states on ledger
        val interiorStateAndRef = serviceHub.vaultService.queryBy<InteriorTokenState>().states
                .filter { it.state.data.linearId.id.equals(interiorId) }[0]

        //get the TokenType object
        val interiorTokenType = interiorStateAndRef.state.data

        //get the pointer pointer to the interior
        val interiorTokenPointer: TokenPointer<*> = interiorTokenType.toPointer(interiorTokenType.javaClass)

        //send tokens
        val session = initiateFlow(holder)

        // Obtain a reference from a notary
        val notary = serviceHub.networkMapCache.getNotary(CordaX500Name.parse("O=Notary,L=London,C=GB"))
        val txBuilder = TransactionBuilder(notary)

        addMoveNonFungibleTokens(txBuilder,serviceHub,chassisTokenPointer,holder)
        addMoveNonFungibleTokens(txBuilder,serviceHub,bodyTokenPointer,holder)
        addMoveNonFungibleTokens(txBuilder,serviceHub,engineTokenPointer,holder)
        addMoveNonFungibleTokens(txBuilder,serviceHub,transmissionTokenPointer,holder)
        addMoveNonFungibleTokens(txBuilder,serviceHub,brakingTokenPointer,holder)
        addMoveNonFungibleTokens(txBuilder,serviceHub,interiorTokenPointer,holder)

        val ptx = serviceHub.signInitialTransaction(txBuilder)
        val stx = subFlow(CollectSignaturesFlow(ptx, listOf(session)))
        val ftx = subFlow(ObserverAwareFinalityFlow(stx, listOf(session)))

        /* Distribution list is a list of identities that should receive updates. For this mechanism to behave correctly we call the UpdateDistributionListFlow flow */
        subFlow(UpdateDistributionListFlow(ftx))

        return ("\nTransfer ownership of a automobile ( " 
				+ "Chassis Id: " + this.chassisId
        		+ ", " 
				+ "Body Id: " + this.bodyId
        		+ ", " 
				+ "Engine Id: " + this.engineId
        		+ ", " 
				+ "Transmission Id: " + this.transmissionId
        		+ ", " 
				+ "Braking Id: " + this.brakingId
        		+ ", " 
				+ "Interior Id: " + this.interiorId
        		+ ") to "
                + holder.name.organisation + "\nTransaction IDs: "
                + ftx.id)
    }
}

@InitiatedBy(TransferAutomobileToken::class)
class TransferAutomobileTokenResponder(val flowSession: FlowSession) : FlowLogic<Unit>() {
    @Suspendable
    override fun call(): Unit {
        subFlow(ObserverAwareFinalityFlowHandler(flowSession))
    }
}

