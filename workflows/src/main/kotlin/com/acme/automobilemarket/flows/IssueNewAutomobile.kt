package com.acme.automobilemarket.flows

import co.paralleluniverse.fibers.Suspendable
import com.r3.corda.lib.tokens.contracts.types.TokenPointer
import com.r3.corda.lib.tokens.contracts.utilities.issuedBy
import com.r3.corda.lib.tokens.workflows.flows.rpc.IssueTokens
import com.r3.corda.lib.tokens.workflows.utilities.heldBy
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.StartableByRPC
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
@StartableByRPC
class IssueNewAutomobile(val chassisId: UUID, val bodyId: UUID, val engineId: UUID, val transmissionId: UUID, val brakingId: UUID, val interiorId: UUID, 
                   val holder: Party) : FlowLogic<String>() {
    override val progressTracker = ProgressTracker()

    @Suspendable
    override fun call():String {
    
        //Step 1 : Chassis Token
        //get chassis states on ledger
        val chassisStateAndRef = serviceHub.vaultService.queryBy<ChassisTokenState>().states
                .filter { it.state.data.linearId.id.equals(chassisId) }[0]

        //get the TokenType object
        val chassistokentype = chassisStateAndRef.state.data

        //get the pointer pointer to the chassis
        val chassistokenPointer = chassistokentype.toPointer(chassistokentype.javaClass)

        //assign the issuer to the chassis type who will be issuing the tokens
        val chassisissuedTokenType = chassistokenPointer issuedBy ourIdentity

        //mention the current holder also
        val chassistoken = chassisissuedTokenType heldBy holder
    
        //Step 2 : Body Token
        //get body states on ledger
        val bodyStateAndRef = serviceHub.vaultService.queryBy<BodyTokenState>().states
                .filter { it.state.data.linearId.id.equals(bodyId) }[0]

        //get the TokenType object
        val bodytokentype = bodyStateAndRef.state.data

        //get the pointer pointer to the body
        val bodytokenPointer = bodytokentype.toPointer(bodytokentype.javaClass)

        //assign the issuer to the body type who will be issuing the tokens
        val bodyissuedTokenType = bodytokenPointer issuedBy ourIdentity

        //mention the current holder also
        val bodytoken = bodyissuedTokenType heldBy holder
    
        //Step 3 : Engine Token
        //get engine states on ledger
        val engineStateAndRef = serviceHub.vaultService.queryBy<EngineTokenState>().states
                .filter { it.state.data.linearId.id.equals(engineId) }[0]

        //get the TokenType object
        val enginetokentype = engineStateAndRef.state.data

        //get the pointer pointer to the engine
        val enginetokenPointer = enginetokentype.toPointer(enginetokentype.javaClass)

        //assign the issuer to the engine type who will be issuing the tokens
        val engineissuedTokenType = enginetokenPointer issuedBy ourIdentity

        //mention the current holder also
        val enginetoken = engineissuedTokenType heldBy holder
    
        //Step 4 : Transmission Token
        //get transmission states on ledger
        val transmissionStateAndRef = serviceHub.vaultService.queryBy<TransmissionTokenState>().states
                .filter { it.state.data.linearId.id.equals(transmissionId) }[0]

        //get the TokenType object
        val transmissiontokentype = transmissionStateAndRef.state.data

        //get the pointer pointer to the transmission
        val transmissiontokenPointer = transmissiontokentype.toPointer(transmissiontokentype.javaClass)

        //assign the issuer to the transmission type who will be issuing the tokens
        val transmissionissuedTokenType = transmissiontokenPointer issuedBy ourIdentity

        //mention the current holder also
        val transmissiontoken = transmissionissuedTokenType heldBy holder
    
        //Step 5 : Braking Token
        //get braking states on ledger
        val brakingStateAndRef = serviceHub.vaultService.queryBy<BrakingTokenState>().states
                .filter { it.state.data.linearId.id.equals(brakingId) }[0]

        //get the TokenType object
        val brakingtokentype = brakingStateAndRef.state.data

        //get the pointer pointer to the braking
        val brakingtokenPointer = brakingtokentype.toPointer(brakingtokentype.javaClass)

        //assign the issuer to the braking type who will be issuing the tokens
        val brakingissuedTokenType = brakingtokenPointer issuedBy ourIdentity

        //mention the current holder also
        val brakingtoken = brakingissuedTokenType heldBy holder
    
        //Step 6 : Interior Token
        //get interior states on ledger
        val interiorStateAndRef = serviceHub.vaultService.queryBy<InteriorTokenState>().states
                .filter { it.state.data.linearId.id.equals(interiorId) }[0]

        //get the TokenType object
        val interiortokentype = interiorStateAndRef.state.data

        //get the pointer pointer to the interior
        val interiortokenPointer = interiortokentype.toPointer(interiortokentype.javaClass)

        //assign the issuer to the interior type who will be issuing the tokens
        val interiorissuedTokenType = interiortokenPointer issuedBy ourIdentity

        //mention the current holder also
        val interiortoken = interiorissuedTokenType heldBy holder

        //distribute the new automobile (two token to be exact)
        //call built in flow to issue non fungible tokens
        val stx = subFlow(IssueTokens(listOf(
        
        										chassistoken,
        										bodytoken,
        										enginetoken,
        										transmissiontoken,
        										brakingtoken,
        										interiortoken
        										)))

        return ("\nA new automobile is being issued to " + holder.name.organisation + " with "
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
                + "\nTransaction ID: " + stx.id)
    }
}