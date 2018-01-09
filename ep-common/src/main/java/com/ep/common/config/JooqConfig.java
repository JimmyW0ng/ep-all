package com.ep.common.config;

import org.jooq.*;
import org.jooq.conf.Settings;
import org.jooq.conf.SettingsTools;
import org.jooq.conf.StatementType;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.jooq.tools.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * jooq配置
 */
public class JooqConfig {

    private Long SLOW_QUERY_LIMIT = 1_000_000_000L;

    @Bean
    @DependsOn("dataSource")
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public Configuration jooqConfiguration(@Autowired ConnectionProvider connectionProvider, @Autowired
            TransactionProvider springTransactionProvider,
                                                    @Autowired ExecuteListenerProvider executeListenerProvider) {
        Settings settings = SettingsTools.defaultSettings();
        settings.setStatementType(StatementType.PREPARED_STATEMENT);
        settings.withRenderSchema(false);
        Configuration derive = new DefaultConfiguration()
                .derive(connectionProvider)
                .derive(settings)
                .derive(executeListenerProvider)
                .derive(SQLDialect.MYSQL)
                .set(springTransactionProvider)
                .set(DefaultExecuteListenerProvider.providers(new SlowQueryListener()));


        return derive;
    }

    @Bean
    public DefaultDSLContext dslContext(Configuration configuration) {
        return new DefaultDSLContext(configuration);
    }

    public class SlowQueryListener extends DefaultExecuteListener {
        private Logger logger = LoggerFactory.getLogger(SlowQueryListener.class);
        private StopWatch watch;

        @Override
        public void executeStart(ExecuteContext ctx) {
            super.executeStart(ctx);
            watch = new StopWatch();
        }

        @Override
        public void executeEnd(ExecuteContext ctx) {
            try {
                super.executeEnd(ctx);
                //记录执行时间超过1s的操作
                if (watch.split() > SLOW_QUERY_LIMIT) {
                    ExecuteType type = ctx.type();
                    StringBuffer sqlBuffer = new StringBuffer();
                    if (type == ExecuteType.BATCH) {
                        for (Query query : ctx.batchQueries()) {
                            sqlBuffer.append(query.toString()).append("\n");
                        }
                    } else {
                        sqlBuffer.append(ctx.query() == null ? "blank query " : ctx.query().toString());
                    }
                    watch.splitInfo(String.format("Slow SQL query meta executed : [ %s ]", sqlBuffer.toString()));
                }
            } catch (Exception e) {
                logger.error(" SlowQueryListener has occur,fix bug  ", e);
            }
        }
    }

}
