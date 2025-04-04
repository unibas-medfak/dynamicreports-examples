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
package ch.unibas.medizin.dynamicreports.examples.crosstab;

import ch.unibas.medizin.dynamicreports.examples.Templates;
import ch.unibas.medizin.dynamicreports.report.builder.crosstab.CrosstabBuilder;
import ch.unibas.medizin.dynamicreports.report.builder.crosstab.CrosstabColumnGroupBuilder;
import ch.unibas.medizin.dynamicreports.report.builder.crosstab.CrosstabMeasureBuilder;
import ch.unibas.medizin.dynamicreports.report.builder.crosstab.CrosstabRowGroupBuilder;
import ch.unibas.medizin.dynamicreports.report.builder.style.ConditionalStyleBuilder;
import ch.unibas.medizin.dynamicreports.report.builder.style.StyleBuilder;
import ch.unibas.medizin.dynamicreports.report.constant.Calculation;
import ch.unibas.medizin.dynamicreports.report.constant.PageOrientation;
import ch.unibas.medizin.dynamicreports.report.constant.PageType;
import ch.unibas.medizin.dynamicreports.report.datasource.DRDataSource;
import ch.unibas.medizin.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

import java.awt.Color;
import java.math.BigDecimal;

import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.cmp;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.cnd;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.ctab;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.report;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.stl;

/**
 * <p>StyleCrosstabReport class.</p>
 *
 * @author Ricardo Mariaca
 * 
 */
public class StyleCrosstabReport {

    /**
     * <p>Constructor for StyleCrosstabReport.</p>
     */
    public StyleCrosstabReport() {
        build();
    }

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    public static void main(String[] args) {
        new StyleCrosstabReport();
    }

    private void build() {
        CrosstabRowGroupBuilder<String> rowGroup = ctab.rowGroup("state", String.class).setTotalHeader("Total for state");

        CrosstabColumnGroupBuilder<String> columnGroup = ctab.columnGroup("item", String.class);

        CrosstabMeasureBuilder<Integer> quantityMeasure = ctab.measure("Quantity", "quantity", Integer.class, Calculation.SUM);
        CrosstabMeasureBuilder<BigDecimal> unitPriceMeasure = ctab.measure("Unit price", "unitprice", BigDecimal.class, Calculation.SUM);

        ConditionalStyleBuilder condition1 = stl.conditionalStyle(cnd.greater(unitPriceMeasure, 600)).setBackgroundColor(new Color(210, 255, 210)).setBorder(stl.pen1Point());
        ConditionalStyleBuilder condition2 = stl.conditionalStyle(cnd.smaller(unitPriceMeasure, 150)).setBackgroundColor(new Color(255, 210, 210)).setBorder(stl.pen1Point());

        StyleBuilder unitPriceStyle = stl.style().conditionalStyles(condition1, condition2).setBorder(stl.pen1Point());
        StyleBuilder totalCellStyle = stl.style().setBackgroundColor(new Color(200, 200, 255)).setBorder(stl.pen1Point());

        unitPriceMeasure.setStyle(unitPriceStyle).setStyle(totalCellStyle, rowGroup).setStyle(totalCellStyle, rowGroup, columnGroup);
        quantityMeasure.setStyle(totalCellStyle, rowGroup).setStyle(totalCellStyle, rowGroup, columnGroup);

        CrosstabBuilder crosstab =
            ctab.crosstab().headerCell(cmp.text("State / Item").setStyle(Templates.boldCenteredStyle)).rowGroups(rowGroup).columnGroups(columnGroup).measures(quantityMeasure, unitPriceMeasure);

        try {
            report().setPageFormat(PageType.A4, PageOrientation.LANDSCAPE)
                    .setTemplate(Templates.reportTemplate)
                    .title(Templates.createTitleComponent("StyleCrosstab"))
                    .summary(crosstab)
                    .pageFooter(Templates.footerComponent)
                    .setDataSource(createDataSource())
                    .show();
        } catch (DRException e) {
            e.printStackTrace();
        }
    }

    private JRDataSource createDataSource() {
        DRDataSource dataSource = new DRDataSource("state", "item", "quantity", "unitprice");
        dataSource.add("New York", "Notebook", 1, BigDecimal.valueOf(500));
        dataSource.add("New York", "DVD", 5, BigDecimal.valueOf(30));
        dataSource.add("New York", "DVD", 2, BigDecimal.valueOf(45));
        dataSource.add("New York", "DVD", 4, BigDecimal.valueOf(36));
        dataSource.add("New York", "DVD", 5, BigDecimal.valueOf(41));
        dataSource.add("New York", "Book", 2, BigDecimal.valueOf(11));
        dataSource.add("New York", "Book", 8, BigDecimal.valueOf(9));
        dataSource.add("New York", "Book", 6, BigDecimal.valueOf(14));

        dataSource.add("Washington", "Notebook", 1, BigDecimal.valueOf(610));
        dataSource.add("Washington", "DVD", 4, BigDecimal.valueOf(40));
        dataSource.add("Washington", "DVD", 6, BigDecimal.valueOf(35));
        dataSource.add("Washington", "DVD", 3, BigDecimal.valueOf(46));
        dataSource.add("Washington", "DVD", 2, BigDecimal.valueOf(42));
        dataSource.add("Washington", "Book", 3, BigDecimal.valueOf(12));
        dataSource.add("Washington", "Book", 9, BigDecimal.valueOf(8));
        dataSource.add("Washington", "Book", 4, BigDecimal.valueOf(14));
        dataSource.add("Washington", "Book", 5, BigDecimal.valueOf(10));

        dataSource.add("Florida", "Notebook", 1, BigDecimal.valueOf(460));
        dataSource.add("Florida", "DVD", 3, BigDecimal.valueOf(49));
        dataSource.add("Florida", "DVD", 4, BigDecimal.valueOf(32));
        dataSource.add("Florida", "DVD", 2, BigDecimal.valueOf(47));
        dataSource.add("Florida", "Book", 4, BigDecimal.valueOf(11));
        dataSource.add("Florida", "Book", 8, BigDecimal.valueOf(6));
        dataSource.add("Florida", "Book", 6, BigDecimal.valueOf(16));
        dataSource.add("Florida", "Book", 3, BigDecimal.valueOf(18));
        return dataSource;
    }
}
