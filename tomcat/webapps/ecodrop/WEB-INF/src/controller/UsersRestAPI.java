package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.UsersDAO;
import model.dao.UsersDAOPostgres;
import model.dto.Users;
import utils.FormatAdapter;

@WebServlet("/users/*")
public class UsersRestAPI extends HttpServlet {

    UsersDAO dao = new UsersDAOPostgres();

    private static final int DEFAULT_LEADERBOARD_LIMIT=10;
    
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = FormatAdapter.mapperFor(req);
        String info = req.getPathInfo();

        System.out.println(info);

        if (info == null || info.equals("/")) {
            Collection<Users> users = dao.findAll();
            String jsonstring = objectMapper.writeValueAsString(users);
            out.print(jsonstring);
            return;
        }
        String[] splits = info.split("/");
        if (splits.length != 2) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String choice = splits[1];

        if(choice.equals("leaderboard")) {
            List<Users> theTenBestRecyclers = dao.findArgumentTopRecyclers(DEFAULT_LEADERBOARD_LIMIT);

            String jsonstring = objectMapper.writeValueAsString(theTenBestRecyclers);
            out.print(jsonstring);
            res.setStatus(HttpServletResponse.SC_OK);
            return ;
        }
        res.sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
    }

    public void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = FormatAdapter.mapperFor(req);
        String info = req.getPathInfo();

        System.out.println(info);

        String[] splits = info.split("/");
        if (splits.length != 2) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String id = splits[1];
        // Lecture du corps de la requête (même principe que doPost)
        String data = new BufferedReader(new InputStreamReader(req.getInputStream())).readLine();

        Users updatedWasteType = objectMapper.readValue(data, Users.class);

        Users user = dao.update(Integer.parseInt(id), updatedWasteType);

        if (user == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        out.print(objectMapper.writeValueAsString(user));
        res.sendError(HttpServletResponse.SC_OK);
        return;
    }

}
