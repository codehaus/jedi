package jedi.example;

import static jedi.option.Options.Some;
import jedi.annotation.JediCommand;
import jedi.option.None;
import jedi.option.Option;

public class CommandExample {
	
	@JediCommand
	public void doX(String arg) {
		
	}

	@JediCommand
	public void doY(None<String> arg) {
		
	}
	
	public static void main(String[] args) {
		CommandExample example = new CommandExample();
		
		Option<String> thing = Some("thing");
		thing.match(CommandExampleStaticClosureFactory.doXProxyCommand(example), CommandExampleStaticClosureFactory.doYProxyCommand(example));
	}
}
