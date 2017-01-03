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
import java.util.Collections;
import java.util.List;

/**
 * Factory object for integer intervals and sequences. Different types of
 * integer sequences may have different constraints on the intervals and
 * sequences. These constraints are implemented in the respective sub-classes
 * which are generated using a implementation-specific factory object.
 * 
 * @author Heiko Mueller <heiko.mueller@nyu.edu>
 */
public abstract class IntegerSequenceFactory {
    
    /**
     * Get implementation-specific interval instance for given array. Expects
     * an array with exactly two values, the first defining the interval start
     * and the second the interval end.
     * 
     * @param interval
     * @return 
     */
    public abstract IntegerInterval getInterval(int[] interval);
    
    /**
     * Get implementation-specific interval where start and end are defined by
     * the given value.
     * 
     * @param value
     * @return 
     */
    public IntegerInterval getInterval(int value) {
	
	return this.getInterval(new int[]{value, value});
    }
    
    /**
     * Get implementation-specific interval instance with given start and end
     * values.
     * @param start
     * @param end
     * @return 
     */
    public IntegerInterval getInterval(int start, int end) {
	
	return this.getInterval(new int[]{start, end});
    }

    /**
     * Get implementation-specific empty integer sequence.
     * 
     * @return 
     */
    public IntegerSequence getSequence() {
    
	return this.getSequence(new int[0][]);
    }
    
    /**
     * Get implementation-specific integer sequence instance for given value.
     * The value defines the start and end of the interval..
     * 
     * @param value
     * @return 
     */
    public IntegerSequence getSequence(int value) {
	
	return this.getSequence(new int[][]{{value, value}});
    }
    
    /**
     * Get implementation-specific integer sequence instance for given array
     * of intervals. Expects a two dimensional array of integers where second
     * dimension contains exactly tow values for each entry.
     * 
     * @param intervals
     * @return 
     */
    public abstract IntegerSequence getSequence(int[][] intervals);
    
    /**
     * Get implementation-specific integer sequence instance for given list
     * of intervals.
     * 
     * @param intervals
     * @return 
     */
    public abstract IntegerSequence getSequence(List<IntegerInterval> intervals);
    
    /**
     * Transform the given list of values to an integer sequence. Uses the given
     * factory implementation to generate intervals and sequences.
     * 
     * The list of values is expected to be sorted in ascending order.
     * 
     * @param values
     * @param factory
     * @return 
     */
    public static IntegerSequence toSequence(List<Integer> values, IntegerSequenceFactory factory) {
	
	ArrayList<IntegerInterval> intervals = new ArrayList<>();

	if (values.size() > 0) {
	    IntegerInterval interval = factory.getInterval(values.get(0));
	    for (int iValue = 1; iValue < values.size(); iValue++) {
		int value = values.get(iValue);
		if (value == interval.end() + 1) {
		    interval = factory.getInterval(interval.start(), value);
		} else {
		    intervals.add(interval);
		    interval = factory.getInterval(value);
		}
	    }
	    intervals.add(interval);
	}
	
	return factory.getSequence(intervals);
    }
}
