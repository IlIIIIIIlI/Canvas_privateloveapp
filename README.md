## This is a java-gradle project
### This is a real-time shared canvas project.

## To run this project
- go to server/ file, use command ```java -jar CreateWhiteBoard.jar <IP> <Port> <Username>```
   - the server one is administrator name 
- go to client/ file, use command ```java -jar JoinWhiteBoard.jar <IP> <Port> <Username>```

**System Architecture**

Refer to Figure1 for a summary of the system architecture of shared whiteboards. When a user receives a message from a client or manager, the server typically broadcasts it simultaneously to all connected clients. The Server's whiteboard keeps track of everyone's plots and functions as a manager, meaning that the Server propagates all system data to all users entering the system via the RMI interface. This indicates that the Server will disseminate all system data to all users who access the system via the RMI interface.

![image](https://user-images.githubusercontent.com/68847099/193992974-ef3929fb-a928-40a7-aa68-dc5e43212655.png)

Figure 1. System Structure

![image](https://user-images.githubusercontent.com/68847099/193993046-9ddbfc1d-911b-4d69-91b2-b9dec11ea8df.png)

Figure 2. Server Sequence Graph

 

![image](https://user-images.githubusercontent.com/68847099/193993095-97a2bc29-969a-4bfc-a99e-7f2cf9566713.png)

Figure 3. Client Sequence Graph

 

 

The Server Sequence Graph in Figure 2 demonstrates that the server regulates the user's permissions via the IRemoteConnect interface of the overwrite RMI. Once this is accomplished, the ServerGUI may accept the user's graph information and simultaneously send and print it to its own canvas.

Referring to Figure 3, from the Client Sequence Graph, we first invoke the remote object interface IRemoteConnect via RMI before exchanging data with the server canvas.

 

**Communication Protocols**

In this project, I utilised a combination of RMI and socket technologies. I will describe how and why I implemented it below.

The Client is able to make remote method calls by invoking remote objects and can look up remote objects in the RMI registry, which is highly adaptable, provides continuous remote object calls, and has a high level of security. I authorise the Client to use RMI, with a readout of the username in the Server's Remote Control and a manual check by the management. The Client-side can only see the IRemoteConnect interface and invoke it, but cannot know the detailed settings of the interface. This ensures that the programme runs securely. Any client wishing to connect to the whiteboard server will only be given the command whether or not to be allowed when invoking the IRemoteConnect interface. IRemoteConnect delivers a useful class object to the Client for validation and permits the interface to be updated and altered multiple times without the user knowing.

Consequently, RMI can also include interfaces to various tools on the Canvas, specified by the server and called directly by the Client, and which can be logged directly by the server during client use to ease the process. However, I was hesitant to use the technology since it was my first time doing so. 

The benefits of using RMI are obvious, no need to manually define datastreams such as in and out. However, the disadvantages are that the Client is under more pressure and has to deal with additional exceptions, and because RMI is dependent on the IP address and port of the server, this also adds to the maintenance costs.

For data transfer, I establish a connection between the Client and server using a TCP socket and send a JSON file with all functions initialised as commands. 

The benefit of this is that each command (such as Line) is clear, and if the transmission fails in the middle, the server is disconnected or prompted rather than receiving a message that is unclear. 

The following is the format:

{"P":{"endY":99,"endX":221,"color":"-10092289","startY":99,"startX":222,"user":"chenoi"}}, {"E":{"endY":99,"endX":222,"color":"-10092289","startY":99,"startX":222,"user":"chenoi"}},

I have sent all the parameters needed for each draw graphic through the socket.

 

**Class Design**

![image](https://user-images.githubusercontent.com/68847099/193993147-66af6849-c8bd-45f0-af3b-29b94a047c3f.png)

Figure 4. Class UML

 

See Figure 4 . I did not define many classes as the design was heavily weighted towards a step-by-step implementation using sockets, but firstly, there is a GUI for both Server and Client, in which I defined directly how each function would look when clicked using buttonListener, and with the help of java.awt and java.Graphics, I can draw images locally on the GUI relatively quickly. This achieves the first step of the functionality. Since I need to manage events for each user, I rewrote the thread method in the Client Class. This thread will make a long connection to the server's socket and constantly fetch the contents of the server's Propagate and send the contents of my canvas to the server. There is also a canvas on the Server side, the overall implementation is similar. However, as new users need access to the data, I have defined accept, feedback, and propagate in Propagation and use an ArrayList<JSONOBJECT> to manage all the history of the instructions and send this to the server when a new socket is received. This is sent to the client when a new socket is received so that it can display the past content of the canvas.

 

**Critical Analysis**

Note that the RMI binding port cannot be the same as the socket port, or an error will be reported. It is easy to confuse the JSON object data type with JSON.simple. Parse. Sending commands is somewhat unstable, particularly when attempting to implement saving the previous Canvas content. I ultimately converted the ArrayList JSON Object to String by replacing the brackets before and after each string.

When reading, Scanner.next() is used to manually parse.

This article also uses the synchronised method to handle potential conflicting transactions. For instance, if the user simultaneously uses the eraser and pen in the paintArea, the middle process must be displayed or the programme will fail. I also utilise data structures such as Hashtables and Vectors, which are excellent for concurrency and provide a high level of thread safety. I manage keystrokes to the canvas using addMouseMotionListener and addMouseListener, which have proven very effective.

 

**Innovations**

I have implemented all the requirements for this project.

I met most of the requirements by establishing a long socket connection to send and receive the results of Canvas operations from both the client and the server. However, for academic purposes, I also discovered that RMI and ZooKeeper could be utilised. Register all RMI services published by the service provider with ZooKeeper and allow the service consumer to listen to ZooKeeper's Znode to obtain the currently available RMI services.

**Appendix**

![image](https://user-images.githubusercontent.com/68847099/193993190-8eb4f20d-5650-4a40-81e0-b41abfca01f3.png)

Figure 5. Manager GUI

 

![image](https://user-images.githubusercontent.com/68847099/193993232-af7b03db-19ec-42c6-99c5-f2e63fe0e848.png)

Figure 6. Client GUI
 
![image](https://user-images.githubusercontent.com/68847099/193993261-0c794268-6ca6-4b48-9949-a2a7710f2250.png)

Figure 7. Shared Canvas

![image](https://user-images.githubusercontent.com/68847099/193993300-468541f2-157f-46cc-9e17-d801718bc49c.png)

Figure 8. Client Request (RMI)

![image](https://user-images.githubusercontent.com/68847099/193993326-96f97913-7e64-4d7e-91a9-4d877b6ba302.png)

Figure 9. Client disconnected

![image](https://user-images.githubusercontent.com/68847099/193993351-e85c38c5-2075-4cfa-9146-7b1bc967027c.png)

Figure 10. Open Files

![image](https://user-images.githubusercontent.com/68847099/193993375-9d7c6839-cee5-4f2b-abb0-ffc333e0cd9c.png)

Figure 11. Save As Files

 
