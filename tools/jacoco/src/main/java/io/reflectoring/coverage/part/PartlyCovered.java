package io.reflectoring.coverage.part;

import io.reflectoring.coverage.Generated;

class PartlyCovered {

	String covered(String string) {
		string = string.toLowerCase();
		return string;
	}

	String partlyCovered(String string, boolean trigger){
		if(trigger){
			string = string.toLowerCase();
		}
		return string;
	}

}
