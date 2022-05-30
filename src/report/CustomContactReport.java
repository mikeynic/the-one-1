package report;

import core.DTNHost;
import core.Message;
import core.MessageListener;
import routing.CustomRouter;

public class CustomContactReport extends Report implements MessageListener {

    @Override
    public void newMessage(Message m) {

    }

    @Override
    public void messageTransferStarted(Message m, DTNHost from, DTNHost to) {
        // only to be used with CustomRouter...
        CustomRouter router = (CustomRouter) to.getRouter();
    }

    @Override
    public void messageDeleted(Message m, DTNHost where, boolean dropped) {

    }

    @Override
    public void messageTransferAborted(Message m, DTNHost from, DTNHost to) {

    }

    @Override
    public void messageTransferred(Message m, DTNHost from, DTNHost to, boolean firstDelivery) {

        int currentNumberOfHops = m.getHops().size();
        if(m.getHops().get(currentNumberOfHops) == to){
            // if current node is the message destination, add a pair of nodes to a map.
        }
    }

    @Override
    public void done() {
        write("Attempt to write message acknowledgment statistics...");

        super.done();
    }
}
