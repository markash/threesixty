package za.co.yellowfire.threesixty.ui.component;

import org.vaadin.viritin.v7.MBeanFieldGroup;

/**
 * This BeanBinder is taken from the Viritin version but allows for the buffering to be on / off
 * 
 * Viritin 1.49 allows for nested properties, therefore the change in the method signature
 * This class will hopefully be obsolete in V7.2 once my patch gets through our
 * insanely slow review process. https://dev.vaadin.com/review/#/c/2351/
 */
public class BeanBinder {

    public static <T> MBeanFieldGroup<T> bind(T bean, Object objectWithMemberFields, final boolean buffered, String... nestedProperties) {
        @SuppressWarnings("unchecked")
		MBeanFieldGroup<T> beanFieldGroup = new MBeanFieldGroup<T>((Class<T>) bean.getClass());
        beanFieldGroup.setItemDataSource(bean);
        if(nestedProperties != null) {
            for (String nestedPropertyId : nestedProperties) {
                beanFieldGroup.getItemDataSource().addNestedProperty(nestedPropertyId);
            }
        }
        beanFieldGroup.setBuffered(buffered);
        beanFieldGroup.bindMemberFields(objectWithMemberFields);
        beanFieldGroup.configureMaddonDefaults();
        return beanFieldGroup;
    }

}
