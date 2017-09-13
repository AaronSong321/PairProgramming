package com.aaron;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ListUDG {
    private static int INF = Integer.MAX_VALUE;
    private class ENode {
        int ivex;
        int weight;
        ENode nextEdge;
    }
    private class VNode {
        char data;
        ENode firstEdge;
    };
    private int mEdgNum;
    private VNode[] mVexs;
    
    public ListUDG(char[] vexs, EData[] edges) {
        int vlen = vexs.length;
        int elen = edges.length;
        mVexs = new VNode[vlen];
        for (int i = 0; i < mVexs.length; i++) {
            mVexs[i] = new VNode();
            mVexs[i].data = vexs[i];
            mVexs[i].firstEdge = null;
        }
        mEdgNum = elen;
        for (int i = 0; i < elen; i++) {
            char c1 = edges[i].start;
            char c2 = edges[i].end;
            int weight = edges[i].weight;
            int p1 = getPosition(c1);
            int p2 = getPosition(c2);
            ENode node1 = new ENode();
            node1.ivex = p2;
            node1.weight = weight;
            linkLast(p1, node1);
            ENode node2 = new ENode();
            node2.ivex = p1;
            node2.weight = weight;
            linkLast(p2, node2);
        }
    }
    public ListUDG(File file) throws NumberFormatException, FileNotFoundException{
    	BufferedReader reader = null;
    	try{
    		reader = new BufferedReader(new FileReader(file));
    		String tempstring = null;
    		tempstring = reader.readLine();
    		mVexs = new VNode[Integer.parseInt(tempstring)];
    		for (int i = 0; i < mVexs.length; i++){
    			mVexs[i] = new VNode();
    			mVexs[i].data = 'a';
    			mVexs[i].firstEdge = null;
    		}
    		tempstring = reader.readLine();
    		mEdgNum = Integer.parseInt(tempstring);
    		for (int i = 0; i < mEdgNum; i++){
    			tempstring = reader.readLine();
    			String[] s = tempstring.split("\t");
    			int vstart = Integer.parseInt(s[0]);
    			ENode newnode = new ENode();
    			newnode.ivex = Integer.parseInt(s[1]);
    			newnode.weight = Integer.parseInt(s[2]);
    			newnode.nextEdge = null;
    			linkLast(vstart, newnode);
    			
    			vstart = Integer.parseInt(s[1]);
    			newnode = new ENode();
    			newnode.ivex = Integer.parseInt(s[0]);
    			newnode.weight = Integer.parseInt(s[2]);
    			newnode.nextEdge = null;
    			linkLast(vstart, newnode);
    		}
    	} catch(IOException e){
    		e.printStackTrace();
    	} finally{
    		if (reader != null){
    			try{
    				reader.close();
    			} catch(IOException e1){
    				e1.printStackTrace();
    			}
    		}
    	}
    }
    private void linkLast(int startv, ENode node){
    	if (mVexs[startv].firstEdge == null) mVexs[startv].firstEdge = node;
    	else{
    		ENode end = mVexs[startv].firstEdge;
    		while (end.nextEdge != null) end = end.nextEdge;
    		end.nextEdge = node;
    	}
    }

    private int getPosition(char ch) {
        for(int i=0; i<mVexs.length; i++)
            if(mVexs[i].data==ch)
                return i;
        return -1;
    }

    public void dijkstra(int vs, int[] prev, int[] dist) {
    	FibHeap H = new FibHeap();
        boolean[] flag = new boolean[mVexs.length];
        for (int i = 0; i < mVexs.length; i++) {
            flag[i] = false;
            prev[i] = 0;
            dist[i] = INF;
        }
        dist[vs] = 0;
        H.insert(vs, 0);
        while(H.minimum_key() != -1) {
        	int u = H.minimum_name();
        	H.removeMin();
            if(flag[u])	continue;
            flag[u] = true;
            ENode p = mVexs[u].firstEdge;
            
            while(p != null){
            	int v = p.ivex;
            	if(flag[v]==false&&dist[v]>dist[u]+p.weight){
            		dist[v] = dist[u]+p.weight;
            		prev[v] = u;
            		H.insert(v, dist[v]);
            	}
            	p = p.nextEdge;
            }
        }
    }

    private static class EData {
        char start;
        char end;
        int weight;
    };

	public int length() {
		return mVexs.length;
	}
}
