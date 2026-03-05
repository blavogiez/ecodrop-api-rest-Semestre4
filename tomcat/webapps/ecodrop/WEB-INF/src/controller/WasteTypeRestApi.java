package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collection;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.WasteTypeDAO;
import model.dao.WasteTypeDAOPostgres;
import model.dto.WasteType;

@WebServlet("/waste-type/*")
public class WasteTypeRestApi extends HttpServlet {

    WasteTypeDAO dao = new WasteTypeDAOPostgres();

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String info = req.getPathInfo();

        System.out.println(info);

        if (info == null || info.equals("/")) {
            Collection<WasteType> l = dao.findAll();
            String jsonstring = objectMapper.writeValueAsString(l);
            out.print(jsonstring);
            return;
        }
        String[] splits = info.split("/");
        if (splits.length != 2) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String id = splits[1];
        WasteType e = dao.findById(Integer.parseInt(id));
        if (e == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        out.print(objectMapper.writeValueAsString(e));
        return;
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        String data = new BufferedReader(new InputStreamReader(req.getInputStream())).readLine();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            WasteType wasteType = objectMapper.readValue(data, wasteType.class);
            System.out.println(wasteType);

            if (!dao.add(wasteType)) {
                res.sendError(HttpServletResponse.SC_CONFLICT);
                return;
            }
            PrintWriter out = res.getWriter();
            String jsonstring = objectMapper.writeValueAsString(wasteType);
            out.print(jsonstring);
        } catch (Exception e) {
            System.out.println(e);
        }

        return;
    }

}
