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
package ch.unibas.medizin.dynamicreports.examples.group;

import ch.unibas.medizin.dynamicreports.examples.Templates;
import ch.unibas.medizin.dynamicreports.report.base.expression.AbstractSimpleExpression;
import ch.unibas.medizin.dynamicreports.report.builder.column.TextColumnBuilder;
import ch.unibas.medizin.dynamicreports.report.builder.group.CustomGroupBuilder;
import ch.unibas.medizin.dynamicreports.report.constant.Calculation;
import ch.unibas.medizin.dynamicreports.report.constant.GroupHeaderLayout;
import ch.unibas.medizin.dynamicreports.report.datasource.DRDataSource;
import ch.unibas.medizin.dynamicreports.report.definition.ReportParameters;
import ch.unibas.medizin.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.col;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.exp;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.field;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.grp;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.report;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.sbt;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.stl;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.type;

/**
 * <p>GroupLayoutReport class.</p>
 *
 * @author Ricardo Mariaca
 * 
 */
public class GroupLayoutReport {

    /**
     * <p>Constructor for GroupLayoutReport.</p>
     */
    public GroupLayoutReport() {
        build();
    }

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    public static void main(String[] args) {
        new GroupLayoutReport();
    }

    private void build() {
        TextColumnBuilder<String> yearColumn = col.column("Order year", exp.text(""));
        TextColumnBuilder<Date> orderDateColumn = col.column("Order date", "orderdate", type.dateType());
        TextColumnBuilder<String> itemColumn = col.column("Item", "item", type.stringType());
        TextColumnBuilder<Integer> quantityColumn = col.column("Quantity", "quantity", type.integerType());
        TextColumnBuilder<BigDecimal> unitPriceColumn = col.column("Unit price", "unitprice", type.bigDecimalType());

        CustomGroupBuilder yearGroup = grp.group(new YearExpression()).groupByDataType().setHeaderLayout(GroupHeaderLayout.EMPTY).setPadding(0);

        try {
            report().setTemplate(Templates.reportTemplate)
                    .setSubtotalStyle(stl.style(Templates.boldStyle))
                    .fields(field("orderdate", type.dateYearType()))
                    .columns(yearColumn, orderDateColumn, itemColumn, quantityColumn, unitPriceColumn)
                    .groupBy(yearGroup)
                    .subtotalsAtGroupHeader(yearGroup, sbt.first(new YearExpression(), yearColumn), sbt.sum(quantityColumn), sbt.sum(unitPriceColumn))
                    .subtotalsAtSummary(sbt.aggregate(exp.text("Total"), yearColumn, Calculation.NOTHING), sbt.sum(quantityColumn), sbt.sum(unitPriceColumn))
                    .title(Templates.createTitleComponent("GroupLayout"))
                    .pageFooter(Templates.footerComponent)
                    .setDataSource(createDataSource())
                    .show();
        } catch (DRException e) {
            e.printStackTrace();
        }
    }

    private JRDataSource createDataSource() {
        DRDataSource dataSource = new DRDataSource("orderdate", "item", "quantity", "unitprice");
        dataSource.add(toDate(2009, 11, 1), "Tablet", 5, BigDecimal.valueOf(250));
        dataSource.add(toDate(2009, 11, 1), "Laptop", 3, BigDecimal.valueOf(480));
        dataSource.add(toDate(2009, 12, 1), "Smartphone", 1, BigDecimal.valueOf(280));
        dataSource.add(toDate(2009, 12, 1), "Tablet", 1, BigDecimal.valueOf(190));
        dataSource.add(toDate(2010, 1, 1), "Tablet", 4, BigDecimal.valueOf(230));
        dataSource.add(toDate(2010, 1, 1), "Laptop", 2, BigDecimal.valueOf(650));
        dataSource.add(toDate(2010, 2, 1), "Laptop", 3, BigDecimal.valueOf(550));
        dataSource.add(toDate(2010, 2, 1), "Smartphone", 5, BigDecimal.valueOf(210));
        return dataSource;
    }

    private Date toDate(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, day);
        return c.getTime();
    }

    private static class YearExpression extends AbstractSimpleExpression<String> {
        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public String evaluate(ReportParameters reportParameters) {
            return type.dateYearType().valueToString("orderdate", reportParameters);
        }
    }
}
