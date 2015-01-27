import java.util.ArrayList;
import java.util.LinkedList;

public class Operate {

	private int maxNodes;
	private ArrayList<LinkedList<Integer>> nonTouching;
	private double[] nodesGain;
	private int[][] nodes;
	private int[] nodesNoFB;
	private int[][] loopsMask;
	private int[][] forwardsMask;

	private ArrayList<Double> sequencesGain;
	private ArrayList<LinkedList<Integer>> forwardPaths;
	private ArrayList<LinkedList<Integer>> loops;
	private ArrayList<LinkedList<Integer>> sequences;
	private ArrayList<Double> forwardPathsGain;
	private ArrayList<Double> loopsGain;

	public void setNodes(int[][] nodes) {
		this.nodes = nodes;

	}

	public void setNodesGain(double[] nodesGain) {

		this.nodesGain = nodesGain;

	}

	public void setMaxNodes(int maxNodes) {
		this.maxNodes = maxNodes;

	}

	public void generate() {
		nonTouching = new ArrayList<LinkedList<Integer>>();
		sequencesGain = new ArrayList<Double>();
		sequences = new ArrayList<LinkedList<Integer>>();
		loops = new ArrayList<LinkedList<Integer>>();
		forwardPaths = new ArrayList<LinkedList<Integer>>();
		forwardPathsGain = new ArrayList<Double>();
		loopsGain = new ArrayList<Double>();

		sortNodes(nodes);
		makeSequences();
		makeLoopsMask();
		makeForwardsMask();
		makeNonTouching();

	}

	public ArrayList<LinkedList<Integer>> getLoops() {
		return loops;
	}

	public ArrayList<LinkedList<Integer>> getForwardPaths() {
		return forwardPaths;
	}

	public ArrayList<LinkedList<Integer>> getnonTouching() {
		return nonTouching;
	}

	public double getTF() {

		double loopgains = 0;
		for (int i = 0; i < loopsGain.size(); i++) {
			loopgains += loopsGain.get(i);
		}
		double untouchgains = 0;

		int e = 2;
		double current;
		do {
			current = nthNontouch(e);
			if (e % 2 == 0)
				untouchgains += current;
			else
				untouchgains -= current;
			e++;

		} while (current != 0);

		double delta = 1 - loopgains + untouchgains;

		double result = 0;
		double deltaN;
		for (int i = 0; i < forwardPaths.size(); i++) {
			deltaN = 1;
			ArrayList<Integer> lop = deltaNLoops(i + 1);
			for (int j = 0; j < lop.size(); j++) {
				deltaN -= loopsGain.get(lop.get(j) - 1);
			}
			result += forwardPathsGain.get(i) * deltaN;
		}

		return result / delta;
	}

	private int occurance(int e) {
		int counter = 0;
		for (int i = 0; i < nodesNoFB.length; i++) {
			if (nodesNoFB[i] == e)
				counter++;
		}
		return counter;
	}

	private void makeNonTouching() {
		ArrayList<LinkedList<Integer>> tempo = new ArrayList<LinkedList<Integer>>();
		tempo = twoNontouch();
		for (int i = 0; i < tempo.size(); i++) {
			nonTouching.add(tempo.get(i));
		}
		tempo = threeNontouch();
		for (int i = 0; i < tempo.size(); i++) {
			nonTouching.add(tempo.get(i));
		}
		tempo = fourNontouch();
		for (int i = 0; i < tempo.size(); i++) {
			nonTouching.add(tempo.get(i));
		}
		for (int i = 0; i < tempo.size(); i++) {
			nonTouching.add(tempo.get(i));
		}

	}

	private void makeSequences() {

		LinkedList<Integer> seq;
		ArrayList<LinkedList<Integer>> seqList;
		ArrayList<Boolean> check = new ArrayList<Boolean>();

		for (int i = 0; i < nodes.length; i++) {

			if (nodes[i][0] == nodes[i][1]) {
				seq = new LinkedList<Integer>();
				seq.add(nodes[i][0]);
				seq.add(nodes[i][0]);
				loops.add(seq);
				loopsGain.add(nodesGain[i]);
			} else {
				boolean flag = true;
				seqList = new ArrayList<LinkedList<Integer>>();

				if (nodes[i][0] == 1) {
					int ocurances = occurance(nodes[i][1]);
					LinkedList<Integer> seq2 = new LinkedList<Integer>();
					seq2.add(nodes[i][0]);
					seq2.add(nodes[i][1]);
					if(ocurances==0)
						ocurances++;

					for (int j = 0; j < ocurances; j++) {
						sequencesGain.add(nodesGain[i]);
						sequences.add(new LinkedList<Integer>());
						sequences.get(sequences.size() - 1).addAll(seq2);

						check.add(true);
						flag = false;
					}
				}

				for (int j2 = 0; j2 < sequences.size(); j2++) {

					if (check.get(j2)) {
						if (sequences.get(j2).getFirst() == 1
								&& sequences.get(j2).getLast() == maxNodes) {
							 forwardPaths.add(sequences.get(j2));
							 forwardPathsGain.add(sequencesGain.get(j2));
							 check.set(j2, false);
						}else if(sequences.get(j2).getFirst() == sequences.get(j2)
								.getLast()) {
							loops.add(sequences.get(j2));
							loopsGain.add(sequencesGain.get(j2));
							check.set(j2, false);
						}else if( sequences.get(j2).getLast() == nodes[i][0]) {
							if (seqList.size() == 0
									|| seqNotIn(sequences.get(j2), seqList)) {

								LinkedList<Integer> seqTemp = new LinkedList<Integer>();
								seqTemp.addAll(sequences.get(j2));
								seqList.add(seqTemp);

								sequences.get(j2).addLast(nodes[i][1]);
								sequencesGain.set(j2, sequencesGain.get(j2)
										* nodesGain[i]);
			
								flag = false;

								if (sequences.get(j2).getFirst() == 1
										&& sequences.get(j2).getLast() == maxNodes) {
									
									forwardPaths.add(sequences.get(j2));
									 forwardPathsGain.add(sequencesGain.get(j2));
									 check.set(j2, false);

								}else if(sequences.get(j2).getFirst() == sequences.get(j2)
										.getLast()) {
									loops.add(sequences.get(j2));
									loopsGain.add(sequencesGain.get(j2));
									check.set(j2, false);
								}


							}

						}
					} 

				}
				if (flag) {
					int ocurances = occurance(nodes[i][1]);
					seq = new LinkedList<Integer>();
					seq.add(nodes[i][0]);
					seq.add(nodes[i][1]);
					if(ocurances==0)
						ocurances++;
					for (int j = 0; j < ocurances; j++) {
						sequencesGain.add(nodesGain[i]);
						sequences.add(new LinkedList<Integer>());
						sequences.get(sequences.size() - 1).addAll(seq);
						check.add(true);
					}
				}

			}
		}

	}

	private void makeLoopsMask() {

		loopsMask = new int[maxNodes][loops.size()];
		for (int i = 0; i < maxNodes; i++) {
			for (int j = 0; j < loops.size(); j++) {
				loopsMask[i][j] = 0;
			}
		}
		LinkedList<Integer> current;
		for (int i = 0; i < loops.size(); i++) {
			current = loops.get(i);
			for (int j = 0; j < current.size() && current.size() != 1; j++) {

				loopsMask[current.get(j) - 1][i] = 1;
			}
		}

	}

	private void makeForwardsMask() {

		forwardsMask = new int[maxNodes][forwardPaths.size()];
		for (int i = 0; i < maxNodes; i++) {
			for (int j = 0; j < forwardPaths.size(); j++) {
				forwardsMask[i][j] = 0;
			}
		}
		LinkedList<Integer> currentForwad;
		for (int i = 0; i < forwardPaths.size(); i++) {
			currentForwad = forwardPaths.get(i);
			for (int j = 0; j < currentForwad.size(); j++) {
				forwardsMask[currentForwad.get(j) - 1][i] = 1;
			}
		}

	}

	public ArrayList<Integer> deltaNLoops(int n) {
		ArrayList<Integer> nontouch = new ArrayList<Integer>();
		boolean flag = true;

		// making combinations of columns
		for (int e = 0; e < loops.size(); e++) {
			// looping over rows
			flag = true;

			for (int i = 0; i < maxNodes && flag; i++) {
				if (loopsMask[i][e] + forwardsMask[i][n - 1] > 1)
					flag = false;
			}
			if (flag) {
				nontouch.add(e + 1);

			}
		}
		return nontouch;
	}

	private boolean noNontouch() {
		if (loops.size() == 0)
			return true;

		int counter = 0;
		for (int i = 0; i < loopsMask.length; i++) {
			for (int j = 0; j < loopsMask[0].length; j++) {
				counter += loopsMask[i][j];
			}
			if (counter == loopsMask.length)
				return true;
			else
				counter = 0;
		}
		return false;

	}

	private double nthNontouch(int n) {
		if (noNontouch())
			return 0;

		switch (n) {
		case 2:
			return calcGain(twoNontouch());
		case 3:
			return calcGain(threeNontouch());
		case 4:
			return calcGain(fourNontouch());
		case 5:
			return calcGain(fiveNontouch());
		default:
			return 0;
		}
	}

	private double calcGain(ArrayList<LinkedList<Integer>> m) {
		double counter = 0;
		double secCounter = 1;
		for (int i = 0; i < m.size(); i++) {
			for (int j = 0; j < m.get(i).size(); j++) {
				secCounter *= loopsGain.get(m.get(i).get(j) - 1);
			}
			counter += secCounter;
			secCounter = 1;
		}
		return counter;
	}

	public ArrayList<LinkedList<Integer>> twoNontouch() {
		ArrayList<LinkedList<Integer>> nontouch = new ArrayList<LinkedList<Integer>>();
		LinkedList<Integer> temp;

		boolean flag = true;
		// looping over columns
		for (int i = 0; i < loops.size() - 1; i++) {
			// making combinations of columns
			for (int e = i + 1; e < loops.size(); e++) {
				flag = true;
				// looping over rows
				for (int j = 0; j < loopsMask.length && flag; j++) {

					if (loopsMask[j][i] + loopsMask[j][e] > 1)
						flag = false;
				}
				if (flag) {
					temp = new LinkedList<Integer>();
					temp.add(i + 1);
					temp.add(e + 1);
					nontouch.add(new LinkedList<Integer>());
					nontouch.get(nontouch.size() - 1).addAll(temp);

				}
			}

		}
		return nontouch;
	}

	public ArrayList<LinkedList<Integer>> threeNontouch() {
		ArrayList<LinkedList<Integer>> nontouch = new ArrayList<LinkedList<Integer>>();
		LinkedList<Integer> temp;

		boolean flag = true;
		// looping over columns
		for (int i = 0; i < loops.size() - 2; i++) {
			// making combinations of columns
			for (int e = i + 1; e < loops.size() - 1; e++) {
				for (int t = e + 1; t < loops.size(); t++) {
					flag = true;
					// looping over rows
					for (int j = 0; j < loopsMask.length && flag; j++) {
						if (loopsMask[j][i] + loopsMask[j][e] + loopsMask[j][t] > 1)
							flag = false;
					}
					if (flag) {
						temp = new LinkedList<Integer>();
						temp.add(i + 1);
						temp.add(e + 1);
						temp.add(t + 1);
						nontouch.add(new LinkedList<Integer>());
						nontouch.get(nontouch.size() - 1).addAll(temp);

					}
				}
			}

		}
		return nontouch;
	}

	public ArrayList<LinkedList<Integer>> fourNontouch() {
		ArrayList<LinkedList<Integer>> nontouch = new ArrayList<LinkedList<Integer>>();
		LinkedList<Integer> temp;

		boolean flag = true;
		// looping over columns
		for (int i = 0; i < loops.size() - 3; i++) {
			// making combinations of columns
			for (int e = i + 1; e < loops.size() - 2; e++) {
				for (int t = e + 1; t < loops.size() - 1; t++) {
					for (int x = t + 1; x < loops.size(); x++) {
						flag = true;
						// looping over rows
						for (int j = 0; j < loopsMask.length && flag; j++) {
							if (loopsMask[j][i] + loopsMask[j][e]
									+ loopsMask[j][t] + loopsMask[j][x] > 1)
								flag = false;
						}
						if (flag) {
							temp = new LinkedList<Integer>();
							temp.add(i + 1);
							temp.add(e + 1);
							temp.add(t + 1);
							temp.add(x + 1);
							nontouch.add(new LinkedList<Integer>());
							nontouch.get(nontouch.size() - 1).addAll(temp);

						}
					}

				}
			}
		}
		return nontouch;
	}

	public ArrayList<LinkedList<Integer>> fiveNontouch() {
		ArrayList<LinkedList<Integer>> nontouch = new ArrayList<LinkedList<Integer>>();
		LinkedList<Integer> temp;

		boolean flag = true;
		// looping over columns
		for (int i = 0; i < loops.size() - 4; i++) {
			// making combinations of columns
			for (int e = i + 1; e < loops.size() - 3; e++) {
				for (int t = e + 1; t < loops.size() - 2; t++) {
					for (int x = t + 1; x < loops.size() - 1; x++) {
						for (int y = x + 1; y < loops.size(); y++) {
							flag = true;
							// looping over rows
							for (int j = 0; j < loopsMask.length && flag; j++) {
								if (loopsMask[j][i] + loopsMask[j][e]
										+ loopsMask[j][t] + loopsMask[j][x]
										+ loopsMask[j][y] > 1)
									flag = false;
							}
							if (flag) {
								temp = new LinkedList<Integer>();
								temp.add(i + 1);
								temp.add(e + 1);
								temp.add(t + 1);
								temp.add(x + 1);
								nontouch.add(new LinkedList<Integer>());
								nontouch.get(nontouch.size() - 1).addAll(temp);

							}
						}
					}

				}
			}
		}
		return nontouch;
	}

	private boolean seqNotIn(LinkedList<Integer> seq,
			ArrayList<LinkedList<Integer>> seqList) {

		boolean flag = true;
		for (int i = 0; i < seqList.size() && flag; i++) {
			if (seqList.get(i).getFirst() == seq.getFirst())
				flag = false;
		}
		if (flag)
			return flag;

		flag = true;
		for (int i = 0; i < seqList.size() && flag; i++) {
			if (seqList.get(i).getLast() == seq.getLast())
				flag = false;
		}
		if (flag)
			return flag;

		flag = true;
		int counter = seq.size();
		for (int i = 0; i < seqList.size(); i++) {
			if (seqList.get(i).size() == seq.size()) {
				for (int j = 0; j < seq.size() && flag; j++) {
					if (seqList.get(i).get(j) != seq.get(j)) {
						counter--;
						flag = false;
					}
				}
			} else {
				counter--;
			}
			if (counter != seq.size() - i - 1)
				return false;
			else
				flag = true;
		}
		return true;

	}

	private void sortNodes(int[][] nodes) {
		int[] temp;
		double dtemp;

		// sorting according to head node
		for (int i = 0; i <= nodes.length - 1; i++) {
			for (int k = 0; k <= nodes.length - 2; k++) {

				if (nodes[k][0] > nodes[k + 1][0]) {
					temp = nodes[k];

					nodes[k] = nodes[k + 1];

					nodes[k + 1] = temp;

					dtemp = nodesGain[k];
					nodesGain[k] = nodesGain[k + 1];
					nodesGain[k + 1] = dtemp;

				}
			}
		}

		// sorting according to tail node after head node

		for (int i = 0; i <= nodes.length - 1; i++) {
			for (int k = 0; k <= nodes.length - 2; k++) {
				if (nodes[k][0] == nodes[k + 1][0]
						&& nodes[k][1] > nodes[k + 1][1]) {
					temp = nodes[k];
					nodes[k] = nodes[k + 1];
					nodes[k + 1] = temp;

					dtemp = nodesGain[k];
					nodesGain[k] = nodesGain[k + 1];
					nodesGain[k + 1] = dtemp;

				}
			}
		}

		nodesNoFB = new int[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			nodesNoFB[i] = nodes[i][0];

		}

		// sorting feedback
		boolean flag;
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i][0] > nodes[i][1]) {
				flag = true;
				for (int k = 0; k < nodes.length && flag; k++) {
					if (nodes[k][0] == nodes[i][1]) {
						flag = false;
						if (nodes[k][0] < nodes[k][1]) {
							temp = nodes[i];
							dtemp = nodesGain[i];
							for (int j = i; j > k; j--) {
								nodes[j] = nodes[j - 1];
								nodesGain[j] = nodesGain[j - 1];
							}
							nodes[k] = temp;
							nodesGain[k] = dtemp;

						} else {
							temp = nodes[i];
							dtemp = nodesGain[i];
							for (int j = i; j > k + 1; j--) {
								nodes[j] = nodes[j - 1];
								nodesGain[j] = nodesGain[j - 1];
							}
							nodes[k + 1] = temp;
							nodesGain[k + 1] = dtemp;

						}

					}
				}
			}

		}

	}
//	public static void main(String[] args) {
//		
//				int[][] nodes = new int[3][2];
//				double[] nodesGain = new double[3];
//				nodes[0][0] = 1;
//				nodes[1][0] = 2;
//				nodes[2][0] = 3;
//		
//				nodes[0][1] = 2;
//				nodes[1][1] = 3;
//				nodes[2][1] = 1;
//		
//				nodesGain[0] = 12;
//				nodesGain[1] = 13;
//				nodesGain[2] = 1;
//		
//		
//				Operate mason = new Operate();
//				mason.setNodes(nodes);
//				mason.setMaxNodes(3);
//				mason.setNodesGain(nodesGain);
//				mason.generate();
//		
//				System.out.println(mason.getLoops());
//				// System.out.println(mason.loopsGain);
//				System.out.println(mason.getForwardPaths());
//				// System.out.println(mason.forwardPathsGain);
//				System.out.println(mason.getnonTouching());
//		
//				System.out.println(mason.getTF());
//			}
}
