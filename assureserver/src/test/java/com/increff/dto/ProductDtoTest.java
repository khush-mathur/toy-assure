package com.increff.dto;

import com.increff.AbstractUnitTest;
import com.increff.exception.ApiException;
import com.increff.model.data.ProductData;
import com.increff.model.data.UserData;
import com.increff.model.form.ProductForm;
import com.increff.model.form.UserForm;
import com.increff.model.retrundata.ProductReturnData;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class ProductDtoTest extends AbstractUnitTest {
    @Resource
    ProductDto productDto;

    @Resource
    UserDto userDto;

    @Test
    public void createProduct() throws ApiException {
        String name = RandomStringUtils.random(6);
        String brand = RandomStringUtils.random(6);
        String description = RandomStringUtils.random(6);
        String clientSku = RandomStringUtils.random(6,true,true);
        String mrp = String.valueOf(new Random().nextDouble());
        List<ProductForm> formList = new ArrayList<>();
        formList.add(createProductForm(name,brand,mrp,description,clientSku));
        List<ProductReturnData> productList = productDto.create(createClient().getId(),formList);
        assertEquals("Product Added",productList.get(0).getLog());
        assertEquals(name.trim().toLowerCase(),productList.get(0).getName());
        assertEquals(brand.trim().toLowerCase(),productList.get(0).getBrand());
        assertEquals(description.trim().toLowerCase(),productList.get(0).getDescription());
        assertEquals(clientSku.trim().toLowerCase(),productList.get(0).getClientSkuId());
        assertEquals(String.valueOf(Math.round(Double.parseDouble(mrp) * 100.0) / 100.0),productList.get(0).getMrp());
    }
    @Test
    public void createProductWInvalidClient() throws ApiException {
        String name = RandomStringUtils.random(6);
        String brand = RandomStringUtils.random(6);
        String description = RandomStringUtils.random(6);
        String clientSku = RandomStringUtils.random(6,true,true);
        String mrp = String.valueOf(new Random().nextDouble());
        List<ProductForm> formList = new ArrayList<>();
        formList.add(createProductForm(name,brand,mrp,description,clientSku));
        List<ProductReturnData> productList = productDto.create(null, formList);
        assertEquals("Unable to add product Invalid Input : Client Id cannot be empty",productList.get(0).getLog());
    }

    @Test
    public void createProductWInvalidClientId() throws ApiException {
        String name = RandomStringUtils.random(6);
        String brand = RandomStringUtils.random(6);
        String description = RandomStringUtils.random(6);
        String clientSku = RandomStringUtils.random(6,true,true);
        String mrp = String.valueOf(new Random().nextDouble());
        List<ProductForm> formList = new ArrayList<>();
        formList.add(createProductForm(name,brand,mrp,description,clientSku));
        List<ProductReturnData> productList = productDto.create(new Random().nextLong(), formList);
        assertEquals("Unable to add product No such client exists.",productList.get(0).getLog());
    }

    @Test
    public void createProductWInvalidInput() throws ApiException {
        String name = RandomStringUtils.random(6);
        String brand = RandomStringUtils.random(6);
        String description = RandomStringUtils.random(6);
        String clientSku = RandomStringUtils.random(6,true,true);
        String mrp = String.valueOf(new Random().nextDouble());

        List<ProductForm> formList = new ArrayList<>();
        formList.add(createProductForm("",brand,mrp,description,clientSku));
        formList.add(createProductForm(name,"",mrp,description,clientSku));
        formList.add(createProductForm(name,brand,"-536.6",description,clientSku));
        formList.add(createProductForm(name,brand,mrp,"",clientSku));
        formList.add(createProductForm(name,brand,mrp,description," "));

        List<ProductReturnData> productList = productDto.create(createClient().getId(), formList);
        assertEquals("Unable to add product Invalid Input : Enter correct Name",productList.get(0).getLog());
        assertEquals("Unable to add product Invalid Input : Enter correct Brand",productList.get(1).getLog());
        assertEquals("Unable to add product Invalid Input : Enter correct Mrp",productList.get(2).getLog());
        assertEquals("Unable to add product Invalid Input : Enter correct Description",productList.get(3).getLog());
        assertEquals("Unable to add product Invalid Input : Enter correct SKU Name",productList.get(4).getLog());
    }

    @Test
    public void getAll() throws ApiException {
        List<ProductForm> formList = new ArrayList<>();
        for(int i =0;i<3;i++){
            formList.add(createProductForm(RandomStringUtils.random(6),RandomStringUtils.random(6),
                    String.valueOf(new Random().nextDouble()),RandomStringUtils.random(6),RandomStringUtils.random(6)));
        }
        productDto.create(createClient().getId(),formList);
        List<ProductData> productList = productDto.getAll();
        assertEquals(3,productList.size());
    }

    @Test
    public void getById() throws ApiException {
        List<ProductForm> formList = new ArrayList<>();
        formList.add(createProductForm(RandomStringUtils.random(6),RandomStringUtils.random(6),
                    String.valueOf(new Random().nextDouble()),RandomStringUtils.random(6),RandomStringUtils.random(6)));
        List<ProductReturnData> createdProductList = productDto.create(createClient().getId(),formList);
        List<ProductData> productList = productDto.getAll();
        ProductData fetchedProduct = productDto.get(productList.get(0).getGlobalSkuId());
        assertEquals(createdProductList.get(0).getName(),fetchedProduct.getName());
        assertEquals(createdProductList.get(0).getBrand(),fetchedProduct.getBrand());
        assertEquals(createdProductList.get(0).getDescription(),fetchedProduct.getDescription());
        assertEquals(createdProductList.get(0).getClientSkuId(),fetchedProduct.getClientSkuId());
        assertEquals(createdProductList.get(0).getMrp(),fetchedProduct.getMrp());
    }

    @Test(expected = ApiException.class)
    public void getByInvalidId() throws ApiException {
        List<ProductForm> formList = new ArrayList<>();
        formList.add(createProductForm(RandomStringUtils.random(6),RandomStringUtils.random(6),
                String.valueOf(new Random().nextDouble()),RandomStringUtils.random(6),RandomStringUtils.random(6)));
        productDto.create(createClient().getId(),formList);
        try {
            productDto.get(new Random().nextLong());
        }
        catch (ApiException e){
            assertEquals("No such product exists.",e.getMessage());
            throw e;
        }
    }

    @Test
    public void updateProduct() throws ApiException {
        UserData client = createClient();
        List<ProductForm> formList = new ArrayList<>();
        formList.add(createProductForm(RandomStringUtils.random(6),RandomStringUtils.random(6),
                String.valueOf(new Random().nextDouble()),RandomStringUtils.random(6),RandomStringUtils.random(6)));
        List<ProductReturnData> createdProductList = productDto.create(client.getId(),formList);
        String newName = RandomStringUtils.random(6);
        String newDescription = RandomStringUtils.random(6);
        String newMrp = String.valueOf(new Random().nextDouble());
        String newBrand = RandomStringUtils.random(6);
        ProductForm newForm = new ProductForm().builder()
                        .name(newName).description(newDescription)
                        .brand(newBrand).mrp(newMrp)
                        .clientSkuId(createdProductList.get(0).getClientSkuId())
                        .build();
        ProductData updatedProduct = productDto.update(client.getName(),newForm);
        assertEquals(newName.trim().toLowerCase(),updatedProduct.getName());
        assertEquals(newBrand.trim().toLowerCase(),updatedProduct.getBrand());
        assertEquals(newDescription.trim().toLowerCase(),updatedProduct.getDescription());
        assertEquals(String.valueOf(Math.round(Double.parseDouble(newMrp) * 100.0) / 100.0),updatedProduct.getMrp());
    }

    @Test(expected = ApiException.class)
    public void updateInvalidProduct() throws ApiException {
        UserData client = createClient();
        List<ProductForm> formList = new ArrayList<>();
        formList.add(createProductForm(RandomStringUtils.random(6),RandomStringUtils.random(6),
                String.valueOf(new Random().nextDouble()),RandomStringUtils.random(6),RandomStringUtils.random(6)));
        List<ProductReturnData> createdProductList = productDto.create(client.getId(),formList);
        String newName = RandomStringUtils.random(6);
        String newDescription = RandomStringUtils.random(6);
        String newMrp = String.valueOf(new Random().nextDouble());
        String newBrand = RandomStringUtils.random(6);
        ProductForm newForm = new ProductForm().builder()
                .name(newName).description(newDescription)
                .brand(newBrand).mrp(newMrp)
                .clientSkuId(RandomStringUtils.random(6))
                .build();
        try{
            productDto.update(client.getName(),newForm);
        }
        catch(ApiException e){
            assertEquals("No such product exists.",e.getMessage());
            throw e;
        }
    }
    private ProductForm createProductForm(String name, String brand,String mrp,String description,String clientSku){
        return new ProductForm().builder()
                .name(name).brand(brand).mrp(mrp)
                .description(description).clientSkuId(clientSku)
                .build();
    }
    private UserData createClient() throws ApiException {
         return userDto.create ( new UserForm().builder()
                                    .name(RandomStringUtils.random(6))
                                    .type("CLIENT").build()
         );
    }


}
