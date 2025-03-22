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
package net.sf.dynamicreports.examples.chartcustomization;

import static net.sf.dynamicreports.report.builder.DynamicReports.cht;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.io.Serializable;
import java.math.BigDecimal;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.renderer.category.BarRenderer;

import net.sf.dynamicreports.examples.Templates;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.chart.BarChartBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.style.FontBuilder;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.chart.DRIChartCustomizer;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

/**
 * <p>ChartLayoutReport class.</p>
 *
 * @author Ricardo Mariaca
 *
 */
public class ChartLayoutReport {

    /**
     * <p>Constructor for ChartLayoutReport.</p>
     */
    public ChartLayoutReport() {
        build();
    }

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    public static void main(String[] args) {
        new ChartLayoutReport();
    }

    private void build() {
        final FontBuilder boldFont = stl.fontArialBold().setFontSize(12);

        final TextColumnBuilder<String> itemColumn = col.column("Item", "item", type.stringType());
        final TextColumnBuilder<Integer> quantityColumn = col.column("Quantity", "quantity", type.integerType());
        final TextColumnBuilder<BigDecimal> unitPriceColumn = col.column("Unit price", "unitprice", type.bigDecimalType());

        final JasperReportBuilder subreport = report();
        subreport.setTemplate(Templates.reportTemplate).columns(itemColumn, quantityColumn, unitPriceColumn).setDataSource(createDataSource());

        final BarChartBuilder chart = cht.barChart()
                                   .customizers(new ChartCustomizer())
                                   .setTitle("Bar chart")
                                   .setTitleFont(boldFont)
                                   .setCategory(itemColumn)
                                   .series(cht.serie(quantityColumn), cht.serie(unitPriceColumn))
                                   .setValueAxisFormat(cht.axisFormat().setRangeMaxValueExpression(500))
                                   .setCategoryAxisFormat(cht.axisFormat().setLabel("Item"));

        try {
            report().setTemplate(Templates.reportTemplate)
                    .title(Templates.createTitleComponent("ChartLayout"))
                    .summary(cmp.horizontalList(chart, cmp.subreport(subreport)))
                    .pageFooter(Templates.footerComponent)
                    .setDataSource(createDataSource())
                    .show();
        } catch (final DRException e) {
            e.printStackTrace();
        }
    }

    private JRDataSource createDataSource() {
        final DRDataSource dataSource = new DRDataSource("item", "quantity", "unitprice");
        dataSource.add("Tablet", 350, BigDecimal.valueOf(300));
        dataSource.add("Laptop", 300, BigDecimal.valueOf(500));
        dataSource.add("Smartphone", 450, BigDecimal.valueOf(250));
        return dataSource;
    }

    private class ChartCustomizer implements DRIChartCustomizer, Serializable {
        private static final long serialVersionUID = 1L;

        @Override
        public void customize(JFreeChart chart, ReportParameters reportParameters) {
            final BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
            renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
            renderer.setDefaultItemLabelsVisible(true);
        }
    }
}
