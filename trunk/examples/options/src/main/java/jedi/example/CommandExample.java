package jedi.example;

import jedi.annotation.JediCommand;
import jedi.option.Option;

import static jedi.option.Options.some;

public class CommandExample {
	
	@JediCommand
	public void doX(String arg) {
		
	}

	@JediCommand
	public void doY() {
		
	}
	
	public static void main(String[] args) {
		CommandExample example = new CommandExample();
		
		Option<String> thing = some("thing");
		thing.match(CommandExampleStaticClosureFactory.doXProxyCommand(example), CommandExampleStaticClosureFactory.doYProxyCommand0(example));
	}
}
