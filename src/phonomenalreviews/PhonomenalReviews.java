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

    /**
     * @param args the command line arguments
     */
    
    
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int M = sc.nextInt();

        Tree tree=readInTree(sc,N,M);
        int diameter=tree.getDiameter();
        int cutSize=tree.size();
        System.out.println((cutSize - 1) * 2 - diameter);
    }

    private static Tree readInTree(Scanner sc, int N, int M) {
        Tree tree=new Tree();
        
        for (int i=0;i<M;i++) {
            tree.phoRestaurants.add(sc.nextInt());
        }
        for (int i=0;i<N-1;i++) {
            int id1=sc.nextInt();
            int id2=sc.nextInt();
            
            Node node1=tree.getNode(id1);
            Node node2=tree.getNode(id2);
            node1.addLink(node2);
            node2.addLink(node1);
        }
        return tree;
    }

}

class Tree {
    HashMap<Integer,Node> idMap=new HashMap();
    HashSet<Integer> phoRestaurants=new HashSet();
    Node aPho=null;
    

    int size() {
        return this.idMap.size();
    }

    int getDiameter() {
        Node startNode=getRandomPhoNode();
        Node middleNode=getFurthestNode(startNode);
        Node endNode=getFurthestNode(middleNode);
        return endNode.info.depth;
    }

    private Node getRandomPhoNode() {
        return this.aPho;
    }

    private Node getFurthestNode(Node root) {
        root.info.depth=0;
        //HashSet<Integer> unvisitedPho=(HashSet<Integer>)this.phoRestaurants.clone();
        //if (root.isPho) unvisitedPho.remove(root.id);
        return getFurthestNode(null,root/*,unvisitedPho*/);
    }

    private Node getFurthestNode(Node parent, Node root/*, HashSet<Integer> unvisitedPho*/) {
        
        if (isLeaf(parent,root/*,unvisitedPho*/)) {
            if (!root.isPho) return null;
            if (parent!=null) {
                root.info.depth=parent.info.depth+1;
            } else {
                root.info.depth=0;
            }
            return root;
        }
            
        Node furthestNode=null;
        if (root.isPho) furthestNode=root;
        HashSet<Node> tobeRemoved=new HashSet();
        for (Node node:root.connectedNodes) {
            if (node==parent) continue;
            node.info.depth=root.info.depth+1;
            
            Node tmpFurthestNode=getFurthestNode(root,node/*,unvisitedPho*/);
            
            if (tmpFurthestNode==null) {
                tobeRemoved.add(node);
            } else {
                if (furthestNode==null) {
                    furthestNode=tmpFurthestNode;
                } else {
                       if (tmpFurthestNode.info.depth>furthestNode.info.depth)
                           furthestNode=tmpFurthestNode;
                }
            }
        }
        for (Node node : tobeRemoved) {
            remove(root,node);
        }
        return furthestNode;
    }

    private boolean isLeaf(Node parent, Node root/*, HashSet<Integer> unvisitedPho*/) {
        if (parent==null) {
            if (root.connectedNodes.isEmpty())
                return true;
            else return false;
        }
        if (root.connectedNodes.size()==1) return true;
        /*if (root.isPho) {
            unvisitedPho.remove(root.id);
        } 
        if (unvisitedPho.isEmpty())
            return true;*/
        return false;
    }

    Node getNode(int id) {
        if (this.idMap.containsKey(id)) return this.idMap.get(id);
        
        Node node=new Node(id);
        if (this.phoRestaurants.contains(id))
        {
            node.isPho=true;
            if (this.aPho==null) this.aPho=node;
        }
        this.idMap.put(id, node);
        
        return node;
    }

    private void remove(Node parent,Node node) {
        parent.connectedNodes.remove(node);
        idMap.remove(node.id);
    }
    
        
}

class Node {
    int id=-1;
    HashSet<Node> connectedNodes=new HashSet();
    NodeInfo info=new NodeInfo();
    boolean isPho=false;

    Node(int pId) {
        this.id=pId;
    }

    void addLink(Node node) {
        this.connectedNodes.add(node);
    }
}

class NodeInfo {
    int depth=-1;
    boolean tobeRemoved=false;
}


    