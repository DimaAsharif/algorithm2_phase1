import java.util.*;

/*
 * Dima Almagayshi 
 * Aseel Alshahrani
 * Mona Alharbi
 */


public class experament {
  
    static class HeapNode {

        int vertex;
        int key;
    }

    static class MST {

        int parent;
        int weight;
    }
    
    static class Edge {

        int source;
        int destination;
        int weight;

        /**Edge constructor*/
        public Edge(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }
    }

    /** shows a menu for user to performe experaments
     * it loops until the user chooses to exit
     * @param args
     */
    public static void main(String[] args) {
        
        int v=0,e=0;
        Scanner input = new Scanner(System.in);
        System.out.println(" Runtime of Different Minimum Spanning Tree Algorithms "
                         +" \n-------------------------------------------------------"
                         + "\n1- Test Kruskal's Algorithm and Prim's Algorithm based on Priority Queue"
                         + "\n2- Test Prim's Algorithm based on Min Heap and Prim's Algorithm based on Priority Queue" 
                         + "\n3- To exit");
        
        while(true){
            System.out.print("\n> Enter your test choice : ");
            
            
            //user's choice of experament
            int testType = input.nextInt();
            if(testType==3){
                break;
            }
        
            System.out.println("> Choose input case ( v = vertices and e = edges): ");
            System.out.print(" 1- v=1,000 - e=10,000\n"+
                           " 2- v=1,000 - e=15,000\n"+
                           " 3- v=1,000 - e=25,000\n"+
                           " 4- v=5,000 - e=15,000\n"+
                           " 5- v=5,000 - e=25,000\n"+
                           " 6- v=10,000 - e=15,000\n"+
                           " 7  v=10,000 - e=25,000\n"+
                           " 8- v=20,000 - e=200,000\n"+
                           " 9- v=20,000 - e=300,000\n"+
                           "10- v=50,000 - e=1,000,000\n\n"+
                           "> Enter an input case to perform the experament: ");
            
            //user's choice of input case
            int userIntput = input.nextInt();
            System.out.println();
            switch (userIntput) {
                case 1: 
                    v=1000; e=10000;
                break;
                case 2: 
                    v=1000; e=15000;
                break;
                case 3: 
                    v=1000; e=25000;
                break;
                case 4: 
                    v=5000; e=15000;
                break;
                case 5: 
                    v=5000; e=25000;
                break;
                case 6: 
                    v=10000; e=15000;
                break;
                case 7: 
                    v=10000; e=25000;
                break;
                case 8: 
                    v=20000; e=200000;
                break;
                case 9: 
                    v=20000; e=300000;
                break;
                case 10: 
                    v=50000; e=1000000;
                break;
                default:
                    System.out.println("Invalid input case!");
            }
            
            //create a Graph g
            Graph g = new Graph(v, e);
            g.makeGraph(g);
        
            switch(testType){ 
                case 1:{
                    g.kruskalMST();
                    g.primPQ();
                } 
                break;
                case 2:{
        
                    g.primMH();
                    g.primPQ();
                }
                break;
            }
        }
    }  
}
