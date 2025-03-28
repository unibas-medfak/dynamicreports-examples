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
package ch.unibas.medizin.dynamicreports.examples.column;

import ch.unibas.medizin.dynamicreports.examples.Templates;
import ch.unibas.medizin.dynamicreports.report.base.expression.AbstractSimpleExpression;
import ch.unibas.medizin.dynamicreports.report.builder.column.TextColumnBuilder;
import ch.unibas.medizin.dynamicreports.report.datasource.DRDataSource;
import ch.unibas.medizin.dynamicreports.report.definition.ReportParameters;
import ch.unibas.medizin.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

import java.io.Serial;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.col;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.field;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.report;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.sbt;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.type;

/**
 * <p>ConversionColumnReport class.</p>
 *
 * @author Ricardo Mariaca
 * 
 */
public class ConversionColumnReport {

    /**
     * <p>Constructor for ConversionColumnReport.</p>
     */
    public ConversionColumnReport() {
        build();
    }

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    public static void main(String[] args) {
        new ConversionColumnReport();
    }

    private void build() {
        try {
            TextColumnBuilder<Date> orderDateColumn1 = col.column("Order date", new OrderDateColumn1()).setDataType(type.dateType());
            TextColumnBuilder<Date> orderDateColumn2 = col.column("Order date", new OrderDateColumn2()).setDataType(type.dateType());
            TextColumnBuilder<BigDecimal> quantityColumn1 = col.column("Quantity", new QuantityColumn1()).setDataType(type.bigDecimalType());
            TextColumnBuilder<BigDecimal> quantityColumn2 = col.column("Quantity", new QuantityColumn2()).setDataType(type.bigDecimalType());

            report().setTemplate(Templates.reportTemplate)
                    .fields(field("orderdate", String.class), field("quantity", String.class))
                    .columns(col.column("Item", "item", type.stringType()), orderDateColumn1, orderDateColumn2, quantityColumn1, quantityColumn2)
                    .title(Templates.createTitleComponent("ConversionColumn"))
                    .pageFooter(Templates.footerComponent)
                    .subtotalsAtSummary(sbt.min(orderDateColumn1), sbt.min(orderDateColumn2), sbt.sum(quantityColumn1), sbt.sum(quantityColumn2))
                    .setDataSource(createDataSource())
                    .show();
        } catch (DRException e) {
            e.printStackTrace();
        }
    }

    private JRDataSource createDataSource() {
        DRDataSource dataSource = new DRDataSource("item", "orderdate", "quantity");
        dataSource.add("Notebook", "1/1/2010", "100.9");
        dataSource.add("Notebook", "2/2/2010", "100.2");
        return dataSource;
    }

    private static class OrderDateColumn1 extends AbstractSimpleExpression<Date> {
        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public Date evaluate(ReportParameters reportParameters) {
            String value = reportParameters.getValue("orderdate");
            try {
                return new SimpleDateFormat("MM/dd/yyyy", reportParameters.getLocale()).parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class OrderDateColumn2 extends AbstractSimpleExpression<Date> {
        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public Date evaluate(ReportParameters reportParameters) {
            try {
                return type.dateType().stringToValue("orderdate", reportParameters);
            } catch (DRException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class QuantityColumn1 extends AbstractSimpleExpression<BigDecimal> {
        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public BigDecimal evaluate(ReportParameters reportParameters) {
            String value = reportParameters.getValue("quantity");
            try {
                Number number = new DecimalFormat("#,###.##", new DecimalFormatSymbols(reportParameters.getLocale())).parse(value);
                return BigDecimal.valueOf(number.doubleValue());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class QuantityColumn2 extends AbstractSimpleExpression<BigDecimal> {
        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public BigDecimal evaluate(ReportParameters reportParameters) {
            try {
                return type.bigDecimalType().stringToValue("quantity", reportParameters);
            } catch (DRException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
