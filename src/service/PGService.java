package service;

import model.PG;
import repository.DataStore;

public class PGService {
	
	public void addPG(PG pg)
	{
		if(pg == null)
		{
			throw new IllegalArgumentException("PG cannot be null");
		}
		
		if(pg.getPgId()==null)
		{
			throw new IllegalArgumentException("PG ID cannot be null");
		}
		
		if(DataStore.pgMap.containsKey(pg.getPgId()))
		{
			throw new IllegalArgumentException("PG ID already exists");
		}
		
		DataStore.pgs.add(pg);
		DataStore.pgMap.put(pg.getPgId(),pg);
	}

}
