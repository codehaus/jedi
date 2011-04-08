package jedi.example;

import jedi.annotation.JediCommand;

import static jedi.option.Options.some;

public class CommandExample {
	
	@JediCommand
	public void doX(String arg) {
		System.out.println("doX called with " + arg);
	}

	@JediCommand
	public void doY() {
	    System.out.println("doY called");
	}
	
	public static void main(String[] args) {
		CommandExample example = new CommandExample();
		
		some("thing").match(CommandExampleStaticClosureFactory.doXProxyCommand(example), CommandExampleStaticClosureFactory.doYProxyCommand0(example));
	}
}
