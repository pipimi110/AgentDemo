package top.popko.agentdemo.flow;

//import com.algorithms.stack.Stack;

import java.util.Stack;

/**
 * @author yjw
 * @date 2019/5/20/020
 */
public class DepthFirstSearch {
    private boolean[] marked;
    private int[] edgeTo;


    public DepthFirstSearch(int vertexNum) {
        marked = new boolean[vertexNum];
        edgeTo = new int[vertexNum];
    }

    public void search(DiGraph g, int v) {
        marked[v] = true;
        for (int w : g.adj(v)) {
            if (!marked[w]) {
                edgeTo[w] = v;
                search(g, w);
            }
        }
    }

    public boolean hasPathTo(int v) {
        return marked[v];
    }


    public Stack<Integer> pathTo(int start, int v) {
//        if (!hasPathTo(v)) return null;//show()里已经调用hasPathTo()
        Stack<Integer> path = new Stack<>();
        for (int x = v; x != start; x = this.edgeTo[x]) {
            path.push(x);
        }
        path.push(start);
        return path;
    }

    public void showAll(int start) {
        for (int i = 0; i < marked.length; i++) {
            show(start, i);
        }
    }
    public void show(int start, int v) {
        if (hasPathTo(v)) {
            System.out.printf("%d to %d:  ", start, v);
            Stack stack = pathTo(start, v);
            while (!stack.empty()) {
                Integer x = (Integer) stack.pop();
                if (x == start) {
                    System.out.print(x);
                } else {
                    System.out.print("->" + x);
                }
            }
            System.out.println();
        } else {
            System.out.printf("%d to %d:  not connected\n", start, v);
        }
    }

    public void dfsTraverse(DiGraph g) {
        for (int i = 0; i < marked.length; i++) {
            if (!marked[i]) {
                System.out.println("[]search");
                search(g, i);
                for (int j = 0; j < marked.length; j++) {
                System.out.println("[]show");
                    show(i, j);
                }
            }
        }
    }

    public static void main(String[] args) {
        DiGraph g = new DiGraph(6);
        g.addEdge(0, 2, 2, 0, 2, 1, 2, 3, 3, 2, 3, 4, 3, 5);
        int start = 3;
        DepthFirstSearch dfs = new DepthFirstSearch(g.vertexNum());
//        dfs.dfsTraverse(g);
        dfs.search(g, start);
//        dfs.showAll(start);
        dfs.showAll(0);//错的结果
//        dfs.show(start, 0);
//        dfs.show(start, 1);
//        dfs.show(start, 2);

    }

}

