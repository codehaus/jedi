package jedi.example;

import static jedi.option.Options.Some;
import jedi.annotation.JediCommand;
import jedi.option.Option;

public class CommandExample {
	
	@JediCommand
	public void doX() {
		
	}

	@JediCommand
	public void doY() {
		
	}
	
	public static void main(String[] args) {
		Option<String> thing = Some("thing");
		thing.match(CommandExampleStaticClosureFactory.doXCommand(), CommandExampleStaticClosureFactory.doYCommand());
	}
}
