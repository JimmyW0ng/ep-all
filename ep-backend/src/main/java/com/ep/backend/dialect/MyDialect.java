package com.ep.backend.dialect;

import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;


public class MyDialect extends AbstractDialect implements IExpressionObjectDialect {
    private final MyExpressionFactory myExpressionFactory = new MyExpressionFactory();

    public MyDialect() {
        super("myUtils");
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return myExpressionFactory;
    }
}
