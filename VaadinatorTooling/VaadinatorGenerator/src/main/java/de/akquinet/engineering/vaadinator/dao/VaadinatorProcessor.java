package de.akquinet.engineering.vaadinator.dao;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import com.google.auto.common.BasicAnnotationProcessor;
import com.google.auto.service.AutoService;
import com.google.common.collect.SetMultimap;

import de.akquinet.engineering.vaadinator.annotations.DisplayBean;
import de.akquinet.engineering.vaadinator.model.BeanDescription;

@AutoService(Processor.class)
@SupportedAnnotationTypes({
        "de.akquinet.engineering.vaadinator.annotations.DisplayBean" })
public class VaadinatorProcessor extends BasicAnnotationProcessor {
    private List<BeanDescription> beanDescriptions = new ArrayList<>();

    @Override
    protected Iterable<? extends ProcessingStep> initSteps() {
        HashSet<ProcessingStep> steps = new HashSet<>();
        steps.add(new VaadinatorProccessingStep());
        return steps;
    }

    @Override
    protected void postRound(RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            System.out.println("Generating code for these DisplayBeans:");
            for (BeanDescription bd : beanDescriptions) {
                System.out.println("\t" + bd.getClassName());
            }
        }
    }

    public List<BeanDescription> getBeanDescriptions() {
        return beanDescriptions;
    }

    public class VaadinatorProccessingStep implements ProcessingStep {

        @Override
        public Set annotations() {
            Set anno = new HashSet<>();
            Class<? extends Annotation> a = DisplayBean.class;
            anno.add(a);
            return anno;
        }

        @Override
        public Set<Element> process(
                SetMultimap<Class<? extends Annotation>, Element> elementsByAnnotation) {
            Set<Element> elements = elementsByAnnotation.get(DisplayBean.class);
            for (Element element : elements) {
                process((TypeElement) element);
            }
            return new HashSet<>();
        }

        private void process(TypeElement displayBeanElement) {
            DisplayBeanPrism displayBeanPrisim = DisplayBeanPrism
                    .getInstanceOn(displayBeanElement);
            BeanDescription beanDescription = new BeanDescription(
                    displayBeanElement.getSimpleName().toString());
            beanDescription
                    .setBeanValidation(displayBeanPrisim.beanValidation());
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

        /**
         * Returns the package name of the given element. NB: This method
         * requires the given element has the kind of {@link ElementKind#CLASS}.
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
}
