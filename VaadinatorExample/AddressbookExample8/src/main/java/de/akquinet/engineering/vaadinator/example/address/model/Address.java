/*
 * Copyright 2014 akquinet engineering GmbH
 *  
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package de.akquinet.engineering.vaadinator.example.address.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import de.akquinet.engineering.vaadinator.annotations.DisplayBean;
import de.akquinet.engineering.vaadinator.annotations.DisplayProperty;
import de.akquinet.engineering.vaadinator.annotations.DisplayPropertySetting;
import de.akquinet.engineering.vaadinator.annotations.FieldType;

@DisplayBean(captionText = "Adresse", beanValidation = true)
@Entity
@Access(AccessType.FIELD)
public class Address implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Address() {
		super();
	}

	public Address(Anreden anrede, String vorname, String nachname, String email) {
		super();
		this.anrede = anrede;
		this.vorname = vorname;
		this.nachname = nachname;
		this.email = email;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@DisplayProperty(profileSettings = { @DisplayPropertySetting(showInTable = false, showInDetail = false) })
	private long id;
	@DisplayProperty(profileSettings = {
			@DisplayPropertySetting(fieldType = FieldType.DROPDOWN) }, captionText = "Anrede")
	private Anreden anrede;
	@DisplayProperty

	private String vorname;
	@DisplayProperty(profileSettings = { @DisplayPropertySetting(required = true) })
	private String nachname;
	private String email;
	@DisplayProperty(profileSettings = { @DisplayPropertySetting(showInTable = true) })
	@Temporal(TemporalType.DATE)
	private Date geburtsdatum;
	@DisplayProperty(converterClassName = "com.vaadin.v7.data.util.converter.StringToIntegerConverter", captionText = "# Katzen")
	@Max(9)
	@Min(0)
	private int numberCats = 0;
	@DisplayProperty(sortable = false, profileSettings = @DisplayPropertySetting(customMultiAuswahlAusListe = true, customClassName = "com.vaadin.v7.ui.TwinColSelect", fieldType = FieldType.CUSTOM))
	@Transient
	private Set<Filme> magFilme = new HashSet<>();

	public Anreden getAnrede() {
		return anrede;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@DisplayProperty(profileSettings = {
			@DisplayPropertySetting(showInTable = true, showInDetail = false, useAsSummary = true) })
	public String getName() {
		return getVorname() + " " + getNachname();
	}

	public void setAnrede(Anreden anrede) {
		this.anrede = anrede;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	@DisplayProperty(captionText = "E-Mail", profileSettings = { @DisplayPropertySetting(showInTable = true) })
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@DisplayProperty(profileSettings = {
			@DisplayPropertySetting(sectionName = "Mehr Infos", fieldType = FieldType.DATEPICKER) })
	public Date getGeburtsdatum() {
		return geburtsdatum;
	}

	public void setGeburtsdatum(Date geburtsdatum) {
		this.geburtsdatum = geburtsdatum;
	}

	public int getNumberCats() {
		return numberCats;
	}

	public void setNumberCats(int numberCats) {
		this.numberCats = numberCats;
	}

	public Set<Filme> getMagFilme() {
		return magFilme;
	}

	public void setMagFilme(Set<Filme> magFilme) {
		this.magFilme = magFilme;
	}

	@Column(length = 1024)
	@Access(AccessType.PROPERTY)
	public String getMagFilmeAsString() {
		StringBuffer res = new StringBuffer();
		Set<Filme> set = getMagFilme();
		if (set != null) {
			for (Filme f : set) {
				if (res.length() > 0) {
					res.append(",");
				}
				res.append(f.name());
			}
		}
		return res.toString();
	}

	public void setMagFilmeAsString(String magFilmeAsString) {
		String[] names = (magFilmeAsString != null ? magFilmeAsString : "").split(",");
		HashSet<Filme> set = new HashSet<>();
		for (String name : names) {
			if (name == null || "".equals(name)) {
				continue;
			}
			set.add(Filme.valueOf(name));
		}
		setMagFilme(set);
	}

	@Override
	public int hashCode() {
		if (id != 0) {
			return (int) id;
		}
		final int prime = 31;
		int result = 1;
		result = prime * result + ((anrede == null) ? 0 : anrede.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((geburtsdatum == null) ? 0 : geburtsdatum.hashCode());
		result = prime * result + ((nachname == null) ? 0 : nachname.hashCode());
		result = prime * result + ((vorname == null) ? 0 : vorname.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Address other = (Address) obj;
		if (id != 0) {
			return id == other.id;
		}
		if (anrede != other.anrede) {
			return false;
		}
		if (email == null) {
			if (other.email != null) {
				return false;
			}
		} else if (!email.equals(other.email)) {
			return false;
		}
		if (geburtsdatum == null) {
			if (other.geburtsdatum != null) {
				return false;
			}
		} else if (!geburtsdatum.equals(other.geburtsdatum)) {
			return false;
		}
		if (nachname == null) {
			if (other.nachname != null) {
				return false;
			}
		} else if (!nachname.equals(other.nachname)) {
			return false;
		}
		if (vorname == null) {
			if (other.vorname != null) {
				return false;
			}
		} else if (!vorname.equals(other.vorname)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Address [id=" + id + ", anrede=" + anrede + ", vorname=" + vorname + ", nachname=" + nachname
				+ ", email=" + email + ", geburtsdatum=" + geburtsdatum + ", numberCats=" + numberCats + ", magFilme="
				+ magFilme + ", getName()=" + getName() + "]";
	}

}
