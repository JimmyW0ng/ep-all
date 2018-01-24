package com.ep.backend.dialect;

import com.ep.common.tool.DateTools;
import com.ep.common.tool.NumberTools;
import com.ep.common.tool.StringTools;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by jimmy on 2016/12/15.
 */
public class MyExpressionFactory implements IExpressionObjectFactory {

    private static final String STRINGUTILS = "stringTools";
    private static final String DATETOOLS = "dateTools";
    private static final String NUMBERTOOLS = "numberTools";

    private static final Set<String> ALL_EXPRESSION_OBJECT_NAMES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList
            (STRINGUTILS, DATETOOLS, NUMBERTOOLS)));

    @Override
    public Set<String> getAllExpressionObjectNames() {
        return ALL_EXPRESSION_OBJECT_NAMES;
    }

    @Override
    public Object buildObject(IExpressionContext iExpressionContext, String s) {
        if (STRINGUTILS.equals(s)) {
            return new StringTools();
        } else if (DATETOOLS.equals(s)) {
            return new DateTools();
        } else if (NUMBERTOOLS.equals(s)) {
            return new NumberTools();
        }

        return null;
    }

    @Override
    public boolean isCacheable(String expressionObjectName) {
        return expressionObjectName != null && ALL_EXPRESSION_OBJECT_NAMES.contains(expressionObjectName);
    }
}
