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
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.expression.JasperExpression;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

import java.math.BigDecimal;

import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.exp;
import static net.sf.dynamicreports.report.builder.DynamicReports.field;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

/**
 * <p>JasperExpressionReport class.</p>
 *
 * @author Ricardo Mariaca
 * 
 */
public class JasperExpressionReport {

    /**
     * <p>Constructor for JasperExpressionReport.</p>
     */
    public JasperExpressionReport() {
        build();
    }

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    public static void main(String[] args) {
        new JasperExpressionReport();
    }

    private void build() {
        try {
            TextColumnBuilder<String> itemColumn = col.column("item", type.stringType()).setTitle(exp.jasperSyntaxText("Item"));
            JasperExpression<BigDecimal> priceExpression = exp.jasperSyntax("BigDecimal.valueOf($F{quantity}).multiply($F{unitprice})", BigDecimal.class);
            TextColumnBuilder<BigDecimal> priceColumn = col.column(priceExpression).setTitle(exp.jasperSyntaxText("Price"));

            report().setTemplate(Templates.reportTemplate)
                    .fields(field("quantity", Integer.class), field("unitprice", BigDecimal.class))
                    .columns(itemColumn, priceColumn)
                    .title(Templates.createTitleComponent("JasperExpression"))
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
}
