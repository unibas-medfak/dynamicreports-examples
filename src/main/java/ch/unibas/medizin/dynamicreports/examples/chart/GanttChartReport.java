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
import ch.unibas.medizin.dynamicreports.report.builder.chart.GanttChartBuilder;
import ch.unibas.medizin.dynamicreports.report.builder.column.TextColumnBuilder;
import ch.unibas.medizin.dynamicreports.report.datasource.DRDataSource;
import ch.unibas.medizin.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

import java.util.Calendar;
import java.util.Date;

import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.cht;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.col;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.report;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.type;

/**
 * <p>GanttChartReport class.</p>
 *
 * @author Ricardo Mariaca
 * 
 */
public class GanttChartReport {

    /**
     * <p>Constructor for GanttChartReport.</p>
     */
    public GanttChartReport() {
        build();
    }

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    public static void main(String[] args) {
        new GanttChartReport();
    }

    private void build() {
        TextColumnBuilder<String> taskColumn = col.column("Task", "task", type.stringType());
        TextColumnBuilder<Date> actualStartDateColumn = col.column("Actual start date", "actualstartdate", type.dateType());
        TextColumnBuilder<Date> actualEndDateColumn = col.column("Actual end date", "actualenddate", type.dateType());
        TextColumnBuilder<Date> scheduledStartDateColumn = col.column("Scheduled start date", "scheduledstartdate", type.dateType());
        TextColumnBuilder<Date> scheduledEndDateColumn = col.column("Scheduled end date", "scheduledenddate", type.dateType());
        TextColumnBuilder<Double> actualColumn = col.column("Actual", "actual", type.doubleType());

        GanttChartBuilder chart1 = cht.ganttChart()
                                      .setTask(taskColumn)
                                      .series(cht.ganttSerie().setStartDate(scheduledStartDateColumn).setEndDate(scheduledEndDateColumn).setLabel("Scheduled"),
                                              cht.ganttSerie().setStartDate(actualStartDateColumn).setEndDate(actualEndDateColumn).setLabel("Actual"))
                                      .setTimeAxisFormat(cht.axisFormat().setLabel("Time"))
                                      .setTaskAxisFormat(cht.axisFormat().setLabel("Task"));

        GanttChartBuilder chart2 = cht.ganttChart()
                                      .setTask(taskColumn)
                                      .series(cht.ganttSerie().setStartDate(scheduledStartDateColumn).setEndDate(scheduledEndDateColumn).setPercent(actualColumn).setLabel("Scheduled"))
                                      .setTimeAxisFormat(cht.axisFormat().setLabel("Time"))
                                      .setTaskAxisFormat(cht.axisFormat().setLabel("Task"));

        try {
            report().setTemplate(Templates.reportTemplate)
                    .columns(taskColumn, actualStartDateColumn, actualEndDateColumn, scheduledStartDateColumn, scheduledEndDateColumn, actualColumn)
                    .title(Templates.createTitleComponent("GanttChart"))
                    .summary(chart1, chart2)
                    .pageFooter(Templates.footerComponent)
                    .setDataSource(createDataSource())
                    .show();
        } catch (DRException e) {
            e.printStackTrace();
        }
    }

    private JRDataSource createDataSource() {
        DRDataSource dataSource = new DRDataSource("task", "actualstartdate", "actualenddate", "scheduledstartdate", "scheduledenddate", "actual");
        dataSource.add("Proposal", toDate(2011, 1, 5), toDate(2011, 1, 7), toDate(2011, 1, 4), toDate(2011, 1, 7), 1d);
        dataSource.add("Analysis", toDate(2011, 1, 7), toDate(2011, 1, 14), toDate(2011, 1, 7), toDate(2011, 1, 12), 0.8d);
        dataSource.add("Implementation", toDate(2011, 1, 15), toDate(2011, 1, 27), toDate(2011, 1, 12), toDate(2011, 1, 25), 0.3d);
        dataSource.add("Testing", toDate(2011, 1, 27), toDate(2011, 1, 30), toDate(2011, 1, 25), toDate(2011, 1, 28), 0d);
        return dataSource;
    }

    private Date toDate(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, day);
        return c.getTime();
    }
}
