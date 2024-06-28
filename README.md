# E-Commerce Application

## Introduction

This is an E-Commerce Application that uses Java, Spring Boot, And MySQL

## Phase One: Categories Controller

### CategoriesController

The CategoriesController implements MySqlCategoryDao and calls CRUD method specifically for categories by using Spring Boot to handle HTTP requests.

### MySqlCategoryDao

The MySqlCategoryDao creates the functionality betwee the MySQL database  and Java to perform CRUD operations on product categories.
The MySqlCategoryDao uses the Category class as its model and is synchronous with the SQL database category variables.

<details>
<summary>Postman Requests for Phase One</summary>
<br>

POST: Login - as User
![POST - Login - New User](https://github.com/ceyhuncafer1/CapstoneThree_ECommerce/assets/70558570/734102dd-c3ed-4b9e-b1f9-5b9221817db1)

POST: Login - as Admin
![POST - Login - as admin](https://github.com/ceyhuncafer1/CapstoneThree_ECommerce/assets/70558570/feb3966c-a399-4bcb-966f-92e09908e77e)

GET: Get specific category

![GET - Get specific category](https://github.com/ceyhuncafer1/CapstoneThree_ECommerce/assets/70558570/3fd11e09-7f31-467f-acd2-acd72cf83c34)

POST: Add Category (not as admin)

![POST - Add category as not admin](https://github.com/ceyhuncafer1/CapstoneThree_ECommerce/assets/70558570/7fe28926-c6f0-4916-8c0c-64e3913dea15)

GET: Get all categories

![GET - Get All categories](https://github.com/ceyhuncafer1/CapstoneThree_ECommerce/assets/70558570/4ebafbfc-831d-4aeb-b291-a7ffaed50828)

POST: Add category as admin

![POST - Add Category as admin](https://github.com/ceyhuncafer1/CapstoneThree_ECommerce/assets/70558570/f096cee4-cc4a-465f-a760-1cf8047039a1)

GET: Get categories after adding a category

![GET - Get Categories after adding a category](https://github.com/ceyhuncafer1/CapstoneThree_ECommerce/assets/70558570/20e6127e-cf2c-4586-93df-c2b85103a831)

DELETE: Delete new category

![DELETE - Delete Category](https://github.com/ceyhuncafer1/CapstoneThree_ECommerce/assets/70558570/b5d0a800-e70f-4723-a3a2-e5ad5ae18a7c)

GET: New category ID shouldn't exist anymore

![Get the Category by ID after deleting category](https://github.com/ceyhuncafer1/CapstoneThree_ECommerce/assets/70558570/9f33f322-5496-4eef-a72c-022cdf692f9c)

</details>

## Phase Two: Fix Bugs

### Bug 1: ProductsController

The ProductsController had the wrong method call in its CRUD method calling, specifically the updateProduct method.

Original Code:

```
public void updateProduct(@PathVariable int id, @RequestBody Product product)
    {
        try
        {
            productDao.create(product);
        }
```

Fixed Code:

```
public void updateProduct(@PathVariable int id, @RequestBody Product product)
    {
        try
        {
            productDao.update(id, product);
        }
```

This is what caused the errors with duplication.

### Bug 2: MySqlProductDao search() method

Original Code:

```
String sql = "SELECT * FROM products " +
                "WHERE (category_id = ? OR ? = -1) " +
                "   AND (price <= ? OR ? = -1) " +
                "   AND (color = ? OR ? = '') ";

PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, categoryId);
            statement.setInt(2, categoryId);
            statement.setBigDecimal(3, minPrice);
            statement.setBigDecimal(4, minPrice);
            statement.setString(5, color);
            statement.setString(6, color);
```

Fixed Code:

```
String sql = "SELECT * FROM products " +
                "WHERE (category_id = ? OR ? = -1) " +
                "   AND (price >= ? OR ? = -1) " +
                "   AND (price <= ? OR ? = -1) " +
                "   AND (color = ? OR ? = '') ";

PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, categoryId);
            statement.setInt(2, categoryId);
            statement.setBigDecimal(3, minPrice);
            statement.setBigDecimal(4, minPrice);
            statement.setBigDecimal(5, maxPrice);
            statement.setBigDecimal(6, maxPrice);
            statement.setString(7, color);
            statement.setString(8, color);
```

The website did not properly display products based on the price searching because there is only one parameter in the original code. 
Meaning, there is no range. 

Here we can see the querystring paramaters do not match with the given syntax.

![image](https://github.com/ceyhuncafer1/CapstoneThree_ECommerce/assets/70558570/b8ff9ac0-6884-4622-9f7f-defca1c29799)

We must also fix this for the PreparedStatement:

By having setBigDecimal(3, minPrice) and setBigDecimal(5, maxPrice) we now have a logically working minimum and upper bound.

<details>
<summary>Postman Requests for Phase Two: Products</summary>
<br>

GET: Search Products

![Search Products with price filter](https://github.com/ceyhuncafer1/CapstoneThree_ECommerce/assets/70558570/c83cb84d-52c8-44f0-9e1e-ff67861f17f2)

GET: Specific Product

![Get specific product](https://github.com/ceyhuncafer1/CapstoneThree_ECommerce/assets/70558570/04839728-ab16-4d7f-b43c-c2aded25d520)

POST: Add Product (Postman)

![Add product (Postman)](https://github.com/ceyhuncafer1/CapstoneThree_ECommerce/assets/70558570/ac2ff141-b31f-4cd0-9a19-63e29028d1ae)

POST: Add Product (Website)

![Add product (Website)](https://github.com/ceyhuncafer1/CapstoneThree_ECommerce/assets/70558570/6e8ed807-77f5-4600-acb4-09caf2ce7d83)

PUT: Update Product (Postman)

![PUT - Update product (Postman)](https://github.com/ceyhuncafer1/CapstoneThree_ECommerce/assets/70558570/1ae1f6d9-fc5b-4a06-adfd-e58c7bb83fe9)


PUT: Update Product (Website)

![Update Product (Website)](https://github.com/ceyhuncafer1/CapstoneThree_ECommerce/assets/70558570/dd3d0908-a3ec-49c9-bb90-4dc9cceb7be6)

DELETE: New product

![Delete - new product](https://github.com/ceyhuncafer1/CapstoneThree_ECommerce/assets/70558570/ea19acb9-a388-4a5c-9409-a99fc2fe5024)

GET: Get all products afted Delete new product

![Get all products after delete](https://github.com/ceyhuncafer1/CapstoneThree_ECommerce/assets/70558570/3236eee3-f724-4ab7-84a1-c59d33fdc347)


</details>

