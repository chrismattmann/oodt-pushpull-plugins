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

//FTP4CHE imports
import org.ftp4che.FTPConnection;
import org.ftp4che.FTPConnectionFactory;
import org.ftp4che.util.ftpfile.FTPFile;

//OODT imports
import org.apache.oodt.cas.protocol.exceptions.ProtocolException;
import org.apache.oodt.cas.protocol.Protocol;
import org.apache.oodt.cas.protocol.ProtocolFile;
import org.apache.oodt.cas.protocol.auth.Authentication;
import org.apache.oodt.cas.protocol.util.ProtocolFileFilter;

/**
 * 
 * @author bfoster
 * @version $Revision$
 * 
 * <p>
 * Describe your class here
 * </p>.
 */
public class Ftp4CheFtpClient implements Protocol {

    FTPConnection ftp;

    @Override
    public void cd(ProtocolFile file) throws ProtocolException {
        try {
            ftp.changeDirectory(file.getPath());
        } catch (Exception e) {
            throw new ProtocolException("Failed to cd to " + file.getPath() + " : "
                    + e.getMessage());
        }
    }



    @Override
    public void cdHome() throws ProtocolException{
	cd(new ProtocolFile(ProtocolFile.SEPARATOR, true));
    }


    @Override
    public void cdRoot() throws ProtocolException {
        try {
            cd(new ProtocolFile("/", true));
        } catch (Exception e) {
            throw new ProtocolException("Failed to cd to root : "
                    + e.getMessage());
        }
    }

    @Override
	public void connect(String host, Authentication auth) 
            throws ProtocolException {
        try {
            ftp = FTPConnectionFactory.getInstance(host, 21, auth.getUser(),
    		   auth.getPass(), null, 10000, FTPConnection.FTP_CONNECTION, true);
            ftp.connect();
        } catch (Exception e) {
            throw new ProtocolException("Failed to connect to " + host + " : "
                    + e.getMessage());
        }
    }

    @Override
    public void close() throws ProtocolException {
        ftp.disconnect();
    }

    @Override
    public void get(ProtocolFile file, File toLocalFile)
            throws ProtocolException {
        try {
            ftp.downloadFile(
                    new FTPFile(file.getPath(), file
                            .isDir()), new FTPFile(toLocalFile));
        } catch (Exception e) {
            throw new ProtocolException("Failed to download " + file + " : "
                    + e.getMessage());
        }
    }

    public List<ProtocolFile> ls() throws ProtocolException {
        try {
            List<FTPFile> fileList = ftp.getDirectoryListing();
            List<ProtocolFile> returnList = new LinkedList<ProtocolFile>();
            for (int i = 0; i < fileList.size(); i++) {
                FTPFile file = fileList.get(i);
                String path = this.pwd().getPath();
                ProtocolFile pFile = new ProtocolFile(path + "/" + file.getName(), 
						      file.isDirectory());
                returnList.add(pFile);
            }
            return returnList;
        } catch (Exception e) {
            throw new ProtocolException("Failed to get file list : "
                    + e.getMessage());
        }
    }

    @Override
    public List<ProtocolFile> ls(ProtocolFileFilter filter) throws ProtocolException{
        try {
            List<FTPFile> fileList = ftp.getDirectoryListing();
            List<ProtocolFile> returnList = new LinkedList<ProtocolFile>();
            for (int i = 0; i < fileList.size(); i++) {
                FTPFile file = fileList.get(i);
                String path = this.pwd().getPath();
                ProtocolFile pFile = new ProtocolFile(path + "/" + file.getName(), file
						      .isDirectory());
                if(filter.accept(pFile)) returnList.add(pFile);
            }
            return returnList;
        } catch (Exception e) {
            throw new ProtocolException("Failed to get file list : "
					+ e.getMessage());
        }


    }

    @Override
	public boolean connected(){
        return ftp.getConnectionStatus() == ftp.CONNECTED;
    }


    @Override
    public void put(File fromFile, ProtocolFile toFile) throws ProtocolException {
	try {
	    ftp.uploadFile(new FTPFile(fromFile.getAbsolutePath(),fromFile.getName(), fromFile.isDirectory()), new FTPFile(toFile.getPath(), toFile.getName(), toFile.isDir()));
	}catch (Exception e) {
	    throw new ProtocolException("Failed to put file '" + fromFile + "' : " + e.getMessage(), e);
	}
    }

    @Override
    public ProtocolFile pwd() throws ProtocolException {
        try {
            return new ProtocolFile(ftp
				    .getWorkDirectory(), true);
        } catch (Exception e) {
            throw new ProtocolException("pwd command failed : "
                    + e.getMessage());
        }
    }

    @Override
    public void delete(ProtocolFile file) throws ProtocolException{
        try {
            ftp.deleteFile(new FTPFile(file.getPath(),
                    false));
        } catch (Exception e) {
            throw new ProtocolException(e.getMessage());
        }
    }

}
