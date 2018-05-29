package ro.mindit.todo.dao;

import ro.mindit.todo.model.Todo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static ro.mindit.todo.util.Constants.*;

public class TodoDao {
    private int idSeq = 1;
    private List<Todo> todos;

    private Connection jdbcConnection;

    public TodoDao() {
        final Todo todo = new Todo() {{
            setId(idSeq++);
            setName("Todo 1");
            setOwner("Adi");
            setPriority("High");
        }};
        todos = new ArrayList<Todo>() {{
            add(todo);
        }};
    }

    public List<Todo> queryAll() {
        return todos;
    }

    public Todo getTodo(int id) {
        Todo result = null;
        for (Todo todo : todos) {
            if (todo.getId() == id) {
                result = todo;
                break;
            }
        }
        return result;
    }


    public void connect() throws SQLException {
        if (jdbcConnection == null || jdbcConnection.isClosed()) {
            try {
                Class.forName(jdbcDriver);
            } catch (ClassNotFoundException e) {
                throw new SQLException(e);
            }
            jdbcConnection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        }
    }

    public void disconnect() throws SQLException {
        if (jdbcConnection != null && !jdbcConnection.isClosed()) {
            jdbcConnection.close();
        }
    }

    public Todo findOne(int id) throws SQLException {
        Todo todo = null;
        String sql = "SELECT * FROM todo WHERE id = ?";

        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setInt(1, id);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String title = resultSet.getString("name");
            String author = resultSet.getString("owner");
            String price = resultSet.getString("priority");

            todo = new Todo(id, title, author, price);
        }

        resultSet.close();
        statement.close();

        return todo;
    }

    public List<Todo> findAll() throws SQLException {
        List<Todo> todoList = new ArrayList<Todo>();

        String sql = "SELECT * FROM todo";

        connect();

        Statement statement = jdbcConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String title = resultSet.getString("name");
            String author = resultSet.getString("owner");
            String price = resultSet.getString("priority");
            Todo todo = new Todo(id, title, author, price);
            todoList.add(todo);
        }

        resultSet.close();
        statement.close();

        disconnect();

        return todoList;
    }

    /**
     * Creates a new Todo
     * @param todo
     * @return the ToDo with the inserted id
     * @throws SQLException
     */
    public Todo saveNew(Todo todo) throws SQLException {

        String sql = "INSERT into TODO (name, owner, priority) VALUES (?, ?, ?)";

        connect();

        PreparedStatement pstmt = jdbcConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        pstmt.setString(1, todo.getName());
        pstmt.setString(2, todo.getOwner());
        pstmt.setString(3, todo.getPriority());

        pstmt.executeUpdate();

        ResultSet rs = pstmt.getGeneratedKeys();
        int last_id = -1;
        if(rs.next()) {
            last_id = rs.getInt(1);
        }


        rs.close();
        pstmt.close();

        disconnect();

        todo.setId(last_id);
        return todo;
    }


    /**
     * Updates an existing Todo
     * @param todo
     * @return the modified Todo
     * @throws SQLException
     */
    public Todo saveExisting(Todo todo) throws Exception {

        if (todo.getId() <= 0) {
            throw new Exception("The provided id must be greater than 0");
        }

        String sql = "UPDATE TODO set name=?, owner=?, priority=? WHERE id=?";

        connect();

        PreparedStatement pstmt = jdbcConnection.prepareStatement(sql);

        pstmt.setString(1, todo.getName());
        pstmt.setString(2, todo.getOwner());
        pstmt.setString(3, todo.getPriority());
        pstmt.setInt(4, todo.getId());

        int num_changed = pstmt.executeUpdate();

        if (num_changed == 0) {
            throw new Exception("No todo updated");
        }

        pstmt.close();

        disconnect();

        return todo;
    }

    /**
     * Deletes an existing Todo
     * @param id
     * @return the modified Todo
     * @throws SQLException
     */
    public int deleteExisting(int id) throws Exception {

        if (id <= 0) {
            throw new Exception("The provided id must be greater than 0");
        }

        String sql = "DELETE FROM TODO WHERE id=?";

        connect();

        PreparedStatement pstmt = jdbcConnection.prepareStatement(sql);

        pstmt.setInt(1, id);


        int num_changed = pstmt.executeUpdate();

        if (num_changed == 0) {
            throw new Exception("No todo deleted");
        }

        pstmt.close();

        disconnect();

        return id;
    }

}
