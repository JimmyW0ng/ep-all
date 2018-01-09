package com.ep.common.tool;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 对象copy
 * Created by jw on 16/8/14.
 */
public class BeanTools {
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = Sets.newHashSet();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * copy 对象属性, 忽列属性为NULL,
     *
     * @param src
     * @param target
     */
    public static void copyPropertiesIgnoreNull(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));

    }

    /**
     * copy 数组
     *
     * @param src
     * @param classz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List copyListPropertiesIgnoreNull(List<?> src, Class<?> classz) {
        List list = Lists.newArrayList();
        if (CollectionsTools.isEmpty(src)) {
            return list;
        }
        src.forEach(object -> {
            Object instantiate = BeanUtils.instantiate(classz);
            copyPropertiesIgnoreNull(object, instantiate);
            list.add(instantiate);
        });
        return list;
    }

    /**
     * Bean转Map<String, String>, 忽略null值
     *
     * @param source
     * @param target
     */
    public static void copyBeanTransToMap(Object source, Map<String, String> target) throws IllegalAccessException {
        if (source == null || target == null) {
            return;
        }
        Class souceClass = source.getClass();
        do {
            for (Field field : souceClass.getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(source);
                if (value == null) {
                    continue;
                }
                target.put(field.getName(), String.valueOf(value));
            }
            souceClass = souceClass.getSuperclass();
        } while (!souceClass.equals(Object.class));
    }
}