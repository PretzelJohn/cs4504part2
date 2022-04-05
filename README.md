<h1>CS 4504 Project (Part 2)</h1>
<h6>by John Hussey, Ryan Kim, Nick Nguyen, and David Shipman</h6>

<h2>Description</h2>
<p>This is a group class project to investigate and analyze peer-to-peer (p2p) networks.</p>
<p>This project requires the use of <em>at least</em> 4 separate computers, which are connected on a local network.</p>
<ul>
    <li>Routers: 2 computers will act as routers, which help two peers establish a direct p2p connection.</li>
    <li>Peers: At least 2 computers will act as peers, where one sends messages and the other responds. To scale up the network, add more pairs of peers.
</ul>

<h2>Usage</h2>
<p>To run this project successfully, you will need at least 4 computers available that have Java 17 installed. Check which version of Java you have installed by running <code>java --version</code> in command prompt (windows) or terminal (Linux, Mac). Once those requirements are met, follow the instructions below for each computer.</p>

<h3>All</h3>
<p>Do the following on ALL computers that will be used:</p>
<ol>
    <li>Open command prompt (Windows) or terminal (Linux, Mac)</li>
    <li>Make note of your local IPv4 (i.e. 192.168.0.27) address, which you can find with <code>ipconfig</code>(windows) or <code>ifconfig -a | grep "inet "</code>(Linux, Mac)</li>
    <li>Find or create a folder to download the program's files into</li>
    <li>Change your directory in command prompt or terminal to the folder (command: <code>cd [path]</code>)</li>
</ol>

<h3>Routers</h3>
On each computer that will act as a router, do the following:

<h6><u>Router #1</u> (receiver)</h6>
<ol>
    <li>Download <b>Router.jar</b> and <b>config-router1.yml</b> from the latest release into the folder</li>
    <li>
        Edit the downloaded .yml file and, if necessary, change:
        <ol>
            <li><code>router-ip</code> to the IP address of <u>Router #2</u></li>
            <li><code>router-port</code> to the port number of <u>Router #2</u></li>
            <li><code>port</code> to any open port number for <u>Router #1</u></li>
        </ol>
    </li>
    <li>Run the command <code>java -jar Router.jar 1</code> in command prompt or terminal</li>
</ol>

<h6><u>Router #2</u> (sender)</h6>
<ol>
    <li>Download <b>Router.jar</b> and <b>config-router2.yml</b> from the latest release into the folder</li>
    <li>
        Edit the downloaded .yml file and, if necessary, change:
        <ol>
            <li><code>router-ip</code> to the IP address of <u>Router #1</u></li>
            <li><code>router-port</code> to the port number of <u>Router #1</u></li>
            <li><code>port</code> to any open port number for <u>Router #2</u></li>
        </ol>
    </li>
    <li>Run the command <code>java -jar Router.jar 2</code> in command prompt or terminal</li>
</ol>



<h3>Peers</h3>
<p>On the computers that will act as peers, do the following for each pair of peers:</p>
<h6><u>Peer #1</u> (receiver)</h6>
<ol>
    <li>Download <b>Peer.jar</b> and <b>config-peer1.yml</b> from the latest release into a folder</li>
    <li>
        Edit the downloaded .yml file and, if necessary, change:
        <ol>
            <li><code>router-ip</code> to the IP address of <u>Router #1</u></li>
            <li><code>router-port</code> to the port number of <u>Router #1</u></li>
            <li><code>port</code> to the port number of <u>Peer #2</u></li>
            <li><code>destination</code> to the name of <u>Peer #2</u></li>
            <li><code>name</code> to an appropriate name for <u>Peer #1</u></li>
        </ol>
    </li>
    <li>Run the command <code>java -jar Peer.jar 1</code> in command prompt or terminal</li>
</ol>

<h6><u>Peer #2</u> (sender)</h6>
<ol>
    <li>Download <b>Peer.jar</b>, <b>config-peer2.yml</b>, and <b>file.txt</b> from the latest release into a folder</li>
    <li>
        Edit the downloaded .yml file and, if necessary, change:
        <ol>
            <li><code>router-ip</code> to the IP address of <u>Router #2</u></li>
            <li><code>router-port</code> to the port number of <u>Router #2</u></li>
            <li><code>port</code> to the port number of <u>Peer #1</u></li>
            <li><code>destination</code> to the name of <u>Peer #1</u></li>
            <li><code>name</code> to an appropriate name for <u>Peer #2</u></li>
        </ol>
    </li>
    <li>Run the command <code>java -jar Peer.jar 2 &lt;file&gt;</code> in command prompt or terminal, replacing <code>&lt;file&gt;</code> with the name of a text file (like <code>file.txt</code>)</li>
</ol>

<h2>Output</h2>
<p>If successful, you should see something similar to the following messages...</p>
<details>
    <summary><b><u>Router #1</u></b></summary>
    <blockquote>
        ServerRouter1 is listening on port 5555...<br/>
        Accepted connection from ServerRouter2 @ 127.0.0.1:5555!<br/>
        <br/>
        ServerRouter1 is listening on port 5555...<br/>
        Accepted connection from Peer @ 127.0.0.1:5555<br/>
        <br/>
        #0. ServerRouter1 <--- Peer (greeting): A1<br/>
        #0. ServerRouter1 ---> A1 (greeting): Hello, A1 @ 127.0.0.1:62793!<br/>
        #0. ServerRouter1 <--- A1 (destination): B4<br/>
        ServerRouter1 is listening on port 5555...<br/>
        #0. Waiting 10 seconds...<br/>
        #0. Asking ServerRouter2 for IP address of B4...<br/>
        #0. ServerRouter1 ---> ServerRouter2 (destination): B4<br/>
        #0. ServerRouter1 <--- ServerRouter2 (destination): A1<br/>
        #0. Finding IP address of A1 for ServerRouter2...<br/>
        #0. Routing lookup took 3700 ns<br/>
        #0. ServerRouter1 ---> ServerRouter2 (IP of A1): 127.0.0.1<br/>
        #0. ServerRouter1 <--- ServerRouter2 (IP of B4): 127.0.0.1<br/>
        #0. ServerRouter1 ---> A1 (IP of B4): 127.0.0.1
    </blockquote>
</details>
<details>
    <summary><b><u>Router #2</u></b></summary>
    <blockquote>
        Connected to ServerRouter1 @ 127.0.0.1:5555!<br/>
        ServerRouter2 is listening on port 5556...<br/>
        Accepted connection from Peer @ 127.0.0.1:5556!<br/>
        <br/>
        #0. ServerRouter2 <--- Peer (greeting): B4<br/>
        #0. ServerRouter2 ---> B4 (greeting): Hello, B4 @ 127.0.0.1:62784!<br/>
        #0. ServerRouter2 <--- B4 (destination): A1<br/>
        ServerRouter2 is listening on port 5556...<br/>
        #0. Waiting 10 seconds...<br/>
        #0. Asking ServerRouter1 for IP address of A1...<br/>
        #0. ServerRouter2 ---> ServerRouter1 (destination): A1<br/>
        #0. ServerRouter2 <--- ServerRouter1 (destination): B4<br/>
        #0. Finding IP address of B4 for ServerRouter1...<br/>
        #0. Routing lookup took 7700 ns<br/>
        #0. ServerRouter2 ---> ServerRouter1 (IP of B4): 127.0.0.1<br/>
        #0. ServerRouter2 <--- ServerRouter1 (IP of A1): 127.0.0.1<br/>
        #0. ServerRouter2 ---> B4 (IP of A1): 127.0.0.1
    </blockquote>
</details>
<details>
    <summary><b><u>Peer #1</u></b></summary>
    <blockquote>
        Connected to ServerRouter @ 127.0.0.1:5556!<br/>
        B4 ---> ServerRouter (greeting): B4<br/>
        B4 <--- ServerRouter (greeting): Hello, B4 @ 127.0.0.1:62784!<br/>
        B4 ---> ServerRouter (destination): A1<br/>
        B4 <--- ServerRouter (destination IP): 127.0.0.1<br/>
        Disconnected from ServerRouter!<br/>
        <br/>
        B4 is listening on port 5550...<br/>
        Accepted connection from A1 @ 127.0.0.1:5550!<br/>
        <br/>
        B4 <--- A1 (message): Genesis 1:1 In the beginning ... coffin in Egypt.<br/>
        B4 ---> A1 (response): GENESIS 1:1 IN THE BEGINNING ... COFFIN IN EGYPT.<br/>
        Disconnected from A1!
    </blockquote>
</details>
<details>
    <summary><b><u>Peer #2</u></b></summary>
    <blockquote>
        Connected to ServerRouter @ 127.0.0.1:5555!<br/>
        A1 ---> ServerRouter (greeting): A1<br/>
        A1 <--- ServerRouter (greeting): Hello, A1 @ 127.0.0.1:62793!<br/>
        A1 ---> ServerRouter (destination): B4<br/>
        A1 <--- ServerRouter (destination IP): 127.0.0.1<br/>
        Disconnected from ServerRouter!<br/>
        <br/>
        Connected to B4 @ 127.0.0.1:5550!<br/>
        A1 ---> B4 (message): Genesis 1:1 In the beginning ... coffin in Egypt.<br/>
        A1 <--- B4 (response): GENESIS 1:1 IN THE BEGINNING ... COFFIN IN EGYPT.<br/>
        Message size: 218445 bytes<br/>
        Cycle time: 23.12 ms<br/>
        Disconnected from B4!
    </blockquote>
</details>