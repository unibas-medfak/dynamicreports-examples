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
package ch.unibas.medizin.dynamicreports.examples.complex.salestableofcontents;

import ch.unibas.medizin.dynamicreports.examples.Templates;
import ch.unibas.medizin.dynamicreports.jasper.base.tableofcontents.JasperTocHeading;
import ch.unibas.medizin.dynamicreports.jasper.builder.JasperReportBuilder;
import ch.unibas.medizin.dynamicreports.report.base.expression.AbstractSimpleExpression;
import ch.unibas.medizin.dynamicreports.report.builder.FieldBuilder;
import ch.unibas.medizin.dynamicreports.report.builder.HyperLinkBuilder;
import ch.unibas.medizin.dynamicreports.report.builder.VariableBuilder;
import ch.unibas.medizin.dynamicreports.report.builder.column.TextColumnBuilder;
import ch.unibas.medizin.dynamicreports.report.builder.component.ComponentBuilder;
import ch.unibas.medizin.dynamicreports.report.builder.component.HorizontalListBuilder;
import ch.unibas.medizin.dynamicreports.report.builder.component.TextFieldBuilder;
import ch.unibas.medizin.dynamicreports.report.builder.expression.AbstractComplexExpression;
import ch.unibas.medizin.dynamicreports.report.builder.group.CustomGroupBuilder;
import ch.unibas.medizin.dynamicreports.report.builder.style.ConditionalStyleBuilder;
import ch.unibas.medizin.dynamicreports.report.builder.style.StyleBuilder;
import ch.unibas.medizin.dynamicreports.report.builder.tableofcontents.TableOfContentsCustomizer;
import ch.unibas.medizin.dynamicreports.report.constant.Calculation;
import ch.unibas.medizin.dynamicreports.report.constant.Evaluation;
import ch.unibas.medizin.dynamicreports.report.constant.GroupHeaderLayout;
import ch.unibas.medizin.dynamicreports.report.constant.HorizontalTextAlignment;
import ch.unibas.medizin.dynamicreports.report.constant.HyperLinkType;
import ch.unibas.medizin.dynamicreports.report.constant.PageOrientation;
import ch.unibas.medizin.dynamicreports.report.constant.PageType;
import ch.unibas.medizin.dynamicreports.report.definition.DRICustomValues;
import ch.unibas.medizin.dynamicreports.report.definition.ReportParameters;
import ch.unibas.medizin.dynamicreports.report.exception.DRException;

import java.awt.Color;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.cmp;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.col;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.exp;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.grp;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.hyperLink;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.report;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.stl;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.type;
import static ch.unibas.medizin.dynamicreports.report.builder.DynamicReports.variable;

/**
 * <p>SalesTableOfContentsDesign class.</p>
 *
 * @author Ricardo Mariaca
 * 
 */
public class SalesTableOfContentsDesign {
    private final SalesTableOfContentsData data = new SalesTableOfContentsData();

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    public static void main(String[] args) {
        SalesTableOfContentsDesign design = new SalesTableOfContentsDesign();
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
     */
    public JasperReportBuilder build() {
        JasperReportBuilder report = report();

        TextColumnBuilder<String> countryColumn = col.column("Country", "country", type.stringType());
        TextColumnBuilder<String> itemColumn = col.column("Item", "item", type.stringType());

        StyleBuilder headingToc1Style = stl.style(Templates.rootStyle).italic();

        CustomTableOfContentsCustomizer tableOfContentsCustomizer = new CustomTableOfContentsCustomizer();
        tableOfContentsCustomizer.setHeadingStyle(1, headingToc1Style);
        tableOfContentsCustomizer.setTextFixedWidth(100);
        tableOfContentsCustomizer.setPageIndexFixedWidth(30);

        TextFieldBuilder<String> pageHeader = cmp.text(new PageHeaderExpression()).setStyle(Templates.bold12CenteredStyle).setEvaluationTime(Evaluation.PAGE);

        report.setPageFormat(PageType.A5, PageOrientation.LANDSCAPE)
              .setTemplate(Templates.reportTemplate)
              .setTableOfContents(tableOfContentsCustomizer)
              .columns(countryColumn, itemColumn, col.column("Order date", "orderdate", type.dateType()), col.column("Quantity", "quantity", type.integerType()),
                       col.column("Unit price", "unitprice", type.bigDecimalType()))
              .groupBy(countryColumn, itemColumn)
              .pageHeader(pageHeader)
              .title(Templates.createTitleComponent("SalesTableOfContents"))
              .pageFooter(Templates.footerComponent)
              .setDataSource(data.createDataSource());

        return report;
    }

    private static class PageHeaderExpression extends AbstractSimpleExpression<String> {
        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public String evaluate(ReportParameters reportParameters) {
            DRICustomValues customValues = reportParameters.getParameterValue(DRICustomValues.NAME);
            Map<String, JasperTocHeading> tocHeadings = customValues.getTocHeadings();
            if (tocHeadings.isEmpty()) {
                return "";
            }
            List<JasperTocHeading> headings = new ArrayList<>(tocHeadings.values());
            for (int i = headings.size() - 1; i >= 0; i--) {
                JasperTocHeading jasperTocHeading = headings.get(i);
                if (jasperTocHeading.getLevel() == 0) {
                    return "Country: " + jasperTocHeading.getText();
                }
            }
            return "";
        }
    }

    private static class CustomTableOfContentsCustomizer extends TableOfContentsCustomizer {
        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public void customize() {
            super.customize();

            CustomGroupBuilder countryGroup = grp.group(new CountryExpression(textField)).setHeaderLayout(GroupHeaderLayout.EMPTY).header(countryHeadingComponent()).footer(cmp.verticalGap(5));

            report.setPageColumnsPerPage(2).setPageColumnSpace(10).groupBy(countryGroup);
        }

        private ComponentBuilder<?, ?> countryHeadingComponent() {
            HorizontalListBuilder headingComponent = cmp.horizontalList();

            HyperLinkBuilder countryReferenceHyperLink = hyperLink();
            countryReferenceHyperLink.setAnchor(new CountryExpression(referenceField));
            countryReferenceHyperLink.setType(HyperLinkType.LOCAL_ANCHOR);

            StyleBuilder style = stl.style(Templates.rootStyle).setFontSize(12).bold().setBackgroundColor(Color.LIGHT_GRAY);

            TextFieldBuilder<String> textComponent = cmp.text(textField).setHyperLink(countryReferenceHyperLink).setStyle(style);
            headingComponent.add(textComponent);

            TextFieldBuilder<String> pageIndexComponent =
                cmp.text(new CountryHeadingExpression()).setHyperLink(countryReferenceHyperLink).setStyle(style).setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT);
            headingComponent.add(pageIndexComponent);

            return headingComponent;
        }

        @Override
        protected ComponentBuilder<?, ?> headingComponent(int level) {
            if (level == 0) {
                return cmp.filler();
            }

            ComponentBuilder<?, ?> headingComponent = super.headingComponent(level);

            ConditionalStyleBuilder conditionalStyle = stl.conditionalStyle(exp.printInOddRow()).setBackgroundColor(new Color(245, 245, 245));
            StyleBuilder rowStyle = stl.style().conditionalStyles(conditionalStyle);
            headingComponent.setStyle(rowStyle);

            return headingComponent;
        }

        private final class CountryExpression extends AbstractComplexExpression<String> {
            @Serial
            private static final long serialVersionUID = 1L;

            private String value;

            private CountryExpression(FieldBuilder<String> field) {
                addExpression(levelField);
                addExpression(field);
            }

            @Override
            public String evaluate(List<?> values, ReportParameters reportParameters) {
                int level = (Integer) values.get(0);
                if (level == 0) {
                    value = (String) values.get(1);
                }
                return value;
            }
        }

        private final class CountryHeadingExpression extends AbstractComplexExpression<String> {
            @Serial
            private static final long serialVersionUID = 1L;

            private CountryHeadingExpression() {
                VariableBuilder<Integer> minCountryPage = variable(pageIndexField, Calculation.LOWEST).setResetType(Evaluation.FIRST_GROUP);
                addExpression(minCountryPage);

                VariableBuilder<Integer> maxCountryPage = variable(pageIndexField, Calculation.HIGHEST).setResetType(Evaluation.FIRST_GROUP);
                addExpression(maxCountryPage);
            }

            @Override
            public String evaluate(List<?> values, ReportParameters reportParameters) {
                return values.get(0) + " - " + values.get(1);
            }
        }
    }
}
