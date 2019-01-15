/*
 * polycasso - Cubism Artwork generator
 * Copyright 2009-2019 MeBigFatGuy.com
 * Copyright 2009-2019 Dave Brosius
 * Inspired by work by Roger Alsing
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0 
 *    
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and limitations 
 * under the License. 
 */
package com.mebigfatguy.polycasso;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

/**
 * a class to keep track of success statistics by improvement type, in order to further tune what
 * improvement types to try in the future. It modifies a purely random selection criteria to one that
 * is tuned by performance.
 */
public class ImprovementTypeStats {
	
	private static final int MAX_FAILURE_RUN = 50;
	private static class Stats {
		public int successes = 1;
		public int totals = 1;
		public double pct = 1.0;
		
		@Override
		public String toString() {
			return successes + "/" + totals + " [" + pct + "]";
		}
	}
	
	private Map<ImprovementType, Stats> typeStats = new EnumMap<ImprovementType, Stats>(ImprovementType.class);
	private Random r = new Random();
	private int failureRun;
	
	/**
	 * creates an initial state of statistics
	 */
	public ImprovementTypeStats() {
		initStats();
	}
	
	/**
	 * increment a type's statistics whether success or fail
	 * 
	 * @param type the improvement type that is to be updated
	 * @param successful if the improvement was successful
	 */
	public void typeWasSuccessful(ImprovementType type, boolean successful) {
		Stats stats = typeStats.get(type);
		if (successful) {
			stats.successes++;
		}
		stats.totals++;
		failureRun = successful ? 0 : failureRun + 1;
		if (failureRun > MAX_FAILURE_RUN) {
			if (Polycasso.DEBUG){
				System.out.println("** Stats at time of failure run **");
				System.out.println(this);
				System.out.println("**********************************");
			}
			initStats();
		} else {
			stats.pct = ((double)stats.successes) / ((double) stats.totals);
		}
	}
	
	/**
	 * returns a random improvement type that is influenced by how successful the types
	 * have been in the past.
	 * 
	 * @return the improvement type to try
	 */
	public ImprovementType getRandomImprovementType() {
		double pct = r.nextDouble();
		
		double totalPct = 0.0;
		for (Stats stat : typeStats.values()) {
			totalPct += stat.pct;
		}
		
		for (Map.Entry<ImprovementType, Stats> entry : typeStats.entrySet()) {
			Stats stat = entry.getValue();
			double typePct = stat.pct / totalPct;
			if (pct <= typePct)
				return entry.getKey();
			
			pct -= typePct;
		}
		
		return ImprovementType.CompleteChange;
	}
	
	/**
	 * builds a string of all the different types success statistics
	 * 
	 * @return a statistics string
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(100);
		for (Map.Entry<ImprovementType, Stats> entry : typeStats.entrySet()) {
			Stats stat = entry.getValue();
			sb.append(" ").append(entry.getKey().name()).append("% = ").append(stat.pct * 100);
		}
		return sb.toString();
	}
	/**
	 * sets the stats to an initial state
	 */
	private void initStats() {
		ImprovementType[] values = ImprovementType.values();
		for (ImprovementType type : values) {
			typeStats.put(type, new Stats());
		}
		failureRun = 0;
	}
}
