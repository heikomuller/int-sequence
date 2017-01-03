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
package org.data.curation.test.util.intseq;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.data.curation.util.intseq.IntegerSequence;
import org.data.curation.util.intseq.IntegerSequenceFactory;
import org.data.curation.util.intseq.NonNegativeIntegerSequence;
import org.data.curation.util.intseq.NonNegativeIntegerSequenceFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Heiko Mueller <heiko.mueller@nyu.edu>
 */
public class IntegerSequenceTest {
    
    private final NonNegativeIntegerSequenceFactory _factory;
    
    public IntegerSequenceTest() {
	
	_factory = new NonNegativeIntegerSequenceFactory();
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void afterVersionTest() {
	
	IntegerSequence s = _factory.getSequence(new int[][]{{2, 5}, {7, 9}});
	assertTrue("Sequence prefix after 9 should be empty", s.after(9).isEmpty());
	assertTrue("Sequence prefix after 10 should be empty", s.after(10).isEmpty());
	assertFalse("Sequence prefix after 6 should not be empty", s.after(6).isEmpty());
	
	IntegerSequence a6 = s.after(6);
	for (int value : new int[]{7, 8, 9}) {
	    assertTrue(a6 + " should contain " + value, a6.contains(value));
	}
	for (int value : new int[]{2, 3, 4, 5, 6, 10}) {
	    assertFalse(a6 + " should not contain " + value, a6.contains(value));
	}
 	IntegerSequence a8 = s.after(8);
	for (int value : new int[]{9}) {
	    assertTrue(a8 + " should contain " + value, a8.contains(value));
	}
	for (int value : new int[]{2, 3, 4, 5, 6, 7, 8, 10}) {
	    assertFalse(a8 + " should not contain " + value, a8.contains(value));
	}
   }
    
    @Test(expected=java.lang.IllegalArgumentException.class)
    public void appendNegativeValueToEmptySequence() {
	
	_factory.getSequence().append(-1);
    }
    
    @Test(expected=java.lang.IllegalArgumentException.class)
    public void appendNegativeValueToValidSequence() {
	
	_factory.getSequence(new int[][]{{2, 6}, {17, 18}, {29, 40}}).append(-10);
    }
    
    @Test(expected=java.lang.IllegalArgumentException.class)
    public void appendSmallerValueToValidSequence() {
	
	_factory.getSequence(new int[][]{{2, 6}, {17, 18}, {29, 40}}).append(10);
    }
    
    @Test
    public void arrayCopyTest() {
	
	IntegerSequence s = _factory.getSequence(0);
	IntegerSequence s1 = s.append(1);

	assertEquals("Last value of s should be 0", 0, s.lastValue());
	assertEquals("Last value of s should be 1", 1, s1.lastValue());
	
	int[][] intervals = new int[][]{{0, 0}};
	IntegerSequence s2 = _factory.getSequence(intervals);
	intervals[0][1] = 2;
	assertEquals("Last value of s2 should be 0", 0, s2.lastValue());
	
	intervals = new int[][]{{0, 5}, {9, 19}};
	IntegerSequence s3 = _factory.getSequence(intervals);
	intervals[1][1] = 2;
	assertEquals("Last value of s2 should be 0", 0, s2.lastValue());
	assertEquals("Last value of s3 should be 19", 19, s3.lastValue());
    }
    
    @Test
    public void closeTimestampTest() {

	IntegerSequence s = _factory.getSequence(new int[][]{{2, 5}, {7, 9}});
	
	assertTrue("Closed sequence at 1 should be empty", s.close(1).isEmpty());
	assertFalse("Closed sequence at 2 should not be empty", s.close(2).isEmpty());
	assertFalse("Closed sequence at 3 should not be empty", s.close(3).isEmpty());
	
	IntegerSequence c6 = s.close(6);
	for (int value : new int[]{2, 3, 4, 5}) {
	    assertTrue(c6 + " should contain " + value, c6.contains(value));
	}
	for (int value : new int[]{6, 7, 8, 9, 10}) {
	    assertFalse(c6 + " should not contain " + value, c6.contains(value));
	}
	
	IntegerSequence c7 = s.close(7);
	for (int value : new int[]{2, 3, 4, 5, 7}) {
	    assertTrue(c7 + " should contain " + value, c7.contains(value));
	}
	for (int value : new int[]{6, 8, 9, 10}) {
	    assertFalse(c7 + " should not contain " + value, c7.contains(value));
	}

	IntegerSequence c8 = s.close(8);
	for (int value : new int[]{2, 3, 4, 5, 7, 8}) {
	    assertTrue(c8 + " should contain " + value, c8.contains(value));
	}
	for (int value : new int[]{6, 9, 10}) {
	    assertFalse(c8 + " should not contain " + value, c8.contains(value));
	}

	IntegerSequence c9 = s.close(9);
	for (int value : new int[]{2, 3, 4, 5, 7, 8, 9}) {
	    assertTrue(c9 + " should contain " + value, c9.contains(value));
	}
	for (int value : new int[]{6, 10}) {
	    assertFalse(c9 + " should not contain " + value, c9.contains(value));
	}

	IntegerSequence c10 = s.close(10);
	for (int value : new int[]{2, 3, 4, 5, 7, 8, 9}) {
	    assertTrue(c10 + " should contain " + value, c10.contains(value));
	}
	for (int value : new int[]{6, 10, 11}) {
	    assertFalse(c10 + " should not contain " + value, c10.contains(value));
	}
    }
    
    @Test(expected=java.lang.IllegalArgumentException.class)
    public void createSequenceWithInvalidIntervalSizeTest() {
	
	_factory.getSequence(new int[][]{{1, 2},{4, 5, 6}, {9, 10}});
    }
    
    @Test(expected=java.lang.IllegalArgumentException.class)
    public void createSequenceWithNegativeEndInterval() {
	
	_factory.getSequence(new int[][]{{1, 3},{5, -8}, {-5, 9}});
    }
    
    @Test(expected=java.lang.IllegalArgumentException.class)
    public void createSequenceWithNegativeStartInterval() {
	
	_factory.getSequence(new int[][]{{1, 3},{-5, 10},{7, 11}});
    }
    
    @Test(expected=java.lang.IllegalArgumentException.class)
    public void createSequenceWithInvalidInterval() {
	
	_factory.getSequence(new int[][]{{1, 3},{5, 9}, {15, 10}});
    }
    
    @Test(expected=java.lang.IllegalArgumentException.class)
    public void createSequenceWithAdjacentIntervals() {
	
	_factory.getSequence(new int[][]{{1, 3},{3, 5}, {9, 13}});
    }

    @Test
    public void containsTimeSequenceTest() {
	
	NonNegativeIntegerSequence s1 = _factory.getSequence(new int[][]{{1, 10}, {20, 22}, {25, 27}});
	NonNegativeIntegerSequence s2 = _factory.getSequence(new int[][]{{2, 3}, {5, 6}, {21, 22}, {24, 27}});
	NonNegativeIntegerSequence s3 = _factory.getSequence(new int[][]{{21, 21}, {26, 27}});
	NonNegativeIntegerSequence s4 = _factory.getSequence(new int[][]{{1, 10}, {21, 21}, {26, 27}});
	NonNegativeIntegerSequence s5 = _factory.getSequence(new int[][]{{1, 10}, {21, 21}, {26, 40}});
	
	assertTrue("Sequence " + s1.toIntervalString() + " should be contained in itsef", s1.contains(s1));
	assertTrue("Sequence " + s2.toIntervalString() + " should be contained in itsef", s2.contains(s2));
	assertTrue("Sequence " + s3.toIntervalString() + " should be contained in itsef", s3.contains(s3));
	assertTrue("Sequence " + s4.toIntervalString() + " should be contained in itsef", s4.contains(s4));
	assertTrue("Sequence " + s5.toIntervalString() + " should be contained in itsef", s5.contains(s5));
	
	assertFalse("Sequence " + s1.toIntervalString() + " does not contain " + s2.toIntervalString(), s1.contains(s2));
	assertFalse("Sequence " + s2.toIntervalString() + " does not contain " + s1.toIntervalString(), s2.contains(s1));
	
	assertTrue("Sequence " + s1.toIntervalString() + " should contain " + s3.toIntervalString(), s1.contains(s3));
	
	assertTrue("Sequence " + s1.toIntervalString() + " should contain " + s4.toIntervalString(), s1.contains(s4));
	
	assertFalse("Sequence " + s1.toIntervalString() + " should not contain " + s5.toIntervalString(), s1.contains(s5));
	
	assertTrue("Sequence " + s4.toIntervalString() + " should contain " + s3.toIntervalString(), s4.contains(s3));
	
	assertTrue("Sequence " + s5.toIntervalString() + " should contain " + s3.toIntervalString(), s5.contains(s3));
	
	assertFalse("Sequence " + s4.toIntervalString() + " should contain " + s5.toIntervalString(), s4.contains(s5));
	
	assertTrue("Sequence " + s5.toIntervalString() + " should contain " + s4.toIntervalString(), s5.contains(s4));
	
	/*
	 * Failed at some point
	*/
	NonNegativeIntegerSequence f1 = _factory.getSequence(new int[][]{{0, 4}});
	NonNegativeIntegerSequence f2 = _factory.getSequence(new int[][]{{0, 2} ,{4, 4}});
	
	assertTrue("Sequence " + f1.toIntervalString() + " should contain " + f2.toIntervalString(), f1.contains(f2));
    }

    @Test
    public void containsVersionTest() {
	
	NonNegativeIntegerSequence s = _factory.getSequence(new int[][]{{4, 17}, {19, 25}, {27, 27}, {29, 33}, {35, 35}});
	
	assertTrue(s.toIntervalString() + " should contain 4", s.contains(4));
	assertTrue(s.toIntervalString() + " should contain 5", s.contains(5));
 	assertTrue(s.toIntervalString() + " should contain 9", s.contains(9));
 	assertTrue(s.toIntervalString() + " should contain 15", s.contains(15));
 	assertTrue(s.toIntervalString() + " should contain 16", s.contains(16));
 	assertTrue(s.toIntervalString() + " should contain 17", s.contains(17));
 	assertTrue(s.toIntervalString() + " should contain 19", s.contains(19));
 	assertTrue(s.toIntervalString() + " should contain 27", s.contains(27));
 	assertTrue(s.toIntervalString() + " should contain 29", s.contains(29));
 	assertTrue(s.toIntervalString() + " should contain 30", s.contains(30));
 	assertTrue(s.toIntervalString() + " should contain 33", s.contains(33));
 	assertTrue(s.toIntervalString() + " should contain 35", s.contains(35));
	
	assertFalse(s.toIntervalString() + " should not contain 1", s.contains(1));
	assertFalse(s.toIntervalString() + " should not contain 18", s.contains(18));
	assertFalse(s.toIntervalString() + " should not contain 26", s.contains(26));
	assertFalse(s.toIntervalString() + " should not contain 28", s.contains(28));
	assertFalse(s.toIntervalString() + " should not contain 34", s.contains(34));
	assertFalse(s.toIntervalString() + " should not contain 40", s.contains(40));
    }
    
    @Test
    public void emptySequenceTest() {
	
	assertEquals("Length of an epmty sequence should be 0", 0, _factory.getSequence().length());	
    }
    
    @Test
    public void intersectTest() {
	
	NonNegativeIntegerSequence s1 = _factory.getSequence(new int[][]{{1, 10}, {20, 22}, {25, 27}});
	NonNegativeIntegerSequence s2 = _factory.getSequence(new int[][]{{2, 3}, {5, 6}, {21, 22}, {24, 27}});
	NonNegativeIntegerSequence s3 = _factory.getSequence(new int[][]{{21, 21}, {26, 27}});
	NonNegativeIntegerSequence s4 = _factory.getSequence(new int[][]{{5, 9}, {19, 29}});
	NonNegativeIntegerSequence s5 = _factory.getSequence(new int[][]{{11, 19}, {23, 23}, {35, 40}});
	NonNegativeIntegerSequence s6 = _factory.getSequence(new int[][]{{1, 40}});
	
	NonNegativeIntegerSequence s12 = (NonNegativeIntegerSequence)s1.intersect(s2);
	assertEquals("Expected intervals 2-3,5-6,21-22,25-27", "2-3,5-6,21-22,25-27", s12.toIntervalString());
	NonNegativeIntegerSequence s21 = (NonNegativeIntegerSequence)s2.intersect(s1);
	assertEquals("Intersection should be commutative", s12.toIntervalString(), s21.toIntervalString());
	
	NonNegativeIntegerSequence s13 = (NonNegativeIntegerSequence)s1.intersect(s3);
	assertEquals("Expected intervals " + s3.toIntervalString(), s3.toIntervalString(), s13.toIntervalString());
	
	NonNegativeIntegerSequence s16 = (NonNegativeIntegerSequence)s1.intersect(s6);
	assertEquals("Expected intervals " + s1.toIntervalString(), s1.toIntervalString(), s16.toIntervalString());
	
	NonNegativeIntegerSequence s23 = (NonNegativeIntegerSequence)s2.intersect(s3);
	assertTrue(s23 + " should contain 21", s23.contains(21));
	assertTrue(s23 + " should contain 26", s23.contains(26));
	assertTrue(s23 + " should contain 27", s23.contains(27));
	assertFalse(s23 + " should not contain 3", s23.contains(3));
	assertFalse(s23 + " should not contain 5", s23.contains(5));
	assertFalse(s23 + " should not contain 22", s23.contains(22));
	assertFalse(s23 + " should not contain 25", s23.contains(25));
	assertFalse(s23 + " should not contain 30", s23.contains(30));
	
	NonNegativeIntegerSequence s24 = (NonNegativeIntegerSequence)s2.intersect(s4);
	assertEquals("Expected intervals 5-6,21-22,24-27", "5-6,21-22,24-27", s24.toIntervalString());
	
	NonNegativeIntegerSequence s15 = (NonNegativeIntegerSequence)s1.intersect(s5);
	assertTrue("Intersection of s1 and s5 should be empty", s15.isEmpty());
	
	IntegerSequence l1 = _factory.getSequence("0,3,5,12,14-15,18-21,23,25,27,29-31,34-46,49,52-57,59,61,63-72,75,82-89,91-96,98-100,102-113,115-121,123-124,126-133,135-136,138,142-148,150-151,153,156-160,162,164,170-173,175-176,180,182-184,186,189-190,194-195,197");
	IntegerSequence l2 = _factory.getSequence("0-1,3-4,12,14-15,17-21,23,25,27,29-32,34-37,39,42-43,45-46,49,52-59,61,63-72,75,82-89,91-96,98-100,102-124,126-133,135-142,144-153,155,157,160,162,165-166,170-173,175-176,180-181,183,186,189-190,193-195,204");

	IntegerSequence intersect = l1.intersect(l2);
	
	assertTrue("Should contain 195", intersect.contains(195));
    }
    
    @Test
    public void toSequenceTest() {
	
	List<Integer> values = Arrays.asList(1, 2, 3, 4, 6, 9, 10, 13, 14, 15, 22, 23, 24, 25, 30);
	Collections.sort(values);
	IntegerSequence seq = IntegerSequenceFactory.toSequence(values, new NonNegativeIntegerSequenceFactory());
	assertEquals("Length of both sequence representations should be equal", seq.length(), values.size());
	assertEquals("There should be 6 intervals in the integer sequence", 6, seq.intervals());
    }
    
    @Test
    public void unionTest() {
	
	IntegerSequence s = _factory.getSequence(new int[][]{{0, 4}, {6, 7}, {9, 9}});
	s = s.union( _factory.getSequence(10));
	s = s.union( _factory.getSequence(12));
	
	for (int value : new int[]{0, 1, 2, 3, 4, 6, 7, 9, 10, 12}) {
	    assertTrue(s + " should contain " + value, s.contains(value));
	}
	
	for (int value : new int[]{5, 8, 11, 13}) {
	    assertFalse(s + " should not contain " + value, s.contains(value));
	}
	
	IntegerSequence s1 = _factory.getSequence(new int[][]{{1, 5}, {7, 25}});
	IntegerSequence s2 = _factory.getSequence(new int[][]{{1, 2}, {4, 7}, {9, 15}});
	
	IntegerSequence s12 = s1.union(s2);
	for (int iValue = 1; iValue <= 25; iValue++) {
	    assertTrue(s12 + " should contain " + iValue, s12.contains(iValue));
	}
 	assertFalse(s12 + " should not contain " + 26, s12.contains(26));

	IntegerSequence s21 = s2.union(s1);
	for (int iValue = 1; iValue <= 25; iValue++) {
	    assertTrue(s21 + " should contain " + iValue, s21.contains(iValue));
	}
 	assertFalse(s21 + " should not contain " + 26, s21.contains(26));
	
	assertTrue("Union of " + s1 + " with empty sequence should return " + s1, s1.union(_factory.getSequence()).equals(s1));
	assertTrue("Union of " + s1 + " with empty sequence should return " + s1, _factory.getSequence().union(s1).equals(s1));

	IntegerSequence l1 = _factory.getSequence("188,468,472,474");
	IntegerSequence l2 = _factory.getSequence("468-473");
	
	IntegerSequence u12 = l1.union(l2);
	
	assertEquals("The union should contain two intervals", 2, u12.intervals());

	NonNegativeIntegerSequence l3 = _factory.getSequence("188,468,472,475");
	
	IntegerSequence u23 = l2.union(l3);
	
	assertEquals("The union should contain three intervals", 3, u23.intervals());
	
	IntegerSequence li1 = _factory.getSequence("16,30,38,40-41,51,53,56,72-79,88,92,96,99,107-125,128,132,134-135,144-148,158,164-167,170,174,177,180,184,191,193-195,210-216,218-219,221,223,226,229-232,237,241,244,253,261-262,265,271-272,279,286-291,300-308,311,315,322-323,334,338-340,344,351,353,363,365-366,373,377,380,382,390,392-393,418,420,424-428,430,460,466-470,474,477-487,500,502,510-511,516-522,525,529,536-537,539-541,543-546,554-555,562,567-568,572,574-585,595,604");
	IntegerSequence li2 = _factory.getSequence("218-225");

	li1.union(li2);
    }
}
