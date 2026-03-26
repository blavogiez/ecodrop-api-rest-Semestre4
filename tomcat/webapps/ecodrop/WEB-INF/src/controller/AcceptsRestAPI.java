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
import model.dao.AcceptsDAO;
import model.dao.AcceptsDAOPostgres;
import model.dto.Accepts;
import utils.FormatAdapter;
import utils.RequestUtils;

@WebServlet("/accepts/*")
public class AcceptsRestAPI extends HttpServlet {
    AcceptsDAO dao = new AcceptsDAOPostgres();

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType(FormatAdapter.contentTypeFor(req));
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = FormatAdapter.mapperFor(req);
        String info = req.getPathInfo();

        System.out.println(info);

        if (info != null && !info.equals("/")) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Collection<Accepts> l = dao.findAll();
        out.print(objectMapper.writeValueAsString(l));
        res.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType(FormatAdapter.contentTypeFor(req));
        ObjectMapper objectMapper = FormatAdapter.mapperFor(req);
        try {
            String data = RequestUtils.readBody(req);
            Accepts accepts = objectMapper.readValue(data, Accepts.class);
            System.out.println(accepts);

            if (!dao.add(accepts)) {
                res.sendError(HttpServletResponse.SC_CONFLICT);
                return;
            }
            PrintWriter out = res.getWriter();
            out.print(objectMapper.writeValueAsString(accepts));
            res.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            System.out.println(e);
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType(FormatAdapter.contentTypeFor(req));
        String info = req.getPathInfo();

        System.out.println(info);

        String[] splits = info.split("/");
        if (splits.length != 3) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int pointId = RequestUtils.parseId(splits[1], res);
        if (pointId < 0) return;
        int wasteTypeId = RequestUtils.parseId(splits[2], res);
        if (wasteTypeId < 0) return;
        boolean success = dao.delete(pointId, wasteTypeId);

        if (!success) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        res.setStatus(HttpServletResponse.SC_OK);
    }
}
