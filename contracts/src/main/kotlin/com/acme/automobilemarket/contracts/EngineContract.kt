package com.acme.automobilemarket.contracts

import com.r3.corda.lib.tokens.contracts.EvolvableTokenContract
import net.corda.core.contracts.Contract
import net.corda.core.contracts.Requirements
import net.corda.core.contracts.requireThat
import net.corda.core.transactions.LedgerTransaction
import com.acme.automobilemarket.states.EngineTokenState


class EngineContract : EvolvableTokenContract(), Contract {
    override fun additionalCreateChecks(tx: LedgerTransaction) {
        val newToken = tx.outputStates.single() as EngineTokenState
        newToken.apply {
        
        }
    }

    override fun additionalUpdateChecks(tx: LedgerTransaction) {
        /*This additional check does not apply to this use case.
         *This sample does not allow token update */
    }

    companion object {
        const val CONTRACT_ID = "com.acme.automobilemarket.contracts.EngineContract"
    }
}
