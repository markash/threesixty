package za.co.yellowfire.threesixty.ui.component;

import org.vaadin.viritin.MBeanFieldGroup;

public class BeanBinder {

    public static <T> MBeanFieldGroup<T> bind(T bean, Object objectWithMemberFields, final boolean buffered) {
        @SuppressWarnings("unchecked")
		MBeanFieldGroup<T> beanFieldGroup = new MBeanFieldGroup<T>((Class<T>) bean.getClass());
        beanFieldGroup.setItemDataSource(bean);
        beanFieldGroup.setBuffered(buffered);
        beanFieldGroup.bindMemberFields(objectWithMemberFields);
        beanFieldGroup.configureMaddonDefaults();
        return beanFieldGroup;
    }

}
