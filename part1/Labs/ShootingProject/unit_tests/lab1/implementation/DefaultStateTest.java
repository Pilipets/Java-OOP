/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab1.implementation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author druid
 */
public class DefaultStateTest {
    /**
     * Test ctor of class DefaultState.
     */
    @Test
    public void testCtorDefaultState() {
        System.out.println("DefaultState");
        DefaultState instance = new DefaultState();
        assertEquals(20f, instance.getCamSpeed(), 1e-4);
        assertEquals(4f, instance.getCamHeight(), 1e-4);
        assertNotNull(instance.getFloor());
    }    
}
