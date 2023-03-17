//package top.popko.agentdemo.flow;
//
///**
// * @author yjw
// * @date 2019/5/20/020
// */
//public class DirectedDFS {
//    private boolean[] marked;
//
//    public DirectedDFS(DiGraph g, int s) {
//        marked = new boolean[g.vertexNum()];
//        dfs(g, s);
//    }
//
//    /**
//     * 在有向图中找到从sources中的所有顶点可达的所有顶点
//     *
//     * @param g
//     * @param sources
//     */
//    public DirectedDFS(DiGraph g, Iterable<Integer> sources) {
//        marked = new boolean[g.vertexNum()];
//        for (int s : sources) {
//            if (!marked[s]) {
//                dfs(g, s);
//            }
//        }
//    }
//
//    private void dfs(DiGraph g, int v) {
//        marked[v] = true;
//        for (int w : g.adj(v)) {
//            if (!marked[w]) {
//                dfs(g,w);
//            }
//        }
//    }
//
//    /**
//     * 返回顶点v是从已知顶点(s或sources)中可达的吗
//     * @param v
//     * @return
//     */
//    public boolean marked(int v) {
//        return marked[v];
//    }
//
//    public static void main(String[] args) {
//        DiGraph dg = new DiGraph(6);
//        dg.addEdge(0,2,2,0,2,1,2,3,3,2,3,4,3,5);
//        DirectedDFS dfs = new DirectedDFS(dg,3);
//        for (int v = 0; v < dg.vertexNum(); v++) {
//            if (dfs.marked[v]) {
//                System.out.print(v + " ");
//            }
//        }
//        System.out.println();
//
//    }
//}
//
