package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.AcceptsDAO;
import model.dao.AcceptsDAOPostgres;
import model.dto.Accepts;
import utils.FormatAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collection;

@WebServlet("/accepts")
public class AcceptsRestAPI extends HttpServlet {
    AcceptsDAO dao = new AcceptsDAOPostgres();

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String info = req.getPathInfo();

        System.out.println(info);

        Collection<Accepts> l = dao.findAll();
        String jsonstring = objectMapper.writeValueAsString(l);
        out.print(jsonstring);
        return;
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        String data = new BufferedReader(new InputStreamReader(req.getInputStream())).readLine();

        ObjectMapper objectMapper = FormatAdapter.mapperFor(req);
        try {
            Accepts accepts = objectMapper.readValue(data, Accepts.class);
            System.out.println(accepts);

            if (!dao.add(accepts)) {
                res.sendError(HttpServletResponse.SC_CONFLICT);
                return;
            }
            PrintWriter out = res.getWriter();
            String jsonstring = objectMapper.writeValueAsString(accepts);
            out.print(jsonstring);
        } catch (Exception e) {
            System.out.println(e);
        }

        return;
    }

    public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        String info = req.getPathInfo();

        System.out.println(info);

        String[] splits = info.split("/");
        if (splits.length != 3) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String pointId = splits[1];
        String wasteTypeId = splits[2];
        boolean success = dao.delete(Integer.parseInt(pointId), Integer.parseInt(wasteTypeId));

        if (!success) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        res.sendError(HttpServletResponse.SC_OK);
        return ;
    }
}
