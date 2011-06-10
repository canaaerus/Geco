/**
 * Copyright (c) 2011 Simon Denier
 * Released under the MIT License (see LICENSE file)
 */
package test.net.geco.control;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;

import net.geco.control.GecoControl;
import net.geco.control.RunnerControl;
import net.geco.model.Registry;
import net.geco.model.Stage;
import net.geco.model.impl.RunnerImpl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author Simon Denier
 * @since Jun 6, 2011
 *
 */
public class RunnerControlTest {
	
	private RunnerControl runnerControl;
	private Registry registry;

	@Before
	public void setUp(){
		registry = Mockito.mock(Registry.class);
		Stage stage = Mockito.mock(Stage.class);
		GecoControl gecoControl = Mockito.mock(GecoControl.class);
		Mockito.when(gecoControl.stage()).thenReturn(stage);
		Mockito.when(stage.registry()).thenReturn(registry);
		runnerControl = new RunnerControl(gecoControl);		
	}
	
	@Test
	public void testValidateStartId(){
		HashSet<Integer> startids = new HashSet<Integer>(Arrays.asList( new Integer[]{ 3 } ));
		Mockito.when(registry.getStartIds()).thenReturn(startids);
		RunnerImpl runner = new RunnerImpl();
		runner.setStartId(1);
		
		assertTrue(runnerControl.validateStartId(runner, "1"));
		assertEquals(1, runner.getStartId().intValue());
		assertTrue(runnerControl.validateStartId(runner, "2"));
		assertEquals(2, runner.getStartId().intValue());
		assertFalse(runnerControl.validateStartId(runner, ""));
		assertEquals(2, runner.getStartId().intValue());
		assertFalse(runnerControl.validateStartId(runner, "3"));
		assertEquals(2, runner.getStartId().intValue());
	}

	@Test
	public void testValidateEcard(){
		HashSet<String> ecards = new HashSet<String>(Arrays.asList( new String[]{"3"} ));
		Mockito.when(registry.getEcards()).thenReturn(ecards);
		RunnerImpl runner = new RunnerImpl();
		runner.setEcard("1");
		
		assertTrue(runnerControl.validateEcard(runner, "1"));
		assertEquals("1", runner.getEcard());
		assertTrue(runnerControl.validateEcard(runner, "2"));
		assertEquals("2", runner.getEcard());
		assertTrue(runnerControl.validateEcard(runner, "2a"));
		assertEquals("2a", runner.getEcard());
		assertFalse(runnerControl.validateEcard(runner, "3"));
		assertEquals("2a", runner.getEcard());
		assertTrue(runnerControl.validateEcard(runner, ""));
		assertEquals("", runner.getEcard());
	}
	
	@Test
	public void testDeriveUniqueEcard(){
		HashSet<String> ecards = new HashSet<String>(Arrays.asList( new String[]{"3"} ));
		Mockito.when(registry.getEcards()).thenReturn(ecards);

		assertEquals("", runnerControl.deriveUniqueEcard(""));
		assertEquals("2", runnerControl.deriveUniqueEcard("2"));
		assertEquals("3a", runnerControl.deriveUniqueEcard("3"));

		ecards = new HashSet<String>(Arrays.asList( new String[]{"3", "3a"} ));
		Mockito.when(registry.getEcards()).thenReturn(ecards);
		
		assertEquals("3aa", runnerControl.deriveUniqueEcard("3"));
	}

}
