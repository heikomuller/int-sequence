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
 * Integer interval of non-negative numbers. Extends base implementation with
 * constraint that start and end of the interval have to be non-negative
 * integers.
 * 
 * @author Heiko Mueller <heiko.mueller@nyu.edu>
 */
public class NonNegativeIntegerInterval extends IntegerInterval {
    
    /**
     * Initialize the interval. Ensures that start and end are non-negative
     * integers.
     * 
     * @param interval 
     */
    public NonNegativeIntegerInterval(int[] interval) {
	
	super(interval);
	
	if (this.start() < 0) {
	    throw new java.lang.IllegalArgumentException("Illegal interval start at " + this.start());
	} else if (this.end() < 0) {
	    throw new java.lang.IllegalArgumentException("Illegal interval end at " + this.end());
	}
    }
}
