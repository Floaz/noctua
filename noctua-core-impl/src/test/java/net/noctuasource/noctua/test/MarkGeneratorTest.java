/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.noctuasource.noctua.test;

import net.noctuasource.noctua.core.test.MarkGenerator;
import net.noctuasource.noctua.core.test.SimpleGermanMarkGenerator;
import org.junit.Assert;
import org.junit.Test;

/**
 * MarkGeneratorTest.
 * @author Philipp Thomas
 */
public class MarkGeneratorTest {


	@Test
	public void germanMarkGeneratorTest() {
		MarkGenerator gen = new SimpleGermanMarkGenerator();

		Assert.assertEquals("1", gen.generateMark(145));
		Assert.assertEquals("1", gen.generateMark(100));
		Assert.assertEquals("1", gen.generateMark(92));
		Assert.assertNotEquals("1", gen.generateMark(91));

		Assert.assertEquals("2", gen.generateMark(91));
		Assert.assertEquals("2", gen.generateMark(81));

		Assert.assertEquals("6", gen.generateMark(1));
		Assert.assertEquals("6", gen.generateMark(0));
		Assert.assertEquals("6", gen.generateMark(-1));
		Assert.assertEquals("6", gen.generateMark(-200));
	}
}