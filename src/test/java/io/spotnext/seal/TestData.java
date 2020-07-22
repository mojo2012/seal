package io.spotnext.seal;

public class TestData implements Sealable<TestData> {
	private String name;

	public TestData() {
		//
	}

	public TestData(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
