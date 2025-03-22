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
import net.sf.dynamicreports.report.builder.FieldBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.ListType;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.field;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

/**
 * <p>FlowColumnPairsReport class.</p>
 *
 * @author Ricardo Mariaca
 * 
 */
public class FlowColumnPairsReport {

    /**
     * <p>Constructor for FlowColumnPairsReport.</p>
     */
    public FlowColumnPairsReport() {
        build();
    }

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    public static void main(String[] args) {
        new FlowColumnPairsReport();
    }

    private void build() {
        StyleBuilder textStyle = stl.style(Templates.columnStyle).setBorder(stl.pen1Point());

        FieldBuilder<Integer> idField = field("id", type.integerType());
        FieldBuilder<String> itemField = field("item", type.stringType());
        FieldBuilder<Integer> quantityField = field("quantity", type.integerType());
        FieldBuilder<BigDecimal> unitPriceField = field("unitprice", type.bigDecimalType());
        FieldBuilder<Date> orderDateField = field("orderdate", type.dateType());
        FieldBuilder<Date> orderYearField = field("orderdate", type.dateYearType());
        FieldBuilder<Date> orderMonthField = field("orderdate", type.dateMonthType());
        FieldBuilder<Date> orderDayField = field("orderdate", type.dateDayType());

        try {
            report().setTemplate(Templates.reportTemplate)
                    .setColumnStyle(textStyle)
                    .columnGrid(ListType.HORIZONTAL_FLOW)
                    .fields(idField, itemField, quantityField, unitPriceField, orderDateField, orderYearField, orderMonthField, orderDayField)
                    .columns(col.componentColumn(columnPair("Id", idField)), col.componentColumn(columnPair("Item", itemField).setFixedWidth(200)),
                             col.componentColumn(columnPair("Quantity", quantityField)), col.componentColumn(columnPair("Unit price", unitPriceField)),
                             col.componentColumn(columnPair("Order date", orderDateField)), col.componentColumn(columnPair("Order year", orderYearField)),
                             col.componentColumn(columnPair("Order month", orderMonthField)), col.componentColumn(columnPair("Order day", orderDayField)))
                    .title(Templates.createTitleComponent("FlowColumnPairs"))
                    .detailFooter(cmp.verticalGap(20))
                    .pageFooter(Templates.footerComponent)
                    .setDataSource(createDataSource())
                    .show();
        } catch (DRException e) {
            e.printStackTrace();
        }
    }

    private VerticalListBuilder columnPair(String title, FieldBuilder<?> value) {
        TextFieldBuilder<String> titleCmp = cmp.text(title).setStyle(Templates.columnTitleStyle);
        TextFieldBuilder<?> valueCmp = cmp.text(value);
        return cmp.verticalList(titleCmp, valueCmp);
    }

    private JRDataSource createDataSource() {
        DRDataSource dataSource = new DRDataSource("id", "item", "orderdate", "quantity", "unitprice");
        dataSource.add(5, "Notebook", Date.from(Instant.now()), 1, BigDecimal.valueOf(500));
        dataSource.add(8, "Book", Date.from(Instant.now()), 7, BigDecimal.valueOf(300));
        dataSource.add(15, "PDA", Date.from(Instant.now()), 2, BigDecimal.valueOf(250));
        return dataSource;
    }
}
