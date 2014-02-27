/*
 * ServerLoadComparator.java
 *  Allows for comparison of server load to
 *  determine best server to connect a client
 *  to.
 */

import java.util.Comparator;

public class ServerLoadComparator implements Comparator<ServerStatus> {

    @Override
    public int compare(ServerStatus ss1, ServerStatus ss2) {
        if (ss1.getLoad() >= ss2.getLoad()) {
            return 1;
        }

        return -1;
    }
}
