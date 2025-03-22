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
package ch.unibas.medizin.dynamicreports.examples.complex.sales;

import ch.unibas.medizin.dynamicreports.examples.Templates;
import ch.unibas.medizin.dynamicreports.jasper.builder.JasperReportBuilder;
import ch.unibas.medizin.dynamicreports.report.builder.chart.Bar3DChartBuilder;
import ch.unibas.medizin.dynamicreports.report.builder.chart.PieChartBuilder;
import ch.unibas.medizin.dynamicreports.report.builder.chart.TimeSeriesChartBuilder;
import ch.unibas.medizin.dynamicreports.report.builder.column.PercentageColumnBuilder;
import ch.unibas.medizin.dynamicreports.report.builder.column.TextColumnBuilder;
import ch.unibas.medizin.dynamicreports.report.builder.group.ColumnGroupBuilder;
import ch.unibas.medizin.dynamicreports.report.builder.style.FontBuilder;
import ch.unibas.medizin.dynamicreports.report.builder.subtotal.AggregationSubtotalBuilder;
import ch.unibas.medizin.dynamicreports.report.constant.Orientation;
import ch.unibas.medizin.dynamicreports.report.exception.DRException;

import java.math.BigDecimal;
import java.util.Date;

import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.cht;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.cmp;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.col;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.grp;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.report;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.sbt;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.stl;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.type;

/**
 * <p>SalesDesign class.</p>
 *
 * @author Ricardo Mariaca
 * 
 */
public class SalesDesign {
    private final SalesData data = new SalesData();

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    public static void main(String[] args) {
        SalesDesign design = new SalesDesign();
        try {
            JasperReportBuilder report = design.build();
            report.show();
        } catch (DRException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>build.</p>
     *
     * @return a {@link ch.unibas.medizin.dynamicreports.jasper.builder.JasperReportBuilder} object.
     * @throws ch.unibas.medizin.dynamicreports.report.exception.DRException if any.
     */
    public JasperReportBuilder build() throws DRException {
        JasperReportBuilder report = report();

        // init styles
        FontBuilder boldFont = stl.fontArialBold();

        // init columns
        TextColumnBuilder<String> stateColumn = col.column("State", "state", type.stringType());
        TextColumnBuilder<String> itemColumn = col.column("Item", "item", type.stringType()).setPrintRepeatedDetailValues(false);
        TextColumnBuilder<Date> orderDateColumn = col.column("Order date", "orderdate", type.dateType());
        TextColumnBuilder<Integer> quantityColumn = col.column("Quantity", "quantity", type.integerType());
        TextColumnBuilder<BigDecimal> unitPriceColumn = col.column("Unit price", "unitprice", Templates.currencyType);
        // price = unitPrice * quantity
        TextColumnBuilder<BigDecimal> priceColumn = unitPriceColumn.multiply(quantityColumn).setTitle("Price").setDataType(Templates.currencyType);
        PercentageColumnBuilder pricePercColumn = col.percentageColumn("Price %", priceColumn);

        // init groups
        ColumnGroupBuilder stateGroup = grp.group(stateColumn);

        // init subtotals
        AggregationSubtotalBuilder<Number> priceAvg = sbt.avg(priceColumn).setValueFormatter(Templates.createCurrencyValueFormatter("avg = "));
        AggregationSubtotalBuilder<BigDecimal> unitPriceSum = sbt.sum(unitPriceColumn).setLabel("Total:").setLabelStyle(Templates.boldStyle);
        AggregationSubtotalBuilder<BigDecimal> priceSum = sbt.sum(priceColumn).setLabel("").setLabelStyle(Templates.boldStyle);

        // init charts
        Bar3DChartBuilder itemChart = cht.bar3DChart()
                                         .setTitle("Sales by item")
                                         .setTitleFont(boldFont)
                                         .setOrientation(Orientation.HORIZONTAL)
                                         .setCategory(itemColumn)
                                         .addSerie(cht.serie(unitPriceColumn), cht.serie(priceColumn));
        TimeSeriesChartBuilder dateChart =
            cht.timeSeriesChart().setTitle("Sales by date").setTitleFont(boldFont).setFixedHeight(150).setTimePeriod(orderDateColumn).addSerie(cht.serie(unitPriceColumn), cht.serie(priceColumn));
        PieChartBuilder stateChart = cht.pieChart().setTitle("Sales by state").setTitleFont(boldFont).setFixedHeight(100).setShowLegend(false).setKey(stateColumn).addSerie(cht.serie(priceColumn));

        // configure report
        report.addProperty("net.sf.jasperreports.chart.pie.ignore.duplicated.key", "true")
              .setTemplate(Templates.reportTemplate)
              // columns
              .columns(stateColumn, itemColumn, orderDateColumn, quantityColumn, unitPriceColumn, priceColumn, pricePercColumn)
              // groups
              .groupBy(stateGroup)
              // subtotals
              .subtotalsAtFirstGroupFooter(sbt.sum(unitPriceColumn), sbt.sum(priceColumn))
              .subtotalsOfPercentageAtGroupFooter(stateGroup, sbt.percentage(priceColumn).setShowInColumn(pricePercColumn))
              .subtotalsAtSummary(unitPriceSum, priceSum, priceAvg)
              // band components
              .title(Templates.createTitleComponent("Sales"), cmp.horizontalList(itemChart, cmp.verticalList(dateChart, stateChart)), cmp.verticalGap(10))
              .pageFooter(Templates.footerComponent)
              .setDataSource(data.createDataSource());

        return report;
    }
}
