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
package net.sf.dynamicreports.examples.chart;

import net.sf.dynamicreports.examples.Templates;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.chart.ThermometerChartBuilder;
import net.sf.dynamicreports.report.constant.ValueLocation;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

import java.awt.Color;

import static net.sf.dynamicreports.report.builder.DynamicReports.cht;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

/**
 * <p>ThermometerChartReport class.</p>
 *
 * @author Ricardo Mariaca
 * 
 */
public class ThermometerChartReport {

    /**
     * <p>Constructor for ThermometerChartReport.</p>
     */
    public ThermometerChartReport() {
        build();
    }

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    public static void main(String[] args) {
        new ThermometerChartReport();
    }

    private void build() {
        ThermometerChartBuilder chart1 = cht.thermometerChart().setValue(DynamicReports.<Number>field("value", type.integerType()));

        ThermometerChartBuilder chart2 = cht.thermometerChart()
                                            .setValue(18)
                                            .setDataRangeHighExpression(30)
                                            .setValueColor(Color.BLACK)
                                            .setValueLocation(ValueLocation.BULB)
                                            .setLowDataRangeLowExpression(0)
                                            .setLowDataRangeHighExpression(10)
                                            .setMediumDataRangeLowExpression(10)
                                            .setMediumDataRangeHighExpression(20)
                                            .setHighDataRangeLowExpression(20)
                                            .setHighDataRangeHighExpression(30);

        try {
            report().setTemplate(Templates.reportTemplate)
                    .title(Templates.createTitleComponent("ThermometerChart"))
                    .summary(cmp.horizontalList(chart1, chart2))
                    .pageFooter(Templates.footerComponent)
                    .setDataSource(createDataSource())
                    .show();
        } catch (DRException e) {
            e.printStackTrace();
        }
    }

    private JRDataSource createDataSource() {
        DRDataSource dataSource = new DRDataSource("value");
        dataSource.add(40);
        return dataSource;
    }
}
