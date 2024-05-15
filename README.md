# Spiderverse

The purpose of this assignment is to practice the understanding of Undirected Graphs and Adjacency Lists.

## Overview 

Welcome to the Spider-Verse!

In the Spider-Verse there are many different Dimensions and just as many Spider-People. As you would’ve guessed, with great power comes great responsibility and these Spiders are tasked with protecting their Dimension from any threat that may hurt anyone.

To help visualize the Spider-Verse, we can make a graph of all the different Dimensions, with some holding any number of Spiders and any number of anomalies. Anomalies are people (mainly villains) who have been sent to a different Dimension as the result of the mob boss named Kingpin running dangerous Particle Collider experiments in Dimension 1610.

Dimension 1610 is home to Miles Morales, one of the many Spider-men, and is also the source of the anomalies. We need to set them straight along with finding out some useful information about graphs along the way.

### Methods

#### 1. Clusters.java

Kingpin is up to no good, and is making a Particle Collider to bridge the gap between the dimensions so he can find his lost wife and child. However, Kingpin is reckless, and his collider is merging all the dimensions towards his own in the form of a chain of dimensions stemming from his. This will destroy not only the dimension he, Miles, and yourself reside in, but will also destroy the other dimensions. To prevent Kingpin’s Collider from destroying the Spider-Verse, Miles has tasked you with exploring the dimensional connections, and determining where they may be linked. We will find which are connected by inserting into a separate chaining hash table, creating clusters of dimensions.

The dimensions that collide (this happens because of the hash function used) will be at the same cluster (same array index). 

* This Java class will take two command line arguments in the following order: a dimensions list input file name and an output file name.
* The dimensions input file will be formatted as follows:
      * The first line consists of three numbers, space-separated:
           * An integer a (the number of dimensions)
           * An integer b (the initial size of the hash table prior to rehashing)
           * A double c (the capacity or threshold used to determine when to rehash the hash table)
      * a lines, each with
           * The dimension number (an integer)
           * The number of canon events (an integer)
           * The dimension weight (an integer)

**To complete this task, I will:**

1) Create a separate-chaining hash table to represent clusters. The initial size of the table is **b, but this size will change later.**

* Our goal is to insert each dimension into the cluster hash table based on its hash function and our algorithm below. Then, we will use these clusters similar to an edge list to fill in   
  our adjacency list later in Collider.java
* The dimensions that collide will be at the same cluster (same hash table index).

2) For each of the a dimensions:

1. Add the dimension to the cluster table: the index of the list it will be placed in can be calculated by taking DimensionNumber % TableSize. **When inserting items into their table 
   index, add to the front of the list.**
2. IF the current load of the cluster table (dimensions added so far / number of clusters so far) meets or exceeds the dimension threshold c, you need to rehash to prevent the Collider and 
   Kingpin from destroying the Spider-Verse!
       1. The size of the cluster table is doubled to accommodate the increase in dimensions.
       2. Because we are rehashing the table, items must be re-indexed to your new list according to the new list size.
3. Once the cluster table is finished, Miles and I need to ensure that these dimensions are connected between different clusters:
       1. For every cluster in the table, I will take the first dimension of the previous two clusters, and add them to the end of the current cluster.
       2. For the first two clusters, their “previous” clusters wrap around to the end of the list.
   
Output file format for clusters.out is as follows:

* n lines containing the first dimension of each cluster followed by all other dimensions in order, space separated
 

Compile command:  **javac -d bin src/spiderman/*.java**

Run command: **java -cp bin spiderman.Clusters dimension.in clusters.out**

**Below is the expected output for Clusters.java when running with arguments “dimension.in clusters.out”**

![image](https://github.com/yugalnshah/Spiderverse/assets/162384655/97b51823-7aa1-452f-9cf8-a42cde95b577)

#### 2. Collider.java

With the dimensional connections mapped out via our separate chaining hash table, we can use this to create a final Adjacency List in order to more efficiently navigate the Spider-Verse and track down anomalies. 

* This Java class will take three command line arguments in the following order: a dimensions list input file name, a spiderverse input file name, and an output file name.
* The dimensions input file will be formatted the same as above.
* The spiderverse input file will be formatted as follows:
      * An integer d (the number of people in the file)
      * d lines, each with
           * The dimension this person is currently at (an integer)
           * The name of the person (a String)
           * The dimensional signature of the person (an integer)

1. With the clusters being completed, Miles and I need to represent these connections between each Dimension as an **adjacency list showing undirected links.**
      * Let the first Dimension in a cluster be linked to every Dimension in its cluster and have a link going back to represent it being undirected. More formally, in the adjacency list 
        there must exist an edge from first→ d and d → first for each dimension appearing in a cluster starting at first.
      * So edges exist from d1 → d2 and d2 → d1, d1 → d3 and d3 → d1, ………….. , d1 → dn and dn → d1 (where dn is the last dimension in the cluster)

2. Insert people from the Spiderverse input file into their corresponding dimensions. They belong to a dimension, they’re not connected via edges.

      * The output file will be formatted as follows:
           * a lines, where each line starts with a dimension number, then lists all the dimensions linked to that dimension (space separated)
           * The order in which I output the lines DOES NOT MATTER.

Compile command: **javac -d bin src/spiderman/*.java**

Run command: **java -cp bin spiderman.Collider dimension.in spiderverse.in collider.out**

Below is one example of a correct “collider.out” file obtained from running the Collider.java file with the command line arguments “dimension.in” “spiderverse.in” and “collider.out” in that order.

![image](https://github.com/yugalnshah/Spiderverse/assets/162384655/bd3bb6ee-748b-4f87-a8d8-2bafe6d44563)

#### 3. TrackSpot.java

Gwen was tasked with stopping a villain known as The Spot, who has the power to jump across dimensions. Gwen got carried away spending time with Miles, and as a result, The Spot has jumped to a different unknown Dimension.

The Spot is trying to become stronger so he can defeat Miles and Gwen, and to do so he needs to find a Dimension with a Collider. Since he is still learning, 
The Spot can only naively use his powers to jump through dimensions. He does not know where a Collider may be, which results in a potentially long and sub-optimal route, as he jumps to the next immediate Dimension in the Dimensional Adjacency List. This means that his search path takes the form of a Depth First Search (DFS).

Once Gwen finds out The Spot has left the Dimension she is currently in, she uses an AI hologram named Lyla to track The Spot to his current Dimension.

* This Java class will take four command line arguments in the following order: a dimension list input file name, a spiderverse input file name, a spot input file name and an output file 
  name.
* The dimension list input file and spiderverse input file will be formatted exactly as the ones from Collider.
* The spot input file will be formatted as follows:

* 1 line containing the number for the initial dimension
* 1 line containing the number for the destination dimension

I will use the graph described by the Dimensional Adjacency List to recreate the route that Spot took from the initial dimension to get to the destination dimension **via a depth-first search traversal.**

Print the route that Spot takes to an output file:

* The output file will be formatted as follows:
* One line, listing the dimensional number of each dimension Spot has visited (space separated)

Here is the correct “trackspot.out” file obtained from running the TrackSpot.java file with the command line arguments “dimension.in”, “spiderverse.in”, “spot.in” and “trackspot.out” in that order.

![image](https://github.com/yugalnshah/Spiderverse/assets/162384655/af3b5cbe-e324-4ca9-8808-6d00960cff5d)

#### 4. CollectAnomalies.java

Because of Kingpin’s Collider experiments in Miles’ home dimension, anomalies are appearing in other Dimensions. An anomaly is someone who is in a Dimension that they do not belong to (in accordance with their dimensional signature and the Dimensions’ dimensional number).

The leader of the inter-dimensional Spider-Society, named Miguel O’Hara and known as Spider-man 2099, is trying to stop any anomalies who are wreaking havoc in the Dimensions they do not belong in. He believes it is his responsibility to protect these Dimensions, as enough damage from anomalies could cause the Dimension to cease to exist, or even the Spider-Verse as a whole.

For Miguel to accomplish his goal, he has created a hub in his home dimension, and recruits Spiders from other Dimensions to help him track down these anomalies, bring them back to the hub, and return them to their own Dimension.

* This Java class will take four command line arguments in the following order: a dimension list input file name, a spiderverse input file name, a hub input file name and an output file name.
* The dimension list input file and spiderverse input file will be formatted exactly as the ones from Collider.
* The hub input file will be formatted as follows:

* 1 line, containing dimensional number of the starting hub

You and Miguel want to stop and bring in any anomalies you find, but are not sure which dimensions they may reside, or the best route to take. To solve this problem, you and Miguel have decided to use a Breadth First Search (BFS) to find the best routes which contain anomalies.

* A Spider is someone whose Dimensional Signature matches the Dimension Number of where they are located.
* An Anomaly is someone whose Dimensional Signature DOES NOT match the Dimension Number of where they are located.
      * **For this file, we will IGNORE Anomalies located at the hub.**

Find the best route from the hub, to any anomaly, and back to the hub. You can do this for all anomalies in the Spider-Verse using BFS. If there is a Spider at that dimension with the anomaly, return ONLY the route going back to the hub (reverse of the route from hub –> anomaly). In both instances, the current Dimension of these anomalies and Spiders will be changed to the source where the hub is located to be sent back home in a later method. Recall that BFS finds the shortest path from a source vertex to every other vertex with respect to the number of edges (hops).

* The output file will be formatted as follows:
* **e** lines, listing the name of the anomaly collected and the name of the Spider who is at the same dimension (if one exists, space separated) followed by the dimensional number of 
  each dimension in the route.

Here is one example of a correct “collected.out” file obtained from running the ColllectAnomalies.java file with the command line arguments “dimension.in”, “spiderverse.in”, “hub.in” and “collected.out” in that order.

![image](https://github.com/yugalnshah/Spiderverse/assets/162384655/a0bd7b30-7a22-475b-9856-64c4201e5ea6)

#### 5. GoHomeMachine.java

With all of the anomalies collected and brought to the hub dimension, you are one step closer to fixing the mess Kingpin caused with the Collider. Now that you have them collected, Miguel wants to use a device called the Go Home Machine. Unlike when we were collecting the anomalies, this machine uses Dijkstra’s algorithm. With this Go Home Machine, we can identify each anomaly’s dimensional signature and use the sum of any two adjacent dimensions weights to find out how long it takes to travel between dimensions. With this additional info we can send the anomaly home as quickly as possible via the Go Home Machine.

While Miles was in the hub dimension, he was deemed as an anomaly. Thus, Miguel wanted to keep him there to prevent damage to the dimensional timelines. But Miles managed to escape and used the Go Home Machine to leave the hub. Miguel blames Gwen for this, and also sends her back to her own dimension. 

* This Java class will take five command line arguments in the following order: a dimension list input file name, a spiderverse input file name, a hub input file name, an anomalies input 
  file name and an output file name.
* The dimension list input file, spiderverse input file and hub input file will be formatted exactly as the ones from previous methods.
* The anomalies input file will be formatted as follows:

* An integer e (the number of anomalies in the file
* **e** lines, each with
     * The name of the anomaly which will go from the hub dimension to their home dimension
     * The time allotted to return the anomaly home before a canon event is missed

A canon event is a critical point in a Spider’s history, which relies on certain events (often tragic) taking place. Anomalies can disrupt, and even break these canon events, putting the Spider-verse in danger. So, we need to get these anomalies home before the time allotted (if possible).

* Remember, an anomaly is anyone whose Dimensional Signature DOES NOT match the Dimension Number of where they are currently located.

If the amount of time it takes to send the anomaly back home is greater than the time alotted, then the number of canon events will be decreased by one, signifying a canon event has been broken. To send these anomalies home:

**BEFORE RUNNING GoHomeMachine**: Run CollectAnomalies and make sure the current dimension of the Anomalies (and spiders, if applicable) are updated to the Hub before I run GoHomeMachine

1. Read each anomaly from the input file.
2. Use Dijkstra’s Algorithm to find the shortest path from the hub to every other Dimension. Each Dimension has a weight (given in the input file), and the weight of a connection between 
   two adjacent Dimensions is the sum of both individual dimension weights.
3. For all anomalies:
       1. If the hub dimension has the anomaly, remove them from the hub and add them home to their correct dimension (according to their dimensional signature).
       2. If the time to get home is larger than the anomaly’s allotted time:
              1. Decrease canon events by 1 according to their Dimensional Signature and current canon events at that time.
                     * **NOTE**: Canon event count will be ignored during grading, but must still exist and be updated.
              2. If dimension count is at or below 0, I should mark this dimension for deletion (for a later program).
              3. Getting this anomaly back home has unfortunately FAILED. This will be important when I create a report.
       3. Otherwise, we were SUCCESSFUL in getting the anomaly home.

Create a report with the findings and write the report to an output file.

* The output file will be formatted as follows:
* **e lines, each with**
      * The number of canon events at that anomalies home Dimension after being returned
      * The name of the anomaly
      * SUCCESS or FAILED in relation to whether or not that anomaly made it back in time
      * The route the anomaly took to get home

Here is one example of a correct “report.out” file obtained from running the GoHomeMachine.java file with the command line arguments “dimension.in”, “spiderverse.in”, “hub.in”, “anomalies.in” and “report.out” in that order.

![image](https://github.com/yugalnshah/Spiderverse/assets/162384655/4a73c976-1333-4e70-b135-38c14bb4b9e7)
