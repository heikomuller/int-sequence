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

/**
 * Interval of integer numbers. Defines that start and end of the interval.
 * Ensures that start is lower or equal that end.
 * 
 * @author Heiko Mueller <heiko.mueller@nyu.edu>
 */
public abstract class IntegerInterval {
    
    private final int[] _interval;
    
    /**
     * Initialize the interval from a given integer array. Expects that the
     * array has exactly two elements: 0 is start and 1 is end. Ensures that
     * start is lower or equal than end.
     * 
     * @param interval 
     */
    public IntegerInterval(int[] interval) {
	
	if (interval.length != 2) {
	    throw new java.lang.IllegalArgumentException("Integer interval expects an array of length 2");
	} else if (interval[0] > interval[1]) {
	    throw new java.lang.IllegalArgumentException("Integer interval [" + interval[0] + "-" + interval[1] + "] is invalid");
	}
	_interval = interval;
    }
    
    /**
     * Returns true if this interval contains the given interval.
     * 
     * @param interval
     * @return 
     */
    public boolean contains(IntegerInterval interval) {
	
	return ((this.start() <= interval.start()) && (this.end() >= interval.end()));
    }
    
    /**
     * The end of the interval.
     * 
     * @return 
     */
    public int end() {
	
	return _interval[1];
    }
    
    /**
     * Returns true if the two intervals overlap.
     * 
     * @param interval
     * @return 
     */
    public boolean overlap(IntegerInterval interval) {
	
	if ((this.start() == interval.start()) || (this.start() == interval.end()) || (this.end() == interval.start()) || (this.end() == interval.end())) {
	    return true;
	}
		
	if (this.start() < interval.start()) {
	    return this.end() > interval.start();
	} else if (this.start() > interval.start()) {
	    return interval.end() > this.start();
	} else {
	    return true;
	}
    }
    
    /**
     * The start of the interval.
     * 
     * @return 
     */
    public int start() {
	
	return _interval[0];
    }
}
