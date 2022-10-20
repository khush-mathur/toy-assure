package com.increff.service;

import com.increff.dao.ChannelOrderDao;
import com.increff.dao.ChannelOrderItemDao;
import com.increff.exception.ApiException;
import com.increff.model.data.ChannelInvoiceData;
import com.increff.model.data.InvoiceData;
import com.increff.pojo.ChannelOrderItemPojo;
import com.increff.pojo.ChannelOrderPojo;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.xmlgraphics.util.MimeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.file.Files;
import java.util.List;

@Service
public class ChannelOrderService {
    @Autowired
    ChannelOrderDao channelOrderDao;

    @Autowired
    ChannelOrderItemDao channelOrderItemDao;

    public File generateInvoice(ChannelInvoiceData invoiceData) throws ApiException {
        String invoice = "main/resources/invoice/invoice" + invoiceData.getOrderId() + ".pdf";
        String xml = jaxbObjectToXML(invoiceData);
        System.out.println(xml);
        File xsltFile = new File("src", "main/resources/com/increff/channel/invoice.xsl");
        File pdfFile = new File("src", invoice);
        try {
            convertToPDF(xsltFile, pdfFile, xml);
        } catch (IOException e) {
            throw new ApiException(e.getMessage());
        }

        return pdfFile;
    }

    private static String jaxbObjectToXML(ChannelInvoiceData invoiceData) {
        StringWriter stringWriter = new StringWriter();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ChannelInvoiceData.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            jaxbMarshaller.marshal(invoiceData, stringWriter);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }

    private void convertToPDF(File xslt, File pdf, String xml) throws ApiException, IOException {
        OutputStream out = null;
        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        try {
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            out = Files.newOutputStream(pdf.toPath());
            out = new java.io.BufferedOutputStream(out);
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xslt));
            Source src = new StreamSource(new StringReader(xml));
            Result res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(src, res);
        } catch (FOPException e) {
            System.out.println("FOP Exception");
            throw new ApiException(e.getMessage());
        } catch (TransformerException e) {
            System.out.println("Transformer Exception");
            throw new ApiException(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApiException(e.getMessage());
        } finally {
            if(out==null)
                System.out.println("out is null");
            else
                out.close();
        }
    }

    @Transactional(rollbackFor = ApiException.class)
    public void addOrder(ChannelOrderPojo orderPojo) {
        channelOrderDao.create(orderPojo);
    }
    @Transactional(rollbackFor = ApiException.class)
    public void addOrderItem(ChannelOrderItemPojo orderItemPojo) {
        channelOrderItemDao.add(orderItemPojo);
    }

    @Transactional(readOnly = true)
    public List<ChannelOrderPojo> getAll() {
        return channelOrderDao.selectAll();
    }

    @Transactional(readOnly = true)
    public List<ChannelOrderItemPojo> getItemsByOrderId(Long orderId) {
        return channelOrderItemDao.getByOrderId(orderId);
    }

    @Transactional(readOnly = true)
    public ChannelOrderPojo getById(Long orderId) {
        return channelOrderDao.getById(orderId);
    }
}
