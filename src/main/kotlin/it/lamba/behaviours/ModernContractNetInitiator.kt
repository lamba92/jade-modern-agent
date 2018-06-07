package it.lamba.behaviours

import it.lamba.agents.ModernAgent
import jade.lang.acl.ACLMessage
import jade.proto.ContractNetInitiator
import java.util.*
import kotlin.collections.ArrayList

/**
 * Commodity class for [ContractNetInitiator].
 * @param modernAgent The [ModernAgent] which will add this [jade.core.behaviours.Behaviour].
 * @param cfpMessage The CFP message that will be sent to the receivers.
 * @param verbose Enable logging on console. By default `true`
 */
abstract class ModernContractNetInitiator(private val modernAgent: ModernAgent, cfpMessage: ACLMessage, var verbose: Boolean = true): ContractNetInitiator(modernAgent, cfpMessage) {


    override fun handlePropose(propose: ACLMessage, acceptances: Vector<*>) {
        if(verbose) modernAgent.log("Agent ${propose.sender.name} proposed ${propose.content}")
        onProposeReceived(propose)
    }

    override fun handleRefuse(refuse: ACLMessage) {
        modernAgent.log("Agent ${refuse.sender.name} refused")
        onRefuseReceived(refuse)
    }

    override fun handleFailure(failure: ACLMessage) {
        if (failure.sender == myAgent.ams) {
            // FAILURE notification from the JADE runtime: the receiver
            // does not exist
            if(verbose) modernAgent.log("Responder does not exist", modernAgent.STD_ERR)
        }
        else {
            if(verbose) modernAgent.log("Agent ${failure.sender.name} failed")
        }
        onFailureReceived(failure)
    }

    override fun handleAllResponses(responses: Vector<Any>, acceptances: Vector<Any>) {
        val responsesArray = ArrayList<ACLMessage>()
                .apply { for(message in responses) add(message as ACLMessage) }.toTypedArray()
        onContractTimeExpired(responsesArray).forEach {
            acceptances.addElement(it)
        }
    }

    override fun handleInform(inform: ACLMessage) {
        modernAgent.log("Agent ${inform.sender.name} successfully performed the requested action")
        onActionPerformed(inform)
    }

    /**
     * Callback method invoked with a propose [ACLMessage].
     * @param propose The [ACLMessage] received from an agent.
     */
    abstract fun onProposeReceived(propose: ACLMessage)

    /**
     * Callback method invoked when previous offer was rejected.
     * @param refuse The refuse [ACLMessage] sent from an agent.
     */
    abstract fun onRefuseReceived(refuse: ACLMessage)

    /**
     * Callback method invoked when a communication between this agent a receiver went wrong.
     * @param failure The failure [ACLMessage] sent from an agent or the AMS.
     */
    abstract fun onFailureReceived(failure: ACLMessage)

    /**
     * Callback method invoked when the contract time expired or all receivers in the cfpMessage.
     * @param responses An [Array]<[ACLMessage]> containing the all responses for the current contract.
     * @return An [Array]<[ACLMessage]> that need to contain all responses for proposers, including the accepted one.
     */
    abstract fun onContractTimeExpired(responses: Array<ACLMessage>): Array<ACLMessage>

    /**
     * Callback method invoked when the agent who won the contract performs the action.
     * @param inform [ACLMessage] containing performative = INFORM.
     */
    abstract fun onActionPerformed(inform: ACLMessage)
}