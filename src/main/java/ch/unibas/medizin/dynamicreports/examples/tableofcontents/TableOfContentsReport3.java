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
package ch.unibas.medizin.dynamicreports.examples.tableofcontents;

import ch.unibas.medizin.dynamicreports.examples.Templates;
import ch.unibas.medizin.dynamicreports.report.builder.column.TextColumnBuilder;
import ch.unibas.medizin.dynamicreports.report.builder.group.ColumnGroupBuilder;
import ch.unibas.medizin.dynamicreports.report.datasource.DRDataSource;
import ch.unibas.medizin.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.col;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.grp;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.report;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.type;

/**
 * <p>TableOfContentsReport3 class.</p>
 *
 * @author Ricardo Mariaca
 * 
 */
public class TableOfContentsReport3 {

    /**
     * <p>Constructor for TableOfContentsReport3.</p>
     */
    public TableOfContentsReport3() {
        build();
    }

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    public static void main(String[] args) {
        new TableOfContentsReport3();
    }

    private void build() {
        TextColumnBuilder<String> countryColumn = col.column("Country", "country", type.stringType());
        TextColumnBuilder<String> itemColumn = col.column("Item", "item", type.stringType());

        ColumnGroupBuilder countryGroup = grp.group(countryColumn);
        ColumnGroupBuilder itemGroup = grp.group(itemColumn).setAddToTableOfContents(false);

        try {
            report().setTemplate(Templates.reportTemplate)
                    .tableOfContents()
                    .columns(countryColumn, itemColumn, col.column("Order date", "orderdate", type.dateType()), col.column("Quantity", "quantity", type.integerType()),
                             col.column("Unit price", "unitprice", type.bigDecimalType()))
                    .groupBy(countryGroup, itemGroup)
                    .title(Templates.createTitleComponent("TableOfContents3"))
                    .pageFooter(Templates.footerComponent)
                    .setDataSource(createDataSource())
                    .show();
        } catch (DRException e) {
            e.printStackTrace();
        }
    }

    private JRDataSource createDataSource() {
        String[] countries = new String[] {"USA", "Canada", "Mexico"};
        String[] items = new String[] {"Book", "Notebook", "PDA"};
        DRDataSource dataSource = new DRDataSource("country", "item", "orderdate", "quantity", "unitprice");
        for (String country : countries) {
            for (String item : items) {
                for (int i = 0; i < 8; i++) {
                    dataSource.add(country, item, toDate(2010, 1, (int) (Math.random() * 10) + 1), (int) (Math.random() * 10) + 1, BigDecimal.valueOf(Math.random() * 100 + 1));
                }
            }
        }
        return dataSource;
    }

    private Date toDate(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, day);
        return c.getTime();
    }
}
