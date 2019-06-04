package com.crm.elmolino.tracker_crm;

import android.util.Log;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    //@Test
    //public void addition_isCorrect() throws Exception {
    //    assertEquals(4, 2 + 2);
    //}
    @Test
    public void calTest() {

        assertEquals("error in add()",3,Calculator.add(1,2));
        assertEquals("error in add()",-3,Calculator.add(-1,-2));
        assertEquals("error in add()",9,Calculator.add(9,0));
    }

    public void calTestAddFail() {
        assertEquals("error in add()",0,Calculator.add(1,2));
    }


    public void calTestSubPass() {
        assertEquals("error in add()",1,Calculator.add(1,2));
        assertEquals("error in add()",-1,Calculator.add(-1,-2));
        assertEquals("error in add()",0,Calculator.add(2,1));
    }

    public void calcSubFail(){
        assertEquals("error in add()",0,Calculator.add(2, 1));
    }


}