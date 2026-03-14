# DynamicReports Examples

A comprehensive collection of examples demonstrating the capabilities of DynamicReports Core - a free Java reporting library for creating reports dynamically.

## About

This repository contains practical examples showcasing various features and use cases of the DynamicReports library. DynamicReports is a powerful Java reporting library that allows you to create reports dynamically at runtime without requiring pre-designed templates.

## Features

The examples are organized into the following categories:

- **Getting Started** - Basic examples to help you get started with DynamicReports
- **Charts** - Examples demonstrating various chart types and visualizations
- **Chart Customization** - Advanced chart customization techniques
- **Columns** - Working with different column types and configurations
- **Column Grids** - Layout examples using column grids
- **Complex Reports** - Advanced real-world report scenarios including:
  - Sales crosstabs
  - Dynamic reports
  - Application forms
  - Sales table of contents
- **Components** - Using various report components
- **Crosstabs** - Creating and customizing crosstab reports
- **Data Sources** - Working with different data sources and data manipulation
- **Exporters** - Exporting reports to various formats (PDF, HTML, Excel, etc.)
- **Expressions** - Using expressions for dynamic calculations
- **Fonts** - Font customization and usage
- **Groups** - Grouping data and creating group headers/footers
- **Miscellaneous** - Various additional features and techniques
- **Styles** - Styling reports and components
- **Subreports** - Creating and using subreports
- **Subtotals** - Adding subtotals and aggregations
- **Table of Contents** - Generating table of contents for reports
- **Template Design** - Working with Jasper template designs

## Prerequisites

- Java 21 or higher
- Maven 3.x or higher

## Building the Project

To build the project, run:

```bash
mvn clean install
```

## Running Examples

Each example is a standalone Java class with a `main` method. You can run any example directly from your IDE or using Maven:

```bash
mvn exec:exec -Dexec.mainClass="ch.unibas.medizin.dynamicreports.examples.gettingstarted.SimpleReportStep01"
```

Replace the class name with any other example class you want to run.

## Project Structure

```
dynamicreports-examples/
├── src/main/java/ch/unibas/medizin/dynamicreports/examples/
│   ├── chart/                    # Chart examples
│   ├── chartcustomization/       # Chart customization examples
│   ├── column/                   # Column examples
│   ├── columngrid/               # Column grid examples
│   ├── complex/                  # Complex report examples
│   ├── component/                # Component examples
│   ├── crosstab/                 # Crosstab examples
│   ├── datasource/               # Data source examples
│   ├── exporter/                 # Export format examples
│   ├── expression/               # Expression examples
│   ├── fonts/                    # Font examples
│   ├── gettingstarted/           # Getting started examples
│   ├── group/                    # Grouping examples
│   ├── miscellaneous/            # Miscellaneous examples
│   ├── style/                    # Style examples
│   ├── subreport/                # Subreport examples
│   ├── subtotal/                 # Subtotal examples
│   ├── tableofcontents/          # Table of contents examples
│   ├── templatedesign/           # Template design examples
│   └── Templates.java            # Common templates and styles
└── pom.xml                       # Maven project configuration
```

## Dependencies

This project uses the following main dependencies:

- **DynamicReports Core** (7.0.6) - The core reporting library
- **JasperReports Jaxen** (7.0.6) - XPath support for JasperReports
- **HSQLDB** (2.7.4) - In-memory database for examples
- **Apache Log4j** (2.25.2) - Logging framework
- **BouncyCastle** (1.83) - For encrypted PDF examples

## License

This project is licensed under the GNU Lesser General Public License v3.0 (LGPL-3.0). See the [license.txt](license.txt) file for details.

## Resources

- **DynamicReports Core**: https://github.com/unibas-medfak/dynamicreports-core
- **DynamicReports**: http://www.dynamicreports.org
- **JasperReports**: https://community.jaspersoft.com/project/jasperreports-library

## Contributing

Contributions are welcome! If you have additional examples or improvements, please feel free to submit a pull request.

## Credits

Original examples by Ricardo Mariaca and the Dynamic Reports Contributors.
Maintained by the University of Basel Medical Faculty.
