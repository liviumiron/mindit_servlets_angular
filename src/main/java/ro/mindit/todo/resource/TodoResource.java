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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ro.mindit.todo.util.Utils;

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

        System.out.println("in post!!!!!");
        String json;
        Map<String,String> myMap = new HashMap<String, String>();
        myMap = Utils.getJSONFromRequest(request);
        System.out.println("The map returned is " + myMap.toString());



        if (myMap.containsKey("delete")) {
            System.out.println("In delete");
            json = deleteTodoFromDb(request, myMap);
        } else if (myMap.containsKey("id") && !myMap.get("id").equals("0")) {
            System.out.println("In put");
            json = putTodoToDb(request, myMap);
        } else {
            System.out.println("In post");
            json = postTodoToDb(request, myMap);
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

    private String postTodoToDb(HttpServletRequest request, Map<String, String> myMap) throws JsonProcessingException {
        String json = "";
        ObjectMapper objectMapper = new ObjectMapper();



        try {
            // Connect to the database
            //todoDao.connect();

            Todo todo = new Todo();
            String name = myMap.get("name");
            String owner = myMap.get("owner");
            String priority = myMap.get("priority");


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


    private String putTodoToDb(HttpServletRequest request, Map<String, String> myMap) throws JsonProcessingException {
        String json = "";
        ObjectMapper objectMapper = new ObjectMapper();



        try {
            // Connect to the database
            //todoDao.connect();

            Todo todo = new Todo();
            String idStr = myMap.get("id");


            String name = myMap.get("name");
            String owner = myMap.get("owner");
            String priority = myMap.get("priority");


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

    private String deleteTodoFromDb(HttpServletRequest request, Map<String, String> myMap) throws JsonProcessingException {
        String json = "";
        ObjectMapper objectMapper = new ObjectMapper();



        try {
            // Connect to the database
            //todoDao.connect();
            Todo todo = new Todo();
            String idStr = myMap.get("id");
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