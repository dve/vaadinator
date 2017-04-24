package de.akquinet.engineering.vaadinator.dao;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementScanner6;

import org.apache.commons.lang.StringUtils;

import de.akquinet.engineering.vaadinator.model.BeanDescription;
import de.akquinet.engineering.vaadinator.model.EnumValueDescription;

public class EnumScanner extends ElementScanner6<Void, BeanDescription> {
    @Override
    public Void visitVariable(VariableElement e, BeanDescription p) {

        if (e.getKind() == ElementKind.ENUM_CONSTANT) {
            EnumValueDescription enumValue = new EnumValueDescription(p,
                    e.getSimpleName().toString());
            DisplayEnumPrism displayEnumPrism = DisplayEnumPrism
                    .getInstanceOn(e);
            if (displayEnumPrism != null) {
                if (StringUtils.isNotBlank(displayEnumPrism.captionText())) {
                    enumValue.setCaptionText(displayEnumPrism.captionText());
                }
            }

            p.addEnumValue(enumValue);
        }
        return super.visitVariable(e, p);
    }
}
