package top.popko.agentdemo.flow;
import java.util.HashSet;
import java.util.Set;

/**
 * 有向图
 *
 * @author yjw
 * @date 2019/5/15/015
 */
@SuppressWarnings("unchecked")
public class DiGraph {
    /**
     * 顶点数
     */
    private int vertexNum;
    /**
     * 边数
     */
    private int edgeNum;

    private Set<Integer>[] adj;

    public DiGraph(int v) {
        this.vertexNum = v;
        this.edgeNum = 0;
        adj = (Set<Integer>[]) new HashSet[vertexNum];
        /**
         * 代表顶点0到v-1
         */
        for (int i = 0; i < v; i++) {
            adj[i] = new HashSet<>();
        }
    }


    public int vertexNum() {
        return vertexNum;
    }

    public int edgeNum() {
        return edgeNum;
    }

    /**
     * 增加v->w的边
     * @param v
     * @param w
     */
    public void addEdge(int v,int w) {
        /**
         * 有向图注意方向，并且防止重复加入
         */
        if (adj[v].add(w)) {
            edgeNum++;
        }
    }

    public void addEdge(int ... edges) {
        if (edges.length % 2 != 0) {
            throw new IllegalStateException("顶点数必须为2的整数倍");
        }
        for (int i = 0; i < edges.length -1; i+=2) {
            addEdge(edges[i],edges[i+1]);
        }
    }

   /**
     * 增加有向边的快速方法
     * @param start 起始节点
     * @param ends 结束节点数组
     */
    public void addDiEdges(int start,int ... ends) {
        for (int w : ends) {
            addEdge(start,w);
        }
    }

    /**
     * 返回由v出发指向的顶点集合(v作为起始节点)
     * @param v
     * @return
     */
    public Iterable<Integer> adj(int v) {
        return adj[v];
    }

    /**
     * 逆转有向图中箭头的方向
     * @return
     */
    public DiGraph reverse() {
        DiGraph g = new DiGraph(vertexNum);
        for (int i = 0; i < vertexNum; i++) {
            for (int w: adj(i)) {
                g.addEdge(w,i);
            }
        }
        return g;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("(" + vertexNum + " vertices, " + edgeNum + " edges)\n");
        for (int v = 0; v < vertexNum; v++) {
            s.append(v).append(": ");
            for (int w: this.adj(v)) {
                s.append(w).append(" ");
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        DiGraph dg = new DiGraph(6);//六个顶点
        dg.addEdge(0,2,2,0,2,1,2,3,3,2,3,4,3,5);//n/2条边
        System.out.println(dg);
    }
}

