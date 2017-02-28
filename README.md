# How to build

You can build this project one at the command line with Apache Ant
or through the NetBeans GUI.

## Building with NetBeans:

You only need to use the NetBeans configuration menus to switch between
building or running the client and the server.

In order to build the project through NetBeans:
1) Use the "Open Project" menu item and point it to the cloned
   project.
2) Set the config to "Server" and click build.
3) Set the config to "Client" and click build.

In order to run the project through NetBeans:
1) Set the config to "Server".
2) Right Click the project and click run.
3) Set the config to "Client".
4) Right click the project and click run.
5) Repeat steps 3 and 4 to get a second client.

## Building on the Command Line with Apache Ant:

Building the client or server with Apache Ant requires you to set a
configuration variable. This variable will be set to the last
NetBeans build target by default

In order to build 
1) Change to the directory of the cloned project.
2) Run "ant -Dconfig=Server" to build the server.
3) Run "ant -Dconfig=Client" to build the client.

In order to run the project on the command line:
1) Open 3 shells and cd to the directory of the cloned project.
2) Run "java -jar dist/Server/BattleshipServer.jar" in one shell.
3) Run "java -jar dist/Client/BattleshipClient.jar" in the other shells.

###OR

In order to build
1) ./compile.sh

In order to run the project on the command line:
1) For the server: ./runServer.sh
2) For the client: ./runClient.sh

