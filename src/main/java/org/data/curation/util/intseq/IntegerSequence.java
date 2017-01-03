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
 * A sequence of integer numbers represented as a list of integer intervals. 
 * There are different sub-classes for integer sequences that may enforce
 * different constraints on the intervals. This base class defines methods that
 * are common to these sequences.
 *
 * @author Heiko Mueller <heiko.mueller@nyu.edu>
 */
public abstract class IntegerSequence {

    private final IntegerSequenceFactory _factory;
    private final int[][] _intervals;

    /**
     * Initialize the integer sequence using an two-dimensional array of
     * integers. Expects an array of integer pairs, where the first value of
     * each pair is the interval start and the second value the interval end.
     * Ensures that all intervals are valid and not adjacent or overlapping.
     * 
     * Expects the implementation dependent factory of integer intervals and
     * sequences to allow a generic implementation of most methods that
     * require instantiation of interval and sequence objects.
     * 
     * @param intervals 
     * @param factory 
     */
    public IntegerSequence(int[][] intervals, IntegerSequenceFactory factory) {
	
	_factory = factory;
	
	if (intervals.length > 0) {
	    int[] prev = null;
	    int intervalCount = 0;
	    for (int[] interval : intervals) {
		if (interval.length != 2) {
		    throw new java.lang.IllegalArgumentException("TimeInterval expects an array of length 2 instead of " + interval.length);
		} else if (interval[0] > interval[1]) {
		    throw new java.lang.IllegalArgumentException("TimeInterval [" + interval[0] + "-" + interval[1] + "] is invalid.");
		}
		if (prev != null) {
		    if (prev[1] >= interval[0]) {
			throw new java.lang.IllegalArgumentException("Adjacent or overlapping intervals [" + prev[0] + "-" + prev[1] + "] and [" + interval[0] + "-" + interval[1] + "].");
		    }
		    if (interval[0] > (prev[1] + 1)) {
			intervalCount++;
		    }
		} else {
		    intervalCount++;
		}
		prev = interval;
	    }
	    _intervals = this.copyIntervals(intervals, intervalCount);
	} else {
	    _intervals = new int[0][];
	}
    }

    /**
     * Initialize the integer sequence using a list of integer intervals.
     * Ensures that all intervals are valid and not adjacent or overlapping.
     * 
     * Expects the implementation dependent factory of integer intervals and
     * sequences to allow a generic implementation of most methods that
     * require instantiation of interval and sequence objects.
     * 
     * @param intervals 
     * @param factory 
     */
    public IntegerSequence(List<IntegerInterval> intervals, IntegerSequenceFactory factory) {
	
	_factory = factory;

	_intervals = new int[intervals.size()][2];
	
	IntegerInterval prev = null;
	for (int iInterval = 0; iInterval < intervals.size(); iInterval++) {
	    IntegerInterval interval = intervals.get(iInterval);
	    _intervals[iInterval][0] = interval.start();
	    _intervals[iInterval][1] = interval.end();
	    if (prev != null) {
		if (prev.end() >= interval.start()) {
		    throw new java.lang.IllegalArgumentException("Adjacent or overlapping intervals [" + prev.start() + "-" + prev.end() + "] and [" + interval.start() + "-" + interval.end() + "].");
		} else if (interval.start() == (prev.end() + 1)) {
		    throw new java.lang.IllegalArgumentException("Adjacent or overlapping intervals [" + prev.start() + "-" + prev.end() + "] and [" + interval.start() + "-" + interval.end() + "].");
		}
	    }
		
	    prev = interval;
	}
    }
    
    /**
     * Returns an integer sequence that is a suffix of this integer sequence.
     * The result contains only those values that are after the given value.
     * 
     * @param value
     * @return 
     */
    public IntegerSequence after(int value) {
	
	ArrayList<IntegerInterval> intervals = new ArrayList<>();
	for (int iInterval = 0; iInterval < this.intervals(); iInterval++) {
	    IntegerInterval interval = this.interval(iInterval);
	    if (interval.start() > (value + 1)) {
		intervals.add(interval);
	    } else if (interval.end() >= (value + 1)) {
		intervals.add(_factory.getInterval((value + 1), interval.end()));
	    }
	}
	
	return _factory.getSequence(intervals);
    }
    
    /**
     * Append the given value to the sequence. Throws an exception if the
     * given value is lower or equal to the last value in the sequence.
     * 
     * @param value
     * @return
     */
    public IntegerSequence append(int value) {

	if (_intervals.length > 0) {
	    int lastValue = _intervals[_intervals.length - 1][1];
	    if (lastValue >= value) {
		throw new java.lang.IllegalArgumentException("Attempt to append value " + value + " to integer sequence that end at value " + lastValue);
	    } else if (value == (lastValue + 1)) {
		int[][] intervals = new int[_intervals.length][2];
		this.arrayCopy(_intervals, intervals, _intervals.length);
		intervals[_intervals.length - 1][1] = value;
		return _factory.getSequence(intervals);
	    } else {
		int[][] intervals = new int[_intervals.length + 1][2];
		this.arrayCopy(_intervals, intervals, _intervals.length);
		intervals[intervals.length - 1] = new int[]{value, value};
		return _factory.getSequence(intervals);
	    }
	} else {
	    return _factory.getSequence(value);
	}
    }

    /**
     * Copy array of integers.
     * 
     * @param src
     * @param target
     * @param length 
     */
    private void arrayCopy(int[][] src, int[][] target, int length) {
	
	for (int iInterval = 0; iInterval < length; iInterval++) {
	    target[iInterval][0] = src[iInterval][0];
	    target[iInterval][1] = src[iInterval][1];
	}
    }
    
    /**
     * Returns a new integer sequence that contains all values in this sequence
     * up to (and including) the given last value. The result is an empty
     * sequence if the last value if before the first version of this
     * timestamp.
     * 
     * @param lastValue
     * @return 
     */
    public IntegerSequence close(int lastValue) {
	
	ArrayList<IntegerInterval> intervals = new ArrayList<>();
	for (int iInterval = 0; iInterval < this.intervals(); iInterval++) {
	    IntegerInterval interval = this.interval(iInterval);
	    if (interval.start() > lastValue) {
		break;
	    } else if (interval.end() < lastValue) {
		intervals.add(interval);
	    } else {
		intervals.add(_factory.getInterval(interval.start(), lastValue));
		break;
	    }
	}
	
	return _factory.getSequence(intervals);
    }
    
    /**
     * Copy intervals and merge adjacent. Assumes that length reflects the
     * total number of resulting intervals.
     * 
     * @param intervals
     * @param length
     * @return 
     */
    private int[][] copyIntervals(int[][] intervals, int length) {
	
	int[][] result = new int[length][2];
	
	int index = 0;
	result[0][0] = intervals[0][0];
	result[0][1] = intervals[0][1];
	for (int iInterval = 1; iInterval < intervals.length; iInterval++) {
	    if (intervals[iInterval][0] == (result[index][1] + 1)) {
		result[index][1] = intervals[iInterval][1];
	    } else {
		result[++index][0] = intervals[iInterval][0];
		result[index][1] = intervals[iInterval][1];
	    }
	}
	
	return result;
    }
    
    /**
     * Returns true if the sequence contains the given integer value.
     * 
     * @param value
     * @return 
     */
    public boolean contains(int value) {

	if (_intervals.length > 0) {
	    for (int[] interval : _intervals) {
		if ((interval[0] <= value) && (interval[1] >= value)) {
		    return true;
		}
	    }
	}
	
	return false;
    }

    /**
     * Returns true if the given integer sequence is a subset or equal to this
     * sequence.
     * 
     * @param sequence
     * @return 
     */
    public boolean contains(IntegerSequence sequence) {

	int idxI = 0;
	int idxJ = 0;
		
	while ((idxI < this.intervals()) && (idxJ < sequence.intervals())) {
	    IntegerInterval intervalI = this.interval(idxI);
	    IntegerInterval intervalJ = sequence.interval(idxJ);
	    if (intervalI.end() < intervalJ.start()) {
		idxI++;
	    } else if (intervalI.start() > intervalJ.start()) {
		return false;
	    } else if (intervalI.end() < intervalJ.end()) {
		return false;
	    } else {
		idxJ++;
	    }
	}
	
	return (idxJ == sequence.intervals());
    }

    /**
     * Returns true if the two sequences are disjoint.
     * 
     * @param sequence
     * @return 
     */
    public boolean disjoint(IntegerSequence sequence) {

	return !this.overlap(sequence);
    }
    
    /**
     * Returns true if both integer sequences represent the same list of
     * integer intervals.
     * 
     * @param sequence
     * @return 
     */
    public boolean equals(IntegerSequence sequence) {
	
	if (this.intervals() == sequence.intervals()) {
	    for (int iInterval = 0; iInterval < this.intervals(); iInterval++ ) {
		IntegerInterval intI = this.interval(iInterval);
		IntegerInterval intJ = sequence.interval(iInterval);
		if ((intI.start() != intJ.start()) || (intI.end() != intJ.end())) {
		    return false;
		}
	    }
	    return true;
	}
	
	return false;
    }

    /**
     * The first value in this sequence. The first value of an empty
     * sequence is undefined and an exception will be thrown.
     * 
     * @return 
     */
    public int firstValue() {
	
	if (this.isEmpty()) {
	    throw new java.lang.IllegalStateException("Integer sequence is empty");
	}
	return this.interval(0).start();
    }

    /**
     * Returns the intersection of the two integer sequences.
     * 
     * @param sequence
     * @return 
     */
    public IntegerSequence intersect(IntegerSequence sequence) {
	
	int idxI = 0;
	int idxJ = 0;
	
	ArrayList<int[]> intersects = new ArrayList<>(0);
	
	while ((idxI < this.intervals()) && (idxJ < sequence.intervals())) {
	    IntegerInterval intervalI = this.interval(idxI);
	    IntegerInterval intervalJ = sequence.interval(idxJ);
	    if (intervalI.end() < intervalJ.start()) {
		idxI++;
	    } else if (intervalI.start() > intervalJ.end()) {
		idxJ++;
	    } else {
		/*
		 * One of the intervals may be 'longer' than the other one. We
		 * can't increment the index for that timestamp as the interval
		 * may overlap with the next interval from the other timestamp.
		 */
		int end = Math.min(intervalI.end(), intervalJ.end());
		intersects.add(new int[]{Math.max(intervalI.start(), intervalJ.start()), end});
		if (intervalI.end() <= end) {
		    idxI++;
		}
		if (intervalJ.end() <= end) {
		    idxJ++;
		}
	    }
	}
	
	if (intersects.size() > 0) {
	    int[][] intervals = new int[intersects.size()][2];
	    for (int iInterval = 0; iInterval < intersects.size(); iInterval++) {
		intervals[iInterval] = intersects.get(iInterval);
	    }
	    return _factory.getSequence(intervals);
	} else {
	    return _factory.getSequence();
	}
    }

    /**
     * The interval at the given index position.
     * 
     * @param index
     * @return 
     */
    public IntegerInterval interval(int index) {
	
	return _factory.getInterval(_intervals[index]);
    }
    
    /**
     * The number of intervals.
     * 
     * @return 
     */
    public int intervals() {

	return _intervals.length;
    }
    
    /**
     * Returns true if the integer sequence is empty.
     * 
     * @return 
     */
    public boolean isEmpty() {
	
	return (this.intervals() == 0);
    }
    
    /**
     * Returns the last value in this integer sequence. The last value
     * for an empty timestamp is undefined and an exception will be thrown.
     * 
     * @return 
     */
    public int lastValue() {

	if (this.isEmpty()) {
	    throw new java.lang.IllegalStateException("Integer sequence is empty");
	}
	return this.interval(this.intervals() - 1).end();
    }
    
    /**
     * Returns the total number of integer values in the sequence of integers.
     * 
     * @return 
     */
    public int length() {
	
	int count = 0;
	
	for (int[] interval : _intervals) {
	    count += ((interval[1] - interval[0]) + 1);
	}
	
	return count;
    }

    /**
     * Returns true if the two sequences overlap. This is the negation of
     * disjoint.
     * 
     * @param sequence
     * @return 
     */
    public boolean overlap(IntegerSequence sequence) {

	int idxI = 0;
	int idxJ = 0;
		
	while ((idxI < this.intervals()) && (idxJ < sequence.intervals())) {
	    IntegerInterval intervalI = this.interval(idxI);
	    IntegerInterval intervalJ = sequence.interval(idxJ);
	    if (intervalI.overlap(intervalJ)) {
		return true;
	    } else if (intervalI.start() < intervalJ.start()) {
		idxI++;
	    } else {
		idxJ++;
	    }
	}
    
	return false;
    }
    
    /**
     * Returns the union of the two integer sequences.
     * 
     * @param sequence
     * @return 
     */
    public IntegerSequence union(IntegerSequence sequence) {
	
	ArrayList<IntegerInterval> intervals = new ArrayList<>();
	
	int idxI = 0;
	int idxJ = 0;
	
	IntegerInterval last = null;
	while ((idxI < this.intervals()) && (idxJ < sequence.intervals())) {
	    IntegerInterval intI = this.interval(idxI);
	    IntegerInterval intJ = sequence.interval(idxJ);
	    IntegerInterval merged;
	    if (last == null) {
		if (intI.end() < intJ.start()) {
		    merged = intI;
		    idxI++;
		} else if (intJ.end() < intI.start()) {
		    merged = intJ;
		    idxJ++;
		} else {
		    merged = _factory.getInterval(Math.min(intI.start(), intJ.start()), Math.max(intI.end(), intJ.end()));
		    idxI++;
		    idxJ++;
		}
	    } else {
		/*
		 * Check if one of the intervals is contained in the last merged
		 * interval.
		 */
		if (last.contains(intI)) {
		    idxI++;
		    continue;
		} else if (last.contains(intJ)) {
		    idxJ++;
		    continue;
		}
		if (intI.end() < (intJ.start() + 1)) {
		    merged = intI;
		    idxI++;
		} else if (intJ.end() < (intI.start() + 1)) {
		    merged = intJ;
		    idxJ++;
		} else {
		    merged = _factory.getInterval(Math.min(intI.start(), intJ.start()), Math.max(intI.end(), intJ.end()));
		    idxI++;
		    idxJ++;
		}
		/*
		 * Check if the merged interval is direct adjacent to the last.
		 * If so, replace last with a merged interval.
		 */
		if ((last.end() + 1) >= merged.start()) {
		    intervals.remove(intervals.size() - 1);
		    merged = _factory.getInterval(last.start(), merged.end());
		}
	    }
	    intervals.add(merged);
	    last = merged;
	}
	
	if (idxI < this.intervals()) {
	    this.unionExtend(this, idxI, intervals);
	} else if (idxJ < sequence.intervals()) {
	    this.unionExtend(sequence, idxJ, intervals);
	}
	
	return _factory.getSequence(intervals);
    }
    
    /**
     * Append list of remaining intervals to a new interval sequence.
     * 
     * @param sequence
     * @param start
     * @param intervals 
     */
    private void unionExtend(IntegerSequence sequence, int start, ArrayList<IntegerInterval> intervals) {
	
	if (intervals.size() > 0) {
	    int index = start;
	    IntegerInterval last = intervals.get(intervals.size() - 1);
	    IntegerInterval interval = sequence.interval(index++);
	    if (!last.contains(interval)) {
		if ((last.end() + 1) >= interval.start()) {
		    intervals.remove(intervals.size() - 1);
		    intervals.add(_factory.getInterval(last.start(), interval.end()));
		} else {
		    intervals.add(interval);
		}
	    }
	    last = intervals.get(intervals.size() - 1);
	    while (index < sequence.intervals()) {
		interval = sequence.interval(index++);
		if (!last.contains(interval)) {
		    if (interval.start() == last.end() + 1) {
			intervals.remove(intervals.size() - 1);
			intervals.add(_factory.getInterval(last.start(), interval.end()));
		    } else {
			intervals.add(interval);
		    }
		    while (index < sequence.intervals()) {
			intervals.add(sequence.interval(index++));
		    }
		}
	    }
	} else {
	    for (int iInterval = start; iInterval < sequence.intervals(); iInterval++) {
		intervals.add(sequence.interval(iInterval));
	    }
	}
    }
}
