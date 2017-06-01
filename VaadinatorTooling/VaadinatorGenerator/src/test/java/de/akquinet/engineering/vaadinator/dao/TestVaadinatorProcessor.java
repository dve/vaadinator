package de.akquinet.engineering.vaadinator.dao;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;

import de.akquinet.engineering.vaadinator.annotations.FieldType;
import de.akquinet.engineering.vaadinator.model.BeanDescription;
import de.akquinet.engineering.vaadinator.model.ConstructorDescription;

public class TestVaadinatorProcessor {
    @Test
    public void testEnumeration() {
        VaadinatorProcessor mappingProcessor = new VaadinatorProcessor();
        Compilation compilation = javac().withProcessors(mappingProcessor)
                .withOptions("-Adebug=true")
                .compile(JavaFileObjects.forResource(
                        "de/akquinet/engineering/vaadinator/example/address/model/Anreden.java"));
        assertThat(compilation).succeeded();

        Assert.assertThat(mappingProcessor.getBeanDescriptions().size(), is(1));
        BeanDescription desc = mappingProcessor.getBeanDescriptions().get(0);
        assertEquals("Anreden", desc.getClassName());
        assertTrue(desc.isEnumeration());
        assertEquals(3, desc.getEnumValues().size());
        assertNull(desc.getEnumValue("gibtesnicht"));
        assertNotNull(desc.getEnumValue("HERR"));
        assertEquals("HERR", desc.getEnumValue("HERR").getValue());
        assertEquals("Herr", desc.getEnumValue("HERR").getEffectiveCaption());
        assertNotNull(desc.getEnumValue("FRAU"));
        assertEquals("FRAU", desc.getEnumValue("FRAU").getValue());
        assertEquals("Frau", desc.getEnumValue("FRAU").getEffectiveCaption());
        assertNotNull(desc.getEnumValue("FROLLEIN"));
        assertEquals("FROLLEIN", desc.getEnumValue("FROLLEIN").getValue());
        assertEquals("Fräulein",
                desc.getEnumValue("FROLLEIN").getCaptionText());
        assertEquals("Fräulein",
                desc.getEnumValue("FROLLEIN").getEffectiveCaption());
        assertFalse(desc.isDisplayed());
        assertFalse(desc.isMapped());
        assertFalse(desc.isWrapped());
    }

    @Test
    public void test() {
        VaadinatorProcessor mappingProcessor = new VaadinatorProcessor();
        Compilation compilation = javac().withProcessors(mappingProcessor)
                .compile(JavaFileObjects.forResource(
                        "de/akquinet/engineering/vaadinator/example/address/model/Address.java"));
        assertThat(compilation).succeeded();

        Assert.assertThat(mappingProcessor.getBeanDescriptions().size(), is(1));
        BeanDescription desc = mappingProcessor.getBeanDescriptions().get(0);

        assertEquals("Address", desc.getClassName());
        assertEquals("Addresses", desc.getClassNameMultiple());
        assertEquals("address", desc.getClassNamePass());
        assertEquals("de.akquinet.engineering.vaadinator.example.address.model",
                desc.getPckg());
        assertEquals("de.akquinet.engineering.vaadinator.example.address",
                desc.getBasePckg());
        assertEquals(
                "de.akquinet.engineering.vaadinator.example.address.ui.std.presenter",
                desc.getPresenterPckg("std"));
        assertEquals(
                "de.akquinet.engineering.vaadinator.example.address.ui.std.view",
                desc.getViewPckg("std"));
        assertNull(desc.getSuperClassName());
        assertFalse(desc.hasSuperClass());
        assertFalse(desc.isEnumeration());
        // Use Mapstruct instead
        // assertTrue(desc.isMapped());
        assertTrue(desc.isDisplayed());
        assertFalse(desc.isWrapped());
        assertEquals("Adresse", desc.getCaptionText());
        assertEquals("Adresse", desc.getEffectiveCaption());
        assertEquals("Adressen", desc.getEffectiveCaptionPlural());
        // null is not a valid default value
        assertEquals("", desc.getCaptionProp());
        System.out.println(desc.getImports());
        assertEquals(5, desc.getImports().size());
        assertTrue(
                desc.getImports().toString().contains("java.io.Serializable"));
        assertTrue(desc.getImports().toString().contains("java.util.Date"));
        assertFalse(desc.getImports().toString().contains("java.util.List"));
        assertEquals(2, desc.getConstructors().size());
        boolean hasZeroCon = false;
        boolean hasFourCon = false;
        for (ConstructorDescription cdesc : desc.getConstructors()) {
            if (cdesc.getParams().size() > 0) {
                hasFourCon = true;
                assertEquals(4, cdesc.getParams().size());
                assertEquals("anrede, vorname, nachname, email",
                        cdesc.getConstructorParamPassStr());
                assertEquals("anrede", cdesc.getParams().get(0).getParamName());
                assertEquals("Anreden",
                        cdesc.getParams().get(0).getParamClassName());
            } else {
                hasZeroCon = true;
            }
        }
        assertTrue(hasZeroCon);
        assertTrue(hasFourCon);
        // assertEquals(3, desc.getMapProfiles().size());
        // assertNotNull(desc.getMapProfile("gibtesnicht"));
        // assertNotNull(desc.getMapProfile("full"));
        // assertEquals("mapFull",
        // desc.getMapProfile("full").getProfileMapperName());
        // assertEquals(6,
        // desc.getMapProfile("full").getPropertiesInProfile().size());
        // assertEquals("Address",
        // desc.getMapProfile("full").getTargetClassName());
        // assertNotNull(desc.getMapProfile("restricted"));
        // assertEquals("mapRestricted",
        // desc.getMapProfile("restricted").getProfileMapperName());
        // assertEquals(4,
        // desc.getMapProfile("restricted").getPropertiesInProfile().size());
        // assertEquals("Address",
        // desc.getMapProfile("restricted").getTargetClassName());
        // assertNotNull(desc.getMapProfile("mini"));
        // assertEquals("mapMini",
        // desc.getMapProfile("mini").getProfileMapperName());
        // assertEquals(1,
        // desc.getMapProfile("mini").getPropertiesInProfile().size());
        // assertEquals("Address",
        // desc.getMapProfile("mini").getTargetClassName());
        // assertNotNull(desc.getMapProfile("dto"));
        assertEquals(2, desc.getSectionNames().size());
        assertTrue(desc.getSectionNames().toString().contains("Basisdaten"));
        assertTrue(desc.getSectionNames().toString().contains("Mehr Infos"));
        assertEquals(1, desc.getDisplayProfiles().size());
        assertNotNull(desc.getDisplayProfile("gibtesnicht"));
        assertNotNull(desc.getDisplayProfile("std"));
        assertEquals(
                "\"anrede\", \"vorname\", \"nachname\", \"numberCats\", \"email\", \"geburtsdatum\"",
                desc.getDisplayProfile("std")
                        .getSortedPropertiesInProfileForDetailStr());
        assertEquals("\"name\", \"email\"", desc.getDisplayProfile("std")
                .getSortedPropertiesInProfileForTableStr());
        assertTrue(desc.getDisplayProfile("std").isShowOnFirstPage());
        assertEquals(0, desc.getDisplayProfile("std").getOrder());
        assertEquals(2,
                desc.getDisplayProfile("std").getSectionsInProfile().size());
        assertTrue(desc.getDisplayProfile("std").getSectionsInProfile()
                .toString().contains("Basisdaten"));
        assertTrue(desc.getDisplayProfile("std").getSectionsInProfile()
                .toString().contains("Mehr Infos"));
        // remove id, it was only mapped
        assertEquals(7, desc.getProperties().size());
        assertNull(desc.getProperty("gibtesnicht"));

        assertNotNull(desc.getProperty("anrede"));
        assertEquals("Anreden",
                desc.getProperty("anrede").getPropertyClassName());
        assertEquals("Anreden",
                desc.getProperty("anrede").getPropertyClassNameBoxed());
        assertFalse(desc.getProperty("anrede").isPropertyClassRange());
        assertTrue(desc.getProperty("anrede").isDisplayed());
        assertEquals("Anrede", desc.getProperty("anrede").getCaptionText());
        assertNull(desc.getProperty("anrede").getCaptionProp());
        assertEquals("getAnrede",
                desc.getProperty("anrede").getPropertyGetterName());
        assertEquals("setAnrede",
                desc.getProperty("anrede").getPropertySetterName());
        assertEquals("Anrede",
                desc.getProperty("anrede").getEffectiveCaption());
        assertNotNull(desc.getProperty("anrede").getEnumClass(Collections
                .singletonList(new BeanDescription("Anreden", true))));
        assertNotNull(desc.getProperty("anrede").getMapPropertyProfile("full"));
        assertFalse(desc.getProperty("anrede").getMapPropertyProfile("full")
                .isExcluded());
        assertFalse(desc.getProperty("anrede").getMapPropertyProfile("full")
                .isIncluded());
        assertEquals("anrede", desc.getProperty("anrede")
                .getMapPropertyProfile("full").getTargetPropertyName());
        assertEquals("getAnrede", desc.getProperty("anrede")
                .getMapPropertyProfile("full").getTargetPropertyGetterName());
        assertEquals("setAnrede", desc.getProperty("anrede")
                .getMapPropertyProfile("full").getTargetPropertySetterName());
        assertNull(desc.getProperty("anrede").getMapPropertyProfile("full")
                .getTargetPropertyClassName());
        assertNotNull(
                desc.getProperty("anrede").getMapPropertyProfile("restricted"));
        assertFalse(desc.getProperty("anrede")
                .getMapPropertyProfile("restricted").isExcluded());
        assertFalse(desc.getProperty("anrede")
                .getMapPropertyProfile("restricted").isIncluded());
        assertNotNull(
                desc.getProperty("anrede").getDisplayPropertyProfile("std"));
        assertEquals(FieldType.DROPDOWN, desc.getProperty("anrede")
                .getDisplayPropertyProfile("std").getFieldType());
        assertTrue(desc.getProperty("anrede").getDisplayPropertyProfile("std")
                .isFieldTypeAuswahlAusListe());
        assertEquals("Basisdaten", desc.getProperty("anrede")
                .getDisplayPropertyProfile("std").getSectionName());
        assertFalse(desc.getProperty("anrede").getDisplayPropertyProfile("std")
                .isShowInTable());
        assertTrue(desc.getProperty("anrede").getDisplayPropertyProfile("std")
                .isShowInDetail());
        assertNotNull(desc.getProperty("vorname"));
        assertEquals("String",
                desc.getProperty("vorname").getPropertyClassName());
        assertEquals("String",
                desc.getProperty("vorname").getPropertyClassNameBoxed());
        assertFalse(desc.getProperty("vorname").isPropertyClassRange());
        assertTrue(desc.getProperty("vorname").isDisplayed());
        assertNull(desc.getProperty("vorname").getCaptionText());
        assertNull(desc.getProperty("vorname").getCaptionProp());
        assertEquals("getVorname",
                desc.getProperty("vorname").getPropertyGetterName());
        assertEquals("setVorname",
                desc.getProperty("vorname").getPropertySetterName());
        assertEquals("Vorname",
                desc.getProperty("vorname").getEffectiveCaption());
        assertNotNull(
                desc.getProperty("vorname").getMapPropertyProfile("full"));
        assertFalse(desc.getProperty("vorname").getMapPropertyProfile("full")
                .isExcluded());
        assertFalse(desc.getProperty("vorname").getMapPropertyProfile("full")
                .isIncluded());
        assertNotNull(desc.getProperty("vorname")
                .getMapPropertyProfile("restricted"));
        assertFalse(desc.getProperty("vorname")
                .getMapPropertyProfile("restricted").isExcluded());
        assertFalse(desc.getProperty("vorname")
                .getMapPropertyProfile("restricted").isIncluded());
        assertNotNull(
                desc.getProperty("vorname").getDisplayPropertyProfile("std"));
        assertEquals(FieldType.TEXTFIELD, desc.getProperty("vorname")
                .getDisplayPropertyProfile("std").getFieldType());
        assertFalse(desc.getProperty("vorname").getDisplayPropertyProfile("std")
                .isFieldTypeAuswahlAusListe());
        assertEquals("Basisdaten", desc.getProperty("vorname")
                .getDisplayPropertyProfile("std").getSectionName());
        assertFalse(desc.getProperty("vorname").getDisplayPropertyProfile("std")
                .isShowInTable());
        assertTrue(desc.getProperty("vorname").getDisplayPropertyProfile("std")
                .isShowInDetail());
        assertNotNull(desc.getProperty("nachname"));
        assertEquals("String",
                desc.getProperty("nachname").getPropertyClassName());
        assertEquals("String",
                desc.getProperty("vorname").getPropertyClassNameBoxed());
        assertFalse(desc.getProperty("vorname").isPropertyClassRange());
        assertTrue(desc.getProperty("nachname").isDisplayed());
        assertNull(desc.getProperty("nachname").getCaptionText());
        assertNull(desc.getProperty("nachname").getCaptionProp());
        assertEquals("getNachname",
                desc.getProperty("nachname").getPropertyGetterName());
        assertEquals("setNachname",
                desc.getProperty("nachname").getPropertySetterName());
        assertEquals("Nachname",
                desc.getProperty("nachname").getEffectiveCaption());
        assertNotNull(
                desc.getProperty("nachname").getMapPropertyProfile("full"));
        assertFalse(desc.getProperty("nachname").getMapPropertyProfile("full")
                .isExcluded());
        assertFalse(desc.getProperty("nachname").getMapPropertyProfile("full")
                .isIncluded());
        assertNotNull(desc.getProperty("nachname")
                .getMapPropertyProfile("restricted"));
        assertFalse(desc.getProperty("nachname")
                .getMapPropertyProfile("restricted").isExcluded());
        assertFalse(desc.getProperty("nachname")
                .getMapPropertyProfile("restricted").isIncluded());
        assertNotNull(
                desc.getProperty("nachname").getDisplayPropertyProfile("std"));
        assertEquals(FieldType.TEXTFIELD, desc.getProperty("nachname")
                .getDisplayPropertyProfile("std").getFieldType());
        assertFalse(desc.getProperty("nachname")
                .getDisplayPropertyProfile("std").isFieldTypeAuswahlAusListe());
        assertEquals("Basisdaten", desc.getProperty("nachname")
                .getDisplayPropertyProfile("std").getSectionName());
        assertFalse(desc.getProperty("nachname")
                .getDisplayPropertyProfile("std").isShowInTable());
        assertTrue(desc.getProperty("nachname").getDisplayPropertyProfile("std")
                .isShowInDetail());
        assertNotNull(desc.getProperty("name"));
        assertEquals("String", desc.getProperty("name").getPropertyClassName());
        assertEquals("String",
                desc.getProperty("name").getPropertyClassNameBoxed());
        assertFalse(desc.getProperty("name").isPropertyClassRange());
        assertFalse(desc.getProperty("name").isMapped());
        assertTrue(desc.getProperty("name").isDisplayed());
        assertNull(desc.getProperty("name").getCaptionText());
        assertNull(desc.getProperty("name").getCaptionProp());
        assertEquals("getName",
                desc.getProperty("name").getPropertyGetterName());
        assertEquals("setName",
                desc.getProperty("name").getPropertySetterName());
        assertEquals("Name", desc.getProperty("name").getEffectiveCaption());
        assertNotNull(
                desc.getProperty("name").getDisplayPropertyProfile("std"));
        assertEquals(FieldType.TEXTFIELD, desc.getProperty("name")
                .getDisplayPropertyProfile("std").getFieldType());
        assertFalse(desc.getProperty("name").getDisplayPropertyProfile("std")
                .isFieldTypeAuswahlAusListe());
        assertEquals("Basisdaten", desc.getProperty("name")
                .getDisplayPropertyProfile("std").getSectionName());
        assertTrue(desc.getProperty("name").getDisplayPropertyProfile("std")
                .isShowInTable());
        assertFalse(desc.getProperty("name").getDisplayPropertyProfile("std")
                .isShowInDetail());
        assertNotNull(desc.getProperty("email"));
        assertEquals("String",
                desc.getProperty("email").getPropertyClassName());
        assertEquals("String",
                desc.getProperty("email").getPropertyClassNameBoxed());
        assertFalse(desc.getProperty("email").isPropertyClassRange());
        assertTrue(desc.getProperty("email").isDisplayed());
        assertEquals("E-Mail", desc.getProperty("email").getCaptionText());
        assertNull(desc.getProperty("email").getCaptionProp());
        assertEquals("getEmail",
                desc.getProperty("email").getPropertyGetterName());
        assertEquals("setEmail",
                desc.getProperty("email").getPropertySetterName());
        assertEquals("E-Mail", desc.getProperty("email").getEffectiveCaption());
        assertNotNull(desc.getProperty("email").getMapPropertyProfile("full"));
        assertFalse(desc.getProperty("email").getMapPropertyProfile("full")
                .isExcluded());
        assertFalse(desc.getProperty("email").getMapPropertyProfile("full")
                .isIncluded());
        assertNotNull(
                desc.getProperty("email").getDisplayPropertyProfile("std"));
        assertEquals(FieldType.TEXTFIELD, desc.getProperty("email")
                .getDisplayPropertyProfile("std").getFieldType());
        assertFalse(desc.getProperty("email").getDisplayPropertyProfile("std")
                .isFieldTypeAuswahlAusListe());
        assertEquals("Basisdaten", desc.getProperty("email")
                .getDisplayPropertyProfile("std").getSectionName());
        assertTrue(desc.getProperty("email").getDisplayPropertyProfile("std")
                .isShowInTable());
        assertTrue(desc.getProperty("email").getDisplayPropertyProfile("std")
                .isShowInDetail());
        assertNotNull(desc.getProperty("geburtsdatum"));
        assertEquals("Date",
                desc.getProperty("geburtsdatum").getPropertyClassName());
        assertEquals("Date",
                desc.getProperty("geburtsdatum").getPropertyClassNameBoxed());
        assertTrue(desc.getProperty("geburtsdatum").isPropertyClassRange());
        assertTrue(desc.getProperty("geburtsdatum").isDisplayed());
        assertNull(desc.getProperty("geburtsdatum").getCaptionText());
        assertNull(desc.getProperty("geburtsdatum").getCaptionProp());
        assertEquals("getGeburtsdatum",
                desc.getProperty("geburtsdatum").getPropertyGetterName());
        assertEquals("setGeburtsdatum",
                desc.getProperty("geburtsdatum").getPropertySetterName());
        assertEquals("Geburtsdatum",
                desc.getProperty("geburtsdatum").getEffectiveCaption());
        assertNotNull(
                desc.getProperty("geburtsdatum").getMapPropertyProfile("full"));
        assertFalse(desc.getProperty("geburtsdatum")
                .getMapPropertyProfile("full").isExcluded());
        assertFalse(desc.getProperty("geburtsdatum")
                .getMapPropertyProfile("full").isIncluded());
        assertNotNull(desc.getProperty("geburtsdatum")
                .getDisplayPropertyProfile("std"));
        assertEquals(FieldType.DATEPICKER, desc.getProperty("geburtsdatum")
                .getDisplayPropertyProfile("std").getFieldType());
        assertFalse(desc.getProperty("geburtsdatum")
                .getDisplayPropertyProfile("std").isFieldTypeAuswahlAusListe());
        assertEquals("Mehr Infos", desc.getProperty("geburtsdatum")
                .getDisplayPropertyProfile("std").getSectionName());
        assertFalse(desc.getProperty("geburtsdatum")
                .getDisplayPropertyProfile("std").isShowInTable());
        assertTrue(desc.getProperty("geburtsdatum")
                .getDisplayPropertyProfile("std").isShowInDetail());
        assertTrue(desc.hasGetter("getAnrede"));
        assertTrue(desc.hasSetter("setAnrede"));
        assertTrue(desc.hasGetter("getName"));
        assertFalse(desc.hasSetter("setName"));
        assertFalse(desc.isBeanValidation());
    }
}
