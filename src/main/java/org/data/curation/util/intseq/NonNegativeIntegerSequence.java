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
 * Sequence of non-negative integers.
 * 
 * @author Heiko Mueller <heiko.mueller@nyu.edu>
 */
public class NonNegativeIntegerSequence extends IntegerSequence {
    
    public NonNegativeIntegerSequence(List<NonNegativeIntegerInterval> intervals) {
	
	super(new ArrayList<IntegerInterval>(intervals), new NonNegativeIntegerSequenceFactory());
    }
    
    /**
     * Returns a string representation of the time sequence in interval
     * notation.
     * 
     * @return 
     */
    public String toIntervalString() {

	StringBuilder buf = new StringBuilder();
	
	if (this.intervals() > 0) {
	    IntegerInterval firstInterval = this.interval(0);
	    buf.append(Integer.toString(firstInterval.start()));
	    if (firstInterval.start() != firstInterval.end()) {
	       buf.append('-');
	       buf.append(Integer.toString(firstInterval.end()));
	    }
	    for (int iInterval = 1; iInterval < this.intervals(); iInterval++) {
		IntegerInterval interval = this.interval(iInterval);
		buf.append(',');
		buf.append(Integer.toString(interval.start()));
		if (interval.start() != interval.end()) {
		    buf.append('-');			
		    buf.append(Integer.toString(interval.end()));
		}
	    }
	}
	
	return buf.toString();
    }
    
    @Override
    public String toString() {
	
	return this.toIntervalString();
    }
}
