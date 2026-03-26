package controller;

import java.io.IOException;
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
import utils.RequestUtils;

@WebServlet("/users/*")
public class UsersRestAPI extends HttpServlet {

    UsersDAO dao = new UsersDAOPostgres();

    private static final int DEFAULT_LEADERBOARD_LIMIT=10;
    
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType(FormatAdapter.contentTypeFor(req));
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
    }

    public void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType(FormatAdapter.contentTypeFor(req));
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = FormatAdapter.mapperFor(req);
        String info = req.getPathInfo();

        System.out.println(info);

        String[] splits = info.split("/");
        if (splits.length != 2) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int id = RequestUtils.parseId(splits[1], res);
        if (id < 0) return;
        String data = RequestUtils.readBody(req);

        Users updatedUser = objectMapper.readValue(data, Users.class);

        Users user = dao.update(id, updatedUser);

        if (user == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        out.print(objectMapper.writeValueAsString(user));
        res.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(FormatAdapter.contentTypeFor(req));

        String info = req.getPathInfo();
        String[] splits = info == null ? new String[0] : info.split("/");
        if (splits.length != 2) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int id = RequestUtils.parseId(splits[1], resp);
        if (id < 0) return;

        try {
            String data = RequestUtils.readBody(req);

            // recup l'objet existant pour ne modifier que les champs fournis
            Users existing = dao.findById(id);
            if (existing == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            ObjectMapper mapper = FormatAdapter.mapperFor(req);
            Users updated = mapper.readerForUpdating(existing).readValue(data);

            if (dao.update(id, updated) == null) {
                resp.sendError(HttpServletResponse.SC_CONFLICT);
                return;
            }

            PrintWriter out = resp.getWriter();
            out.print(mapper.writeValueAsString(updated));
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            System.out.println("Could not update user : " + e.getMessage());
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
