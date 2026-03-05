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
import utils.FormatAdapter;

@WebServlet("/waste-types/*")
public class WasteTypeRestApi extends HttpServlet {

    WasteTypeDAO dao = new WasteTypeDAOPostgres();

    // GET /waste-types : Liste tous les types de déchets disponibles. Ce endpoint
    // doit supporter application/json et application/xml.
    // GET /waste-types/id : Détails d’un type spécifique. Renvoie 404 si l’ID
    // n’existe pa
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = FormatAdapter.mapperFor(req);
        String info = req.getPathInfo();

        System.out.println(info);

        if (info == null || info.equals("/")) {
            Collection<WasteType> lesWasteTypes = dao.findAll();
            String jsonstring = objectMapper.writeValueAsString(lesWasteTypes);
            out.print(jsonstring);
            return;
        }
        String[] splits = info.split("/");
        if (splits.length != 2) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String id = splits[1];
        WasteType wasteType = dao.findById(Integer.parseInt(id));
        if (wasteType == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        out.print(objectMapper.writeValueAsString(wasteType));
        res.sendError(HttpServletResponse.SC_OK);
        return;
    }

    // POST /waste-types : Ajoute un nouveau type.
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        String data = new BufferedReader(new InputStreamReader(req.getInputStream())).readLine();

        ObjectMapper objectMapper = FormatAdapter.mapperFor(req);
        try {
            WasteType wasteType = objectMapper.readValue(data, WasteType.class);
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

        res.sendError(HttpServletResponse.SC_OK);
        return;
    }

    public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        String info = req.getPathInfo();

        System.out.println(info);

        String[] splits = info.split("/");
        if (splits.length != 2) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String id = splits[1];
        boolean success = dao.delete(Integer.parseInt(id));

        if (!success) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        res.sendError(HttpServletResponse.SC_OK);
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

        WasteType updatedWasteType = objectMapper.readValue(data, WasteType.class);

        WasteType wasteType = dao.update(Integer.parseInt(id), updatedWasteType);

        if (wasteType == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        out.print(objectMapper.writeValueAsString(wasteType));
        res.sendError(HttpServletResponse.SC_OK);
        return;
    }

}
