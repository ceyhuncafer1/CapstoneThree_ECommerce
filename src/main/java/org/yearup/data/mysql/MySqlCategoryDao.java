package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories(String name, String description) {
        // get all categories
        List<Category> categories = new ArrayList<>();

        String query = "SELECT * FROM categories WHERE name LIKE ? AND description LIKE ?";

        String nameToSearch = name == null ? "%" : name;
        String descriptionToSearch = description == null ? "%": description;

        try(Connection connection = getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nameToSearch);
            preparedStatement.setString(2, descriptionToSearch);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                Category category = mapRow(resultSet);
                categories.add(category);
            }
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
            throw new RuntimeException(sqlException);
        }
        return categories;
    }

    @Override
    public Category getById(int categoryId)
    {


        String query = "SELECT * FROM categories WHERE category_id = ?";

        try(Connection connection = getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, categoryId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                return mapRow(resultSet);
            }

        } catch(SQLException sql) {
            sql.printStackTrace();
            throw new RuntimeException(sql);
        }

        return null;


    }

    @Override
    public Category create(Category category)
    {
        // create a new category

        String query = "INSERT INTO categories(name, description) VALUES(?, ?)";

        try(Connection connection = getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, category.getName());
            preparedStatement.setString(2, category.getDescription());

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if(generatedKeys.next()) {
                    int categoryId = generatedKeys.getInt(1);

                    return getById(categoryId);
                }


            }

        } catch(SQLException sql) {
            sql.printStackTrace();
            throw new RuntimeException(sql);

        }
        return null;
    }

    @Override
    public void update(int categoryId, Category category)
    {
        // update category

        String query = "UPDATE categories SET name = ?, description = ? WHERE categoryId = ?";

        try(Connection connection = getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, category.getName());
            preparedStatement.setString(2, category.getDescription());

            preparedStatement.setInt(3, category.getCategoryId());

            preparedStatement.executeUpdate();

        } catch(SQLException sql) {
            sql.printStackTrace();
            throw new RuntimeException(sql);
        }

    }

    @Override
    public void delete(int categoryId)
    {
        // delete category
        String query = "DELETE FROM categories WHERE category_id = ?";

        try(Connection connection = getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, categoryId);

            preparedStatement.executeUpdate();

        } catch(SQLException sql) {
            sql.printStackTrace();
            throw new RuntimeException(sql);
        }

    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
