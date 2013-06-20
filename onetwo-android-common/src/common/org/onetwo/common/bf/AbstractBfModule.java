package org.onetwo.common.bf;


abstract public class AbstractBfModule implements BfModule, ContainerAware{
	
	protected Container container;

	public void setContainer(Container container) {
		this.container = container;
	}

	public Container getContainer() {
		return container;
	}
	
}
