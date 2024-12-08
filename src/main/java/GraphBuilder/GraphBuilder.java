package GraphBuilder;

import GraphBuilder.json_representations.JCell;
import GraphBuilder.json_representations.JPort;
import GraphBuilder.json_representations.JsonFile;
import GraphBuilder.json_representations.Module;
import MinecraftGraph.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import minecrafthdl.MHDLException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


//main class
//json file->javaObject->Vertex->graph
public class GraphBuilder {
	// Lists of stuff for graph construction
	private static ArrayList<String> ports_names=new ArrayList<String>();
	private static ArrayList<String> cells_names=new ArrayList<String>();
	private static ArrayList<Port> ports=new ArrayList<Port>();
	private static ArrayList<Cell> cells=new ArrayList<Cell>();
	
	private static ArrayList<In_output> inputs=new ArrayList<In_output>();
	private static ArrayList<In_output> outputs=new ArrayList<In_output>();
	private static ArrayList<Function> gates=new ArrayList<Function>();

	static int test_i = 1;

	static int high_low_nets = Integer.MAX_VALUE;
	static int cell_ids = 0;

	// maps net IDs to verticies
	// TODO: double declaration? (in class??)
	static HashMap<Integer, Vertex> from_net = new HashMap<Integer, Vertex>();
	static HashMap<Integer, ArrayList<Vertex>> to_net = new HashMap<Integer, ArrayList<Vertex>>();
	
	/**
	 * Links vertex to given net (to_net map)
	 * Handles special cases for HIGH or LOW nets by creating corresponding Function nodes
	 */
	public static int putInToNet(int i, Vertex v, Graph g){
		ArrayList<Vertex> l = to_net.get(i);

		if (i == 0) {
			high_low_nets--;
			Function f = new Function(cell_ids++, FunctionType.LOW, 0);
			from_net.put(high_low_nets, f);
			to_net.put(high_low_nets, new ArrayList<Vertex>());
			to_net.get(high_low_nets).add(v);
			g.addVertex(f);
			return high_low_nets;
		} else if (i == 1){
			high_low_nets--;
			Function f = new Function(cell_ids++, FunctionType.HIGH, 0);
			from_net.put(high_low_nets, f);
			to_net.put(high_low_nets, new ArrayList<Vertex>());
			to_net.get(high_low_nets).add(v);
			g.addVertex(f);
			return high_low_nets;
		} else {
			if (l == null) to_net.put(i, new ArrayList<Vertex>());
			to_net.get(i).add(v);
			return i;
		}
	}

	public static int putInFromNet(int i, Vertex v){
		Vertex vr = from_net.get(i);

		if (vr != null) throw new MHDLException("TWO OUTPUTS ON SAME NET");
		from_net.put(i, v);
		return i;
	}

	public static Graph buildGraph(String path){
		high_low_nets = Integer.MAX_VALUE;

		Gson gson= new com.google.gson.Gson();
		JsonFile jf = null;
		try {
			FileReader fr = new FileReader(path);
			JsonReader jreader = new JsonReader(fr);
			jreader.setLenient(true);
			jf = gson.fromJson(jreader, JsonFile.class);
			fr.close();
			jreader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		jf.postInit();

		Graph g = new Graph();

		Module m = jf.modules.values().iterator().next();

		from_net = new HashMap<Integer, Vertex>();
		to_net = new HashMap<Integer, ArrayList<Vertex>>();

		for (String p_name : m.ports.keySet()){
			JPort p = m.ports.get(p_name);

			In_output io;

			if (p.direction.equals("input")){

				io = new In_output(p.bits.size(), VertexType.INPUT, p_name);

				for (int i : p.bits){
					putInFromNet(i, io);
				}
			} else {
				io = new In_output(p.bits.size(), VertexType.OUTPUT, p_name);

				for (int i : p.bits){
					putInToNet(i, io, g);
				}
			}
			g.addVertex(io);
		}

		cell_ids = 0;

		for (String c_name : m.cells.keySet()){
			JCell c = m.cells.get(c_name);

			FunctionType f_type = resolveType(c.type);

			Function f;

			if (f_type == FunctionType.MUX){
				f = new MuxVertex(cell_ids++, f_type, c.numInputs());
			} else {
				f = new Function(cell_ids++, f_type, c.numInputs());
			}

			for (String conn_name : c.connections.keySet()){
				String direction = c.port_directions.get(conn_name);
				ArrayList<Integer> conn_nets = c.connections.get(conn_name);

				int conn_net = -1;

				if (direction.equals("input")){
					for (int i : conn_nets){
						conn_net = putInToNet(i, f, g);
					}
				} else {
					for (int i : conn_nets){
						conn_net = putInFromNet(i, f);
					}
				}

				if (f_type == FunctionType.MUX){
					if (conn_name.equals("S")){
						((MuxVertex) f).s_net_num = conn_net;
					} else if (conn_name.equals(("A"))) {
						((MuxVertex) f).a_net_num = conn_net;
					} else if (conn_name.equals(("B"))) {
						((MuxVertex) f).b_net_num = conn_net;
					}
				}
			}
			g.addVertex(f);
		}

		for (int i : to_net.keySet()){
			for (Vertex v : to_net.get(i)){
				Vertex from = from_net.get(i);
				if (from == null){
					throw new MHDLException("NET HAS NO FROM VERTEX");
				}

				if (v.type == VertexType.FUNCTION){
					Function f = ((Function) v);
					if (((Function) v).func_type == FunctionType.MUX){
						MuxVertex mux = ((MuxVertex) f);

						if (i == mux.a_net_num){
							mux.a_vertex = from;
						} else if (i == mux.b_net_num){
							mux.b_vertex = from;
						} else if (i == mux.s_net_num){
							mux.s_vertex = from;
						}
					}
				}
				g.addEdge(from, v);
			}
		}

		return g;
	}

	/**
	 * TODO: CURRENTLY UNUSED!
	 * supposed to remove redundant gates
	 * We should probably use this if needed to reduce number of gates
	 * OR add a param that tells you whether or not to optimize, for learning purposes????
	 * 
	 * synthesis doesn't simplify (a&b) | (a&c) into a & (b|c)
	 */
	private static void optimizeGraph(Graph graph){
		//iterate through all the nodes of the graph
		//if or or and gate check outputs
		//if all outputs are of the same type
		//remove lower level and reconnect its inputs with the higher level
		//got back
		ArrayList<Vertex> verToRemove=new ArrayList<Vertex>();
		for(Vertex v: graph.getVertices()){
			System.out.println("vertex for");

			//check if vertex is a gate
			if(v.getType()==VertexType.FUNCTION){
				//check if gate type is and, or
				Function f=(Function)v;
				
				FunctionType f_t=f.getFunc_Type();
				if(f_t==FunctionType.AND||f_t==FunctionType.OR){
					if(canMerge(f)){
						for(Vertex s:f.getNext()){
							System.out.println("vertex inner for");

							graph.mergeVertices(f, s);
							verToRemove.add(f);
						}
					}
				}
			}
		}
		
		for(Vertex t:verToRemove){
			System.out.println("vertex inner 2 for");

			graph.removeVertex(t);
		}
	}

	private static boolean canMerge(Function v){
		for(Vertex x:v.getNext()){
			if(x.getType()!=VertexType.FUNCTION){
				return false;
			}
			Function f=(Function)x;
			if(f.getFunc_Type()!=v.getFunc_Type()){
				return false;
			}	
		}
		
		return true;
	}
	
	
	/**
	 * Finds vertex in graph corresponding to port/cell by matching IDs
	 */
	private static Vertex getVertex(Graph g, Port p){
	
		for(Vertex v:g.getVertices()){
			if(v.getID().equals(p.name)){
				return v;
			}
		}

		return null;
	}

	private static Vertex getVertex(Graph g, Cell p){
		
		for(Vertex v:g.getVertices()){
			
			if(v.getID().equals(String.valueOf(p.id))){
				return v;
			}
		}

		return null;
	}
	

	//checks if signals, gates are connected
	private static int areConnected(ArrayList<Integer> bits, ArrayList<Integer> inputs2){
		
		int count=0; 
		
		for(Integer x: bits){
			for(Integer y: inputs2){
				if(x==y){
					count++;
				}
			}
		}

		return count;
	}

	
	/**
	 * Counts number of inputs in JSON object by analyzing connections field
	 */
	private static int num_of_inputs(JsonObject j_o){
		int num=0;
		char[] connection=j_o.get("connections").toString().toCharArray();
		for(char c: connection){
			if(c==':') num++;
		}

		return num-1;
	}


	/**
	 * Maps string representations of gate types to the FunctionType enum
	 */
	private static FunctionType resolveType(String type){
		//TODO: make sure all strings get included
		
		if(type.contains("AND")||type.contains("and")){
			return FunctionType.AND;
			
		} else if(type.contains("MUX")||type.contains("mux")){
			return FunctionType.MUX;

		} else if(type.contains("XOR")||type.contains("xor")){
			return FunctionType.XOR;

		} else if(type.contains("OR")||type.contains("or")){
			return FunctionType.OR;
			
		} else if(type.contains("DLATCH_P")||type.contains("dlatch_p")) {
			return FunctionType.D_LATCH;

		} else if(type.contains("NOT")||type.contains("not")){
				return FunctionType.INV;
				
		} else{
			throw new MHDLException("Unknown Cell:" + type);
		}
	}
}


class Port{
	String name;
	String direction;
	ArrayList<Integer> bits=new ArrayList<Integer>();

	public Port(String n, String d, ArrayList<Integer> b){
		name=n;
		direction=d;
		bits=b;
	}
}

class Cell{
	int id;
	String type;
	ArrayList<Connection> connections=new ArrayList<Connection>();

	ArrayList<Integer> inputs=new ArrayList<Integer>();
	ArrayList<Integer> outputs=new ArrayList<Integer>();

	public Cell(int i, String t, ArrayList<Connection> cns){
		id=i;
		type=t;
		connections=cns;


		for(Connection c:connections){
			if(c.direction.equals("input")){
				for(int j=0; j<c.IDs.length; j++){
					inputs.add(c.IDs[j]);
				}
			}
			else{
				for(int j=0; j<c.IDs.length; j++){
					outputs.add(c.IDs[j]);
				}
			}
		}
	}

	public Connection getConn(String name){
		for (Connection c : this.connections){
			if (c.name.equals(name)) return c;
		}

		return null;
	}
}

class Connection{
	String name;
	String direction;
	int IDs[];

	public Connection(String d, ArrayList<Integer> arr, String name){
		this.name = name;
		direction=d;
		IDs= new int[arr.size()];
		for(int j=0; j<arr.size(); j++){
			IDs[j]=arr.get(j);
		}
	}
}
