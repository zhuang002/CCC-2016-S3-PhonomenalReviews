/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phonomenalreviews;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

/**
 *
 * @author huang
 */
public class PhonomenalReviews {

    static HashMap<Integer, HashSet<Integer>> graph = new HashMap();
    static HashSet<Integer> phoRestaurants = new HashSet();
    static int N, M;
    static int aPhoRestaurant = -1;
    static HashMap<Int2DKey,Integer> getTreeDepthBackup=new HashMap();
    static HashMap<Int2DKey,Integer> getTreeDiameterBackup=new HashMap();
    static int trimSize=0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        N = sc.nextInt();
        M = sc.nextInt();
        
        for (int i = 0; i < M; i++) {
            aPhoRestaurant = sc.nextInt();
            phoRestaurants.add(aPhoRestaurant);
        }
        for (int i = 0; i < N - 1; i++) {
            int node1 = sc.nextInt();
            int node2 = sc.nextInt();
            
            addConnection(node1, node2);
            addConnection(node2, node1);

        }

        int diameter = getTreeDiameter();
        System.out.println((graph.size()-trimSize-1)*2-diameter);
    }

    private static void addConnection(int node1, int node2) {
        HashSet<Integer> connectedNodes;
        if (graph.containsKey(node1)) {
            connectedNodes = graph.get(node1);
            connectedNodes.add(node2);
        } else {
            connectedNodes = new HashSet();
            connectedNodes.add(node2);
            graph.put(node1, connectedNodes);
        }
    }

   

    private static int getTreeDiameter() {
        return getTreeDiameter(-1,aPhoRestaurant);
    }

    private static int getTreeDiameter(Integer root, Integer subRoot) {
        Int2DKey key=new Int2DKey(root,subRoot);
        if (getTreeDiameterBackup.containsKey(key))
            return getTreeDiameterBackup.get(key);
        
        HashSet<Integer> connectedNodes=graph.get(subRoot);
        if (root==-1 && connectedNodes.isEmpty() || root!=-1 && connectedNodes.size() ==1) {
            //if (!phoRestaurants.contains(subRoot)) trimSize++;
            return 0;
        }
        int maxDiameter=-1;
        int maxDepth1=-1;
        int maxDepth2=-1;
        for (Integer node:connectedNodes) {
            if (node==root) continue;
            int diameter=getTreeDiameter(subRoot,node);
            if (diameter>maxDiameter) maxDiameter=diameter;
            int depth=getTreeDepth(subRoot,node);
            if (depth>=maxDepth1) {
                maxDepth2=maxDepth1;
                maxDepth1=depth;
            } else if (depth>maxDepth2)
                maxDepth2=depth;
        }
        int retDiameter=Math.max(maxDiameter, maxDepth1+maxDepth2+2);
        getTreeDiameterBackup.put(key,retDiameter);
        return retDiameter;
    }

    private static int getTreeDepth(Integer root, Integer subRoot) {
        Int2DKey backupKey=new Int2DKey(root,subRoot);
        if (getTreeDepthBackup.containsKey(backupKey)) return getTreeDepthBackup.get(backupKey);
        
        int maxDepth=-1;
        HashSet<Integer> connectedNodes=graph.get(subRoot);
        
        for (Integer node:connectedNodes) {
            if (node==root) continue;
            int depth=getTreeDepth(subRoot,node);
            if (depth>maxDepth) maxDepth=depth;
        }
        if (maxDepth!=-1 || phoRestaurants.contains(subRoot)) {
            maxDepth++;
        }
        if (maxDepth==-1) trimSize++;
        getTreeDepthBackup.put(backupKey, maxDepth);
        return maxDepth;
    }
    
}

class Int2DKey {
    Integer i;
    Integer j;
    
    public Int2DKey(Integer x,Integer y){
        i=x;
        j=y;
    } 
    
    @Override
    public boolean equals(Object o) {
        return this.i.equals(((Int2DKey)o).i) && this.j.equals(((Int2DKey)o).j);
    }
    
    @Override
    public int hashCode() {
        int prime=31;
        int result=1;
        result=prime*result+i.hashCode();
        result=prime*result+j.hashCode();
        return result;
    }
}
