import java.util.*;
import javafx.util.Pair;

/*
 * Dima Almagayshi 
 * Aseel Alshahrani
 * Mona Alharbi
 */

    public class Graph {

        int vertices;
        int edges;
        LinkedList<experament.Edge>[] adjacencylist;
        

        /**Graph constructor*/ 
        Graph(int vertices, int edges) {
            this.vertices = vertices;
            this.edges = edges;
            adjacencylist = new LinkedList[vertices];
            //initialize adjacency lists for all the vertices
            for (int i = 0; i < vertices; i++) {
                adjacencylist[i] = new LinkedList<>();
            }
        }

        static class MinHeap {

            int capacity;
            int currentSize;
            experament.HeapNode[] mH;
            //used to decrease the key
            int[] indexes; 

            
            public MinHeap(int n) {
              this.capacity = n;
                mH = new experament.HeapNode[n + 1];
                indexes = new int[n];
                mH[0] = new experament.HeapNode();
                mH[0].key = Integer.MIN_VALUE;
                mH[0].vertex = -1;
                currentSize = 0;
            }

            
            public void insert(experament.HeapNode x) {
                currentSize++;
                int idx = currentSize;
                mH[idx] = x;
                indexes[x.vertex] = idx;
                bubbleUp(idx);
            }

            
            public void bubbleUp(int pos) {
                int parentIdx = pos / 2;
                int currentIdx = pos;
                while (currentIdx > 0 && mH[parentIdx].key > mH[currentIdx].key) {
                    experament.HeapNode currentNode = mH[currentIdx];
                    experament.HeapNode parentNode = mH[parentIdx];

                    //swap the positions
                    indexes[currentNode.vertex] = parentIdx;
                    indexes[parentNode.vertex] = currentIdx;
                    swap(currentIdx, parentIdx);
                    currentIdx = parentIdx;
                    parentIdx = parentIdx / 2;
                }
            }

            
            public experament.HeapNode extractMin() {
                experament.HeapNode min = mH[1];
                experament.HeapNode lastNode = mH[currentSize];
                //update the indexes[] and move the last node to the top
                indexes[lastNode.vertex] = 1;
                mH[1] = lastNode;
                mH[currentSize] = null;
                sinkDown(1);
                currentSize--;
                return min;
            }

            
            public void sinkDown(int k) {
                int smallest = k;
                int leftChildIdx = 2 * k;
                int rightChildIdx = 2 * k + 1;
                if (leftChildIdx < heapSize() && mH[smallest].key > mH[leftChildIdx].key) {
                    smallest = leftChildIdx;
                }
                if (rightChildIdx < heapSize() && mH[smallest].key > mH[rightChildIdx].key) {
                    smallest = rightChildIdx;
                }
                if (smallest != k) {

                    experament.HeapNode smallestNode = mH[smallest];
                    experament.HeapNode kNode = mH[k];

                    //swap the positions
                    indexes[smallestNode.vertex] = k;
                    indexes[kNode.vertex] = smallest;
                    swap(k, smallest);
                    sinkDown(smallest);
                }
            }

            public void swap(int a, int b) {
                experament.HeapNode temp = mH[a];
                mH[a] = mH[b];
                mH[b] = temp;
            }

            public boolean isEmpty() {
                return currentSize == 0;
            }

            public int heapSize() {
                return currentSize;
            }
        }

        /**used to add a new edge
         *
         * @param source edge
         * @param destination edge
         * @param weight of edge
         */
        public void addEdge(int source, int destination, int weight) {
            experament.Edge e = new experament.Edge(source, destination, weight);
            adjacencylist[source].addFirst(e);
            
            //because graph is undirected graph
            e = new experament.Edge(destination, source, weight);
            adjacencylist[destination].addFirst(e); 
            
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        //Prim's Algorithm PQ

        /**
         * used to do prim's algorithm using PQ
         * on a random generated graph
         */
        public void primPQ() {
            
            //start time
            double startTime = System.currentTimeMillis();
            
            //To represent the vertices that are not in MST yet
            boolean[] set = new boolean[vertices];
            //for final result
            experament.MST[] mstPQ = new experament.MST[vertices];
            //used to know whether priority queue update is required
            int[] key = new int[vertices];  

            //Initialize all the keys to infinity and initialize resultSet for all the vertices
            for (int i = 0; i < vertices; i++) {
                key[i] = Integer.MAX_VALUE;
                mstPQ[i] = new experament.MST();
            }

            //sorting based on keys
            PriorityQueue<Pair<Integer, Integer>> pq = new PriorityQueue<>(vertices, new Comparator<Pair<Integer, Integer>>() {
                @Override
                public int compare(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) {
                    int key1 = p1.getKey();
                    int key2 = p2.getKey();
                    return key1 - key2;
                }
            }
            );


            //for first vertix in mst
            key[0] = 0;
            Pair<Integer, Integer> k1 = new Pair<>(key[0], 0);
            //add it to pq
            pq.offer(k1);
            mstPQ[0] = new experament.MST();
            //first vertix does not have a parent
            mstPQ[0].parent = -1;
            
            
            //while pq is not empty
            while (!pq.isEmpty()) {
                //extract the minmum 
                Pair<Integer, Integer> extractedPair = pq.poll();

                //extracted vertex
                int extractedV = extractedPair.getValue();
                set[extractedV] = true;

                //for all the adjacent vertices update the keys
                LinkedList<experament.Edge> list = adjacencylist[extractedV];
                for (int i = 0; i < list.size(); i++) {
                    experament.Edge e = list.get(i);
                    //if edge dest is not in mst
                    if (set[e.destination] == false) {
                        int dest = e.destination;
                        int newKey = e.weight;
                        //check if new key smaller than existing key
                        if (key[dest] > newKey) {
                            //add new key to the pq
                            Pair<Integer, Integer> p = new Pair<>(newKey, dest);
                            pq.offer(p);
                            //update the mstPQ for dest vertex
                            mstPQ[dest].parent = extractedV;
                            mstPQ[dest].weight = newKey;
                            key[dest] = newKey;
                        }
                    }
                }
            }
            
            //finish time
            double finishTime = System.currentTimeMillis();
            //total time 
            System.out.println("Total runtime of Prim's Algorithm Using PQ is : "+(finishTime-startTime)+" ms.");
            cost(mstPQ);
        }
        
        /**
         * to get the MST cost
         *
         * @param mstPQ 
         */
        public void cost(experament.MST[] mstPQ) {
            
            int total_min_weight = 0;
            
            for (int i = 1; i < vertices; i++) {
                total_min_weight += mstPQ[i].weight;
            }
            
            System.out.println("Total cost: " + total_min_weight);
        }
        
        //--------------------------------------------------------------------------------------------------------------------------------
        
        /**generate graph with random input
         *
         * @param graph To do the experament on
         */
        public void makeGraph(Graph graph) {
            
            Random random = new Random();
            // add edges to vertcises 
            for (int i = 0; i < vertices-1; i++) {
                    int w = random.nextInt(50) + 1;
                    addEdge(i,i+1,w);
                
            }
            
            //generate random graph with the remaining edges
            int remaning = edges- (vertices-1);
            
            for (int i = 0; i < remaning; i++) {
                
                int source = random.nextInt(graph.vertices);
                int dest = random.nextInt(graph.vertices);
                if (dest == source || isConnected(source, dest, graph.adjacencylist)) { // to avoid self loops and duplicate edges
                    i--;
                    continue;
                }
                
                //generate random weights for edges in range 1 to 50
                int weight = random.nextInt(50) + 1;
                //add edge to the graph
                addEdge(source, dest, weight);
            }
        }
        
        //-------------------------------------------------------------------------------------------------------------------------

        /**test of connectivity
         *
         * @param src edge
         * @param dest edge
         * @param allEdges of graph
         * @return true if connected
         */
        public boolean isConnected(int src, int dest, LinkedList<experament.Edge>[] allEdges) {
            for (LinkedList<experament.Edge> i : allEdges) {
                for (experament.Edge edge : i) {
                    if ((edge.source == src && edge.destination == dest) || (edge.source == dest && edge.destination == src)) {
                        return true;
                    }
                }
            }
            return false;
        }
        
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        /**
         *
         * @param parent
         */
        public void makeSet(int[] parent) {
            //creating a new element with a parent pointer to itself.
            for (int i = 0; i < vertices; i++) {
                parent[i] = i;
            }
        }

        /** search for vertex v in parent[]
         *
         * @param parent
         * @param v vertex
         * @return its location in the array
         */
        public int find(int[] parent, int v) {
            if (parent[v] != v) {
                return find(parent, parent[v]);
            }
            return v;
        }

        /**Does union for the vertex 
         *
         * @param parent
         * @param x
         * @param y
         */
        public void union(int[] parent, int x, int y) {
            int xSetParent = find(parent, x);
            int ySetParent = find(parent, y);
            parent[ySetParent] = xSetParent;
        }
         
        /**used to do kruskal's algorithm using PQ
         * on a random generated graph
         */
        public void kruskalMST() {
            
            //start time
            double startTime = System.currentTimeMillis();
            LinkedList<experament.Edge>[] allEdges = adjacencylist.clone(); 
            PriorityQueue<experament.Edge> pq = new PriorityQueue<>(edges, Comparator.comparingInt(o -> o.weight));

            //add all edges to priority queue
            //sort the edges based on their weights
            for (int i = 0; i < allEdges.length; i++) {
                for (int j = 0; j < allEdges[i].size(); j++) {
                    pq.add(allEdges[i].get(j));
                }
            }
                         
            //create int array of parents
            int[] parent = new int[vertices];

            //makeset
            makeSet(parent);
            
            //create linked list to store mst
            LinkedList<experament.Edge> mstK = new LinkedList<>();

            //loop while edges=vertices-1 and pq not empty
            int i = 0;
            while (i < vertices - 1 && !pq.isEmpty()) {
                experament.Edge e = pq.remove();
                
                //test if adding this edge creates a cycle
                int x = find(parent, e.source);
                int y = find(parent, e.destination);
                
                if (x != y) {
                    //add it to final result
                    mstK.add(e);
                    i++;
                    union(parent, x, y);
                }
            }
            
            //finish time
            double finishTime = System.currentTimeMillis();
            //total time 
            System.out.println("Total runtime of Kruskal's Algorithm is: "+(finishTime-startTime)+" ms.");
        
            costK(mstK);
        }
        
        /**evaluate total cost of kruskal
         *
         * @param mstK
         */
        public void costK(LinkedList<experament.Edge> mstK) {
            int cost = 0;
            for (int i = 0; i < mstK.size(); i++) {
                experament.Edge e = mstK.get(i);
                cost += e.weight;
            }
            System.out.println("Total Cost: " + cost+"\n");
        }


        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        
        /**used to do prim's algorithm using min heap
         * on a random generated graph
         *
         */
        public void primMH() {
            
            //start time
            double stime = System.currentTimeMillis();
            
            //To represent the vertices that are not in MST yet
            boolean[] set = new boolean[vertices];
            //for final result
            experament.MST[] mstMH = new experament.MST[vertices];
            //used to know whether min heap update is required
            int[] key = new int[vertices];
            
            
            //create heapNode for all vertices
            experament.HeapNode[] heapNodes = new experament.HeapNode[vertices];
            
            // Initialize all keys
            for (int i = 0; i < vertices; i++) {
                heapNodes[i] = new experament.HeapNode();
                heapNodes[i].vertex = i;
                heapNodes[i].key = Integer.MAX_VALUE;
                mstMH[i] = new experament.MST();
                mstMH[i].parent = -1;
                set[i] = true;
                key[i] = Integer.MAX_VALUE;
            }

            //decrease the key for the first index
            heapNodes[0].key = 0;

            //add all the vertices to the MinHeap
            MinHeap MH = new MinHeap(vertices);
            for (int i = 0; i < vertices; i++) {
                MH.insert(heapNodes[i]);
            }

            //while min Heap is not empty
            while (!MH.isEmpty()) {
                //extract the minmum
                experament.HeapNode extractedNode = MH.extractMin();

                //extracted vertex
                int extractedVertex = extractedNode.vertex;
                set[extractedVertex] = false;

                //loop through all the adjacent vertices
                LinkedList<experament.Edge> list = adjacencylist[extractedVertex];
                for (int i = 0; i < list.size(); i++) {
                    experament.Edge e = list.get(i);
                    //test if edge destination is present in heap
                    if (set[e.destination]) {
                        int destination = e.destination;
                        int newKey = e.weight;
                        //check if updated key smaller than existing key
                        if (key[destination] > newKey) {
                            decreaseKey(MH, newKey, destination);
                            //update the parent node for dest vertex
                            mstMH[destination].parent = extractedVertex;
                            mstMH[destination].weight = newKey;
                            key[destination] = newKey;
                        }
                    }
                }
            }
            
            //finish time of the algorithm
            double ftime = System.currentTimeMillis();
            //total time 
            System.out.println("Total runtime of Prim's Algorithm Using Min Heap is : "+(ftime-stime)+" ms.");
            cost(mstMH);
        }
        
        /** it replaces the node's (v) key with newKey
         *
         * @param minHeap
         * @param newKey
         * @param v vertex
         */
        public void decreaseKey(MinHeap minHeap, int newKey, int v) {

            //get the index which key's needs a decrease
            int index = minHeap.indexes[v];

            //get the node and update its value
            experament.HeapNode node = minHeap.mH[index];
            node.key = newKey;
            minHeap.bubbleUp(index);
        }
    }
