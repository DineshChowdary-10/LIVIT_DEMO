package repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import model.*;


public class DataStore {
	
	public static HashMap<Integer,Student> students = new HashMap<>();
	
	public static HashMap<Integer,Owner> owners = new HashMap<>();
	
	public static ArrayList<PG> pgs = new ArrayList<>();
	
	public static HashMap<Integer,PG> pgMap = new HashMap<>();
	
	public static Queue<Booking> bookingQueue = new LinkedList<>();
	
	public static HashSet<String> facilities = new HashSet<>();
	
	
	

}
