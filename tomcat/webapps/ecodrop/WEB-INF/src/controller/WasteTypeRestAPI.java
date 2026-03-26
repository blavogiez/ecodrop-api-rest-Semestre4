package controller;

import java.io.IOException;
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
import utils.RequestUtils;

@WebServlet("/waste-types/*")
public class WasteTypeRestAPI extends HttpServlet {

    WasteTypeDAO dao = new WasteTypeDAOPostgres();

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType(FormatAdapter.contentTypeFor(req));
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = FormatAdapter.mapperFor(req);
        String info = req.getPathInfo();

        System.out.println(info);

        if (info == null || info.equals("/")) {
            Collection<WasteType> lesWasteTypes = dao.findAll();
            out.print(objectMapper.writeValueAsString(lesWasteTypes));
            return;
        }
        String[] splits = info.split("/");
        if (splits.length != 2) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int id = RequestUtils.parseId(splits[1], res);
        if (id < 0) return;
        WasteType wasteType = dao.findById(id);
        if (wasteType == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        out.print(objectMapper.writeValueAsString(wasteType));
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType(FormatAdapter.contentTypeFor(req));
        ObjectMapper objectMapper = FormatAdapter.mapperFor(req);
        try {
            String data = RequestUtils.readBody(req);
            WasteType wasteType = objectMapper.readValue(data, WasteType.class);
            System.out.println(wasteType);

            if (!dao.add(wasteType)) {
                res.sendError(HttpServletResponse.SC_CONFLICT);
                return;
            }
            PrintWriter out = res.getWriter();
            out.print(objectMapper.writeValueAsString(wasteType));
            res.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            System.err.println("Could not add waste type to database : " + e.getMessage());
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType(FormatAdapter.contentTypeFor(req));
        String info = req.getPathInfo();

        System.out.println(info);

        String[] splits = info.split("/");
        if (splits.length != 2) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int id = RequestUtils.parseId(splits[1], res);
        if (id < 0) return;
        boolean success = dao.delete(id);

        if (!success) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        res.setStatus(HttpServletResponse.SC_OK);
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

        WasteType updatedWasteType = objectMapper.readValue(data, WasteType.class);

        WasteType wasteType = dao.update(id, updatedWasteType);

        if (wasteType == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        out.print(objectMapper.writeValueAsString(wasteType));
        res.setStatus(HttpServletResponse.SC_OK);
    }

}
