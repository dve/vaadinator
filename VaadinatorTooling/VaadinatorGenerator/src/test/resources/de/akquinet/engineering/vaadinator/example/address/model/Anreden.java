package de.akquinet.engineering.vaadinator.example.address.model;

import de.akquinet.engineering.vaadinator.annotations.DisplayBean;
import de.akquinet.engineering.vaadinator.annotations.DisplayEnum;

@DisplayBean
public enum Anreden {
	HERR, FRAU, @DisplayEnum(captionText = "Fräulein")
	FROLLEIN
}
