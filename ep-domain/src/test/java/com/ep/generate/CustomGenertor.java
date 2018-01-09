package com.ep.generate;

import org.jooq.tools.StringUtils;
import org.jooq.util.DefaultGeneratorStrategy;
import org.jooq.util.Definition;


public class CustomGenertor extends DefaultGeneratorStrategy {

    public String getJavaClassName(Definition definition, Mode mode) {
        return this.getJavaClassName0(definition, mode);
    }

    private String getJavaClassName0(Definition definition, Mode mode) {
        StringBuilder result = new StringBuilder();
        result.append(StringUtils.toCamelCase(definition.getOutputName()
                .replace(' ', '_')
                .replace('-', '_')
                .replace('.', '_')));
        if (mode == Mode.RECORD) {
            result.append("Record");
        } else if (mode == Mode.DAO) {
            result.append("Repository");
        } else if (mode == Mode.INTERFACE) {
            result.insert(0, "I");
        } else if (mode == Mode.POJO) {
            result.append("Po");
        }

        return result.toString();
    }

//    @Override
//    public String getJavaPackageName(Definition definition, Mode mode) {
//        if (mode == Mode.POJO) {
//            return generJavaPackageName(definition,mode);
//        } else {
//            return super.getJavaPackageName(definition, mode);
//        }
//
//    }
//    private String generJavaPackageName(Definition definition, Mode mode){
//        StringBuilder sb = new StringBuilder();
//
//        sb.append("com.changfu.pojos");

//        // [#2032] In multi-catalog setups, the catalog name goes into the package
//        if (definition.getDatabase().getCatalogs().size() > 1) {
//            sb.append(".");
//            sb.append(getJavaIdentifier(definition.getCatalog()).toLowerCase());
//        }
//
//        if (!(definition instanceof CatalogDefinition)) {
//
//            // [#282] In multi-schema setups, the schema name goes into the package
//            if (definition.getDatabase().getSchemata().size() > 1) {
//                sb.append(".");
//                sb.append(getJavaIdentifier(definition.getSchema()).toLowerCase());
//            }
//
//            if (!(definition instanceof SchemaDefinition)) {
//
//                // Some definitions have their dedicated subpackages, e.g. "tables", "routines"
//                if (!StringUtils.isBlank(getSubPackage(definition))) {
//                    sb.append(".");
//                    sb.append(getSubPackage(definition));
//                }
//
//                // Record are yet in another subpackage
//                if (mode == Mode.RECORD) {
//                    sb.append(".records");
//                }
//
//                // POJOs too
//                else if (mode == Mode.POJO) {
//                    sb.append(".pojos");
//                }
//
//                // DAOs too
//                else if (mode == Mode.DAO) {
//                    sb.append(".daos");
//                }
//
//                // Interfaces too
//                else if (mode == Mode.INTERFACE) {
//                    sb.append(".interfaces");
//                }
//
//
//
//
//
//
//            }
//        }

//        return sb.toString();
//    }


}
