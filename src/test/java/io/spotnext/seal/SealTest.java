package io.spotnext.seal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class SealTest {

	@Test
	public void testSealing() {
		final var data = new TestData();
		data.setName("Test");
		final var sealedData = data.seal();
		
		assertEquals("Test", sealedData.getName());
		
		assertThrows(UnsupportedOperationException.class, () -> {
			sealedData.setName("A");
		});
	}
	
	@Disabled
	@Test
	public void testSealedTypesCache() {
		final var data1 = new TestData("Test1");
		final var data2 = new TestData("Test2");
		
		final var dataClass1 = data1.seal().getClass();
		final var dataClass2 = data2.seal().getClass();
		
		assertEquals(dataClass1, dataClass2);
	}
}
