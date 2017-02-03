package de.akquinet.engineering.vaadinator.dao;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementScanner6;

import org.apache.commons.lang.StringUtils;

import de.akquinet.engineering.vaadinator.annotations.FieldType;
import de.akquinet.engineering.vaadinator.model.BeanDescription;
import de.akquinet.engineering.vaadinator.model.ConstructorDescription;
import de.akquinet.engineering.vaadinator.model.ConstructorParamDescription;
import de.akquinet.engineering.vaadinator.model.DisplayPropertyProfileDescription;
import de.akquinet.engineering.vaadinator.model.PropertyDescription;

public class BeanScanner extends ElementScanner6<Void, BeanDescription> {
	private Set<String> importedTypes = new HashSet<>();
	private ProcessingEnvironment processingEnvironment;

	public Set<String> getImportedTypes() {
		return importedTypes;
	}

	@Override
	public Void visitType(TypeElement e, BeanDescription p) {
		for (TypeMirror interfaceType : e.getInterfaces()) {
			importedTypes.add(interfaceType.toString());
		}
		importedTypes.add(e.getSuperclass().toString());
		return super.visitType(e, p);
	}

	@Override
	public Void visitExecutable(ExecutableElement e, BeanDescription p) {
		TypeMirror returnType = e.getReturnType();
		if (returnType.getKind() == TypeKind.DECLARED) {
			importedTypes.add(returnType.toString());
		}
		if (e.getKind() == ElementKind.CONSTRUCTOR) {
			ConstructorDescription cd = new ConstructorDescription(p);
			p.addConstructor(cd);
			ExecutableElement ee = e;
			for (VariableElement ve : ee.getParameters()) {
				cd.addParam(new ConstructorParamDescription(cd, ve.getSimpleName().toString(),
						processingEnvironment.getTypeUtils().asElement(ve.asType()).getSimpleName().toString()));
			}
		}

		if (e.getKind() == ElementKind.METHOD) {
			String simpleName = e.getSimpleName().toString();
			if (simpleName.startsWith("get")) {
				p.addGetter(simpleName);
			} else if (simpleName.startsWith("set")) {
				p.addSetter(simpleName);
			}
			DisplayPropertyPrism displayPropertyPrism = DisplayPropertyPrism.getInstanceOn(e);
			if (displayPropertyPrism != null) {
				System.out.println(e + " annotated method");
				String propertyClassName = processingEnvironment.getTypeUtils().asElement(e.getReturnType())
						.getSimpleName().toString();
				if (simpleName != null && simpleName.length() >= 4
						&& (simpleName.startsWith("set") || simpleName.startsWith("get"))) {
					String propertyName = simpleName.substring(3, 4).toLowerCase() + simpleName.substring(4);
					createPropertyDescriptions(p, displayPropertyPrism, propertyClassName, propertyName);
				}
			}
		}
		return super.visitExecutable(e, p);
	}

	@Override
	public Void visitTypeParameter(TypeParameterElement e, BeanDescription p) {
		if (e.asType().getKind() == TypeKind.DECLARED) {
			importedTypes.add(e.asType().toString());
		}

		return super.visitTypeParameter(e, p);
	}

	@Override
	public Void visitVariable(VariableElement e, BeanDescription p) {
		if (e.asType().getKind() == TypeKind.DECLARED) {
			importedTypes.add(e.asType().toString());
		}

		if (e.getKind() == ElementKind.FIELD) {
			DisplayPropertyPrism displayPropertyPrism = DisplayPropertyPrism.getInstanceOn(e);
			if (displayPropertyPrism != null) {
				System.out.println(e + " annotated field");
				String propertyClassName = processingEnvironment.getTypeUtils().asElement(e.asType()).getSimpleName()
						.toString();
				String propertyName = e.getSimpleName().toString();
				createPropertyDescriptions(p, displayPropertyPrism, propertyClassName, propertyName);
			}
		}
		return super.visitVariable(e, p);
	}

	private void createPropertyDescriptions(BeanDescription p, DisplayPropertyPrism displayPropertyPrism,
			String propertyClassName, String propertyName) {

		PropertyDescription pd = new PropertyDescription(p, propertyName, propertyClassName);
		pd.setDisplayed(!displayPropertyPrism.ignore());
		if (StringUtils.isNotBlank(displayPropertyPrism.captionText())) {
			pd.setCaptionText(displayPropertyPrism.captionText());
		}
		if (StringUtils.isNotBlank(displayPropertyPrism.captionProp())) {
			pd.setCaptionProp(displayPropertyPrism.captionProp());
		}
		p.addProperty(pd);
		System.out.println("Creaded property " + pd);
		addProfiles(pd, displayPropertyPrism);
	}

	private void addProfiles(PropertyDescription pd, DisplayPropertyPrism displayPropertyPrism) {
		for (DisplayPropertySettingPrism dpsp : displayPropertyPrism.profileSettings()) {
			DisplayPropertyProfileDescription dpp = pd.getDisplayPropertyProfile(dpsp.profileName());
			System.out.println("sectionName: " + dpsp.sectionName());
			dpp.setSectionName(dpsp.sectionName());
			dpp.setShowInDetail(dpsp.showInDetail());
			dpp.setShowInTable(dpsp.showInTable());
			dpp.setFieldType(FieldType.valueOf(dpsp.fieldType()));
		}

	}

	public void setProcessingEnviroment(ProcessingEnvironment processingEnv) {
		this.processingEnvironment = processingEnv;
	}
}
