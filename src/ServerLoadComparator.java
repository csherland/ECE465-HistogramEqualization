import java.util.Comparator;

/**
 * Created by lusterdome on 2/26/14.
 */
public class ServerLoadComparator implements Comparator<ServerStatus> {

    @Override
    public int compare(ServerStatus ss1, ServerStatus ss2) {
        if (ss1.getLoad() >= ss2.getLoad())
            return 1;

        return -1;
    }
}
