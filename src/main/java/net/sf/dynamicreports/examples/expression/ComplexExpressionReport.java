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
package net.sf.dynamicreports.examples.expression;

import net.sf.dynamicreports.examples.Templates;
import net.sf.dynamicreports.report.builder.expression.AbstractComplexExpression;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

import java.math.BigDecimal;
import java.util.List;

import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.field;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

/**
 * <p>ComplexExpressionReport class.</p>
 *
 * @author Ricardo Mariaca
 * 
 */
public class ComplexExpressionReport {

    /**
     * <p>Constructor for ComplexExpressionReport.</p>
     */
    public ComplexExpressionReport() {
        build();
    }

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    public static void main(String[] args) {
        new ComplexExpressionReport();
    }

    private void build() {
        try {
            report().setTemplate(Templates.reportTemplate)
                    .columns(col.column("Item", "item", type.stringType()), col.column("Price", new ComplexExpression()))
                    .title(Templates.createTitleComponent("ComplexExpression"))
                    .pageFooter(Templates.footerComponent)
                    .setDataSource(createDataSource())
                    .show();
        } catch (DRException e) {
            e.printStackTrace();
        }
    }

    private JRDataSource createDataSource() {
        DRDataSource dataSource = new DRDataSource("item", "quantity", "unitprice");
        dataSource.add("Book", 20, BigDecimal.valueOf(10));
        return dataSource;
    }

    private class ComplexExpression extends AbstractComplexExpression<BigDecimal> {
        private static final long serialVersionUID = 1L;

        public ComplexExpression() {
            addExpression(field("quantity", Integer.class));
            addExpression(field("unitprice", BigDecimal.class));
        }

        @Override
        public BigDecimal evaluate(List<?> values, ReportParameters reportParameters) {
            Integer quantity = (Integer) values.get(0);
            BigDecimal unitPrice = (BigDecimal) values.get(1);
            return BigDecimal.valueOf(quantity).multiply(unitPrice);
        }
    }
}
