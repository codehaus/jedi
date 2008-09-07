package jedi.example;

import static jedi.option.Options.Some;
import jedi.annotation.JediCommand;
import jedi.option.Option;

public class CommandExample {
	
	@JediCommand
	public void doX(String arg) {
		
	}

	@JediCommand
	public void doY(String arg) {
		
	}
	
	public static void main(String[] args) {
		Option<String> thing = Some("thing");
		thing.match(CommandExampleStaticClosureFactory.doXCommand(), CommandExampleStaticClosureFactory.doYCommand());
	}
}
