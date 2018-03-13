package com.ep.generate;

import com.google.common.collect.Lists;
import org.jooq.util.GenerationTool;
import org.jooq.util.jaxb.*;

import java.util.List;


public class GeneratorClass {
    public static String EXCLUDES = "QRTZ_FIRED_TRIGGERS|QRTZ_PAUSED_TRIGGER_GRPS|QRTZ_SCHEDULER_STATE|QRTZ_LOCKS" +
            "|QRTZ_SIMPLE_TRIGGERS|QRTZ_SIMPROP_TRIGGERS|QRTZ_CRON_TRIGGERS|QRTZ_CRON_TRIGGERS|QRTZ_TRIGGERS" +
            "|QRTZ_JOB_DETAILS|QRTZ_CALENDARS|QRTZ_BLOB_TRIGGERS";

    public static void main(String[] args) throws Exception {
        GeneratorClass generatorClass = new GeneratorClass();
        generatorClass.codeGgenerator();
    }

    public void codeGgenerator() throws Exception {
        List list = Lists.newArrayList();
        Schema schema = new Schema();
        schema.setInputSchema("ep");
        list.add(schema);
        schema = new Schema();
        schema.setOutputSchemaToDefault(true);
        list.add(schema);

        Configuration configuration = new Configuration()
                .withJdbc(new Jdbc()
                        .withDriver("com.mysql.jdbc.Driver")
                        .withUrl("jdbc:mysql://gz-cdb-h1zql3x3.sql.tencentcdb.com:63058/ep?useUnicode=true&amp;" +
                                "characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull&amp;autoReconnect=true")
                        .withUser("root")
                        .withPassword(""))
                .withGenerator(new Generator()
                        .withDatabase(new Database()
                                // .withRecordTimestampFields("create_at")
                                .withRecordVersionFields("version").withDateAsTimestamp(true)
                                //.withForcedTypes(new ForcedType().withExpression("is_*").withName("BOOLEAN"))

                                .withName("org.jooq.util.mysql.MySQLDatabase")
                                .withIncludes(".*")
                                .withExcludes(EXCLUDES)
                                .withSchemata(list))
                        .withStrategy(new Strategy().withName("com.ep.generate.CustomGenertor"))
                        .withGenerate(new Generate().withFluentSetters(true)
                                .withPojos(true).withDaos(true))
                        .withTarget(new Target()
                                .withPackageName("com.ep.domain.repository.domain")
                                .withDirectory("ep-domain/src/main/java")));

        GenerationTool.generate(configuration);

    }
}