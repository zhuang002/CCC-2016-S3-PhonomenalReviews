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
    static HashMap<String,Integer> getTreeDepthBackup=new HashMap();

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

        removeUnnecessaryNodes(aPhoRestaurant);
        int diameter = getTreeDiameter();
        System.out.println((graph.size()-1)*2-diameter);
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

    private static void removeUnnecessaryNodes(int root) {
        removeUnecessaryChildTree(null, root);
    }

    private static boolean removeUnecessaryChildTree(Integer root, Integer subRoot) {
        HashSet<Integer> subNodes = graph.get(subRoot);
        if (subNodes == null) {
            return true;
        }
        HashSet<Integer> toBeRemoved=new HashSet();
        for (Integer subNode : subNodes) {
            if (subNode==root) continue;
            
            if (removeUnecessaryChildTree(subRoot, subNode)) {
                toBeRemoved.add(subNode);
            }
        }
        for (Integer subNode:toBeRemoved) {
            removeSubtree(subNode);
        }

        if (subNodes.size() == 1 && !phoRestaurants.contains(subRoot)) {
            //removeSubtree(subRoot);
            return true;
        }
        return false;
    }    

    private static void removeSubtree(Integer subRoot) {
        HashSet<Integer> connectedNodes=graph.get(subRoot);
        for (Integer node : connectedNodes) {
            graph.get(node).remove(subRoot); 
        }
        graph.remove(subRoot);
    }

    private static int getTreeDiameter() {
        return getTreeDiameter(null,aPhoRestaurant);
        
    }

    private static int getTreeDiameter(Object root, int subRoot) {
        HashSet<Integer> connectedNodes=graph.get(subRoot);
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
        return Math.max(maxDiameter, maxDepth1+maxDepth2+2);
    }

    private static int getTreeDepth(Integer root, Integer subRoot) {
        String backupKey=root+"-"+subRoot;
        if (getTreeDepthBackup.containsKey(backupKey)) return getTreeDepthBackup.get(backupKey);
        
        int maxDepth=-1;
        HashSet<Integer> connectedNodes=graph.get(subRoot);
        for (Integer node:connectedNodes) {
            if (node==root) continue;
            int depth=getTreeDepth(subRoot,node);
            if (depth>maxDepth) maxDepth=depth;
        }
        maxDepth++;
        getTreeDepthBackup.put(backupKey, maxDepth);
        return maxDepth;
    }

}
