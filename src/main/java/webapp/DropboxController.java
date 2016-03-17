package webapp;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.FullAccount;

/**
 * This class controls the Dropbox application logic.
 */
public class DropboxController {
    private static Dropbox dropbox;
    private DbxClientV2 client;
    private DbxRequestConfig config;

    public DropboxController() {
        initialize();
    }

    private void initialize() {
        dropbox = new Dropbox(); // uses default json file credentials
        config = new DbxRequestConfig("dropbox/IronGate", "en_US");
        client = new DbxClientV2(config, dropbox.getToken());
    }

    public void test() throws DbxException {
        FullAccount account = client.users.getCurrentAccount();
        System.out.println(account);
    }
}
