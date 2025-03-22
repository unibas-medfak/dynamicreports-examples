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
package net.sf.dynamicreports.examples.columngrid;

import net.sf.dynamicreports.examples.Templates;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.grid;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

/**
 * <p>ColumnGridReport class.</p>
 *
 * @author Ricardo Mariaca
 * 
 */
public class ColumnGridReport {

    /**
     * <p>Constructor for ColumnGridReport.</p>
     */
    public ColumnGridReport() {
        build();
    }

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    public static void main(String[] args) {
        new ColumnGridReport();
    }

    private void build() {
        StyleBuilder textStyle = stl.style(Templates.columnStyle).setBorder(stl.pen1Point());

        TextColumnBuilder<String> itemColumn = col.column("Item", "item", type.stringType());
        TextColumnBuilder<Integer> quantityColumn = col.column("Quantity", "quantity", type.integerType());
        TextColumnBuilder<BigDecimal> unitPriceColumn = col.column("Unit price", "unitprice", type.bigDecimalType());
        TextColumnBuilder<Date> orderDateColumn = col.column("Order date", "orderdate", type.dateType());
        TextColumnBuilder<Date> orderDateFColumn = col.column("Order date", "orderdate", type.dateYearToFractionType());
        TextColumnBuilder<Date> orderYearColumn = col.column("Order year", "orderdate", type.dateYearType());
        TextColumnBuilder<Date> orderMonthColumn = col.column("Order month", "orderdate", type.dateMonthType());
        TextColumnBuilder<Date> orderDayColumn = col.column("Order day", "orderdate", type.dateDayType());

        try {
            report().setTemplate(Templates.reportTemplate)
                    .setColumnStyle(textStyle)
                    .columns(itemColumn, quantityColumn, unitPriceColumn, orderDateColumn, orderDateFColumn, orderYearColumn, orderMonthColumn, orderDayColumn)
                    .columnGrid(grid.verticalColumnGridList(itemColumn, grid.horizontalColumnGridList(quantityColumn, unitPriceColumn)),
                                grid.verticalColumnGridList(orderDateColumn, grid.horizontalColumnGridList(orderDateFColumn, orderYearColumn),
                                                            grid.horizontalColumnGridList(orderMonthColumn, orderDayColumn)))
                    .title(Templates.createTitleComponent("ColumnGrid"))
                    .pageFooter(Templates.footerComponent)
                    .setDataSource(createDataSource())
                    .show();
        } catch (DRException e) {
            e.printStackTrace();
        }
    }

    private JRDataSource createDataSource() {
        DRDataSource dataSource = new DRDataSource("item", "orderdate", "quantity", "unitprice");
        dataSource.add("Notebook", Date.from(Instant.now()), 1, BigDecimal.valueOf(500));
        dataSource.add("Book", Date.from(Instant.now()), 7, BigDecimal.valueOf(300));
        dataSource.add("PDA", Date.from(Instant.now()), 2, BigDecimal.valueOf(250));
        return dataSource;
    }
}
