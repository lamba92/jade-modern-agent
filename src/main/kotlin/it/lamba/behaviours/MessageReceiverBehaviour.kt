package it.lamba.behaviours

import it.lamba.agents.ModernAgent
import jade.core.behaviours.CyclicBehaviour
import jade.lang.acl.MessageTemplate
import jade.lang.acl.ACLMessage
import jade.core.Agent

/**
 * This [CyclicBehaviour] allows to notify the [modernAgent] when an [ACLMessage] with desired [MessageTemplate] is received.
 * @param messageType Message types are specified in [ACLMessage] class as public [Int]s.
 * @param modernAgent The [Agent] extending [ModernAgent] that needs to be notified of the messages.
 */
class MessageReceiverBehaviour(private val modernAgent: ModernAgent, private val messageType: Int): CyclicBehaviour(){
    var isActive = false
    override fun action() {
        val message = modernAgent.blockingReceive(MessageTemplate.MatchPerformative(messageType))
        modernAgent.onMessageReceived(message)
    }
}