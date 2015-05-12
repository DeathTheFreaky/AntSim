package at.antSim.KI;

import java.util.ArrayList;

public class Hive {

	// Gibts was besseres als static wenn mehrere Threads drauf zugreifen
	public static ArrayList<Feedable> fa = new ArrayList<Feedable>();
	public static ArrayList<Ant> ant = new ArrayList<Ant>();
	public static int foodStacks;
	public static Queen queen;
	
	public Hive(){
		foodStacks = 20;
		queen = new Queen();
		fa.add(queen);
	}
	
	//Startbedingungen aendern
	public Hive(int food){
		foodStacks = food;
		queen = new Queen();
	}
	
	public void addEgg(Egg e){
		fa.add(e);
	}
	
	public void addLarva(Larva l){
		fa.add(l);
	}
	
	public void takeFood(){
		foodStacks--;
	}
	
	public void storeFood(){
		foodStacks++;
	}
}
