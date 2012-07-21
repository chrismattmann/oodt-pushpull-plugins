/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apacheextras.oodt.cas.pushpull.protocol.ftp;

//JDK imports
import java.io.File;
import java.util.LinkedList;
import java.util.List;

//Jvftp imports
import cz.dhl.ftp.Ftp;
import cz.dhl.ftp.FtpFile;
import cz.dhl.io.CoFile;
import cz.dhl.io.CoLoad;
import cz.dhl.io.LocalFile;
import cz.dhl.ui.CoConsole;

//OODT imports
import org.apache.oodt.cas.protocol.Protocol;
import org.apache.oodt.cas.protocol.ProtocolFile;
import org.apache.oodt.cas.protocol.auth.Authentication;
import org.apache.oodt.cas.protocol.exceptions.ProtocolException;
import org.apache.oodt.cas.protocol.util.ProtocolFileFilter;

/**
 * 
 * @author bfoster
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * Describe your class here
 * </p>.
 */
public class JvFtpClient implements Protocol {

    private Ftp ftp;

    public JvFtpClient() {
        ftp = new Ftp();
        ftp.getContext().setConsole(null);
    }

    @Override
    public void cd(ProtocolFile file) throws ProtocolException {
        if (!ftp.cd(file.getPath()))
            throw new ProtocolException("Failed to cd to " + file.getPath());
    }

    public void put(File fromFile, ProtocolFile toFile) throws ProtocolException{
	throw new ProtocolException("Not implemented yet.");
    }

    public void cdRoot() throws ProtocolException {
        try {
            cd(new ProtocolFile("/", true));
        } catch (Exception e) {
            throw new ProtocolException("Failed to cd to root : "
                    + e.getMessage());
        }
    }

    public void cdHome() throws ProtocolException {
	cd(new ProtocolFile("/", true));
    }


    @Override
    public void connect(String host, Authentication auth) 
            throws ProtocolException {
        try {
            if (ftp.connect(host, 21)) {
                if (!ftp.login(auth.getUser(), auth.getPass())){
                    throw new ProtocolException("Failed to login as user "
						+ auth.getUser());
                }
            } else {
                throw new ProtocolException("Failed to connect at port 21");
            }
        } catch (Exception e) {
            throw new ProtocolException("Unsuccessful connect to " + host
                    + " : " + e.getMessage());
        }
    }

    @Override
    public void close() throws ProtocolException {
        ftp.disconnect();
    }

    @Override
    public ProtocolFile pwd() throws ProtocolException {
        try {
            return new ProtocolFile(ftp
				    .pwd(), true);
        } catch (Exception e) {
            throw new ProtocolException(
                    "Failed to get current working directory : "
                            + e.getMessage());
        }
    }

    @Override
    public void get(ProtocolFile file, File toLocalFile)
            throws ProtocolException {
        try {
            CoFile downloadFile = new FtpFile(file.getPath(), ftp);
            CoFile to = new LocalFile(toLocalFile.getParentFile()
                    .getAbsolutePath(), toLocalFile.getName());
            if (!CoLoad.copy(to, downloadFile))
                throw new ProtocolException("Download returned false");
        } catch (Exception e) {
            throw new ProtocolException("Failed to download file " + file
                    + " : " + e.getMessage());
        }
    }

    @Override
    public boolean connected(){
        return ftp.isConnected();
    }

    @Override
    public List<ProtocolFile> ls() throws ProtocolException {
        LinkedList<ProtocolFile> returnList = new LinkedList<ProtocolFile>();
        try {
            CoFile dir = new FtpFile(ftp.pwd(), ftp);
            CoFile fls[] = dir.listCoFiles();
            for (CoFile file : fls) {
                returnList.add(new ProtocolFile(file.getAbsolutePath(), file
						.isDirectory()));
            }
            return returnList;
        } catch (Exception e) {
            throw new ProtocolException("Failed to ls : " + e.getMessage());
        }
    }

    @Override
    public List<ProtocolFile> ls(ProtocolFileFilter filter) throws ProtocolException {
        LinkedList<ProtocolFile> returnList = new LinkedList<ProtocolFile>();
        try {
            CoFile dir = new FtpFile(ftp.pwd(), ftp);
            CoFile fls[] = dir.listCoFiles();
            for (CoFile file : fls) {
                ProtocolFile pFile = new ProtocolFile(file.getAbsolutePath(), file
						      .isDirectory());
                if(filter.accept(pFile)){
		    returnList.add(pFile);
		}
            }
            return returnList;
        } catch (Exception e) {
            throw new ProtocolException("Failed to ls : " + e.getMessage());
        }


    }

    @Override
    public void delete(ProtocolFile file) throws ProtocolException{
        try {
            ftp.rm(file.getPath());
        } catch (Exception e) {
            throw new ProtocolException(e.getMessage());
        }
    }

}
