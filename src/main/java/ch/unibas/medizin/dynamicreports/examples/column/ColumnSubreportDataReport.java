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
import ch.unibas.medizin.dynamicreports.jasper.builder.JasperReportBuilder;
import ch.unibas.medizin.dynamicreports.report.base.expression.AbstractSimpleExpression;
import ch.unibas.medizin.dynamicreports.report.builder.component.SubreportBuilder;
import ch.unibas.medizin.dynamicreports.report.definition.ReportParameters;
import ch.unibas.medizin.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.cmp;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.col;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.field;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.report;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.type;

/**
 * <p>ColumnSubreportDataReport class.</p>
 *
 * @author Ricardo Mariaca
 * 
 */
public class ColumnSubreportDataReport {

    /**
     * <p>Constructor for ColumnSubreportDataReport.</p>
     */
    public ColumnSubreportDataReport() {
        build();
    }

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    public static void main(String[] args) {
        new ColumnSubreportDataReport();
    }

    private void build() {
        SubreportBuilder subreport = cmp.subreport(new SubreportDesign()).setDataSource(new SubreportData());
        try {
            report().setTemplate(Templates.reportTemplate)
                    .fields(field("comments", List.class))
                    .columns(col.column("Item", "item", type.stringType()), col.column("Quantity", "quantity", type.integerType()), col.componentColumn("Comments", subreport))
                    .title(Templates.createTitleComponent("ColumnSubreportData"))
                    .pageFooter(Templates.footerComponent)
                    .setDataSource(createDataSource())
                    .show();
        } catch (DRException e) {
            e.printStackTrace();
        }
    }

    private JRDataSource createDataSource() {
        List<ReportData> datasource = new ArrayList<>();

        ReportData data = new ReportData();
        List<Map<String, Object>> comments = new ArrayList<>();
        Map<String, Object> values = new HashMap<>();
        values.put("comment", "comment1");
        comments.add(values);
        values = new HashMap<>();
        values.put("comment", "comment2");
        comments.add(values);
        values = new HashMap<>();
        values.put("comment", "comment3");
        comments.add(values);
        data.setItem("Book");
        data.setQuantity(10);
        data.setComments(comments);
        datasource.add(data);

        data = new ReportData();
        comments = new ArrayList<>();
        values = new HashMap<>();
        values.put("comment", "comment1");
        comments.add(values);
        values = new HashMap<>();
        values.put("comment", "comment2");
        comments.add(values);
        data.setItem("Notebook");
        data.setQuantity(20);
        data.setComments(comments);
        datasource.add(data);

        return new JRBeanCollectionDataSource(datasource);
    }

    private static class SubreportDesign extends AbstractSimpleExpression<JasperReportBuilder> {
        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public JasperReportBuilder evaluate(ReportParameters reportParameters) {
            return report().columns(col.column("comment", type.stringType()));
        }
    }

    private static class SubreportData extends AbstractSimpleExpression<JRDataSource> {
        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public JRDataSource evaluate(ReportParameters reportParameters) {
            Collection<Map<String, ?>> value = reportParameters.getValue("comments");
            return new JRMapCollectionDataSource(value);
        }
    }

    public static class ReportData {
        private String item;
        private Integer quantity;
        private List<Map<String, Object>> comments;

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public List<Map<String, Object>> getComments() {
            return comments;
        }

        public void setComments(List<Map<String, Object>> comments) {
            this.comments = comments;
        }
    }
}
