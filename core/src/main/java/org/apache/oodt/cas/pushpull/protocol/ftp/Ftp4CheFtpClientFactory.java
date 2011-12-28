package org.apache.oodt.cas.pushpull.protocol.ftp;

//OODT imports
import org.apache.oodt.cas.pushpull.protocol.Protocol;
import org.apache.oodt.cas.pushpull.protocol.ProtocolFactory;

/**
 * 
 * @author bfoster
 * @version $Revision$
 * 
 * <p>
 * Describe your class here
 * </p>.
 */
public class Ftp4CheFtpClientFactory implements ProtocolFactory {

    public Protocol newInstance() {
        return new Ftp4CheFtpClient();
    }

}
