/**
 *
 */
package ro.mindit.todo.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ro.mindit.todo.dao.TodoDao;
import ro.mindit.todo.model.Todo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

public class TodoResource extends HttpServlet {

    private TodoDao todoDao;

    @Override
    public void init() throws ServletException {
        todoDao = new TodoDao();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // set response content type
        response.setContentType("application/json");

//        String json = getTodoFromMemory(request);
		String json = getTodoFromDb(request);

        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // set response content type
        response.setContentType("application/json");

//        String json = getTodoFromMemory(request);


        String json;

        if (request.getParameter("delete") != null) {
            json = deleteTodoFromDb(request);
        } else if (request.getParameter("todoid") != null) {
            json = putTodoToDb(request);
        } else {
            json = postTodoToDb(request);
        }
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }






    @Override
    public void destroy() {
    }

    private String getTodoFromDb(HttpServletRequest request) throws JsonProcessingException {
        String json = "";
        ObjectMapper objectMapper = new ObjectMapper();

        String id = request.getQueryString();
        try {
            // Connect to the database
            // todoDao.connect();

            if (id != null) {
                id = id.substring(3);
                Todo todo = todoDao.findOne(Integer.parseInt(id));
                json = objectMapper.writeValueAsString(todo);
            } else {
                List<Todo> todos = todoDao.findAll();
                json = objectMapper.writeValueAsString(todos);
            }

            // Disconnect from the database
            // todoDao.disconnect();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return json;
    }

    private String postTodoToDb(HttpServletRequest request) throws JsonProcessingException {
        String json = "";
        ObjectMapper objectMapper = new ObjectMapper();


        try {
            // Connect to the database
            //todoDao.connect();

            Todo todo = new Todo();
            String name = request.getParameter("name");
            String owner = request.getParameter("owner");
            String priority = request.getParameter("priority");


            todo.setName(name);
            todo.setOwner(owner);
            todo.setPriority(priority);

            System.out.println("The todo will be " + todo);

            todoDao.saveNew(todo);
            json = objectMapper.writeValueAsString(todo);


            // Disconnect from the database
            //todoDao.disconnect();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return json;
    }


    private String putTodoToDb(HttpServletRequest request) throws JsonProcessingException {
        String json = "";
        ObjectMapper objectMapper = new ObjectMapper();


        try {
            // Connect to the database
            //todoDao.connect();

            Todo todo = new Todo();
            String idStr = request.getParameter("todoid");


            String name = request.getParameter("name");
            String owner = request.getParameter("owner");
            String priority = request.getParameter("priority");


            System.out.println("Id: " + idStr);
            System.out.println("Name: " + name);
            System.out.println("Owner: " + owner);
            System.out.println("Priority: " + priority);

            int id = Integer.parseInt(idStr);
            todo.setId(id);
            todo.setName(name);
            todo.setOwner(owner);
            todo.setPriority(priority);

            System.out.println("The todo will be " + todo);

            todoDao.saveExisting(todo);
            json = objectMapper.writeValueAsString(todo);


            // Disconnect from the database
            //todoDao.disconnect();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    private String deleteTodoFromDb(HttpServletRequest request) throws JsonProcessingException {
        String json = "";
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Connect to the database
            //todoDao.connect();
            Todo todo = new Todo();
            String idStr = request.getParameter("todoid");
            int id = Integer.parseInt(idStr);
            todo.setId(id);

            todoDao.deleteExisting(id);

            json = objectMapper.writeValueAsString(todo);

            // Disconnect from the database
            //todoDao.disconnect();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }



    private String getTodoFromMemory(HttpServletRequest request) throws JsonProcessingException {
        String json = "";
        ObjectMapper objectMapper = new ObjectMapper();

        String id = request.getQueryString();
        if (id != null) {
            Todo todo = todoDao.getTodo(1);
            json = objectMapper.writeValueAsString(todo);
        } else {
            List<Todo> todos = todoDao.queryAll();
            json = objectMapper.writeValueAsString(todos);
        }
        return json;
    }
}