package de.akquinet.engineering.vaadinator.dao;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementKindVisitor6;
import javax.tools.Diagnostic.Kind;

import de.akquinet.engineering.vaadinator.model.BeanDescription;

@SupportedAnnotationTypes({
        "de.akquinet.engineering.vaadinator.annotations.DisplayBean" })
public class VaadinatorProcessor extends AbstractProcessor {

    private List<BeanDescription> beanDescriptions = new ArrayList<>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
            RoundEnvironment roundEnvironment) {
        // nothing to do in the last round
        if (!roundEnvironment.processingOver()) {
            // process any mappers left over from previous rounds

            // get and process any mappers from this round
            Set<TypeElement> displayBeans = getGetDisplayBeans(annotations,
                    roundEnvironment);
            processDisplayBeanElements(displayBeans);
        }
        return false;
    }

    private void processDisplayBeanElements(
            Set<TypeElement> displayBeanElements) {
        for (TypeElement displayBeanElement : displayBeanElements) {
            try {
                process(displayBeanElement);
            } catch (Throwable t) {
                handleUncaughtError(displayBeanElement, t);
                break;
            }
        }

    }

    private void process(TypeElement displayBeanElement) {
        DisplayBeanPrism displayBeanPrisim = DisplayBeanPrism
                .getInstanceOn(displayBeanElement);
        BeanDescription beanDescription = new BeanDescription(
                displayBeanElement.getSimpleName().toString());
        beanDescription.setBeanValidation(displayBeanPrisim.beanValidation());
        beanDescription.setCaptionText(displayBeanPrisim.captionText());
        beanDescription.setCaptionProp(displayBeanPrisim.captionProp());
        beanDescription.setDisplayed(!displayBeanPrisim.ignore());
        beanDescription.setPckg(getPackageName(displayBeanElement));
        if (displayBeanElement.getKind() == ElementKind.ENUM) {
            beanDescription.setEnumeration(true);
            EnumScanner enumScanner = new EnumScanner();
            enumScanner.scan(displayBeanElement, beanDescription);
            beanDescription.setDisplayed(false);
        } else {
            BeanScanner importScanner = new BeanScanner();
            importScanner.setProcessingEnviroment(processingEnv);
            importScanner.scan(displayBeanElement, beanDescription);
            beanDescription.setImports(
                    new ArrayList<>(importScanner.getImportedTypes()));
        }
        beanDescriptions.add(beanDescription);
    }

    private Set<TypeElement> getGetDisplayBeans(
            final Set<? extends TypeElement> annotations,
            final RoundEnvironment roundEnvironment) {
        Set<TypeElement> mapperTypes = new HashSet<TypeElement>();

        for (TypeElement annotation : annotations) {
            // Indicates that the annotation's type isn't on the class path of
            // the compiled
            // project. Let the compiler deal with that and print an appropriate
            // error.
            if (annotation.getKind() != ElementKind.ANNOTATION_TYPE) {
                continue;
            }

            try {
                Set<? extends Element> annotatedMappers = roundEnvironment
                        .getElementsAnnotatedWith(annotation);
                for (Element mapperElement : annotatedMappers) {
                    TypeElement mapperTypeElement = asTypeElement(
                            mapperElement);

                    // on some JDKs, RoundEnvironment.getElementsAnnotatedWith(
                    // ... ) returns types with
                    // annotations unknown to the compiler, even though they are
                    // not declared Mappers
                    if (mapperTypeElement != null && DisplayBeanPrism
                            .getInstanceOn(mapperTypeElement) != null) {
                        mapperTypes.add(mapperTypeElement);
                    }
                }
            } catch (Throwable t) { // whenever that may happen, but just to
                                    // stay on the save side
                handleUncaughtError(annotation, t);
                continue;
            }
        }
        return mapperTypes;
    }

    private TypeElement asTypeElement(Element element) {
        return element.accept(new ElementKindVisitor6<TypeElement, Void>() {
            @Override
            public TypeElement visitTypeAsInterface(TypeElement e, Void p) {
                return e;
            }

            @Override
            public TypeElement visitTypeAsClass(TypeElement e, Void p) {
                return e;
            }

            @Override
            public TypeElement visitTypeAsEnum(TypeElement e, Void p) {
                return e;
            }

        }, null);
    }

    private void handleUncaughtError(Element element, Throwable thrown) {
        StringWriter sw = new StringWriter();
        thrown.printStackTrace(new PrintWriter(sw));

        String reportableStacktrace = sw.toString()
                .replace(System.getProperty("line.separator"), "  ");

        processingEnv.getMessager().printMessage(Kind.ERROR,
                "Internal error in the mapping processor: "
                        + reportableStacktrace,
                element);
    }

    protected List<BeanDescription> getBeanDescriptions() {
        return beanDescriptions;
    }

    /**
     * Returns the package name of the given element. NB: This method requires
     * the given element has the kind of {@link ElementKind#CLASS}.
     *
     * @param element
     * @return the package name
     * 
     */
    public String getPackageName(Element element) {
        return processingEnv.getElementUtils().getPackageOf(element)
                .getQualifiedName().toString();
    }
}
