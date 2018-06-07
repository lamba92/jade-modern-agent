package it.lamba.behaviours

import it.lamba.agents.ModernAgent
import jade.lang.acl.ACLMessage
import jade.lang.acl.MessageTemplate
import jade.proto.ContractNetResponder

/**
 * Commodity class for [ContractNetResponder].
 * @param modernAgent The [ModernAgent] which will add this [jade.core.behaviours.Behaviour].
 * @param cfpTemplate The [MessageTemplate] that will be used to filter incoming messages.
 * @param verbose Enable logging on console. By default `true`
 */
abstract class ModernContractNetResponder(private val modernAgent: ModernAgent, cfpTemplate: MessageTemplate, var verbose: Boolean = true):
        ContractNetResponder(modernAgent, cfpTemplate) {

    override fun handleCfp(cfp: ACLMessage): ACLMessage {
        if(verbose) modernAgent.log("CFP propose from: ${cfp.sender.name} | content:\n${cfp.content}")
        return onContractReceived(cfp)
    }

    override fun handleAcceptProposal(cfp: ACLMessage, propose: ACLMessage, accept: ACLMessage): ACLMessage {
        if(verbose) modernAgent.log("Contract propose accepted, accept message content is:\n${accept.content}")
        return onContractProposalAccepted(cfp, propose, accept)
    }

    override fun handleRejectProposal(cfp: ACLMessage, propose: ACLMessage, reject: ACLMessage) {
        if(verbose) modernAgent.log("Contract propose rejected. Reject message is: ${reject.content}")
        onContractProposalRejected(cfp, propose, reject)
    }

    /**
     * Callback method invoked when a new contract is received.
     * @param cfp The [ACLMessage] containing performative = CFP.
     */
    abstract fun onContractReceived(cfp: ACLMessage): ACLMessage

    /**
     * Callback method invoked when the current contract proposal has been accepted.
     * @param cfp The original contract initiator [ACLMessage].
     * @param propose The propose massage which has been accepted.
     * @param accept The message of the initiator agent used to accept the proposal.
     */
    abstract fun onContractProposalAccepted(cfp: ACLMessage, propose: ACLMessage, accept: ACLMessage): ACLMessage

    /**
     * Callback invoked when the agent who initiated the contract refused the proposal.
     * @param cfp The original contract initiator [ACLMessage].
     * @param propose The propose massage which has been reject.
     * @param reject The message of the initiator agent used to reject the proposal.
     */
    abstract fun onContractProposalRejected(cfp: ACLMessage, propose: ACLMessage, reject: ACLMessage)
}