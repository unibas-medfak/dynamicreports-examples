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
package ch.unibas.medizin.dynamicreports.examples.column;

import ch.unibas.medizin.dynamicreports.examples.Templates;
import ch.unibas.medizin.dynamicreports.report.datasource.DRDataSource;
import ch.unibas.medizin.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.col;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.report;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.type;

/**
 * <p>EmptyColumnReport class.</p>
 *
 * @author Ricardo Mariaca
 * 
 */
public class EmptyColumnReport {

    /**
     * <p>Constructor for EmptyColumnReport.</p>
     */
    public EmptyColumnReport() {
        build();
    }

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    public static void main(String[] args) {
        new EmptyColumnReport();
    }

    private void build() {
        try {
            report().setTemplate(Templates.reportTemplate)
                    .columns(col.column("Item", "item", type.stringType()), col.emptyColumn().setFixedWidth(30), col.column("Quantity", "quantity", type.integerType()),
                             col.emptyColumn(false, true).setFixedWidth(20), col.column("Unit price", "unitprice", type.bigDecimalType()), col.emptyColumn(true, true).setFixedWidth(20),
                             col.column("Order date", "orderdate", type.dateType()))
                    .title(Templates.createTitleComponent("EmptyColumn"))
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
        return dataSource;
    }
}
