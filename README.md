**System Architecture**

Refer to Figure1 for a summary of the system architecture of shared whiteboards. When a user receives a message from a client or manager, the server typically broadcasts it simultaneously to all connected clients. The Server's whiteboard keeps track of everyone's plots and functions as a manager, meaning that the Server propagates all system data to all users entering the system via the RMI interface. This indicates that the Server will disseminate all system data to all users who access the system via the RMI interface.

**![Diagram  Description automatically generated](file:////Users/chenoi/Library/Group%20Containers/UBF8T346G9.Office/TemporaryItems/msohtmlclip/clip_image001.png)**

Figure 1. System Structure

![img](file:////Users/chenoi/Library/Group%20Containers/UBF8T346G9.Office/TemporaryItems/msohtmlclip/clip_image002.png)

Figure 2. Server Sequence Graph

 

![Timeline  Description automatically generated](file:////Users/chenoi/Library/Group%20Containers/UBF8T346G9.Office/TemporaryItems/msohtmlclip/clip_image003.png)

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

![Graphical user interface, table  Description automatically generated](file:////Users/chenoi/Library/Group%20Containers/UBF8T346G9.Office/TemporaryItems/msohtmlclip/clip_image004.png)

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

![图形用户界面, 应用程序  描述已自动生成](file:////Users/chenoi/Library/Group%20Containers/UBF8T346G9.Office/TemporaryItems/msohtmlclip/clip_image005.png)

Figure 5. Manager GUI

 

![图片包含 图示  描述已自动生成](file:////Users/chenoi/Library/Group%20Containers/UBF8T346G9.Office/TemporaryItems/msohtmlclip/clip_image006.png)

Figure 6. Client GUI

 

 

![Graphical user interface, application  Description automatically generated](file:////Users/chenoi/Library/Group%20Containers/UBF8T346G9.Office/TemporaryItems/msohtmlclip/clip_image007.png)

Figure 7. Shared Canvas

![图形用户界面, 应用程序  描述已自动生成](file:////Users/chenoi/Library/Group%20Containers/UBF8T346G9.Office/TemporaryItems/msohtmlclip/clip_image008.png)

Figure 8. Client Request (RMI)

![Graphical user interface, application  Description automatically generated](file:////Users/chenoi/Library/Group%20Containers/UBF8T346G9.Office/TemporaryItems/msohtmlclip/clip_image009.png)

Figure 9. Client disconnected

![Graphical user interface, application  Description automatically generated](file:////Users/chenoi/Library/Group%20Containers/UBF8T346G9.Office/TemporaryItems/msohtmlclip/clip_image010.png)

Figure 10. Open Files

 

 

![Graphical user interface, application  Description automatically generated](file:////Users/chenoi/Library/Group%20Containers/UBF8T346G9.Office/TemporaryItems/msohtmlclip/clip_image011.png)

Figure 11. Save As Files

 