/**
 * ServerLoadComparator.java
 *  Allows for comparison of server load to determine best
 *  server to connect a client to.
 *
 *  If the load on ss1 is greater, return 1. If the load on
 *  ss2 is greater return -1. If the loads are equal return 0.
 *
 *  @author Christian Sherland
 *  @author Ethan Lusterman
 *  @author Michael Scibor
 *
 *  @version 1.0 Mar 6 2014
 */

import java.util.Comparator;

public class ServerLoadComparator implements Comparator<ServerStatus> {

    @Override
    public int compare(ServerStatus ss1, ServerStatus ss2) {
        if (ss1.getLoad() > ss2.getLoad()) {
            return 1;
        } else if (ss1.getLoad == ss2.getLoad()) {
            return 0;
        } else {
            return -1;
        }
    }
}
