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
package ch.unibas.medizin.dynamicreports.examples.chart;

import ch.unibas.medizin.dynamicreports.examples.Templates;
import ch.unibas.medizin.dynamicreports.report.builder.column.TextColumnBuilder;
import ch.unibas.medizin.dynamicreports.report.builder.style.FontBuilder;
import ch.unibas.medizin.dynamicreports.report.datasource.DRDataSource;
import ch.unibas.medizin.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

import java.util.stream.IntStream;

import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.cht;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.col;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.report;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.stl;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.type;

/**
 * <p>ScatterChartReport class.</p>
 *
 * @author Ricardo Mariaca
 * 
 */
public class ScatterChartReport {

    /**
     * <p>Constructor for ScatterChartReport.</p>
     */
    public ScatterChartReport() {
        build();
    }

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    public static void main(String[] args) {
        new ScatterChartReport();
    }

    private void build() {
        FontBuilder boldFont = stl.fontArialBold().setFontSize(12);

        TextColumnBuilder<Integer> xColumn = col.column("X", "x", type.integerType());
        TextColumnBuilder<Integer> y1Column = col.column("Y1", "y1", type.integerType());
        TextColumnBuilder<Integer> y2Column = col.column("Y2", "y2", type.integerType());

        try {
            report().setTemplate(Templates.reportTemplate)
                    .columns(xColumn, y1Column, y2Column)
                    .title(Templates.createTitleComponent("ScatterChart"))
                    .summary(cht.scatterChart()
                                .setTitle("Scatter chart")
                                .setTitleFont(boldFont)
                                .setShowLines(false)
                                .setXValue(xColumn)
                                .series(cht.xySerie(y1Column), cht.xySerie(y2Column))
                                .setXAxisFormat(cht.axisFormat().setLabel("X"))
                                .setYAxisFormat(cht.axisFormat().setLabel("Y")))
                    .pageFooter(Templates.footerComponent)
                    .setDataSource(createDataSource())
                    .show();
        } catch (DRException e) {
            e.printStackTrace();
        }
    }

    private JRDataSource createDataSource() {
        DRDataSource dataSource = new DRDataSource("x", "y1", "y2");
        IntStream.range(-10, 10).forEachOrdered(i -> dataSource.add(i, i + (int) (Math.random() * 5), i + (int) (Math.random() * 5)));
        return dataSource;
    }
}
