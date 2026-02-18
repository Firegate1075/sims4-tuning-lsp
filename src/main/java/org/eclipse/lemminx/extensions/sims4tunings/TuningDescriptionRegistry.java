package org.eclipse.lemminx.extensions.sims4tunings;

import org.eclipse.lemminx.extensions.sims4tunings.TuningDescriptionDataModel.*;

import java.util.*;
import java.util.logging.Logger;

public class TuningDescriptionRegistry {
    private static final Logger LOGGER = Logger.getLogger(TuningDescriptionRegistry.class.getName());

    // map that stores tuning descriptions of <Module> elements by className
    private final Map<String, ModuleElement> moduleElementsTuningDescriptionMap = HashMap.newHashMap(636);

    // map that stores tuning descriptions of <Instance> elements by className
    private final Map<String, InstanceElement> instanceElementTuningDescriptionMap = HashMap.newHashMap(1120);

    // map that stores tuning descriptions of <TdescFrag> elements by className
    private final Map<String, TdescFrag> tdescFragTuningDescriptionMap = HashMap.newHashMap(8);


    public void addTuningDescription(TuningRoot root) {
        String className = "";

        ITuningDescriptionElement element = root.getTunableElements().getFirst();

        if (element instanceof InstanceElement instanceElement) {
            addInstanceElement(instanceElement);
        } else if (element instanceof ModuleElement moduleElement) {
            addModuleElement(moduleElement);
        } else if (element instanceof TdescFrag tdescFrag) {
            addTdescFrag(tdescFrag);
        }
    }

    private void addModuleElement(ModuleElement element) {
        if (moduleElementsTuningDescriptionMap.containsKey(element.getName())) {
            LOGGER.warning("Class tuning description for class " + element.getName() + " already contained in registry. Skipping");
            return;
        }
        moduleElementsTuningDescriptionMap.put(element.getName(), element);
    }

    private void addInstanceElement(InstanceElement element) {
        if (instanceElementTuningDescriptionMap.containsKey(element.getClassName())) {
            LOGGER.warning("Instance tuning description for class " + element.getClassName() + " already contained in registry. Skipping");
            return;
        }
        instanceElementTuningDescriptionMap.put(element.getClassName(), element);
    }

    private void addTdescFrag(TdescFrag element) {
        ITuningDescriptionElement child = element.getTunableElements().getFirst();
        String className = "";

        switch (child) {
            case TunableVariant tunableVariant -> className = tunableVariant.getClassName();
            case TunableList tunableList -> className = tunableList.getClassName();
            case TunableTuple tunableTuple -> className = tunableTuple.getClassName();
            default -> {
                    LOGGER.warning("Unexpected child element of type" + child.getClass() + " for TdescFrag " + element);
                    return;
            }
        }

        if (tdescFragTuningDescriptionMap.containsKey(className)) {
            LOGGER.warning("TdescFrag tuning description for class " + className + " already contained in registry. Skipping");
            return;
        }
        tdescFragTuningDescriptionMap.put(className, element);
    }

    public Optional<InstanceElement> getInstanceElementByClassName(String className) {
        return Optional.ofNullable(instanceElementTuningDescriptionMap.get(className));
    }

    // path is a string [moduleName].[className]..., where moduleName is the name of the module with dots replaced by dashes
    public Optional<ClassElement> getClassElementByPath(String path) {
        String[] pathElements = path.split("\\.");
        String moduleName = pathElements[0].replace("-", ".");
        ModuleElement moduleElement = moduleElementsTuningDescriptionMap.get(moduleName);
        if (moduleElement == null) {
            return Optional.empty();
        }

        Deque<String> pathDeque = new ArrayDeque<>(Arrays.asList(pathElements).subList(1, pathElements.length));

        List<ClassElement> children = moduleElement.getTunableElements().stream()
                .filter(ClassElement.class::isInstance)
                .map(ClassElement.class::cast).toList();

        return findClassElementRecursively(children, pathDeque);
    }

    private Optional<ClassElement> findClassElementRecursively(List<ClassElement> classElements, Deque<String> pathDeque) {
        for (ClassElement classElement : classElements) {
            // check <Class> name
            if (classElement.getName().equals(pathDeque.peekFirst())) {
                // found our next element
                pathDeque.removeFirst();

                if (pathDeque.isEmpty()) {
                    // done, found our target ClassElement
                    return Optional.of(classElement);
                }

                // continue searching children
                List<ClassElement> children = classElement.getTunableElements().stream()
                        .filter(ClassElement.class::isInstance)
                        .map(ClassElement.class::cast).toList();

                return findClassElementRecursively(children, pathDeque);
            }
        }

        // target not found
        return Optional.empty();
    }

    public Optional<TdescFrag> getTdescFragByClassName(String className) {
        return Optional.ofNullable(tdescFragTuningDescriptionMap.get(className));
    }
}
