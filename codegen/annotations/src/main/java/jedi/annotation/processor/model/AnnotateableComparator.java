package jedi.annotation.processor.model;

import java.util.Comparator;
import java.util.List;

public class AnnotateableComparator implements Comparator<Annotateable> {
	public int compare(Annotateable annotateable1, Annotateable annotateable2) {
		int comparison = annotateable1.getName(true).compareToIgnoreCase(annotateable2.getName(true));
		if (comparison != 0) {
			return comparison;
		}
		return compareUncutParameters(annotateable1.getUncutParameters(), annotateable2.getUncutParameters());
	}

	private int compareUncutParameters(List<Attribute> attributes1, List<Attribute> attributes2) {
		for (int i = 0; i < attributes1.size(); i++) {
			if (i >= attributes2.size()) {
				return -1;
			}
			Attribute p1 = attributes1.get(i);
			Attribute p2 = attributes2.get(i);
			int comparison = p1.getType().compareToIgnoreCase(p2.getType());
			if (comparison != 0) {
				return comparison;
			}
		}
		return 0;
	}
}