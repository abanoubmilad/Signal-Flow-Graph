import java.io.Serializable;
import java.util.ArrayList;

public class Data implements Serializable{
	private ArrayList<ArrayList<String>> nodes;
	private ArrayList<ArrayList<String>> painted;
	public ArrayList<ArrayList<String>> getNodes() {
		return nodes;
	}
	public void setNodes(ArrayList<ArrayList<String>> nodes) {
		this.nodes = nodes;
	}
	public ArrayList<ArrayList<String>> getPainted() {
		return painted;
	}
	public void setPainted(ArrayList<ArrayList<String>> painted) {
		this.painted = painted;
	}
}
