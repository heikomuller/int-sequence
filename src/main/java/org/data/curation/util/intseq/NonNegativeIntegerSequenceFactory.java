/*
 * Copyright 2016 New York University.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.data.curation.util.intseq;

import java.util.ArrayList;
import java.util.List;

/**
 * Instance factory implementation for non negative integer intervals and
 * sequences.
 * 
 * @author Heiko Mueller <heiko.mueller@nyu.edu>
 */
public class NonNegativeIntegerSequenceFactory extends IntegerSequenceFactory {

    @Override
    public NonNegativeIntegerInterval getInterval(int[] interval) {

	return new NonNegativeIntegerInterval(interval);
    }

    @Override
    public NonNegativeIntegerSequence getSequence(int[][] intervals) {

	ArrayList<NonNegativeIntegerInterval> nonNegIntervals = new ArrayList<>();
	for (int[] interval : intervals) {
	    nonNegIntervals.add(new NonNegativeIntegerInterval(interval));
	}
	return new NonNegativeIntegerSequence(nonNegIntervals);
    }

    @Override
    public NonNegativeIntegerSequence getSequence(List<IntegerInterval> intervals) {

	ArrayList<NonNegativeIntegerInterval> nonNegIntervals = new ArrayList<>();
	for (IntegerInterval interval : intervals) {
	    nonNegIntervals.add(new NonNegativeIntegerInterval(new int[]{interval.start(), interval.end()}));
	}
	return new NonNegativeIntegerSequence(nonNegIntervals);
    }

    /**
     * Create a integer sequence from a string. Expects a string as produced by
     * the toIntervalString() method of the non-negative integer sequence
     * implementation. The format is a comma delimited list of intervals or
     * single integers, e.g., v1-v2,v3,v4-v5.
     * 
     * @param text 
     * @return  
     */
    public NonNegativeIntegerSequence getSequence(String text) {
	
	String[] tokens = text.split(",");
	
	int[][] intervals = new int[tokens.length][2];
	for (int iToken = 0; iToken < tokens.length; iToken++) {
	    String token = tokens[iToken];
	    int pos = token.indexOf("-");
	    if (pos != -1) {
		intervals[iToken][0] = Integer.parseInt(token.substring(0, pos).trim());
		intervals[iToken][1] = Integer.parseInt(token.substring(pos + 1).trim());
	    } else {
		intervals[iToken][0] = Integer.parseInt(token);
		intervals[iToken][1] = intervals[iToken][0];
	    }
	}
	int[] prev = null;
	for (int[] interval : intervals) {
	    if (interval[0] < 0)  {
		throw new java.lang.IllegalArgumentException("TimeInterval start at " + interval[0] + ".");
	    } else if (interval[1] < 0)  {
		throw new java.lang.IllegalArgumentException("TimeInterval end at " + interval[1] + ".");
	    } else if (interval[0] > interval[1]) {
		throw new java.lang.IllegalArgumentException("TimeInterval [" + interval[0] + "-" + interval[1] + "] is invalid.");
	    }
	    if (prev != null) {
		if (prev[1] >= interval[0]) {
		    throw new java.lang.IllegalArgumentException("Adjacent or overlapping intervals [" + prev[0] + "-" + prev[1] + "] and [" + interval[0] + "-" + interval[1] + "].");
		}
	    }
	    prev = interval;
	}
	return this.getSequence(intervals);
    }
}
