package com.aaron;

public class FibHeap {
    private int keyNum;
    private FibNode min;

    private class FibNode {
    	int name;
        int key;
        int degree;
        FibNode left;
        FibNode right;
        FibNode child;
        FibNode parent;
        boolean marked;
        public FibNode(int name,int key) {
        	this.name 	= name;
            this.key    = key;
            this.degree = 0;
            this.marked = false;
            this.left   = this;
            this.right  = this;
            this.parent = null;
            this.child  = null;
        }
    }

    public FibHeap() {
        this.keyNum = 0;
        this.min = null;
    }

    private void removeNode(FibNode node) {
        node.left.right = node.right;
        node.right.left = node.left;
    }

    private void addNode(FibNode node, FibNode root) {
        node.left        = root.left;
        root.left.right  = node;
        node.right       = root;
        root.left        = node;
    }

    private void insert(FibNode node) {
        if (keyNum == 0)
            min = node;
        else {
            addNode(node, min);
            if (node.key < min.key)
                min = node;
        }
        keyNum++;
    }

    public void insert(int name,int key) {
        FibNode node;

        node = new FibNode(name,key);
        insert(node);
    }

    private void catList(FibNode a, FibNode b) {
        FibNode tmp;
        tmp           = a.right;
        a.right       = b.right;
        b.right.left  = a;
        b.right       = tmp;
        tmp.left      = b;
    }

    public void union(FibHeap other) {
        if (other==null)
            return ;
        if((this.min) == null) {
            this.min = other.min;
            this.keyNum = other.keyNum;
            other = null;
        } else if((other.min) == null) {
            other = null;
        } else {
            catList(this.min, other.min) ;
            if (this.min.key > other.min.key)
                this.min = other.min;
            this.keyNum += other.keyNum;
            other = null;
        }
    }

    private FibNode extractMin() {
        FibNode p = min;
        if (p == p.right)
            min = null;
        else {
            removeNode(p);
            min = p.right;
        }
        p.left = p.right = p;
        return p;
    }

    private void link(FibNode node, FibNode root) {
        removeNode(node);
        if (root.child == null)
            root.child = node;
        else
            addNode(node, root.child);
        node.parent = root;
        root.degree++;
        node.marked = false;
    }

    private void consolidate() {
        int maxDegree = (int) Math.floor(Math.log(keyNum) / Math.log(2.0));
        int D = maxDegree + 1;
        FibNode[] cons = new FibNode[D+1];
        for (int i = 0; i < D; i++)
            cons[i] = null;
        while (min != null) {
            FibNode x = extractMin();
            int d = x.degree;
            while (cons[d] != null) {
                FibNode y = cons[d];
                if (x.key > y.key) {
                    FibNode tmp = x;
                    x = y;
                    y = tmp;
                }
                link(y, x);
                cons[d] = null;
                d++;
            }
            cons[d] = x;
        }
        min = null;
        for (int i=0; i<D; i++) {
            if (cons[i] != null) {
                if (min == null)
                    min = cons[i];
                else {
                    addNode(cons[i], min);
                    if ((cons[i]).key < min.key)
                        min = cons[i];
                }
            }
        }
    }

    public void removeMin() {
        if (min==null)
            return ;
        FibNode m = min;
        while (m.child != null) {
            FibNode child = m.child;
            removeNode(child);
            if (child.right == child)
                m.child = null;
            else
                m.child = child.right;
            addNode(child, min);
            child.parent = null;
        }
        removeNode(m);
        if (m.right == m)
            min = null;
        else {
            min = m.right;
            consolidate();
        }
        keyNum--;
        m = null;
    }

    public int minimum_key() {
        if (min==null)
            return -1;

        return min.key;
    }
    public int minimum_name(){
    	if (min==null)
            return -1;
        return min.name;
    }

    private void renewDegree(FibNode parent, int degree) {
        parent.degree -= degree;
        if (parent. parent != null)
            renewDegree(parent.parent, degree);
    }

    private void cut(FibNode node, FibNode parent) {
        removeNode(node);
        renewDegree(parent, node.degree);
        if (node == node.right) 
            parent.child = null;
        else 
            parent.child = node.right;
        node.parent = null;
        node.left = node.right = node;
        node.marked = false;
        addNode(node, min);
    }

    private void cascadingCut(FibNode node) {
        FibNode parent = node.parent;
        if (parent != null) {
            if (node.marked == false) 
                node.marked = true;
            else {
                cut(node, parent);
                cascadingCut(parent);
            }
        }
    }

    private void decrease(FibNode node, int key) {
        if (min==null ||node==null) 
            return ;
        if (key > node.key) {
            System.out.printf("decrease failed: the new key(%d) is no smaller than current key(%d)\n", key, node.key);
            return ;
        }
        FibNode parent = node.parent;
        node.key = key;
        if (parent!=null && (node.key < parent.key)) {
            cut(node, parent);
            cascadingCut(parent);
        }
        if (node.key < min.key)
            min = node;
    }

    private void increase(FibNode node, int key) {
        if (min==null ||node==null) 
            return ;
        if ( key <= node.key) {
            System.out.printf("increase failed: the new key(%d) is no greater than current key(%d)\n", key, node.key);
            return ;
        }
        while (node.child != null) {
            FibNode child = node.child;
            removeNode(child);
            if (child.right == child)
                node.child = null;
            else
                node.child = child.right;
            addNode(child, min);
            child.parent = null;
        }
        node.degree = 0;
        node.key = key;

        FibNode parent = node.parent;
        if(parent != null) {
            cut(node, parent);
            cascadingCut(parent);
        } else if(min == node) {
            FibNode right = node.right;
            while(right != node) {
                if(node.key > right.key)
                    min = right;
                right = right.right;
            }
        }
    }

    private void update(FibNode node, int key) {
        if(key < node.key)
            decrease(node, key);
        else if(key > node.key)
            increase(node, key);
        else
            System.out.printf("No need to update!!!\n");
    }
      
    public void update(int oldkey, int newkey) {
        FibNode node;
        node = search(oldkey);
        if (node!=null)
            update(node, newkey);
    }

    private FibNode search(FibNode root, int key) {
        FibNode t = root;
        FibNode p = null;
        if (root==null)
            return root;

        do {
            if (t.key == key) {
                p = t;
                break;
            } else {
                if ((p = search(t.child, key)) != null) 
                    break;
            }
            t = t.right;
        } while (t != root);

        return p;
    }

    private FibNode search(int key) {
        if (min==null)
            return null;

        return search(min, key);
    }

    public boolean contains(int key) {
        return search(key)!=null ? true: false;
    }

    private void remove(FibNode node) {
        int m = min.key;
        decrease(node, m-1);
        removeMin();
    }

    public void remove(int key) {
        if (min==null)
            return ;
        FibNode node = search(key);
        if (node==null)
            return ;
        remove(node);
    }

    private void destroyNode(FibNode node) {
        if(node == null)
            return;
        FibNode start = node;
        do {
            destroyNode(node.child);
            node = node.right;
            node.left = null;
        } while(node != start);
    }
     
    public void destroy() {
        destroyNode(min);
    }

    private void print(FibNode node, FibNode prev, int direction) {
        FibNode start=node;
        if (node==null)
            return ;
        do {
            if (direction == 1)
                System.out.printf("%8d(%d) is %2d's child\n", node.key, node.degree, prev.key);
            else
                System.out.printf("%8d(%d) is %2d's next\n", node.key, node.degree, prev.key);

            if (node.child != null)
                print(node.child, node, 1);
            prev = node;
            node = node.right;
            direction = 2;
        } while(node != start);
    }

    public void print() {
        if (min==null)
            return ;
        int i=0;
        FibNode p = min;
        System.out.printf("== 斐波那契堆的详细信息: ==\n");
        do {
            i++;
            System.out.printf("%2d. %4d(%d) is root\n", i, p.key, p.degree);
            print(p.child, p, 1);
            p = p.right;
        } while (p != min);
        System.out.printf("\n");
    }
}