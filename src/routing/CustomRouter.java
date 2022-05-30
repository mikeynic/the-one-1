package routing;

import core.DTNHost;
import core.Message;
import core.Settings;

import java.util.HashMap;
import java.util.Map;

/**
 * CustomRouter message router with drop-oldest buffer and only single transferring
 * connections at a time.
 */
public class CustomRouter extends ActiveRouter {

    /**
     * The first time a message is sent from this node,
     * the destination is added to the acknowledgedContacts map.
     *
     * Upon receiving a message from another node, we check to see
     * if we have already sent a message to that node, if yes, we
     * set the boolean value in acknowledgedContacts to true.
     *
     * Even though this is not an accurate representation that a secure
     * channel has been created between nodes, this can give us a general
     * sense of which nodes are able to contact each other numerous times.
     */
    public Map<Integer, Boolean> acknowledgedContacts;

    /**
     * Constructor. Creates a new message router based on the settings in
     * the given Settings object.
     * @param s The settings object
     */
    public CustomRouter(Settings s) {
        super(s);
        acknowledgedContacts = new HashMap<>();
        //TODO: read&use CustomRouter specific settings (if any)
    }

    /**
     * Copy constructor.
     * @param r The router prototype where setting values are copied from
     */
    protected CustomRouter(CustomRouter r) {
        super(r);
        this.acknowledgedContacts = r.acknowledgedContacts;
        //TODO: copy CustomRouter settings here (if any)
    }

    @Override
    public void update() {
        // Transmission only by direct delivery
        super.update();
        if (isTransferring() || !canStartTransfer()) {
            return; // can't start a new transfer
        }

        // Try only the messages that can be delivered to final recipient
        exchangeDeliverableMessages();
    }

    @Override
    public int receiveMessage(Message m, DTNHost from) {
        // Check if this node (receiving the message) is the destination
        if(m.getTo() == this.getHost()){
            // check if this DTNHost (destination) has sent a message to the sender before
            if(acknowledgedContacts.get(m.getFrom().getAddress()) != null){
                Integer address = m.getFrom().getAddress();
                acknowledgedContacts.remove(address);
                acknowledgedContacts.put(address, true);
            }
            else{
                // if not in acknowledgedContacts, don't add, since we
                // didn't initiate the connection
            }
        }


        return super.receiveMessage(m, from);
    }

    @Override
    public CustomRouter replicate() {
        return new CustomRouter(this);
    }

}
