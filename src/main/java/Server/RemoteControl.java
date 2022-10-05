package Server;

import Remote.IRemoteConnect;

import javax.swing.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Server side implementation of the remote interface.
 * Must extend UnicastRemoteObject, to allow the JVM to create a
 * remote proxy/stub.
 *
 * @author Andy
 * @date 5/23/2022 9:45 PM
 */
// Implementing the remote interface
public class RemoteControl extends UnicastRemoteObject implements IRemoteConnect {

    protected RemoteControl() throws RemoteException {
    }

    // overwrites the interface of request for connection
    public Boolean connectionWall(String userName) throws RemoteException {
        int option = JOptionPane.showConfirmDialog(null, userName + " wants to share your whiteboard", "Join Request", JOptionPane.YES_NO_OPTION);

        // if approved, add to the user list
        if (option == 0) {
            System.out.println("client " + userName + " is connected!");
            CreateWhiteBoard.validatedUsers.put(CreateWhiteBoard.threadPool.size(), userName);
            return true;
        }
        return false;
    }
}
