/*
 * DynamicReports - Free Java reporting library for creating reports dynamically
 *
 * Copyright (C) 2010 - 2018 Ricardo Mariaca and the Dynamic Reports Contributors
 *
 * This file is part of DynamicReports.
 *
 * DynamicReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamicReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamicReports. If not, see <http://www.gnu.org/licenses/>.
 */
package ch.unibas.medizin.dynamicreports.examples.miscellaneous;

import ch.unibas.medizin.dynamicreports.examples.Templates;
import ch.unibas.medizin.dynamicreports.report.base.AbstractScriptlet;
import ch.unibas.medizin.dynamicreports.report.base.expression.AbstractSimpleExpression;
import ch.unibas.medizin.dynamicreports.report.builder.column.TextColumnBuilder;
import ch.unibas.medizin.dynamicreports.report.builder.group.CustomGroupBuilder;
import ch.unibas.medizin.dynamicreports.report.builder.subtotal.CustomSubtotalBuilder;
import ch.unibas.medizin.dynamicreports.report.datasource.DRDataSource;
import ch.unibas.medizin.dynamicreports.report.definition.ReportParameters;
import ch.unibas.medizin.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.col;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.grp;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.report;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.sbt;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.type;

/**
 * <p>ScriptletReport class.</p>
 *
 * @author Ricardo Mariaca
 * 
 */
public class ScriptletReport {
    private Map<String, Integer> itemsCount;

    /**
     * <p>Constructor for ScriptletReport.</p>
     */
    public ScriptletReport() {
        build();
    }

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    public static void main(String[] args) {
        new ScriptletReport();
    }

    private void build() {
        itemsCount = new ConcurrentHashMap<>();

        TextColumnBuilder<String> itemColumn = col.column("Item", "item", type.stringType());
        CustomSubtotalBuilder<String> itemSbt = sbt.customValue(new ItemSubtotal(), itemColumn);
        CustomGroupBuilder group = grp.group("country", String.class);

        try {
            report().setTemplate(Templates.reportTemplate)
                    .scriptlets(new ReportScriptlet())
                    .columns(itemColumn)
                    .groupBy(group)
                    .subtotalsAtGroupFooter(group, itemSbt)
                    .title(Templates.createTitleComponent("Scriptlet"))
                    .pageFooter(Templates.footerComponent)
                    .setDataSource(createDataSource())
                    .show();
        } catch (DRException e) {
            e.printStackTrace();
        }
    }

    private JRDataSource createDataSource() {
        DRDataSource dataSource = new DRDataSource("country", "item");
        dataSource.add("USA", "Book");
        dataSource.add("USA", "DVD");
        dataSource.add("USA", "Book");
        dataSource.add("USA", "Book");
        dataSource.add("USA", "DVD");
        dataSource.add("USA", "Book");
        dataSource.add("USA", "DVD");

        dataSource.add("Canada", "Book");
        dataSource.add("Canada", "Book");
        dataSource.add("Canada", "DVD");
        dataSource.add("Canada", "Book");
        dataSource.add("Canada", "DVD");
        dataSource.add("Canada", "Phone");
        return dataSource;
    }

    private class ItemSubtotal extends AbstractSimpleExpression<String> {
        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public String evaluate(ReportParameters reportParameters) {
            String result = itemsCount.keySet().stream().map(item -> item + " = " + itemsCount.get(item) + "\n").collect(Collectors.joining());
            return StringUtils.removeEnd(result, "\n");
        }
    }

    private class ReportScriptlet extends AbstractScriptlet {

        @Override
        public void afterDetailEval(ReportParameters reportParameters) {
            super.afterDetailEval(reportParameters);
            String item = reportParameters.getValue("item");
            Integer count;
            count = itemsCount.getOrDefault(item, 0);
            itemsCount.put(item, ++count);
        }

        @Override
        public void afterGroupInit(String groupName, ReportParameters reportParameters) {
            super.afterGroupInit(groupName, reportParameters);
            itemsCount.clear();
        }
    }
}
