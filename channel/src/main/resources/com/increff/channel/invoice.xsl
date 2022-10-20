<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:date="http://exslt.org/dates-and-times"
                version="1.0"
                extension-element-prefixes="date">
    <xsl:import href="fop/functions/date/date.date.xsl"/>
    <xsl:output method="xml" indent="yes"/>
    <xsl:decimal-format name="euro" decimal-separator="," grouping-separator="."/>
    <xsl:template match="channelInvoiceData">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="A4" page-height="29.7cm" page-width="21cm" margin-top="2cm"
                                       margin-bottom="2cm" margin-left="2cm" margin-right="2cm"
                                       font-family="Calibri">
                    <fo:region-body/>
                    <fo:region-after region-name="invoice-footer" extent="5mm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="A4">
                <fo:flow flow-name="xsl-region-body" font-size="8pt">
                    <fo:block space-after="1cm"></fo:block>

                    <fo:block space-before="2cm" space-after="2cm" text-align="center" font-size="20pt"
                              font-weight="bold">
                        INVOICE
                    </fo:block>

                    <fo:table>
                        <fo:table-column column-width="30%"/>
                        <fo:table-column column-width="30%"/>
                        <fo:table-column column-width="30%"/>

                        <fo:table-body>
                            <fo:table-row>
                                <fo:table-cell text-align="center">
                                    <fo:block>Order Id:
                                        <xsl:value-of select="orderId"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell text-align="center">
                                    <fo:block>Time:
                                        <!-- <xsl:value-of select="date:format-date(invoice-date, 'dd-MM-yyyy')"/> -->
                                        <xsl:value-of select="invoiceTime"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell text-align="center">
                                    <fo:block>Channel Name:
                                        <xsl:value-of select="channelName"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell text-align="center">
                                    <fo:block>Channel Name:
                                        <xsl:value-of select="customerName"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>


                    <fo:block space-after="1cm"></fo:block>
                    <fo:table>
                        <fo:table-column column-width="20%"/>
                        <fo:table-column column-width="30%"/>
                        <fo:table-column column-width="10%"/>
                        <fo:table-column column-width="20%"/>
                        <fo:table-column column-width="20%"/>

                        <fo:table-header font-weight="bold">
                            <fo:table-row border-bottom="1px solid #eee">
                                <fo:table-cell text-align="center" padding-bottom="2px">
                                    <fo:block>Channel Sku Id</fo:block>
                                </fo:table-cell>
<!--                                <fo:table-cell text-align="center" padding-bottom="2px">-->
<!--                                    <fo:block>Product Name</fo:block>-->
<!--                                </fo:table-cell>-->
                                <fo:table-cell text-align="center" padding-bottom="2px">
                                    <fo:block>Quantity</fo:block>
                                </fo:table-cell>
                                <fo:table-cell text-align="center" padding-bottom="2px">
                                    <fo:block>Selling Price per Unit</fo:block>
                                </fo:table-cell>
                                <fo:table-cell text-align="center" padding-bottom="2px">
                                    <fo:block>Amount</fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-header>
                        <fo:table-body>
                            <xsl:for-each select="orderItems">
                                <fo:table-row border-bottom="1px solid #eee">
                                    <fo:table-cell text-align="center" padding-bottom="2px" padding-top="2px">
                                        <fo:block>
                                            <xsl:value-of select="channSku"/>
                                        </fo:block>
                                    </fo:table-cell>
<!--                                    <fo:table-cell text-align="center" padding-bottom="2px" padding-top="2px">-->
<!--                                        <fo:block>-->
<!--                                            <xsl:value-of select="product-name"/>-->
<!--                                        </fo:block>-->
<!--                                    </fo:table-cell>-->
                                    <fo:table-cell text-align="center" padding-bottom="2px" padding-top="2px">
                                        <fo:block>
                                            <xsl:value-of select="format-number(orderedQuantity, '###,###.00')"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell text-align="center" padding-bottom="2px" padding-top="2px">
                                        <fo:block>
                                            <xsl:value-of select="format-number(sellingPricePerUnit, '###,###.00')"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell text-align="center" padding-bottom="2px" padding-top="2px">
                                        <fo:block>
                                            <xsl:value-of select="format-number(orderedQuantity * sellingPricePerUnit, '###.00')"/>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </xsl:for-each>
                        </fo:table-body>
                    </fo:table>
                    <fo:block space-after="1cm"></fo:block>
                    <fo:table>
                        <fo:table-column column-width="9cm"/>
                        <fo:table-column column-width="4cm"/>
                        <fo:table-column column-width="4cm"/>
                        <fo:table-body>
                            <fo:table-row>
                                <fo:table-cell>
                                    <fo:block></fo:block>
                                </fo:table-cell>
                                <fo:table-cell text-align="center" padding-bottom="2px" padding-top="2px">
                                    <fo:block font-weight="bold">Sub Total:</fo:block>
                                </fo:table-cell>
                                <fo:table-cell text-align="center" padding-bottom="2px" padding-top="2px">
                                    <fo:block>
                                        <xsl:value-of select="format-number(total, '###,###.00')"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>